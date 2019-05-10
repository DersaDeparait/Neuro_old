import java.util.LinkedList;
import java.util.Scanner;

/**
 * Вся нейромереажа разом
 */
class Network {
    //region static
    private static double findMistakeMSE(double[] real, double[] ideal){
        if (real != null && ideal != null && real.length>0 && ideal.length > 0) {
            double result = 0;
            for (int i = 0; i < real.length && i < ideal.length; i++) {
                result += (ideal[i] - real[i])*(ideal[i] - real[i]);
            }
            result /= Math.min(real.length, ideal.length);
            return result;
        }
        return Double.NaN;
    }
    private static double findMistakeRootMSE(double[] real, double[] ideal){
        if (real != null && ideal != null && real.length>0 && ideal.length > 0) {
            double result = 0;
            for (int i = 0; i < real.length && i < ideal.length; i++) {
                result += (ideal[i] - real[i])*(ideal[i] - real[i]);
            }
            result /= Math.min(real.length, ideal.length);
            result = Math.sqrt(result);
            return result;
        }
        return Double.NaN;
    }
    private static double findMistake(double[] real, double[] ideal){
        return findMistakeMSE(real, ideal);
    }
    //endregion

    //region fields
    private NetworkData networkData;

    private LinkedList<Axon> axons; // Всі аксони
    private LinkedList<Neuron> neurons; // Всі нейрони

    private Scanner scanner; // Сканер для отримання даних із вводу
    //endregion

    //region construct
    public Network() {
        networkData = new NetworkData();

        neurons = new LinkedList<>();
        axons = new LinkedList<>();

        scanner = new Scanner(System.in);

        makeLayers(networkData.getLayers());
    }

    /**
     * Створити шари, які будуть звязані між собою по порядку
     * @param numberOfNeuronInLayer кількість нейронів на кожному слої
     */
    private void makeLayers(int[] numberOfNeuronInLayer){
        if (numberOfNeuronInLayer != null && numberOfNeuronInLayer.length > 1 && numberOfNeuronInLayer[0] > 0) {
            int[] lastNumberOfNeuronsMassive = new int[numberOfNeuronInLayer.length];
            lastNumberOfNeuronsMassive[0] = neurons.size();

            for (int i = lastNumberOfNeuronsMassive[0]; i < numberOfNeuronInLayer[0] + lastNumberOfNeuronsMassive[0]; i++) {
                neurons.add(new Neuron("t0-n" + (i - lastNumberOfNeuronsMassive[0])));
            }

            for (int i = 1; i < numberOfNeuronInLayer.length; i++) {
                if (numberOfNeuronInLayer[i] > 0) {
                    lastNumberOfNeuronsMassive[i] = neurons.size();
                    for (int j = lastNumberOfNeuronsMassive[i]; j < lastNumberOfNeuronsMassive[i] + numberOfNeuronInLayer[i]; j++) {
                        neurons.add(new Neuron("t"+ i +"-n" + (j - lastNumberOfNeuronsMassive[i])));
                    }
                    layerCompound(neurons,
                            lastNumberOfNeuronsMassive[i-1], lastNumberOfNeuronsMassive[i],
                            lastNumberOfNeuronsMassive[i], lastNumberOfNeuronsMassive[i] + numberOfNeuronInLayer[i]);
                } else
                    break;
            }
        }
    }
    /**
     * Створити звязки між двома групами нейронів. Кожен нейрон з першої групи звязаний із нейронами із другої
     * @param neurons масив нейронів
     * @param firstMassiveFrom перший номер масива першої групи
     * @param firstMassiveTo останній номер масива першої групи(цей номер не включається)
     * @param secondMassiveFrom перший номер масива другої групи
     * @param secondMassiveTo останній номер масива другої групи(цей номер не включається)
     */
    private void layerCompound(LinkedList<Neuron> neurons, int firstMassiveFrom, int firstMassiveTo, int secondMassiveFrom, int secondMassiveTo){
        if (neurons != null
                && firstMassiveFrom > -1 && firstMassiveTo > -1
                && secondMassiveFrom > -1 && secondMassiveTo > -1
                && firstMassiveTo > firstMassiveFrom && secondMassiveTo > secondMassiveFrom
                && neurons.size() >= firstMassiveTo && neurons.size() >= secondMassiveTo
        ){
            for (int i = firstMassiveFrom; i < firstMassiveTo; i++) {
                for (int j = secondMassiveFrom; j < secondMassiveTo; j++) {
                    if (neurons.get(i) != null && neurons.get(j) != null)
                        compound(neurons.get(i), neurons.get(j));
                }
            }
        }
    }
    /**
     * Створити звязки між двома групами нейронів. Кожен нейрон з першої групи звязаний із нейронами із другої
     * @param parentLayer масив першої групи
     * @param childLayer масив другої групи
     */
    private void layerCompound(Neuron[] parentLayer, Neuron[] childLayer) {
        if (parentLayer != null && childLayer != null)
            for (int i = 0; i < parentLayer.length; i++) {
                for (int j = 0; j < childLayer.length; j++) {
                    if (parentLayer[i] != null && childLayer[j] != null)
                        compound(parentLayer[i], childLayer[j]);
                }
            }
    }
    /**
     * Зєднати два нейрони аксоном
     * @param parent нейрон батько
     * @param child нейрон наслідник
     */
    private void compound(Neuron parent, Neuron child){
        Axon axon = new Axon();
        axon.setParent(parent);
        axon.setChild(child);
        axons.add(axon);

        parent.addAxonOutput(axon);
        child.addAxonInput(axon);
    }
    //endregion

    //region update
    public void update(){
        trainingFull();
        test();
    }
    private void trainingFull(){
        for (; networkData.isEndOfEpoch(); networkData.addOneEpoch()){
            System.out.println("\n---------------------------------------  "
                    + networkData.getEpoch() + "/"
                    + networkData.getMaxEpoch() + "  --------------------------------------------"
            );
            trainingOne();
        }
        networkData.resetEpoch();
    }
    private void test() {
        while (cheekDataFromUser()){
            double[] userData = new double[networkData.getNumberOfFirstLayer()];
            for (int i = 0; i < networkData.getNumberOfFirstLayer(); i++) {
                System.out.print("\nData " + (i+1) + "/" + networkData.getNumberOfFirstLayer() + " : ");
                userData[i] = scanner.nextDouble();
                System.out.print("You wrote: " + userData[i]);
                System.out.print("\n");
                scanner.reset();
            }
            networkData.setNewInputData(userData);
            startImpulse(userData);
            runOneTime();
        }
    }
    private boolean cheekDataFromUser(){
        System.out.print("\nDo you want to test?:\n");
        String inputData = scanner.next();
        scanner.reset();

        if (inputData.matches ("Y(.*)") || inputData.matches("y(.*)")){
            System.out.print("\nYour answer: " + "YES");
            return true;
        }
        else {
            System.out.print("\nYour answer: " + "NO");
            return false;
        }
    }
    private void trainingOne(){
        for (;networkData.isEndOfTrainSet(); networkData.addOneExample()) {
            refreshExample();
            startImpulse(networkData.getInputData());
            runOneTime();
        }
        resetExample();
    }
    private void refreshExample() { networkData.makeNewSetData(); }
    private void startImpulse(double[] inputData) {
        if (inputData != null)
            for (int i = 0; i < inputData.length || i < networkData.getNumberOfFirstLayer(); i++) {
                neurons.get(i).makeImpulse(inputData[i]);
            }
    }
    private void runOneTime() {
        print();

        do { oneImpulse(); }
        while(checkActivity());

        setResult();
        beginReverseMove();

        do { oneImpulse(); }
        while (checkActivity());

        clearData();
    }
    private void setResult(){
        writeData();
        printData();
    }
    private void beginReverseMove(){
        getTotalMistake();
        printTotalMistake();
        startReverseMove();
    }
    private void getTotalMistake(){
        networkData.setMistake(findMistake(networkData.getOutputData(), networkData.getIdealResult()));
    }
    private void printTotalMistake(){
        System.out.printf("\nMistake: %5f", networkData.getMistake());
    }
    private void startReverseMove(){
        for (int i = 0; i < networkData.getOutputDataLength(); i++) {
            neurons.get(neurons.size() - networkData.getOutputDataLength() + i)
                    .setBackwardFirst(networkData.getIdealResult(i));
        }
    }

    private void writeData(){
        for (int i = 0; i < networkData.getOutputDataLength(); i++) {
            networkData.setInternalOutputData(i,
                    neurons.get(neurons.size() - networkData.getOutputDataLength() + i).getDataOutput());
        }
    }
    /**
     * Вивести результат
     */
    private void printData() {

        System.out.print("\nInputData ");
        if (networkData.getInputData() != null)
            for (int i = 0; i < networkData.getInputDataLength(); i++) {
                System.out.printf("(%2d: %-5f)",
                        i, networkData.getInputData(i));
            }
        System.out.print("\n\nOutput       Real      Ideal");
        if (networkData.getOutputData() != null)
            for (int i = 0; i < networkData.getOutputDataLength(); i++) {
                System.out.printf("\nOutput %2d:   %-5f  %5f",
                        i, networkData.getOutputData(i), networkData.getIdealResult(i));
            }
    }
    private void clearData(){
        for (int i = 0; i < neurons.size(); i++) {
            neurons.get(i).clearData();
        }
        System.out.print("\n");
        for (int i = 0; i < axons.size(); i++) {
            axons.get(i).clearData();
        }
    }

    private void resetExample() { networkData.resetExample(); }

    /**
     * Перевірити чи є хочаб один елемент активним
     * @return true якщо хочаб один активний
     */
    private boolean checkActivity(){
        for (int i = 0; i < axons.size(); i++) {
            if (axons.get(i).isWork())
                return true;
        }
        for (int i = 0; i < neurons.size(); i++) {
            if (neurons.get(i).isWork())
                return true;
        }
        return false;
    }
    /**
     * Обновити все один раз
     */
    private void oneImpulse(){
        if (axons != null)
            for (int i = 0; i < axons.size(); i++) {
                axons.get(i).update();
        }
        System.out.println();
        if (neurons != null)
            for (int i = 0; i < neurons.size(); i++) {
            neurons.get(i).update();
        }
        System.out.println();
    }
    //endregion

    /**
     * Вивести дані про поточний стан
     */
    public void print() {
        System.out.printf("\nEpoch: %5d/%-5d, TrainingSet: %5d/%-5d\n",
                networkData.getEpoch() + 1, networkData.getMaxEpoch(), networkData.getTrainingExample()+1, networkData.getTrainingSet());
    }
}
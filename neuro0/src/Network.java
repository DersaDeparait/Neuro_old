import java.util.LinkedList;

/**
 * Вся нейромереажа разом
 */
public class Network {
    //region fields
    int numberOfTry = 3; // Кількість закидувань даних в нейромережу
    int currentTry = 0; // Поточний номер закидування
    int cycleCounter = 0; // Лічильник циклів

    LinkedList<Axon> axons; // Всі аксони
    LinkedList<Neuron> neurons; // Всі нейрони
    //endregion

    //region construct
    public Network() {
        neurons = new LinkedList<>();
        axons = new LinkedList<>();

        makeLayers(new int[]{4, 2, 6, 2, 1, 2});

    }
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
    private void layerCompound(Neuron[] parentLayer, Neuron[] childLayer) {
        if (parentLayer != null && childLayer != null)
            for (int i = 0; i < parentLayer.length; i++) {
                for (int j = 0; j < childLayer.length; j++) {
                    if (parentLayer[i] != null && childLayer[j] != null)
                        compound(parentLayer[i], childLayer[j]);
                }
            }
    }
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
        for (currentTry = 0; currentTry < numberOfTry; currentTry++){
            System.out.println("\n---------------------------------------------  " + currentTry + "  --------------------------------------------");
            startImpulse();
            runOneTime();
        }
    }
    private void startImpulse(){
        neurons.get(0).makeImpulse(1);
    }
    private void runOneTime() {
        cycleCounter = 0; // Лічильник циклів
        print();
        while(checkActivity()){
            oneImpulse();
            cycleCounter++;
            print();
        }
    }
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
        if (neurons != null)
            for (int i = 0; i < neurons.size(); i++) {
            neurons.get(i).update();
        }
    }
    //endregion

    public void print(){
        System.out.printf("\nTry: %5d/%-5d, Cycle%-5d\n", currentTry, numberOfTry, cycleCounter);
        for (int i = 0; i < axons.size(); i++) {
            axons.get(i).print();
        }
        System.out.print("\n");
        for (int i = 0; i < neurons.size(); i++) {
            neurons.get(i).print();
        }
    }
}
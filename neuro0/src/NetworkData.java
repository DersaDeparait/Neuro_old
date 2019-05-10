import java.util.LinkedList;

public class NetworkData {
    private int[] layers; // Кількість слоїв

    private int maxEpoch; // Кількість епох
    private int epoch; // Поточна епоха
    private int trainingSet; // Тренувальний сет
    private int trainingExample; // Поточний тренувальний номер

    private double[][] allInputData; //
    private double[] inputData; // Вхідні дані
    private double[] outputData; // Вихідні дані
    private double[] idealResult; // Ідеальні дані результату

    private LinkedList<Double> mistake;

    public NetworkData(){
        architecture();
        generateTraining();
        generateData();
        generateResultData();
    }
    private void architecture(){
        layers = new int[]{ 1, 1 };
    }
    private void generateTraining(){
        maxEpoch = 40;
        epoch = 0;
        trainingSet = 10;
        trainingExample = 0;
    }
    private void generateData(){
        allInputData = new double[trainingSet][getNumberOfFirstLayer()];
        for (int i = 0; i < allInputData.length; i++) {
            allInputData[i][0] = i/10d;
            //allInputData[i][1] = i/2;
        }

        inputData = new double[getNumberOfFirstLayer()];
        outputData = new double[layers[layers.length - 1]];
        idealResult = new double[layers[layers.length - 1]];
    }
    private void generateResultData(){
        mistake = new LinkedList<>();
    }


    public int[] getLayers() { return layers; }
    public int getNumberOfFirstLayer(){
        if (layers!= null)
            return layers[0];
        else
            return 0;
    }
    public int getNumberOfLastLayer() {
        if (layers != null)
            return layers[layers.length - 1];
        else
            return 0;
    }

    public void makeNewSetData(){
        setInternalInputData();
        setInternalOutputData();
        setInternalIdealData();
    }
    private void setInternalInputData(){ inputData = allInputData[trainingExample]; }
    private void setInternalOutputData() {
        for (int i = 0; i < outputData.length; i++) {
            outputData[i] = 0;
        }
    }
    public void setInternalOutputData(int number, double data){
        if (number>=0 && outputData != null && number < outputData.length){
            outputData[number] = data;
        }
    }
    public void setNewInputData(double[] inputData) {
        this.inputData = inputData;
        setInternalIdealData();
    }
    private void setInternalIdealData() {
        idealResult[0] = inputData[0] > 0.5 ? 1 : 0;
    }

    public double getMistake() {
        if (mistake.size() > 0)
            return mistake.getLast();
        else
            return Double.NaN;
    }
    public void setMistake(double mistake) {
        this.mistake.add(mistake);
    }

    public double[] getInputData() { return inputData; }
    public double getInputData(int number) {
        if (number<inputData.length)
            return inputData[number];
        else
            return Double.NaN;
    }
    public int getInputDataLength(){
        if (inputData != null)
            return inputData.length;
        else
            return -1;
    }
    public double[] getOutputData() { return outputData; }
    public double getOutputData(int number) {
        if (number<outputData.length)
            return outputData[number];
        else
            return Double.NaN;
    }
    public int getOutputDataLength(){
        if (outputData != null)
            return outputData.length;
        else
            return -1;
    }
    public double[] getIdealResult() { return idealResult; }
    public double getIdealResult(int number) {
        if (number<idealResult.length)
            return idealResult[number];
        else
            return Double.NaN;
    }
    public int getIdealDataLength(){
        if (idealResult != null)
            return idealResult.length;
        else
            return -1;
    }

    public int getMaxEpoch() { return maxEpoch; }
    public int getEpoch() { return epoch; }
    public int getTrainingSet() { return trainingSet; }
    public int getTrainingExample() { return trainingExample; }

    public void addOneEpoch(){ epoch++; }
    public void addOneExample() { trainingExample++; }
    public boolean isEndOfEpoch(){ return epoch < maxEpoch; }
    public boolean isEndOfTrainSet(){ return trainingExample < trainingSet; }
    public void resetEpoch(){epoch = 0;}
    public void resetExample(){ trainingExample = 0;}
}

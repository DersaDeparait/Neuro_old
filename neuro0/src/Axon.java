public class Axon extends ActiveElement {
    public static final double EPSILON = 3.0; // Швидкість вивчення
    public static final double ALPHA = 0.1; // Момент вивчення

    static int internalNumber = -1;
    static int giveName(){
        internalNumber++;
        return internalNumber;
    }

    //region fields
    String name; // Назва

    Neuron parent; // Батьківський нейрон
    Neuron child; // Нейрон наслідник

    double weight; // Поточна вага
    double inputData; // Вхідні дані
    double outputData; // Вихідні дані

    double grad; // Градієнт
    double deltaWeight; // Зміна ваги нейрона
    double deltaWeightLast; // Минула зміна ваги
    //endregion

    public Axon(){
        name = "A" + giveName();
        parent = null;
        child = null;
        weight = 0.5;

        grad = Double.NaN;
        deltaWeight = Double.NaN;
        deltaWeightLast = 0;
    }

    //region get/set
    public String getName() { return name; }
    public Neuron getParent() { return parent; }
    public Neuron getChild() { return child; }
    public double getWeight() { return weight; }
    public double getInputData() { return inputData; }
    public double getOutputData() { return outputData; }

    public void setName(String name) { this.name = name; }
    public void setParent(Neuron parent) { this.parent = parent; }
    public void setChild(Neuron child) { this.child = child; }
    public void setWeight(double weight) { this.weight = weight; }
    public void setInputData(double inputData) { this.inputData = inputData; }
    public void setOutputData(double outputData) { this.outputData = outputData; }
    //endregion

    @Override
    protected void updateActive() {
        child.getDataFromAxon(this, outputData); // Надіслати дані на нейрон наслідник
        setPassive();
    }

    /**
     * Отримати дані із нейрона
     * @param data
     */
    public void getDataFromParent(double data){
        this.inputData = data;
        this.outputData = inputData * weight;
        setActive();
    }

    public void calculateNewWeight(double fatherOutput, double childDelta){
        calculateGrad(fatherOutput, childDelta);
        calculateDeltaWeight();
        printShortData();
        changeWeight();
        changeDeltaWeightLast();
    }
    private void calculateGrad(double fatherOutput, double childDelta){
        grad = fatherOutput * childDelta;
    }
    private void calculateDeltaWeight(){
        deltaWeight = EPSILON * grad + ALPHA * deltaWeightLast;
    }
    private void printShortData(){
        System.out.printf("\nChanges %9s(%.10s : %.10s)  Weight:%9f  deltaWei:%9f  lastWei:%9f  newWei:%9f  grab:%9f",
                name, parent.getName(), child.getName(), weight, deltaWeight,   deltaWeightLast, (weight + deltaWeight), grad);
    }
    private void changeWeight(){
        weight += deltaWeight;
    }
    private void changeDeltaWeightLast(){
        deltaWeightLast = deltaWeight;
    }

    @Override
    public void printActive(){
        System.out.printf("\n%9s(%.10s : %.10s) dI:%9f  Weight:%9f  dO:%9f ",
                name, parent.getName(), child.getName(), inputData, weight, outputData);
    }

    public void clearData(){
        inputData = Double.NaN;
        outputData = Double.NaN;
        grad = Double.NaN;
        deltaWeight = Double.NaN;
    }
}
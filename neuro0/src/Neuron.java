import java.util.LinkedList;

public class Neuron extends ActiveElement {
    //region static
    static double defaultValueOfData = -100; // Початкове значення всіх даних із аксонів
    static double findOutputFromInput(double input) { return findNormalSigmoid(input); }
    static double findNormalSigmoid(double input) { return 1/(1 + Math.exp(-input)); }
    static double findNormalLinear(double input) { return input; }
    static double findNormalHipper(double input) { return (Math.exp(2 * input) - 1)/(Math.exp(2 * input) + 1); }

    static double findSigmoidDerivative(double out){ return findSigmoidDerivativeSigmoid(out); }
    static double findSigmoidDerivativeSigmoid(double out){ return (1-out) * out; }
    static double findSigmoidDerivativeTangh(double out){ return 1-out * out; }
    //endregion

    //region fields
    private String name; // Назва нейрона

    private double dataInput; // Дані входу
    private double dataOutput; // Дані виходу
    private double dataIdeal; // Ідеальні дані які мають бути
    private double offset; // Нейрон зміщення

    private double delta;

    private LinkedList<Double> axonInputData; // Вхідні дані із відповідних аксонів
    private LinkedList<Boolean> isNewDataFromAxon; // Чи є поточні дані з аксона новими
    private LinkedList<Axon> axonInput; // Вхідні аксони

    private LinkedList<Axon> axonOutput; // Вихідні аксони
    //endregion

    //region construct
    public Neuron(String name){
        this();
        this.name = name;
    }
    public Neuron(){
        name = "";

        dataInput = 0;
        dataOutput = 0;
        offset = 0;

        axonInputData =  new LinkedList<>();
        isNewDataFromAxon = new LinkedList<>();
        axonInput = new LinkedList<>();
        axonOutput = new LinkedList<>();
    }
    //endregion

    //region add/get/set
    public void addAxonInput(Axon axon){ axonInput.add(axon); axonInputData.add(defaultValueOfData); isNewDataFromAxon.add(false); }
    public void addAxonOutput(Axon axon){ axonOutput.add(axon); }

    public String getName() { return name; }
    public LinkedList<Axon> getAxonInput() { return axonInput; }
    public LinkedList<Axon> getAxonOutput() { return axonOutput; }
    public double getDataInput() { return dataInput; }
    public double getDataOutput() { return dataOutput; }

    public void setDataInput(double dataInput) { this.dataInput = dataInput; }
    public void setDataOutput(double dataOutput) { this.dataOutput = dataOutput; }
    //endregion

    //region update
    @Override
    protected void updateActive() {
        calculateSum(); // Вирахувати суму із всіх аксонів
        activationFunction(); // Вирахувати значення після підстановки в функції
        sendData(); // Надіслати дані на нейрони
        setPassive(); // Зробити пасивним нейрон
    }
    protected void updateFirst(){
        sendData();
        setPassive();
    }
    protected void calculateSum(){
        dataInput = 0;
        for (int i = 0; i < axonInputData.size(); i++) {
            if (isNewDataFromAxon.get(i)) {
                dataInput += axonInputData.get(i);
                isNewDataFromAxon.set(i, false);
            }
        }
        dataInput += offset;
    }
    protected void activationFunction() {
        dataOutput = findOutputFromInput(dataInput);
    }
    protected void sendData() {
        for (int i = 0; i < axonOutput.size(); i++) {
            axonOutput.get(i).getDataFromParent(dataOutput);
        }
    }

    protected void updateBackwardFirst(){
        findDelta0();
        sendDataBack();
        setPassive();
    }
    protected void findDelta0(){
         delta = (dataIdeal - dataOutput) * findSigmoidDerivative(dataOutput);
    }
    private void sendDataBack(){
        for (int i = 0; i < axonInput.size(); i++) {
            axonInput.get(i).getParent().setBackward();
        }
    }

    protected void updateBackward(){
        findDelta();
        findGradForAxon();
        setPassive();
    }
    protected void findDelta(){
        double wei = 0;
        for (int i = 0; i < axonOutput.size(); i++) {
            wei += axonOutput.get(i).weight * axonOutput.get(i).getChild().delta;
        }
        delta = findSigmoidDerivative(dataOutput) * wei;
    }
    protected void findGradForAxon(){
        for (int i = 0; i < axonOutput.size(); i++) {
            axonOutput.get(i).calculateNewWeight(dataOutput, axonOutput.get(i).getChild().delta);
        }
    }

    @Override
    public void setFirst() { state = State.FIRST; }
    public void setBackwardFirst(double dataIdeal){ state = State.BACKWARD_FIRST; this.dataIdeal = dataIdeal; }

    //endregion

    public void makeImpulse(double data){
        dataInput = dataOutput = data;
        setFirst();
    }

    public void getDataFromAxon(Axon axon, double data){
        for (int i = 0; i < axonInput.size(); i++) {
            if (axonInput.get(i) == axon) {
                this.axonInputData.set(i, data);
                this.isNewDataFromAxon.set(i, true);
                setActive();
                break;
            }
        }
    }

    @Override
    public void printActive(){
        System.out.printf("\n\t\t\t   %9s dI:%9f  offset:%9f  dO:%9f", name, dataInput, offset, dataOutput);
    }
    @Override
    public void printBackward(){
        System.out.printf("\n\t\t\t   %9s dI:%9f  offset:%9f  dO:%9f  delta:%9f", name, dataInput, offset, dataOutput, delta);
    }

    public void clearData(){
        dataInput = Double.NaN;
        dataOutput = Double.NaN;
        dataIdeal = Double.NaN;
        delta = Double.NaN;
    }
}
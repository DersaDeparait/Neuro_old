import java.util.LinkedList;

public class Neuron extends ActiveElement {
    //region static
    double defaultValueOfData = -100; // Початкове значення всіх даних із аксонів
    static double findOutputFromInput(double input) { return findNormalSigmoid(input); }
    static double findNormalSigmoid(double input) { return 1/(1- Math.exp(-input)); }
    static double findNormalLinear(double input) { return input; }
    static double findNormalHipper(double input) { return (Math.exp(2*input)-1)/(Math.exp(2*input)+1); }
    //endregion

    //region fields
    private String name; // Назва нейрона

    private double dataInput; // Дані входу
    private double dataOutput; // Дані виходу
    private double offset; // Нейрон зміщення

    private LinkedList<Double> axonInputData; // Вхідні дані із відповідних аксонів
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
        axonInput = new LinkedList<>();
        axonOutput = new LinkedList<>();
    }
    //endregion

    //region add/get/set
    public void addAxonInput(Axon axon){ axonInput.add(axon); axonInputData.add(defaultValueOfData); }
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
        System.out.println(name); // FIXME: 4/8/2019 change
    }
    protected void calculateSum(){
        dataInput = 0;
        for (int i = 0; i < axonInputData.size(); i++) {
            dataInput += axonInputData.get(i);
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
    //endregion

    public void makeImpulse(double data){
        dataInput = dataOutput = data;
        setActive();
    }

    public void getDataFromAxon(Axon axon, double data){
        for (int i = 0; i < axonInput.size(); i++) {
            if (axonInput.get(i) == axon) {
                this.axonInputData.set(i, data);
                setActive();
                break;
            }
        }
    }

}
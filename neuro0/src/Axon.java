public class Axon extends ActiveElement {
    //region fields
    String name; // Назва

    Neuron parent; // Батьківський нейрон
    Neuron child; // Нейрон наслідник

    double weight; // Поточна вага
    double inputData; // Вхідні дані
    double outputData; // Вихідні дані
    //endregion

    public Axon(){
        parent = null;
        child = null;
        weight = 0.5;
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
        System.out.println(parent.getName() + " " + child.getName()); // FIXME: 4/8/2019
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
}
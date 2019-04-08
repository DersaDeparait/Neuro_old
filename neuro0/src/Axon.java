public class Axon extends ActiveElement {
    String name;

    Neuron parent;
    Neuron child;

    double weight;
    double inputData;
    double outputData;

    public Axon(){
        parent = null;
        child = null;
        weight = 0.5;
    }

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

    public void getDataFromParent(double data){
        this.inputData = data;
        this.outputData = inputData * weight;
        setActive();
    }

    @Override
    protected void updateActive() {
        child.getDataFromAxon(this, outputData);
        setPassive();
        System.out.println(parent.getName() + " " + child.getName()); // FIXME: 4/8/2019
    }
}
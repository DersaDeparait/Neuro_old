import java.util.LinkedList;

public class Network {
    int numberOfTry = 1;

    LinkedList<Axon> axons;
    LinkedList<Neuron> neurons;

    public Network() {
        neurons = new LinkedList<>();
        axons = new LinkedList<>();
        neurons.add(new Neuron("First"));
        neurons.add(new Neuron("Second"));
        neurons.add(new Neuron("Third"));
        neurons.add(new Neuron("Last"));

        compound(neurons.get(0), neurons.get(1));
        compound(neurons.get(1), neurons.get(2));
        compound(neurons.get(2), neurons.get(3));

        neurons.get(0).makeImpulse(1);
    }
    private void compound(Neuron parent, Neuron child){
        Axon axon = new Axon();
        axon.setParent(parent);
        axon.setChild(child);
        axons.add(axon);

        parent.addAxonOutput(axon);
        child.addAxonInput(axon);
    }

    public void update(){
        for (int i = 0; i < numberOfTry; i++){
            runOneTime();
        }
    }
    private void runOneTime() {
        for (int i = 0; i < 10; i++) { // FIXME: 4/8/2019 change 10
            System.out.println("Generation "+ i);
            oneImpulse();
        }
    }
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

    @Override
    public String toString() {
        return "" + numberOfTry;
    }
}
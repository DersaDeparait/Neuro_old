import java.util.LinkedList;

/**
 * Вся нейромереажа разом
 */
public class Network {
    //region fields
    int numberOfTry = 8; // Кількість закидувань даних в нейромережу
    int currentTry = 0; // Поточний номер закидування
    int cycleCounter = 0; // Лічильник циклів

    LinkedList<Axon> axons; // Всі аксони
    LinkedList<Neuron> neurons; // Всі нейрони
    //endregion

    //region construct
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
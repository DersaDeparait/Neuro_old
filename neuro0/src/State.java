public enum State {
    FIRST,
    ACTIVE,
    PASSIVE,
    BACKWARD,
    BACKWARD_FIRST;

    public String getName(){
        switch (this){
            case FIRST:return  "First";
            case ACTIVE:return "Active";
            case PASSIVE:return "Passive";
            case BACKWARD: return "Backward";
            case BACKWARD_FIRST: return "ActiveBackward";
            default:return "Nan";
        }
    }
}
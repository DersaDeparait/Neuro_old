public enum State {
    ACTIVE,
    PASSIVE;
    public String getName(){
        switch (this){
            case ACTIVE:return "Active";
            case PASSIVE:return "Passive";
            default:return "Nan";
        }
    }
}
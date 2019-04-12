public enum State {
    FIRST,
    ACTIVE,
    PASSIVE;
    public String getName(){
        switch (this){
            case FIRST:return  "First";
            case ACTIVE:return "Active";
            case PASSIVE:return "Passive";
            default:return "Nan";
        }
    }
}
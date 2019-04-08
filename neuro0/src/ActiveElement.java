public class ActiveElement {
    State state;

    public ActiveElement(){
        state = State.PASSIVE;
    }

    public void setActive(){ state = State.ACTIVE; }
    public void setPassive(){ state = State.PASSIVE; }

    public void update(){
        switch(state){
            case ACTIVE:{ updateActive(); } break;
            case PASSIVE: { updatePassive(); } break;
            default: {} break;
        }
    }
    protected void updateActive(){

    }
    protected void updatePassive(){

    }
}

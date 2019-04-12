public class ActiveElement {
    State state;

    public ActiveElement(){
        state = State.PASSIVE;
    }

    public void setFirst() { state = State.FIRST; }
    public void setActive(){ state = State.ACTIVE; }
    public void setPassive(){ state = State.PASSIVE; }
    public boolean isWork(){
        if (state == State.FIRST || state == State.ACTIVE)
            return true;
        else
            return false;
    }

    public void update(){
        switch(state){
            case FIRST: { updateFirst(); } break;
            case ACTIVE:{ updateActive(); } break;
            case PASSIVE: { updatePassive(); } break;
            default: {} break;
        }
    }
    protected void updateFirst(){ }
    protected void updateActive(){ }
    protected void updatePassive(){ }
}

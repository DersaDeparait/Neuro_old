public class ActiveElement {
    State state;

    public ActiveElement(){
        state = State.PASSIVE;
    }

    public void setFirst()   { state = State.FIRST; }
    public void setActive()  { state = State.ACTIVE; }
    public void setPassive() { state = State.PASSIVE; }
    public void setBackward(){ state = State.BACKWARD; }
    public void setBackwardFirst() { state = State.BACKWARD_FIRST; }
    public boolean isWork()  {
        if (state == State.FIRST || state == State.ACTIVE
        || state == State.BACKWARD || state == State.BACKWARD_FIRST)
            return true;
        else
            return false;
    }

    public void update(){
        switch(state){
            case FIRST: { updateFirst(); printActive(); } break;
            case ACTIVE:{ updateActive(); printActive(); } break;
            case BACKWARD: { updateBackward(); printBackward(); } break;
            case BACKWARD_FIRST: { updateBackwardFirst(); printBackward(); } break;
            case PASSIVE: { updatePassive(); } break;
            default: {} break;
        }
    }
    protected void updateFirst(){ }
    protected void updateActive(){ }
    protected void updatePassive(){ }
    protected void updateBackward(){ }
    protected void updateBackwardFirst(){ }

    public void printActive(){ }
    public void printBackward(){ }
}
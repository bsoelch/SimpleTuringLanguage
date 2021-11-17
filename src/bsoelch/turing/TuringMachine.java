package bsoelch.turing;

import java.util.HashMap;

public class TuringMachine {
    final BitTape tape;
    final HashMap<Integer, Transition> stateTable;
    int state;

    public TuringMachine( HashMap<Integer, Transition> stateTable) {
        this.tape = new BitTape();
        this.stateTable = stateTable;
    }

    public boolean step(){
        if(state==-1)
            return false;
        Transition next=stateTable.get(state|(tape.get()?0x80000000:0));
        if(next==null){
            System.err.println("missing transition "+(tape.get()?'1':'0')+""+state+":");
            return false;
        }
        tape.set(next.getNewValue());
        tape.move(next.moveLeft);
        state=next.nextState;
        return state!=-1;
    }

    static public class Transition {
        final boolean moveLeft;
        final boolean newValue;
        final int nextState;

        /**@param nextState id of the next state, id -1 leads to halt-state*/
        Transition(boolean moveLeft, boolean newValue, int nextState) {
            this.moveLeft = moveLeft;
            this.newValue = newValue;
            this.nextState = nextState;
            if(nextState<-1){
                throw new IllegalArgumentException("nextState has to be >=0");
            }
        }

        public boolean getNewValue() {
            return newValue;
        }
    }

    @Override
    public String toString() {
        return "("+state+") "+tape;
    }
}

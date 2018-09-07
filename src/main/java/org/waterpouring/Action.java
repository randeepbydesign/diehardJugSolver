package org.waterpouring;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Defines a single action that can be taken to proceed in solving a water-pouring problem.
 * One or more actions will combine to create the total solution if it can be solved.
 */
public class Action {
    Integer[] jugIndex;
    MoveType moveType;
    ArrayList<Integer> endState;

    /**
     * Create an action
     * @param JUGS The jugs, in gallon size, that we are working with
     * @param startState The state at the time of taking this action
     * @param moveType
     * @param jugIndex The indices of the jugs we are working with. Depending on the move type this can
     *                 be 1 or 2 values
     * @return
     */
    public static Action of(Integer[] JUGS, ArrayList<Integer> startState, MoveType moveType, Integer... jugIndex) {
        return new Action(JUGS, startState, moveType, jugIndex);
    }

    private Action(Integer[] JUGS, ArrayList<Integer> startState, MoveType moveType, Integer... jugIndex) {
        this.jugIndex = jugIndex;
        this.moveType = moveType;
        this.endState = moveType==null ?
                startState : moveType.move.doMove(JUGS, startState, jugIndex);
    }


    public ArrayList<Integer> getEndState() {
        return endState;
    }

    @Override
    public String toString() {
        return "Action{" +
                "jugIndex=" + Arrays.toString(jugIndex) +
                ", moveType=" + moveType +
                ", endState=" + endState +
                '}';
    }
}

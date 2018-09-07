package org.waterpouring;

import java.util.Arrays;

/**
 * Defines a single action that can be taken to proceed in solving a water-pouring problem.
 * One or more actions will combine to create the total solution if it can be solved.
 */
public class Action {
    Integer[] glassIndex;
    MoveType moveType;
    Integer[] endState;

    /**
     * Create an action
     * @param JUGS The jugs, in gallon size, that we are working with
     * @param startState The state at the time of taking this action
     * @param moveType
     * @param jugIndex The indices of the jugs we are working with. Depending on the move type this can
     *                 be 1 or 2 values
     * @return
     */
    public static Action of(Integer[] JUGS, Integer[] startState, MoveType moveType, Integer... jugIndex) {
        return new Action(JUGS, startState, moveType, jugIndex);
    }

    private Action(Integer[] GLASSES, Integer[] startState, MoveType moveType, Integer... glassIndex) {
        this.glassIndex = glassIndex;
        this.moveType = moveType;
        this.endState = moveType==null ?
                startState : moveType.move.doMove(GLASSES, startState, glassIndex);
    }

    @Override
    public String toString() {
        return "Action{" +
                "glassIndex=" + Arrays.toString(glassIndex) +
                ", moveType=" + moveType +
                ", endState=" + Arrays.toString(endState) +
                '}';
    }
}

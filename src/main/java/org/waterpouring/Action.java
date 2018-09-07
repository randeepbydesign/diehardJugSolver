package org.waterpouring;

import java.util.Arrays;

/**
 * Defines a single action that can be taken to proceed in solving a water-pouring problem.
 * One or more actions will combine to create the total solution if it can be solved.
 */
public class Action {
    private Integer[] jugIndex;
    private MoveType moveType;
    private Integer[] state;

    /**
     * Create an action
     * @param state The state at the time immediately before taking this action
     * @param moveType
     * @param jugIndex The indices of the jugs we are working with. Depending on the move type this can
     *                 be 1 or 2 values
     */
    public Action(Integer[] state, MoveType moveType, Integer... jugIndex) {
        this.jugIndex = jugIndex;
        this.moveType = moveType;
        this.state = state;
    }

    public Integer[] getJugIndex() {
        return jugIndex;
    }

    public void setJugIndex(Integer[] jugIndex) {
        this.jugIndex = jugIndex;
    }

    public MoveType getMoveType() {
        return moveType;
    }

    public void setMoveType(MoveType moveType) {
        this.moveType = moveType;
    }

    public Integer[] getState() {
        return state;
    }

    public void setState(Integer[] state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return "Action{" +
                "jugIndex=" + Arrays.toString(jugIndex) +
                ", moveType=" + moveType +
                ", state=" + Arrays.toString(state) +
                '}';
    }
}

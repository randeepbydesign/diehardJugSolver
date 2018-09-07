package org.waterpouring;


import java.util.ArrayList;
import java.util.Arrays;

/**
 * An enumeration of all possible moves
 */
public enum MoveType {
    /** Dump a jug out so that it is empty. Receives a single index parameter in its associated move */
    EMPTY((JUGS, state, jugIndex) -> {
        ArrayList<Integer> ret = new ArrayList<>(state);
        ret.set(jugIndex[0], 0);
        return ret;
    }),
    /** Pour water into a jug up so that it is full. Receives a single index parameter in its move */
    FILL((JUGS, state, jugIndex) -> {
        assert jugIndex.length==1;
        ArrayList<Integer> ret = new ArrayList<>(state);
        ret.set(jugIndex[0], JUGS[jugIndex[0]]);
        return ret;
    }),
    /** Pour the contents from one jug to another. Receives two index parameters: 0 = the from jug, 1 = the to jug */
    POUR((JUGS, state, jugIndex) -> {
        assert jugIndex.length ==2;

        Integer fromJug = jugIndex[0];
        Integer toJug = jugIndex[1];

        if(fromJug==toJug) {
            //Invalid move, cannot pour jug into itself
            return state;
        }

        ArrayList<Integer> ret = new ArrayList<>(state);

        Integer amtPourable = Math.min(state.get(fromJug), (JUGS[toJug] - state.get(toJug)));
        ret.set(fromJug, ret.get(fromJug) - amtPourable);
        ret.set(toJug, ret.get(toJug) + amtPourable);
        return ret;
    }, true);

    public final Move move;
    public final Boolean requiresTwoJugs;

    MoveType(Move move) {
        this(move, false);
    }

    MoveType(Move move, Boolean requiresTwoJugs) {
        this.move = move;
        this.requiresTwoJugs = requiresTwoJugs;
    }
}

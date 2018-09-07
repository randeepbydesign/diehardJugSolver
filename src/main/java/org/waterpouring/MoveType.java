package org.waterpouring;


/**
 * An enumeration of all possible moves
 */
public enum MoveType {
    /** Dump a jug out so that it is empty. Receives a single index parameter in its associated move */
    EMPTY(new Move() {
        @Override
        public Integer[] doMove(Integer[] JUGS, Integer[] state, Integer[] jugIndex) {
            return new Integer[0];
        }
    }), //TODO:
    /** Pour water into a jug up so that it is full. Receives a single index parameter in its move */
    FILL(new Move() {
        @Override
        public Integer[] doMove(Integer[] JUGS, Integer[] state, Integer[] jugIndex) {
            return new Integer[0];
        }
    }), //TODO:
    /** Pour the contents from one jug to another. Receives two index parameters: 0 = the from jug, 1 = the to jug */
    POUR(new Move() {
        @Override
        public Integer[] doMove(Integer[] JUGS, Integer[] state, Integer[] jugIndex) {
            return new Integer[0];
        }
    }); //TODO:

    public final Move move;
    MoveType(Move move) {
        this.move = move;
    }
}

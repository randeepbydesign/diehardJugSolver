package org.waterpouring;


import java.util.Arrays;

/**
 * An enumeration of all possible moves
 */
public enum MoveType {
    /** Dump a jug out so that it is empty. Receives a single index parameter in its associated move */
    EMPTY(new Move() {
        @Override
        public Integer[] doMove(Integer[] JUGS, Integer[] state, Integer[] jugIndex) {
            assert jugIndex.length==1;
            Integer[] ret = Arrays.copyOf(state, state.length);
            ret[jugIndex[0]] = 0;
            return ret;
        }
    }), 
    /** Pour water into a jug up so that it is full. Receives a single index parameter in its move */
    FILL(new Move() {
        @Override
        public Integer[] doMove(Integer[] JUGS, Integer[] state, Integer[] jugIndex) {
            assert jugIndex.length==1;
            Integer[] ret = Arrays.copyOf(state, state.length);
            ret[jugIndex[0]] = JUGS[jugIndex[0]];
            return ret;
        }
    }), 
    /** Pour the contents from one jug to another. Receives two index parameters: 0 = the from jug, 1 = the to jug */
    POUR(new Move() {
        @Override
        public Integer[] doMove(Integer[] JUGS, Integer[] state, Integer[] jugIndex) {
            assert jugIndex.length ==2;
            Integer[] ret = Arrays.copyOf(state, state.length);

            Integer fromJug = jugIndex[0];
            Integer toJug = jugIndex[1];

            if(fromJug==toJug) {
                //Invalid move, cannot pour jug into itself
                return state;
            }

            Integer amtPourable = state[fromJug] <= (JUGS[toJug] - state[toJug]) ?
                    state[fromJug] : (JUGS[toJug] - state[toJug]);
            ret[fromJug] = ret[fromJug] - amtPourable;
            ret[toJug] = ret[toJug] + amtPourable;
            return ret;
        }
    }); 

    private final Move move;

    public Move getMove() {
        return move;
    }

    MoveType(Move move) {
        this.move = move;
    }
}

package org.waterpouring;

/**
 * A move describes some action taken on one or more jugs
 */
@FunctionalInterface
public interface Move {
    /**
     *
     * @param JUGS the jugs, defined by their gallon capacity, for the problem
     * @param state The current capacity of each jug
     * @param jugIndex The indices of the various jugs to be operated on
     * @return the new state of the jugs after performing the given move
     */
    Integer[] doMove(final Integer[] JUGS, Integer[] state, Integer[] jugIndex);
}

package org.waterpouring;

import java.util.*;

public class Solver {
    final Integer[] JUGS;
    final Integer GOAL;

    // The initial state we begin with (all jugs empty)
    final Integer[] startState;

    /**
     *
     * @param jugs The capacity, in gallons, of the jugs we are working with
     * @param goal in gallons, of the desired amount
     */
    public Solver(final Integer[] jugs, final Integer goal) {
        this.JUGS = jugs;
        this.GOAL = goal;
        this.startState = new Integer[]{JUGS.length};
        for(int i=0; i<startState.length; i++) {
            this.startState[i] = 0;
        }
    }

    /**
     * For the given state, return all possible actions that can be taken
     * @param state
     * @return
     */
    public List<Action> createActions(Integer[] state) {
        //TODO:
        return null;
    }

    /**
     * Look within the tree of all possible actions to see if a goal solution exists
     * @param actions
     * @return
     */
    public boolean solutionExists(List<List<Action>> actions) {
        //TODO:
        return false;
    }

    /**
     * For the given trail, take all the possible actions in the next list and create
     * a new list for each one where the trail of actions becomes the head of the list,
     * and the next element is appended to the end.
     * @param trail of actions
     * @param next possible next actions from the end of the trail.
     * @return For example: <p>
     *     trail = "A", "B", "C" and next = "D", "E", "F"
     * <p>yields:
     * <ul>
     *     <li> "A", "B", "C", "D"</li>
     *     <li> "A", "B", "C", "E"</li>
     *     <li> "A", "B", "C", "F"</li>
     * </ul>
     */
    public List<List<Action>> combineTrails(List<Action> trail, List<Action> next) {
        //TODO:
        return null;
    }

    /**
     * For the given parameters, find a solution where the Glass capacities can produce
     * the goal amount.
     * @return A list of actions that yield the goal amount, or null if no solution exists
     */
    public List<Action> findSolution() {
        //TODO:
        return null;
    }

    public static void main(String[] args) {
        List<Action> solution = new Solver(new Integer[]{5, 3}, 4).findSolution();
        if(solution != null) {
            System.out.println("Solution found: " + solution);
        } else {
            System.out.println("No solution found for the given parameters");
        }
    }
}
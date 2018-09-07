package org.waterpouring;

import java.util.*;

public class Solver {
    final Integer[] JUGS;
    final Integer GOAL;

    // The initial state we begin with (all jugs empty)
    final Integer[] START_STATE;

    /**
     *
     * @param jugs The capacity, in gallons, of the jugs we are working with
     * @param goal in gallons, of the desired amount
     */
    public Solver(final Integer[] jugs, final Integer goal) {
        this.JUGS = jugs;
        this.GOAL = goal;
        this.START_STATE = new Integer[JUGS.length];
        for(int i = 0; i< START_STATE.length; i++) {
            this.START_STATE[i] = 0;
        }
    }

    /**
     * For the given state, return all possible actions that can be taken
     * @param state
     * @return
     */
    public List<Action> createActions(Integer[] state) {
        List<Action> pourStates = new ArrayList<>();
        for(int i=0; i<JUGS.length; i++) {
            pourStates.add(new Action(state, MoveType.EMPTY, i));
            pourStates.add(new Action(state, MoveType.FILL, i));
            for(int j=0; j<JUGS.length; j++) {
                pourStates.add(new Action(state, MoveType.POUR, i, j));
            }
        }
        return pourStates;
    }

    public Integer[] renderAction(Action action) {
        if(action.getMoveType()==null) return START_STATE;
        return action.getMoveType().getMove()
                .doMove(JUGS, action.getState(), action.getJugIndex());
    }

    /**
     * Look within the tree of all possible actions to see if a goal solution exists
     * @param actions
     * @return
     */
    public boolean solutionExists(List<List<Action>> actions) {
        for(List<Action> trail : actions) {
            if(solutionExistsInTrail(trail)) {
                return true;
            }
        }
        return false;
    }

    private boolean solutionExistsInTrail(List<Action> actionTrail) {
        Action lastAction = actionTrail.get(actionTrail.size() - 1);
        Integer[] endState = renderAction(lastAction);
        for(int i=0; i < endState.length; i++) {
            if(endState[i] == GOAL) {
                return true;
            }
        }
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
        List<List<Action>> retList = new ArrayList<>();
        for(Action nextAction : next) {
            List<Action> updated = new ArrayList<>(trail);
            updated.add(nextAction);
            retList.add(updated);
        }
        return retList;
    }

    /**
     * Helper method that takes an application state and return new actions
     * that can be performed from it
     * @param action The current state of jugs (ie, how much water is in each one)
     * @param stateHistory The list of states we have already visited
     * @return The list of new actions that can be taken
     */
    private List<Action> solve(Action action, Set<List<Integer>> stateHistory) {
        Integer[] state = renderAction(action);
        List<Action> retState = createActions(state);
        List<Action> retActions = new ArrayList<>();
        for (Action nextAction : retState) {
            Integer[] nextState = renderAction(nextAction);
            if (stateHistory.contains(Arrays.asList(nextState))) continue;
            stateHistory.add(Arrays.asList(nextState));
            retActions.add(nextAction);
        }
        return retActions;
    }

    /**
     * For the given parameters, find a solution where the Jug capacities can produce
     * the goal amount.
     * @return A list of actions that yield the goal amount, or null if no solution exists
     */
    public List<Action> findSolution() {
        Set<List<Integer>> history = new HashSet<>();
        history.add(Arrays.asList(START_STATE));
        List<List<Action>> trails = new ArrayList<>();

        List<Action> initialTrail = new ArrayList<>();
        initialTrail.add(new Action(START_STATE, null, new Integer[]{}));
        trails.add(initialTrail);

        while(!solutionExists(trails) && trails.size() > 0) {
            ListIterator<List<Action>> it = trails.listIterator();
            while(it.hasNext()) {
                List<Action> trail = it.next();
                Action endState= trail.get(trail.size()-1);
                List<Action> next = solve(endState, history);
                it.remove();

                if(next.size() > 0) {
                    List<List<Action>> combined = new ArrayList<>();
                    combined.addAll(combineTrails(trail, next));
                    for(List<Action> combinedTrail : combined) {
                        it.add(combinedTrail);
                    }
                }
            }
        }

        for(List<Action> trail : trails) {
            if(solutionExistsInTrail(trail)) return trail;
        }
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
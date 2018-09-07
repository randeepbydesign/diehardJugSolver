package org.waterpouring;

import java.util.*;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Solver {
    final Integer[] JUGS;
    final Integer GOAL;

    // The initial state we begin with (all jugs empty)
    final ArrayList<Integer> startState;
    final List<Action> startActionTrail;

    /**
     *
     * @param jugs The capacity, in gallons, of the jugs we are working with
     * @param goal in gallons, of the desired amount
     */
    public Solver(final Integer[] jugs, final Integer goal) {
        this.JUGS = jugs;
        this.GOAL = goal;
        this.startState = new ArrayList(Collections.nCopies(this.JUGS.length, 0));
        this.startActionTrail = Collections.singletonList(Action.of(JUGS, this.startState, null));
    }

    public IntFunction<List<Action>> actionTrailToAvailableActions(final List<Action> actionTrail) {
        return (jugIndex) -> Arrays.stream(MoveType.values())
                .map(moveTypeToAction(jugIndex, last(actionTrail).map(Action::getEndState)))
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    private Function<MoveType, List<Action>> moveTypeToAction(final Integer jugIndex, final Optional<ArrayList<Integer>> state) {
        return moveType -> !state.isPresent() ? Collections.emptyList() : moveType.requiresTwoJugs ?
                //Create n-1 actions representing the from jug defined at jugIndex to the other JUGS
                IntStream.range(0, JUGS.length)
                        //Only consider jugs that don't match the "from" jugIndex
                        .filter(toJugIndex -> toJugIndex != jugIndex)
                        //Transform the index to the appropriate action
                        .mapToObj(toJugIndex -> Action.of(JUGS, state.get(), moveType, jugIndex, toJugIndex))
                        .collect(Collectors.toList()) :
                //For single jug actions, just create the Action at the defined jugIndex as a singleton list
                Collections.singletonList(Action.of(JUGS, state.get(), moveType, jugIndex));
    }

    /**
     * For the given state, return all possible actions that can be taken
     * @param actionTrail list of defined actions. The last action is the most
     *                    recent and contains the current state
     * @return
     */
    public List<Action> createActions(List<Action> actionTrail) {
        return IntStream.range(0, JUGS.length)
                .mapToObj(actionTrailToAvailableActions(actionTrail))
                .flatMap(Collection::stream).collect(Collectors.toList());
    }
    /**
     * Look within the tree of all possible actions to see if a goal solution exists
     * @param actions
     * @return
     */
    public boolean solutionExists(List<List<Action>> actions) {
        return actions.stream()
                //Get the last action element in the list
                .map(this::last)
                .filter(Optional::isPresent).map(Optional::get)
                //Get the end state of all jugs and flatMap them to a stream of Ints
                .map(Action::getEndState)
                .flatMap(Collection::stream)
                .anyMatch(GOAL::equals);
    }

    /**
     *
     */
    private final Predicate<List<Action>> SolutionExistsInTrail =
            (actionTrail) -> solutionExists(Collections.singletonList(actionTrail));

    /**
     *
     * @param sourceList The list to concatenate to (this list is unmodified)
     * @param <T>
     * @return A function that transforms an element by concatenating it to the end of a
     * new list that begins with the elements supplied by trail
     */
    private <T> Function<T, List<T>> concatenateToList(List<T> sourceList) {
        return (T element) -> {
            List<T> retList = new ArrayList<>(sourceList);
            retList.add(element);
            return retList;
        };
    }

    /**
     *
     * @param list
     * @param <T>
     * @return the last element in a list if it is not empty
     */
    private <T> Optional<T> last(List<T> list) {
        return list.isEmpty() ?
                Optional.empty() : Optional.of(list.get(list.size()-1));
    }

    /**
     * Helper method that takes an application state and return new actions
     * that can be performed from it
     * @param stateHistory The list of states we have already visited to prevent circular
     *                     paths
     * @return Mapper that transforms an action trail to the same trail ended with each
     * possible next action
     */
    private Function<List<Action>, List<List<Action>>> actionTrailToNextActionTrails(Set<ArrayList<Integer>> stateHistory) {
        return (actionTrail) -> createActions(actionTrail).stream()
                .filter(newAction -> !stateHistory.contains(newAction.endState))
                .map(concatenateToList(actionTrail))
                .collect(Collectors.toList());
    }

    public Optional<List<Action>> findSolution(List<List<Action>> trails, Set<ArrayList<Integer>> history) {
        return (!solutionExists(trails) && !trails.isEmpty()) ?
                findSolution(trails.stream()
                //Map each trail to list of possible paths; flat map organizes them into a single stream
                    .map(actionTrailToNextActionTrails(history)).flatMap(Collection::stream)
                    .collect(Collectors.toList()),
                Stream.concat(history.stream(), trails.stream()
                    .map(this::last).filter(Optional::isPresent)
                    .map(Optional::get)
                    .map(Action::getEndState))
                    .collect(Collectors.toSet())) :
                trails.stream().filter(SolutionExistsInTrail).findAny();
    }

    /**
     * For the given parameters, find a solution where the Jug capacities can produce
     * the goal amount.
     * @return A list of actions that yield the goal amount, or null if no solution exists
     */
    public Optional<List<Action>> findSolution() {
        return findSolution(Collections.singletonList(startActionTrail), Collections.singleton(startState));
    }

    public static void main(String[] args) {
        Optional<List<Action>> solution = new Solver(new Integer[]{5, 3}, 4).findSolution();
        if(solution.isPresent()) {
            System.out.println("Solution found:\n" + solution.get().stream()
                    .map(Action::toString)
                    .collect(Collectors.joining("\n\t")));
        } else {
            System.out.println("No solution found for the given parameters");
        }
        assert solution.isPresent();

        solution = new Solver(new Integer[]{5, 5}, 10).findSolution();
        assert !solution.isPresent() : "solution should not be possible: " + solution.get();

        solution = new Solver(new Integer[]{3, 4}, 5).findSolution();
        assert !solution.isPresent() : "solution should not be possible: " + solution.get();
    }
}
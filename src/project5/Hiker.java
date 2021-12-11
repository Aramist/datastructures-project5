package project5;

import java.util.ArrayList;

/** Represents the hiker descending the mountain.
 * Note that there is only one instance of the Hiker class shared across
 * all descents of the same mountain.
 *
 * @author Aramis Tanelus
 * @version 1
 */

public class Hiker {

    private int foodCount;
    private int axeCount;
    private int raftCount;

    private ArrayList<RestStop> visitedStops;

    public Hiker() {
        // Start with one food because my algorithm treats entering the root node
        // as a complete step in the hiker's descent
        foodCount = 1;
        axeCount = 0;
        raftCount = 0;
        visitedStops = new ArrayList<>();
    }

    /** Updates supply counts based on supplies and obstacles present at the stop.
     * Does not verify hiker supply count afterward, this is performed in the public
     * method.
     */
    private void visitRestStop(RestStop restStop) {
        foodCount--;  // Account for the cost of traveling
        visitedStops.add(restStop);

        for (Supply s : restStop.getSupplies()) {
            switch (s) {
                case FOOD:
                foodCount++;
                break;
                case AXE:
                axeCount++;
                break;
                case RAFT:
                raftCount++;
                break;
            }
        }

        for (Obstacle o : restStop.getObstacles()) {
            switch (o) {
                case RIVER:
                raftCount--;
                break;
                case FALLEN_TREE:
                axeCount--;
                break;
            }
        }
    }

    /** Attempts to travel through a rest stop.
     * If there aren't sufficent supplies to cross the stop, the hiker's position
     * remains unchanged. Otherwise, the hiker's position and supply counts are
     * updated.
     *
     * @param restStop Rest stop to travel through. Assumed to be a child of the
     * last visited rest stop.
     * @return true if there are sufficent supplies to make it through the stop.
     */
    public boolean attemptToVisit(RestStop restStop) {
        visitRestStop(restStop);

        // If these become negative, the path is a dead end for us
        if (foodCount < 0 || axeCount < 0 || raftCount < 0) {
            backtrack();
            return false;
        }

        return true;
    }

    /** Undoes the hiker's arrival at the most recent rest stop.
     */
    public void backtrack() {
        if (visitedStops.isEmpty())
            return;
        RestStop restStop = visitedStops.get(visitedStops.size() - 1);

        foodCount++;  // Account for the cost of traveling
        visitedStops.remove(visitedStops.size() - 1);

        for (Supply s : restStop.getSupplies()) {
            switch (s) {
                case FOOD:
                foodCount--;
                break;
                case AXE:
                axeCount--;
                break;
                case RAFT:
                raftCount--;
                break;
            }
        }

        for (Obstacle o : restStop.getObstacles()) {
            switch (o) {
                case RIVER:
                raftCount++;
                break;
                case FALLEN_TREE:
                axeCount++;
                break;
            }
        }
    }

    /** A List of all stops visited by the hiker in the current traversal
     */
    public ArrayList<RestStop> getVisitedStops() {
        return visitedStops;
    }

}

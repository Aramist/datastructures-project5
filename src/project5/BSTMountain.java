package project5;

import java.util.ArrayList;

/** A mountain represented by a binary search tree.
 * Contains an interface for constructing a representation of a mountain.
 * from a list of rest stops and finding paths down the mountain.
 *
 * @author Aramis Tanelus
 * @version 1
 */
public class BSTMountain {

    /** Internally represents each station along the mountain
     * The RestStop member contains information about the obstacles 
     * and supplies available at each node.
     */
    private class Node {
        public RestStop restStop;
        public Node left;
        public Node right;
        public int height;
        
        private Node(RestStop restStop) {
            this.restStop = restStop;
            this.height = 1;
        }

        /** Convenience method for identifying leaves
         *
         * @return true if this node has no children
         */
        public boolean isLeaf() {
            return right == null && left == null;
        }

        /** The height difference between the right and left subtrees.
         * Used to determine whether the node should be rebalanced through
         * AVL rotations.
         *
         * @return the balance of the node
         */
        public int balanceFactor() {
            if (right == null && left == null)
                // Empty subtrees are treated as having height equal to 0
                return 0;
            else if (right == null)
                return -left.height;
            else if (left == null)
                return right.height;
            else
                return right.height - left.height;
        }
    }

    private Node root;

    public BSTMountain() {
        root = null;
    }

    /** Adds a rest stop to the mountain by inserting it into the
     * appropriate location on the tree.
     *
     * @param restStop RestStop to insert
     */
    public void addRestStop(RestStop r) {
        root = addRestStop(root, r);
    }

    /** Private helper for adding rest stops while accounting for any
     * changes in the tree's structure.
     * In the event `start` needs to change places, the method will handle
     * all changes within start and its subtrees and return a reference to
     * the node adopting the position formerly held by `start`
     */
    private Node addRestStop(Node start, RestStop r) {
        // Base case, `start` was an empty subtree, return a ref to the new node
        if (start == null)
            return new Node(r);

        int comparison = r.compareTo(start.restStop);
        // I don't believe the project will be tested on duplicates, but for
        // the sake of making a decision, rest stops with duplicate names will
        // be ignored
        if (comparison == 0) {
            return start;
        } else if (comparison > 0) {
            start.right = addRestStop(start.right, r);
            return start;
        } else {
            start.left = addRestStop(start.left, r);
            return start;
        }

    }


    /** Traverses the tree to find paths to the bottom of the mountain
     *
     * @param hiker the hiker descending this mountain
     * @throws NullPointerException if hiker is null
     */
    public void findSolutions(Hiker hiker) throws NullPointerException {
        if (root == null) {
            // If there are no rest stops, there are no solutions
            return;
        }
        if (hiker == null) {
            throw new NullPointerException("Error: findSolutions should not be called with a null Hiker reference.");
        }
        findSolutions(root, hiker);
    }

    /** Helper function that performs the task of findSolutions recursively.
     *
     * Assumes start is not null
     */
    private void findSolutions(Node start, Hiker hiker) {
        boolean success = hiker.attemptToVisit(start.restStop);
        if (!success) {
            // Always backtrack before returning to the calling function
            // Otherwise the hiker will have fewer supplies then they should
            hiker.backtrack();
            return;
        }
        if (start.isLeaf()) {
            if (hiker.getVisitedStops().size() == root.height){
                // The level of the leaf (given by size()) is equal the tree height,
                // so we don't have a cliff
                printSolution(hiker.getVisitedStops());
            }
            hiker.backtrack();
            return;
        }

        // Call left before right to prioritize solutions on the left side of
        // the mountain
        findSolutions(start.left, hiker);
        findSolutions(start.right, hiker);
    }

    /** Helper function to print a solution once it's been found
     */
    private void printSolution(ArrayList<RestStop> solution) {
        if (solution.size() == 0)
            return;  // Not sure if this can actually happen
        StringBuilder builder = new StringBuilder();
        builder.append(solution.get(0));

        for(int i = 1; i < solution.size(); i++) {
            builder.append(" ");
            builder.append(solution.get(i));
        }
        System.out.println(builder.toString());
    }
}


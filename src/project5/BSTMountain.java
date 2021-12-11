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

        /** Updates the height of this node using the children's heights
         */
        public void updateHeight() {
            int rightHeight = right == null ? 0 : right.height;
            int leftHeight = left == null ? 0 : left.height;
            height = ((rightHeight > leftHeight) ? rightHeight : leftHeight) + 1;
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
    public void add(RestStop r) {
        root = add(root, r);
    }

    /** Private helper for adding rest stops while accounting for any
     * changes in the tree's structure.
     * In the event `start` needs to change places, the method will handle
     * all changes within start and its subtrees and return a reference to
     * the node adopting the position formerly held by `start`
     */
    private Node add(Node start, RestStop r) {
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
            start.right = add(start.right, r);
            start.updateHeight();
            return start;
        } else {
            start.left = add(start.left, r);
            start.updateHeight();
            return start;
        }

    }


    /** Traverses the tree to find paths to the bottom of the mountain
     */
    public void findSolutions(){
        if (root == null) {
            // If there are no rest stops, there are no solutions
            return;
        }
        Hiker hiker = new Hiker();
        findSolutions(root, hiker);
    }

    /** Helper function that performs the task of findSolutions recursively.
     *
     * Assumes start is not null
     */
    private void findSolutions(Node start, Hiker hiker) {
        boolean success = hiker.attemptToVisit(start.restStop);
        if (!success) {
            // Backtracking occurs in the attemptToVisit method in the event of failure
            return;
        }
        if (start.isLeaf()) {
            if (hiker.getVisitedStops().size() == root.height){
                // The level of the leaf (given by size()) is equal the tree height,
                // so we don't have a cliff
                printSolution(hiker.getVisitedStops());
            }
            // Ensure the hiker has all the supplies it should have
            hiker.backtrack();
            return;
        }

        // Call left before right to prioritize solutions on the left side of
        // the mountain
        if (start.left != null)
            findSolutions(start.left, hiker);
        if (start.right != null)
            findSolutions(start.right, hiker);

        hiker.backtrack();
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


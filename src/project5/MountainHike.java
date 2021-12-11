package project5;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

/** Entry point for the mountain hike pathfinding program
 *
 * @author Aramis Tanelus
 * @version 1
 */

public class MountainHike {

    /** Parses a text file representing a BSTMountain to yield a BSTMountain object.
     *
     * @param filename Path to the text file
     * @return The BSTMountain described by the file
     * @throws IllegalArgumentException when the file is unreachable, unreadable, or of invalid format
     */
    private static BSTMountain parseFile(String filename) throws IllegalArgumentException {
        // Read and validate the file
        // This segment of code is largely copied from my project 2 submission
        File dataFile = new File(filename);

        if (!dataFile.exists())
            throw new IllegalArgumentException("Error: could not find the given file: " + filename);
        if (!dataFile.canRead())
            throw new IllegalArgumentException("Error: could not read the given file: " + filename);

        Scanner s = null;

        try {
            s = new Scanner(dataFile);
        } catch (FileNotFoundException e){ 
            throw new IllegalArgumentException("Error: could not find the given file: " + filename);
        }

        BSTMountain mountain = new BSTMountain();

        while (s.hasNextLine()) {
            String nextLine = s.nextLine().strip();
            String[] description = nextLine.split(" ");
        
            String restStopName = description[0];
            int firstObstacleLocation = firstObstacleLocation(description);
            ArrayList<Supply> supplies = parseSupplies(description, firstObstacleLocation);
            ArrayList<Obstacle> obstacles = parseObstacles(description, firstObstacleLocation);

            RestStop stop = new RestStop(restStopName, supplies, obstacles);
            mountain.add(stop);
        }

        return mountain;
    }

    /** Parses supplies from each line of the input file.
     * Will stop at endIdx, which is the pre-determined location of the first obstacle
     */
    private static ArrayList<Supply> parseSupplies(String[] restStopDescription, int endIdx) {
        ArrayList<Supply> supplies = new ArrayList<>();
        // Element 0 contains the rest stop name, so start at 1
        for (int i = 1; i < endIdx; i++) {
            if (restStopDescription[i].equals("food"))
                supplies.add(Supply.FOOD);
            else if (restStopDescription[i].equals("axe"))
                supplies.add(Supply.AXE);
            else if (restStopDescription[i].equals("raft"))
                supplies.add(Supply.RAFT);
        }
        return supplies;
    }

    /** Parses obstacles from each line of the input file.
     * startIdx is the location of the first obstacle, and is implemented in order
     * to ignore any supplies listed after the first obstacle.
     */
    private static ArrayList<Obstacle> parseObstacles(String[] restStopDescription, int startIdx){
        ArrayList<Obstacle> obstacles = new ArrayList<>();
        int idx = startIdx;

        // A while loop is used instead of a for loop because the index may need to increment
        // by 2, in the event a fallen tree shows up
        while (idx < restStopDescription.length) {
            if (isFallenTree(restStopDescription, idx)) {
                obstacles.add(Obstacle.FALLEN_TREE);
                idx += 2;
            } else if (restStopDescription[idx].equals("RIVER")) {
                obstacles.add(Obstacle.RIVER);
            } else {
                idx++;
            }
        }

        return obstacles;
    }

    /** Helper to determine whether the words at index and index+1 form the 
     * falling tree obstacle.
     *
     * Assumes index is non-negative
     */
    private static boolean isFallenTree(String[] restStopDescription, int index) {
        // Through short-circuiting, this should avoid any index bounds errors.
        return restStopDescription.length > index + 1 
            && restStopDescription[index].equals("fallen")
            && restStopDescription[index + 1].equals("tree");
    }

    /** Helper that determines the index of the first obstacle.
     *
     * If none exist, should return the length of the array
     */
    private static int firstObstacleLocation(String[] restStopDescription) {
        int idx = 0; 
        boolean foundObstacle = false;

        if (restStopDescription.length == 1)
            return 1;

        do {
            // Like above, start at element 1 since element 0 is the rest stop's name
            // Here this is done by incrementing before performing checks
            // This makes returning from the function more convenient, as idx holds
            // the desired value once the loop exits
            idx++;
            boolean foundRiver = restStopDescription[idx].equals("river");
            boolean foundTree = isFallenTree(restStopDescription, idx);
            foundObstacle = foundRiver || foundTree;
        } while (idx < restStopDescription.length - 1 && !foundObstacle);
        if (!foundObstacle)
            idx++;  // Make it equal to the length of the array
        return idx;
    }
    
    public static void main(String[] args) {
        if (args.length < 1) {
            System.err.println("Error: user must provide a filename as a command-line argument");
            return;
        }

        String filename = args[0];
        try {
            BSTMountain mountain = parseFile(filename);
            mountain.findSolutions();
        } catch (IllegalArgumentException e) {
            System.err.println(e.getMessage());
            return;
        }
    }
}

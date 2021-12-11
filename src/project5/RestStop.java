package project5;

import java.util.ArrayList;

/** Represents a rest stop along the mountain.
 * May contain supplies and obstacles for the hiker.
 *
 * @author Aramis Tanelus
 * @version 1
 */
public class RestStop implements Comparable<RestStop> {

    private String name;
    private ArrayList<Supply> supplies;
    private ArrayList<Obstacle> obstacles;

    public RestStop(String name, ArrayList<Supply> supplies, ArrayList<Obstacle> obstacles) {
        if (name == null || supplies == null || obstacles == null) {
            throw new NullPointerException("RestStop must not be instantiated with null data");
        }
        if (name.equals("")) {
            throw new IllegalArgumentException("Cannot have a RestStop with an empty name");
        }

        this.name = name;
        this.supplies = supplies;
        this.obstacles = obstacles;
    }

    /** A List of all obstacles at the stop.
     *
     * Do not mutate
     */
    public ArrayList<Obstacle> getObstacles() {
        return obstacles;
    }

    /** A List of all supplies at the stop.
     *
     * Do not mutate
     */
    public ArrayList<Supply> getSupplies() {
        return supplies;
    }

    /** A string representation of the rest stop via its name.
     *
     * @return the name of the rest stop
     */
    public String toString() {
        return name;
    }

    /** Compares the names of the rest stops to determine their ordering on the mountain.
     * 
     * @param other RestStop to compare to
     * @return positive int if this is greater, negative if `other` is greater, zero if both are equal
     * @throws NullPointerException when `other` is null
     */
    public int compareTo(RestStop other) throws NullPointerException{
        if (other == null) {
            throw new NullPointerException("Cannot compare to a null RestStop");
        }
        return name.compareTo(other.name);
    }
}


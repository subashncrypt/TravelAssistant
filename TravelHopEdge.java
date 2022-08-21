//package com.company;

/**
 Travel hop edge is the path between 2 cities in a Travel planner
 * it has all the Travel information between 2 cities
 * start and destination cities
 * cost of travel between them
 * Time in minutes
 */

public class TravelHopEdge {

    // Private variables

    private String startCity;               // Start city name
    private String destinationCity;         // Destination city name
    private String modeOfTravel;            // Mode of travel [ Flight, Train ]
    private int timeMinutes;                // Time of travel in minutes
    private int cost;                       // Cost of Travel between 2 cities

    // Relative weight between 2 cities [Time and cost with user precedence].
    private int weight = Integer.MAX_VALUE;

    // if this is Chosen edge
    private boolean chosen = false;
    private boolean testedPath = false;

    // Constructor that initializes [startCity, destination, modeOfTravel, Time, cost]
    public TravelHopEdge(String startCity, String destinationCity, String modeOfTravel, int time, int cost) {
        this.startCity = startCity;
        this.destinationCity = destinationCity;
        this.modeOfTravel = modeOfTravel;
        this.timeMinutes = time;
        this.cost = cost;
    }

    /**
     * Getter function that gets you city name
     * @return start city
     */
    public String getStartCity() {
        return startCity;
    }

    /**
     * Setter function that sets start city
     * @param startCity
     */
    public void setStartCity(String startCity) {
        this.startCity = startCity;
    }

    /**
     * Getter function that gets destination city
     * @return destination city
     */
    public String getDestinationCity() {
        return destinationCity;
    }

    /**
     * Setter function that sets destination city
     * @param destinationCity
     */
    public void setDestinationCity(String destinationCity) {
        this.destinationCity = destinationCity;
    }

    /**
     * Getter function that gets modeOfTravel
     * @return modeOfTravel
     */
    public String getModeOfTravel() {
        return modeOfTravel;
    }

    /**
     * Setter function that sets your mode of travel
     * @param modeOfTravel
     */
    public void setModeOfTravel(String modeOfTravel) {
        this.modeOfTravel = modeOfTravel;
    }

    /**
     * Getter function that gets time in minutes
     * @return timeMinutes
     */
    public int getTimeMinutes() {
        return timeMinutes;
    }

    /**
     * Setter function that sets Time in minutes
     * @param timeMinutes
     */
    public void setTimeMinutes(int timeMinutes) {
        this.timeMinutes = timeMinutes;
    }

    /**
     * Getter function that gets costs
     * @return cost
     */
    public int getCost() {
        return cost;
    }

    /**
     * Setter function that sets cost
     * @param cost
     */
    public void setCost(int cost) {
        this.cost = cost;
    }

    /**
     * Getter function that gets you the weight of each edge
     * @return
     */
    public int getWeight() {
        return weight;
    }

    /**
     * Setter function that sets weight
     * @param weight
     */
    public void setWeight(int weight) {
        this.weight = weight;
    }

    /**
     * Getter functions the returns if this the chosen path between 2 cities
     * @return
     */
    public boolean isChosen() {
        return chosen;
    }

    /**
     * Setter function that sets chosen path between 2 cities
     * @param chosen
     */
    public void setChosen(boolean chosen) {
        this.chosen = chosen;
    }

}

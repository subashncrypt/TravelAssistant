//package com.company;

import java.util.ArrayList;

/**
 City Vertex class that represent each city
 in a travelPlanner that stores crucial information about the state of each city like
 * City name
 * is Testing required to enter into this city
 * Is testing done in this city
 * Previous city through which it has been visited
 * connection with other cities along with mode of transportation
 * also stores the shortest weight through which it can be visited
 */
public class CityVertex {

    // Private variables of the class
    private String cityName;                    // Stores city name
    private boolean testRequired;               // Testing required if True
    private int timeToTestDays;                 // Testing can be done when Duration >=0
    private int nightlyHotelCosts;              // Cost each day while taking test

    private String previousCity = null;         // holds the previous city visited while travelling

    // Holds the shortest weight from the source - initialized to MAX value
    private int ShortestWeightFound = Integer.MAX_VALUE;

    // Holds if the city is visited or not.
    private boolean visited = false;           // True is the city is visited
    private boolean tested  = false;           // True if the traveller is tested in this path

    // Stores all the edges between a particular city.
    private ArrayList<TravelHopEdge> edges = new ArrayList<TravelHopEdge>();

    // Constructor that Initializes [cityName/ testRequired/ TimetoTest/ nightlyCost]
    public CityVertex(String cityName, boolean testRequired, int timeToTest, int nightlyHotelCosts) {
        this.cityName = cityName;
        this.testRequired = testRequired;
        this.timeToTestDays = timeToTest;
        this.nightlyHotelCosts = nightlyHotelCosts;
    }

    /**
     * Function that returns :
     * @return True when there is no route to a city with same mode of transportation,
     * False if the edge already exists [Travel info is already known]
     **/
    public boolean isValidTravelInfo(TravelHopEdge edge){

        // Loop through and see if an edge exits with same source, destination & Mode of travel
        for(int i =0; i< this.edges.size(); i++){
            if (this.edges.get(i).getStartCity().equalsIgnoreCase(edge.getStartCity()) &&
                this.edges.get(i).getDestinationCity().equalsIgnoreCase(edge.getDestinationCity()) &&
                this.edges.get(i).getModeOfTravel().equalsIgnoreCase(edge.getModeOfTravel())){
                // return false if the info is already known
                return false;
            }
        }
        return true;    // else return true
    }

    // Getter and setter functions

    /**
     * Function that returns a city name
     * @return String City name
     */
    public String getCityName() {
        return cityName;
    }

    /**
     * Function that sets the city name
     * @param cityName
     *
     */
    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    /**
     * Getter Function that sets testRequired
     * @return test Required in a particular city
     */
    public boolean isTestRequired() {
        return testRequired;
    }

    /**
     * Setter function that sets testRequired variable
     * @param testRequired
     */
    public void setTestRequired(boolean testRequired) {
        this.testRequired = testRequired;
    }

    /**
     * Getter Function that returns Time to test
     * @return Time to test before reults
     */
    public int getTimeToTest() {
        return timeToTestDays;
    }

    /**
     * Setter function that returns Time to test
     * @param timeToTest
     */
    public void setTimeToTest(int timeToTest) {
        this.timeToTestDays = timeToTest;
    }

    /**
     * Getter Function that returns NightlyHotelCost
     * @return nightlyHotelCosts
     */
    public int getNightlyHotelCosts() {
        return nightlyHotelCosts;
    }

    /**
     * Setter function that sets Cost of stay for each night
     * @param nightlyHotelCosts
     */
    public void setNightlyHotelCosts(int nightlyHotelCosts) {
        this.nightlyHotelCosts = nightlyHotelCosts;
    }

    /**
     * Getter function that gets all connections between cities
     * @return edges
     */
    public ArrayList<TravelHopEdge> getEdges() {
        return this.edges;
    }

    /**
     * Setter function that sets all the edges
     * @param edge
     */
    public void setEdges(TravelHopEdge edge) {
        this.edges.add(edge);
    }

    /**
     * Getter function that gets shortest cost to reach this city
     * @return ShortestWeightFound
     */
    public int getShortestWeightFound() {
        return ShortestWeightFound;
    }

    /**
     * Setter function that sets the shortest weight found
     * @param ShortestWeightFound
     */
    public void setShortestWeightFound(int ShortestWeightFound) {
        this.ShortestWeightFound = ShortestWeightFound;
    }

    /**
     * Getter function that sets if the city is visited or not
     * @return visited
     */
    public boolean isVisited() {
        return this.visited;
    }

    /**
     * Setter function that sets if teh city is visited or not
     * @param visited
     */
    public void setVisited(boolean visited) {
        this.visited = visited;
    }

    /**
     * Getter function that gets previous city through which it was visted
     * @return previousCity
     */
    public String getPreviousCity() {
        return previousCity;
    }

    /**
     * Setter function that sets Previous city
     * @param previousCity
     */
    public void setPreviousCity(String previousCity) {
        this.previousCity = previousCity;
    }

    /**
     * Getter function that gets if the path is test or not
     * @return tested
     */
    public boolean isTested() {
        return tested;
    }

    /**
     * Setter function that sets if this path is tested or not
     * @param tested
     */
    public void setTested(boolean tested) {
        this.tested = tested;
    }
}

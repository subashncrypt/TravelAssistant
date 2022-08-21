//package com.company;

import java.util.ArrayList;
import java.util.List;

/**
 * Travel assistant is where you find the best path between 2 cities which may or may not be connected
 * between each other based on user precedence it stores all the cities and then path between then
 * based on the user preference of cost and hops and time we calculate the best path
 */
public class TravelAssistant {
    // Private variable
    // List of cities
    private ArrayList<CityVertex> cities = new ArrayList<>();

    // Add city function which adds a city into the travel assistant and makes it available for travelling
    /**
     *
     * @param cityName
     * @param testRequired
     * @param timeToTest
     * @param nightlyHotelCost
     * @return True if the city is added else returns false
     * @throws IllegalArgumentException
     */
    public boolean addCity(String cityName, boolean testRequired, int timeToTest, int nightlyHotelCost )
            throws IllegalArgumentException{

        // validate if bad data exits
        // Throws exception in this case
        cityBadDataExists(cityName,nightlyHotelCost);

        // creating and instance of the city
        CityVertex newCity = new CityVertex(cityName,testRequired,timeToTest,nightlyHotelCost);

        // if the list of cities are empty add them to the list
        if (this.cities.isEmpty()){
            cities.add(newCity);
            return true;
        }
        // if the list of cities is not empty and City dose not exits then add them to the list.
        else if(!this.cities.isEmpty() && (cityExists(newCity.getCityName()) == -1)){
            cities.add(newCity);
            return true;
        }
        else{
            return false;       // in all other cases return false.
        }

    }

    /**
     * AddFlight functions adds a flight path between 2 cities in a travel planner
     * @param startCity
     * @param destinationCity
     * @param flightTime
     * @param flightCost
     * @return
     * @throws IllegalArgumentException
     */
    public boolean addFlight( String startCity, String destinationCity, int flightTime, int flightCost)
            throws IllegalArgumentException{
        return addEdge(startCity,destinationCity,"Flight",flightTime,flightCost);
    }

    /**
     * AddTrain function adds a train path between 2 cities in a Travel planner
     * @param startCity
     * @param destinationCity
     * @param trainTime
     * @param trainCost
     * @return True if path is added successfully into the system
     * @throws IllegalArgumentException
     */
    public boolean addTrain( String startCity, String destinationCity, int trainTime, int trainCost)
            throws IllegalArgumentException{
        return addEdge(startCity,destinationCity,"Train",trainTime,trainCost);
    }

    /**
     * Function that calculates the best possible path between 2 cities based on Dijkstra algorithm
     * Based on user precedence calculates weight between cities and finds the best path between them
     * @param startCity
     * @param destinationCity
     * @param isVaccinated
     * @param costImportance
     * @param travelTimeImportance
     * @param travelHopImportance
     * @return List of String [best path cities]
     * @throws IllegalArgumentException
     */
    public List<String> planTrip (String startCity, String destinationCity, boolean isVaccinated, int
            costImportance, int travelTimeImportance, int travelHopImportance ) throws IllegalArgumentException{

        // Initializing the return string
        List<String> routeInOrder = new ArrayList<String>();        // main output sting of the path

        // variables to perform operations
        int nextCity;                       // Stores the next city index
        int weight;                         // Weight of the city up to that point in the path
        boolean canTravel;                  // True if a path exits you can use to travel based on the des requirements
        CityVertex nextCityVertex;          // stores the next city in the path

        // Initialize all the values before finding the best path for the user
        InitializeDefaultValues();

        // Data validation
        int startIndex = cityExists(startCity);
        int destinationIndex = cityExists(destinationCity);

        // if bad input data exists then throw an exception
        if(startCity == null   || destinationCity == null   ||
           startCity.isEmpty() || destinationCity.isEmpty() ||
           costImportance <0   || travelHopImportance <0    ||
           startIndex == -1 || destinationIndex == -1       || travelTimeImportance <0){
            throw new IllegalArgumentException("Bad input data");
        }

        // only if both start city and destination city exits
        if((startIndex >=0 && destinationIndex >=0)) {

            // initialize the source node
            CityVertex source = this.cities.get(startIndex);
            source.setShortestWeightFound(0);
            source.setPreviousCity(source.getCityName());

            //initialize
            CityVertex minimumVertex = null;

            // calculate the relative weight between edges
            calculateRelativeEdgeWeight(costImportance, travelTimeImportance, travelHopImportance,isVaccinated);

            // main logic
            while (!vistedAllCities()) {

                try{
                    // function that returns a minimum vertex.
                    minimumVertex = minimumWeightedVertex();
                    minimumVertex.setVisited(true);
                }
                catch (Exception e){
                    break;
                }

                // Logic to visit all the nodes from the chosen vertex
                // and calculate its minimum weight
                for (int i = 0; i < minimumVertex.getEdges().size(); i++) {

                    // get the values of the next city
                    nextCity = cityExists(minimumVertex.getEdges().get(i).getDestinationCity());
                    nextCityVertex = this.cities.get(nextCity);
                    canTravel = canTravelNext(minimumVertex,nextCityVertex,isVaccinated);

                    // perform operation if we can travel into the city
                    if(canTravel){

                        // calculate weight
                        weight = calculateWeight(minimumVertex,minimumVertex.getEdges().get(i));

                        if(weight < nextCityVertex.getShortestWeightFound()){

                            nextCityVertex.setShortestWeightFound(weight);
                            nextCityVertex.setPreviousCity(minimumVertex.getCityName());

                            //chosen edge -- make sure that other edges to the same destination are not chosen
                            for(int j=0; j<minimumVertex.getEdges().size(); j++ ){
                                if(minimumVertex.getEdges().get(j).getDestinationCity().equalsIgnoreCase(nextCityVertex.getCityName())){
                                    minimumVertex.getEdges().get(j).setChosen(false);
                                }
                            }
                            minimumVertex.getEdges().get(i).setChosen(true);
                        }
                    }

                }
            }
        }
        else{
            return null;
        }

        // Calculate the Travel path and return the string
        routeInOrder = travelPath(startIndex,destinationIndex);

        return routeInOrder;

    }

    /**
     * Function that calculates the bets path based on the weight and returns a path string
     * @param startIndex
     * @param desIndex
     * @return List of String of the best path from source to destination
     */
    private List<String> travelPath(int startIndex, int desIndex) {
        List <String> routeInOrder = new ArrayList<String>();
        List <String> cityPathList = new ArrayList<>();
        String previousCityName = null;
        CityVertex city = null;
        String path = null;

        if(this.cities.get(desIndex).getPreviousCity() != null){
            city = this.cities.get(desIndex);

            // loop until we reach the source by back tracking it
            while(!city.getPreviousCity().equalsIgnoreCase(city.getCityName())){
                // add the city into the list
                cityPathList.add(city.getCityName());
                city = this.cities.get(cityExists(city.getPreviousCity()));
            }

            cityPathList.add(city.getCityName());

            System.out.println(cityPathList);

            for(int j = cityPathList.size()-1; j>=0; j--){
                path = "";
                city = this.cities.get(cityExists(cityPathList.get(j)));
                if(city.getPreviousCity().equalsIgnoreCase(city.getCityName())){

                    // get city name from start
                    path = "start"+" "+city.getCityName();
                    routeInOrder.add(path);

                    if(j-1 >= 0){
                        path = getChosenPath(cityPathList.get(j),cityPathList.get(j-1));
                        routeInOrder.add(path);
                    }

                }
                else {
                    if(j-1 >= 0){
                        path = getChosenPath(cityPathList.get(j),cityPathList.get(j-1));
                        routeInOrder.add(path);
                    }
                }
            }

        }
        else{
            routeInOrder = null;
        }

        return routeInOrder;
    }

    /**
     * Function that returns Path based on user preference between 2 cities
     * @param current
     * @param next
     * @return chosen path between 2 cities
     */
    private String getChosenPath(String current, String next) {

        String path = "";
        TravelHopEdge edge;
        CityVertex nextCity = this.cities.get(cityExists(next));
        CityVertex currentCity = this.cities.get(cityExists(current));

        for(int i=0; i< currentCity.getEdges().size(); i++ ){

            edge = currentCity.getEdges().get(i);

            if(edge.getDestinationCity().equalsIgnoreCase(nextCity.getCityName())){
                if(edge.isChosen()){
                    if(edge.getModeOfTravel().equalsIgnoreCase("Flight")){
                        path = "fly"+" "+nextCity.getCityName();
                    }
                    else if(edge.getModeOfTravel().equalsIgnoreCase("Train")){
                        path = "train"+" "+nextCity.getCityName();
                    }
                }
            }
        }

        return path;
    }

    /**
     * Function that initializes default values across all values of cities and edges
     */
    private void InitializeDefaultValues() {

        //Loop through and set all the cities and its edges
        for(int k=0; k<this.cities.size(); k++){
            this.cities.get(k).setPreviousCity(null);
            this.cities.get(k).setShortestWeightFound(Integer.MAX_VALUE);
            this.cities.get(k).setVisited(false);
            this.cities.get(k).setTested(false);

            // initialize all the edges of the cities as well
            for(int j=0; j<this.cities.get(k).getEdges().size(); j++){

                // Set relative edge between cities
                TravelHopEdge e = this.cities.get(k).getEdges().get(j);
                e.setWeight(Integer.MAX_VALUE);
                e.setChosen(false);
            }
        }
    }

    /**
     * Function that calculates weights
     * @param currentCity
     * @param edge
     * @return weight up to the city from the previous path
     */
    private int calculateWeight(CityVertex currentCity,TravelHopEdge edge){

        int weight = 0;
        int timeWeight = 0;

        weight = edge.getWeight()+currentCity.getShortestWeightFound();
        return weight;
    }
    
    // Function that return true if we can travel into the next city

    /**
     * Function that validates true if we can travel into the next city.
     * @param minimumVertex
     * @param nextCityVertex
     * @param isVaccinated
     * @return True if we can travers to the next city
     */
    private boolean canTravelNext(CityVertex minimumVertex,CityVertex nextCityVertex, boolean isVaccinated){

        // Return value initialization
        boolean canTravel;

        // Check if we can traverse to the next city
        if(!isVaccinated && nextCityVertex.isTestRequired()){

            // check if the city requires testing and the user is travelling in the tested path
            if(minimumVertex.isTested()){
                canTravel = true;  // can travel
            }
            else {
                // cannot travel when the path requires testing
                // and the path is not tested
                canTravel = false;
            }
        }
        else {
            canTravel = true;  // can travel
        }

        return canTravel;
    }

    /**
     * Function that finds the minimum weighted city and returns it
     * @return Returns the minimum weighted city.
     */
    private CityVertex minimumWeightedVertex(){
        int minimumValue = Integer.MAX_VALUE;
        int minimumIndex = -1;

        for(int i=0; i< this.cities.size(); i++){

            if(this.cities.get(i).getShortestWeightFound() < minimumValue && (!this.cities.get(i).isVisited())){
                minimumValue = this.cities.get(i).getShortestWeightFound();
                minimumIndex = i;
            }
        }

        return this.cities.get(minimumIndex);
    }

    /**
     * Function that finds if there are any unvisited vertices
     * @return True if we find all the vertices have been visited
     */
    private boolean vistedAllCities() {

        // loop through and check if there are any unvisited
        for(int i=0; i <this.cities.size(); i++) {

            // if single unvisited city exits return false
            if(!this.cities.get(i).isVisited()){
                return false;
            }
        }
        return true;
    }

    /**
     * Function to calculate relative weight of the edges between cities
     * @param costImportance
     * @param travelTimeImportance
     * @param travelHopImportance
     * @param isVaccinated
     */
    private void calculateRelativeEdgeWeight(int costImportance, int travelTimeImportance
            , int travelHopImportance, boolean isVaccinated) {

        // Initialize the weight to 0
        int relativeWeight = 0;        // Final relative weight including all factors
        int edgeCost =0;               // is the cost of travel along with hotel cost

        // logic to calculate the relative weight based on the user preference
        for(int i=0; i <this.cities.size(); i++){
            for(int j=0; j<this.cities.get(i).getEdges().size(); j++){

                // get the edge to set weight
                TravelHopEdge edge = this.cities.get(i).getEdges().get(j);

                //calculate edge cost based on various factors
                edgeCost = calculateEdgeCost(edge,isVaccinated);

                // relative weight for each edge from a city
                relativeWeight = (costImportance*edgeCost)+
                                 (travelTimeImportance*edge.getTimeMinutes())+
                                 (travelHopImportance*1);
                // set the weight
                edge.setWeight(relativeWeight);
            }
        }

    }

    /**
     * Function the calculates the edge cost between based on hotel stay / vaccinated / cost of travel
     * @param edge
     * @param isVaccinated
     * @return
     */
    private int calculateEdgeCost(TravelHopEdge edge, boolean isVaccinated) {

        int edgeCost = Integer.MAX_VALUE;
        CityVertex nextCity = this.cities.get(cityExists(edge.getDestinationCity()));
        CityVertex currentCity = this.cities.get(cityExists(edge.getStartCity()));

        // handle the case when it required testing and need to add waiting time into it
        if(!isVaccinated && nextCity.isTestRequired()){

            // if the current city is same as source then no cost or hotel stay
            if(currentCity.getShortestWeightFound() == 0){

                if (currentCity.getTimeToTest() >= 0){
                    edgeCost = edge.getCost();
                    currentCity.setTested(true);
                }
            }else if(currentCity.isTested()){
                edgeCost = edge.getCost();
                            }
            else if(!currentCity.isTested()) {
                if(currentCity.getTimeToTest() >= 0){
                    edgeCost = (currentCity.getNightlyHotelCosts()*currentCity.getTimeToTest())+edge.getCost();
                    currentCity.setTested(true);
                }
            }
        }
        else {
            edgeCost = edge.getCost();
        }

        if(currentCity.isTested()){
            nextCity.setTested(true);
        }

        return edgeCost;
    }

    /**
     * Function that checks if bad data exits while adding a city into the system
     * @param cityName
     * @param nightlyHotelCost
     * @return
     */
    private boolean cityBadDataExists(String cityName ,int nightlyHotelCost){

        // Throw exception if bad data for Hotel costs
        if(nightlyHotelCost <= 0){
            throw new IllegalArgumentException("bad data input for hotel cost, cannot be "+ nightlyHotelCost);
        }
        // Throw exception if bad data for City name
        else if(cityName == null || cityName.isEmpty()){
            throw new IllegalArgumentException("bad data input for city name, cannot be "+ cityName);
        }
        return false;
    }

    /**
     * Function that checks if a city exits and returns its index
     * @param cityName
     * @return
     */
    private int cityExists(String cityName){
        // if the city list is not empty then
        if(!this.cities.isEmpty()){
            for(int i=0; i< this.cities.size(); i++){
                //check if there is city of the same name
                if(this.cities.get(i).getCityName().equalsIgnoreCase(cityName))
                {
                    return i;  // returns the index
                }
            }
        }
        return -1;  // returns -1 if the city dosent exits
    }

    /**
     * Function that adds edges between cities
     * @param startCity
     * @param destinationCity
     * @param mode
     * @param Time
     * @param Cost
     * @return True if edge is added sucessfully else returns a false
     */
    private boolean addEdge(String startCity, String destinationCity,String mode, int Time, int Cost){

        // Variables
        int startIndex = -1;        // stores the source city index
        int desIndex = -1;          // stores the destination city index
        Boolean outcome = false;    // stores outcome of path addition into the travel planner

        //Check if the city exits and get the index for the same
        startIndex = cityExists(startCity);             // Stores the index of start city
        desIndex   = cityExists(destinationCity);       // Stores the index of destination

        // Check bad data for city if it exits and throw an exception for the same
        if(startCity == null   || destinationCity == null ||
           startCity.isEmpty() || destinationCity.isEmpty() ||
           startIndex == -1    || desIndex == -1){
            throw new IllegalArgumentException("Bad input data start city and destination city");
        }

        // If Time and cost are zero or less
        if(Time <= 0 || Cost <= 0){
            throw new IllegalArgumentException("Bad input data FlightTime > 0 and cost > 0");
        }

        // only if both start city and destination city exits
        if(startIndex >=0 &&  desIndex >=0){

            //create a new edge between cities with the Mode/Time/cost
            TravelHopEdge edge = new TravelHopEdge(startCity,destinationCity,mode,Time,Cost);
            
            // Validate if the information is already know to the travel planner
            if(this.cities.get(startIndex).isValidTravelInfo(edge)){
                // Add the edge
                this.cities.get(startIndex).setEdges(edge);
                outcome = true;
            }
            else {
                // return false if there is discrepancies
                // in the already known information
                outcome = false;
            }
        }
    return outcome;  // send out the outcome of the new path
    }

    // check for validation

    /**
     * Function that returns the list of cities
     * @return
     */
    public String listOfCities(){

        String s= new String();

        for(int i=0; i < this.cities.size(); i++){
            s += this.cities.get(i).getCityName()+" -- ";
            for (int j=0;j<this.cities.get(i).getEdges().size();j++){
                TravelHopEdge a = this.cities.get(i).getEdges().get(j);
                System.out.println(a.getStartCity()+"----"+ a.getModeOfTravel()+"----"+ a.getWeight()+"----"+a.getDestinationCity());
            }
        }
        return s;
    }

    // check for validation
    public String mainGraph(){

        String s= new String();

        for(int i=0; i < this.cities.size(); i++){
            s += this.cities.get(i).getCityName()+" -- "+this.cities.get(i).getShortestWeightFound()+"--via--"+this.cities.get(i).getPreviousCity()+"\n";
            for (int j=0;j<this.cities.get(i).getEdges().size();j++){
                TravelHopEdge a = this.cities.get(i).getEdges().get(j);
                if(a.isChosen()){
                    s += a.getStartCity()+"----"+ a.getModeOfTravel()+"----"+ a.getWeight()+"----"+a.getDestinationCity()+"--chosen--"+"\n";
                }else{
                    s += a.getStartCity()+"----"+ a.getModeOfTravel()+"----"+ a.getWeight()+"----"+a.getDestinationCity()+"\n";
                }
            }
        }
        return s;
    }

}

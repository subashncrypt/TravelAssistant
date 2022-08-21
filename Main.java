//package com.company;

public class Main {

    public static void main(String[] args) {
	    // write your code here
        TravelAssistant santosh = new TravelAssistant();

        System.out.println(santosh.addCity("A",false,-1,10));
        System.out.println(santosh.addCity("B",false,1,10));
        System.out.println(santosh.addCity("C",false,2,10));
        System.out.println(santosh.addCity("D",true,-3,15));
        System.out.println(santosh.addCity("E",true,2,12));

        santosh.addFlight("A","B",1,4);
        santosh.addFlight("B","C",1,5);
        santosh.addTrain("A","C",3,1);
        santosh.addFlight("C","D",1,4);
        santosh.addTrain("B","D",4,2);
        santosh.addFlight("D","E",2,4);


        System.out.println(santosh.planTrip("A","E",false,1,4,0));

            System.out.println(santosh.mainGraph());
    }
}

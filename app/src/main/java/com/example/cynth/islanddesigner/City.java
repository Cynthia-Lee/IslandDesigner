package com.example.cynth.islanddesigner; /**
 * Class that represents a city on the island. It will represent each vertex/node on the graph.
 * 
 * CSE 214-R12 Recitation, CSE 214-02 Lecture
 * @author Cynthia Lee
 * e-mail: cynthia.lee.2@stonybrook.edu
 * Stony Brook ID: 111737790
 */
import java.util.HashMap;

public class City implements Comparable<City> {
	// each vertex/node
	HashMap<String, Integer> neighbors = new HashMap<String, Integer>();
	// the key is the name of the city, integer is the cost of the road
	String name;

	/**
	 * Constructor that creates a City object with a name
	 * @param cityName
	 * String name of the City
	 */
	public City(String cityName) {
		name = cityName;
	}

	/**
	 * Method that adds a neighboring city to the City
	 * @param neighborName
	 * String name of the city that is the neighbor
	 * @param neighborRoadCost
	 * Integer value of road cost between the City and the neighboring city
	 */
	public void addNeighbor(String neighborName, int neighborRoadCost) {
		neighbors.put(neighborName, neighborRoadCost);
	}

	/**
	 * Method that compares two Cities names based on alphabetical order
	 * @return
	 * Integer of the difference of the String of first City to the second City, based on alphabetical order
	 */
	public int compareTo(City o) { // looks at name so it can be alphabetical
		return name.compareTo(o.name);
	}

	// OPTIONAL
	HashMap<String, Integer> tempNeighbors = new HashMap<String, Integer>(); 
	// useful for figuring out maximum network flow (can keep track of how much flow you have left on a given edge)
	Boolean discovered = false;
	Boolean visited = false;

	public void setDiscovered(boolean b) {
		discovered = b;
	}

	public void setVisited(boolean b) {
		visited = b;
	}
	
	public void setTempNeighbors(HashMap<String, Integer> t) {
		tempNeighbors = t;
	}
}

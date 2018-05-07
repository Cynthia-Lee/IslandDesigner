package com.example.cynth.islanddesigner; /**
 * Class that represents a path of nodes/cities.
 * 
 * CSE 214-R12 Recitation, CSE 214-02 Lecture
 * @author Cynthia Lee
 * e-mail: cynthia.lee.2@stonybrook.edu
 * Stony Brook ID: 111737790
 */
import java.util.LinkedList;

public class Path {
	private int capacity = 0;
	private LinkedList<String> pathList = new LinkedList<String>();

	/**
	 * Method to get capacity value of the path
	 * @return
	 * Integer capacity
	 */
	public int getCapacity() {
		return capacity;
	}

	/**
	 * Method to set the capacity value of the path
	 * @param i
	 * Integer to be set as the new capacity
	 */
	public void setCapacity(int i) {
		capacity = i;
	}
	
	/**
	 * Method to get the LinkedList of the path. It is a LinkedList of Strings
	 * @return
	 * LinkedList<String> pathList
	 */
	public LinkedList<String> getPathList() {
		return pathList;
	}
	
	/**
	 * A method that prints out the path in a String
	 * @return
	 * String that shows the path of nodes/cities with "->" between each city name
	 */
	public String toString() {
		String s = pathList.toString();
		s = s.replace("[", "");
		s = s.replace("]", "");
		s = s.replace(" ", "");
		s += ": " + capacity;
		s = s.replace(",", "->");
		return s;
	}
}

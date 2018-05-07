package com.example.cynth.islanddesigner; /**
 * Class that allows user to run depth first search from a City on the island to any other City on the island, 
 * as well as allowing the user to find the maximum network flow from any City on the island to any other City on
 * the island.
 * 
 * CSE 214-R12 Recitation, CSE 214-02 Lecture
 * @author Cynthia Lee
 * e-mail: cynthia.lee.2@stonybrook.edu
 * Stony Brook ID: 111737790
 */
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

import big.data.DataSource;
import big.data.DataSourceException;

public class IslandDesigner {
	/**
	 * Prompts user with a menu to interact with information about the Cities on the island
	 */
	public static void main(String[] args) {
		System.out.println(
				"Welcome to the Island Designer, because when you're trying to stay above water, Seas get degrees!\n");
		System.out.print("Please enter an url: ");
		Scanner input = new Scanner(System.in);
		String url = input.nextLine();
		IslandNetwork island = new IslandNetwork();
		try {
			island = IslandNetwork.loadFromFile(url);
			System.out.println("\nMap loaded.\n");
			System.out.println("Cities: \n---------------------");
			List<String> cities = new ArrayList<String>(island.getGraph().keySet());
			Collections.sort(cities);
			for (int i = 0; i < cities.size(); i++) {
				System.out.println(cities.get(i));
			}
			System.out.format("%-37s %s%n", "Road", "Capacity");
			System.out.println("----------------------------------------------");
			DataSource ds = DataSource.connectXML(url);
			ds.load();
			String roadNamesStr = ds.fetchString("roads");
			String[] roadNames = roadNamesStr.substring(2, roadNamesStr.length() - 2).split("\",\"");
			for (int j = 0; j < roadNames.length; j++) {
				String[] parts = roadNames[j].split(","); // each string
				System.out.format("%-43s %s%n", parts[0] + " to " + parts[1], parts[2]);
				// System.out.println(parts[0]+" to "+parts[1]+"\t\t\t"+parts[2]);
			}
			System.out.println("\nMenu:");
			System.out.println("\tD) Destinations reachable (Depth First Search)");
			System.out.println("\tF) Maximum Flow");
			System.out.println("\tS) Shortest Path (Extra Credit)");
			System.out.println("\tQ) Quit");
			String option;
			do {
				System.out.print("Please enter an option: ");
				option = input.nextLine();
				if (option.equalsIgnoreCase("D")) {
					System.out.print("Please enter a starting city: ");
					String start = input.nextLine();
					String s = "";
					try {
						s = island.dfs(start).toString();
						System.out.println("DFS Starting from " + start + ":");
					} catch (CityDoesNotExistException e) {
						System.out.print(
								"The city with this name does not exist. City names are case sensitive. (Upper-case matters)");
					}
					if (s.equals("[]")) { // empty list
						System.out.println("No destinations after this city.");
					} else {
						s = s.replace("[", "");
						s = s.replace("]", "");
						System.out.println(s);
					}
				} else if (option.equalsIgnoreCase("F")) {
					System.out.print("Please enter a starting city: ");
					String starting = input.nextLine();
					System.out.print("Please enter a destination: ");
					String dest = input.nextLine();
					// System.out.println("Routing:");
					// System.out.println(island.findCap(starting, dest).toString());
					try {
						System.out.println(island.maxFlow(starting, dest));
					} catch (CityDoesNotExistException e) {
						System.out.println("City or cities does not exist.");
					}
				} else if (option.equalsIgnoreCase("S")) {

				} else {
					System.out.println("Invalid choice!");
				}
			} while (!option.equalsIgnoreCase("Q"));
			System.out.println("You can go your own way! Goodbye!");
		} catch (DataSourceException ex) {
			System.out.println("Map could not be loaded. The url is invalid.");
		}
	}
}

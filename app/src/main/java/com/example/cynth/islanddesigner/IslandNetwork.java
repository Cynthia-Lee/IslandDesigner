package com.example.cynth.islanddesigner; /**
 * Class that holds the graph with cities and represents the island.
 * 
 * CSE 214-R12 Recitation, CSE 214-02 Lecture
 * @author Cynthia Lee
 * e-mail: cynthia.lee.2@stonybrook.edu
 * Stony Brook ID: 111737790
 */
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import big.data.DataSource;

public class IslandNetwork {
	// holds the graph
	// extra credit, implement Djikstra's algorithm for shortest path, using edge
	// capacities as weights
	private static HashMap<String, City> graph; // stores the cities in the graph
	private static List<String> pathList = new ArrayList<String>(); // path list for DFS

	/**
	 * Loads the file from the given URL location. Adds cities to the graph/island.
	 * @param url
	 * URL to be read from
	 * @return
	 * IslandNetwork, island object 
	 */
	public static IslandNetwork loadFromFile(String url) { // loads the file from the given URL location
		// DataSource ds = DataSource.connectXML(url);
		// ds.load();
		// HashMap<String,Node> cities = new HashMap<String,Node>();
		IslandNetwork network = new IslandNetwork();
		graph = new HashMap<String, City>();
		DataSource ds = DataSource.connectXML(url);
		ds.load();
		String cityNamesStr = ds.fetchString("cities");
		String[] cityNames = cityNamesStr.substring(1, cityNamesStr.length() - 1).replace("\"", "").split(",");
		String roadNamesStr = ds.fetchString("roads");
		String[] roadNames = roadNamesStr.substring(2, roadNamesStr.length() - 2).split("\",\"");
		// Fill the HashMap here...
		for (int i = 0; i < cityNames.length; i++) { // create the cities
			City cityNode = new City(cityNames[i]);
			graph.put(cityNames[i], cityNode);
		}
		for (int j = 0; j < roadNames.length; j++) {
			String[] parts = roadNames[j].split(","); // each string
			// System.out.println(parts[0]+" "+parts[1]+" "+parts[2]);
			String from = parts[0];
			String to = parts[1];
			int value = Integer.parseInt(parts[2]);
			// System.out.println(from+" "+to+" "+value);
			// System.out.println(graph.get(from));
			graph.get(from).addNeighbor(to, value);
		}
		return network;
	}

	/**
	 * Returns the maximum network flow from any node to any other node
	 * @param from
	 * String that is the name of city to start from
	 * @param to
	 * String that is the name of the city to be the destination/end
	 * @return
	 * A String that shows the maximum flow with the paths
	 * @throws CityDoesNotExistException
	 * If String from and/or String to are not valid names for cities on the island
	 */
	public String maxFlow(String from, String to) throws CityDoesNotExistException {
		// reset temp neighbor capacities to neighbor capacities for all cities!
		// because we removed the edges and put 0s
		// before we call findCap
		List<String> allNodes = new LinkedList<String>(graph.keySet());
		for (int j = 0; j < allNodes.size(); j++) {
			String node = allNodes.get(j);
			graph.get(node).setTempNeighbors((HashMap<String, Integer>) graph.get(node).neighbors.clone());
		}
		int capSum = 0;
		int maxCap = 0;
		String result = "Routing:\n";
		Path thePath = findCap(from, to);
		while(thePath.getCapacity() > 0) { // make sure that the edge exists
			// System.out.println(thePath.getCapacity());
			// print path in findCap
			result = result + thePath.toString() + "\n"; 
			// add findCap.cap to cap sum
			capSum += thePath.getCapacity();
			maxCap = findCap(from, to).getCapacity();
			// for every edge in findCap list, edge capacity -= find cap.cap (updating the paths to get rid of)
			for(int i = 0; i < thePath.getPathList().size() - 1; i++) {
				String firstN = thePath.getPathList().get(i);
				String secondN = thePath.getPathList().get(i+1);
				// int newEdgeValue = temp.get(firstN).neighbors.get(secondN) - thePath.getCapacity();
				// System.out.println(graph.get(firstN).tempNeighbors.toString()+" "+graph.get(firstN).tempNeighbors.get(secondN));
				int originalEdge = graph.get(firstN).tempNeighbors.get(secondN);
				// System.out.println("this is the min value of path?"+findCap(from, to).getCapacity());
				// int newEdgeValue = originalEdge - findCap(from, to).getCapacity(); 
				int newEdgeValue = originalEdge - maxCap; 
				graph.get(firstN).tempNeighbors.put(secondN, newEdgeValue);
				// graph.get(firstN).neighbors.put(secondN, newEdgeValue);
				// System.out.println("this is the min value of path?"+findCap(from, to).getCapacity());
				// System.out.println(secondN + " " + graph.get(firstN).tempNeighbors.get(secondN));
				// System.out.println(originalEdge + " " + findCap(from, to).getCapacity());
			}
			thePath = findCap(from, to);
		}
		if (maxCap == 0) { // if there is no flow
			return "No route available!";
		}
		return result + "Maximum Flow: " +capSum;
	}

	/**
	 * Helper method for max flow that finds the maximum flow of one path
	 * @param start
	 * String that is the name of the city to start from
	 * @param end
	 * String that is the name of the city to be the destination/end
	 * @return
	 * Path object of the path between the start city and end city
	 * @throws CityDoesNotExistException
	 * If String from and/or String to are not valid names for cities on the island 
	 */
	public Path findCap(String start, String end) throws CityDoesNotExistException {
		int maxCap = 0;
		int cap = 0;
		Path bestNeighbor = new Path(); // path that has the greatest capacity/max flow
		if(graph.get(start) == null) {
			throw new CityDoesNotExistException();
		}
		City startCity = graph.get(start);
		List<String> cityNeighbors = new LinkedList<String>(startCity.tempNeighbors.keySet());
		for (int i = 0; i < cityNeighbors.size(); i++) {
			String key = cityNeighbors.get(i);
			City neighbor = graph.get(key);
			if (neighbor == graph.get(end)) { // if directly neighbors
				// cap = startCity.neighbors.get(end);
				cap = startCity.tempNeighbors.get(end);
			} else {
				// recursively call to find the other connections of the path we want
				// check the minimum edge from the path because cars cannot go more than that
				// System.out.println(graph.get(start).neighbors.get(key));
				// System.out.println(graph.get(start).neighbors.toString());
				// cap = Math.min(findCap(neighbor.name, end).getCapacity(), startCity.neighbors.get(end));
				cap = Math.min(findCap(neighbor.name, end).getCapacity(), startCity.tempNeighbors.get(key));
				// System.out.println(findCap(neighbor.name, end).getCapacity());
				// System.out.println(cap);
			}
			if (cap > maxCap) { // if the path we found has a greater value than the max capacity/previous max
				maxCap = cap;
				bestNeighbor = findCap(neighbor.name, end);
				// the best path (greatest capacity/max flow) is now this path
			}
		}
		bestNeighbor.getPathList().addFirst(start); // add the string of the path to this so it can be printed
		bestNeighbor.setCapacity(maxCap);
		return bestNeighbor;
	}

	// can use dfs as path finder method
	/**
	 * Calculates the depth first search from any node in the graph
	 * @param from
	 * String of the city to start for the depth first search
	 * @return
	 * A list of strings of the nodes that can be reached
	 * @throws CityDoesNotExistException
	 * If the city name is not valid
	 */
	public List<String> dfs(String from) throws CityDoesNotExistException { // depth first search (destination reachable)
		if(graph.get(from) == null) {
			throw new CityDoesNotExistException();
		}
		pathList.clear();
		// reset every visited to be false (reset so starts clear every time)
		resetVisited();
		dfsHelp(from);
		return pathList;
	}

	/**
	 * Helper method for the dfs method. Finds each neighbor and neighbor of neighbor nodes that is reachable.
	 * @param from
	 * String of the city to start for the depth first search
	 */
	public void dfsHelp(String from) {
		// DFS at node, visiting node
		City cityNode = graph.get(from);
		cityNode.visited = true;
		List<String> listNeighbors = new LinkedList<String>(cityNode.neighbors.keySet());
		for (int i = 0; i < listNeighbors.size(); i++) { // go through the neighbors
			// for neighbor in node.neighbors()
			String key = listNeighbors.get(i);
			City nextNeighbor = graph.get(key);
			if (!nextNeighbor.visited) {
				pathList.add(key);
				dfsHelp(nextNeighbor.name);
			}
		}
	}

	/*
	// got to use both discovered and visited!
	// EXTRA CREDIT 
	public String djikstra(String from, String to) throws CityDoesNotExistException {
		// need to assign each node a vertex value from the starting point
		// use default graph's edges for values
		Path result = new Path();
		List<String> allNodes = new LinkedList<String>(graph.keySet());
		HashMap<String, Integer> vertices = new HashMap<String, Integer>();
		// int distance[] = new int[allNodes.size()];
		// for each vertex v in V, do distance[v] = infinity
		// vertices will hold the shortest distance "from" to that node
		for (int i = 0; i < allNodes.size(); i++) {
			String key = allNodes.get(i);
			vertices.put(key, Integer.MAX_VALUE);
			// distance[i] = Integer.MAX_VALUE;
		}
		// distance[source] = 0
		// distance from the source vertex is always 0
		vertices.put(from, 0);
		// S = {};
		// for i = 1 to (number of vertices -1)
		// Find the shortest path for all vertices
		List<String> vNodes = new LinkedList<String>(vertices.keySet());
		String next = from;
		for (int i = 0; i < vNodes.size() - 1; i++) {
			int min = Integer.MAX_VALUE;
			if (vNodes.get(i).equals(from)) { // skip the source
				// next = vNodes.get(i);
			} else {
				City node = graph.get(vNodes.get(i));
				if(node.discovered == false) { // if we used the discovered value way
					// pick minimum distance vertex from set of vertices
					// gets a city name?
					for (int m = 0; m < vertices.size(); m++) {
						String key = vNodes.get(m);
						if (vertices.get(key) < min) {
							System.out.println("inside" + vertices.get(key) + " " +key);
							min = vertices.get(key);
							next = key;
						}
						//System.out.println(min + " " + m + "/" + vertices.size());
					}
					//System.out.println(min);
					if (min == Integer.MAX_VALUE) {
						next = vNodes.get(i);
					}
					// next = vNodes.get(i);
					
					//if (vertices.get(vNodes.get(i)) < min) {
					//	next = vNodes.get(i);
					//}
					node.setDiscovered(true);
					result.getPathList().add(next);
				}
				// for each vertex in V-S that is a neighbor of next...
				for (int v = 0; v < vNodes.size() - 1; v++) {
					City citynode = graph.get(vNodes.get(v));
					int weight = graph.get(next).neighbors.get(vNodes.get(v));
					if(citynode.discovered == false) {
						if (vertices.get(next) + weight < vertices.get(v)) {
							// vertices.get(v) = vertices.get(next) + weight(next, vNodes.get(v));
							int value = vertices.get(next) + weight;
							vertices.put(vNodes.get(v), value);
							result.setCapacity(result.getCapacity()+value);
						}
					}
				}
			}
		}
			// next = index of min distance of all the vertices in V-S
			// S = S u next????
			// for each vertex v in V-S that is neighbor of next
				// if (distance[next] + weight(next,v) < distance[v])
					// distance[v] = distance[next] + weight(next,v)				 
		return "Path: " + result.toString() + "\nCost: " + result.getCapacity();	
	}
	*/
	
	/*
	public Path sumCap(String start, String end) throws CityDoesNotExistException {
		// int minCap = Integer.MAX_VALUE;
		int sumCap = 0;
		Path thePath = new Path(); // path that has the smallest sum of capacity
		if(graph.get(start) == null) {
			throw new CityDoesNotExistException();
		}
		City startCity = graph.get(start);
		List<String> cityNeighbors = new LinkedList<String>(startCity.neighbors.keySet());
		for (int i = 0; i < cityNeighbors.size(); i++) {
			String key = cityNeighbors.get(i);
			City neighbor = graph.get(key);
			if (neighbor == graph.get(end)) { // if directly neighbors
				sumCap += startCity.neighbors.get(end);
			} else {
				// recursively call to find the other connections of the path we want
				// check the edges from the path
				sumCap += sumCap(neighbor.name, end).getCapacity();
			}
			/*
			if (sumCap < minCap) { // if the path we found has a greater value than the max capacity/previous max
				minCap = sumCap;
				bestPath = minCap(neighbor.name, end);
				// the best path (greatest capacity/max flow) is now this path
			}
			
		}
		thePath.getPathList().addFirst(start); // add the string of the path to this so it can be printed
		System.out.println(thePath.getPathList().toString());
		thePath.setCapacity(sumCap);
		return thePath;
	}	*/
	
	// METHODS TO ADD CITIES TO THE GRAPH

	// OPTIONAL HELPER METHODS
	/**
	 * Method that resets the visited values for each node in the graph
	 */
	public void resetVisited() {
		List<String> allNodes = new LinkedList<String>(graph.keySet());
		for (int i = 0; i < allNodes.size(); i++) {
			String key = allNodes.get(i);
			City node = graph.get(key);
			node.setVisited(false);
		}
	}
	
	/**
	 * Getter method to get the graph object
	 * @return
	 * HashMap<String, City> graph
	 */
	public HashMap<String, City> getGraph() {
		return graph;
	}
}

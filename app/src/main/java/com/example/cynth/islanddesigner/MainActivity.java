package com.example.cynth.islanddesigner;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import big.data.DataSource;
import big.data.DataSourceException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv = (TextView) this.findViewById(R.id.tv); // associates xml objects into java
        tv.setMovementMethod(new ScrollingMovementMethod());
        url = (EditText) this.findViewById(R.id.url); // associates xml objects into java
    }
    TextView tv;
    EditText url;
    static IslandNetworkAndroid island1 = new IslandNetworkAndroid();

    public void read(View view) {
        String s = url.getText().toString();
        try {
            //String test = "http://www3.cs.stonybrook.edu/~cse214/hw/hw7-images/hw7.xml";
            if (s.equals("http://www3.cs.stonybrook.edu/~cse214/hw/hw7-images/hw7.xml") || s.equals("www3.cs.stonybrook.edu/~cse214/hw/hw7-images/hw7.xml") || s.equals("http://www.cs.stonybrook.edu/~cse214/hw/hw7-images/hw7.xml")) {
                island1 = IslandNetworkAndroid.loadFromFileAndroid(s);
                // island = IslandNetwork.loadFromFile(s);
                tv.setText("Map loaded.\n");
                tv.append("Cities: \n---------------------\n");
                List<String> cities = new ArrayList<String>(island1.getGraph().keySet());
                Collections.sort(cities);
                for (int i = 0; i < cities.size(); i++) {
                    tv.append(cities.get(i) + "\n");
                }
                tv.append(String.format("%-37s %s%n", "Road", "             Capacity"));
                tv.append("------------------------------------------------------------------\n");
                // DataSource ds = DataSource.connectXML(s);
                // ds.load();
                // String roadNamesStr = ds.fetchString("roads");
                String roadNamesStr = "[\"Composting Fields,Small Pear,12\",\"Lawn City,Small Pear,30\",\"Hipster,Small Pear,14\",\"Lawn City,Hipster,16\",\"Gatsby,Composting Fields,10\",\"Fishingville,Lawn City,17\",\"Fishingville,Gatsby,11\",\"Bones Beach,Hipster,12\",\"Bones Beach,Lawn City,8\",\"Fire Hazard,Bones Beach,13\",\"Kingkongoma,Fire Hazard,7\",\"Kingkongoma,Lawn City,20\",\"University,Kingkongoma,6\",\"University,Fishingville,18\",\"Stream Foot,University,6\",\"Stream Foot,Kingkongoma,11\",\"North Spoon,Stream Foot,15\",\"South Spoon,Stream Foot,20\"]\n";
                String[] roadNames = roadNamesStr.substring(2, roadNamesStr.length() - 3).split("\",\"");
                for (int j = 0; j < roadNames.length; j++) {
                    String[] parts = roadNames[j].split(","); // each string
                    tv.append(String.format("%-43s %s%n", parts[0] + " to " + parts[1], parts[2]));
                }
            } else {
                tv.setText("Map could not be loaded. The url is invalid.");
            }
        } catch (DataSourceException ex) {
            tv.setText("Map could not be loaded. The url is invalid.");
        }
    }

    public void dfs(View view) {
        Intent i = new Intent(getApplicationContext(),DFS.class);
        startActivity(i);
    }

    public void maxFlow(View view) {
        Intent i = new Intent(getApplicationContext(),maxFlow.class);
        startActivity(i);
    }

    public void end(View view) {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                // Actions to do after 10 seconds
                System.exit(0);
            }
        }, 1000);
    }

    /*
    public static IslandNetwork loadFromFileAndroid(String url) { // loads the file from the given URL location
        // DataSource ds = DataSource.connectXML(url);
        // ds.load();
        // HashMap<String,Node> cities = new HashMap<String,Node>();
        IslandNetwork network = new IslandNetwork();
        // graph = new HashMap<String, City>();
        island1.setGraph(new HashMap<String, City>());
        // DataSource ds = DataSource.connectXML(url);
        // ds.load();
        // String cityNamesStr = ds.fetchString("cities");
        String cityNamesStr = "[\"Small Pear\",\"Composting Fields\",\"Hipster\",\"Gatsby\",\"Lawn City\",\"Bones Beach\",\"Fishingville\",\"Fire Hazard\",\"University\",\"Kingkongoma\",\"Stream Foot\",\"North Spoon\",\"South Spoon\"]";
        String[] cityNames = cityNamesStr.substring(1, cityNamesStr.length() - 1).replace("\"", "").split(",");

        // String roadNamesStr = ds.fetchString("roads");
        String roadNamesStr = "[\"Composting Fields,Small Pear,12\",\"Lawn City,Small Pear,30\",\"Hipster,Small Pear,14\",\"Lawn City,Hipster,16\",\"Gatsby,Composting Fields,10\",\"Fishingville,Lawn City,17\",\"Fishingville,Gatsby,11\",\"Bones Beach,Hipster,12\",\"Bones Beach,Lawn City,8\",\"Fire Hazard,Bones Beach,13\",\"Kingkongoma,Fire Hazard,7\",\"Kingkongoma,Lawn City,20\",\"University,Kingkongoma,6\",\"University,Fishingville,18\",\"Stream Foot,University,6\",\"Stream Foot,Kingkongoma,11\",\"North Spoon,Stream Foot,15\",\"South Spoon,Stream Foot,20\"]\n";
        String[] roadNames = roadNamesStr.substring(2, roadNamesStr.length() - 3).split("\",\"");
        // Fill the HashMap here...
        for (int i = 0; i < cityNames.length; i++) { // create the cities
            City cityNode = new City(cityNames[i]);
            island1.getGraph().put(cityNames[i], cityNode);
        }
        for (int j = 0; j < roadNames.length; j++) {
            String[] parts = roadNames[j].split(","); // each string
            // System.out.println(parts[0]+" "+parts[1]+" "+parts[2]);
            String from = parts[0];
            String to = parts[1];
            int value = Integer.parseInt(parts[2]);
            // System.out.println(from+" "+to+" "+value);
            // System.out.println(graph.get(from));
            island1.getGraph().get(from).addNeighbor(to, value);
        }
        return network;
    }
    */

}

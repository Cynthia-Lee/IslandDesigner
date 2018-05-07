package com.example.cynth.islanddesigner;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class DFS extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dfs);
        print = (TextView)this.findViewById(R.id.tv2);
        start = (EditText)this.findViewById(R.id.editText);
    }
    TextView print;
    EditText start;

    public void showDFS(View view) {
        String s = start.getText().toString();
        String result = "";
        try {
            result = MainActivity.island1.dfs(s).toString();
            print.setText("DFS Starting from " + s + ":\n");
        } catch (CityDoesNotExistException e) {
            print.setText(
                    "The city with this name does not exist. City names are case sensitive. (Upper-case matters)");
        }
        if (result.equals("[]")) { // empty list
            print.append("No destinations after this city.");
        } else {
            result = result.replace("[", "");
            result = result.replace("]", "");
           print.append(result);
        }
    }
}

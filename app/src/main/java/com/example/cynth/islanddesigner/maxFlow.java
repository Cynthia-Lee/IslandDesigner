package com.example.cynth.islanddesigner;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class maxFlow extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_max_flow);
        tv3 = (TextView)this.findViewById(R.id.textView2);
        start = (EditText)this.findViewById(R.id.editText2);
        end = (EditText)this.findViewById(R.id.editText3);
    }
    TextView tv3;
    EditText start;
    EditText end;

    public void printMaxFlow(View view) {
        String starting = start.getText().toString();
        String dest = end.getText().toString();
        try {
            // System.out.println(island.findCap(starting, dest).toString());
            tv3.setText(MainActivity.island1.maxFlow(starting, dest));
            // tv3.setText(MainActivity.island1.maxFlow("University", "Hipster"));
        } catch (CityDoesNotExistException e) {
            tv3.setText("City or cities does not exist.");
        }
    }
}

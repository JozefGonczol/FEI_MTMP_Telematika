package com.example.dgonc.test;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.util.ArrayList;

import classes.Result;

public class DisplayListActivity extends AppCompatActivity {

    private void displayList(ArrayList<Result> results){
        StringBuilder message = new StringBuilder();
        for (Result result : results) {
            message.append("t: ").append(String.format("%.2f\n", result.getT()));
            message.append("x: ").append(String.format("%.2f\n", result.getX()));
            message.append("y: ").append(String.format("%.2f\n\n", result.getY()));
        }
        TextView text_list = (TextView)findViewById(R.id.text_list);
        text_list.setText(message.toString());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_list);
        Intent intent = getIntent();
        ArrayList<Result> results = (ArrayList<Result>) intent.getExtras().get(MainActivity.EXTRA_MESSAGE);
        displayList(results);
    }
}

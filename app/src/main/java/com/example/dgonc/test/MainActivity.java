package com.example.dgonc.test;

import android.app.ListActivity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.PointsGraphSeries;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import classes.Calculator;
import classes.Result;

import static android.provider.AlarmClock.EXTRA_MESSAGE;

public class MainActivity extends AppCompatActivity {

    public static final String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";;
    private Calculator calc = new Calculator();
    private double maxY;
    private double maxD;
    private double angle = 0;
    private double v0 = 0;
    private ArrayList<Result> results;

    public void sendData(View view){
        Intent intent = new Intent(this, DisplayListActivity.class);
        intent.putExtra(EXTRA_MESSAGE, results);
        startActivity(intent);
    }

    public void animate(View view){
        Intent intent = new Intent(this, Animate.class);
        intent.putExtra(EXTRA_MESSAGE, results);
        startActivity(intent);
    }

    public void fillGraph(GraphView graph){
        DataPoint[] dataPoints = new DataPoint[results.size()];
        graph.removeAllSeries();
        for(int i =0 ;i<results.size();i++){
            dataPoints[i]=new DataPoint(results.get(i).getX(),results.get(i).getY());
        }

        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(dataPoints);
        graph.getViewport().setScalable(true);
        graph.getViewport().setScalableY(false);
        graph.getViewport().setScrollable(true);
        graph.getViewport().setScrollableY(true);
        graph.getViewport().setMinX(0);
        graph.getViewport().setMinY(0);
        graph.getViewport().setMaxX(200);
        graph.getViewport().setMaxY(maxY);
        graph.addSeries(series);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final RequestQueue queue = Volley.newRequestQueue(this);

        SeekBar seek_angle = (SeekBar)findViewById(R.id.seek_angle);
        SeekBar seek_speed = (SeekBar)findViewById(R.id.seek_speed);
        final TextView text_angle = (TextView)findViewById(R.id.text_angle);
        final TextView text_speed = (TextView)findViewById(R.id.text_speed);
        final TextView text_maxY = (TextView)findViewById(R.id.text_maxY);
        final TextView text_maxD = (TextView)findViewById(R.id.text_maxD);
        final Button btn_compute = (Button)findViewById(R.id.btn_compute);
        final Button btn_list = (Button)findViewById(R.id.btn_list);
        final Button btn_anim = (Button)findViewById(R.id.btn_anim);
        final GraphView graph = (GraphView) findViewById(R.id.graph);
        final Switch swtch = (Switch)findViewById(R.id.switch1);
        final ProgressBar spinner = (ProgressBar)findViewById((R.id.spinner));
        btn_list.setEnabled(false);
        btn_anim.setEnabled(false);





        seek_angle.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                text_angle.setText(String.valueOf(progress));
                angle = (double)progress;
                v0 = Double.parseDouble(String.valueOf(text_speed.getText()));
                maxY = calc.calculateMaxHeight(angle,v0);
                maxD = calc.calculateMaxDistance(angle,v0);
                text_maxY.setText(String.format("%.2f",maxY));
                text_maxD.setText(String.format("%.2f",maxD));
                btn_list.setEnabled(false);
                btn_anim.setEnabled(false);
                btn_compute.setEnabled(true);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        seek_speed.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                text_speed.setText(String.valueOf(progress));
                v0 = (double)progress;
                String angleText = String.valueOf(text_angle.getText());
                if(angle > 0) {
                    angle = Double.parseDouble(angleText);
                }
                maxY = calc.calculateMaxHeight(angle,v0);
                maxD = calc.calculateMaxDistance(angle,v0);
                text_maxY.setText(String.format("%.2f",maxY));
                text_maxD.setText(String.format("%.2f",maxD));
                btn_list.setEnabled(false);
                btn_anim.setEnabled(false);
                btn_compute.setEnabled(true);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        btn_compute.setOnClickListener(v -> {
            results = new ArrayList<>();
            if(swtch.isChecked()){
                spinner.setVisibility(View.VISIBLE);
                btn_compute.setEnabled(false);
                String url = "http://relay.knet.sk/?url=https%3A%2F%2Fwt-b4758815686724bb23b1983eab32b73e-0.sandbox.auth0-extend.com%2Fserver%3Fangle%3D"+angle+"%26velocity%3D"+v0;
                JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(Request.Method.GET, url, null, response -> {
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject o = response.getJSONObject(i);
                            results.add(new Result(o.getDouble("x"),o.getDouble("y"),o.getDouble("t")));

                        } catch (JSONException e) {
                            System.out.println("err3");
                            e.printStackTrace();
                        }
                    }
                    spinner.setVisibility(View.GONE);
                    results=calc.checkResults(results,angle,v0);
                    fillGraph(graph);
                    btn_anim.setEnabled(true);
                    btn_list.setEnabled(true);

                }, e -> {
                    System.out.println("err2");
                    e.printStackTrace();
                    spinner.setVisibility(View.GONE);
                });
                queue.add(jsonObjectRequest);
            }else{
                btn_compute.setEnabled(false);
                results = calc.getResults(angle,v0);
                fillGraph(graph);
                btn_anim.setEnabled(true);
                btn_list.setEnabled(true);

            }



        });

        btn_list.setOnClickListener(this::sendData);

        swtch.setOnClickListener(v -> {
            if(swtch.isChecked()){
                swtch.setText("Online");
            }else{
                swtch.setText("Offline");
            }
            btn_anim.setEnabled(false);
            btn_list.setEnabled(false);
            btn_compute.setEnabled(true);
        });

        btn_anim.setOnClickListener(this::animate);


    }
}

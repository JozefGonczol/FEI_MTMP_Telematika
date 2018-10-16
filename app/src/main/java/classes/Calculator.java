package classes;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Calculator {

    public double calculateMaxHeight(double angle, double initSpeed){
        if(angle == 0){
            return 0.0;
        }
        double sin2 = Math.sin(Math.toRadians(angle)) * Math.sin(Math.toRadians(angle));
        double v02 = initSpeed * initSpeed;
        double g = 9.823;

        return (v02*sin2)/(2*g);
    }

    public double calculateMaxDistance(double angle, double initSpeed){
        double sin =  Math.sin(2*(Math.toRadians(angle)));
        double v02 = initSpeed * initSpeed;
        double g = 9.823;
        return (v02/g)*sin;
    }

    private double calculateTime(double angle, double initSpeed){
        double g = 9.823;
        double sin = Math.sin(Math.toRadians(angle));
        return (2*initSpeed*sin)/g;
    }

    private double calculateX(double angle, double initSpeed, double time){
        double cos = Math.cos(Math.toRadians(angle));
        return initSpeed*time*cos;
    }

    private double calculateY(double angle, double initSpeed, double time){
        double g = 9.823;
        double sin = Math.sin(Math.toRadians(angle));
        return (initSpeed*time*sin)-((g*time*time)/2);
    }

    public ArrayList<Result>getResults(double angle, double initSpeed){
        double t = 0.1;
        double y = 0;
        double x = 0;
        ArrayList<Result> results = new ArrayList<Result>();
        results.add(new Result(0,0,0));
        x = calculateX(angle, initSpeed, t);
        y = calculateY(angle, initSpeed, t);
        if(y<0){
            return results;
        }
        results.add(new Result(x,y,t));
        while(y>0){
            x = calculateX(angle, initSpeed, t);
            y = calculateY(angle, initSpeed, t);
            if(y<0){
                break;
            }
            results.add(new Result(x,y,t));
            t+=0.1;
        }

        y = 0;
        x = calculateMaxDistance(angle, initSpeed);
        t = calculateTime(angle, initSpeed);

        results.add(new Result(x,y,t));

        return results;
    }

    public ArrayList<Result>checkResults(ArrayList<Result> results, double angle, double initSpeed){
        double y = 0;
        double x = calculateMaxDistance(angle, initSpeed);
        double t = calculateTime(angle, initSpeed);

        if(results.get(results.size()-1).getY()<0){
            results.remove(results.size()-1);
            results.add(new Result(x,y,t));
        }else{
            results.add(new Result(x,y,t));
        }
        return results;
    }

}

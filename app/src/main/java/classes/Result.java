package classes;

import java.io.Serializable;

public class Result implements Serializable {
    private double x;
    private double y;
    private double t;

    public Result(double x, double y, double t) {
        this.x = x;
        this.y = y;
        this.t = t;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getT() {
        return t;
    }
}

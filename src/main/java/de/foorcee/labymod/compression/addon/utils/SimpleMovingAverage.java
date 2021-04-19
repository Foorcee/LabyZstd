package de.foorcee.labymod.compression.addon.utils;

import java.util.LinkedList;
import java.util.Queue;

public class SimpleMovingAverage {

    private final Queue<Double> dataset = new LinkedList<>();
    private final int period;
    private double sum;

    public SimpleMovingAverage(int period) {
        this.period = period;
    }

    public void add(double num) {
        sum += num;
        dataset.add(num);

        if (dataset.size() > period) {
            sum -= dataset.remove();
        }
    }

    public double getMean() {
        return sum / Math.min(dataset.size(), period);
    }

    public double getSum() {
        return sum;
    }

    public void reset() {
        dataset.clear();
        sum = 0;
    }
}

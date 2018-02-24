package com.jary.eval.entity;

/**
 * @author Fantasy
 * @date 2018/2/24 21:04
 * @description 统计结果
 */
public class StatisticalResult {

    private double min;
    private double max;
    private double mean;
    private double std;
    private double avgtime;

    public StatisticalResult(){}

    public StatisticalResult(double min, double max, double mean, double std, double avgtime){
        this.min = min;
        this.max = max;
        this.mean = mean;
        this.std = std;
        this.avgtime = avgtime;
    }

    public double getMin() {
        return min;
    }

    public void setMin(double min) {
        this.min = min;
    }

    public double getMax() {
        return max;
    }

    public void setMax(double max) {
        this.max = max;
    }

    public double getMean() {
        return mean;
    }

    public void setMean(double mean) {
        this.mean = mean;
    }

    public double getStd() {
        return std;
    }

    public void setStd(double std) {
        this.std = std;
    }

    public double getAvgtime() {
        return avgtime;
    }

    public void setAvgtime(double avgtime) {
        this.avgtime = avgtime;
    }
}

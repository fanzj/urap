package com.jary.eval.entity;

import java.util.Arrays;

/**
 * Created by Fantasy on 2018/2/21.
 */
public class Solution implements Cloneable,Comparable<Solution>{

    private int id;//解的唯一标识

    private double[] content;//解的内容

    private int dimension;//解的维度

    private double value;//解的适应度值

    private int stages;//解的演化次数

    private double time;//耗时

    public Solution(){}

    public Solution(double[] content){
        this.content = content;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double[] getContent() {
        return content;
    }

    public void setContent(double[] content) {
        this.content = content;
    }

    public int getDimension() {
        return dimension;
    }

    public void setDimension(int dimension) {
        this.dimension = dimension;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public int getStages() {
        return stages;
    }

    public void setStages(int stages) {
        this.stages = stages;
    }

    public double getTime() {
        return time;
    }

    public void setTime(double time) {
        this.time = time;
    }

    @Override
    public Solution clone(){
        Solution solution = null;
        try{
            solution = (Solution) super.clone();
            solution.content = this.content.clone();
        }catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return solution;
    }

    public int compareTo(Solution o) {
        return Double.compare(this.value,o.getValue());
    }

    @Override
    public String toString() {
        return "Solution{" +
                "id=" + id +
                ", content=" + Arrays.toString(content) +
                ", dimension=" + dimension +
                ", value=" + value +
                ", stages=" + stages +
                ", time=" + time +
                '}';
    }


}

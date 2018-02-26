package com.jary.eval.entity;

/**
 * @author Fantasy
 * @date 2018/2/26 20:27
 * @description 一个水波，即一个解
 */
public class Wave extends Solution{

    //振幅
    public double amplitude;

    //波长
    public double length;

    public Wave(){
        super();
        this.length = 0.5;
    }

    public Wave(int[] content){
        super(content);
        this.length = 0.5;
    }

    @Override
    public String toString() {
        return "Wave{" +
                "amplitude=" + amplitude +
                ", length=" + length +
                "} " + super.toString();
    }
}

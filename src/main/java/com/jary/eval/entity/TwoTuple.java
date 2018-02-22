package com.jary.eval.entity;

/**
 * Created by Fantasy on 2018/2/21.
 * 定义二元组
 */
public class TwoTuple<A,B> {

    public final A first;
    public final B second;

    public TwoTuple(A first, B second){
        this.first = first;
        this.second = second;
    }

    @Override
    public String toString() {
        return "TwoTuple{" +
                "first=" + first +
                ", second=" + second +
                '}';
    }
}

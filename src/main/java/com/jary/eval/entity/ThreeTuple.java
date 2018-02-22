package com.jary.eval.entity;

/**
 * Created by Fantasy on 2018/2/21.
 * 定义三元组
 */
public class ThreeTuple<A,B,C> extends TwoTuple<A,B>{
    public final C third;

    public ThreeTuple(A first, B second, C third) {
        super(first, second);
        this.third = third;
    }

    @Override
    public String toString() {
        return "ThreeTuple{" +
                "third=" + third +
                '}';
    }
}

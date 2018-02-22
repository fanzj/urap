package com.jary.eval.entity;

/**
 * Created by Fantasy on 2018/2/22.
 */
public class FourTuple<A,B,C,D> extends ThreeTuple<A,B,C> {
    public final D forth;

    public FourTuple(A first, B second, C third, D forth) {
        super(first, second, third);
        this.forth = forth;
    }

    @Override
    public String toString() {
        return "FourTuple{" +
                "forth=" + forth +
                '}';
    }
}

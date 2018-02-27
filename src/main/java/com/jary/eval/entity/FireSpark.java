package com.jary.eval.entity;


/**
 * @author Fantasy
 * @date 2018/2/27 20:26
 * @description
 */
public class FireSpark extends Solution implements Cloneable {

    public double pi;//累积概率

    public FireSpark(){
        super();
    }

    public FireSpark(int[] content){
        super(content);
    }

    @Override
    public FireSpark clone(){
        FireSpark spark = null;
        spark = (FireSpark) super.clone();
        spark.content = this.content.clone();
        return spark;
    }

    @Override
    public String toString() {
        return "FireSpark{" +
                "pi=" + pi +
                "} " + super.toString();
    }
}

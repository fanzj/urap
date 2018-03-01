package com.jary.eval.entity;

/**
 * @author Fantasy
 * @date 2018/2/28 13:38
 * @description
 */
public class Particle extends Solution implements Cloneable {

    //速度
    public int[] velocity;

    //历史最优
    public Solution pBest;

    public Particle(){
        super();
        pBest = new Solution();
    }

    public Particle(int[] content){
        super(content);
        pBest = new Solution();
    }

    @Override
    public Particle clone(){
        Particle particle = null;
        particle = (Particle) super.clone();
        particle.content = this.content.clone();
        particle.velocity = this.velocity.clone();
        particle.pBest = this.pBest.clone();
        return particle;
    }

    @Override
    public String toString() {
        return "Particle{" +
                //"velocity=" + Arrays.toString(velocity) +
                ", pBest=" + pBest +
                super.toString() +
                "} ";
    }
}

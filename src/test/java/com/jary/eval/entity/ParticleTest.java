package com.jary.eval.entity;

import org.junit.Test;

/**
 * @author Fantasy
 * @date 2018/3/1 16:09
 * @description
 */
public class ParticleTest {

    @Test
    public void testParticleClone(){
        Particle p1 = new Particle();
        p1.setValue(2.45);
        p1.setDimension(3);
        p1.setContent(new int[]{3,4,2});
        Solution pbest1 = new Solution();
        pbest1.content = p1.content.clone();
        pbest1.value = p1.getValue();
        p1.pBest = pbest1;
        System.out.println(p1);

        Particle p2 = new Particle();
        p2.setValue(4.3);
        p2.setDimension(3);
        p2.setContent(new int[]{5,4,1});

        Particle p3 = (Particle) p1.clone();
        System.out.println(p3);

        Particle p4 = (Particle) p2.clone();
        System.out.println(p4);


    }
}

package com.jary.eval.entity;

import java.util.Map;

/**
 * @author Fantasy
 * @date 2018/2/24 18:24
 * @description
 */
public class RunConfig {

    private Map<Integer,String> instance;

    public Map<Integer, String> getInstance() {
        return instance;
    }

    public void setInstance(Map<Integer, String> instance) {
        this.instance = instance;
    }

}

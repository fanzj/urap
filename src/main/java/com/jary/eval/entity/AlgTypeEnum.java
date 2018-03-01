package com.jary.eval.entity;

/**
 * @author Fantasy
 * @date 2018/3/1 13:53
 * @description 算法类型
 */
public enum AlgTypeEnum {

    DE(1,"DE"),
    WWO(2,"WWO"),
    FADE(3,"FADE"),
    PSO(4,"PSO"),
    DNSPSO(5,"DNSPSO"),
    DEDNSPSO(6,"DEDNSPSO");

    private int code;
    private String desc;

    AlgTypeEnum(int code, String desc){
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}

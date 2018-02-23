package com.jary.eval.exception;

/**
 * Created by Fantasy on 2018/2/22.
 */
public class AlgException extends RuntimeException {

    private String message;

    public AlgException(){
        super();
    }

    public AlgException(String message){
        super(message);
        this.message = message;
    }
}

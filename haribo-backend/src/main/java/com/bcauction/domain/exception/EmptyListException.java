package com.bcauction.domain.exception;

/**
 * Created by g on 2017-06-12.
 */
public class EmptyListException extends RuntimeException {

    private static final long serialVersionUID = 6410744374489766116L;

    public EmptyListException(String msg){
        super(msg);
    }
}
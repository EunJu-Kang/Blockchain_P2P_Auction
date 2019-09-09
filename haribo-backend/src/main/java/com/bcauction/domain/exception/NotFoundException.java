package com.bcauction.domain.exception;

/**
 * Created by g on 2017-06-12.
 */
public class NotFoundException extends RuntimeException {
    private static final long serialVersionUID = 2157902426746147324L;

    public NotFoundException(String msg){
        super(msg);
    }
}
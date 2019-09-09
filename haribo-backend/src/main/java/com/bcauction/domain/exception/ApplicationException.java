package com.bcauction.domain.exception;

/**
 * Created by g on 2017-06-12.
 */
public class ApplicationException extends RuntimeException {
    static final long serialVersionUID = 1;

    public ApplicationException(Throwable throwable, String msg){
        super(msg, throwable);
    }

    public ApplicationException(String msg){
        super(msg);
    }

    public ApplicationException(Throwable throwable){
        super(throwable);
    }

}


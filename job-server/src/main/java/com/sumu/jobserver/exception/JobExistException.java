package com.sumu.jobserver.exception;

/**
 * @author 陈龙
 * @version 1.0
 * @date 2020-12-21 15:36
 */
public class JobExistException extends RuntimeException {

    public JobExistException(String message) {
        super(message);
    }


}

package org.seckill.exception;

/**
 * 秒杀相关业务异常
 * Created by LSM on 2017/11/22.
 */
public class SeckillException extends Exception {

    public SeckillException(String message) {
        super(message);
    }

    public SeckillException(String message, Throwable cause) {
        super(message, cause);
    }

}

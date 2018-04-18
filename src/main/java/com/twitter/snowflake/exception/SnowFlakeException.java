package com.twitter.snowflake.exception;

/**
 * SnowFlakeException
 *
 * @author svili
 */
public class SnowFlakeException extends RuntimeException {

    /**
     * Serial Version UID
     */
    private static final long serialVersionUID = 861134492266247879L;

    /**
     * Default constructor
     */
    public SnowFlakeException() {
        super();
    }

    /**
     * Constructor with message & cause
     */
    public SnowFlakeException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructor with message
     */
    public SnowFlakeException(String message) {
        super(message);
    }

    /**
     * Constructor with message format
     */
    public SnowFlakeException(String msgFormat, Object... args) {
        super(String.format(msgFormat, args));
    }

    /**
     * Constructor with cause
     */
    public SnowFlakeException(Throwable cause) {
        super(cause);
    }

}

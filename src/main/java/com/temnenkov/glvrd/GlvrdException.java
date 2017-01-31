package com.temnenkov.glvrd;

public class GlvrdException extends RuntimeException {

    public GlvrdException() {
        super();
    }

    public GlvrdException(String message) {
        super(message);
    }

    public GlvrdException(String message, Throwable cause) {
        super(message, cause);
    }

    public GlvrdException(Throwable cause) {
        super(cause);
    }
}

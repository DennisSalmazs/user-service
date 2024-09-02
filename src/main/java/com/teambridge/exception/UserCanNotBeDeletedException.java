package com.teambridge.exception;

public class UserCanNotBeDeletedException extends RuntimeException {

    public UserCanNotBeDeletedException(String message) {
        super(message);
    }

}

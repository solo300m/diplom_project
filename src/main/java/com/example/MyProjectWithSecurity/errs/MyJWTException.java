package com.example.MyProjectWithSecurity.errs;

public class MyJWTException extends Exception {

    private final  String message;

    public MyJWTException(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return message;
    }
}

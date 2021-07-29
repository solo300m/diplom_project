package com.example.MyProjectWithSecurity.errs;

public class EmptySearchException extends Exception {
    public EmptySearchException(String messages) {
        super(messages);
    }
}

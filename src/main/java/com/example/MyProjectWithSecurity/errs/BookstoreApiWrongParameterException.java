package com.example.MyProjectWithSecurity.errs;

public class BookstoreApiWrongParameterException extends Exception {
    public BookstoreApiWrongParameterException(String message) {
        super(message);
    }
}

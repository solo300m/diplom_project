package com.example.MyProjectWithSecurity.security.data;

public class ContactConfirmationPayload {
    private String contact;
    private String code;

    public ContactConfirmationPayload() {
    }

    public ContactConfirmationPayload(String contact, String code) {
        this.contact = contact;
        this.code = code;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}

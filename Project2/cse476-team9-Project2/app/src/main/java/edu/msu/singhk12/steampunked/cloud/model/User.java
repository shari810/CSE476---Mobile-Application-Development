package edu.msu.singhk12.steampunked.cloud.model;

import androidx.annotation.Nullable;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

@Root(name = "user")
public final class User {
    @Attribute
    private String status;

    @Attribute(name = "msg", required = false)
    private String message;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public User(String status, @Nullable String msg) {
        this.status = status;
        this.message = msg;
    }

    public User() {
    }
}

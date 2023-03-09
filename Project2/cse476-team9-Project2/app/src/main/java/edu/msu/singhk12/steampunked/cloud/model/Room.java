package edu.msu.singhk12.steampunked.cloud.model;

import androidx.annotation.Nullable;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.ArrayList;
import java.util.List;

@Root(name = "room")
public class Room {
    @Attribute(name = "status")
    private String status;

    @ElementList(name = "user", inline = true, required = false, type = User.class)
    private List<User> users;


    @Attribute(name = "msg", required = false)
    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Room() {}

    public Room(String status, ArrayList<User> users, @Nullable String msg) {
        this.status = status;
        this.message = msg;
        this.users = users;
    }
}

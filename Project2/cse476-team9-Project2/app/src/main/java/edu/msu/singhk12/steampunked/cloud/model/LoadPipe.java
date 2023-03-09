package edu.msu.singhk12.steampunked.cloud.model;
import androidx.annotation.Nullable;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.ArrayList;
import java.util.List;

@Root(name = "pipes")
public class LoadPipe {
    @Attribute
    private String status;

    @ElementList(name = "pipe", inline = true, required = false, type = Pipe.class)
    private List<Pipe> items;

    @Attribute(name = "msg", required = false)
    private String message;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Pipe> getItems() {
        return items;
    }

    public void setItems(List<Pipe> items) {
        this.items = items;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LoadPipe(String status, ArrayList<Pipe> items, @Nullable String msg) {
        this.status = status;
        this.items = items;
        this.message = msg;
    }

    public LoadPipe() {
    }
}


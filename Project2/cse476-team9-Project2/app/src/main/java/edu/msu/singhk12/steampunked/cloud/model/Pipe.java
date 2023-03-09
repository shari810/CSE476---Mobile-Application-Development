package edu.msu.singhk12.steampunked.cloud.model;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

@Root(name = "pipe")
public class Pipe {
    @Attribute
    private String connect0;

    public String getConnect0() {
        return connect0;
    }

    public void setConnect0(String name) {
        this.connect0 = name;
    }

    @Attribute
    private String connect1;

    public String getConnect1() {
        return connect1;
    }

    public void setConnect1(String name) {
        this.connect1 = name;
    }

    @Attribute
    private String connect2;

    public String getConnect2() {
        return connect2;
    }

    public void setConnect2(String name) {
        this.connect2 = name;
    }

    @Attribute
    private String connect3;

    public String getConnect3() {
        return connect3;
    }

    public void setConnect3(String name) {
        this.connect3 = name;
    }

    @Attribute
    private String originconnect0;

    public String getOriginconnect0() {
        return originconnect0;
    }

    public void setOriginconnect0(String name) {
        this.originconnect0 = name;
    }

    @Attribute
    private String originconnect1;

    public String getOriginconnect1() {
        return originconnect1;
    }

    public void setOriginconnect1(String name) {
        this.originconnect1 = name;
    }
    @Attribute
    private String originconnect2;

    public String getOriginconnect2() {
        return originconnect2;
    }

    public void setOriginconnect2(String name) {
        this.originconnect2 = name;
    }
    @Attribute
    private String originconnect3;

    public String getOriginconnect3() {
        return originconnect3;
    }

    public void setOriginconnect3(String name) {
        this.originconnect3 = name;
    }

    @Attribute
    private String x;

    public String getX() {
        return x;
    }

    public void setX(String x) {
        this.x = x;
    }

    @Attribute
    private String y;

    public String getY() {
        return y;
    }

    public void setY(String y) {
        this.y = y;
    }

    @Attribute
    private String rotation;

    public String getRotation() {
        return rotation;
    }

    public void setRotation(String angle) {
        this.rotation = angle;
    }

    @Attribute
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Attribute
    private String player;

    public String getPlayer() {
        return player;
    }

    public void setPlayer(String id) {
        this.player = id;
    }

    public Pipe() {}

    public Pipe(String id, String x, String y, String rotation, String connect0, String connect1, String connect2, String connect3, String originconnect0, String originconnect1, String originconnect2, String originconnect3, String player) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.rotation = rotation;
        this.connect0 = connect0;
        this.connect1 = connect1;
        this.connect2 = connect2;
        this.connect3 = connect3;
        this.originconnect0 = originconnect0;
        this.originconnect1 = originconnect1;
        this.originconnect2 = originconnect2;
        this.originconnect3 = originconnect3;
        this.player = player;
    }
}
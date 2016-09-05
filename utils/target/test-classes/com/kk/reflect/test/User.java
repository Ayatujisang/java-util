package com.kk.reflect.test;

import java.util.Date;

public class User {
    private int id;
    private String name;
    private double height;
    private Date bitth;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public Date getBitth() {
        return bitth;
    }

    public void setBitth(Date bitth) {
        this.bitth = bitth;
    }
}

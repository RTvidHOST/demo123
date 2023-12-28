package com.example.demo123;

public class service {
    String model, info;
    public service(String model, String info) {
        this.model = model;
        this.info = info;
    }
    public String getModel() {
        return model;
    }
    public void setModel(String model) {
        this.model = model;
    }
    public String getInfo() {
        return info;
    }
    public void setInfo(String info) {
        this.info = info;
    }
}
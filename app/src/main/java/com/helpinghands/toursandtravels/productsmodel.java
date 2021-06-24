package com.helpinghands.toursandtravels;

public class productsmodel {

    private String duration;
    private String location;
    private String money;

    public productsmodel(String location, String money, String duration) {
        this.location = location;
        this.money = money;
        this.duration = duration;
    }

    private productsmodel() {
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

}

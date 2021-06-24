package com.helpinghands.toursandtravels;

public class productsModelForContactUs {


    private String UserName;
    private String contact;

    public productsModelForContactUs(String name, String number) {
        this.UserName = name;
        this.contact = number;
    }

    public productsModelForContactUs() {
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }


}

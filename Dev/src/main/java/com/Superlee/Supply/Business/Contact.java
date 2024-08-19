package com.Superlee.Supply.Business;


public class Contact {

    private String name;
    private String phoneNumber;
    private final int contactId;

    public Contact(String name, String phoneNumber, int contactId) {
        this.contactId = contactId;
        this.name = name;
        this.phoneNumber = phoneNumber;
    }

    public String getName() {
        return name;
    }

    public int getContactId() {
        return contactId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String toString(){
        return "id - " + contactId + ", name - " + name + ", phone number - " + phoneNumber;
    }
}

package com.example.gustavo.techsoybean.model;

public class User {
    private String id;
    private String name;
    private String address;
    private String mail;

    public User(){
        //DEFAULT CONSTRUCTOR
    }

    public User(String id, String name, String address, String mail){
        this.id = id;
        this.name = name;
        this.address = address;
        this.mail = mail;
    }

    public String getId() {
        return id;
    }
    public String getName(){return name;}
    public String getAddress(){return address;}
    public String getEmail(){return mail;}

}

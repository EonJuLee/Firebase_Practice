package com.example.dbapp02;

public class User {
    private String ID,PW;

    public User(){

    }

    public User(String ID,String PW){
        this.ID=ID;
        this.PW=PW;
    }

    public String getPW() {
        return PW;
    }

    public void setPW(String PW) {
        this.PW = PW;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }
}

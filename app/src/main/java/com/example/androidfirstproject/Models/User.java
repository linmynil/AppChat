package com.example.androidfirstproject.Models;

import java.io.Serializable;
import java.util.ArrayList;

public class User implements Serializable {
    String phoneNumber, fullName, picture, id;
    ArrayList<String> phoneBook;

    public User(String fullName, ArrayList<String> phoneBook, String phoneNumber, String picture, String id) {
        this.phoneNumber = phoneNumber;
        this.fullName = fullName;
        this.picture = picture;
        this.id = id;
        this.phoneBook = phoneBook;
    }

    public User(String phoneNumber, String fullName, String picture) {
        this.phoneNumber = phoneNumber;
        this.fullName = fullName;
        this.picture = picture;
    }
    public ArrayList<String> getPhoneBook() {
        return phoneBook;
    }

    public void setPhoneBook(ArrayList<String> phoneBook) {
        this.phoneBook = phoneBook;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public User(String fullName) {
        this.fullName = fullName;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public User() {
    }

}

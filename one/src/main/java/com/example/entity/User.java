package com.example.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;
    String userid;
    String username;
    String password;
    String usercategory;
    int groupno;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsercategory() {
        return usercategory;
    }

    public void setUsercategory(String usercategory) {
        this.usercategory = usercategory;
    }

    public int getGroupno() {
        return groupno;
    }

    public void setGroupno(int groupno) {
        this.groupno = groupno;
    }
}

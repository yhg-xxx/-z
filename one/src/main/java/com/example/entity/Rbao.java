package com.example.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "rbao")
public class Rbao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;
    String publishdate;
    String stuno;
    String name;
    String spenttime;
    String codeamount;
    String blogsnumber;
    String knowlegepoints;
    String state;
    String dailyscore;
    String statereason;



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPublishdate() {
        return publishdate;
    }

    public void setPublishdate(String publishdate) {
        this.publishdate = publishdate;
    }

    public String getStuno() {
        return stuno;
    }

    public void setStuno(String stuno) {
        this.stuno = stuno;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSpenttime() {
        return spenttime;
    }

    public void setSpenttime(String spenttime) {
        this.spenttime = spenttime;
    }

    public String getCodeamount() {
        return codeamount;
    }

    public void setCodeamount(String codeamount) {
        this.codeamount = codeamount;
    }

    public String getBlogsnumber() {
        return blogsnumber;
    }

    public void setBlogsnumber(String blogsnumber) {
        this.blogsnumber = blogsnumber;
    }

    public String getKnowlegepoints() {
        return knowlegepoints;
    }

    public void setKnowlegepoints(String knowlegepoints) {
        this.knowlegepoints = knowlegepoints;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getDailyscore() {
        return dailyscore;
    }

    public void setDailyscore(String dailyscore) {
        this.dailyscore = dailyscore;
    }

    public String getStatereason() {
        return statereason;
    }

    public void setStatereason(String statereason) {
        this.statereason = statereason;
    }
}

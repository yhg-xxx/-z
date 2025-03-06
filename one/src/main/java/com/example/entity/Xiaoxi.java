package com.example.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "xiaoxi")
public class Xiaoxi {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;
    String category;
    String sendername;
    String senderno;
    String receivername;
    String receiverno;
    String content;
    String state;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSendername() {
        return sendername;
    }

    public void setSendername(String sendername) {
        this.sendername = sendername;
    }

    public String getSenderno() {
        return senderno;
    }

    public void setSenderno(String senderno) {
        this.senderno = senderno;
    }

    public String getReceivername() {
        return receivername;
    }

    public void setReceivername(String receivername) {
        this.receivername = receivername;
    }

    public String getReceiverno() {
        return receiverno;
    }

    public void setReceiverno(String receiverno) {
        this.receiverno = receiverno;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}

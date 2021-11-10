package com.example.Taupyk.lt.Models;

import com.sun.istack.NotNull;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Table
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long UId;

    @NotNull
    private String text;

    @ManyToOne
    @JoinColumn(name="User_UID", referencedColumnName = "UId")
    private CustomUser user;

    @ManyToOne
    @JoinColumn(name="Product_UID", referencedColumnName = "UId")
    private Product product;

    private long timeStamp;

    public Comment()
    {

    }
    public long getTimeStampTime()
    {
        Instant instant = Instant.now();
        return instant.getEpochSecond();
    }

    public Product getProduct() {
        return product;
    }

    public long getUId() {
        return UId;
    }

    public CustomUser getUser() {
        return user;
    }

    public String getText() {
        return text;
    }

    public void setProduct(Product market) {
        this.product = market;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setUser(CustomUser customUser) {
        this.user = customUser;
    }

    public void setUId(long UId) {
        this.UId = UId;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public long getTimeStamp() {
        return timeStamp;
    }
}

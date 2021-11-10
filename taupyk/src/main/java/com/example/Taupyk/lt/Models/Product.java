package com.example.Taupyk.lt.Models;


import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "Products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long UId;


    private String name;
    private double price;

    @ManyToOne(cascade = {CascadeType.REMOVE})
    @JoinColumn(name = "market_uid", nullable = false)
    private Market market;


    public Product()
    {

    }
    public Product(String name, double price)
    {
        this.name = name;
        this.price = price;
    }

    public Product(String name, double price, Market market) {
        this.name = name;
        this.price = price;
        this.market = market;
    }
    public String getName()
    {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public Market getMarket() {
        return market;
    }

    public long getUId() {return UId;}

    public void setUId(long UId) {
        this.UId = UId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setMarket(Market market) {
        this.market = market;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}

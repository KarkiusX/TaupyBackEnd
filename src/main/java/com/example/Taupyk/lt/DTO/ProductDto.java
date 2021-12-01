package com.example.Taupyk.lt.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotBlank;

public class ProductDto {

    @NotBlank(message = "Name is mandatory")
    @JsonProperty("name")
    private String name;

    @NotBlank(message = "Price is mandatory")
    @JsonProperty("price")
    private double price;

    public String getProductLink() {
        return productLink;
    }

    public void setProductLink(String productLink) {
        this.productLink = productLink;
    }

    private String productLink;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}

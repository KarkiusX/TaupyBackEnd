package com.example.Taupyk.lt.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sun.istack.NotNull;

import javax.validation.constraints.NotBlank;


public class MarketDto {

    @NotBlank(message = "Name is mandatory")
    @JsonProperty("name")
    private String name;

    @NotBlank(message = "Icon name is mandatory")
    @JsonProperty("iconName")
    private String iconName;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIconName() {
        return iconName;
    }

    public void setIconName(String iconName) {
        this.iconName = iconName;
    }

}

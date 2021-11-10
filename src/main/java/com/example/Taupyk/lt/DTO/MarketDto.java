package com.example.Taupyk.lt.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sun.istack.NotNull;

import javax.validation.constraints.NotBlank;


public class MarketDto {

    @JsonProperty("uid")
    private long UId;

    @NotBlank(message = "Name is mandatory")
    @JsonProperty("name")
    private String name;

    @NotBlank(message = "Icon name is mandatory")
    @JsonProperty("iconName")
    private String iconName;

    public long getUId() {
        return UId;
    }

    public void setUId(long UId) {
        this.UId = UId;
    }

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

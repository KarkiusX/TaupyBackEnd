package com.example.Taupyk.lt.Models;

import com.sun.istack.NotNull;
import jdk.vm.ci.code.site.Mark;
import org.springframework.validation.annotation.Validated;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Entity
public class Market {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long UId;

    private String name;
    private String iconName;

    @OneToMany(mappedBy = "market", cascade = {CascadeType.REMOVE})
    private List<Product> products;


    public Market(String name, String iconName)
    {
        this.name = name;
        this.iconName = iconName;
    }
    public Market()
    {

    }
    @Override
    public String toString()
    {
        return name + "" + iconName + "" + UId;
    }

    public String getName() {
        return name;
    }

    public String getIconName() {
        return iconName;
    }

    public long getUId() {
        return UId;
    }

    public void setUId(long UId) {
        this.UId = UId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setIconName(String iconName) {
        this.iconName = iconName;
    }


}
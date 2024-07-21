package com.example.shoesapp;

public class ProductModel {

    String name;
    String price;
    String imgurl;
    String description;

    String pid;

    public ProductModel() {
    }

    public ProductModel(String name, String price, String imgurl, String description, String pid) {
        this.name = name;
        this.price = price;
        this.imgurl = imgurl;
        this.description = description;
        this.pid = pid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }
}

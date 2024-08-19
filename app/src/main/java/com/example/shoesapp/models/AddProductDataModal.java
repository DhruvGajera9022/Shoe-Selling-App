package com.example.shoesapp.models;

public class AddProductDataModal {

    String name, image, price, description, date, time, key;

    public AddProductDataModal() {
    }

    public AddProductDataModal(String name, String image, String price, String description, String date, String time, String key) {
        this.name = name;
        this.image = image;
        this.price = price;
        this.description = description;
        this.date = date;
        this.time = time;
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}

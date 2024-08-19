package com.example.shoesapp.models;

public class OrderModel {
    String productName, productPrice, productSize, productImage, productDescription, oid, userName, email, number, address, date;

    public OrderModel() {
    }

    public OrderModel(String productName, String productPrice, String productSize, String productImage, String oid, String productDescription, String userName, String email, String number, String address, String date) {
        this.productName = productName;
        this.productPrice = productPrice;
        this.productSize = productSize;
        this.productImage = productImage;
        this.oid = oid;
        this.userName = userName;
        this.productDescription = productDescription;
        this.email = email;
        this.number = number;
        this.address = address;
        this.date = date;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }

    public String getProductSize() {
        return productSize;
    }

    public void setProductSize(String productSize) {
        this.productSize = productSize;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public String getOid() {
        return oid;
    }

    public void setOid(String oid) {
        this.oid = oid;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getdate() {
        return date;
    }

    public void setdate(String date) {
        this.date = date;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }
}

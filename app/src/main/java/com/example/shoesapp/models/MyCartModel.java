package com.example.shoesapp.models;

public class MyCartModel {

    String productImage;
    String productName;
    String productPrice;
    String productDescription;
    String productSize;
    String uid;
    String oid;
    int totalPrice;

    public MyCartModel() {
    }

    public MyCartModel(String productImage, String productName, String productPrice, String productDescription, int totalPrice, String productSize, String uid, String oid) {
        this.productImage = productImage;
        this.productName = productName;
        this.productPrice = productPrice;
        this.productDescription = productDescription;
        this.totalPrice = totalPrice;
        this.productSize = productSize;
        this.uid = uid;
        this.oid = oid;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
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

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getProductSize() {
        return productSize;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String pid) {
        this.uid = pid;
    }

    public void setProductSize(String productSize) {
        this.productSize = productSize;
    }

    public String getOid() {
        return oid;
    }

    public void setOid(String oid) {
        this.oid = oid;
    }
}

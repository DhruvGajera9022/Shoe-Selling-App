package com.example.shoesapp.models;

public class MyCartModel {

    String productImage;
    String productName;
    String productPrice;
    String productDescription;
    String productSize;
    String totalQty;
    String totalAmount;
    String productCompany;
    String uid;
    String oid;
    int totalPrice;

    public MyCartModel() {
    }

    public MyCartModel(String productImage, String productName, String productPrice, String productDescription, int totalPrice, String productCompany, String productSize, String totalQty, String totalAmount, String uid, String oid) {
        this.productImage = productImage;
        this.productName = productName;
        this.productPrice = productPrice;
        this.productDescription = productDescription;
        this.totalPrice = totalPrice;
        this.productSize = productSize;
        this.productCompany = productCompany;
        this.totalQty = totalQty;
        this.totalAmount = totalAmount;
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

    public String getTotalQty() {
        return totalQty;
    }

    public void setTotalQty(String totalQty) {
        this.totalQty = totalQty;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getProductCompany() {
        return productCompany;
    }

    public void setProductCompany(String productCompany) {
        this.productCompany = productCompany;
    }
}

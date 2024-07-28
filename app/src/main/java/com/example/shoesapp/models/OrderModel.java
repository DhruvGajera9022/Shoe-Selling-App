package com.example.shoesapp.models;

public class OrderModel {
    String orderProductName, orderProductPrice, orderProductSize, currentOrderDate, orderProductImage, currentUserId;

    public OrderModel() {
    }

    public OrderModel(String orderProductName, String orderProductPrice, String orderProductSize, String currentOrderDate, String orderProductImage, String currentUserId) {
        this.orderProductName = orderProductName;
        this.orderProductPrice = orderProductPrice;
        this.orderProductSize = orderProductSize;
        this.currentOrderDate = currentOrderDate;
        this.orderProductImage = orderProductImage;
        this.currentUserId = currentUserId;
    }

    public String getOrderProductName() {
        return orderProductName;
    }

    public void setOrderProductName(String orderProductName) {
        this.orderProductName = orderProductName;
    }

    public String getOrderProductPrice() {
        return orderProductPrice;
    }

    public void setOrderProductPrice(String orderProductPrice) {
        this.orderProductPrice = orderProductPrice;
    }

    public String getOrderProductSize() {
        return orderProductSize;
    }

    public void setOrderProductSize(String orderProductSize) {
        this.orderProductSize = orderProductSize;
    }

    public String getCurrentOrderDate() {
        return currentOrderDate;
    }

    public void setCurrentOrderDate(String currentOrderDate) {
        this.currentOrderDate = currentOrderDate;
    }

    public String getOrderProductImage() {
        return orderProductImage;
    }

    public void setOrderProductImage(String orderProductImage) {
        this.orderProductImage = orderProductImage;
    }

    public String getCurrentUserId() {
        return currentUserId;
    }

    public void setCurrentUserId(String currentUserId) {
        this.currentUserId = currentUserId;
    }
}

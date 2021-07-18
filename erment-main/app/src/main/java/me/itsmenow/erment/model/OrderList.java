package me.itsmenow.erment.model;

import java.util.List;

import me.itsmenow.erment.util.Util;

public class OrderList {
    private int id;
    private String buyerName;
    private String buyerAddress;
    private String buyerNumber;
    private List<PayItem> payItemList;
    private int totalPrice;

    public OrderList(int id, String buyerName, String buyerAddress, String buyerNumber, int totalPrice, List<PayItem> payItemList){
        this.id = id;
        this.buyerName = buyerName;
        this.buyerAddress = buyerAddress;
        this.buyerNumber = buyerNumber;
        this.totalPrice = totalPrice;
        this.payItemList = payItemList;
    }

    public OrderList(String buyerName, String buyerAddress, String buyerNumber, int totalPrice, List<PayItem> payItemList){
        this.buyerName = buyerName;
        this.buyerAddress = buyerAddress;
        this.buyerNumber = buyerNumber;
        this.totalPrice = totalPrice;
        this.payItemList = payItemList;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBuyerName() {
        return buyerName;
    }

    public void setBuyerName(String buyerName) {
        this.buyerName = buyerName;
    }

    public String getBuyerAddress() {
        return buyerAddress;
    }

    public void setBuyerAddress(String buyerAddress) {
        this.buyerAddress = buyerAddress;
    }

    public String getBuyerNumber() {
        return buyerNumber;
    }

    public void setBuyerNumber(String buyerNumber) {
        this.buyerNumber = buyerNumber;
    }

    public List<PayItem> getPayItemList() {
        return payItemList;
    }

    public void setPayItemList(List<PayItem> payItemList) {
        this.payItemList = payItemList;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getFormattedTotalPrice() {
        return Util.formattedMoney(getTotalPrice());
    }
}

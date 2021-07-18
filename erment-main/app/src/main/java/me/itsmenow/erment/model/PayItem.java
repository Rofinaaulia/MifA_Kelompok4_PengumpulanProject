package me.itsmenow.erment.model;

import android.os.Parcel;
import android.os.Parcelable;

import me.itsmenow.erment.util.Util;

public class PayItem implements Parcelable {
    private final int id;
    private final String productID;
    private final String name;
    private int quantity;
    private int price;

    public PayItem(int id, String productID, String name, int quantity, int price){
        this.id = id;
        this.productID = productID;
        this.name = name;
        this.quantity = quantity;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public String getProductID() {
        return productID;
    }

    public String getName() {
        return name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void addQuantity(int quantityAdd) {
        this.quantity = this.quantity + quantityAdd;
    }

    public int getPrice() {
        return price;
    }

    public String getFormattedPrice() {
        return Util.formattedMoney(getPrice());
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getTotalPrice() {
        return quantity*price;
    }

    public String getFormattedTotalPrice() {
        return Util.formattedMoney(getTotalPrice());
    }

    protected PayItem(Parcel in) {
        id = in.readInt();
        productID = in.readString();
        name = in.readString();
        quantity = in.readInt();
        price = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(productID);
        dest.writeString(name);
        dest.writeInt(quantity);
        dest.writeInt(price);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<PayItem> CREATOR = new Parcelable.Creator<PayItem>() {
        @Override
        public PayItem createFromParcel(Parcel in) {
            return new PayItem(in);
        }

        @Override
        public PayItem[] newArray(int size) {
            return new PayItem[size];
        }
    };


}
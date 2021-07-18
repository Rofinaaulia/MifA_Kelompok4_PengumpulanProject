package me.itsmenow.erment.model;


import android.graphics.drawable.Drawable;

import me.itsmenow.erment.util.Util;

public class Product {

    private final int id;
    private final String productID;
    private final String productName;
    private final Drawable productImage;
    private final int productPrice;

    public Product(int id, String productID, String productName, Drawable productImage, int productPrice) {
        this.id = id;
        this.productID = productID;
        this.productName = productName;
        this.productImage = productImage;
        this.productPrice = productPrice;
    }

    public int getID() {
        return id;
    }

    public String getProductID() {
        return productID;
    }

    public String getProductName() {
        return productName;
    }

    public Drawable getProductImage() {
        return productImage;
    }

    public int getProductPrice() {
        return productPrice;
    }

    public String getProductFormattedPrice() {
        return Util.formattedMoney(getProductPrice());
    }


}

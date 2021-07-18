package me.itsmenow.erment.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;
import java.util.Locale;

import me.itsmenow.erment.R;
import me.itsmenow.erment.model.Product;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder>{

    private final List<Product> mProductList;
    private final OnProductClickListener mOnProductClickListener;

    public ProductAdapter(List<Product> mProductList, OnProductClickListener onProductClickListener) {
        this.mProductList = mProductList;
        this.mOnProductClickListener = onProductClickListener;
    }

    public interface OnProductClickListener{
        void onProductClick(int position);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView textProductName;
        TextView textProductPrice;
        ImageView imageProduct;
        Button buttonBuy;
        OnProductClickListener onProductClickListener;
        public ViewHolder(View itemView, OnProductClickListener onProductClickListener){
            super(itemView);
            textProductName = itemView.findViewById(R.id.text_product_name);
            textProductPrice = itemView.findViewById(R.id.text_product_price);
            imageProduct = itemView.findViewById(R.id.image_product);
            buttonBuy = itemView.findViewById(R.id.button_buy);
            this.onProductClickListener = onProductClickListener;
            buttonBuy.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onProductClickListener.onProductClick(getAdapterPosition());
        }
    }

    @NonNull
    @Override
    public ProductAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        // Inflate the custom layout
        View userView = inflater.inflate(R.layout.item_product, parent, false);
        // Return a new holder instance
        return new ViewHolder(userView, mOnProductClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductAdapter.ViewHolder holder, int position) {
        // Get the data model based on position
        Product product = mProductList.get(position);
        // Set item views based on your views and data model
        TextView productName = holder.textProductName;
        productName.setText(product.getProductName());
        TextView productPrice = holder.textProductPrice;
        productPrice.setText(String.format(Locale.getDefault(),"Harga : %s / Kg", product.getProductFormattedPrice()));
        ImageView imageView = holder.imageProduct;
        Glide.with(holder.itemView.getContext())
                .load(product.getProductImage())
                .fitCenter()
                .into(imageView);
    }

    @Override
    public int getItemCount() {
        return mProductList.size();
    }
}

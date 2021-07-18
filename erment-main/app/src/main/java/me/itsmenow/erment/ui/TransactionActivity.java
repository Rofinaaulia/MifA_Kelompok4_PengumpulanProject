package me.itsmenow.erment.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import me.itsmenow.erment.R;
import me.itsmenow.erment.adapter.ProductAdapter;
import me.itsmenow.erment.model.PayItem;
import me.itsmenow.erment.model.Product;
import me.itsmenow.erment.util.Util;

public class TransactionActivity extends AppCompatActivity implements ProductAdapter.OnProductClickListener {

    private List<Product> mProductList;
    private List<PayItem> mPayItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction);
        findViewById(R.id.button_back).setOnClickListener(view -> onBackPressed());
        mPayItems = new ArrayList<>();
        Button buttonPay = findViewById(R.id.button_pay);
        Product product1 = new Product(0, "BR005", getString(R.string.product_1), AppCompatResources.getDrawable(this, R.drawable.product_kuning), 10000); // Membuat objek produk
        Product product2 = new Product(1, "BR006", getString(R.string.product_2), AppCompatResources.getDrawable(this, R.drawable.product_sayur), 10000);
        Product product3 = new Product(2, "BR007", getString(R.string.product_3), AppCompatResources.getDrawable(this, R.drawable.product_ketumbar), 10000);
        RecyclerView mProductRecycler = findViewById(R.id.recycler_product);
        mProductList = new ArrayList<>(Arrays.asList(product1, product2, product3));
        ProductAdapter mProductAdapter = new ProductAdapter(mProductList, this);
        mProductRecycler.setAdapter(mProductAdapter); // Mengatur produk
        mProductRecycler.setLayoutManager(new LinearLayoutManager(this));
        mProductAdapter.notifyDataSetChanged();
        buttonPay.setOnClickListener(view -> payNow()); // Tombol bayar sekarang listener
    }

    @Override
    public void onProductClick(int position) {
        Product product = mProductList.get(position);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);  // Membuat dialog
        View inputView = LayoutInflater.from(this).inflate(R.layout.layout_buy_item, null);
        TextView textViewTitle = inputView.findViewById(R.id.pay_dialog_title);
        TextInputLayout textInputQuantity = inputView.findViewById(R.id.pay_dialog_input_quantity);
        TextInputLayout textInputPrice = inputView.findViewById(R.id.pay_dialog_input_price);
        TextInputLayout textInputTotal = inputView.findViewById(R.id.pay_dialog_input_total);
        textViewTitle.setText(product.getProductName());
        textInputPrice.getEditText().setText(String.valueOf(product.getProductPrice()));
        textInputTotal.getEditText().setText(Util.formattedMoney(0));
        textInputQuantity.getEditText().addTextChangedListener(new TextWatcher() { // Listener total price pada dialog beli
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!textInputQuantity.getEditText().getText().toString().equals("")){
                    textInputTotal.getEditText().setText(Util.formattedMoney(Integer.parseInt(textInputQuantity.getEditText().getText().toString())*product.getProductPrice()));
                } else {
                    textInputTotal.getEditText().setText(Util.formattedMoney(0));
                }
            }
            @Override
            public void afterTextChanged(Editable editable) { }
        });
        for (int l = 0; l < mPayItems.size(); l++){ // Check apakah barang sudah di keranjang
            if (mPayItems.get(l).getId() == product.getID()){
                textInputQuantity.getEditText().setText(String.valueOf(mPayItems.get(l).getQuantity()));
                break;
            }
        }
        builder.setView(inputView);
        builder.setPositiveButton("Tambahkan", (dialogInterface, i) -> {
            dialogInterface.dismiss();
            if (textInputQuantity.getEditText().getText().toString().equals("0") || textInputQuantity.getEditText().getText().toString().equals("")){
                return;
            }
            PayItem itemBuy = new PayItem(product.getID(), product.getProductID(),product.getProductName(),Integer.parseInt(textInputQuantity.getEditText().getText().toString()),product.getProductPrice());
            for (int l = 0; l < mPayItems.size(); l++){
                if (mPayItems.get(l).getId() == product.getID()){
                    textInputQuantity.getEditText().setText(String.valueOf(mPayItems.get(l).getQuantity()));
                    mPayItems.set(l,itemBuy);
                    return;
                }
            }
            mPayItems.add(itemBuy);
        });
        AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.background_dialog_pay);
        dialog.show();
        dialog.getButton(dialog.BUTTON_POSITIVE).setTextColor(getColor(R.color.brown_0)); // Menampilkan dialog
    }


    private void payNow(){ // Bayar sekarang
        if (mPayItems.size()!=0){
            Intent intent = new Intent(this, PayActivity.class);
            intent.putParcelableArrayListExtra("data", (ArrayList<? extends Parcelable>) mPayItems);
            startActivity(intent);
        }
        Snackbar.make(findViewById(R.id.button_pay), "Please add product.", Snackbar.LENGTH_SHORT);
    }
}
package me.itsmenow.erment.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.itsmenow.erment.R;
import me.itsmenow.erment.adapter.PayListAdapter;
import me.itsmenow.erment.model.OrderList;
import me.itsmenow.erment.model.PayItem;
import me.itsmenow.erment.util.ProgressDialog;
import me.itsmenow.erment.util.SharedPrefManager;
import me.itsmenow.erment.util.Util;

public class PayActivity extends AppCompatActivity {

    private List<PayItem> mPayItems; // Array yang di beli
    private int totalPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);
        findViewById(R.id.button_back).setOnClickListener(view -> onBackPressed());
        Intent intent = getIntent();
        Button buttonPay = findViewById(R.id.button_pay);
        buttonPay.setOnClickListener(view -> payItem());
        mPayItems = intent.getParcelableArrayListExtra("data"); // Mengambil data extra dari activity sebelumnya
        TextInputLayout textInputTotalPrice = findViewById(R.id.pay_input_total);
        RecyclerView payListRecycler = findViewById(R.id.pay_list);
        PayListAdapter payListAdapter = new PayListAdapter(mPayItems, position -> { });
        payListRecycler.setAdapter(payListAdapter); // Mengatur produk yang di beli pada Recycler View
        payListRecycler.setLayoutManager(new LinearLayoutManager(this));
        payListAdapter.notifyDataSetChanged();
        totalPrice = 0;
        for (int l = 0; l < mPayItems.size(); l++){ //Menambah total harga
            totalPrice += mPayItems.get(l).getTotalPrice();
        }
        textInputTotalPrice.getEditText().setText(Util.formattedMoney(totalPrice));
    }

    private void payItem(){
        TextInputLayout textInputName = findViewById(R.id.pay_input_name);
        TextInputLayout textInputAddress = findViewById(R.id.pay_input_address);
        TextInputLayout textInputNumber = findViewById(R.id.pay_input_number);
        String buyerName = textInputName.getEditText().getText().toString();
        String buyerAddress = textInputAddress.getEditText().getText().toString();
        String buyerNumber = textInputNumber.getEditText().getText().toString();
        SharedPrefManager sharedPrefManager = SharedPrefManager.getInstance(this);
        if (Util.isEmpty(textInputName)){ // Validasi Nama
            textInputName.setErrorEnabled(true);
            textInputName.setError("Name must not be empty");
            return;
        } else {
            textInputName.setErrorEnabled(false);
        }
        if (Util.isEmpty(textInputAddress)){ // Validasi Alamat
            textInputAddress.setErrorEnabled(true);
            textInputAddress.setError("Address must not be empty");
            return;
        } else {
            textInputAddress.setErrorEnabled(false);
        }
        if (Util.isEmpty(textInputNumber)){ // Validasi Nomor
            textInputNumber.setErrorEnabled(true);
            textInputNumber.setError("Number must not be empty");
            return;
        } else {
            textInputNumber.setErrorEnabled(false);
        }
        OrderList orderList = new OrderList(buyerName,buyerAddress,buyerNumber,totalPrice,mPayItems);
        String urlOrder = getString(R.string.url_order);
        String user = SharedPrefManager.getInstance(this).getUsernamePref();
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.showDialog();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, urlOrder, (Response.Listener<String>) response -> { // REST API melakukan order
            progressDialog.hideDialog();
            try {
                JSONObject jsonObject = new JSONObject(response);
                if (jsonObject.getBoolean("success")){
                    Toast.makeText(this, "Produk telah diorder, Terima Kasih!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(this, MainActivity.class));
                    finish();
                } else {
                    Toast.makeText(this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> {
            progressDialog.hideDialog();
            Toast.makeText(this, error.getMessage(), Toast.LENGTH_SHORT).show();
        }){
            @Override
            protected Map<String, String> getParams() { // Parameter order
                Map<String, String> params = new HashMap<>();
                params.put("user", user);
                params.put("name", orderList.getBuyerName());
                params.put("address", orderList.getBuyerAddress());
                params.put("number", orderList.getBuyerNumber());
                params.put("total", String.valueOf(orderList.getTotalPrice()));
                for (int i = 0; i < orderList.getPayItemList().size(); i++){
                    params.put(String.format("product%s", orderList.getPayItemList().get(i).getId()), String.valueOf(orderList.getPayItemList().get(i).getQuantity()));
                    params.put(String.format("price%s", orderList.getPayItemList().get(i).getId()), String.valueOf(orderList.getPayItemList().get(i).getPrice()));
                }
                params.put("token", sharedPrefManager.getTokenPref());
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

}
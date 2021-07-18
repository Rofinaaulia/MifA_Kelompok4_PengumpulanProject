package me.itsmenow.erment.ui;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.itsmenow.erment.R;
import me.itsmenow.erment.adapter.OrderListAdapter;
import me.itsmenow.erment.adapter.PayListAdapter;
import me.itsmenow.erment.model.OrderList;
import me.itsmenow.erment.model.PayItem;
import me.itsmenow.erment.util.ProgressDialog;
import me.itsmenow.erment.util.SharedPrefManager;

public class HistoryActivity extends AppCompatActivity {

    private List<OrderList> mOrderList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        findViewById(R.id.button_back).setOnClickListener(view -> onBackPressed());
        mOrderList = new ArrayList<>();
        loadOrder();
    }

    private final OrderListAdapter.OnOrderItemClickListener onOrderRemove = position -> { // Hapus order click
        AlertDialog.Builder builder = new AlertDialog.Builder(this); // Dialog konfirm
        builder.setTitle("Yakin Hapus ?");
        builder.setMessage("Menghapus data berikut berarti menghapus data selamanya. Apakah anda yakin ?");
        builder.setPositiveButton("Ya, Hapus Data", (dialog, which) -> removeOrder(position));
        builder.setNegativeButton("Tidak", (dialog, i) -> dialog.dismiss());
        builder.setCancelable(true);
        builder.show();
    };

    private void removeOrder(int position){ // REST API Hapus order
        OrderList orderList = mOrderList.get(position);
        String urlRemoveOrder = getString(R.string.url_remove_order);
        SharedPrefManager sharedPrefManager = SharedPrefManager.getInstance(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, urlRemoveOrder, (Response.Listener<String>) response -> {
            try {
                JSONObject jsonObject = new JSONObject(response);
                if (jsonObject.getBoolean("success")){
                    Toast.makeText(this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    loadOrder();
                } else {
                    Toast.makeText(this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> {
            Toast.makeText(this, error.getMessage(), Toast.LENGTH_SHORT).show();
        }){
            @Override
            protected Map<String, String> getParams() { // Parameter menghapus order
                Map<String, String> params = new HashMap<>();
                params.put("id", String.valueOf(orderList.getId()));
                params.put("user", sharedPrefManager.getUsernamePref());
                params.put("token", sharedPrefManager.getTokenPref());
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void loadOrder(){ // Mengambil data order
        mOrderList.clear();
        String urlOrderList = getString(R.string.url_order_list);
        SharedPrefManager sharedPrefManager = SharedPrefManager.getInstance(this);
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.showDialog();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, urlOrderList, (Response.Listener<String>) response -> { // List order REST API
            progressDialog.hideDialog();
            try {
                JSONObject jsonObject = new JSONObject(response);
                if (jsonObject.getBoolean("success")){
                    Toast.makeText(this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    JSONArray jsonArray = jsonObject.getJSONArray("buyer");
                    for (int i = 0; i < jsonArray.length(); i++){
                        JSONObject buyer = jsonArray.getJSONObject(i);
                        JSONArray order = buyer.getJSONArray("order");
                        List<PayItem> mPayItemList = new ArrayList<>();
                        for (int l = 0; l< order.length(); l++){
                            JSONObject singleOrder = order.getJSONObject(l);
                            String productName;
                            if (singleOrder.getString("id_product").equals("BR005")){
                                productName = getString(R.string.product_1);
                            } else if (singleOrder.getString("id_product").equals("BR006")){
                                productName = getString(R.string.product_2);
                            } else if (singleOrder.getString("id_product").equals("BR007")){
                                productName = getString(R.string.product_3);
                            } else {
                                productName = "unknown";
                            }
                            mPayItemList.add(new PayItem(singleOrder.getInt("id"),singleOrder.getString("id_product"),productName,singleOrder.getInt("quantity"),singleOrder.getInt("price")));
                        }
                        OrderList orderList = new OrderList(buyer.getInt("id"), buyer.getString("buyer_name"),buyer.getString("buyer_address"),buyer.getString("buyer_number"),buyer.getInt("total_price"), mPayItemList);
                        mOrderList.add(orderList);
                    }
                    RecyclerView recyclerViewOrder = findViewById(R.id.recycler_history);
                    OrderListAdapter orderListAdapter = new OrderListAdapter(mOrderList, onOrderRemove);
                    recyclerViewOrder.setAdapter(orderListAdapter); // Atur data ke RecyclerView
                    recyclerViewOrder.setLayoutManager(new LinearLayoutManager(this));
                    orderListAdapter.notifyDataSetChanged();
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
            protected Map<String, String> getParams() { // Parameter mengambil order
                Map<String, String> params = new HashMap<>();
                params.put("user", sharedPrefManager.getUsernamePref());
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

}
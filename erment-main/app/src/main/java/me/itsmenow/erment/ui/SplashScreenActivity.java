package me.itsmenow.erment.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import me.itsmenow.erment.R;
import me.itsmenow.erment.util.SharedPrefManager;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        Handler handler = new Handler();
        handler.postDelayed(() -> {
            String urlToken = getString(R.string.url_token);
            SharedPrefManager sharedPrefManager = SharedPrefManager.getInstance(this);
            boolean remember = sharedPrefManager.getRememberPref(); // Shared Preferences ingat saya
            if (remember){
                StringRequest stringRequest = new StringRequest(Request.Method.POST, urlToken, (Response.Listener<String>) response -> { // Cek token login
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        if (jsonObject.getBoolean("success")){
                            startActivity(new Intent(SplashScreenActivity.this, MainActivity.class));
                        } else {
                            Toast.makeText(this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(SplashScreenActivity.this, LoginActivity.class));
                        }
                        finish();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, error -> {
                    Toast.makeText(this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(SplashScreenActivity.this, LoginActivity.class));
                    finish();
                }){
                    @Override
                    protected Map<String, String> getParams() { // Parameter cek token
                        Map<String, String> params = new HashMap<>();
                        params.put("token",sharedPrefManager.getTokenPref());
                        return params;
                    }
                };
                RequestQueue requestQueue = Volley.newRequestQueue(this);
                requestQueue.add(stringRequest);
            } else { // Tidak centang ingat saya
                sharedPrefManager.clearPref();
                Intent intent = new Intent(SplashScreenActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        },1000);
    }
}
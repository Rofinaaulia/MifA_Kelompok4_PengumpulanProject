package me.itsmenow.erment.ui;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import me.itsmenow.erment.R;
import me.itsmenow.erment.util.ProgressDialog;
import me.itsmenow.erment.util.Util;

public class RegisterActivity extends AppCompatActivity {


    private TextInputLayout textInputUsername;
    private TextInputLayout textInputEmail;
    private TextInputLayout textInputPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        findViewById(R.id.button_back).setOnClickListener(view -> onBackPressed());
        textInputUsername = findViewById(R.id.register_input_username);
        textInputEmail = findViewById(R.id.register_input_email);
        textInputPassword = findViewById(R.id.register_input_password);
        textInputPassword.getEditText().setOnEditorActionListener((textView, i, keyEvent) -> {
            boolean handled = false;
            if (i == EditorInfo.IME_ACTION_SEND) {
                registerAccount();
                handled = true;
            }
            return handled;
        });
        Button buttonRegister = findViewById(R.id.register_button_register);
        buttonRegister.setOnClickListener(view -> registerAccount()); // Listener tombol register
    }

    private void registerAccount(){ // Register akun
        String urlRegister = getString(R.string.url_register);
        String username = textInputUsername.getEditText().getText().toString().trim();
        String email = textInputEmail.getEditText().getText().toString().trim();
        String password = textInputPassword.getEditText().getText().toString().trim();
        if (Util.isValidEmail(email)){ // Validasi Email
            textInputEmail.setErrorEnabled(false);
        } else {
            Toast.makeText(this, "Please input a valid email", Toast.LENGTH_SHORT).show();
            textInputEmail.setErrorEnabled(true);
            textInputEmail.setError("Please input a valid email");
            return;
        }
        if (Util.isValidUsername(username)){ // Validasi username
            textInputUsername.setErrorEnabled(false);
        } else {
            Toast.makeText(this, "Please input a valid username", Toast.LENGTH_SHORT).show();
            textInputUsername.setErrorEnabled(true);
            textInputUsername.setError("Please input a valid username. Username must be 4 characters or longer");
            return;
        }
        if (Util.isValidPassword(password)){ // Validasi password
            textInputPassword.setErrorEnabled(false);
        } else {
            Toast.makeText(this, "Please input a valid password", Toast.LENGTH_SHORT).show();
            textInputPassword.setErrorEnabled(true);
            textInputPassword.setError("Please input a valid password. Password must be 3 characters or longer");
            return;
        }
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.showDialog();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, urlRegister, (Response.Listener<String>) response -> { // REST API Register akun
            progressDialog.hideDialog();
            try {
                JSONObject jsonObject = new JSONObject(response);
                if (jsonObject.getBoolean("success")){
                    Toast.makeText(this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    onBackPressed();
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
            protected Map<String, String> getParams() { // Parameter register akun
                Map<String, String> params = new HashMap<>();
                params.put("user",username);
                params.put("pass", password);
                params.put("email", email);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}
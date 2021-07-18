package me.itsmenow.erment.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

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
import me.itsmenow.erment.util.SharedPrefManager;
import me.itsmenow.erment.util.Util;

public class LoginActivity extends AppCompatActivity {

    TextInputLayout textInputUsername;
    TextInputLayout textInputPassword;
    Button buttonLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this,R.color.brown_2));
        textInputUsername = findViewById(R.id.login_input_username);
        textInputPassword = findViewById(R.id.login_input_password);
        TextView tvSignUp = findViewById(R.id.login_textview_signup);
        buttonLogin = findViewById(R.id.login_button_login);
        String textViewSignUpText = getString(R.string.signup_text);
        SpannableString spannableString = new SpannableString(textViewSignUpText);
        spannableString.setSpan(onSignUpClick, 23, 30, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvSignUp.setText(spannableString);
        tvSignUp.setMovementMethod(LinkMovementMethod.getInstance());
        textInputPassword.getEditText().setOnEditorActionListener((textView, i, keyEvent) -> {
            boolean handled = false;
            if (i == EditorInfo.IME_ACTION_SEND) {
                loginAccount();
                handled = true;
            }
            return handled;
        });
        buttonLogin.setOnClickListener(view -> {
            loginAccount();
        });
    }

    private final ClickableSpan onSignUpClick = new ClickableSpan() { // Sign up text clickable
        @Override
        public void onClick(@NonNull View view) {
            startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
        }
        @Override
        public void updateDrawState(@NonNull TextPaint ds) {
            ds.setColor(getColor(R.color.blue_1));
            ds.setUnderlineText(false);
        }
    };

    private void loginAccount(){
        String urlLogin = getString(R.string.url_login);
        String username = textInputUsername.getEditText().getText().toString();
        String password = textInputPassword.getEditText().getText().toString();
        CheckBox checkboxRemember = findViewById(R.id.login_checkbox_remember);
        if (Util.isValidUsername(username)){  // Validasi username
            textInputUsername.setErrorEnabled(false);
        } else {
            Toast.makeText(this, "Please input a valid username", Toast.LENGTH_SHORT).show();
            textInputUsername.setErrorEnabled(true);
            textInputUsername.setError("Please input a valid username. Username must be 4 characters or longer");
            return;
        }
        if (Util.isValidPassword(password)){ // Validasi Password
            textInputPassword.setErrorEnabled(false);
        } else {
            Toast.makeText(this, "Please input a valid password", Toast.LENGTH_SHORT).show();
            textInputPassword.setErrorEnabled(true);
            textInputPassword.setError("Please input a valid password. Password must be 3 characters or longer");
            return;
        }
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.showDialog();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, urlLogin, (Response.Listener<String>) response -> { // REST API untuk login
            progressDialog.hideDialog();
            try {
                JSONObject jsonObject = new JSONObject(response);
                if (jsonObject.getBoolean("success")){
                    Toast.makeText(this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    if (checkboxRemember.isChecked()) {
                        SharedPrefManager sharedPrefManager = SharedPrefManager.getInstance(this);
                        sharedPrefManager.savePref(jsonObject.getString("user"), jsonObject.getString("email"), jsonObject.getString("token"), true);
                    } else {
                        SharedPrefManager sharedPrefManager = SharedPrefManager.getInstance(this);
                        sharedPrefManager.savePref(jsonObject.getString("user"), jsonObject.getString("email"), jsonObject.getString("token"), false);
                    }
                    startActivity(new Intent(this, MainActivity.class));
                    finish();
                } else {
                    Toast.makeText(this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> { // Gagal login
            progressDialog.hideDialog();
            Toast.makeText(this, error.getMessage(), Toast.LENGTH_SHORT).show();
        }){
            @Override
            protected Map<String, String> getParams() { // Parameter POST login
                Map<String, String> params = new HashMap<>();
                params.put("user",username);
                params.put("pass", password);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }


}
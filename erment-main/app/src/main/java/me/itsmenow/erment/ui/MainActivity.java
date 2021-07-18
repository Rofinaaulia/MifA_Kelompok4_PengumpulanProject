package me.itsmenow.erment.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import me.itsmenow.erment.R;
import me.itsmenow.erment.util.SharedPrefManager;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button buttonProfile = findViewById(R.id.menu_profile); // Definisi Tombol
        Button buttonAbout = findViewById(R.id.menu_about);
        Button buttonTransaction = findViewById(R.id.menu_transaction);
        Button buttonHistory = findViewById(R.id.menu_history);
        Button buttonSignOut = findViewById(R.id.menu_signout);
        buttonProfile.setOnClickListener(onMenuClick); // Atur tombol listener
        buttonAbout.setOnClickListener(onMenuClick);
        buttonTransaction.setOnClickListener(onMenuClick);
        buttonHistory.setOnClickListener(onMenuClick);
        buttonSignOut.setOnClickListener(onMenuClick);
    }

    @SuppressLint("NonConstantResourceId")
    private final View.OnClickListener onMenuClick = view -> { // Listener tombol
        switch (view.getId()) {
            case R.id.menu_profile:
                startActivity(new Intent(this,ProfileActivity.class));
                break;
            case R.id.menu_about:
                startActivity(new Intent(this,AboutActivity.class));
                break;
            case R.id.menu_transaction:
                startActivity(new Intent(this,TransactionActivity.class));
                break;
            case R.id.menu_history:
                startActivity(new Intent(this,HistoryActivity.class));
                break;
            case R.id.menu_signout:
                SharedPrefManager sharedPrefManager = SharedPrefManager.getInstance(this);
                sharedPrefManager.clearPref();
                startActivity(new Intent(this,LoginActivity.class));
                finish();
                break;
            default:
                break;
        }
    };
}
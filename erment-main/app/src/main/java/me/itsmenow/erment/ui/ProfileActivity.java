package me.itsmenow.erment.ui;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import me.itsmenow.erment.R;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        findViewById(R.id.button_back).setOnClickListener(view -> onBackPressed());
    }
}
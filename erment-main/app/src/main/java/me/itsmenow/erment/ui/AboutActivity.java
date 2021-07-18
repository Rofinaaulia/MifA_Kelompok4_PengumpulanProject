package me.itsmenow.erment.ui;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import me.itsmenow.erment.R;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        findViewById(R.id.button_back).setOnClickListener(view -> onBackPressed());
    }
}
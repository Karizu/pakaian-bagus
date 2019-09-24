package com.example.pakaianbagus.util.interceptor;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.pakaianbagus.R;

public class ErrorActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.error_activity);

        Intent intent = getIntent();
        String errorMessage = intent.getStringExtra("errorMessage");
        TextView tv_error = findViewById(R.id.tv_error);
        tv_error.setText(errorMessage);

        Button bt_ok = findViewById(R.id.bt_ok);
        bt_ok.setOnClickListener((View v) -> finish());
    }
}

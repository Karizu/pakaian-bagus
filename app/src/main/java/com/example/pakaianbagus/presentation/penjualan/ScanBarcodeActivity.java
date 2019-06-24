package com.example.pakaianbagus.presentation.penjualan;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pakaianbagus.R;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class ScanBarcodeActivity extends AppCompatActivity {

    TextView tvCardText;
    public static final int KEYCODE_SCAN_LEFT = 229;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_barcode);

        tvCardText = findViewById(R.id.tv_code_text);
        startQRScanner();

    }


    private void startQRScanner() {
        new IntentIntegrator(this).initiateScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result =   IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(this,    "Cancelled",Toast.LENGTH_LONG).show();
            } else {
                updateText(result.getContents());
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void updateText(String scanCode) {
        tvCardText.setText(scanCode);
    }

    public boolean onKeyDown(int keyCode, KeyEvent event){
        if (keyCode == KEYCODE_SCAN_LEFT){
            startQRScanner();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}

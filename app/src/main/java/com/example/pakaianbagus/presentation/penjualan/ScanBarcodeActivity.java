package com.example.pakaianbagus.presentation.penjualan;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pakaianbagus.R;
import com.example.pakaianbagus.api.KatalogHelper;
import com.example.pakaianbagus.models.ApiResponse;
import com.example.pakaianbagus.models.stock.Stock;
import com.example.pakaianbagus.util.IntentToFragment;
import com.example.pakaianbagus.util.dialog.Loading;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.rezkyatinnov.kyandroid.reztrofit.ErrorResponse;
import com.rezkyatinnov.kyandroid.reztrofit.RestCallback;

import java.util.List;
import java.util.Objects;

import okhttp3.Headers;

public class ScanBarcodeActivity extends AppCompatActivity {

    private TextView tvCardText;
    private String mode;
    private Context context;
    private Dialog dialog;
    public static final int KEYCODE_SCAN_LEFT = 229;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_barcode);
        context = this;

        Intent intent = getIntent();
        mode = intent.getExtras().getString("mode");

        tvCardText = findViewById(R.id.tv_code_text);
        startQRScanner();

    }


    private void startQRScanner() {
        new IntentIntegrator(this).initiateScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
                onBackPressed();
            } else {
                updateText(result.getContents());
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @SuppressLint("SetTextI18n")
    private void updateText(String scanCode) {
        tvCardText.setText(scanCode);

        switch (mode) {
            case "PENJUALAN":
//                getBarang(scanCode);
                Intent inten = new Intent(ScanBarcodeActivity.this, IntentToFragment.class);
                inten.putExtra("keyword", "Penjualan");
                inten.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(inten);
                break;
            case "KATALOG":
                Intent intent = new Intent(ScanBarcodeActivity.this, IntentToFragment.class);
                intent.putExtra("keyword", scanCode);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                break;
            case "STOCK":
                showDialog();
                TextView title = dialog.findViewById(R.id.tvTitle);
                EditText qty = dialog.findViewById(R.id.etQty);
                Button submit = dialog.findViewById(R.id.btnUbah);
                ImageView close = dialog.findViewById(R.id.imgClose);

                title.setText("MASUKAN QTY");
                submit.setText("OK");
                submit.setOnClickListener(v -> {
                    if (!qty.getText().toString().equals("")) {
                        dialog.dismiss();
                        onBackPressed();
                    } else {
                        Toast.makeText(context, "Harap isi quantity", Toast.LENGTH_SHORT).show();
                    }
                });

                close.setOnClickListener(v ->
                        dialog.dismiss()
                );
//                Intent intent1 = new Intent(ScanBarcodeActivity.this, IntentToFragment.class);
//                intent1.putExtra("keyword", scanCode);
//                intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                startActivity(intent1);
                break;
        }
    }

    private void getBarang(String keyword) {
        Loading.show(ScanBarcodeActivity.this);
        KatalogHelper.searchBarangPenjualan(keyword, new RestCallback<ApiResponse<List<Stock>>>() {
            @Override
            public void onSuccess(Headers headers, ApiResponse<List<Stock>> body) {
                Loading.hide(ScanBarcodeActivity.this);
                Log.d("TAG RESPOGNSE", body.getMessage());
            }

            @Override
            public void onFailed(ErrorResponse error) {
                Loading.hide(ScanBarcodeActivity.this);
                Log.d("TAG ERROR", error.getMessage());
            }

            @Override
            public void onCanceled() {

            }
        });
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KEYCODE_SCAN_LEFT) {
            startQRScanner();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void showDialog() {
        dialog = new Dialog(Objects.requireNonNull(context));
        //set content
        dialog.setContentView(R.layout.dialog_ubah_qty);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(Objects.requireNonNull(dialog.getWindow()).getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }
}

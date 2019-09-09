package com.example.pakaianbagus.presentation.home.inputpenjualan;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pakaianbagus.R;
import com.example.pakaianbagus.api.InputHelper;
import com.example.pakaianbagus.models.ApiResponse;
import com.example.pakaianbagus.models.Discount;
import com.example.pakaianbagus.models.stock.Item;
import com.example.pakaianbagus.models.stock.StokToko;
import com.example.pakaianbagus.presentation.home.HomeFragment;
import com.example.pakaianbagus.presentation.home.inputpenjualan.adapter.PenjualanKompetitorAdapter;
import com.example.pakaianbagus.presentation.home.inputpenjualan.adapter.SalesReportAdapter;
import com.example.pakaianbagus.util.Scanner;
import com.example.pakaianbagus.util.dialog.Loading;
import com.rezkyatinnov.kyandroid.reztrofit.ErrorResponse;
import com.rezkyatinnov.kyandroid.reztrofit.RestCallback;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Headers;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class InputPenjualan extends Fragment {

    @BindView(R.id.tvDate)
    TextView tvDate;
    @BindView(R.id.tabSalesReport)
    TextView tabSalesReport;
    @BindView(R.id.tabPenjualan)
    TextView tabPenjualan;
    @BindView(R.id.btnBarcode)
    ImageView btnBarcode;
    @BindView(R.id.btnAdd)
    ImageView btnAdd;
    @BindView(R.id.rvSales)
    RecyclerView rvSales;
    @BindView(R.id.rvPenjualan)
    RecyclerView rvPenjualan;

    View rootView;
    String date;
    int pager;
    final int REQUEST_CODE = 564;
    final int REQUEST_SCANNER = 999;
    private List<StokToko> salesReportList = new ArrayList<>();
    private List<StokToko> salesFinal = new ArrayList<>();
    private List<StokToko> kompetitorList = new ArrayList<>();
    private List<Discount> discounts = new ArrayList<>();
    private SalesReportAdapter salesReportAdapter;
    private PenjualanKompetitorAdapter kompetitorAdapter;

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.input_penjualan_fragment, container, false);
        ButterKnife.bind(this, rootView);

        date = Objects.requireNonNull(getArguments()).getString("date");

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        SimpleDateFormat dateFormatView = new SimpleDateFormat("yyyy MMMM dd", Locale.US);
        try {
            Date theDate = dateFormat.parse(date);
            tvDate.setText(dateFormatView.format(theDate));
        } catch (ParseException e) {
            e.printStackTrace();
            tvDate.setText(date);
        }

        defaultScreen();

        LinearLayoutManager salesLayoutManager = new LinearLayoutManager(getContext(), LinearLayout.VERTICAL, false);
        rvSales.setLayoutManager(salesLayoutManager);
        salesReportAdapter = new SalesReportAdapter(salesReportList, getContext(), InputPenjualan.this, discounts);
        rvSales.setAdapter(salesReportAdapter);

        LinearLayoutManager kompetitorlayoutManager = new LinearLayoutManager(getContext(), LinearLayout.VERTICAL, false);
        rvPenjualan.setLayoutManager(kompetitorlayoutManager);
        kompetitorAdapter = new PenjualanKompetitorAdapter(kompetitorList, getContext(), InputPenjualan.this, date);
        rvPenjualan.setAdapter(kompetitorAdapter);

        getData();

        return rootView;
    }

    private void getData() {
        Loading.show(getContext());
        getDiscount();
    }

    private void getDiscount() {
        discounts.clear();
        InputHelper.getDiscount(new RestCallback<ApiResponse<List<Discount>>>() {
            @Override
            public void onSuccess(Headers headers, ApiResponse<List<Discount>> body) {
                Loading.hide(getContext());
                discounts.addAll(body.getData());
            }

            @Override
            public void onFailed(ErrorResponse error) {
                Loading.hide(getContext());
                Toast.makeText(getContext(), "Error get diskon : " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCanceled() {

            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_SCANNER && resultCode == Activity.RESULT_OK) {
            String resultData = data.getStringExtra("scan_data");
            addListFromBarcode(resultData);
        }
    }

    private void addListFromBarcode(String resultData) {
        Loading.show(getContext());
        InputHelper.getDetailStock(resultData, new RestCallback<ApiResponse<List<StokToko>>>() {
            @Override
            public void onSuccess(Headers headers, ApiResponse<List<StokToko>> body) {
                Loading.hide(getContext());
                if (body.getData().size() > 0) {
                    StokToko stokToko = body.getData().get(0);
                    if (salesReportList.size() > 0) {
                        boolean done = false;
                        for (int x = 0; x < salesReportList.size(); x++) {
                            if (salesReportList.get(x).getArticleCode().equals(stokToko.getArticleCode())) {
                                stokToko.setQty(salesReportList.get(x).getQty() + 1);
                                salesReportList.set(x, stokToko);
                                done = true;
                                break;
                            }
                        }
                        if (!done) {
                            stokToko.setQty(1);
                            salesReportList.add(stokToko);
                        }
                    } else {
                        stokToko.setQty(1);
                        salesReportList.add(stokToko);
                    }

                    salesReportAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(getContext(), "Tidak ada dari barcode tersebut, mohon periksa kembali barcode atau coba lagi.", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailed(ErrorResponse error) {
                Loading.hide(getContext());
                Log.d("onFailed: ", error.getMessage());
                Toast.makeText(getContext(), "Error : " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCanceled() {
            }
        });
    }

    @OnClick(R.id.tabSalesReport)
    public void onSalesReportSelected() {
        pager = 0;
        rvSales.setVisibility(View.VISIBLE);
        rvPenjualan.setVisibility(View.GONE);
        tabSalesReport.setTextColor(getResources().getColor(R.color.DarkSlateBlue));
        tabPenjualan.setTextColor(getResources().getColor(R.color.Background));
        btnBarcode.setVisibility(View.VISIBLE);
        btnAdd.setVisibility(View.GONE);
    }

    @OnClick(R.id.tabPenjualan)
    public void onPenjualanKompetitorSelected() {
        pager = 1;
        rvSales.setVisibility(View.GONE);
        rvPenjualan.setVisibility(View.VISIBLE);
        tabSalesReport.setTextColor(getResources().getColor(R.color.Background));
        tabPenjualan.setTextColor(getResources().getColor(R.color.DarkSlateBlue));
        btnBarcode.setVisibility(View.GONE);
        btnAdd.setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.btnSubmit)
    public void btnSubmitClicked() {
        if (pager == 0) {
            doSalesReport();
        } else if (pager == 1) {
            doKompetitor();
        }
    }

    private void doSalesReport() {
        if (salesFinal.size() > 0) {
            Loading.show(getContext());

            //StringBuilder from = new StringBuilder();
            StringBuilder description = new StringBuilder();
            StringBuilder stock_id = new StringBuilder();
            StringBuilder qty = new StringBuilder();
            int totalQty = 0;
            int totalPrice = 0;

            int from = salesFinal.get(0).getMPlaceId();

            for (int x = 0; x < salesFinal.size(); x++) {
                //from.append(salesFinal.get(x).getMPlaceId()).append(",");
                totalQty = salesFinal.get(x).getQty() + totalQty;
                totalPrice = salesFinal.get(x).getTotal() + totalPrice;
                description.append(salesFinal.get(x).getDescription()).append(",");
                stock_id.append(salesFinal.get(x).getId()).append(",");
                qty.append(salesFinal.get(x).getQty()).append(",");
            }

            Log.d("doSalesReport: ", String.valueOf(totalQty));
            Log.d("doSalesReport: ", String.valueOf(totalPrice));
            Log.d("doSalesReport: ", String.valueOf(totalPrice));

            RequestBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("sales_id", "1")
                    .addFormDataPart("no", "2")
                    .addFormDataPart("date", date)
                    .addFormDataPart("from", String.valueOf(from))
                    .addFormDataPart("total_qty", String.valueOf(totalQty))
                    .addFormDataPart("total_price", String.valueOf(totalPrice))
                    .addFormDataPart("description", description.toString())
                    .addFormDataPart("type", "1")
                    .addFormDataPart("payment_method", "1")
                    .addFormDataPart("details[0][stock_id]", stock_id.toString())
                    .addFormDataPart("details[0][qty]", qty.toString())
                    .build();

            InputHelper.postSalesReport(requestBody, new RestCallback<ApiResponse>() {
                @Override
                public void onSuccess(Headers headers, ApiResponse body) {
                    Loading.hide(getContext());
                    Toast.makeText(getContext(), "Berhasil mengirimkan data report", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailed(ErrorResponse error) {
                    Loading.hide(getContext());
                    Toast.makeText(getContext(), "Gagal mengirimkan data report : " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onCanceled() {

                }
            });
        } else {
            Toast.makeText(getContext(), "Tidak ada data yang harus di laporkan", Toast.LENGTH_SHORT).show();
        }

    }

    private void doKompetitor() {
        if (kompetitorList.size() > 0) {
            Toast.makeText(getContext(), "Data tinggal dikirim dengan jumlah data = " + String.valueOf(kompetitorList.size()), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getContext(), "Tidak ada penjualan kompetitor yang harus di laporkan", Toast.LENGTH_SHORT).show();
        }
    }

    @OnClick(R.id.btnAdd)
    public void btnAddClicked() {
        Dialog dialog = new Dialog(Objects.requireNonNull(getActivity()));
        dialog.setContentView(R.layout.dialog_penjualan_kompetitor_add);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(Objects.requireNonNull(dialog.getWindow()).getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        ImageView imgClose = dialog.findViewById(R.id.imgClose);
        EditText etNama = dialog.findViewById(R.id.etNama);
        EditText etJumlah = dialog.findViewById(R.id.etJumlah);
        EditText etQuantity = dialog.findViewById(R.id.etQuantity);
        Button btnAdd = dialog.findViewById(R.id.btnAdd);

        imgClose.setOnClickListener(v -> dialog.dismiss());
        btnAdd.setOnClickListener(v -> {
            dialog.dismiss();
            addPenjualanKompetitor(
                    etNama.getText().toString(),
                    etJumlah.getText().toString(),
                    etQuantity.getText().toString()
            );
        });

        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }

    private void addPenjualanKompetitor(String nama, String jumlah, String quantity) {
        StokToko stokToko = new StokToko();
        Item item = new Item();
        item.setName(nama);
        stokToko.setItem(item);
        stokToko.setQty(Integer.parseInt(quantity));
        stokToko.setTotal(Integer.parseInt(jumlah));

        kompetitorList.add(stokToko);
        kompetitorAdapter.notifyDataSetChanged();

    }

    @OnClick(R.id.btnBarcode)
    public void btnBarcodeClicked() {
        if (ContextCompat.checkSelfPermission(Objects.requireNonNull(getContext()), android.Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(Objects.requireNonNull(getActivity()), new String[]{Manifest.permission.CAMERA}, REQUEST_CODE);
        } else {
            Intent intent = new Intent(getContext(), Scanner.class);
            startActivityForResult(intent, REQUEST_SCANNER);
        }
    }

    @OnClick(R.id.toolbar_back)
    public void toolbarBack() {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = Objects.requireNonNull(fm).beginTransaction();
        HomeFragment homeFragment = new HomeFragment();
        ft.setCustomAnimations(R.animator.fade_in, R.animator.fade_out);
        ft.replace(R.id.baseLayout, homeFragment);
        ft.commit();
    }

    private void defaultScreen() {
        pager = 0;
        rvSales.setVisibility(View.VISIBLE);
        rvPenjualan.setVisibility(View.GONE);
        tabSalesReport.setTextColor(getResources().getColor(R.color.DarkSlateBlue));
        tabPenjualan.setTextColor(getResources().getColor(R.color.Background));
        btnBarcode.setVisibility(View.VISIBLE);
        btnAdd.setVisibility(View.GONE);
    }

    public void salesData(List<StokToko> stokTokoList) {
        this.salesFinal = stokTokoList;
    }

}

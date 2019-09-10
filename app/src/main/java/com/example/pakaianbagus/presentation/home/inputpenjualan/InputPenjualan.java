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
import com.example.pakaianbagus.models.Detail;
import com.example.pakaianbagus.models.Discount;
import com.example.pakaianbagus.models.Kompetitor;
import com.example.pakaianbagus.models.SalesReport;
import com.example.pakaianbagus.models.api.penjualankompetitor.KompetitorResponse;
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
    @BindView(R.id.btnSubmit)
    Button btnSubmit;

    View rootView;
    String userId;
    String date;
    String placeId;
    int pager;
    final int REQUEST_CODE = 564;
    final int REQUEST_SCANNER = 999;
    private List<StokToko> salesReportList = new ArrayList<>();
    private List<StokToko> salesReportTemp = new ArrayList<>();
    private List<KompetitorResponse> kompetitorList = new ArrayList<>();
    //private List<Kompetitor> kompetitorListTemp = new ArrayList<>();
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

        userId = "1";
        placeId = "1";

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        SimpleDateFormat dateFormatView = new SimpleDateFormat("yyyy MMMM dd", Locale.US);
        try {
            Date theDate = dateFormat.parse(date);
            tvDate.setText(dateFormatView.format(theDate));
        } catch (ParseException e) {
            e.printStackTrace();
            tvDate.setText(date);
        }

        viewScreen(1);

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
        if (getDiscount() && getSalesReport() && getKompetitorData()) {
            Loading.hide(getContext());
        }
    }

    private boolean getKompetitorData() {
        InputHelper.getPenjualanKompetitor(placeId, date, new RestCallback<ApiResponse<List<KompetitorResponse>>>() {
            @Override
            public void onSuccess(Headers headers, ApiResponse<List<KompetitorResponse>> body) {
                kompetitorList.clear();
                kompetitorList.addAll(body.getData());
                kompetitorAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailed(ErrorResponse error) {

            }

            @Override
            public void onCanceled() {

            }
        });

        return true;
    }

    private boolean getSalesReport() {
        salesReportList.clear();
        salesReportTemp.clear();
        /*InputHelper.getSalesReport(userId, date, new RestCallback<ApiResponse<List<SalesReportResponse>>>() {
            @Override
            public void onSuccess(Headers headers, ApiResponse<List<SalesReportResponse>> body) {
                if (body.getData().size() > 0) {
                    List<SalesReportResponse> responses = body.getData();
                    for (int x = 0; x < responses.size(); x++) {
                        Item item = new Item();
                        item.setName(responses.get(x).);

                        StokToko stokToko = new StokToko();
                        stokToko.setNew(false);
                        salesReportList.add(stokToko);
                    }
                }
            }

            @Override
            public void onFailed(ErrorResponse error) {
                Log.d("onFailed: ", error.toString());
            }

            @Override
            public void onCanceled() {

            }
        });*/
        return true;
    }

    private boolean getDiscount() {
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
        return true;
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
        viewScreen(1);
    }

    @OnClick(R.id.tabPenjualan)
    public void onPenjualanKompetitorSelected() {
        viewScreen(2);
    }

    @OnClick(R.id.btnSubmit)
    public void btnSubmitClicked() {
        doSalesReport();
        /*if (pager == 0) {
            doSalesReport();
        } else if (pager == 1) {
            doKompetitor();
        }*/
    }

    private void doSalesReport() {
        if (salesReportTemp.size() > 0) {
            Loading.show(getContext());

            StringBuilder description = new StringBuilder();
            int totalQty = 0;
            int totalPrice = 0;

            int from = salesReportTemp.get(0).getMPlaceId();

            List<Detail> details = new ArrayList<>();

            for (int x = 0; x < salesReportTemp.size(); x++) {
                totalQty = salesReportTemp.get(x).getQty() + totalQty;
                totalPrice = salesReportTemp.get(x).getTotal() + totalPrice;
                description.append(salesReportTemp.get(x).getDescription()).append(",");

                Detail detail = new Detail();
                detail.setStockId(salesReportTemp.get(x).getId());
                detail.setQty(salesReportTemp.get(x).getQty());
                detail.setPrice(salesReportTemp.get(x).getTotal());
                details.add(detail);
            }

            SalesReport salesReport = new SalesReport();

            salesReport.setSalesId(userId);
            salesReport.setNo("2");
            salesReport.setDate(date);
            salesReport.setFrom(String.valueOf(from));
            salesReport.setTotalQty(String.valueOf(totalQty));
            salesReport.setTotalPrice(String.valueOf(totalPrice));
            salesReport.setDescription(description.toString());
            salesReport.setType("1");
            salesReport.setPaymentMethod("1");
            salesReport.setDetails(details);

            InputHelper.postSalesReport(salesReport, new RestCallback<ApiResponse>() {
                @Override
                public void onSuccess(Headers headers, ApiResponse body) {
                    Loading.hide(getContext());
                    Toast.makeText(getContext(), "Berhasil mengirimkan data report", Toast.LENGTH_SHORT).show();
                    FragmentManager fm = getFragmentManager();
                    FragmentTransaction ft = Objects.requireNonNull(fm).beginTransaction();
                    HomeFragment homeFragment = new HomeFragment();
                    ft.setCustomAnimations(R.animator.fade_in, R.animator.fade_out);
                    ft.replace(R.id.baseLayout, homeFragment);
                    ft.commit();
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
        /*if (kompetitorListTemp.size() > 0) {
            Loading.show(getContext());

            *//*InputHelper.postPenjualanKompetitor(kompetitorListTemp, new RestCallback<ApiResponse>() {
                @Override
                public void onSuccess(Headers headers, ApiResponse body) {
                    Loading.hide(getContext());
                    Toast.makeText(getContext(), "Berhasil mengirimkan data penjualan kompetitor", Toast.LENGTH_SHORT).show();
                    FragmentManager fm = getFragmentManager();
                    FragmentTransaction ft = Objects.requireNonNull(fm).beginTransaction();
                    HomeFragment homeFragment = new HomeFragment();
                    ft.setCustomAnimations(R.animator.fade_in, R.animator.fade_out);
                    ft.replace(R.id.baseLayout, homeFragment);
                    ft.commit();
                }

                @Override
                public void onFailed(ErrorResponse error) {
                    Loading.hide(getContext());
                    Toast.makeText(getContext(), "Gagal mengirimkan data penjualan kompetitor : " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onCanceled() {

                }
            });*//*
        } else {
            Toast.makeText(getContext(), "Tidak ada penjualan kompetitor yang harus di laporkan", Toast.LENGTH_SHORT).show();
        }*/
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
        Loading.show(getContext());

        Kompetitor kompetitor = new Kompetitor();
        kompetitor.setmPlaceId(placeId);
        kompetitor.setBrand(nama);
        kompetitor.setFromDate(date);
        kompetitor.setToDate(date);
        kompetitor.setQty(Integer.parseInt(quantity));
        kompetitor.setPrice(Integer.parseInt(jumlah));

        InputHelper.postPenjualanKompetitor(kompetitor, new RestCallback<ApiResponse>() {
            @Override
            public void onSuccess(Headers headers, ApiResponse body) {
                Toast.makeText(getContext(), "Berhasil mengirimkan data penjualan kompetitor", Toast.LENGTH_SHORT).show();
                if (getKompetitorData()) {
                    Loading.hide(getContext());
                }
            }

            @Override
            public void onFailed(ErrorResponse error) {
                Loading.hide(getContext());
                Toast.makeText(getContext(), "Gagal mengirimkan data penjualan kompetitor : " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCanceled() {

            }
        });
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

    private void viewScreen(int page) {
        pager = page;
        if (page == 1) {
            rvSales.setVisibility(View.VISIBLE);
            rvPenjualan.setVisibility(View.GONE);
            tabSalesReport.setTextColor(getResources().getColor(R.color.DarkSlateBlue));
            tabPenjualan.setTextColor(getResources().getColor(R.color.Background));
            btnBarcode.setVisibility(View.VISIBLE);
            btnAdd.setVisibility(View.GONE);
            btnSubmit.setVisibility(View.VISIBLE);
        } else if (page == 2) {
            rvSales.setVisibility(View.GONE);
            rvPenjualan.setVisibility(View.VISIBLE);
            tabSalesReport.setTextColor(getResources().getColor(R.color.Background));
            tabPenjualan.setTextColor(getResources().getColor(R.color.DarkSlateBlue));
            btnBarcode.setVisibility(View.GONE);
            btnAdd.setVisibility(View.VISIBLE);
            btnSubmit.setVisibility(View.GONE);
        }
    }

    public void salesData(List<StokToko> stokTokoList) {
        this.salesReportTemp = stokTokoList;
    }

}

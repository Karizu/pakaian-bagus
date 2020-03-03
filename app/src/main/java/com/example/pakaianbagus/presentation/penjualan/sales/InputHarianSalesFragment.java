package com.example.pakaianbagus.presentation.penjualan.sales;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.pakaianbagus.R;
import com.example.pakaianbagus.api.InputHelper;
import com.example.pakaianbagus.api.PenjualanHelper;
import com.example.pakaianbagus.models.ApiResponse;
import com.example.pakaianbagus.models.Details;
import com.example.pakaianbagus.models.Discount;
import com.example.pakaianbagus.models.PenjualanResponse;
import com.example.pakaianbagus.models.SalesReportModel;
import com.example.pakaianbagus.models.TransactionModel;
import com.example.pakaianbagus.models.stock.Stock;
import com.example.pakaianbagus.presentation.penjualan.InputHarianFragment;
import com.example.pakaianbagus.presentation.penjualan.PenjualanKompetitorFragment;
import com.example.pakaianbagus.presentation.penjualan.PenjualanListTokoFragment;
import com.example.pakaianbagus.presentation.penjualan.adapter.SalesReportAdapter;
import com.example.pakaianbagus.util.Constanta;
import com.example.pakaianbagus.util.Scanner;
import com.example.pakaianbagus.util.SessionManagement;
import com.example.pakaianbagus.util.dialog.Loading;
import com.rezkyatinnov.kyandroid.reztrofit.ErrorResponse;
import com.rezkyatinnov.kyandroid.reztrofit.RestCallback;
import com.rezkyatinnov.kyandroid.session.Session;
import com.rezkyatinnov.kyandroid.session.SessionNotFoundException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Headers;

public class InputHarianSalesFragment extends Fragment {

    View rootView;
    private List<SalesReportModel> salesReportModels;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.tvDate)
    TextView tvDate;
    @BindView(R.id.swipeRefresh)
    SwipeRefreshLayout swipeRefresh;
    @BindView(R.id.tvNoData)
    TextView tvNoData;
    @BindView(R.id.toolbar_scan)
    ImageView toolbar_scan;
    @BindView(R.id.btnSubmit)
    Button btnSubmit;
    @BindView(R.id.btnDownload)
    Button btnDownload;
    @BindView(R.id.layLine)
    LinearLayout layLine;
    @BindView(R.id.tabSalesReport)
    TextView tabSalesReport;
    @BindView(R.id.tabPenjualan)
    TextView tabPenjualan;

    int limit = 10;
    int offset = 0;
    String payment_method = null;
    Dialog dialog;
    Calendar myCalendar;
    String limitHutang, totalHutang;
    String id, userId, placeId, brandId;
    String NoTrx;

    final int REQUEST_CODE = 564;
    final int REQUEST_SCANNER = 999;
    private SimpleDateFormat dateFormatter;
    private List<Stock> salesReportList = new ArrayList<>();
    private List<Stock> salesFinal = new ArrayList<>();
    private com.example.pakaianbagus.presentation.home.inputpenjualan.adapter.SalesReportAdapter salesReportAdapter;
    private List<Discount> discounts = new ArrayList<>();

    public InputHarianSalesFragment() {

    }

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.input_harian_sales_fragment, container, false);
        ButterKnife.bind(this, rootView);

        dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        salesReportModels = new ArrayList<>();

        try {
            userId = Session.get("UserId").getValue();
            placeId = Session.get(Constanta.TOKO).getValue();
            brandId = Session.get(Constanta.BRAND).getValue();
        } catch (SessionNotFoundException e) {
            e.printStackTrace();
        }

        try {
            if (Session.get("RoleId").getValue().equals(SessionManagement.ROLE_SPG)) {
                btnSubmit.setVisibility(View.VISIBLE);
                btnDownload.setVisibility(View.GONE);
                swipeRefresh.setOnRefreshListener(() -> swipeRefresh.setRefreshing(false));
            } else if (Session.get("RoleId").getValue().equals(SessionManagement.ROLE_SALES)) {
                btnSubmit.setVisibility(View.VISIBLE);
                btnDownload.setVisibility(View.GONE);
                tabPenjualan.setVisibility(View.GONE);
                layLine.setVisibility(View.GONE);
                swipeRefresh.setOnRefreshListener(() -> swipeRefresh.setRefreshing(false));
            } else {
                btnSubmit.setVisibility(View.GONE);
                btnDownload.setVisibility(View.VISIBLE);
                getPenjualan();

                swipeRefresh.setOnRefreshListener(() -> {
                    salesReportModels.clear();
                    getPenjualan();
                });
            }

        } catch (SessionNotFoundException e) {
            e.printStackTrace();
        }

        getData();

        id = Objects.requireNonNull(getArguments()).getString("id");
        limitHutang = Objects.requireNonNull(getArguments()).getString("limit");
        totalHutang = Objects.requireNonNull(getArguments()).getString("totalHutang");

        getCurrentDateChecklist();

        @SuppressLint("WrongConstant") LinearLayoutManager salesLayoutManager = new LinearLayoutManager(getContext(), LinearLayout.VERTICAL, false);
        recyclerView.setLayoutManager(salesLayoutManager);
        salesReportAdapter = new com.example.pakaianbagus.presentation.home.inputpenjualan.adapter.SalesReportAdapter(salesReportList, getContext(), InputHarianSalesFragment.this, discounts);
        recyclerView.setAdapter(salesReportAdapter);

        return rootView;
    }

    private void getData() {
        Loading.show(getContext());
        Date c = Calendar.getInstance().getTime();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
        String formattedDate = df.format(c);
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dfT = new SimpleDateFormat("ss");
        String formattedTime = dfT.format(c);
        NoTrx = "T" + formattedDate + "/AA/" + formattedTime;
        Log.d("NO TRX", "T" + formattedDate + "/AA/" + formattedTime);
        getDiscount();
    }

    private void getDiscount() {
        discounts.clear();
        InputHelper.getDiscount(new RestCallback<ApiResponse<List<Discount>>>() {
            @Override
            public void onSuccess(Headers headers, ApiResponse<List<Discount>> body) {
                Loading.hide(getContext());
                Log.d("Discount", body.getMessage());
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

    @OnClick(R.id.btnSubmit)
    void onClickSubmit() {
        showDialog();
        Button tunai = dialog.findViewById(R.id.btnTunai);
        tunai.setOnClickListener(v -> {
            payment_method = new Constanta().CASH;
            doSalesReport();
            dialog.dismiss();
        });
        Button hutang = dialog.findViewById(R.id.btnHutang);
        hutang.setOnClickListener(v -> {
            payment_method = new Constanta().HUTANG;
            doSalesReport();
            dialog.dismiss();
        });
        ImageView close = dialog.findViewById(R.id.imgClose);
        close.setOnClickListener(v -> dialog.dismiss());
    }

    private void doSalesReport() {
        if (salesFinal.size() > 0) {
            Loading.show(getContext());

            //StringBuilder from = new StringBuilder();
            StringBuilder description = new StringBuilder();
            StringBuilder stock_id = new StringBuilder();
            StringBuilder qty = new StringBuilder();
            StringBuilder price = new StringBuilder();
            int totalQty = 0;
            int totalPrice = 0;

            int from = salesFinal.get(0).getMPlaceId();

            List<Details> detailList = new ArrayList<>();

            for (int x = 0; x < salesFinal.size(); x++) {
                //from.append(salesFinal.get(x).getMPlaceId()).append(",");
                totalQty = salesFinal.get(x).getQty() + totalQty;
                totalPrice = salesFinal.get(x).getTotal() + totalPrice;
                description.append(salesFinal.get(x).getDescription()).append(",");
                stock_id.append(salesFinal.get(x).getId()).append("");
                qty.append(salesFinal.get(x).getQty()).append("");
                price.append(salesFinal.get(x).getPrice()).append("");

                detailList.add(new Details(salesFinal.get(x).getQty() + "", salesFinal.get(x).getPrice() + "", salesFinal.get(x).getId() + ""));
            }

            Log.d("doSalesReport: ", String.valueOf(totalQty));
            Log.d("doSalesReport: ", String.valueOf(totalPrice));

            TransactionModel transactionModel;
            transactionModel = new TransactionModel(userId, id, NoTrx, dateFormatter.format(myCalendar.getTime()), String.valueOf(from),
                    String.valueOf(totalQty), String.valueOf(totalPrice), description.toString(), "1", payment_method, detailList);

            int mLimit = Integer.parseInt(limitHutang) - Integer.parseInt(totalHutang);
            if (payment_method.equals(new Constanta().CASH)){
                InputHelper.postSalesReportJson(transactionModel, new RestCallback<ApiResponse>() {
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
            } else if (payment_method.equals(new Constanta().HUTANG)){
                if (mLimit > totalPrice || mLimit == totalPrice){
                    InputHelper.postSalesReportJson(transactionModel, new RestCallback<ApiResponse>() {
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
                    Loading.hide(getContext());
                    Toast.makeText(getContext(), "Transaksi melebihi limit hutang", Toast.LENGTH_SHORT).show();
                }
            }
        } else {
            Toast.makeText(getContext(), "Tidak ada data yang harus di laporkan", Toast.LENGTH_SHORT).show();
        }

    }

//    private void getListTransaction(){
//        swipeRefresh.setRefreshing(true);
//        PenjualanHelper.getListTransaction(new RestCallback<ApiResponse<List<Transaction>>>() {
//            @Override
//            public void onSuccess(Headers headers, ApiResponse<List<Transaction>> body) {
//                swipeRefresh.setRefreshing(false);
//                if (body.getData() != null){
//                    List<Transaction> res = body.getData();
//
//                    if (res.size() < 1){
//                        tvNoData.setVisibility(View.VISIBLE);
//                    } else {
//                        tvNoData.setVisibility(View.GONE);
//                    }
//
//                    for (int i = 0; i < res.size(); i++){
//                        Transaction penjualanResponse = res.get(i);
//                        salesReportModels.add(new SalesReportModel(penjualanResponse.getId(),
//                                penjualanResponse.getDescription(),
//                                penjualanResponse.getTransaksi_detail().get(i).getQty(),
//                                penjualanResponse.getTransaksi_detail().get(i).getDiscount(),
//                                penjualanResponse.getTransaksi_detail().get(i).getHarga(),
//                                penjualanResponse.getTotal_harga()));
//                    }
//
//                    SalesReportAdapter adapter = new SalesReportAdapter(salesReportModels, getContext());
//                    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),
//                            LinearLayout.VERTICAL,
//                            false));
//                    recyclerView.setAdapter(adapter);
//                }
//            }
//
//            @Override
//            public void onFailed(ErrorResponse error) {
//
//            }
//
//            @Override
//            public void onCanceled() {
//
//            }
//        });
//    }

    private void getPenjualan() {
        swipeRefresh.setRefreshing(true);
        PenjualanHelper.getPenjualan(id, limit, offset, new RestCallback<ApiResponse<List<PenjualanResponse>>>() {
            @SuppressLint("WrongConstant")
            @Override
            public void onSuccess(Headers headers, ApiResponse<List<PenjualanResponse>> body) {
                swipeRefresh.setRefreshing(false);
                if (body.getData() != null) {
                    List<PenjualanResponse> res = body.getData();

                    if (res.size() < 1) {
                        tvNoData.setVisibility(View.VISIBLE);
                    } else {
                        tvNoData.setVisibility(View.GONE);
                    }

                    for (int i = 0; i < res.size(); i++) {
                        PenjualanResponse penjualanResponse = res.get(i);
                        salesReportModels.add(new SalesReportModel(penjualanResponse.getTransaksi_detail().get(i).getId_transaksi(),
                                penjualanResponse.getTanggal(),
                                penjualanResponse.getTransaksi_detail().get(i).getQty(),
                                penjualanResponse.getTransaksi_detail().get(i).getDiscount(),
                                penjualanResponse.getTransaksi_detail().get(i).getHarga(),
                                penjualanResponse.getTotal_harga()));
                    }

                    SalesReportAdapter adapter = new SalesReportAdapter(salesReportModels, getContext());
                    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),
                            LinearLayout.VERTICAL,
                            false));
                    recyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onFailed(ErrorResponse error) {
                swipeRefresh.setRefreshing(false);
                Log.d("TAG ERROR", error.getMessage());
            }

            @Override
            public void onCanceled() {

            }
        });
    }

    private void getCurrentDateChecklist() {
        Date c = Calendar.getInstance().getTime();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat df = new SimpleDateFormat("yyyy MMMM dd");
        String formattedDate = df.format(c);
        tvDate.setText(formattedDate);
    }

    @OnClick(R.id.toolbar_back)
    public void toolbarBack() {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = Objects.requireNonNull(fm).beginTransaction();
        PenjualanListTokoSalesFragment penjualanListTokoFragment = new PenjualanListTokoSalesFragment();
        ft.setCustomAnimations(R.animator.fade_in, R.animator.fade_out);
        ft.replace(R.id.baseLayoutInputHarian, penjualanListTokoFragment);
        ft.commit();
    }

    @SuppressLint("SetTextI18n")
    @OnClick(R.id.toolbar_scan)
    public void onClickToolbarInput() {
        myCalendar = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener date = (view, year, month, dayOfMonth) -> {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, month);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            if (ContextCompat.checkSelfPermission(Objects.requireNonNull(getContext()), Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(Objects.requireNonNull(getActivity()), new String[]{Manifest.permission.CAMERA}, REQUEST_CODE);
            } else {
                Intent intent = new Intent(getContext(), Scanner.class);
                intent.putExtra("limit", limitHutang);
                Log.d("SCAN Limit", limitHutang);
                startActivityForResult(intent, REQUEST_SCANNER);
            }
        };

        new DatePickerDialog(Objects.requireNonNull(getActivity()), date, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_SCANNER && resultCode == Activity.RESULT_OK) {
            String resultData = data.getStringExtra("scan_data");
            limitHutang = data.getStringExtra("limit");
            Log.d("onActivityResult", limitHutang);
            addListFromBarcode(resultData);
        }
    }

    private void addListFromBarcode(String resultData) {
        Loading.show(getContext());
        InputHelper.getDetailStock(resultData, placeId, brandId, new RestCallback<ApiResponse<List<Stock>>>() {
            @Override
            public void onSuccess(Headers headers, ApiResponse<List<Stock>> body) {
                Loading.hide(getContext());
                if (body.getData().size() > 0) {
                    Stock stokToko = body.getData().get(0);
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
                        tvNoData.setVisibility(View.GONE);
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

    @OnClick(R.id.tabPenjualan)
    public void tabPenjualan() {
        Bundle bundle = new Bundle();
        bundle.putString("id", id);
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = Objects.requireNonNull(fm).beginTransaction();
        PenjualanKompetitorFragment penjualanFragment = new PenjualanKompetitorFragment();
        penjualanFragment.setArguments(bundle);
        ft.setCustomAnimations(R.animator.fade_in, R.animator.fade_out);
        ft.replace(R.id.baseLayoutInputHarian, penjualanFragment);
        ft.commit();
    }

    @OnClick(R.id.btnMore)
    public void btnMore() {
        View v = rootView.findViewById(R.id.btnMore);
        PopupMenu pm = new PopupMenu(Objects.requireNonNull(getActivity()), v);
        pm.getMenuInflater().inflate(R.menu.menu_options, pm.getMenu());
        pm.setOnMenuItemClickListener(menuItem -> {
            if (menuItem.getItemId() == R.id.navigation_ubah) {
                Toast.makeText(getActivity(), String.valueOf(menuItem.getTitle()), Toast.LENGTH_SHORT).show();
            }
            return true;
        });
        pm.show();
    }

    private void showDialog() {
        dialog = new Dialog(Objects.requireNonNull(getActivity()));
        //set content
        dialog.setContentView(R.layout.dialog_metode_bayar);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(Objects.requireNonNull(dialog.getWindow()).getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }

    public void salesData(List<Stock> stokTokoList) {
        this.salesFinal = stokTokoList;
        tvNoData.setVisibility(View.GONE);
    }
}

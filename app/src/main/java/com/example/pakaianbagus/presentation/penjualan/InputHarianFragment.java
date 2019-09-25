package com.example.pakaianbagus.presentation.penjualan;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pakaianbagus.R;
import com.example.pakaianbagus.api.InputHelper;
import com.example.pakaianbagus.models.ApiResponse;
import com.example.pakaianbagus.models.Discount;
import com.example.pakaianbagus.models.SalesReport;
import com.example.pakaianbagus.models.api.penjualankompetitor.Kompetitor;
import com.example.pakaianbagus.models.api.salesreport.Detail;
import com.example.pakaianbagus.models.api.salesreport.SalesReportResponse;
import com.example.pakaianbagus.models.stock.Item;
import com.example.pakaianbagus.models.stock.Stock;
import com.example.pakaianbagus.presentation.home.inputpenjualan.adapter.PenjualanKompetitorAdapter;
import com.example.pakaianbagus.presentation.home.inputpenjualan.adapter.SalesReportAdapter;
import com.example.pakaianbagus.util.Constanta;
import com.example.pakaianbagus.util.DateUtils;
import com.example.pakaianbagus.util.SessionManagement;
import com.example.pakaianbagus.util.dialog.Loading;
import com.rezkyatinnov.kyandroid.reztrofit.ErrorResponse;
import com.rezkyatinnov.kyandroid.reztrofit.RestCallback;
import com.rezkyatinnov.kyandroid.session.Session;
import com.rezkyatinnov.kyandroid.session.SessionNotFoundException;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Headers;

public class InputHarianFragment extends Fragment {

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
    @BindView(R.id.btnFilter)
    ImageView btnFilter;
    @BindView(R.id.toolbar_back)
    ImageView toolbar_back;
    @BindView(R.id.rvSales)
    RecyclerView rvSales;
    @BindView(R.id.rvPenjualan)
    RecyclerView rvPenjualan;
    @BindView(R.id.btnSubmit)
    Button btnSubmit;

    private View rootView;
    private String userId;
    private String date;
    private String placeId;
    private String brandId;
    private String roleId;
    private boolean isSpg;
    private int pager;
    private List<Stock> salesReportList = new ArrayList<>();
    private List<Stock> salesReportTemp = new ArrayList<>();
    private List<Kompetitor> kompetitorList = new ArrayList<>();
    //private List<Kompetitor> kompetitorListTemp = new ArrayList<>();
    private List<Discount> discounts = new ArrayList<>();
    private SalesReportAdapter salesReportAdapter;
    private PenjualanKompetitorAdapter kompetitorAdapter;
    private Dialog dialog;

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.input_penjualan_fragment, container, false);
        ButterKnife.bind(this, rootView);

        date = Objects.requireNonNull(getArguments()).getString("date");
        toolbar_back.setVisibility(View.GONE);

        try {
            userId = Session.get(Constanta.USER_ID).getValue();
            roleId = Session.get(Constanta.ROLE_ID).getValue();
            isSpg = roleId.equals(SessionManagement.ROLE_SPG);
            if (isSpg) {
                placeId = Session.get(Constanta.TOKO).getValue();
                brandId = Session.get(Constanta.BRAND).getValue();
            } else {
                placeId = Objects.requireNonNull(getArguments()).getString("id_toko");
                brandId = Objects.requireNonNull(getArguments()).getString("id_brand");
            }
        } catch (SessionNotFoundException e) {
            e.printStackTrace();
            placeId = "1";
            brandId = "1";
        }

        tvDate.setText(new DateUtils().formatDateStringToString(date, "yyyy-MM-dd", "yyyy MMMM dd"));

        viewScreen(1);

        LinearLayoutManager salesLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        rvSales.setLayoutManager(salesLayoutManager);
        salesReportAdapter = new SalesReportAdapter(salesReportList, getContext(), InputHarianFragment.this, discounts);
        rvSales.setAdapter(salesReportAdapter);

        LinearLayoutManager kompetitorlayoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        rvPenjualan.setLayoutManager(kompetitorlayoutManager);
        kompetitorAdapter = new PenjualanKompetitorAdapter(kompetitorList, getContext(), InputHarianFragment.this);
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
        InputHelper.getPenjualanKompetitor(userId, date, new RestCallback<ApiResponse<List<Kompetitor>>>() {
            @Override
            public void onSuccess(Headers headers, ApiResponse<List<Kompetitor>> body) {
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
        InputHelper.getSalesReport(placeId, date, new RestCallback<ApiResponse<List<SalesReportResponse>>>() {
            @Override
            public void onSuccess(Headers headers, ApiResponse<List<SalesReportResponse>> body) {
                if (body.getData().size() > 0) {
                    List<SalesReportResponse> responses = body.getData();
                    for (int x = 0; x < responses.size(); x++) {
                        SalesReportResponse reportResponse = body.getData().get(x);

                        for (int y = 0; y < reportResponse.getDetails().size(); y++) {
                            Item item = new Item();
                            item.setName(reportResponse.getDetails().get(y).getStock().getItem().getName());
                            item.setImage(reportResponse.getDetails().get(y).getStock().getItem().getImage());

                            Stock stokToko = new Stock();
                            stokToko.setId(reportResponse.getDetails().get(y).getStock().getId());
                            stokToko.setMPlaceId(reportResponse.getDetails().get(y).getStock().getMPlaceId());
                            stokToko.setMItemId(reportResponse.getDetails().get(y).getStock().getMItemId());
                            stokToko.setItem(item);
                            stokToko.setArticleCode(reportResponse.getDetails().get(y).getStock().getArticleCode());
                            stokToko.setPrice(reportResponse.getDetails().get(y).getPrice());
                            stokToko.setQty(reportResponse.getDetails().get(y).getQty());
                            stokToko.setNew(false);

                            salesReportList.add(stokToko);
                        }
                    }
                    salesReportAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailed(ErrorResponse error) {
                Log.d("onFailed: ", error.toString());
            }

            @Override
            public void onCanceled() {

            }
        });
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

    @OnClick(R.id.btnFilter)
    public void clickFilterBtn() {
        Calendar newCalendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(Objects.requireNonNull(getContext()),
                (view, year, monthOfYear, dayOfMonth) -> {
                    Calendar newDate = Calendar.getInstance();
                    newDate.set(year, monthOfYear, dayOfMonth);
                    date = new DateUtils().formatDateToString(newDate.getTime(), "yyyy-MM-dd");
                    Loading.show(getContext());
                    if (getKompetitorData()) {
                        Loading.hide(getContext());
                    }
                },
                newCalendar.get(Calendar.YEAR),
                newCalendar.get(Calendar.MONTH),
                newCalendar.get(Calendar.DAY_OF_MONTH));

        datePickerDialog.show();
    }

    private void viewScreen(int page) {
        btnAdd.setVisibility(View.GONE);
        btnBarcode.setVisibility(View.GONE);
        btnFilter.setVisibility(View.VISIBLE);
        pager = page;
        if (page == 1) {
            rvSales.setVisibility(View.VISIBLE);
            rvPenjualan.setVisibility(View.GONE);
            tabSalesReport.setTextColor(getResources().getColor(R.color.DarkSlateBlue));
            tabPenjualan.setTextColor(getResources().getColor(R.color.Background));
            btnSubmit.setVisibility(View.VISIBLE);
        } else if (page == 2) {
            rvSales.setVisibility(View.GONE);
            rvPenjualan.setVisibility(View.VISIBLE);
            tabSalesReport.setTextColor(getResources().getColor(R.color.Background));
            tabPenjualan.setTextColor(getResources().getColor(R.color.DarkSlateBlue));
            btnSubmit.setVisibility(View.GONE);
        }
    }

    public void salesData(List<Stock> stockList) {
        salesReportTemp.clear();
        for (int x = 0; x < stockList.size(); x++) {
            if (stockList.get(x).isNew()) {
                salesReportTemp.add(stockList.get(x));
            }
        }
    }

    public void addPenjualanKompetitor(String id, Kompetitor kompetitor) {
        Loading.show(getContext());
        InputHelper.postUpdatePenjualanKompetitor(id, kompetitor, new RestCallback<ApiResponse>() {
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
}

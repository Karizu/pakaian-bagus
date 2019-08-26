package com.example.pakaianbagus.presentation.penjualan;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
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
import com.example.pakaianbagus.api.PenjualanHelper;
import com.example.pakaianbagus.models.ApiResponse;
import com.example.pakaianbagus.models.PenjualanResponse;
import com.example.pakaianbagus.models.SalesReportModel;
import com.example.pakaianbagus.presentation.penjualan.adapter.SalesReportAdapter;
import com.example.pakaianbagus.util.SessionManagement;
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

public class InputHarianFragment extends Fragment {

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

    int limit = 10;
    int offset = 0;
    Dialog dialog;
    Calendar myCalendar;
    EditText startDate;
    EditText endDate;
    String id;

    public InputHarianFragment() {

    }

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.input_harian_fragment, container, false);
        ButterKnife.bind(this, rootView);

        try {
            if (Session.get("RoleId").getValue().equals(SessionManagement.ROLE_SPG) ||
                    Session.get("RoleId").getValue().equals(SessionManagement.ROLE_SALES)) {
                btnSubmit.setVisibility(View.VISIBLE);
                btnDownload.setVisibility(View.GONE);
            } else {
                btnSubmit.setVisibility(View.GONE);
                btnDownload.setVisibility(View.VISIBLE);
            }

        } catch (SessionNotFoundException e) {
            e.printStackTrace();
        }

        id = Objects.requireNonNull(getArguments()).getString("id");

        salesReportModels = new ArrayList<>();
        getCurrentDateChecklist();
        getPenjualan();

        swipeRefresh.setOnRefreshListener(() -> {
            salesReportModels.clear();
            getPenjualan();
        });

        return rootView;
    }

//    private void setRecylerView(){
//        for (int i = 0; i < 20; i++){
//            stockOpnameModels.add(new StockOpnameModel("Celana Jeans", "2 pcs"));
//        }
//
//        StockOpnameAdapter stockOpnameAdapter = new StockOpnameAdapter(stockOpnameModels, getContext());
//        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),
//                LinearLayout.VERTICAL,
//                false));
//        recyclerView.setAdapter(stockOpnameAdapter);
//    }

    private void getPenjualan(){
        swipeRefresh.setRefreshing(true);
        PenjualanHelper.getPenjualan(id, limit, offset, new RestCallback<ApiResponse<List<PenjualanResponse>>>() {
            @Override
            public void onSuccess(Headers headers, ApiResponse<List<PenjualanResponse>> body) {
                swipeRefresh.setRefreshing(false);
                if (body.getData() != null){
                    List<PenjualanResponse> res = body.getData();

                    if (res.size() < 1){
                        tvNoData.setVisibility(View.VISIBLE);
                    } else {
                        tvNoData.setVisibility(View.GONE);
                    }

                    for (int i = 0; i < res.size(); i++){
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
    public void toolbarBack(){
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = Objects.requireNonNull(fm).beginTransaction();
        PenjualanListTokoFragment penjualanListTokoFragment = new PenjualanListTokoFragment();
        ft.setCustomAnimations(R.animator.fade_in, R.animator.fade_out);
        ft.replace(R.id.baseLayoutInputHarian, penjualanListTokoFragment);
        ft.commit();
    }

    @SuppressLint("SetTextI18n")
    @OnClick(R.id.toolbar_scan)
    public void onClickToolbarInput(){
//        showDialog();
        myCalendar = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener date = (view, year, month, dayOfMonth) -> {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, month);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
//            updateLabel();
            Intent intent = new Intent(getActivity(), ScanBarcodeActivity.class);
            intent.putExtra("mode", "PENJUALAN");
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        };

        new DatePickerDialog(Objects.requireNonNull(getActivity()), date, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void updateLabel() {
        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        startDate.setText(sdf.format(myCalendar.getTime()));
    }

    private void updateLabel2() {
        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        endDate.setText(sdf.format(myCalendar.getTime()));
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
        dialog.setContentView(R.layout.dialog_filter_penjualan);
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
}

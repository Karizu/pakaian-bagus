package com.example.pakaianbagus.presentation.home.piutang;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.pakaianbagus.R;
import com.example.pakaianbagus.api.InputHelper;
import com.example.pakaianbagus.models.ApiResponse;
import com.example.pakaianbagus.models.DetailInvoiceModel;
import com.example.pakaianbagus.models.api.salesreport.Detail;
import com.example.pakaianbagus.presentation.home.piutang.adapter.HistoryInvoiceAdapter;
import com.rezkyatinnov.kyandroid.reztrofit.ErrorResponse;
import com.rezkyatinnov.kyandroid.reztrofit.RestCallback;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Headers;

public class HistoryInvoiceFragment extends Fragment {

    View rootView;
    Dialog dialog;
    private List<DetailInvoiceModel> detailInvoiceModels;
    private String id, amount, hutang, name, noTrx, trxId;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.swipeRefresh)
    SwipeRefreshLayout swipeRefresh;
    @BindView(R.id.tvNoData)
    TextView tvNoData;
    @BindView(R.id.toolbar_title)
    TextView toolbar_title;
    @BindView(R.id.btnPembayaran)
    TextView btnPembayaran;
    @BindView(R.id.tv_code_invoice)
    TextView tv_code_invoice;
    @BindView(R.id.tvHeader)
    TextView tvHeader;
    @BindView(R.id.tvSisaPiutang)
    TextView tvSisaPiutang;

    public HistoryInvoiceFragment() {

    }

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.list_invoice_fragment, container, false);
        ButterKnife.bind(this, rootView);

        btnPembayaran.setVisibility(View.GONE);

        id = Objects.requireNonNull(getArguments()).getString("id");
        name = getArguments().getString("name");
        amount = getArguments().getString("amount");
        hutang = getArguments().getString("hutang");
        trxId = getArguments().getString("trxId");

        int mAmount = Integer.parseInt(amount);
        noTrx = getArguments().getString("noTrx");
        toolbar_title.setText(name);
        tv_code_invoice.setText(noTrx);
        tvSisaPiutang.setText("Rp. " + NumberFormat.getNumberInstance(Locale.US).format(mAmount));
        tvHeader.setText("HISTORY PEMBAYARAN");

        detailInvoiceModels = new ArrayList<>();
        getDetailTransaction();

        swipeRefresh.setOnRefreshListener(() -> {
            detailInvoiceModels.clear();
            getDetailTransaction();
        });

        return rootView;
    }

    private void getDetailTransaction(){
        swipeRefresh.setRefreshing(true);
        InputHelper.getDetailTransaction(trxId, new RestCallback<ApiResponse<com.example.pakaianbagus.models.Transaction>>() {
            @SuppressLint("WrongConstant")
            @Override
            public void onSuccess(Headers headers, ApiResponse<com.example.pakaianbagus.models.Transaction> body) {
                swipeRefresh.setRefreshing(false);
                com.example.pakaianbagus.models.Transaction transaction = body.getData();
                List<Detail> res = transaction.getDetails();

                for (int i = 0; i < res.size(); i++){
                    Detail detail = res.get(i);
                    detailInvoiceModels.add(new DetailInvoiceModel(detail.getId(),
                            detail.getQty(),
                            detail.getPrice(),
                            detail.getStock().getItem().getName(),
                            detail.getStock().getItem().getImage(),
                            Integer.parseInt(transaction.getTotalPrice())));
                }

                HistoryInvoiceAdapter historyInvoiceAdapter = new HistoryInvoiceAdapter(detailInvoiceModels, getContext());
                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),
                        LinearLayout.VERTICAL,
                        false));
                recyclerView.setAdapter(historyInvoiceAdapter);
            }

            @Override
            public void onFailed(ErrorResponse error) {
                swipeRefresh.setRefreshing(false);
                Log.d("Error Detail Trx", error.getMessage());
            }

            @Override
            public void onCanceled() {

            }
        });
    }

    @OnClick(R.id.toolbar_back)
    public void toolbarBack(){
        Bundle bundle = new Bundle();
        bundle.putString("id", id);
        bundle.putString("amount", amount);
        bundle.putString("name", name);
        bundle.putString("hutang", hutang);
        bundle.putString("noTrx", noTrx);

        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = Objects.requireNonNull(fm).beginTransaction();
        ListInvoiceFragment stockListTokoFragment = new ListInvoiceFragment();
        stockListTokoFragment.setArguments(bundle);
        ft.setCustomAnimations(R.animator.fade_in, R.animator.fade_out);
        ft.replace(R.id.baseLayoutStock, stockListTokoFragment);
        ft.commit();
    }

    private void showDialog(int layout) {
        dialog = new Dialog(Objects.requireNonNull(getActivity()));
        //set content
        dialog.setContentView(layout);
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

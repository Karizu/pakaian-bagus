package com.example.pakaianbagus.presentation.home.piutang;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.example.pakaianbagus.api.PenjualanHelper;
import com.example.pakaianbagus.models.AccountsReceivable;
import com.example.pakaianbagus.models.ApiResponse;
import com.example.pakaianbagus.models.InvoiceModels;
import com.example.pakaianbagus.presentation.home.piutang.adapter.ListInvoiceAdapter;
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

public class ListInvoiceFragment extends Fragment {

    View rootView;
    private List<InvoiceModels> invoiceModels;
    private String id, name, hutang;
    private int totalHutang, mAmount;

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
    @BindView(R.id.tvSisaPiutang)
    TextView tvSisaPiutang;

    public ListInvoiceFragment() {

    }

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.list_invoice_fragment, container, false);
        ButterKnife.bind(this, rootView);

        id = Objects.requireNonNull(getArguments()).getString("id");
        name = getArguments().getString("name");
        hutang = getArguments().getString("hutang");
        toolbar_title.setText(name);

        invoiceModels = new ArrayList<>();
        getListPiutang();

        swipeRefresh.setOnRefreshListener(() -> {
            invoiceModels.clear();
            mAmount = 0;
            getListPiutang();
        });

        return rootView;
    }

    private void getListPiutang(){
        swipeRefresh.setRefreshing(true);
        PenjualanHelper.getPiutang(id, new RestCallback<ApiResponse<List<AccountsReceivable>>>() {
            @SuppressLint({"SetTextI18n", "WrongConstant"})
            @Override
            public void onSuccess(Headers headers, ApiResponse<List<AccountsReceivable>> body) {
                swipeRefresh.setRefreshing(false);
                List<AccountsReceivable> res = body.getData();

                if (res.size() > 0){
                    tvNoData.setVisibility(View.GONE);
                } else {
                    tvNoData.setVisibility(View.VISIBLE);
                }

                for (int i = 0; i < res.size(); i++){
                    AccountsReceivable response = res.get(i);
                    invoiceModels.add(new InvoiceModels(response.getId(),
                            response.getTransactionId(),
                            response.getDate(),
                            response.getAmount(),
                            response.getTransaction().getNo()));

                    mAmount += Integer.parseInt(response.getAmount());
                }

                int mHutang = Integer.parseInt(hutang);
                tvSisaPiutang.setText("Rp. " + NumberFormat.getNumberInstance(Locale.US).format(mHutang));

                ListInvoiceAdapter stockOpnameAdapter = new ListInvoiceAdapter(invoiceModels, getContext(), ListInvoiceFragment.this);
                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),
                        LinearLayout.VERTICAL,
                        false));
                recyclerView.setAdapter(stockOpnameAdapter);
            }

            @Override
            public void onFailed(ErrorResponse error) {
                swipeRefresh.setRefreshing(false);
                Log.d("Error Piutang", error.getMessage());
            }

            @Override
            public void onCanceled() {

            }
        });
    }

    @OnClick(R.id.btnPembayaran)
    void onClickPembayaran(){
        Bundle bundle = new Bundle();
        bundle.putString("id", id);
        bundle.putString("name", name);
        bundle.putString("hutang", hutang);

        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = Objects.requireNonNull(fm).beginTransaction();
        PembayaranInvoiceFragment pembayaranInvoiceFragment = new PembayaranInvoiceFragment();
        pembayaranInvoiceFragment.setArguments(bundle);
        ft.setCustomAnimations(R.animator.fade_in, R.animator.fade_out);
        ft.replace(R.id.baseLayoutStock, pembayaranInvoiceFragment).addToBackStack("pembayaranInvoiceFragment");
        ft.commit();
    }

    public void onClickItem(String amount, String noTrx, String trxId){
        Bundle bundle = new Bundle();
        bundle.putString("id", id);
        bundle.putString("name", name);
        bundle.putString("amount", amount);
        bundle.putString("noTrx", noTrx);
        bundle.putString("trxId", trxId);
        bundle.putString("hutang", String.valueOf(mAmount));

        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = Objects.requireNonNull(fm).beginTransaction();
        HistoryInvoiceFragment historyInvoiceFragment = new HistoryInvoiceFragment();
        historyInvoiceFragment.setArguments(bundle);
        ft.setCustomAnimations(R.animator.fade_in, R.animator.fade_out);
        ft.replace(R.id.baseLayoutStock, historyInvoiceFragment).addToBackStack("historyInvoiceFragment");
        ft.commit();
    }

    @OnClick(R.id.toolbar_back)
    public void toolbarBack(){
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = Objects.requireNonNull(fm).beginTransaction();
        PiutangListTokoFragment stockListTokoFragment = new PiutangListTokoFragment();
        ft.setCustomAnimations(R.animator.fade_in, R.animator.fade_out);
        ft.replace(R.id.baseLayoutStock, stockListTokoFragment);
        ft.commit();
    }
}

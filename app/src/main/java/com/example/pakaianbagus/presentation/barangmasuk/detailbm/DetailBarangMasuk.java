package com.example.pakaianbagus.presentation.barangmasuk.detailbm;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.pakaianbagus.R;
import com.example.pakaianbagus.api.BarangHelper;
import com.example.pakaianbagus.models.ApiResponse;
import com.example.pakaianbagus.models.DetailBarangMasukModel;
import com.example.pakaianbagus.models.PenerimaanBarangResponse;
import com.example.pakaianbagus.models.StokPenerimaan;
import com.example.pakaianbagus.presentation.barangmasuk.BarangMasukFragment;
import com.example.pakaianbagus.presentation.barangmasuk.ListTokoBMFragment;
import com.example.pakaianbagus.presentation.barangmasuk.detailbm.adapter.DetailBarangMasukAdapter;
import com.example.pakaianbagus.util.dialog.Loading;
import com.rezkyatinnov.kyandroid.reztrofit.ErrorResponse;
import com.rezkyatinnov.kyandroid.reztrofit.RestCallback;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Headers;

public class DetailBarangMasuk extends Fragment {

    View rootView;
    Dialog dialog;
    String id;

    private List<DetailBarangMasukModel> detailBarangMasukModels;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.tvDate)
    TextView tvDate;

    public DetailBarangMasuk() {
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.detail_barang_masuk, container, false);
        ButterKnife.bind(this, rootView);

        detailBarangMasukModels = new ArrayList<>();

        assert getArguments() != null;
        id = getArguments().getString("id");

        getCurrentDateChecklist();
        if (id != null){
            getDetailBM(id);
        }

        return rootView;
    }

    private void getCurrentDateChecklist() {
        Date c = Calendar.getInstance().getTime();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat df = new SimpleDateFormat("yyyy MMMM dd");
        String formattedDate = df.format(c);
        tvDate.setText(formattedDate);
    }

    private void getDetailBM(String id){
        Loading.show(getContext());
        BarangHelper.getDetailBarangMasuk(id, new RestCallback<ApiResponse<PenerimaanBarangResponse>>() {
            @Override
            public void onSuccess(Headers headers, ApiResponse<PenerimaanBarangResponse> body) {
                Loading.hide(getContext());
                if (body.getData() != null){
                    PenerimaanBarangResponse barangResponse = body.getData();
                    List<StokPenerimaan> res = barangResponse.getStok_artikel();
                    for (int i = 0; i < res.size(); i++){
                        StokPenerimaan stokPenerimaan = res.get(i);
                        detailBarangMasukModels.add(new DetailBarangMasukModel(stokPenerimaan.getNama_barang(),
                                stokPenerimaan.getQty()));
                    }
                    DetailBarangMasukAdapter detailBarangMasukAdapter = new DetailBarangMasukAdapter(detailBarangMasukModels, getContext());
                    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),
                            LinearLayout.VERTICAL,
                            false));
                    recyclerView.setAdapter(detailBarangMasukAdapter);
                }
            }

            @Override
            public void onFailed(ErrorResponse error) {
                Loading.hide(getContext());
                Log.d("onFailed Detail BM", error.getMessage());
            }

            @Override
            public void onCanceled() {

            }
        });
    }

    @OnClick(R.id.toolbar_back)
    public void toolbarBack() {
        Bundle bundle = new Bundle();
        bundle.putString("id", id);

        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = Objects.requireNonNull(fm).beginTransaction();
        BarangMasukFragment bmFragment = new BarangMasukFragment();
        bmFragment.setArguments(bundle);
        ft.setCustomAnimations(R.animator.fade_in, R.animator.fade_out);
        ft.replace(R.id.baseLayoutDetailBM, bmFragment);
        ft.commit();
    }

    @OnClick(R.id.btnVerifikasi)
    public void btnVerifikasi(){
        showDialog();
        Button btnOK = dialog.findViewById(R.id.btnOK);
        btnOK.setOnClickListener(v -> dialog.dismiss());
    }

    private void showDialog() {
        dialog = new Dialog(Objects.requireNonNull(getActivity()));
        //set content
        dialog.setContentView(R.layout.dialog_submit_verifikasi);
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

package com.example.pakaianbagus.presentation.barangmasuk.detailbm;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.example.pakaianbagus.R;
import com.example.pakaianbagus.models.DetailBarangMasukModel;
import com.example.pakaianbagus.presentation.barangmasuk.BarangMasukFragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DetailBarangMasuk extends Fragment {

    View rootView;
    Dialog dialog;
    String id, idBarangMasuk;

    private List<DetailBarangMasukModel> detailBarangMasukModels;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.tvDate)
    TextView tvDate;
    @BindView(R.id.tvNoData)
    TextView tvNoData;
    @BindView(R.id.swipeRefresh)
    SwipeRefreshLayout swipeRefresh;

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
        idBarangMasuk = getArguments().getString("id_barang_masuk");

        getCurrentDateChecklist();
        if (id != null) {
            getDetailBM(id);
            swipeRefresh.setOnRefreshListener(() -> {
                detailBarangMasukModels.clear();
                getDetailBM(id);
            });
        }

        return rootView;
    }

    private void getCurrentDateChecklist() {
        Date c = Calendar.getInstance().getTime();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat df = new SimpleDateFormat("yyyy MMMM dd");
        String formattedDate = df.format(c);
        tvDate.setText(formattedDate);
    }

    private void getDetailBM(String id) {
        swipeRefresh.setRefreshing(true);
        /*BarangHelper.getDetailBarangMasuk(id, new RestCallback<ApiResponse<PenerimaanBarangResponse>>() {
            @Override
            public void onSuccess(Headers headers, ApiResponse<PenerimaanBarangResponse> body) {
                swipeRefresh.setRefreshing(false);
                if (body.getData() != null) {
                    PenerimaanBarangResponse barangResponse = body.getData();
                    List<StokPenerimaan> res = barangResponse.getStok_artikel();

                    if (res.size() < 1) {
                        tvNoData.setVisibility(View.VISIBLE);
                    } else {
                        tvNoData.setVisibility(View.GONE);
                    }

                    for (int i = 0; i < res.size(); i++) {
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
                swipeRefresh.setRefreshing(false);
                Log.d("onFailed Detail BM", error.getMessage());
            }

            @Override
            public void onCanceled() {

            }
        });*/
    }

    @OnClick(R.id.toolbar_back)
    public void toolbarBack() {
        Bundle bundle = new Bundle();
        bundle.putString("id", idBarangMasuk);

        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = Objects.requireNonNull(fm).beginTransaction();
        BarangMasukFragment bmFragment = new BarangMasukFragment();
        bmFragment.setArguments(bundle);
        ft.setCustomAnimations(R.animator.fade_in, R.animator.fade_out);
        ft.replace(R.id.baseLayoutDetailBM, bmFragment);
        ft.commit();
    }

    @OnClick(R.id.btnVerifikasi)
    public void btnVerifikasi() {
        showDialog(R.layout.dialog_submit_verifikasi);
        Button btnOK = dialog.findViewById(R.id.btnOK);
        btnOK.setOnClickListener(v -> dialog.dismiss());
    }

    @OnClick(R.id.btnAttachment)
    void onClickAttachment() {
        showDialog(R.layout.dialog_attach_barang);
    }

    private void showDialog(int layout) {
        dialog = new Dialog(Objects.requireNonNull(getActivity()));
        //set content
        dialog.setContentView(layout);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);
        if (layout == R.layout.dialog_attach_barang) {
            Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        } else {
            Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        }
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(Objects.requireNonNull(dialog.getWindow()).getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }
}

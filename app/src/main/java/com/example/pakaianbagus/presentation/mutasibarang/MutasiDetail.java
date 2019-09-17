package com.example.pakaianbagus.presentation.mutasibarang;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pakaianbagus.R;
import com.example.pakaianbagus.api.MutasiHelper;
import com.example.pakaianbagus.models.ApiResponse;
import com.example.pakaianbagus.models.MutationRequest;
import com.example.pakaianbagus.models.api.mutation.detail.Detail;
import com.example.pakaianbagus.models.api.mutation.detail.MutationDetail;
import com.example.pakaianbagus.presentation.mutasibarang.adapter.MutasiDetailAdapter;
import com.example.pakaianbagus.presentation.mutasibarang.adapter.MutasiImageAdapter;
import com.example.pakaianbagus.util.dialog.Loading;
import com.rezkyatinnov.kyandroid.reztrofit.ErrorResponse;
import com.rezkyatinnov.kyandroid.reztrofit.RestCallback;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Headers;

/**
 * A simple {@link Fragment} subclass.
 */
public class MutasiDetail extends Fragment {

    @BindView(R.id.tvNamaToko)
    TextView tvNamaToko;
    @BindView(R.id.tvNumberPengiriman)
    TextView tvNumberPengiriman;
    @BindView(R.id.tvDeskripsi)
    TextView tvDeskripsi;
    @BindView(R.id.tvNoReg)
    TextView tvNoReg;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    private int mutationId;
    private List<Detail> details = new ArrayList<>();
    private MutasiDetailAdapter mutasiDetailAdapter;
    private MutationDetail mutationDetail;
    private Dialog dialog;

    public MutasiDetail() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.mutation_detail_fragment, container, false);
        ButterKnife.bind(this, view);

        mutationId = Objects.requireNonNull(getArguments()).getInt("mutationId");
        getData();

        LinearLayoutManager salesLayoutManager = new LinearLayoutManager(getContext(), LinearLayout.VERTICAL, false);
        recyclerView.setLayoutManager(salesLayoutManager);
        mutasiDetailAdapter = new MutasiDetailAdapter(details, getContext(), MutasiDetail.this);
        recyclerView.setAdapter(mutasiDetailAdapter);

        return view;
    }

    private void getData() {
        Loading.show(getContext());
        MutasiHelper.getDetailMutation(mutationId, new RestCallback<ApiResponse<MutationDetail>>() {
            @Override
            public void onSuccess(Headers headers, ApiResponse<MutationDetail> body) {
                Loading.hide(getContext());

                mutationDetail = body.getData();

                tvNamaToko.setText(mutationDetail.getExpedition().getName());
                tvNumberPengiriman.setText(mutationDetail.getNo());
                tvDeskripsi.setText(mutationDetail.getDescription());
                tvNoReg.setText(mutationDetail.getReceiptNo());

                details.addAll(mutationDetail.getDetails());
                mutasiDetailAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailed(ErrorResponse error) {
                Log.d("onFailed: ", error.toString());
                Loading.hide(getContext());
            }

            @Override
            public void onCanceled() {

            }
        });
    }

    public void setDetailsTemp(List<Detail> detailsTemp) {
        mutationDetail.setDetails(detailsTemp);
    }

    @OnClick(R.id.btnAttachment)
    public void onAttachmentClick() {
        showDialog(R.layout.dialog_attach_barang);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
        RecyclerView dialogrv = dialog.findViewById(R.id.recyclerViewDialog);
        dialogrv.setLayoutManager(gridLayoutManager);
        MutasiImageAdapter mutasiImageAdapter = new MutasiImageAdapter(details, getContext());
        dialogrv.setAdapter(mutasiImageAdapter);
    }

    @OnClick(R.id.btnSubmit)
    public void onSubmitClick() {
        Loading.show(getContext());

        MutationRequest mutationRequest = new MutationRequest();
        mutationRequest.setmExpeditionId(mutationDetail.getMExpeditionId());
        mutationRequest.setNo(mutationDetail.getNo());
        mutationRequest.setDate(mutationDetail.getDate());
        mutationRequest.setFrom(mutationDetail.getFrom().getId());
        mutationRequest.setTo(mutationDetail.getTo().getId());
        mutationRequest.setType(mutationDetail.getType());
        mutationRequest.setTotalQty(mutationDetail.getTotalQty());
        mutationRequest.setTotalPrice(mutationDetail.getTotalPrice());
        mutationRequest.setDescription(mutationDetail.getDescription());
        mutationRequest.setReceiptNo(mutationDetail.getReceiptNo());
        mutationRequest.setReceiptNote(mutationDetail.getReceiptNote());
        mutationRequest.setReceiptProof(mutationDetail.getReceiptProof());
        mutationRequest.setDetails(mutationDetail.getDetails());

        MutasiHelper.postMutasi(mutationRequest, new RestCallback<ApiResponse>() {
            @Override
            public void onSuccess(Headers headers, ApiResponse body) {
                Loading.hide(getContext());
                Toast.makeText(getContext(), "Berhasil mengirimkan data mutasi", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailed(ErrorResponse error) {
                Loading.hide(getContext());
                Toast.makeText(getContext(), "Gagal mengirimkan data mutasi", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCanceled() {

            }
        });
    }

    @OnClick(R.id.toolbar_back)
    public void toolbarBack() {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = Objects.requireNonNull(fm).beginTransaction();
        MutasiBarangSPG homeFragment = new MutasiBarangSPG();
        ft.setCustomAnimations(R.animator.fade_in, R.animator.fade_out);
        ft.replace(R.id.baseLayout, homeFragment);
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

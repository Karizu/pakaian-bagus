package com.example.pakaianbagus.presentation.mutasibarang;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pakaianbagus.R;
import com.example.pakaianbagus.api.MutasiHelper;
import com.example.pakaianbagus.models.ApiResponse;
import com.example.pakaianbagus.models.api.mutation.Mutation;
import com.example.pakaianbagus.models.api.mutation.detail.Detail;
import com.example.pakaianbagus.models.api.mutation.detail.MutationDetail;
import com.example.pakaianbagus.models.mutation.MutationResponse;
import com.example.pakaianbagus.presentation.mutasibarang.adapter.MutasiDetailAdapter;
import com.example.pakaianbagus.presentation.mutasibarang.adapter.MutasiImageAdapter;
import com.example.pakaianbagus.util.Constanta;
import com.example.pakaianbagus.util.SessionManagement;
import com.example.pakaianbagus.util.dialog.Loading;
import com.rezkyatinnov.kyandroid.reztrofit.ErrorResponse;
import com.rezkyatinnov.kyandroid.reztrofit.RestCallback;
import com.rezkyatinnov.kyandroid.session.Session;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.LongFunction;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import static com.example.pakaianbagus.util.Constanta.MUTASI_BARANG_SPG;

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
    @BindView(R.id.btnSubmit)
    Button btnSubmit;
    @BindView(R.id.tvStatusDesc)
    TextView tvStatusDesc;

    private int mutationId, status;
    private String flag = null;
    private String idToko, idBrand, flagMutasi;
    private List<Detail> details = new ArrayList<>();
    private MutasiDetailAdapter mutasiDetailAdapter;
    private MutationDetail mutationDetail;
    private Dialog dialog;

    public MutasiDetail() {
        // Required empty public constructor
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.mutation_detail_fragment, container, false);
        ButterKnife.bind(this, view);

        try {
            mutationId = Objects.requireNonNull(getArguments()).getInt("mutationId");
            status = Objects.requireNonNull(getArguments()).getInt("status");
            flagMutasi = Objects.requireNonNull(getArguments()).getString(Constanta.FLAG_MUTASI);
            if (!Objects.equals(flagMutasi, MUTASI_BARANG_SPG)){
                if (status == Constanta.MUTASI_VERIFIED_BY_SPG){
                    btnSubmit.setVisibility(View.VISIBLE);
                    tvStatusDesc.setText(Constanta.WAITING_VERIFY_BY_KOORDINATOR);
                    tvStatusDesc.setTextColor(ContextCompat.getColor(Objects.requireNonNull(getContext()), R.color.Red));
                } else {
                    btnSubmit.setVisibility(View.GONE);
                    if (status == Constanta.MUTASI_NOT_VERIFIED){
                        tvStatusDesc.setText(Constanta.WAITING_VERIFY_BY_SPG);
                        tvStatusDesc.setTextColor(ContextCompat.getColor(Objects.requireNonNull(getContext()), R.color.Red));
                    } else if (status == Constanta.MUTASI_VERIFIED_BY_KOORDINATOR){
                        tvStatusDesc.setText(Constanta.MUTATION_HAS_BEEN_RECEIVED);
                        tvStatusDesc.setTextColor(ContextCompat.getColor(Objects.requireNonNull(getContext()), R.color.Green));
                    }
                }
            } else {
                if (status == Constanta.MUTASI_VERIFIED_BY_SPG){
                    btnSubmit.setVisibility(View.GONE);
                    tvStatusDesc.setText(Constanta.WAITING_VERIFY_BY_KOORDINATOR);
                    tvStatusDesc.setTextColor(ContextCompat.getColor(Objects.requireNonNull(getContext()), R.color.Red));
                } else {
                    if (status == Constanta.MUTASI_NOT_VERIFIED){
                        btnSubmit.setVisibility(View.VISIBLE);
                        tvStatusDesc.setText(Constanta.WAITING_VERIFY_BY_SPG);
                        tvStatusDesc.setTextColor(ContextCompat.getColor(Objects.requireNonNull(getContext()), R.color.Red));
                    } else if (status == Constanta.MUTASI_VERIFIED_BY_KOORDINATOR){
                        btnSubmit.setVisibility(View.GONE);
                        tvStatusDesc.setText(Constanta.MUTATION_HAS_BEEN_RECEIVED);
                        tvStatusDesc.setTextColor(ContextCompat.getColor(Objects.requireNonNull(getContext()), R.color.Green));
                    }
                }
            }

        } catch (Exception e){
            e.printStackTrace();
        }

        try {
            idToko = Objects.requireNonNull(getArguments()).getString("store_id");
            idBrand = Objects.requireNonNull(getArguments()).getString("brand_id");
        } catch (Exception e){
            e.printStackTrace();
        }

        try {
            if (Session.get(Constanta.ROLE_ID).getValue().equals(SessionManagement.ROLE_SPG)) {
                idBrand = Session.get(Constanta.BRAND).getValue();
                idToko = Session.get(Constanta.TOKO).getValue();
                Log.d("ID", idBrand+" "+idToko);
            }
        } catch (Exception e){
            e.printStackTrace();
        }

        getData();

        @SuppressLint("WrongConstant") LinearLayoutManager salesLayoutManager = new LinearLayoutManager(getContext(), LinearLayout.VERTICAL, false);
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
    void onAttachmentClick() {
        showDialog(R.layout.dialog_attach_barang);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
        RecyclerView dialogrv = dialog.findViewById(R.id.recyclerViewDialog);
        dialogrv.setLayoutManager(gridLayoutManager);
        MutasiImageAdapter mutasiImageAdapter = new MutasiImageAdapter(details, getContext());
        dialogrv.setAdapter(mutasiImageAdapter);
    }

    @OnClick(R.id.btnSubmit)
    void onSubmitClick() {

        showDialog(R.layout.dialog_submit_verifikasi);
        Button btnOK = dialog.findViewById(R.id.btnOK);
        btnOK.setOnClickListener(view -> {
            Loading.show(getContext());

            RequestBody requestBody;
            Fragment homeFragment;
            Bundle bundle = new Bundle();
            if (!flagMutasi.equals(MUTASI_BARANG_SPG)){
                homeFragment = new MutasiBarangFragment();
                bundle.putString("store_id", idToko);
                bundle.putString("brand_id", idBrand);
                requestBody = new MultipartBody.Builder()
                        .setType(MultipartBody.FORM)
                        .addFormDataPart("status", "5")
                        .build();
            } else {
                homeFragment = new MutasiBarangSPG();
                requestBody = new MultipartBody.Builder()
                        .setType(MultipartBody.FORM)
                        .addFormDataPart("status", "4")
                        .build();
            }

            MutasiHelper.verifyMutation(mutationId+"", requestBody, new RestCallback<ApiResponse<MutationResponse>>() {
                @Override
                public void onSuccess(Headers headers, ApiResponse<MutationResponse> body) {
                    Loading.hide(getContext());
                    dialog.dismiss();
                    try {
                        FragmentManager fm = getFragmentManager();
                        FragmentTransaction ft = Objects.requireNonNull(fm).beginTransaction();
                        homeFragment.setArguments(bundle);
                        ft.setCustomAnimations(R.animator.fade_in, R.animator.fade_out);
                        ft.replace(R.id.baseLayout, homeFragment);
                        ft.commit();
                        Toast.makeText(getContext(), body.getMessage(), Toast.LENGTH_SHORT).show();
                    } catch (Exception e){
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailed(ErrorResponse error) {
                    Loading.hide(getContext());
                    error.getMessage();
                }

                @Override
                public void onCanceled() {

                }
            });
        });

//        MutationRequest mutationRequest = new MutationRequest();
//        mutationRequest.setmExpeditionId(mutationDetail.getMExpeditionId());
//        mutationRequest.setNo(mutationDetail.getNo());
//        mutationRequest.setDate(mutationDetail.getDate());
//        mutationRequest.setFrom(mutationDetail.getFrom().getId());
//        mutationRequest.setTo(mutationDetail.getTo().getId());
//        mutationRequest.setType(mutationDetail.getType());
//        mutationRequest.setTotalQty(mutationDetail.getTotalQty());
//        mutationRequest.setTotalPrice(mutationDetail.getTotalPrice());
//        mutationRequest.setDescription(mutationDetail.getDescription());
//        mutationRequest.setReceiptNo(mutationDetail.getReceiptNo());
//        mutationRequest.setReceiptNote(mutationDetail.getReceiptNote());
//        mutationRequest.setReceiptProof(mutationDetail.getReceiptProof());
//        mutationRequest.setDetails(mutationDetail.getDetails());
//
//        MutasiHelper.postMutasi(mutationRequest, new RestCallback<ApiResponse>() {
//            @Override
//            public void onSuccess(Headers headers, ApiResponse body) {
//                Loading.hide(getContext());
//                Toast.makeText(getContext(), "Berhasil mengirimkan data mutasi", Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onFailed(ErrorResponse error) {
//                Loading.hide(getContext());
//                Toast.makeText(getContext(), "Gagal mengirimkan data mutasi", Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onCanceled() {
//
//            }
//        });
    }

    @OnClick(R.id.toolbar_back)
    public void toolbarBack() {
//        Bundle bundle = new Bundle();
//        bundle.putString("store_id", idToko);
//        bundle.putString("brand_id", idBrand);
//        bundle.putString(Constanta.FLAG_MUTASI, flagMutasi);
//        FragmentManager fm = getFragmentManager();
//        FragmentTransaction ft = Objects.requireNonNull(fm).beginTransaction();
//        Fragment homeFragment;
//
//        if (flag != null){
//            homeFragment = new MutasiBarangFragment();
//        } else {
//            homeFragment = new MutasiBarangSPG();
//        }
//        homeFragment.setArguments(bundle);
//        ft.setCustomAnimations(R.animator.fade_in, R.animator.fade_out);
//        ft.replace(R.id.baseLayout, homeFragment);
//        ft.commit();
        int count = Objects.requireNonNull(getFragmentManager()).getBackStackEntryCount();
        if (count != 0) {
            Objects.requireNonNull(getFragmentManager()).popBackStack();
        }
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

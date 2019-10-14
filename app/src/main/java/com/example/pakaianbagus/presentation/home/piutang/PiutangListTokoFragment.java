package com.example.pakaianbagus.presentation.home.piutang;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pakaianbagus.R;
import com.example.pakaianbagus.api.InputHelper;
import com.example.pakaianbagus.models.ApiResponse;
import com.example.pakaianbagus.models.KatalogTokoModel;
import com.example.pakaianbagus.models.transaction.Member;
import com.example.pakaianbagus.presentation.home.HomeFragment;
import com.example.pakaianbagus.presentation.home.piutang.adapter.PiutangTokoAdapter;
import com.example.pakaianbagus.util.IOnBackPressed;
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

public class PiutangListTokoFragment extends Fragment implements IOnBackPressed {


    private List<KatalogTokoModel> katalogTokoModels;
    Dialog dialog;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.tvDate)
    TextView tvDate;
    @BindView(R.id.toolbar_title)
    TextView toolbar_title;
    @BindView(R.id.toolbar_back)
    ImageView toolbar_back;

    public PiutangListTokoFragment() {
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.spg_fragment, container, false);
        ButterKnife.bind(this, rootView);
        toolbar_title.setText("TOKO");
        katalogTokoModels = new ArrayList<>();

        getCurrentDateChecklist();
        getListToko();

        return rootView;
    }

    private void getCurrentDateChecklist() {
        Date c = Calendar.getInstance().getTime();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat df = new SimpleDateFormat("yyyy MMMM dd");
        String formattedDate = df.format(c);
        tvDate.setText(formattedDate);
    }

    public void getListToko() {
        Loading.show(getActivity());
        InputHelper.getMember(new RestCallback<ApiResponse<List<Member>>>() {
            @SuppressLint("WrongConstant")
            @Override
            public void onSuccess(Headers headers, ApiResponse<List<Member>> body) {
                Loading.hide(getActivity());
                if (Objects.requireNonNull(body).getData() != null) {
                    List<Member> tokoResponse = body.getData();
                    for (int i = 0; i < tokoResponse.size(); i++) {
                        Member dataToko = tokoResponse.get(i);
                        katalogTokoModels.add(new KatalogTokoModel(dataToko.getId(), dataToko.getFullname(), dataToko.getAddress(), dataToko.getLimit(), dataToko.getAccountsReceivable()));
                    }
                    PiutangTokoAdapter spgTokoAdapter = new PiutangTokoAdapter(katalogTokoModels, getContext(), PiutangListTokoFragment.this);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),
                            LinearLayout.VERTICAL, false));
                    recyclerView.setAdapter(spgTokoAdapter);
                }
            }

            @Override
            public void onFailed(ErrorResponse error) {
                Loading.hide(getActivity());
                Log.d("List Toko Katalog", error.getMessage());
            }

            @Override
            public void onCanceled() {

            }
        });
    }

    public void onClickItem(String id, String name, String hutang) {
        Bundle bundle = new Bundle();
        bundle.putString("id", id);
        bundle.putString("name", name);
        bundle.putString("hutang", hutang);

        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = Objects.requireNonNull(fm).beginTransaction();
        ListInvoiceFragment listInvoiceFragment = new ListInvoiceFragment();
        listInvoiceFragment.setArguments(bundle);
        ft.setCustomAnimations(R.animator.fade_in, R.animator.fade_out);
        ft.replace(R.id.baseLayoutSpg, listInvoiceFragment).addToBackStack("listInvoiceFragment");
        ft.commit();
    }

    @OnClick(R.id.toolbar_back)
    public void toolbarBack() {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = Objects.requireNonNull(fm).beginTransaction();
        HomeFragment spgListBrandFragment = new HomeFragment();
        ft.setCustomAnimations(R.animator.fade_in, R.animator.fade_out);
        ft.replace(R.id.baseLayoutSpg, spgListBrandFragment);
        ft.commit();
    }

    @Override
    public boolean onBackPressed() {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = Objects.requireNonNull(fm).beginTransaction();
        HomeFragment homeFragment = new HomeFragment();
        ft.setCustomAnimations(R.animator.fade_in, R.animator.fade_out);
        ft.replace(R.id.baseLayoutSpg, homeFragment);
        ft.commit();

        return false;
    }
}

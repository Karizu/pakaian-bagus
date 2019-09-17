package com.example.pakaianbagus.presentation.mutasibarang;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.pakaianbagus.R;
import com.example.pakaianbagus.api.MutasiHelper;
import com.example.pakaianbagus.models.ApiResponse;
import com.example.pakaianbagus.models.api.mutation.Mutation;
import com.example.pakaianbagus.presentation.mutasibarang.adapter.MutasiSpgAdapter;
import com.example.pakaianbagus.util.Constanta;
import com.example.pakaianbagus.util.dialog.Loading;
import com.rezkyatinnov.kyandroid.reztrofit.ErrorResponse;
import com.rezkyatinnov.kyandroid.reztrofit.RestCallback;
import com.rezkyatinnov.kyandroid.session.Session;
import com.rezkyatinnov.kyandroid.session.SessionNotFoundException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Headers;

/**
 * A simple {@link Fragment} subclass.
 */
public class MutasiBarangSPG extends Fragment {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    private List<Mutation> mutationList = new ArrayList<>();
    private MutasiSpgAdapter mutasiSpgAdapter;
    private String idBrand, idToko;

    public MutasiBarangSPG() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.mutasi_barang_spg_fragment, container, false);
        ButterKnife.bind(this, view);

        mutasiSpgAdapter = new MutasiSpgAdapter(mutationList, getContext(), MutasiBarangSPG.this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),
                LinearLayout.VERTICAL, false));
        recyclerView.setAdapter(mutasiSpgAdapter);

        try {
            idBrand = Session.get(Constanta.BRAND).getValue();
            idToko = Session.get(Constanta.TOKO).getValue();
        } catch (SessionNotFoundException e) {
            e.printStackTrace();
            idBrand = "1";
            idToko = "1";
        }

        getData();

        return view;
    }

    private void getData() {
        Loading.show(getContext());
        List<Integer> status = new ArrayList<>();
        status.add(3);
        status.add(4);
        MutasiHelper.getListMutation(status, idBrand, idToko, new RestCallback<ApiResponse<List<Mutation>>>() {
            @Override
            public void onSuccess(Headers headers, ApiResponse<List<Mutation>> body) {
                Loading.hide(getContext());
                mutationList.clear();
                mutationList.addAll(body.getData());
                mutasiSpgAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailed(ErrorResponse error) {
                Loading.hide(getContext());
            }

            @Override
            public void onCanceled() {

            }
        });
    }

    public void onClickItem(Mutation mutation) {
        Bundle bundle = new Bundle();
        bundle.putInt("mutationId", mutation.getId());

        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = Objects.requireNonNull(fm).beginTransaction();
        Fragment fragment = new MutasiDetail();
        fragment.setArguments(bundle);
        ft.setCustomAnimations(R.animator.fade_in, R.animator.fade_out);
        ft.replace(R.id.baseLayout, fragment);
        ft.commit();
    }
}
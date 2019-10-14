package com.example.pakaianbagus.presentation.mutasibarang;

import android.annotation.SuppressLint;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.pakaianbagus.R;
import com.example.pakaianbagus.api.MutasiHelper;
import com.example.pakaianbagus.models.ApiResponse;
import com.example.pakaianbagus.models.api.mutation.Mutation;
import com.example.pakaianbagus.presentation.mutasibarang.adapter.MutasiSpgAdapter;
import com.example.pakaianbagus.util.Constanta;
import com.example.pakaianbagus.util.SessionManagement;
import com.example.pakaianbagus.util.dialog.Loading;
import com.rezkyatinnov.kyandroid.reztrofit.ErrorResponse;
import com.rezkyatinnov.kyandroid.reztrofit.RestCallback;
import com.rezkyatinnov.kyandroid.session.Session;
import com.rezkyatinnov.kyandroid.session.SessionNotFoundException;

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
    @BindView(R.id.tvNoData)
    TextView tvNoData;

    private List<Mutation> mutationList = new ArrayList<>();
    private MutasiSpgAdapter mutasiSpgAdapter;
    private String idBrand, idToko;

    public MutasiBarangSPG() {
        // Required empty public constructor
    }

    @SuppressLint("WrongConstant")
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
            if (Session.get(Constanta.ROLE_ID).getValue().equals(SessionManagement.ROLE_SPG)){
                idBrand = Session.get(Constanta.BRAND).getValue();
                idToko = Session.get(Constanta.TOKO).getValue();
            }
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
        MutasiHelper.getListMutation(status, idBrand, idToko, new RestCallback<ApiResponse<List<Mutation>>>() {
            @Override
            public void onSuccess(Headers headers, ApiResponse<List<Mutation>> body) {
                Loading.hide(getContext());
                try {
                    mutationList.clear();
                    mutationList.addAll(body.getData());
                    mutasiSpgAdapter.notifyDataSetChanged();
                    if (body.getData().size() < 1){
                        tvNoData.setVisibility(View.VISIBLE);
                    } else {
                        tvNoData.setVisibility(View.GONE);
                    }
                } catch (Exception e){
                    e.printStackTrace();
                }

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
        bundle.putInt("status", mutation.getStatus());

        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = Objects.requireNonNull(fm).beginTransaction();
        Fragment fragment = new MutasiDetail();
        fragment.setArguments(bundle);
        ft.setCustomAnimations(R.animator.fade_in, R.animator.fade_out).addToBackStack("fragment");
        ft.replace(R.id.baseLayout, fragment);
        ft.commit();
    }
}

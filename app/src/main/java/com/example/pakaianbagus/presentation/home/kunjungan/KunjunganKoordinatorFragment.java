package com.example.pakaianbagus.presentation.home.kunjungan;

import android.annotation.SuppressLint;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.pakaianbagus.R;
import com.example.pakaianbagus.api.HomeHelper;
import com.example.pakaianbagus.models.ApiResponse;
import com.example.pakaianbagus.models.KunjunganKoordinatorModel;
import com.example.pakaianbagus.models.User;
import com.example.pakaianbagus.presentation.home.HomeFragment;
import com.example.pakaianbagus.presentation.home.kunjungan.adapter.KunjunganKoordinatorAdapter;
import com.example.pakaianbagus.presentation.home.kunjungan.tambahkunjungan.TambahKunjunganFragment;
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

public class KunjunganKoordinatorFragment extends Fragment {

    private List<KunjunganKoordinatorModel> kunjunganModels;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    public KunjunganKoordinatorFragment() {

    }

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.kunjungan_fragment, container, false);
        ButterKnife.bind(this, rootView);

        TextView tvTitle = rootView.findViewById(R.id.toolbar_title);
        if ((getArguments() != null ? getArguments().getString("kunjunganKoordinator") : null)!= null){
            tvTitle.setText(getArguments().getString("kunjunganKoordinator"));
        }

        kunjunganModels = new ArrayList<>();
        getListKoordinator();

        return rootView;
    }

//    private void setRecylerView(){
//        for (int i = 0; i < 20; i++){
//            kunjunganModels.add(new KunjunganModel("Toko Adil Makmur", "08:00 | 29 Juni 2019"));
//        }
//
//        KunjunganAdapter kunjunganAdapter = new KunjunganAdapter(kunjunganModels, getContext());
//        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),
//                LinearLayout.VERTICAL,
//                false));
//        recyclerView.setAdapter(kunjunganAdapter);
//    }

    private void getListKoordinator(){
        Loading.show(getContext());
        HomeHelper.getListKoordinator(new RestCallback<ApiResponse<List<User>>>() {
            @Override
            public void onSuccess(Headers headers, ApiResponse<List<User>> body) {
                Loading.hide(getContext());
                if (body.getData() != null){
                    List<User> users = body.getData();
                    for (int i = 0; i < users.size(); i++){
                        User user = users.get(i);
                        kunjunganModels.add(new KunjunganKoordinatorModel(user.getId(),
                                user.getRoleId(),
                                user.getFirstname(),
                                user.getEmail()));
                    }
                    KunjunganKoordinatorAdapter kunjunganAdapter = new KunjunganKoordinatorAdapter(kunjunganModels, getContext(), KunjunganKoordinatorFragment.this);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),
                            LinearLayout.VERTICAL,
                            false));
                    recyclerView.setAdapter(kunjunganAdapter);
                }
            }

            @Override
            public void onFailed(ErrorResponse error) {
                Loading.hide(getContext());
                Log.d("OnFailed List Kunjungan", error.getMessage());
            }

            @Override
            public void onCanceled() {

            }
        });
    }

    public void onClickItem(String id){
        Bundle bundle = new Bundle();
        bundle.putString("id", id);

        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = Objects.requireNonNull(fm).beginTransaction();
        KunjunganFragment kunjunganFragment = new KunjunganFragment();
        kunjunganFragment.setArguments(bundle);
        ft.setCustomAnimations(R.animator.fade_in, R.animator.fade_out);
        ft.replace(R.id.baseLayoutSpg, kunjunganFragment);
        ft.commit();
    }

    @OnClick(R.id.toolbar_back)
    public void toolbarBack(){
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = Objects.requireNonNull(fm).beginTransaction();
        HomeFragment homeFragment = new HomeFragment();
        ft.setCustomAnimations(R.animator.fade_in, R.animator.fade_out);
        ft.replace(R.id.baseLayout, homeFragment);
        ft.commit();
    }

    @OnClick(R.id.toolbar_tambah_pengeluaran)
    public void toolbarTambahPengeluaran(){
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = Objects.requireNonNull(fm).beginTransaction();
        TambahKunjunganFragment tambahKunjunganFragment = new TambahKunjunganFragment();
        ft.setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out);
        ft.replace(R.id.baseLayoutKunjungan, tambahKunjunganFragment);
        ft.commit();
    }
}

package com.example.pakaianbagus.presentation.home.spg;

import android.annotation.SuppressLint;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pakaianbagus.R;
import com.example.pakaianbagus.api.MutasiHelper;
import com.example.pakaianbagus.models.ApiResponse;
import com.example.pakaianbagus.models.SpgModel;
import com.example.pakaianbagus.models.usermutation.UserMutationResponse;
import com.example.pakaianbagus.presentation.home.spg.adapter.SpgMutasiAdapter;
import com.example.pakaianbagus.presentation.home.spg.detailspg.DetailSpgFragment;
import com.example.pakaianbagus.util.Constanta;
import com.example.pakaianbagus.util.IOnBackPressed;
import com.example.pakaianbagus.util.dialog.Loading;
import com.rezkyatinnov.kyandroid.reztrofit.ErrorResponse;
import com.rezkyatinnov.kyandroid.reztrofit.RestCallback;
import com.rezkyatinnov.kyandroid.session.Session;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Headers;

public class SpgListMutasiFragment extends Fragment {
    private List<SpgModel> spgModels;
    private String groupId, store_id, brand_id;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.tvNoData)
    TextView tvNoData;
    @BindView(R.id.swipeRefresh)
    SwipeRefreshLayout swipeRefresh;

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.spg_list_mutasi_fragment, container, false);
        ButterKnife.bind(this, rootView);

        try {
            groupId = Session.get(Constanta.GROUP_ID).getValue();
            store_id = Objects.requireNonNull(getArguments()).getString("store_id");
            brand_id = Objects.requireNonNull(getArguments()).getString("brand_id");
        } catch (Exception e){
            e.printStackTrace();
        }

        spgModels = new ArrayList<>();

        swipeRefresh.setOnRefreshListener(() -> {
            spgModels.clear();
            getListUserMutation(groupId, brand_id);
        });

        getListUserMutation(groupId, brand_id);
        return rootView;
    }

    private void getListUserMutation(String group_id, String brand_id){
        swipeRefresh.setRefreshing(true);
        MutasiHelper.getUserMutation(group_id, brand_id, new RestCallback<ApiResponse<List<UserMutationResponse>>>() {
            @SuppressLint("WrongConstant")
            @Override
            public void onSuccess(Headers headers, ApiResponse<List<UserMutationResponse>> body) {
                swipeRefresh.setRefreshing(false);
                try {
                   List<UserMutationResponse> res = body.getData();

                    if (res.size() < 1){
                        tvNoData.setVisibility(View.VISIBLE);
                    } else {
                        tvNoData.setVisibility(View.GONE);
                    }

                   for (int i = 0; i < res.size(); i++){
                       UserMutationResponse mutationRespons = res.get(i);
                       spgModels.add(new SpgModel(mutationRespons.getId()+"",
                               mutationRespons.getUser().getName(),
                               mutationRespons.getFromGroup().getName(),
                               mutationRespons.getToGroup().getName(),
                               mutationRespons.getStatus()));

                       SpgMutasiAdapter spgMutasiAdapter = new SpgMutasiAdapter(spgModels, getContext(), SpgListMutasiFragment.this);
                       recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),
                               LinearLayout.VERTICAL,
                               false));
                       recyclerView.setAdapter(spgMutasiAdapter);
                   }

                } catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailed(ErrorResponse error) {
                swipeRefresh.setRefreshing(false);
                Log.d("TAG FAILED", error.getMessage());
                Toast.makeText(getContext(), "Terjadi gannguan, coba beberapa saat lagi", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCanceled() {

            }
        });
    }

    @OnClick(R.id.toolbar_back)
    public void toolbarBack() {
//        Bundle bundle = new Bundle();
//        bundle.putString("store_id", store_id);
//        bundle.putString("brand_id", brand_id);
//
//        FragmentManager fm = getFragmentManager();
//        FragmentTransaction ft = Objects.requireNonNull(fm).beginTransaction();
//        SpgListTokoFragment listTokoFragment = new SpgListTokoFragment();
//        listTokoFragment.setArguments(bundle);
//        ft.setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out);
//        ft.replace(R.id.baseLayoutSpg, listTokoFragment);
//        ft.commit();
        int count = Objects.requireNonNull(getFragmentManager()).getBackStackEntryCount();
        if (count != 0) {
            Objects.requireNonNull(getFragmentManager()).popBackStack();
        }
    }
}

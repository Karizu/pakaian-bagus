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

import com.example.pakaianbagus.R;
import com.example.pakaianbagus.api.SpgHelper;
import com.example.pakaianbagus.models.ApiResponse;
import com.example.pakaianbagus.models.SpgModel;
import com.example.pakaianbagus.models.auth.Group;
import com.example.pakaianbagus.models.auth.Place;
import com.example.pakaianbagus.models.user.User;
import com.example.pakaianbagus.presentation.home.spg.adapter.SpgAdapter;
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

public class SpgListFragment extends Fragment {

    private List<User> spgModels;
    private String groupId, store_id, brand_id;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.tvTitleHeader)
    TextView tvTitleHeader;
    @BindView(R.id.tvNoData)
    TextView tvNoData;
    @BindView(R.id.swipeRefresh)
    SwipeRefreshLayout swipeRefresh;

    public SpgListFragment() {
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.spg_fragment, container, false);
        ButterKnife.bind(this, rootView);

        try {
            groupId = Session.get(Constanta.GROUP_ID).getValue();
            store_id = Objects.requireNonNull(getArguments()).getString("store_id");
            brand_id = Objects.requireNonNull(getArguments()).getString("brand_id");
        } catch (Exception e){
            e.printStackTrace();
        }

        spgModels = new ArrayList<>();
        tvTitleHeader.setText("LIST SPG");

        getListSpg();

        swipeRefresh.setOnRefreshListener(() -> {
            spgModels.clear();
            getListSpg();
        });

        return rootView;
    }

    private void getListSpg(){
        swipeRefresh.setRefreshing(true);
        SpgHelper.getListSpg(brand_id, store_id, new RestCallback<ApiResponse<List<User>>>() {
            @SuppressLint("WrongConstant")
            @Override
            public void onSuccess(Headers headers, ApiResponse<List<User>> body) {
                swipeRefresh.setRefreshing(false);
                List<User> res = body.getData();

                if (res.size() < 1){
                    tvNoData.setVisibility(View.VISIBLE);
                } else {
                    tvNoData.setVisibility(View.GONE);
                }

                for (int i = 0; i < res.size(); i++){
                    User user = res.get(i);
                    if (user.getRoleId().equals(Constanta.ROLE_SPG)){

                        spgModels.add(user);
                    }

                    SpgAdapter spgAdapter = new SpgAdapter(spgModels, getContext(), SpgListFragment.this);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),
                            LinearLayout.VERTICAL,
                            false));
                    recyclerView.setAdapter(spgAdapter);
                }
            }

            @Override
            public void onFailed(ErrorResponse error) {
                swipeRefresh.setRefreshing(false);
                Log.d("TAG FAILED", error.getMessage());
            }

            @Override
            public void onCanceled() {

            }
        });
    }

    public void onClickLayoutListSPG(String id, String placeId) {
        Bundle bundle = new Bundle();
        bundle.putString("id", id);
        bundle.putString("store_id", store_id);
        bundle.putString("brand_id", brand_id);
        bundle.putString("place_id", placeId);
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = Objects.requireNonNull(fm).beginTransaction();
        DetailSpgFragment detailSpgFragment = new DetailSpgFragment();
        detailSpgFragment.setArguments(bundle);
        ft.setCustomAnimations(R.animator.fade_in, R.animator.fade_out).addToBackStack("detailSpgFragment");
        ft.replace(R.id.baseLayoutSpg, detailSpgFragment);
        ft.commit();
    }

    @OnClick(R.id.toolbar_back)
    public void toolbarBack() {
        Bundle bundle = new Bundle();
        bundle.putString("store_id", store_id);
        bundle.putString("brand_id", brand_id);

        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = Objects.requireNonNull(fm).beginTransaction();
        SpgListTokoFragment listTokoFragment = new SpgListTokoFragment();
        listTokoFragment.setArguments(bundle);
        ft.setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out);
        ft.replace(R.id.baseLayoutSpg, listTokoFragment);
        ft.commit();
    }
}

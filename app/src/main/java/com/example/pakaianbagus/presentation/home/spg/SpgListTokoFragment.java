package com.example.pakaianbagus.presentation.home.spg;

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
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.pakaianbagus.R;
import com.example.pakaianbagus.api.KatalogHelper;
import com.example.pakaianbagus.models.ApiResponse;
import com.example.pakaianbagus.models.KatalogTokoModel;
import com.example.pakaianbagus.models.TokoResponse;
import com.example.pakaianbagus.presentation.home.HomeFragment;
import com.example.pakaianbagus.presentation.home.spg.adapter.SpgTokoAdapter;
import com.example.pakaianbagus.util.IOnBackPressed;
import com.example.pakaianbagus.util.dialog.Loading;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SpgListTokoFragment extends Fragment implements IOnBackPressed {


    private List<KatalogTokoModel> katalogTokoModels;
    Dialog dialog;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.tvDate)
    TextView tvDate;

    public SpgListTokoFragment() {
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.spg_fragment, container, false);
        ButterKnife.bind(this, rootView);

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
        KatalogHelper.getListToko(getContext(), new Callback<ApiResponse<List<TokoResponse>>>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponse<List<TokoResponse>>> call, @NonNull Response<ApiResponse<List<TokoResponse>>> response) {
                Loading.hide(getActivity());
                if (Objects.requireNonNull(response.body()).getData() != null) {
                    List<TokoResponse> tokoResponse = response.body().getData();
                    for (int i = 0; i < tokoResponse.size(); i++) {
                        TokoResponse dataToko = tokoResponse.get(i);
                        if (dataToko.getType().equalsIgnoreCase("S")) {
                            katalogTokoModels.add(new KatalogTokoModel(dataToko.getId(), dataToko.getName(), dataToko.getType()));
                        }
                    }
                    SpgTokoAdapter spgTokoAdapter = new SpgTokoAdapter(katalogTokoModels, getContext(), SpgListTokoFragment.this);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),
                            LinearLayout.VERTICAL, false));
                    recyclerView.setAdapter(spgTokoAdapter);
                }
            }

            @Override
            public void onFailure(@NonNull Call<ApiResponse<List<TokoResponse>>> call, @NonNull Throwable t) {
                Loading.hide(getActivity());
                Log.d("List Toko Katalog", t.getMessage());

            }
        });
    }

    public void onClickItem(String id) {
        showDialog(id);
    }

    public void toListSPG(String id) {
        dialog.dismiss();
        Bundle bundle = new Bundle();
        bundle.putString("id", id);

        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = Objects.requireNonNull(fm).beginTransaction();
        SpgListFragment spgListFragment = new SpgListFragment();
        spgListFragment.setArguments(bundle);
        ft.setCustomAnimations(R.animator.fade_in, R.animator.fade_out);
        ft.replace(R.id.baseLayoutSpg, spgListFragment);
        ft.commit();
    }

    public void toMutasiSPG(String id) {
        dialog.dismiss();
        Bundle bundle = new Bundle();
        bundle.putString("id", id);

        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = Objects.requireNonNull(fm).beginTransaction();
        SpgListMutasiFragment spgListMutasiFragment = new SpgListMutasiFragment();
        spgListMutasiFragment.setArguments(bundle);
        ft.setCustomAnimations(R.animator.fade_in, R.animator.fade_out);
        ft.replace(R.id.baseLayoutSpg, spgListMutasiFragment);
        ft.commit();
    }

    @OnClick(R.id.toolbar_back)
    public void toolbarBack() {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = Objects.requireNonNull(fm).beginTransaction();
        SpgListBrandFragment spgListBrandFragment = new SpgListBrandFragment();
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
        ft.replace(R.id.baseLayout, homeFragment);
        ft.commit();

        return false;
    }

    private void showDialog(String id) {
        dialog = new Dialog(Objects.requireNonNull(getActivity()));
        //set content
        dialog.setContentView(R.layout.dialog_spg);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);

        Button btnListSPG = dialog.findViewById(R.id.btnListSPG);
        Button btnMutasiSPG = dialog.findViewById(R.id.btnMutasiSPG);
        btnListSPG.setOnClickListener(v -> toListSPG(id));
        btnMutasiSPG.setOnClickListener(v -> toMutasiSPG(id));

        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(Objects.requireNonNull(dialog.getWindow()).getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }
}

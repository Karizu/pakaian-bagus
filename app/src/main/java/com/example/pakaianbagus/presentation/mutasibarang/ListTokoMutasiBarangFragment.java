package com.example.pakaianbagus.presentation.mutasibarang;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pakaianbagus.R;
import com.example.pakaianbagus.api.KatalogHelper;
import com.example.pakaianbagus.models.ApiResponse;
import com.example.pakaianbagus.models.KatalogTokoModel;
import com.example.pakaianbagus.models.TokoResponse;
import com.example.pakaianbagus.presentation.home.HomeFragment;
import com.example.pakaianbagus.presentation.home.spg.SpgListBrandFragment;
import com.example.pakaianbagus.presentation.home.spg.SpgListFragment;
import com.example.pakaianbagus.presentation.home.spg.SpgListMutasiFragment;
import com.example.pakaianbagus.presentation.home.spg.adapter.SpgTokoAdapter;
import com.example.pakaianbagus.util.Constanta;
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

public class ListTokoMutasiBarangFragment extends Fragment {


    private List<KatalogTokoModel> katalogTokoModels;
    Dialog dialog;
    String brand_id, flagMutasi;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.tvDate)
    TextView tvDate;
    @BindView(R.id.toolbar_title)
    TextView toolbar_title;

    public ListTokoMutasiBarangFragment() {
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.spg_fragment, container, false);
        ButterKnife.bind(this, rootView);

        toolbar_title.setText("MUTASI BARANG");

        try {
            brand_id = Objects.requireNonNull(getArguments()).getString("brand_id");
            flagMutasi = Objects.requireNonNull(getArguments()).getString(Constanta.FLAG_MUTASI);
        } catch (Exception e){
            e.printStackTrace();
        }

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
        KatalogHelper.getListToko(new Constanta().PLACE_TYPE_SHOP, new Callback<ApiResponse<List<TokoResponse>>>() {
            @SuppressLint("WrongConstant")
            @Override
            public void onResponse(@NonNull Call<ApiResponse<List<TokoResponse>>> call, @NonNull Response<ApiResponse<List<TokoResponse>>> response) {
                Loading.hide(getActivity());
                if (Objects.requireNonNull(response.body()).getData() != null) {
                    List<TokoResponse> tokoResponse = response.body().getData();
                    for (int i = 0; i < tokoResponse.size(); i++) {
                        TokoResponse dataToko = tokoResponse.get(i);
                        katalogTokoModels.add(new KatalogTokoModel(dataToko.getId(), dataToko.getName(), dataToko.getType()));
                    }
                    SpgTokoAdapter spgTokoAdapter = new SpgTokoAdapter(katalogTokoModels, getContext(), ListTokoMutasiBarangFragment.this);
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
        Bundle bundle = new Bundle();
        bundle.putString("store_id", id);
        bundle.putString("brand_id", brand_id);
        bundle.putString(Constanta.FLAG_MUTASI, flagMutasi);

        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = Objects.requireNonNull(fm).beginTransaction();
        MutasiBarangFragment mutasiBarangFragment = new MutasiBarangFragment();
        mutasiBarangFragment.setArguments(bundle);
        ft.setCustomAnimations(R.animator.fade_in, R.animator.fade_out).addToBackStack(null);
        ft.replace(R.id.baseLayoutSpg, mutasiBarangFragment);
        ft.commit();
    }

    @OnClick(R.id.toolbar_back)
    public void toolbarBack() {
//        FragmentManager fm = getFragmentManager();
//        FragmentTransaction ft = Objects.requireNonNull(fm).beginTransaction();
//        ListBrandMutasiBarangFragment listBrandMutasiBarangFragment = new ListBrandMutasiBarangFragment();
//        ft.setCustomAnimations(R.animator.fade_in, R.animator.fade_out);
//        ft.replace(R.id.baseLayoutSpg, listBrandMutasiBarangFragment);
//        ft.commit();
        int count = Objects.requireNonNull(getFragmentManager()).getBackStackEntryCount();
        if (count != 0) {
            Objects.requireNonNull(getFragmentManager()).popBackStack();
        }
    }
}

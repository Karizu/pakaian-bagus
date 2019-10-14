package com.example.pakaianbagus.presentation.barangmasuk;

import android.annotation.SuppressLint;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.pakaianbagus.R;
import com.example.pakaianbagus.api.KatalogHelper;
import com.example.pakaianbagus.models.ApiResponse;
import com.example.pakaianbagus.models.KatalogTokoModel;
import com.example.pakaianbagus.models.TokoResponse;
import com.example.pakaianbagus.presentation.barangmasuk.adapter.ListTokoBMAdapter;
import com.example.pakaianbagus.util.Constanta;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListTokoBMFragment extends Fragment {
    private List<KatalogTokoModel> katalogTokoModels;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.tvDate)
    TextView tvDate;
    @BindView(R.id.toolbar_title)
    TextView toolbar_title;
    @BindView(R.id.toolbar_history)
    ImageView toolbar_history;
    @BindView(R.id.toolbar_back)
    ImageView toolbar_back;
    @BindView(R.id.tvList)
    TextView tvList;
    @BindView(R.id.tvNoData)
    TextView tvNoData;
    @BindView(R.id.swipeRefresh)
    SwipeRefreshLayout swipeRefresh;

    public ListTokoBMFragment() {

    }

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.barang_masuk_fragment, container, false);
        ButterKnife.bind(this, rootView);

        toolbar_title.setText("BARANG MASUK");
        toolbar_history.setVisibility(View.GONE);
        toolbar_back.setVisibility(View.GONE);
        tvList.setText("LIST TOKO");

        katalogTokoModels = new ArrayList<>();
        getCurrentDateChecklist();
        getListToko();

        swipeRefresh.setOnRefreshListener(() -> {
            katalogTokoModels.clear();
            getListToko();
        });

        return rootView;
    }

    private void getCurrentDateChecklist() {
        Date c = Calendar.getInstance().getTime();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat df = new SimpleDateFormat("yyyy MMMM dd");
        String formattedDate = df.format(c);
        tvDate.setText(formattedDate);
    }

    public void getListToko() {
        swipeRefresh.setRefreshing(true);
        KatalogHelper.getListToko(new Constanta().PLACE_TYPE_SHOP, new Callback<ApiResponse<List<TokoResponse>>>() {
            @SuppressLint("WrongConstant")
            @Override
            public void onResponse(@NonNull Call<ApiResponse<List<TokoResponse>>> call, @NonNull Response<ApiResponse<List<TokoResponse>>> response) {
                swipeRefresh.setRefreshing(false);
                if (Objects.requireNonNull(response.body()).getData() != null) {
                    List<TokoResponse> tokoResponse = response.body().getData();

                    if (tokoResponse.size() < 1) {
                        tvNoData.setVisibility(View.VISIBLE);
                    } else {
                        tvNoData.setVisibility(View.GONE);
                    }

                    for (int i = 0; i < tokoResponse.size(); i++) {
                        TokoResponse dataToko = tokoResponse.get(i);
                        katalogTokoModels.add(new KatalogTokoModel(dataToko.getId(), dataToko.getName(), dataToko.getType()));
                    }
                    ListTokoBMAdapter listTokoBMAdapter = new ListTokoBMAdapter(katalogTokoModels, ListTokoBMFragment.this);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),
                            LinearLayout.VERTICAL,
                            false));
                    recyclerView.setAdapter(listTokoBMAdapter);
                }
            }

            @Override
            public void onFailure(@NonNull Call<ApiResponse<List<TokoResponse>>> call, @NonNull Throwable t) {
                swipeRefresh.setRefreshing(false);
                Log.d("List Toko Katalog", t.getMessage());

            }
        });
    }

    public void layoutListBarangMasuk(String id) {
        Bundle bundle = new Bundle();
        bundle.putString("id", id);

        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = Objects.requireNonNull(fm).beginTransaction();
        BarangMasukFragment barangMasukFragment = new BarangMasukFragment();
        barangMasukFragment.setArguments(bundle);
        ft.setCustomAnimations(R.animator.fade_in, R.animator.fade_out);
        ft.replace(R.id.baseLayoutBM, barangMasukFragment);
        ft.commit();
    }
}

package com.example.pakaianbagus.presentation.home.kunjungan;

import android.annotation.SuppressLint;
import android.os.Bundle;
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

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.pakaianbagus.R;
import com.example.pakaianbagus.api.HomeHelper;
import com.example.pakaianbagus.models.ApiResponse;
import com.example.pakaianbagus.models.KunjunganModel;
import com.example.pakaianbagus.models.expenditures.Detail;
import com.example.pakaianbagus.models.expenditures.Expenditures;
import com.example.pakaianbagus.presentation.home.HomeFragment;
import com.example.pakaianbagus.presentation.home.kunjungan.adapter.DetailKunjunganAdapter;
import com.example.pakaianbagus.presentation.home.kunjungan.adapter.KunjunganAdapter;
import com.example.pakaianbagus.presentation.home.kunjungan.tambahkunjungan.TambahKunjunganFragment;
import com.example.pakaianbagus.util.RoundedCornersTransformation;
import com.example.pakaianbagus.util.dialog.Loading;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.pakaianbagus.presentation.home.photocounter.adapter.PhotoAdapter.sCorner;
import static com.example.pakaianbagus.presentation.home.photocounter.adapter.PhotoAdapter.sMargin;

public class DetailKunjunganFragment extends Fragment {


    private List<KunjunganModel> kunjunganModels;
    private List<Detail> detailList = new ArrayList<>();
    private String id;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.tvDateTime)
    TextView tvDateTime;
    @BindView(R.id.tvGroup)
    TextView tvGroup;
    @BindView(R.id.tvBrand)
    TextView tvBrand;
    @BindView(R.id.tvDeskripsi)
    TextView tvDeskripsi;
    @BindView(R.id.imgPhotoCounter)
    ImageView imgPhotoCounter;

    public DetailKunjunganFragment() {

    }

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_detail_kunjungan, container, false);
        ButterKnife.bind(this, rootView);

        try {
            id = Objects.requireNonNull(getArguments()).getString("id");
        } catch (Exception e){
            e.printStackTrace();
        }

        kunjunganModels = new ArrayList<>();
        getDetailKunjungan();

        return rootView;
    }

    private void getDetailKunjungan(){
        Loading.show(getContext());
        HomeHelper.getDetailKunjungan(id, new Callback<ApiResponse<Expenditures>>() {
            @SuppressLint("WrongConstant")
            @Override
            public void onResponse(Call<ApiResponse<Expenditures>> call, Response<ApiResponse<Expenditures>> response) {
                Loading.hide(getContext());
                try {
                    Expenditures expenditures = Objects.requireNonNull(response.body()).getData();
                    List<Detail> res = expenditures.getDetails();

                    tvBrand.setText(expenditures.getBrand().getName());
                    tvDeskripsi.setText(expenditures.getDescription());
                    tvGroup.setText(expenditures.getGroup().getName());
                    tvDateTime.setText(getDate(expenditures.getDate()));
                    Glide.with(Objects.requireNonNull(getContext()))
                            .load(expenditures.getImage())
                            .placeholder(R.drawable.img_default)
                            .apply(RequestOptions.bitmapTransform(
                                    new RoundedCornersTransformation(getContext(), sCorner, sMargin))).into(imgPhotoCounter);

                    for (int i = 0; i < res.size(); i++){
                        Detail detail = res.get(i);
                        Detail mDetail = new Detail();
                        mDetail.setDescription(detail.getDescription());
                        mDetail.setAmount(detail.getAmount());
                        mDetail.setM_expend_id(detail.getM_expend_id());
                        mDetail.setImage(detail.getImage());
                        detailList.add(mDetail);
                    }

                    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayout.VERTICAL, false));
                    DetailKunjunganAdapter adapter = new DetailKunjunganAdapter(detailList, getActivity());
                    recyclerView.setAdapter(adapter);

                } catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Expenditures>> call, Throwable t) {
                Loading.hide(getContext());
                t.printStackTrace();
            }
        });
    }


    @OnClick(R.id.toolbar_back)
    public void toolbarBack(){
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = Objects.requireNonNull(fm).beginTransaction();
        KunjunganFragment homeFragment = new KunjunganFragment();
        ft.setCustomAnimations(R.animator.fade_in, R.animator.fade_out);
        ft.replace(R.id.baseLayout, homeFragment);
        ft.commit();
    }

    @SuppressLint("SimpleDateFormat")
    private static String getDate(String strDate) {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date newDate = null;
        try {
            newDate = format.parse(strDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        format = new SimpleDateFormat("HH:mm | dd MMMM yyyy");
        return format.format(Objects.requireNonNull(newDate));
    }
}

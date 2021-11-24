package com.example.pakaianbagus.presentation.home.spg.detailspg;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pakaianbagus.MainActivity;
import com.example.pakaianbagus.R;
import com.example.pakaianbagus.api.MutasiHelper;
import com.example.pakaianbagus.api.SpgHelper;
import com.example.pakaianbagus.models.ApiResponse;
import com.example.pakaianbagus.models.auth.Group;
import com.example.pakaianbagus.models.auth.Place;
import com.example.pakaianbagus.models.detailspg.AttendanceResponse;
import com.example.pakaianbagus.models.user.User;
import com.example.pakaianbagus.models.usermutation.UserMutationResponse;
import com.example.pakaianbagus.presentation.home.spg.SpgListFragment;
import com.example.pakaianbagus.presentation.home.spg.adapter.SpgAdapter;
import com.example.pakaianbagus.presentation.home.spg.detailspg.adapter.DetailSpgAdapter;
import com.example.pakaianbagus.presentation.home.spg.detailspg.model.DetailSpgModel;
import com.example.pakaianbagus.util.Constanta;
import com.example.pakaianbagus.util.dialog.Loading;
import com.rezkyatinnov.kyandroid.reztrofit.ErrorResponse;
import com.rezkyatinnov.kyandroid.reztrofit.RestCallback;
import com.rezkyatinnov.kyandroid.session.Session;

import java.text.ParseException;
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
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class DetailSpgFragment extends Fragment {

    Dialog dialog;
    String id, store_id, brand_id, placeId;
    private List<DetailSpgModel> detailSpgModels;
    private List<String> tokoList = new ArrayList<>();
    private List<String> tokoIdList = new ArrayList<>();
    String asalToko, asalTokoId;
    private String tujuanTokoId;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.tvName)
    TextView tvName;
    @BindView(R.id.tvDateIn)
    TextView tvDateIn;
    @BindView(R.id.tvNoData)
    TextView tvNoData;
    @BindView(R.id.tvNamaToko)
    TextView tvNamaToko;

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.detail_spg_fragment, container, false);
        ButterKnife.bind(this, rootView);

        try {
            id = Objects.requireNonNull(getArguments()).getString("id");
            placeId = Objects.requireNonNull(getArguments()).getString("place_id");
            store_id = Objects.requireNonNull(getArguments()).getString("store_id");
            brand_id = Objects.requireNonNull(getArguments()).getString("brand_id");
        } catch (Exception e) {
            e.printStackTrace();
        }

        detailSpgModels = new ArrayList<>();

        getDetailSpg();
        getDetailPlace();
        getAttendance();
        getFromPlace(placeId);

        return rootView;
    }

    @OnClick(R.id.toolbar_back)
    public void toolbarBack() {
//        Bundle bundle = new Bundle();
//        bundle.putString("id", id);
//        bundle.putString("store_id", store_id);
//        bundle.putString("brand_id", brand_id);
//        FragmentManager fm = getFragmentManager();
//        FragmentTransaction ft = Objects.requireNonNull(fm).beginTransaction();
//        SpgListFragment spgListFragment = new SpgListFragment();
//        spgListFragment.setArguments(bundle);
//        ft.setCustomAnimations(R.animator.fade_in, R.animator.fade_out);
//        ft.replace(R.id.baseLayoutDetailSpg, spgListFragment);
//        ft.commit();

        int count = Objects.requireNonNull(getFragmentManager()).getBackStackEntryCount();
        if (count != 0) {
            Objects.requireNonNull(getFragmentManager()).popBackStack();
        }
    }

    private void getAttendance() {
        Loading.show(getContext());
        SpgHelper.getAttendance(id, new RestCallback<ApiResponse<List<AttendanceResponse>>>() {
            @SuppressLint("WrongConstant")
            @Override
            public void onSuccess(Headers headers, ApiResponse<List<AttendanceResponse>> body) {
                Loading.hide(getContext());
                try {
                    List<AttendanceResponse> res = body.getData();

                    if (res.size() < 1) {
                        tvNoData.setVisibility(View.VISIBLE);
                    } else {
                        tvNoData.setVisibility(View.GONE);
                    }

                    for (int i = 0; i < res.size(); i++) {
                        AttendanceResponse response = res.get(i);
                        detailSpgModels.add(new DetailSpgModel(
                                getDate(response.getCreatedAt(), true),
                                getDate(response.getCheckIn() != null ? response.getCheckIn() : Constanta.BELUM_ABSEN, false),
                                getDate(response.getCheckOut() != null ? response.getCheckIn() : Constanta.BELUM_ABSEN, false)));
                    }

                    try {
                        if (res.get(0).getGroup().getId() != null) {
                            asalTokoId = String.valueOf(res.get(0).getGroup().getId());
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    DetailSpgAdapter detailSpgAdapter = new DetailSpgAdapter(detailSpgModels, getContext());
                    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),
                            LinearLayout.VERTICAL,
                            false));
                    recyclerView.setAdapter(detailSpgAdapter);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailed(ErrorResponse error) {
                Loading.hide(getContext());
                error.getMessage();
            }

            @Override
            public void onCanceled() {

            }
        });
    }

    private void getDetailSpg() {
        Loading.show(getContext());
        SpgHelper.getDetailSpg(id, new RestCallback<ApiResponse<User>>() {
            @Override
            public void onSuccess(Headers headers, ApiResponse<User> body) {
                Loading.hide(getContext());
                try {
                    User response = body.getData();
                    toolbarTitle.setText(response.getName());
                    tvName.setText(response.getName());
                    tvDateIn.setText(response.getProfile().getEntryDate() != null ?
                            getDate(response.getProfile().getEntryDate(), true) :
                            getDate(response.getCreatedAt(), true));

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailed(ErrorResponse error) {

            }

            @Override
            public void onCanceled() {

            }
        });
    }

    private void getDetailPlace() {
        Loading.show(getContext());
        SpgHelper.getDetailPlaceSpg(placeId, new RestCallback<ApiResponse<Place>>() {
            @Override
            public void onSuccess(Headers headers, ApiResponse<Place> body) {
                Loading.hide(getContext());
                try {
                    Place place = body.getData();
                    tvNamaToko.setText(place.getName());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailed(ErrorResponse error) {
                Loading.hide(getContext());
                error.getMessage();
            }

            @Override
            public void onCanceled() {

            }
        });
    }

    @SuppressLint("SimpleDateFormat")
    private static String getDate(String strDate, boolean asDate) {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date newDate = null;
        try {
            newDate = format.parse(strDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (asDate) {
            format = new SimpleDateFormat("dd MMM yyyy");
        } else {
            format = new SimpleDateFormat("hh:mm");
        }

        if (strDate.equals(Constanta.BELUM_ABSEN)) {
            return Constanta.BELUM_ABSEN;
        } else {
            return format.format(Objects.requireNonNull(newDate));
        }
    }

    private void getFromPlace(String placeId) {
        SpgHelper.getDetailPlace(placeId, new RestCallback<ApiResponse<Place>>() {
            @Override
            public void onSuccess(Headers headers, ApiResponse<Place> body) {
                try {
                    Place place = body.getData();
                    asalToko = place.getName();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailed(ErrorResponse error) {
                Loading.hide(getContext());
                error.getMessage();
            }

            @Override
            public void onCanceled() {

            }
        });
    }

    private void settingTokoSpinner(Spinner spinnerTujuanToko) {
        Loading.show(getContext());
        tokoList.clear();
        tokoIdList.clear();
        SpgHelper.getListGroup(new RestCallback<ApiResponse<List<Group>>>() {
            @Override
            public void onSuccess(Headers headers, ApiResponse<List<Group>> body) {
                Loading.hide(getContext());
                try {
                    List<Group> res = body.getData();
                    tokoList.add("Pilih Tujuan Toko");
                    tokoIdList.add("null");
                    for (int i = 0; i < res.size(); i++) {
                        Group place = res.get(i);
                        tokoList.add(place.getName());
                        tokoIdList.add(String.valueOf(place.getId()));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(Objects.requireNonNull(getActivity()), R.layout.spinner_item, tokoList) {
                    @Override
                    public boolean isEnabled(int position) {
                        return position != 0;
                    }
                };

                dataAdapter.setDropDownViewResource(R.layout.spinner_stock_dropdown_item);
                spinnerTujuanToko.setAdapter(dataAdapter);
            }

            @Override
            public void onFailed(ErrorResponse error) {
                Loading.hide(getContext());
                error.getMessage();
            }

            @Override
            public void onCanceled() {

            }
        });

        spinnerTujuanToko.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i != 0) {
                    tujuanTokoId = tokoIdList.get(i);
                } else {
                    tujuanTokoId = null;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    @OnClick(R.id.toolbar_mutasi)
    void onClickToolbarMutasi() {
        showDialog();
        ImageView imgClose = dialog.findViewById(R.id.imgClose);
        imgClose.setOnClickListener(v -> dialog.dismiss());
        TextView etAsalToko = dialog.findViewById(R.id.etDialogAsalToko);
        etAsalToko.setText(asalToko);
        Spinner spinnerTujuanToko = dialog.findViewById(R.id.spinnerTujuanToko);
        settingTokoSpinner(spinnerTujuanToko);
        EditText etDeskripsi = dialog.findViewById(R.id.etDialogDeskripsi);
        Button btnCreateMutasi = dialog.findViewById(R.id.btnDialogTambah);
        btnCreateMutasi.setOnClickListener(view -> {
            if (tujuanTokoId == null || etDeskripsi.getText().toString().equals("")) {
                Toast.makeText(getContext(), "Silahkan lengkapi form isian", Toast.LENGTH_SHORT).show();
            } else {
                createMutasi(etDeskripsi.getText().toString());
            }
        });
    }

    private void createMutasi(String note) {
        Loading.show(getContext());
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("user_id", id)
                .addFormDataPart("from_group_id", asalTokoId)
                .addFormDataPart("to_group_id", tujuanTokoId)
                .addFormDataPart("date", getCurrentDateChecklist())
                .addFormDataPart("note", note)
                .build();
        MutasiHelper.createUserMutation(requestBody, new RestCallback<ApiResponse<UserMutationResponse>>() {
            @Override
            public void onSuccess(Headers headers, ApiResponse<UserMutationResponse> body) {
                Loading.hide(getContext());
                dialog.dismiss();
                try {
                    Toast.makeText(getContext(), "Mutation Created", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailed(ErrorResponse error) {
                Loading.hide(getContext());
                error.getMessage();
            }

            @Override
            public void onCanceled() {

            }
        });
    }

    private static String getCurrentDateChecklist() {
        Date c = Calendar.getInstance().getTime();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        return df.format(c);
    }

    private void showDialog() {
        dialog = new Dialog(Objects.requireNonNull(getActivity()));
        //set content
        dialog.setContentView(R.layout.dialog_mutasi_spg);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(Objects.requireNonNull(dialog.getWindow()).getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }
}

package com.example.pakaianbagus.presentation.home;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.provider.MediaStore;

import androidx.annotation.NonNull;

import com.example.pakaianbagus.presentation.home.piutang.PiutangListTokoFragment;
import com.google.android.material.snackbar.Snackbar;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.core.content.ContextCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.pakaianbagus.MainActivity;
import com.example.pakaianbagus.R;
import com.example.pakaianbagus.api.HomeHelper;
import com.example.pakaianbagus.models.AnnouncementResponse;
import com.example.pakaianbagus.models.ApiResponse;
import com.example.pakaianbagus.models.Checklist;
import com.example.pakaianbagus.models.ChecklistResponse;
import com.example.pakaianbagus.models.News;
import com.example.pakaianbagus.models.RoleChecklist;
import com.example.pakaianbagus.models.RoleChecklistModel;
import com.example.pakaianbagus.presentation.announcement.AnnouncementFragment;
import com.example.pakaianbagus.presentation.auth.LoginActivity;
import com.example.pakaianbagus.presentation.home.adapter.ChecklistAdapter;
import com.example.pakaianbagus.presentation.home.inputpenjualan.InputPenjualan;
import com.example.pakaianbagus.presentation.home.kunjungan.KunjunganFragment;
import com.example.pakaianbagus.presentation.home.kunjungan.KunjunganKoordinatorFragment;
import com.example.pakaianbagus.presentation.home.notification.NotificationFragment;
import com.example.pakaianbagus.presentation.home.photocounter.PhotoCounterActivity;
import com.example.pakaianbagus.presentation.home.spg.SpgListBrandFragment;
import com.example.pakaianbagus.presentation.home.stockopname.StockListBrandFragment;
import com.example.pakaianbagus.presentation.home.stockopname.StockOpnameFragment;
import com.example.pakaianbagus.util.Constanta;
import com.example.pakaianbagus.util.DateUtils;
import com.example.pakaianbagus.util.GetLocation;
import com.example.pakaianbagus.util.SessionManagement;
import com.example.pakaianbagus.util.dialog.Loading;
import com.rezkyatinnov.kyandroid.localdata.LocalData;
import com.rezkyatinnov.kyandroid.reztrofit.ErrorResponse;
import com.rezkyatinnov.kyandroid.reztrofit.RestCallback;
import com.rezkyatinnov.kyandroid.session.Session;
import com.rezkyatinnov.kyandroid.session.SessionNotFoundException;
import com.rezkyatinnov.kyandroid.session.SessionObject;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ViewListener;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;

public class HomeFragment extends Fragment {

    @BindView(R.id.carousel)
    CarouselView customCarouselView;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.tvDateChecklist)
    TextView tvDateChecklist;
    @BindView(R.id.imgCheck)
    ImageView imgCheck;
    @BindView(R.id.toolbarPhoto)
    ImageView toolbarPhoto;
    @BindView(R.id.tvCheck)
    TextView tvCheck;
    @BindView(R.id.tvNoData)
    TextView tvNoData;
    @BindView(R.id.swipeRefresh)
    SwipeRefreshLayout swipeRefresh;
    @BindView(R.id.btnSubmit)
    Button btnSubmit;

    Dialog dialog;
    List<News> newsPager = new ArrayList<>();
    View rootView;
    List<RoleChecklistModel> roleChecklistModels;
    List<RoleChecklist> checklists;
    ChecklistAdapter checklistAdapter;
    SessionManagement sessionManagement;
    //SessionChecklist sessionChecklist;
    int Flag = 0;
    Calendar myCalendar;
    EditText startDate;
    EditText endDate;
    private final int REQEUST_CAMERA = 1;
    private String TAG = "Home Fragment";
    String groupId;
    String scheduleId;
    String roleId;
    String brandId;
    String tokoId;
    String longitude = "asd";
    String latitude = "asd";
    ProgressBar progressBar;
    String checklist;
    String userId;
    String userName = "Hallo Null";
    private DatePickerDialog datePickerDialog;
    private List<Checklist> checklistssss = new ArrayList<>();

    public HomeFragment() {
        // Required empty public constructor
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.home_fragment, container, false);
        ButterKnife.bind(this, rootView);

        checklists = new ArrayList<>();
        sessionManagement = new SessionManagement(Objects.requireNonNull(getActivity()));

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        roleChecklistModels = new ArrayList<>();

        try {
            groupId = Session.get(Constanta.GROUP_ID).getValue();
            scheduleId = "1";
            userId = Session.get(Constanta.USER_ID).getValue();
            roleId = Session.get(Constanta.ROLE_ID).getValue();
            userName = Session.get(Constanta.NAME).getValue();
            if (roleId.equals(SessionManagement.ROLE_SPG)) {
                brandId = Session.get(Constanta.BRAND).getValue();
                tokoId = Session.get(Constanta.TOKO).getValue();
            }
            if (roleId.equals(SessionManagement.ROLE_KOORDINATOR)
                    || roleId.equals(SessionManagement.ROLE_MANAGER)
                    || roleId.equals(SessionManagement.ROLE_ADMIN)) {
                Log.d(TAG, "masuk if");
                hideItemForManagerScreen();
            } else if (roleId.equals(SessionManagement.ROLE_SALES)) {
                hideItemForSales();
            } else {
                hideItemForSPGScreen();
            }
        } catch (SessionNotFoundException e) {
            e.printStackTrace();
            groupId = "4";
            scheduleId = "1";
            userId = "1";
            roleId = "1";
            brandId = "1";
            tokoId = "1";
        }

        swipeRefresh.setOnRefreshListener(() -> {
            getCurrentDateChecklist();
            newsPager.clear();
            getAnnouncement();
            roleChecklistModels.clear();
            if (checklists != null) {
                checklists.clear();
            }
            getListChecklist();
        });

        customCarouselView.setImageClickListener(position ->
        {
            FragmentManager fm = getFragmentManager();
            FragmentTransaction ft = Objects.requireNonNull(fm).beginTransaction();

            Bundle bundle = new Bundle();
            bundle.putString("announcement_id", newsPager.get(position).getId());
            bundle.putString("fragment", "home");

            AnnouncementFragment fragment = new AnnouncementFragment();
            fragment.setArguments(bundle);

            ft.setCustomAnimations(R.animator.fade_in, R.animator.fade_out);
            ft.replace(R.id.baseLayout, fragment);
            ft.commit();
        });

        TextView toolbarTitle = rootView.findViewById(R.id.toolbar_title);
        toolbarTitle.setText("Hallo " + userName);

        new GetLocation(getContext(), HomeFragment.this).getLocation();

        getData();

        return rootView;
    }

    public void setLocation(Double longitude, Double latitude) {
        this.longitude = String.valueOf(longitude);
        this.latitude = String.valueOf(latitude);
    }

    private void setTextCheckInOut() {
        try {
            if (Session.get(Constanta.CHECK_INOUT_STATUS).getValue().equals(SessionManagement.CHECK_IN)) {
                tvCheck.setText(SessionManagement.CHECK_OUT);
            } else {
                tvCheck.setText(SessionManagement.CHECK_IN);
            }
        } catch (SessionNotFoundException e) {
            e.printStackTrace();
            tvCheck.setText(SessionManagement.CHECK_IN);
        }
    }

    private void getData() {
        setTextCheckInOut();
        getCurrentDateChecklist();
        getAnnouncement();
        getListChecklist();
    }

    private void getCurrentDateChecklist() {
        Date c = Calendar.getInstance().getTime();
        tvDateChecklist.setText(new DateUtils().formatDateToString(c, "dd MMMM yyyy"));
    }

//    @OnClick(R.id.btnTambah)
//    public void tambahChecklist() {
//        sessionChecklist.setArraylistChecklist(checklists);
//        FragmentManager fm = getFragmentManager();
//        FragmentTransaction ft = Objects.requireNonNull(fm).beginTransaction();
//        TambahChecklistFragment homeFragment = new TambahChecklistFragment();
//        ft.setCustomAnimations(R.animator.fade_in, R.animator.fade_out);
//        ft.replace(R.id.baseLayout, homeFragment);
//        ft.commit();
//
////        showDialog(R.layout.dialog_tambah_checklist);
////        EditText etChecklist = dialog.findViewById(R.id.etDialogChecklist);
////        ImageView btnClose = dialog.findViewById(R.id.imgClose);
////        btnClose.setOnClickListener(v -> dialog.dismiss());
////        Button btnTambah = dialog.findViewById(R.id.btnDialogTambah);
////        btnTambah.setOnClickListener(v -> {
////            if (etChecklist.getText().length() < 1) {
////                etChecklist.setError("Field ini harus diisi");
////            } else {
////                String uniqueID = UUID.randomUUID().toString();
////                @SuppressLint("SimpleDateFormat") SimpleDateFormat s1 = new SimpleDateFormat("yyyy-MM-dd");
////                String date = s1.format(new Date());
////                RoleChecklist myObject = new RoleChecklist();
////                myObject.setId(uniqueID);
////                myObject.setChecklist_id(null);
////                myObject.setCreated_at(date);
////                Checklist checklist = new Checklist();
////                checklist.setId(uniqueID);
////                checklist.setName(etChecklist.getText().toString());
////                myObject.setChecklist(checklist);
////                checklists.add(myObject);
////                checklistAdapter = new ChecklistAdapter(checklists, getActivity(), 0);
////                sessionChecklist.setArraylistChecklist(checklists);
////                Toast.makeText(getContext(), "Berhasil tambah checklist", Toast.LENGTH_SHORT).show();
////                dialog.dismiss();
////            }
////        });
//    }

    private void getListChecklist() {
        swipeRefresh.setRefreshing(true);
        HomeHelper.getListChecklist(getContext(), new Callback<ApiResponse<List<ChecklistResponse>>>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponse<List<ChecklistResponse>>> call,
                                   @NonNull Response<ApiResponse<List<ChecklistResponse>>> response) {
                try {
                    if (Objects.requireNonNull(response.body()).getData() != null) {
                        List<ChecklistResponse> res = response.body().getData();
                        /*@SuppressLint("SimpleDateFormat") SimpleDateFormat s1 = new SimpleDateFormat("yyyy-MM-dd");
                        String date = s1.format(new Date());*/

                        if (res.size() < 1) {
                            tvNoData.setVisibility(View.VISIBLE);
                        } else {
                            tvNoData.setVisibility(View.GONE);

                            for (int i = 0; i < res.size(); i++) {
                                ChecklistResponse checklistResponse = res.get(i);
                                Checklist checklist = new Checklist();
                                checklist.setName(checklistResponse.getName());
                                checklist.setId(String.valueOf(checklistResponse.getId()));
                                checklistssss.add(checklist);
                            }

                            getUserChecklist(checklistssss);
                        }

                        /*for (int i = 0; i < res.size(); i++) {
                            RoleChecklist roleChecklist = res.get(i);
                            RoleChecklist myObject = new RoleChecklist();
                            Checklist myObject2 = new Checklist();
                            if (roleChecklist.getChecklist() != null) {
                                myObject.setId(roleChecklist.getId());
                                myObject.setCreated_at(date);
                                myObject2.setId(roleChecklist.getChecklist().getId());
                                myObject2.setName(roleChecklist.getChecklist().getName());
                                myObject.setChecklist(myObject2);
                                checklists.add(myObject);
                            } else {
                                myObject.setId(roleChecklist.getId());
                                myObject.setCreated_at(date);
                                myObject2.setId(roleChecklist.getId());
                                myObject2.setName("Lorem Ipsum Lorem Ipsum");
                                myObject.setChecklist(myObject2);
                                checklists.add(myObject);
                            }

                            roleChecklistModels.add(new RoleChecklistModel(roleChecklist.getId(),
                                    roleChecklist.getChecklist_id(),
                                    roleChecklist.getChecklist()));
                        }*/

//                        if (sessionChecklist.getArrayListChecklist() != null) {
//                            Log.d("ArrayList IF", sessionChecklist.getArrayListChecklist());
//                            checklists.clear();
//                            roleChecklistModels.clear();
//
//                            try {
//                                JSONArray jsonArray = new JSONArray(sessionChecklist.getArrayListChecklist());
//                                for (int i = 0; i < jsonArray.length(); i++) {
//                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
//                                    JSONObject jsonObjeckChecklist = jsonObject.getJSONObject("checklist");
//
////                                    if (!String.valueOf(jsonObject.get("created_at")).equals(date)) {
////                                        sessionChecklist.logoutUser();
////                                        getActivity().finish();
////                                        startActivity(getActivity().getIntent());
////                                    }
//
//                                    RoleChecklist myObject = new RoleChecklist();
//                                    myObject.setId(String.valueOf(jsonObject.get("id")));
//
//                                    Checklist checklist = new Checklist();
//                                    checklist.setId(String.valueOf(jsonObjeckChecklist.get("id")));
//                                    checklist.setName(String.valueOf(jsonObjeckChecklist.get("name")));
//                                    myObject.setChecklist(checklist);
//
//                                    checklists.add(myObject);
//                                }
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
//
//                            checklistAdapter = new ChecklistAdapter(checklists, getActivity(), 0, HomeFragment.this);
//
//                        } else {
//                            sessionChecklist.setArraylistChecklist(checklists);

                    } else {
                        tvNoData.setVisibility(View.VISIBLE);
                    }
                } catch (Exception e) {
                    swipeRefresh.setRefreshing(false);
                    Log.d("TAG EXCEPTION", e.toString());
                }
            }

            @Override
            public void onFailure(@NonNull Call<ApiResponse<List<ChecklistResponse>>> call, @NonNull Throwable t) {
                swipeRefresh.setRefreshing(false);
                Log.d("onFailure", t.getMessage());
            }
        });
    }

    private void getUserChecklist(List<Checklist> checklists) {
        String today = new DateUtils().getTodayWithFormat("yyyy-MM-dd");
        HomeHelper.getListChecklistUser(userId, today, new Callback<ApiResponse<List<com.example.pakaianbagus.models.user.Checklist>>>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponse<List<com.example.pakaianbagus.models.user.Checklist>>> call,
                                   @NonNull Response<ApiResponse<List<com.example.pakaianbagus.models.user.Checklist>>> response) {

                if (Objects.requireNonNull(response.body()).getData() != null) {
                    List<com.example.pakaianbagus.models.user.Checklist> checklistsUser = response.body().getData();

                    for (int x = 0; x < checklists.size(); x++) {
                        String item = checklists.get(x).getName();
                        for (int y = 0; y < checklistsUser.size(); y++) {
                            String[] userItem = checklistsUser.get(y).getChecklists().split("\\s*,\\s*");
                            for (String s : userItem) {
                                if (s.equalsIgnoreCase(item)) {
                                    checklists.get(x).setChecked(true);
                                }
                            }
                        }
                    }

                    int sumc = 0;
                    for (int x = 0; x < checklists.size(); x++) {
                        if (checklists.get(x).isChecked()) {
                            sumc = sumc + 1;
                        }
                    }
                    try {
                        if (checklists.size() == sumc) {
                            btnSubmit.setEnabled(false);
                            //btnSubmit.setBackground(Objects.requireNonNull(getContext()).getResources().getDrawable(R.drawable.background_photo_counter));
                            btnSubmit.setBackgroundColor(getResources().getColor(R.color.Gray));
                        } else {
                            //btnSubmit.setBackgroundColor(getResources().getColor(R.color.DarkSlateBlue));
                            btnSubmit.setBackground(Objects.requireNonNull(getContext()).getResources().getDrawable(R.drawable.btn_rounded_blackyellow));
                        }
                    } catch (Exception ignore){}
                }

                checklistAdapter = new ChecklistAdapter(checklists, getContext(), HomeFragment.this);
                recyclerView.setAdapter(checklistAdapter);
                swipeRefresh.setRefreshing(false);
            }

            @Override
            public void onFailure(@NonNull Call<ApiResponse<List<com.example.pakaianbagus.models.user.Checklist>>> call,
                                  @NonNull Throwable t) {
                swipeRefresh.setRefreshing(false);
                Log.d("onFailure", t.toString());
                checklistAdapter = new ChecklistAdapter(checklists, getContext(), HomeFragment.this);
                recyclerView.setAdapter(checklistAdapter);
                swipeRefresh.setRefreshing(false);
            }
        });
    }

//    private void getListChecklist() {
//        Loading.show(getActivity());
//        HomeHelper.getListChecklist(getContext(), new RestCallback<ApiResponse<List<RoleChecklist>>>() {
//            @Override
//            public void onSuccess(Headers headers, ApiResponse<List<RoleChecklist>> body) {
//                Loading.hide(getActivity());
//                if (body != null) {
//                    List<RoleChecklist> res = body.getData();
//                    for (int i = 0; i < res.size(); i++) {
//                        RoleChecklist roleChecklist = res.get(i);
//                        roleChecklistModels.add(new RoleChecklistModel(roleChecklist.getId(),
//                                roleChecklist.getChecklist_id(),
//                                roleChecklist.getChecklist()));
//                    }
//
//                    if (sessionManagement.getArrayListChecklist() != null) {
//                        Log.d("ArrayList IF", sessionManagement.getArrayListChecklist());
//                        checklistAdapter = new ChecklistAdapter(sessionManagement.getArrayListChecklist(), getActivity(), 0);
//                    } else {
//                        checklistAdapter = new ChecklistAdapter(roleChecklistModels, getActivity());
//                    }
//                    recyclerView.setAdapter(checklistAdapter);
//                }
//            }
//
//            @Override
//            public void onFailed(ErrorResponse error) {
//                Loading.hide(getActivity());
//                Log.d("Error Get Checklist", error.getMessage());
//            }
//
//            @Override
//            public void onCanceled() {
//
//            }
//        });
//    }

    private void getAnnouncement() {
        swipeRefresh.setRefreshing(true);
        @SuppressLint("InflateParams") View customView = getLayoutInflater().inflate(R.layout.item_carousel_banner, null);
//        progressBar = customView.findViewById(R.id.progressBar);
//        progressBar.setVisibility(View.VISIBLE);
        HomeHelper.getAnnouncement(new RestCallback<ApiResponse<List<AnnouncementResponse>>>() {
            @Override
            public void onSuccess(Headers headers, ApiResponse<List<AnnouncementResponse>> body) {
                swipeRefresh.setRefreshing(false);
                if (body.getData() != null) {
                    List<AnnouncementResponse> responses = body.getData();
                    for (int i = 0; i < responses.size(); i++) {
                        AnnouncementResponse announcementResponse = responses.get(i);
                        News news = new News();
                        news.setId(announcementResponse.getId());
                        news.setTitle(announcementResponse.getTitle());
                        news.setContent(announcementResponse.getDescription());
                        news.setBanner(announcementResponse.getImage());
                        newsPager.add(news);
                    }

                    customCarouselView.setViewListener(viewListener);
                    customCarouselView.setPageCount(newsPager.size());
                    customCarouselView.setSlideInterval(4000);
                }

            }

            @Override
            public void onFailed(ErrorResponse error) {
                swipeRefresh.setRefreshing(false);
                Log.d("TAG", error.getMessage());
            }

            @Override
            public void onCanceled() {

            }
        });
    }

    // To set custom views
    ViewListener viewListener = position -> {
        @SuppressLint("InflateParams") View customView = getLayoutInflater().inflate(R.layout.item_carousel_banner, null);
        TextView tvLabelCarousel = customView.findViewById(R.id.tv_label_carousel);
        ImageView ivCarousel = customView.findViewById(R.id.iv_carousel);

        Glide.with(Objects.requireNonNull(getActivity()))
                .load(newsPager.get(position).getBanner())
                .into(ivCarousel);
        tvLabelCarousel.setText(newsPager.get(position).getTitle());

//            ivCarousel.setOnClickListener(new NewsClickListener(newsPager.get(position)));
        return customView;
    };

    private void hideItemForSPGScreen() {
        LinearLayout layout = rootView.findViewById(R.id.layoutHeaderButton2);
        layout.setVisibility(View.GONE);
        Button button = rootView.findViewById(R.id.btnTambah);
        button.setVisibility(View.GONE);

        LinearLayout layout1 = rootView.findViewById(R.id.layoutHeaderIcon02);
        layout1.setWeightSum(3f);
        LinearLayout layoutTitle = rootView.findViewById(R.id.layoutHeaderTitle02);
        layoutTitle.setWeightSum(3f);
        CardView cardViewPenjualan = rootView.findViewById(R.id.btnPenjualan);
        cardViewPenjualan.setVisibility(View.VISIBLE);
        TextView tvTitlePenjualan = rootView.findViewById(R.id.tvPenjualan);
        tvTitlePenjualan.setVisibility(View.VISIBLE);

        toolbarPhoto.setVisibility(View.VISIBLE);
    }

    private void hideItemForKoordinatorScreen() {
        LinearLayout layout2 = rootView.findViewById(R.id.layoutHeaderButton2);
        layout2.setVisibility(View.VISIBLE);
        Button button = rootView.findViewById(R.id.btnTambah);
        button.setVisibility(View.VISIBLE);

        LinearLayout layout1 = rootView.findViewById(R.id.layoutHeaderIcon02);
        layout1.setWeightSum(2f);
        LinearLayout layoutTitle = rootView.findViewById(R.id.layoutHeaderTitle02);
        layoutTitle.setWeightSum(2f);
        LinearLayout layoutTitle1 = rootView.findViewById(R.id.layoutHeaderTitle01);
        layoutTitle1.setWeightSum(2f);
        CardView cardViewPenjualan = rootView.findViewById(R.id.btnPenjualan);
        cardViewPenjualan.setVisibility(View.GONE);
        TextView tvTitlePenjualan = rootView.findViewById(R.id.tvPenjualan);
        tvTitlePenjualan.setVisibility(View.GONE);

        toolbarPhoto.setVisibility(View.VISIBLE);
    }

    private void hideItemForManagerScreen() {
        LinearLayout layout2 = rootView.findViewById(R.id.layoutHeaderButton2);
        layout2.setVisibility(View.VISIBLE);
        Button button = rootView.findViewById(R.id.btnTambah);
        button.setVisibility(View.VISIBLE);

        LinearLayout layout1 = rootView.findViewById(R.id.layoutHeaderIcon02);
        layout1.setWeightSum(2f);
        LinearLayout layoutTitle1 = rootView.findViewById(R.id.layoutHeaderTitle02);
        layoutTitle1.setWeightSum(2f);
        LinearLayout layoutTitle2 = rootView.findViewById(R.id.layoutHeaderTitle01);
        layoutTitle2.setWeightSum(2f);
        CardView cardViewPenjualan = rootView.findViewById(R.id.btnPenjualan);
        cardViewPenjualan.setVisibility(View.GONE);
        TextView tvTitlePenjualan = rootView.findViewById(R.id.tvPenjualan);
        tvTitlePenjualan.setVisibility(View.GONE);

        toolbarPhoto.setVisibility(View.VISIBLE);
    }

    private void hideItemForSales() {
        LinearLayout layout2 = rootView.findViewById(R.id.layoutHeaderButton2);
        layout2.setVisibility(View.VISIBLE);
        Button button = rootView.findViewById(R.id.btnTambah);
        button.setVisibility(View.VISIBLE);

        LinearLayout layoutHead = rootView.findViewById(R.id.layoutHeaderIcon01);
        layoutHead.setWeightSum(2f);

        LinearLayout layout1 = rootView.findViewById(R.id.layoutHeaderIcon02);
        layout1.setWeightSum(2f);
        LinearLayout layoutTitle1 = rootView.findViewById(R.id.layoutHeaderTitle02);
        layoutTitle1.setWeightSum(2f);
        LinearLayout layoutTitle2 = rootView.findViewById(R.id.layoutHeaderTitle01);
        layoutTitle2.setWeightSum(2f);

        CardView cardViewPenjualan = rootView.findViewById(R.id.btnPenjualan);
        cardViewPenjualan.setVisibility(View.GONE);
        CardView cardViewStock = rootView.findViewById(R.id.btnStock);
        cardViewStock.setVisibility(View.GONE);
        TextView tvTitlePenjualan = rootView.findViewById(R.id.tvPenjualan);
        tvTitlePenjualan.setVisibility(View.GONE);
        TextView tvStock = rootView.findViewById(R.id.tvStock);
        tvStock.setVisibility(View.GONE);
        CardView cardViewPiutang = rootView.findViewById(R.id.btnPiutang);
        cardViewPiutang.setVisibility(View.VISIBLE);
        TextView tvTitlePiutang = rootView.findViewById(R.id.tvPiutang);
        tvTitlePiutang.setVisibility(View.VISIBLE);

        toolbarPhoto.setVisibility(View.GONE);
    }

    @OnClick(R.id.btnStock)
    public void onClickBtnStock() {
        showDialog(R.layout.dialog_filter_stock);
        ImageView imgClose = dialog.findViewById(R.id.imgClose);
        imgClose.setOnClickListener(v -> dialog.dismiss());

        Button btnFilterArtikel = dialog.findViewById(R.id.btnFilterArtikel);
        Button btnFilterKategori = dialog.findViewById(R.id.btnFilterKategori);

        btnFilterArtikel.setOnClickListener(v -> doStok(Constanta.STOK_ARTIKEL));
        btnFilterKategori.setOnClickListener(v -> doStok(Constanta.STOK_KATEGORI));
    }

    private void doStok(int choose) {
        dialog.dismiss();
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = Objects.requireNonNull(fm).beginTransaction();
        Fragment fragment;
        Bundle args = new Bundle();
        args.putInt("choose", choose);
        if (roleId.equals(SessionManagement.ROLE_SPG)) {
            fragment = new StockOpnameFragment();
            args.putString("id_brand", brandId);
            args.putString("id_toko", tokoId);
        } else {
            fragment = new StockListBrandFragment();
        }
        fragment.setArguments(args);
        ft.setCustomAnimations(R.animator.fade_in, R.animator.fade_out);
        ft.replace(R.id.baseLayout, fragment).addToBackStack("fragment");
        ft.commit();
    }

    @OnClick(R.id.btnPiutang)
    void onClickBtnPiutang(){
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = Objects.requireNonNull(fm).beginTransaction();
        PiutangListTokoFragment piutangListTokoFragment = new PiutangListTokoFragment();
        ft.setCustomAnimations(R.animator.fade_in, R.animator.fade_out);
        ft.replace(R.id.baseLayout, piutangListTokoFragment).addToBackStack("piutangListTokoFragment");
        ft.commit();
    }

    @OnClick(R.id.btnPenjualan)
    public void onClickBtnPenjualan() {
        Calendar newCalendar = Calendar.getInstance();
        datePickerDialog = new DatePickerDialog(Objects.requireNonNull(getContext()),
                (view, year, monthOfYear, dayOfMonth) -> {
                    Calendar newDate = Calendar.getInstance();
                    newDate.set(year, monthOfYear, dayOfMonth);

                    FragmentManager fm = getFragmentManager();
                    FragmentTransaction ft = Objects.requireNonNull(fm).beginTransaction();
                    Bundle bundle = new Bundle();
                    bundle.putString("date", new DateUtils().formatDateToString(newDate.getTime(), "yyyy-MM-dd"));
                    InputPenjualan inputPenjualan = new InputPenjualan();
                    inputPenjualan.setArguments(bundle);
                    ft.setCustomAnimations(R.animator.fade_in, R.animator.fade_out);
                    ft.replace(R.id.baseLayout, inputPenjualan).addToBackStack("inputPenjualan");
                    ft.commit();

                    //Toast.makeText(getContext(), "Tanggal dipilih : " + dateFormatter.format(newDate.getTime()), Toast.LENGTH_SHORT).show();
                },
                newCalendar.get(Calendar.YEAR),
                newCalendar.get(Calendar.MONTH),
                newCalendar.get(Calendar.DAY_OF_MONTH));

        datePickerDialog.show();

        /*myCalendar = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener date = (view, year, month, dayOfMonth) -> {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, month);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();
        };
        new DatePickerDialog(
                Objects.requireNonNull(getActivity()),
                date,
                myCalendar.get(Calendar.YEAR),
                myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)
        ).show();*/

//        showDialog(R.layout.dialog_filter_penjualan);
//        myCalendar = Calendar.getInstance();
//        ImageView imgClose = dialog.findViewById(R.id.imgClose);
//        imgClose.setOnClickListener(v -> dialog.dismiss());
//        startDate = dialog.findViewById(R.id.etDialogStartDate);
//        startDate.setOnClickListener(v -> {
//            new DatePickerDialog(Objects.requireNonNull(getActivity()), date, myCalendar
//                    .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
//                    myCalendar.get(Calendar.DAY_OF_MONTH)).show();
//        });
//        endDate = dialog.findViewById(R.id.etDialogEndDate);
//        DatePickerDialog.OnDateSetListener date2 = (view, year, month, dayOfMonth) -> {
//            myCalendar.set(Calendar.YEAR, year);
//            myCalendar.set(Calendar.MONTH, month);
//            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
//            updateLabel2();
//        };
//        endDate.setOnClickListener(v -> {
//            new DatePickerDialog(Objects.requireNonNull(getActivity()), date2, myCalendar
//                    .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
//                    myCalendar.get(Calendar.DAY_OF_MONTH)).show();
//        });
//        Button btnOK = dialog.findViewById(R.id.btnDialogTambah);
//        btnOK.setText("OK");
//        btnOK.setOnClickListener(v -> {
//            if (startDate.getText().toString().length() >= 1 && endDate.getText().toString().length() >= 1) {
//                dialog.dismiss();
//                FragmentManager fm = getFragmentManager();
//                FragmentTransaction ft = Objects.requireNonNull(fm).beginTransaction();
//                InputPenjualan inputPenjualan = new InputPenjualan();
//                ft.setCustomAnimations(R.animator.fade_in, R.animator.fade_out);
//                ft.replace(R.id.baseLayout, inputPenjualan);
//                ft.commit();
//
//            } else {
//                Snackbar.make(rootView, "Field Tidak Boleh Kosong", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

//        Intent intent = new Intent(getActivity(), MainActivity.class);
//        intent.putExtra("FromHome", "1");
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        startActivity(intent);
    }

    private void restartApp() {
        Intent mStartActivity = new Intent(getActivity(), MainActivity.class);
        int mPendingIntentId = 123456;
        PendingIntent mPendingIntent = PendingIntent.getActivity(getActivity(), mPendingIntentId, mStartActivity,
                PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager mgr = (AlarmManager) Objects.requireNonNull(getActivity()).getSystemService(Context.ALARM_SERVICE);
        Objects.requireNonNull(mgr).set(AlarmManager.RTC, System.currentTimeMillis() + 100, mPendingIntent);
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);
    }

    @SuppressLint("SetTextI18n")
    private void updateLabel() {
        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        showDialog(R.layout.dialog_filter_penjualan);
        endDate = dialog.findViewById(R.id.etDialogEndDate);
        endDate.setVisibility(View.GONE);
        myCalendar = Calendar.getInstance();
        ImageView imgClose = dialog.findViewById(R.id.imgClose);
        imgClose.setOnClickListener(v -> dialog.dismiss());

        DatePickerDialog.OnDateSetListener date = (view, year, month, dayOfMonth) -> {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, month);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            dialog.dismiss();
            updateLabel();
        };

        startDate = dialog.findViewById(R.id.etDialogStartDate);
        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT,
                2f
        );
        startDate.setLayoutParams(param);
        startDate.setOnClickListener(v -> new DatePickerDialog(Objects.requireNonNull(getActivity()), date, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show());

        startDate.setText(sdf.format(myCalendar.getTime()));

        Button btnOK = dialog.findViewById(R.id.btnDialogTambah);
        btnOK.setText("OK");
        btnOK.setOnClickListener(v -> {
            if (startDate.getText().toString().length() >= 1) {
                dialog.dismiss();
                FragmentManager fm = getFragmentManager();
                FragmentTransaction ft = Objects.requireNonNull(fm).beginTransaction();
                InputPenjualan inputPenjualan = new InputPenjualan();
                ft.setCustomAnimations(R.animator.fade_in, R.animator.fade_out);
                ft.replace(R.id.baseLayout, inputPenjualan);
                ft.commit();

            } else {
                Snackbar.make(rootView, "Field Tidak Boleh Kosong", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    private void updateLabel2() {
        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        endDate.setText(sdf.format(myCalendar.getTime()));
    }

    @OnClick(R.id.btnCheck)
    void onClickBtnCheck() {
        selectImage();
    }

    private void selectImage() {
        final CharSequence[] options = {"Take Photo", "Cancel"};
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(Objects.requireNonNull(getActivity()));
        builder.setTitle("Select Option");
        builder.setItems(options, (dialog, item) -> {
            if (options[item].equals("Take Photo")) {
                if (ContextCompat.checkSelfPermission(Objects.requireNonNull(getContext()), Manifest.permission.CAMERA)
                        == PackageManager.PERMISSION_DENIED) {
                    ActivityCompat.requestPermissions(getActivity(),
                            new String[]{Manifest.permission.CAMERA}, REQEUST_CAMERA);
                } else {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, REQEUST_CAMERA);
                }
            } else if (options[item].equals("Cancel")) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQEUST_CAMERA && resultCode == RESULT_OK) {

            Bitmap photoResult = (Bitmap) Objects.requireNonNull(data.getExtras()).get("data");

            Log.d(TAG, String.valueOf(photoResult));

            try {
                File result = File.createTempFile("photo", ".jpeg",
                        Objects.requireNonNull(getContext()).getExternalCacheDir());

                OutputStream outStream = new FileOutputStream(result);
                Objects.requireNonNull(photoResult).compress(Bitmap.CompressFormat.JPEG, 100, outStream);
                outStream.flush();
                outStream.close();

                try {
                    if (Session.get(Constanta.CHECK_INOUT_STATUS).getValue().equals(SessionManagement.CHECK_IN)) {
                        postCheckOut(result);
                    } else {
                        postCheckIn(result);
                    }
                } catch (SessionNotFoundException e) {
                    postCheckIn(result);
                }

            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(getContext(), "Terjadi kesalahan ketika mengunggah file", Toast.LENGTH_SHORT).show();
            }

        }
    }

    private void postCheckIn(File file) {
        Loading.show(getContext());
        RequestBody fileReqBody = RequestBody.create(MediaType.parse("image/*"), file);
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("user_id", userId)
                .addFormDataPart("m_group_id", groupId)
                .addFormDataPart("m_schedule_id", scheduleId)
                .addFormDataPart("long_check_in", longitude)
                .addFormDataPart("lat_check_in", latitude)
                .addFormDataPart("photo_check_in", file.getName(), fileReqBody)
                .build();
        HomeHelper.postCheckIn(requestBody, new RestCallback<ApiResponse>() {
            @Override
            public void onSuccess(Headers headers, ApiResponse body) {
                Loading.hide(getContext());
                showDialog(R.layout.dialog_check_in_out);
                ImageView btnClose = dialog.findViewById(R.id.imgClose);
                btnClose.setOnClickListener(view -> dialog.dismiss());
                TextView tvTime = dialog.findViewById(R.id.tvTime);
                Button btnOK = dialog.findViewById(R.id.btnOK);
                btnOK.setOnClickListener(view -> {
                    Session.save(new SessionObject(Constanta.CHECK_INOUT_STATUS, SessionManagement.CHECK_IN));
                    setTextCheckInOut();
                    dialog.dismiss();
                });
                Date c = Calendar.getInstance().getTime();
                String formattedDate = new DateUtils().formatDateToString(c, "hh:mm");
                tvTime.setText(formattedDate);
            }

            @Override
            public void onFailed(ErrorResponse error) {
                Loading.hide(getContext());
                Log.d("TAG CheckIN", error.getMessage());
                if (error.getMessage().equalsIgnoreCase("Data already exist")) {
                    Session.save(new SessionObject(Constanta.CHECK_INOUT_STATUS, SessionManagement.CHECK_IN));
                    setTextCheckInOut();
                }
            }

            @Override
            public void onCanceled() {

            }
        });
    }

    private void postCheckOut(File file) {
        Loading.show(getContext());
        RequestBody fileReqBody = RequestBody.create(MediaType.parse("image/*"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("photo_check_out", file.getName(), fileReqBody);
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("user_id", userId)
                .addFormDataPart("long_check_out", longitude)
                .addFormDataPart("lat_check_out", latitude)
                .addPart(body)
                .build();
        HomeHelper.postCheckOut(requestBody, new RestCallback<ApiResponse>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onSuccess(Headers headers, ApiResponse body) {
                Loading.hide(getContext());
                showDialog(R.layout.dialog_check_in_out);
                ImageView btnClose = dialog.findViewById(R.id.imgClose);
                btnClose.setOnClickListener(view -> dialog.dismiss());
                TextView tvTitle = dialog.findViewById(R.id.tvTitle);
                TextView tvContent = dialog.findViewById(R.id.tvContent);
                tvContent.setText("Selamat anda berhasil check out");
                tvTitle.setText("CHECKOUT");
                TextView tvTime = dialog.findViewById(R.id.tvTime);
                Button btnOK = dialog.findViewById(R.id.btnOK);
                btnOK.setOnClickListener(view -> {
                    Session.save(new SessionObject(Constanta.CHECK_INOUT_STATUS, SessionManagement.CHECK_OUT));
                    dialog.dismiss();
                    setTextCheckInOut();
                });
                Date c = Calendar.getInstance().getTime();
                String formattedDate = new DateUtils().formatDateToString(c, "hh:mm");
                tvTime.setText(formattedDate);
            }

            @Override
            public void onFailed(ErrorResponse error) {
                Loading.hide(getContext());
                Log.d("TAG CheckIN", error.getMessage());
            }

            @Override
            public void onCanceled() {

            }
        });
    }

    @NonNull
    private RequestBody createPartFromString(String descriptionString) {
        return RequestBody.create(okhttp3.MultipartBody.FORM, descriptionString);
    }

    @OnClick(R.id.toolbarPhoto)
    void onClickToolbarPhoto() {
        Intent intent = new Intent(getActivity(), PhotoCounterActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.btnSPG)
    void onClickBtnSPG() {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = Objects.requireNonNull(fm).beginTransaction();
        SpgListBrandFragment spgListBrandFragment = new SpgListBrandFragment();
        ft.setCustomAnimations(R.animator.fade_in, R.animator.fade_out).addToBackStack("spgListBrandFragment");
        ft.replace(R.id.baseLayout, spgListBrandFragment).addToBackStack("spgListBrandFragment");
        ft.commit();
    }

    @OnClick(R.id.btnKunjungan)
    void onClickBtnKunjungan() {

        if (roleId.equals(SessionManagement.ROLE_MANAGER)) {
            showDialog(R.layout.dialog_pilih_kunjungan);

            Button btnKunjunganKoordinator = dialog.findViewById(R.id.btnKunjunganKoordinator);
            btnKunjunganKoordinator.setOnClickListener(v -> {
                dialog.dismiss();
                FragmentManager fm = getFragmentManager();
                FragmentTransaction ft = Objects.requireNonNull(fm).beginTransaction();
                KunjunganFragment kunjunganFragment = new KunjunganFragment();
                Bundle bundle = new Bundle();
                bundle.putString("kunjunganKoordinator", "KUNJUNGAN KOORDINATOR");
                kunjunganFragment.setArguments(bundle);
                ft.setCustomAnimations(R.animator.fade_in, R.animator.fade_out);
                ft.replace(R.id.baseLayout, kunjunganFragment).addToBackStack("kunjunganFragment");
                ft.commit();
            });

            Button btnKunjunganSaya = dialog.findViewById(R.id.btnKunjunganSaya);
            btnKunjunganSaya.setOnClickListener(v -> {
                dialog.dismiss();
                FragmentManager fm = getFragmentManager();
                FragmentTransaction ft = Objects.requireNonNull(fm).beginTransaction();
                KunjunganFragment kunjunganFragment = new KunjunganFragment();
                Bundle bundle = new Bundle();
                bundle.putString("kunjunganSaya", "KUNJUNGAN SAYA");
                kunjunganFragment.setArguments(bundle);
                ft.setCustomAnimations(R.animator.fade_in, R.animator.fade_out);
                ft.replace(R.id.baseLayout, kunjunganFragment).addToBackStack("kunjunganFragment");
                ft.commit();
            });
        } else {
            FragmentManager fm = getFragmentManager();
            FragmentTransaction ft = Objects.requireNonNull(fm).beginTransaction();
            KunjunganFragment kunjunganFragment = new KunjunganFragment();
            Bundle bundle = new Bundle();
            bundle.putString("kunjunganSaya", "KUNJUNGAN SAYA");
            kunjunganFragment.setArguments(bundle);
            ft.setCustomAnimations(R.animator.fade_in, R.animator.fade_out);
            ft.replace(R.id.baseLayout, kunjunganFragment).addToBackStack("kunjunganFragment");
            ft.commit();
        }
    }

//    @OnClick(R.id.btnInventaris)
//    public void onClickBtnInventaris() {
//        FragmentManager fm = getFragmentManager();
//        FragmentTransaction ft = Objects.requireNonNull(fm).beginTransaction();
//        InventarisFragment inventarisFragment = new InventarisFragment();
//        ft.setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out);
//        ft.replace(R.id.baseLayout, inventarisFragment);
//        ft.commit();
//    }

    @OnClick(R.id.btnTambah)
    void btnTambah() {
//        showDialog(R.layout.dialog_tambah_barang);
//        ImageView imgClose = dialog.findViewById(R.id.imgClose);
//        imgClose.setOnClickListener(v -> dialog.dismiss());
    }

    private void showDialog(int layout) {
        dialog = new Dialog(Objects.requireNonNull(getActivity()));
        //set content
        dialog.setContentView(layout);
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

    @OnClick(R.id.toolbar_logout)
    void onClickToolbar() {
        if (!roleId.equals(SessionManagement.ROLE_ADMIN)) {
            doLogout();
            return;
        }

        View v1 = rootView.findViewById(R.id.toolbar_logout);
        PopupMenu pm = new PopupMenu(Objects.requireNonNull(getActivity()), v1);
        pm.getMenuInflater().inflate(R.menu.menu_switch_account, pm.getMenu());
        switch (Flag) {
            case 1:
                pm.getMenu().findItem(R.id.navigation_spg).setVisible(true);
                pm.getMenu().findItem(R.id.navigation_manager).setVisible(true);
                pm.getMenu().findItem(R.id.navigation_koordinator).setVisible(true);
                pm.getMenu().findItem(R.id.navigation_sales).setVisible(false);
                break;
            case 2:
                pm.getMenu().findItem(R.id.navigation_spg).setVisible(false);
                pm.getMenu().findItem(R.id.navigation_manager).setVisible(true);
                pm.getMenu().findItem(R.id.navigation_koordinator).setVisible(false);
                pm.getMenu().findItem(R.id.navigation_sales).setVisible(true);
                break;
            case 3:
                pm.getMenu().findItem(R.id.navigation_spg).setVisible(true);
                pm.getMenu().findItem(R.id.navigation_manager).setVisible(true);
                pm.getMenu().findItem(R.id.navigation_koordinator).setVisible(false);
                pm.getMenu().findItem(R.id.navigation_sales).setVisible(true);
                break;
            case 4:
                pm.getMenu().findItem(R.id.navigation_spg).setVisible(true);
                pm.getMenu().findItem(R.id.navigation_manager).setVisible(false);
                pm.getMenu().findItem(R.id.navigation_koordinator).setVisible(true);
                pm.getMenu().findItem(R.id.navigation_sales).setVisible(true);
                break;
        }

        pm.setOnMenuItemClickListener(menuItem -> {
            switch (menuItem.getItemId()) {
                case R.id.navigation_sales:
                    Flag = 1;
                    Session.save(new SessionObject("RoleId", SessionManagement.ROLE_SALES));
                    hideItemForSales();
                    restartApp();
                    break;
                case R.id.navigation_spg:
                    Flag = 2;
                    Session.save(new SessionObject("RoleId", SessionManagement.ROLE_SPG));
                    hideItemForSPGScreen();
                    restartApp();
                    break;
                case R.id.navigation_koordinator:
                    Flag = 3;
                    Session.save(new SessionObject("RoleId", SessionManagement.ROLE_KOORDINATOR));
                    hideItemForKoordinatorScreen();
                    restartApp();
                    break;
                case R.id.navigation_manager:
                    Flag = 4;
                    Session.save(new SessionObject("RoleId", SessionManagement.ROLE_MANAGER));
                    hideItemForManagerScreen();
                    restartApp();
                    break;
                case R.id.navigation_logout:
                    showDialog(R.layout.dialog_logout);
                    Button btn_ya = dialog.findViewById(R.id.btnYa);
                    Button btn_no = dialog.findViewById(R.id.btnTidak);
                    btn_ya.setOnClickListener(v -> {
                        dialog.dismiss();
                        doLogout();
                    });

                    btn_no.setOnClickListener(v -> dialog.dismiss());
                    break;
            }
            return true;
        });
        pm.show();
    }

    private void doLogout() {
        Session.clear();
        LocalData.clear();

        Intent intent = new Intent(getActivity(), LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        Objects.requireNonNull(getActivity()).finish();
    }

    @OnClick(R.id.toolbar_notif)
    void onClickToolbarNotif() {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = Objects.requireNonNull(fm).beginTransaction();
        NotificationFragment notifFragment = new NotificationFragment();
        ft.setCustomAnimations(R.animator.fade_in, R.animator.fade_out);
        ft.replace(R.id.baseLayout, notifFragment);
        ft.commit();
    }

    @OnClick(R.id.btnSubmit)
    void onClickSubmit() {
        if (checklist == null) {
            Toast.makeText(getContext(), "Tidak ada yang harus dikirimkan", Toast.LENGTH_SHORT).show();
        } else {
            String date = new DateUtils().getTodayWithFormat("yyyy-MM-dd");
            Loading.show(getActivity());
            HomeHelper.postChecklistByUserId(userId, date, checklist, new RestCallback<ApiResponse>() {
                @Override
                public void onSuccess(Headers headers, ApiResponse body) {
                    Loading.hide(getContext());
                    Toast.makeText(getContext(), "Berhasil menyimpan data checklist hari ini", Toast.LENGTH_SHORT).show();
                    Log.d("onSuccess: ", body.getMessage());
                    getUserChecklist(checklistssss);
                }

                @Override
                public void onFailed(ErrorResponse error) {
                    Loading.hide(getContext());
                    Toast.makeText(getContext(), "Gagal menyimpan data " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.d("TAG CheckIN", error.getMessage());
                }

                @Override
                public void onCanceled() {

                }
            });
        }
    }

    public void setChecklistItem(String checklist) {
        this.checklist = checklist;
    }

}

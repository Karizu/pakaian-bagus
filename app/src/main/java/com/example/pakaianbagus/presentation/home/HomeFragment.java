package com.example.pakaianbagus.presentation.home;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.pakaianbagus.MainActivity;
import com.example.pakaianbagus.R;
import com.example.pakaianbagus.api.HomeHelper;
import com.example.pakaianbagus.models.ApiResponse;
import com.example.pakaianbagus.models.Checklist;
import com.example.pakaianbagus.models.News;
import com.example.pakaianbagus.models.RoleChecklist;
import com.example.pakaianbagus.models.RoleChecklistModel;
import com.example.pakaianbagus.presentation.home.adapter.ChecklistAdapter;
import com.example.pakaianbagus.presentation.home.kunjungan.KunjunganFragment;
import com.example.pakaianbagus.presentation.home.kunjungan.KunjunganKoordinatorFragment;
import com.example.pakaianbagus.presentation.home.notification.NotificationFragment;
import com.example.pakaianbagus.presentation.home.spg.SpgListTokoFragment;
import com.example.pakaianbagus.presentation.home.stockopname.StockListTokoFragment;
import com.example.pakaianbagus.util.SessionManagement;
import com.example.pakaianbagus.util.dialog.Loading;
import com.rezkyatinnov.kyandroid.session.Session;
import com.rezkyatinnov.kyandroid.session.SessionNotFoundException;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ViewListener;

import java.io.File;
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
import pl.aprilapps.easyphotopicker.DefaultCallback;
import pl.aprilapps.easyphotopicker.EasyImage;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {

    @BindView(R.id.carousel)
    CarouselView customCarouselView;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.tvDateChecklist)
    TextView tvDateChecklist;
    @BindView(R.id.imgCheck)
    ImageView imgCheck;
    Dialog dialog;
    List<News> newsPager = new ArrayList<>();
    View rootView;
    List<RoleChecklistModel> roleChecklistModels;
    ChecklistAdapter checklistAdapter;
    SessionManagement sessionManagement;
    int Flag = 0;
    Calendar myCalendar;
    EditText startDate;
    EditText endDate;
    private final int REQEUST_CAMERA = 1;
    private String TAG = "Home Fragment";
    File imageCheck;

    public HomeFragment() {
        // Required empty public constructor
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.home_fragment, container, false);
        ButterKnife.bind(this, rootView);

        sessionManagement = new SessionManagement(Objects.requireNonNull(getActivity()));
//        hideItemForSPGScreen();
        TextView toolbarTitle = rootView.findViewById(R.id.toolbar_title);
        toolbarTitle.setText("Hallo Rizal");

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(),
                LinearLayout.VERTICAL,
                false);
        recyclerView.setLayoutManager(layoutManager);
        roleChecklistModels = new ArrayList<>();


        try {
            if (Session.get("RoleId").getValue().equals(SessionManagement.ROLE_KOORDINATOR)
                    || Session.get("RoleId").getValue().equals(SessionManagement.ROLE_ADMIN)){
                hideItemForManagerScreen();
                Log.d(TAG, "masuk if");
            } else if (Session.get("RoleId").getValue().equals(SessionManagement.ROLE_SALES)){
                hideItemForSales();
            } else {
                hideItemForSPGScreen();
            }

        } catch (SessionNotFoundException e) {
            e.printStackTrace();
        }

        getCurrentDateChecklist();

        initSlider();

        getListChecklist();

        return rootView;
    }

    private void getCurrentDateChecklist() {
        Date c = Calendar.getInstance().getTime();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat df = new SimpleDateFormat("yyyy MMMM dd");
        String formattedDate = df.format(c);
        tvDateChecklist.setText(formattedDate);
    }

    @OnClick(R.id.btnTambah)
    public void tambahChecklist() {
        showDialog(R.layout.dialog_tambah_checklist);
        EditText etChecklist = dialog.findViewById(R.id.etDialogChecklist);
        ImageView btnClose = dialog.findViewById(R.id.imgClose);
        btnClose.setOnClickListener(v -> dialog.dismiss());
        Button btnTambah = dialog.findViewById(R.id.btnDialogTambah);
        btnTambah.setOnClickListener(v -> {
            if (etChecklist.getText().length() < 1) {
                etChecklist.setError("Field ini harus diisi");
            } else {
                Checklist checklist = new Checklist();
                checklist.setName(etChecklist.getText().toString());
                List<RoleChecklistModel> roleChecklistModelList = new ArrayList<>();
//                roleChecklistModelList.add(new RoleChecklistModel("id", "null", checklist));
//                roleChecklistModels.addAll(roleChecklistModelList);
//                checklistAdapter.notifyDataSetChanged();
                roleChecklistModels.add(new RoleChecklistModel("id", "null", checklist));
                checklistAdapter.notifyDataSetChanged();
                sessionManagement.setArraylistChecklist(roleChecklistModels);
                Log.d("ArrayList", sessionManagement.getArrayListChecklist());
                Intent intent = new Intent(getActivity(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }

    private void getListChecklist() {
        Loading.show(getActivity());
        HomeHelper.getListChecklist(getContext(), new Callback<ApiResponse<List<RoleChecklist>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<RoleChecklist>>> call, Response<ApiResponse<List<RoleChecklist>>> response) {
                Loading.hide(getContext());
                List<RoleChecklist> res = response.body().getData();
                    for (int i = 0; i < res.size(); i++) {
                        RoleChecklist roleChecklist = res.get(i);
                        roleChecklistModels.add(new RoleChecklistModel(roleChecklist.getId(),
                                roleChecklist.getChecklist_id(),
                                roleChecklist.getChecklist()));
                    }

                    if (sessionManagement.getArrayListChecklist() != null) {
                        Log.d("ArrayList IF", sessionManagement.getArrayListChecklist());
                        checklistAdapter = new ChecklistAdapter(sessionManagement.getArrayListChecklist(), getActivity(), 0);
                    } else {
                        checklistAdapter = new ChecklistAdapter(roleChecklistModels, getActivity());
                    }
                    recyclerView.setAdapter(checklistAdapter);
            }

            @Override
            public void onFailure(Call<ApiResponse<List<RoleChecklist>>> call, Throwable t) {
                Loading.hide(getContext());
                Log.d("onFailure", t.getMessage());
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

    private void initSlider() {
        newsPager.add(new News("Title", "Lorem Ipsum Lorem Ipsum", "https://d1csarkz8obe9u.cloudfront.net/posterpreviews/purple-ramadan-charity-event-invitation-banner-design-template-c8a1a4d8e5747a5a4f75d56b7974d233_screen.jpg?ts=1556628102"));

        //customCarouselView.setPageCount(sampleTitles.length);
        customCarouselView.setViewListener(viewListener);
        customCarouselView.setPageCount(newsPager.size());
        customCarouselView.setSlideInterval(4000);
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
        CardView cardViewPenjualan = rootView.findViewById(R.id.btnPenjualan);
        cardViewPenjualan.setVisibility(View.GONE);
        TextView tvTitlePenjualan = rootView.findViewById(R.id.tvPenjualan);
        tvTitlePenjualan.setVisibility(View.GONE);
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
        CardView cardViewPenjualan = rootView.findViewById(R.id.btnPenjualan);
        cardViewPenjualan.setVisibility(View.GONE);
        TextView tvTitlePenjualan = rootView.findViewById(R.id.tvPenjualan);
        tvTitlePenjualan.setVisibility(View.GONE);
    }

    private void hideItemForSales(){
        LinearLayout layout2 = rootView.findViewById(R.id.layoutHeaderButton2);
        layout2.setVisibility(View.VISIBLE);
        Button button = rootView.findViewById(R.id.btnTambah);
        button.setVisibility(View.VISIBLE);

        LinearLayout layoutHead = rootView.findViewById(R.id.layoutHeaderIcon01);
        layoutHead.setWeightSum(3f);
        CardView cardViewPiutang = rootView.findViewById(R.id.btnPiutang);
        cardViewPiutang.setVisibility(View.VISIBLE);
        TextView tvTitlePiutang = rootView.findViewById(R.id.tvPiutang);
        tvTitlePiutang.setVisibility(View.VISIBLE);

        LinearLayout layout1 = rootView.findViewById(R.id.layoutHeaderIcon02);
        layout1.setWeightSum(2f);
        LinearLayout layoutTitle1 = rootView.findViewById(R.id.layoutHeaderTitle02);
        layoutTitle1.setWeightSum(2f);
        CardView cardViewPenjualan = rootView.findViewById(R.id.btnPenjualan);
        cardViewPenjualan.setVisibility(View.GONE);
        TextView tvTitlePenjualan = rootView.findViewById(R.id.tvPenjualan);
        tvTitlePenjualan.setVisibility(View.GONE);
    }

    @OnClick(R.id.btnStock)
    public void onClickBtnStock() {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = Objects.requireNonNull(fm).beginTransaction();
        StockListTokoFragment stockFragment = new StockListTokoFragment();
        ft.setCustomAnimations(R.animator.fade_in, R.animator.fade_out);
        ft.replace(R.id.baseLayout, stockFragment);
        ft.commit();
    }

    @SuppressLint("SetTextI18n")
    @OnClick(R.id.btnPenjualan)
    public void onClickBtnPenjualan() {
        showDialog(R.layout.dialog_filter_penjualan);
        myCalendar = Calendar.getInstance();
        ImageView imgClose = dialog.findViewById(R.id.imgClose);
        imgClose.setOnClickListener(v -> dialog.dismiss());
        startDate = dialog.findViewById(R.id.etDialogStartDate);
        DatePickerDialog.OnDateSetListener date = (view, year, month, dayOfMonth) -> {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, month);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();
        };
        startDate.setOnClickListener(v -> {
            new DatePickerDialog(Objects.requireNonNull(getActivity()), date, myCalendar
                    .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                    myCalendar.get(Calendar.DAY_OF_MONTH)).show();
        });
        endDate = dialog.findViewById(R.id.etDialogEndDate);
        DatePickerDialog.OnDateSetListener date2 = (view, year, month, dayOfMonth) -> {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, month);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel2();
        };
        endDate.setOnClickListener(v -> {
            new DatePickerDialog(Objects.requireNonNull(getActivity()), date2, myCalendar
                    .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                    myCalendar.get(Calendar.DAY_OF_MONTH)).show();
        });
        Button btnOK = dialog.findViewById(R.id.btnDialogTambah);
        btnOK.setText("OK");
        btnOK.setOnClickListener(v -> {
            if (startDate.getText().toString().length() >= 1 && endDate.getText().toString().length() >= 1) {
                dialog.dismiss();

            } else {
                Snackbar.make(rootView, "Field Tidak Boleh Kosong", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

//        Intent intent = new Intent(getActivity(), MainActivity.class);
//        intent.putExtra("FromHome", "1");
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        startActivity(intent);
    }

    private void updateLabel() {
        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        startDate.setText(sdf.format(myCalendar.getTime()));
    }

    private void updateLabel2() {
        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        endDate.setText(sdf.format(myCalendar.getTime()));
    }

    @OnClick(R.id.btnCheck)
    public void onClickBtnCheck() {
        selectImage();
    }

    private void selectImage() {
        final CharSequence[] options = {"Take Photo", "Choose From Gallery", "Cancel"};
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(Objects.requireNonNull(getActivity()));
        builder.setTitle("Select Option");
        builder.setItems(options, (dialog, item) -> {
            if (options[item].equals("Take Photo")) {
                EasyImage.openCamera(this, REQEUST_CAMERA);
            } else if (options[item].equals("Cancel")) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        EasyImage.handleActivityResult(requestCode, resultCode, data, getActivity(), new DefaultCallback() {
            @Override
            public void onImagePickerError(Exception e, EasyImage.ImageSource source, int type) {
                //Some error handling
            }

            @Override
            public void onImagePicked(File imageFile, EasyImage.ImageSource source, int type) {
                Glide.with(Objects.requireNonNull(getContext()))
                        .load(imageFile)
                        .apply(RequestOptions.circleCropTransform())
                        .into(imgCheck);
                imageCheck = imageFile;
            }
        });
    }

    @OnClick(R.id.btnSPG)
    public void onClickBtnSPG() {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = Objects.requireNonNull(fm).beginTransaction();
        SpgListTokoFragment spgFragment = new SpgListTokoFragment();
        ft.setCustomAnimations(R.animator.fade_in, R.animator.fade_out);
        ft.replace(R.id.baseLayout, spgFragment);
        ft.commit();
    }

    @OnClick(R.id.btnKunjungan)
    public void onClickBtnKunjungan() {
        if (Flag == 3
        ) {
            showDialog(R.layout.dialog_pilih_kunjungan);
            Button btnKunjunganKoordinator = dialog.findViewById(R.id.btnKunjunganKoordinator);
            btnKunjunganKoordinator.setOnClickListener(v -> {
                dialog.dismiss();
                FragmentManager fm = getFragmentManager();
                FragmentTransaction ft = Objects.requireNonNull(fm).beginTransaction();
                KunjunganKoordinatorFragment kunjunganFragment = new KunjunganKoordinatorFragment();
                Bundle bundle = new Bundle();
                bundle.putString("kunjunganKoordinator", "KUNJUNGAN KOORDINATOR");
                kunjunganFragment.setArguments(bundle);
                ft.setCustomAnimations(R.animator.fade_in, R.animator.fade_out);
                ft.replace(R.id.baseLayout, kunjunganFragment);
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
                ft.replace(R.id.baseLayout, kunjunganFragment);
                ft.commit();
            });
        } else {
            FragmentManager fm = getFragmentManager();
            FragmentTransaction ft = Objects.requireNonNull(fm).beginTransaction();
            KunjunganFragment kunjunganFragment = new KunjunganFragment();
            ft.setCustomAnimations(R.animator.fade_in, R.animator.fade_out);
            ft.replace(R.id.baseLayout, kunjunganFragment);
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
    public void btnTambah() {
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
    public void onClickToolbar() {
//        Objects.requireNonNull(getActivity()).finishAffinity();
//        getActivity().finish();
        View v1 = rootView.findViewById(R.id.toolbar_logout);
        PopupMenu pm = new PopupMenu(Objects.requireNonNull(getActivity()), v1);
        pm.getMenuInflater().inflate(R.menu.menu_switch_account, pm.getMenu());
        switch (Flag) {
            case 1:
                pm.getMenu().findItem(R.id.navigation_spg).setVisible(false);
                pm.getMenu().findItem(R.id.navigation_manager).setVisible(true);
                pm.getMenu().findItem(R.id.navigation_koordinator).setVisible(true);
                break;
            case 2:
                pm.getMenu().findItem(R.id.navigation_spg).setVisible(true);
                pm.getMenu().findItem(R.id.navigation_manager).setVisible(true);
                pm.getMenu().findItem(R.id.navigation_koordinator).setVisible(false);
                break;
            case 3:
                pm.getMenu().findItem(R.id.navigation_spg).setVisible(true);
                pm.getMenu().findItem(R.id.navigation_manager).setVisible(false);
                pm.getMenu().findItem(R.id.navigation_koordinator).setVisible(true);
                break;
        }
        pm.setOnMenuItemClickListener(menuItem -> {
            switch (menuItem.getItemId()) {
                case R.id.navigation_spg:
                    Flag = 1;
                    hideItemForSPGScreen();
                    break;
                case R.id.navigation_koordinator:
                    Flag = 2;
                    hideItemForKoordinatorScreen();
                    break;
                case R.id.navigation_manager:
                    Flag = 3;
                    hideItemForManagerScreen();
                    break;
            }
            return true;
        });
        pm.show();
    }

    @OnClick(R.id.toolbar_notif)
    public void onClickToolbarNotif() {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = Objects.requireNonNull(fm).beginTransaction();
        NotificationFragment notifFragment = new NotificationFragment();
        ft.setCustomAnimations(R.animator.fade_in, R.animator.fade_out);
        ft.replace(R.id.baseLayout, notifFragment);
        ft.commit();
    }

}

package com.example.pakaianbagus.presentation.home.kunjungan.tambahkunjungan;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.core.content.ContextCompat;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.pakaianbagus.R;
import com.example.pakaianbagus.api.HomeHelper;
import com.example.pakaianbagus.api.KatalogHelper;
import com.example.pakaianbagus.api.SpgHelper;
import com.example.pakaianbagus.models.ApiResponse;
import com.example.pakaianbagus.models.BrandResponse;
import com.example.pakaianbagus.models.Expend;
import com.example.pakaianbagus.models.Kunjungan;
import com.example.pakaianbagus.models.TokoResponse;
import com.example.pakaianbagus.models.auth.Group;
import com.example.pakaianbagus.models.expenditures.Detail;
import com.example.pakaianbagus.models.expenditures.Expenditures;
import com.example.pakaianbagus.models.expenditures.RequestExpenditures;
import com.example.pakaianbagus.presentation.home.kunjungan.KunjunganFragment;
import com.example.pakaianbagus.util.Constanta;
import com.example.pakaianbagus.util.RoundedCornersTransformation;
import com.example.pakaianbagus.util.dialog.Loading;
import com.github.jjobes.slidedatetimepicker.SlideDateTimeListener;
import com.github.jjobes.slidedatetimepicker.SlideDateTimePicker;
import com.google.gson.Gson;
import com.rezkyatinnov.kyandroid.reztrofit.ErrorResponse;
import com.rezkyatinnov.kyandroid.reztrofit.RestCallback;
import com.rezkyatinnov.kyandroid.session.Session;
import com.rezkyatinnov.kyandroid.session.SessionNotFoundException;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;
import static com.example.pakaianbagus.presentation.home.photocounter.adapter.PhotoAdapter.sCorner;
import static com.example.pakaianbagus.presentation.home.photocounter.adapter.PhotoAdapter.sMargin;

public class TambahKunjunganFragment extends Fragment {

    private Dialog dialog;
    private List<String> brandList, brandIdList;
    private List<String> tokoList, tokoIdList;
    private List<String> tipePengeluaranIdList, namaTipePengeluaranList;
    private List<Kunjungan> kunjunganModels;
    private String brandId, tokoId, tipePengeluaranId, namaTipePengeluaran, userId;
    private final int REQEUST_CAMERA = 1, REQEUST_CAMERA_KUNJUNGAN = 2;
    private File imageCheck;
    private Bitmap photoImage, photoImageKunjungan;
    private ImageView imgPhoto;
    private Date time;
    private View dialogView;
    private AlertDialog alertDialog;
    private Calendar calendar;
    private RequestExpenditures requestExpenditures;
    private List<Detail> detailList = new ArrayList<>();
    private List<MultipartBody.Part> parts;
    private DatePicker datePicker;
    private TimePicker timePicker;
    private Bundle bundle;

    @BindView(R.id.spinnerBrand)
    Spinner spinnerBrand;
    @BindView(R.id.spinnerToko)
    Spinner spinnerToko;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.imgPhotoCounter)
    ImageView imgPhotoCounter;
    @BindView(R.id.btnAddPhotoCounter)
    LinearLayout btnAddPhotoCounter;
    @BindView(R.id.tvDateTime)
    TextView tvDateTime;
    @BindView(R.id.etDeskripsi)
    EditText etDeskripsi;

    public TambahKunjunganFragment(List<Kunjungan> kunjunganModels) {
        this.kunjunganModels = kunjunganModels;
    }

    public TambahKunjunganFragment() {

    }

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.tambah_kunjungan_fragment, container, false);
        ButterKnife.bind(this, rootView);

        bundle = new Bundle();
        try {
            if (Objects.requireNonNull(getArguments()).getString(Constanta.KUNJUNGAN_SAYA) != null){
                bundle.putString(Constanta.KUNJUNGAN_SAYA, "KUNJUNGAN SAYA");
            }
        } catch (Exception e){
            e.printStackTrace();
        }

        try {
            if (Objects.requireNonNull(getArguments()).getString(Constanta.KUNJUNGAN_KOORDINATOR) != null){
                bundle.putString(Constanta.KUNJUNGAN_KOORDINATOR, "KUNJUNGAN KOORDINATOR");
            }
        } catch (Exception e){
            e.printStackTrace();
        }

        try {
            userId = Session.get(Constanta.USER_ID).getValue();
        } catch (SessionNotFoundException e) {
            e.printStackTrace();
        }

        brandList = new ArrayList<>();
        brandIdList = new ArrayList<>();
        tokoList = new ArrayList<>();
        tokoIdList = new ArrayList<>();
        kunjunganModels = new ArrayList<>();
        tipePengeluaranIdList = new ArrayList<>();
        namaTipePengeluaranList = new ArrayList<>();
        parts = new ArrayList<>();

        dialogView = View.inflate(getActivity(), R.layout.date_time_picker, null);
        alertDialog = new AlertDialog.Builder(Objects.requireNonNull(getActivity())).create();
        datePicker = dialogView.findViewById(R.id.date_picker);
        timePicker = dialogView.findViewById(R.id.time_picker);
        timePicker.setIs24HourView(true);

        getListBrand();
        getListToko();

        return rootView;
    }

    @OnClick(R.id.btnDialogTambah)
    void onClickProses(){
        createExpenditures();
    }

    private void createExpenditures(){
        Loading.show(getContext());

        if (tvDateTime.getText().toString().equals("PILIH WAKTU KUNJUNGAN") ||
                etDeskripsi.getText().toString().equals("") ||
                photoImageKunjungan == null ||
                detailList.size() < 1) {
            Loading.hide(getContext());
            Toast.makeText(getContext(), "Silahkan lengkapi form isian", Toast.LENGTH_SHORT).show();
        } else {
            parts.add(prepareParts("user_id", userId));
            parts.add(prepareParts("m_group_id", tokoId));
            parts.add(prepareParts("date", tvDateTime.getText().toString()));
            parts.add(prepareParts("m_brand_id", brandId));
            parts.add(prepareParts("description", etDeskripsi.getText().toString()));
            parts.add(prepareFilePart("image", photoImageKunjungan));

            for (int i = 0; i < detailList.size(); i++){
                Detail detail = detailList.get(i);
                parts.add(prepareParts("details["+i+"][m_expend_id]", detail.getM_expend_id()));
                parts.add(prepareParts("details["+i+"][description]", detail.getDescription()));
                parts.add(prepareParts("details["+i+"][amount]", detail.getAmount()));
                parts.add(prepareFilePart("details["+i+"][image]", photoImage));
            }

            HomeHelper.createExpenditures(parts, new Callback<ApiResponse<Expenditures>>() {
                @Override
                public void onResponse(Call<ApiResponse<Expenditures>> call, Response<ApiResponse<Expenditures>> response) {
                    Loading.hide(getContext());
                    try {
                        Toast.makeText(getContext(), "Berhasil tambah kunjungan", Toast.LENGTH_SHORT).show();

                        FragmentManager fm = getFragmentManager();
                        FragmentTransaction ft = Objects.requireNonNull(fm).beginTransaction();
                        KunjunganFragment kunjunganFragment = new KunjunganFragment();
                        kunjunganFragment.setArguments(bundle);
                        ft.setCustomAnimations(R.animator.fade_in, R.animator.fade_out);
                        ft.replace(R.id.baseLayoutTambahKunjungan, kunjunganFragment);
                        ft.commit();
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
    }

    @SuppressLint("SimpleDateFormat")
    private static String getDate(String strDate) {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date newDate = null;
        try {
            newDate = format.parse(strDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(Objects.requireNonNull(newDate));
    }

    @OnClick(R.id.btnAddPhotoCounter)
    void onClickPhotoCounter(){
        if (ContextCompat.checkSelfPermission(Objects.requireNonNull(getContext()), Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(Objects.requireNonNull(getActivity()),
                    new String[]{Manifest.permission.CAMERA}, REQEUST_CAMERA);
        } else {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent, REQEUST_CAMERA_KUNJUNGAN);
        }
    }

    @SuppressLint("WrongConstant")
    @OnClick(R.id.btnAddPengeluaran)
    public void onCLickBtnAddPengeluaran() {
        showDialog();
        Spinner spinnerTipe = dialog.findViewById(R.id.spinnerTipePengeluaran);
        getListTipePengeluaran(spinnerTipe);
        EditText deskripsi = dialog.findViewById(R.id.etDialogDeskripsi);
        EditText nominal = dialog.findViewById(R.id.etDialogNominal);
        Button ambilPhoto = dialog.findViewById(R.id.btnDialogPhoto);
        imgPhoto = dialog.findViewById(R.id.imgPhoto);
        imgPhoto.setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(Objects.requireNonNull(getContext()), Manifest.permission.CAMERA)
                    == PackageManager.PERMISSION_DENIED) {
                ActivityCompat.requestPermissions(Objects.requireNonNull(getActivity()),
                        new String[]{Manifest.permission.CAMERA}, REQEUST_CAMERA);
            } else {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, REQEUST_CAMERA);
            }
        });
        ambilPhoto.setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(Objects.requireNonNull(getContext()), Manifest.permission.CAMERA)
                    == PackageManager.PERMISSION_DENIED) {
                ActivityCompat.requestPermissions(Objects.requireNonNull(getActivity()),
                        new String[]{Manifest.permission.CAMERA}, REQEUST_CAMERA);
            } else {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, REQEUST_CAMERA);
            }
        });

        Button tambah = dialog.findViewById(R.id.btnDialogTambah);
        tambah.setOnClickListener(v -> {
            if (tipePengeluaranId.equals("null") || nominal.getText().toString().equals("")) {
                Toast.makeText(getContext(), "Field tidak boleh kosong", Toast.LENGTH_SHORT).show();
            } else {
                String uniqueID = UUID.randomUUID().toString();
                Kunjungan kunjunganModel = new Kunjungan();
                kunjunganModel.setId(uniqueID);
                kunjunganModel.setName(spinnerTipe.getSelectedItem().toString());
                kunjunganModel.setNominal(nominal.getText().toString());
                kunjunganModel.setImage(photoImage);
                kunjunganModels.add(kunjunganModel);

                Detail detail = new Detail();
                detail.setM_expend_id(tipePengeluaranId);
                detail.setAmount(nominal.getText().toString());
                detail.setDescription(deskripsi.getText().toString());
                detailList.add(detail);

                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayout.VERTICAL, false));
                TambahKunjunganAdapter adapter = new TambahKunjunganAdapter(kunjunganModels, detailList, getActivity(), TambahKunjunganFragment.this);
                recyclerView.setAdapter(adapter);
            }
            dialog.dismiss();
        });
        ImageView imgClose = dialog.findViewById(R.id.imgClose);
        imgClose.setOnClickListener(v -> dialog.dismiss());
    }

    @NonNull
    private MultipartBody.Part prepareFilePart(String partName, Bitmap photoImage) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        photoImage.compress(Bitmap.CompressFormat.JPEG, 20, byteArrayOutputStream);
        RequestBody requestFile = RequestBody.create(MediaType.parse("image/jpeg"), byteArrayOutputStream.toByteArray());

        return MultipartBody.Part.createFormData(partName, "photo.jpeg", requestFile);
    }

    @NonNull
    private MultipartBody.Part prepareParts(String partName, String value) {
        return MultipartBody.Part.createFormData(partName, value);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQEUST_CAMERA && resultCode == RESULT_OK) {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            photoImage = (Bitmap) Objects.requireNonNull(data.getExtras()).get("data");
            Objects.requireNonNull(photoImage).compress(Bitmap.CompressFormat.JPEG, 100, stream);

            try {
                Glide.with(Objects.requireNonNull(getContext()))
                        .asBitmap()
                        .load(stream.toByteArray())
                        .apply(RequestOptions.bitmapTransform(
                                new RoundedCornersTransformation(getContext(), sCorner, sMargin))).into(imgPhoto);
            } catch (Exception e){
                e.printStackTrace();
            }

            try {
                File outputDir = Objects.requireNonNull(getContext()).getCacheDir();
                imageCheck = File.createTempFile("photo", "jpeg", outputDir);
                FileOutputStream outputStream = getContext().openFileOutput("photo.jpeg", Context.MODE_PRIVATE);
                outputStream.write(stream.toByteArray());
                outputStream.close();
                Log.d("Write File", "Success");
            } catch (IOException e) {
                e.printStackTrace();
                Log.d("Write File", "Failed2");
            }
        } else if (requestCode == REQEUST_CAMERA_KUNJUNGAN && resultCode == RESULT_OK) {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            photoImageKunjungan = (Bitmap) Objects.requireNonNull(data.getExtras()).get("data");
            Objects.requireNonNull(photoImageKunjungan).compress(Bitmap.CompressFormat.JPEG, 100, stream);

            try {
                Glide.with(Objects.requireNonNull(getContext()))
                        .asBitmap()
                        .load(stream.toByteArray())
                        .apply(RequestOptions.bitmapTransform(
                                new RoundedCornersTransformation(getContext(), sCorner, sMargin))).into(imgPhotoCounter);
            } catch (Exception e){
                e.printStackTrace();
            }

            try {
                File outputDir = Objects.requireNonNull(getContext()).getCacheDir();
                imageCheck = File.createTempFile("photo", "jpeg", outputDir);
                FileOutputStream outputStream = getContext().openFileOutput("photo.jpeg", Context.MODE_PRIVATE);
                outputStream.write(stream.toByteArray());
                outputStream.close();
                Log.d("Write File", "Success");
            } catch (IOException e) {
                e.printStackTrace();
                Log.d("Write File", "Failed2");
            }
        }
    }

    private void getListTipePengeluaran(Spinner spinnerTipePengeluaran) {
        Loading.show(getActivity());
        namaTipePengeluaranList.clear();
        tipePengeluaranIdList.clear();
        HomeHelper.getListExpends(new RestCallback<ApiResponse<List<Expend>>>() {
            @Override
            public void onSuccess(Headers headers, ApiResponse<List<Expend>> listApiResponse) {
                Loading.hide(getActivity());
                try {
                    List<Expend> res = listApiResponse.getData();
                    namaTipePengeluaranList.add("Pilih Tipe Pengeluaran");
                    tipePengeluaranIdList.add("null");
                    for (int i = 0; i < res.size(); i++) {
                        Expend response = res.get(i);
                        namaTipePengeluaranList.add(response.getName());
                        tipePengeluaranIdList.add(response.getId());
                    }

                    // Creating adapter for spinner
                    ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(Objects.requireNonNull(getActivity()), R.layout.layout_spinner_text, namaTipePengeluaranList) {
                        @Override
                        public boolean isEnabled(int position) {
                            return position != 0;
                        }
                    };

                    dataAdapter.setDropDownViewResource(R.layout.layout_spinner_dropdown);
                    spinnerTipePengeluaran.setAdapter(dataAdapter);
                    spinnerTipePengeluaran.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

//                            setSpinnerNestedKatgori(position);

                            if (position != 0) {
                                tipePengeluaranId = brandIdList.get(position);
                            } else {
                                tipePengeluaranId = "null";
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });

                } catch (Exception e) {
                    Log.d("TAG EXCEPTION", Objects.requireNonNull(e.getMessage()));
                }
            }

            @Override
            public void onFailed(ErrorResponse errorResponse) {
                Loading.hide(getContext());
                Log.d("TAG onFialed", errorResponse.getMessage());
            }

            @Override
            public void onCanceled() {

            }
        });
    }

    public void getListBrand() {
        Loading.show(getActivity());
        KatalogHelper.getListBrand(new RestCallback<ApiResponse<List<BrandResponse>>>() {
            @Override
            public void onSuccess(Headers headers, ApiResponse<List<BrandResponse>> listApiResponse) {
                Loading.hide(getActivity());
                try {
                    List<BrandResponse> res = listApiResponse.getData();
                    brandList.add("Pilih Brand");
                    brandIdList.add("null");
                    for (int i = 0; i < res.size(); i++) {
                        BrandResponse response = res.get(i);
                        brandList.add(response.getName());
                        brandIdList.add(response.getId());
                    }

                    // Creating adapter for spinner
                    ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(Objects.requireNonNull(getActivity()), R.layout.layout_spinner_text, brandList) {
                        @Override
                        public boolean isEnabled(int position) {
                            return position != 0;
                        }
                    };

                    dataAdapter.setDropDownViewResource(R.layout.layout_spinner_dropdown);
                    spinnerBrand.setAdapter(dataAdapter);
                    spinnerBrand.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

//                            setSpinnerNestedKatgori(position);

                            if (position != 0) {
                                brandId = brandIdList.get(position);
                            } else {
                                brandId = "null";
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });

                } catch (Exception e) {
                    Log.d("TAG EXCEPTION", Objects.requireNonNull(e.getMessage()));
                }
            }

            @Override
            public void onFailed(ErrorResponse errorResponse) {
                Loading.hide(getContext());
                Log.d("TAG onFialed", errorResponse.getMessage());
            }

            @Override
            public void onCanceled() {

            }
        });
    }

    public void getListToko() {
        Loading.show(getActivity());
        SpgHelper.getListGroup(new RestCallback<ApiResponse<List<Group>>>() {
            @Override
            public void onSuccess(Headers headers, ApiResponse<List<Group>> listApiResponse) {
                Loading.hide(getActivity());
                try {
                    List<Group> res = listApiResponse.getData();
                    tokoList.add("Pilih Toko");
                    tokoIdList.add("null");
                    for (int i = 0; i < res.size(); i++) {
                        Group response = res.get(i);
                        tokoList.add(response.getName());
                        tokoIdList.add(response.getId()+"");
                    }

                    // Creating adapter for spinner
                    ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(Objects.requireNonNull(getActivity()), R.layout.layout_spinner_text, tokoList) {
                        @Override
                        public boolean isEnabled(int position) {
                            return position != 0;
                        }
                    };

                    dataAdapter.setDropDownViewResource(R.layout.layout_spinner_dropdown);
                    spinnerToko.setAdapter(dataAdapter);
                    spinnerToko.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

//                            setSpinnerNestedKatgori(position);

                            if (position != 0) {
                                tokoId = tokoIdList.get(position);
                            } else {
                                tokoId = null;
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });

                } catch (Exception e) {
                    Log.d("TAG EXCEPTION", Objects.requireNonNull(e.getMessage()));
                }
            }

            @Override
            public void onFailed(ErrorResponse errorResponse) {
                Loading.hide(getContext());
                Log.d("TAG onFialed", errorResponse.getMessage());
            }

            @Override
            public void onCanceled() {

            }
        });
    }

    @OnClick(R.id.tvDateTime)
    void onClickDateTime(){
        dialogView.findViewById(R.id.date_time_set).setOnClickListener(view -> {

            calendar = new GregorianCalendar(datePicker.getYear(),
                    datePicker.getMonth(),
                    datePicker.getDayOfMonth(),
                    timePicker.getCurrentHour(),
                    timePicker.getCurrentMinute());

            @SuppressLint("SimpleDateFormat") SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String date = df.format(calendar.getTime());
            Log.d("DATE", date);
            tvDateTime.setText(date);

            alertDialog.dismiss();
        });

        alertDialog.setView(dialogView);
        alertDialog.show();
    }

    @OnClick(R.id.toolbar_back)
    public void toolbarBack() {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = Objects.requireNonNull(fm).beginTransaction();
        KunjunganFragment kunjunganFragment = new KunjunganFragment();
        ft.setCustomAnimations(R.animator.fade_in, R.animator.fade_out);
        ft.replace(R.id.baseLayoutTambahKunjungan, kunjunganFragment);
        ft.commit();
    }

    private void showDialog() {
        dialog = new Dialog(Objects.requireNonNull(getActivity()));
        //set content
        dialog.setContentView(R.layout.dialog_tambah_pengeluaran);
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

    public void updateData(List<Detail> newList){
        detailList = new ArrayList<>();
        detailList.addAll(newList);
        Gson gson = new Gson();
        String json = gson.toJson(detailList);
        Log.d("KUNJUNGAN", json);
    }
}

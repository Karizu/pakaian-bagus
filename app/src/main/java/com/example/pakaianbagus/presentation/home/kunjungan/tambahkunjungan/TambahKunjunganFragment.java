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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.pakaianbagus.R;
import com.example.pakaianbagus.api.KatalogHelper;
import com.example.pakaianbagus.models.ApiResponse;
import com.example.pakaianbagus.models.BrandResponse;
import com.example.pakaianbagus.models.Kunjungan;
import com.example.pakaianbagus.models.TokoResponse;
import com.example.pakaianbagus.presentation.home.kunjungan.KunjunganFragment;
import com.example.pakaianbagus.util.Constanta;
import com.example.pakaianbagus.util.RoundedCornersTransformation;
import com.example.pakaianbagus.util.dialog.Loading;
import com.rezkyatinnov.kyandroid.reztrofit.ErrorResponse;
import com.rezkyatinnov.kyandroid.reztrofit.RestCallback;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Headers;

import static android.app.Activity.RESULT_OK;
import static com.example.pakaianbagus.presentation.home.photocounter.adapter.PhotoAdapter.sCorner;
import static com.example.pakaianbagus.presentation.home.photocounter.adapter.PhotoAdapter.sMargin;

public class TambahKunjunganFragment extends Fragment {

    private Dialog dialog;
    private List<String> brandList, brandIdList;
    private List<String> tokoList, tokoIdList;
    private List<Kunjungan> kunjunganModels;
    private String brandId, tokoId;
    private final int REQEUST_CAMERA = 1;
    private File imageCheck;
    private Bitmap photoImage;
    private ImageView imgPhoto;

    @BindView(R.id.spinnerBrand)
    Spinner spinnerBrand;
    @BindView(R.id.spinnerToko)
    Spinner spinnerToko;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    public TambahKunjunganFragment() {
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.tambah_kunjungan_fragment, container, false);
        ButterKnife.bind(this, rootView);

        brandList = new ArrayList<>();
        brandIdList = new ArrayList<>();
        tokoList = new ArrayList<>();
        tokoIdList = new ArrayList<>();
        kunjunganModels = new ArrayList<>();

        getListBrand();
        getListToko();

        return rootView;
    }

    @OnClick(R.id.btnAddPengeluaran)
    public void onCLickBtnAddPengeluaran() {
        showDialog();
        EditText tipePengeluaran = dialog.findViewById(R.id.etDialogTipePengeluaran);
        EditText nominal = dialog.findViewById(R.id.etDialogNominal);

        Button ambilPhoto = dialog.findViewById(R.id.btnDialogPhoto);
        imgPhoto = dialog.findViewById(R.id.imgPhoto);
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
            if (tipePengeluaran.getText().toString().equals("") || nominal.getText().toString().equals("")) {
                Toast.makeText(getContext(), "Field tidak boleh kosong", Toast.LENGTH_SHORT).show();
            } else {
                String uniqueID = UUID.randomUUID().toString();
                Kunjungan kunjunganModel = new Kunjungan();
                kunjunganModel.setId(uniqueID);
                kunjunganModel.setName(tipePengeluaran.getText().toString());
                kunjunganModel.setNominal(nominal.getText().toString());
                kunjunganModel.setImage(photoImage);
                kunjunganModels.add(kunjunganModel);
                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayout.VERTICAL, false));
                TambahKunjunganAdapter adapter = new TambahKunjunganAdapter(kunjunganModels, getActivity());
                recyclerView.setAdapter(adapter);
            }
            dialog.dismiss();
        });
        ImageView imgClose = dialog.findViewById(R.id.imgClose);
        imgClose.setOnClickListener(v -> dialog.dismiss());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQEUST_CAMERA && resultCode == RESULT_OK) {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            photoImage = (Bitmap) Objects.requireNonNull(data.getExtras()).get("data");
            Objects.requireNonNull(photoImage).compress(Bitmap.CompressFormat.JPEG, 100, stream);
            Glide.with(getContext())
                    .asBitmap()
                    .load(stream.toByteArray())
                    .apply(RequestOptions.bitmapTransform(
                            new RoundedCornersTransformation(getContext(), sCorner, sMargin))).into(imgPhoto);
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
                                brandId = null;
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });

                } catch (Exception e) {
                    Log.d("TAG EXCEPTION", e.getMessage());
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
        KatalogHelper.getListToko(new Constanta().PLACE_TYPE_SHOP, new RestCallback<ApiResponse<List<TokoResponse>>>() {
            @Override
            public void onSuccess(Headers headers, ApiResponse<List<TokoResponse>> listApiResponse) {
                Loading.hide(getActivity());
                try {
                    List<TokoResponse> res = listApiResponse.getData();
                    tokoList.add("Pilih Toko");
                    tokoIdList.add("null");
                    for (int i = 0; i < res.size(); i++) {
                        TokoResponse response = res.get(i);
                        tokoList.add(response.getName());
                        tokoIdList.add(response.getId());
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
                    Log.d("TAG EXCEPTION", e.getMessage());
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
}

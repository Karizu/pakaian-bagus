package com.example.pakaianbagus.presentation.home.piutang;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.pakaianbagus.R;
import com.example.pakaianbagus.api.PenjualanHelper;
import com.example.pakaianbagus.models.ApiResponse;
import com.example.pakaianbagus.models.MutasiBarang;
import com.example.pakaianbagus.util.dialog.Loading;
import com.rezkyatinnov.kyandroid.reztrofit.ErrorResponse;
import com.rezkyatinnov.kyandroid.reztrofit.RestCallback;

import java.io.ByteArrayOutputStream;
import java.io.File;
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
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import pl.aprilapps.easyphotopicker.DefaultCallback;
import pl.aprilapps.easyphotopicker.EasyImage;

public class PembayaranInvoiceFragment extends Fragment {

    private Dialog dialog;
    private List<String> tipeMutsaiList, mutasiIdList;
    private List<String> tokoList, tokoIdList;
    private List<MutasiBarang> barangList;
    private String mutasiId, tokoId;
    private final int REQEUST_CAMERA = 1, REQUEST_GALLERY = 2;
    private final int MY_PERMISSIONS_REQUEST_CAMERA = 100;
    private File profileImage;
    private Bitmap photoImage;
    private ImageView imgPhoto;
    private String id, amount, hutang, name, noTrx, formattedDate;
    private String spinnerValue = null;

    @BindView(R.id.tvTanggal)
    TextView tvTanggal;
    @BindView(R.id.etNominal)
    EditText etNominal;
    @BindView(R.id.etFromCash)
    EditText etFromCash;
    @BindView(R.id.etToCash)
    EditText etToCash;
    @BindView(R.id.etFromTransfer)
    EditText etFromTransfer;
    @BindView(R.id.etToTransfer)
    EditText etToTransfer;
    @BindView(R.id.spinnerType)
    Spinner spinnerType;
    @BindView(R.id.layoutForCash)
    LinearLayout layoutForCash;
    @BindView(R.id.layoutForTransfer)
    LinearLayout layoutForTransfer;
    @BindView(R.id.imgPhotoBukti)
    ImageView imgPhotoBukti;
    @BindView(R.id.btnAddPhotoCounter)
    LinearLayout btnAddPhotoCounter;

    public PembayaranInvoiceFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.pembayaran_invoice_fragment, container, false);
        ButterKnife.bind(this, rootView);

        id = Objects.requireNonNull(getArguments()).getString("id");
        name = getArguments().getString("name");
        amount = getArguments().getString("amount");
        hutang = getArguments().getString("hutang");
        noTrx = getArguments().getString("noTrx");

        tipeMutsaiList = new ArrayList<>();
        barangList = new ArrayList<>();
        mutasiIdList = new ArrayList<>();

        getDate();
        settingSpinnerType();

        return rootView;
    }

    private void settingSpinnerType() {
        // Spinner Drop down elements
        List<String> type = new ArrayList<>();
        type.add("TIPE PEMBAYARAN");
        type.add("Cash");
        type.add("Transfer");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(Objects.requireNonNull(getActivity()), R.layout.spinner_item, type){
            @Override
            public boolean isEnabled(int position) {
                return position != 0;
            }
        };
        dataAdapter.setDropDownViewResource(R.layout.spinner_stock_dropdown_item);
        spinnerType.setAdapter(dataAdapter);

        spinnerType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (type.get(position).equals("Cash")){
                        layoutForCash.setVisibility(View.VISIBLE);
                        layoutForTransfer.setVisibility(View.GONE);
                        spinnerValue = "Cash";
                    } else if (type.get(position).equals("Transfer")){
                        layoutForCash.setVisibility(View.GONE);
                        layoutForTransfer.setVisibility(View.VISIBLE);
                        spinnerValue = "Transfer";
                    }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @OnClick(R.id.btnBayar)
    void onClickBayar(){
        if (spinnerValue == null || etNominal.getText().toString().equals("")){
            Toast.makeText(getActivity(), "Silahkan lengkapi data terlebih dahulu", Toast.LENGTH_SHORT).show();

        } else {
            postPembayaranPiutang();
        }
    }

    private void postPembayaranPiutang(){
        Loading.show(getContext());
        RequestBody requestBody = null;
        Bitmap bitmap = BitmapFactory.decodeFile(profileImage.getAbsolutePath());

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 20, byteArrayOutputStream);

        if (spinnerType.getSelectedItem().toString().equals("Cash")){
            if (etFromCash.getText().toString().equals("") || etToCash.getText().toString().equals("")){
                Toast.makeText(getActivity(), "Silahkan lengkapi data", Toast.LENGTH_SHORT).show();
                Loading.hide(getContext());
            } else {
                requestBody = new MultipartBody.Builder()
                        .setType(MultipartBody.FORM)
                        .addFormDataPart("m_member_id", id)
                        .addFormDataPart("date", formattedDate)
                        .addFormDataPart("amount", etNominal.getText().toString())
                        .addFormDataPart("type", spinnerType.getSelectedItem().toString())
                        .addFormDataPart("from", etFromCash.getText().toString())
                        .addFormDataPart("to", etToCash.getText().toString())
                        .addFormDataPart("image", "photo.jpeg", RequestBody.create(MediaType.parse("image/jpeg"), byteArrayOutputStream.toByteArray()))
                        .build();
            }
        } else if (spinnerType.getSelectedItem().toString().equals("Transfer")){
            if (etFromTransfer.getText().toString().equals("") || etToTransfer.getText().toString().equals("")){
                Toast.makeText(getActivity(), "Silahkan lengkapi data", Toast.LENGTH_SHORT).show();
                Loading.hide(getContext());
            } else {
                requestBody = new MultipartBody.Builder()
                        .setType(MultipartBody.FORM)
                        .addFormDataPart("m_member_id", id)
                        .addFormDataPart("date", formattedDate)
                        .addFormDataPart("amount", etNominal.getText().toString())
                        .addFormDataPart("type", spinnerType.getSelectedItem().toString())
                        .addFormDataPart("from", etFromTransfer.getText().toString())
                        .addFormDataPart("to", etToTransfer.getText().toString())
                        .addFormDataPart("image", "photo.jpeg", RequestBody.create(MediaType.parse("image/jpeg"), byteArrayOutputStream.toByteArray()))
                        .build();
            }
        }

        PenjualanHelper.postPembayaranPiutang(requestBody, new RestCallback<ApiResponse>() {
            @Override
            public void onSuccess(Headers headers, ApiResponse body) {
                Loading.hide(getContext());
                Toast.makeText(getActivity(), "Pembayaran sukses", Toast.LENGTH_SHORT).show();

                Bundle bundle = new Bundle();
                bundle.putString("id", id);
                bundle.putString("name", name);
                bundle.putString("hutang", hutang);
                FragmentManager fm = getFragmentManager();
                FragmentTransaction ft = Objects.requireNonNull(fm).beginTransaction();
                ListInvoiceFragment listInvoiceFragment = new ListInvoiceFragment();
                listInvoiceFragment.setArguments(bundle);
                ft.setCustomAnimations(R.animator.fade_in, R.animator.fade_out);
                ft.replace(R.id.baseLayoutTambahMutasi, listInvoiceFragment);
                ft.commit();
            }

            @Override
            public void onFailed(ErrorResponse error) {
                Loading.hide(getContext());
                Log.d("FAILED Pembayaran", error.getMessage());
                Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCanceled() {

            }
        });
    }

    private void getDate() {
        Date c = Calendar.getInstance().getTime();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        formattedDate = df.format(c);
        tvTanggal.setText(formattedDate);
    }

    @OnClick(R.id.toolbar_back)
    public void toolbarBack(){
        Bundle bundle = new Bundle();
        bundle.putString("id", id);
        bundle.putString("amount", amount);
        bundle.putString("name", name);
        bundle.putString("hutang", hutang);
        bundle.putString("noTrx", noTrx);

        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = Objects.requireNonNull(fm).beginTransaction();
        ListInvoiceFragment listInvoiceFragment = new ListInvoiceFragment();
        listInvoiceFragment.setArguments(bundle);
        ft.setCustomAnimations(R.animator.fade_in, R.animator.fade_out);
        ft.replace(R.id.baseLayoutTambahMutasi, listInvoiceFragment);
        ft.commit();
    }

    @OnClick(R.id.btnAddPhotoCounter)
    public void setProfilePicture() {
        selectImage();
    }

    private void selectImage() {
        final CharSequence[] options = {"Take Photo", "Choose From Gallery","Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getActivity()));
        builder.setTitle("Select Option");
        builder.setItems(options, (dialog, item) -> {
            if (options[item].equals("Take Photo")) {
                EasyImage.openCamera(this, REQEUST_CAMERA);
            } else if (options[item].equals("Choose From Gallery")) {
                EasyImage.openGallery(this, REQUEST_GALLERY);
            } else if (options[item].equals("Cancel")) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

//    @SuppressLint("SetTextI18n")
//    @OnClick(R.id.btnAddMutasi)
//    public void onCLickBtnAddBarang(){
//        showDialog();
//        TextView title = dialog.findViewById(R.id.tvTitle);
//        title.setText("PEMBAYARAN");
//        EditText nama = dialog.findViewById(R.id.etDialogNamaBarang);
//        nama.setHint("TIPE PEMBAYARAN");
//        EditText qty = dialog.findViewById(R.id.etDialogQuantity);
//        qty.setHint("NOMINAL");
//        Button tambah = dialog.findViewById(R.id.btnDialogTambah);
//        tambah.setOnClickListener(v -> {
//            if (nama.getText().toString().equals("") || qty.getText().toString().equals("")){
//                Toast.makeText(getActivity(), "Silahkan isi semua field", Toast.LENGTH_SHORT).show();
//            } else {
//                String uniqueID = UUID.randomUUID().toString();
//                MutasiBarang barang = new MutasiBarang();
//                barang.setId(uniqueID);
//                barang.setName(nama.getText().toString());
//                barang.setQty(qty.getText().toString());
//                barangList.add(barang);
//                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayout.VERTICAL, false));
//                TambahMutasiBarangAdapter adapter = new TambahMutasiBarangAdapter(barangList, getActivity());
//                recyclerView.setAdapter(adapter);
//                dialog.dismiss();
//            }
//        });
//        ImageView imgClose = dialog.findViewById(R.id.imgClose);
//        imgClose.setOnClickListener(v -> dialog.dismiss());
//    }

    private void showDialog() {
        dialog = new Dialog(Objects.requireNonNull(getActivity()));
        //set content
        dialog.setContentView(R.layout.dialog_tambah_barang);
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
                btnAddPhotoCounter.setVisibility(View.GONE);
                Glide.with(Objects.requireNonNull(getContext()))
                        .load(imageFile)
                        .apply(RequestOptions.circleCropTransform())
                        .into(imgPhotoBukti);
                profileImage = imageFile;
            }
        });
    }

    private void checkPermissionGrant(){
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(Objects.requireNonNull(getActivity()),
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.CAMERA)) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.CAMERA},
                        MY_PERMISSIONS_REQUEST_CAMERA);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_CAMERA: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

}

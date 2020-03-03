package com.example.pakaianbagus.presentation.home.stockopname.verify;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pakaianbagus.R;
import com.example.pakaianbagus.api.HomeHelper;
import com.example.pakaianbagus.models.ApiResponse;
import com.example.pakaianbagus.models.CounterResponse;
import com.example.pakaianbagus.models.PlaceWorkModel;
import com.example.pakaianbagus.presentation.home.photocounter.PhotoCounterActivity;
import com.example.pakaianbagus.presentation.home.photocounter.adapter.PhotoAdapter;
import com.example.pakaianbagus.util.Constanta;
import com.example.pakaianbagus.util.LinePagerIndicatorDecoration;
import com.example.pakaianbagus.util.dialog.Loading;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VerifyStockOpnameActivity extends AppCompatActivity {

    private final int REQEUST_CAMERA = 1;
    private Bitmap photoImage;
    private File imageCheck;
    private List<PlaceWorkModel> picturesList;
    private ArrayList<Uri> arrayList;
    private List<MultipartBody.Part> parts;
    private List<RequestBody> requestBodyList = new ArrayList<>();
    private PhotoAdapter mAdapter;
    private Context context;
    private Uri yourUri;
    private String placeId, brandId;
    private String roleId, userId, counterId;
    private boolean isSpg;

    @BindView(R.id.layoutPreview)
    LinearLayout layoutPreview;
    @BindView(R.id.imgSelectedImageWork)
    ImageView selectedImageWork;
    @BindView(R.id.imgNoData)
    ImageView imgNoData;
    @BindView(R.id.toolbar_title)
    TextView toolbar_title;
    @BindView(R.id.header_title)
    TextView header_title;
    @BindView(R.id.btnAmbilPhoto)
    Button btnAmbilPhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_stock_opname);
        ButterKnife.bind(this);

        context = this;

    }

    @OnClick(R.id.btnSubmit)
    void onClickSubmit(){
        doVerifyStockOpName();
    }

    @OnClick(R.id.btnAmbilPhoto)
    void onClickBtnPhoto() {
        if (ContextCompat.checkSelfPermission(Objects.requireNonNull(VerifyStockOpnameActivity.this), Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(VerifyStockOpnameActivity.this,
                    new String[]{Manifest.permission.CAMERA}, REQEUST_CAMERA);
        } else {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent, REQEUST_CAMERA);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQEUST_CAMERA && resultCode == RESULT_OK) {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            photoImage = (Bitmap) Objects.requireNonNull(data.getExtras()).get("data");
            Objects.requireNonNull(photoImage).compress(Bitmap.CompressFormat.JPEG, 100, stream);
            imgNoData.setImageBitmap(photoImage);
            try {
                File outputDir = getCacheDir();
                imageCheck = File.createTempFile("photo", "jpeg", outputDir);
                yourUri = Uri.fromFile(imageCheck);
                FileOutputStream outputStream = openFileOutput("photo.jpeg", Context.MODE_PRIVATE);
                outputStream.write(stream.toByteArray());
                outputStream.close();
                Log.d("Write File", "Success");
            } catch (IOException e) {
                e.printStackTrace();
                Log.d("Write File", "Failed2");
            }
        }
    }

    private void doVerifyStockOpName() {
        Loading.show(context);
        if (imageCheck != null) {

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            photoImage.compress(Bitmap.CompressFormat.JPEG, 20, byteArrayOutputStream);

            RequestBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("m_place_id", placeId)
                    .addFormDataPart("m_brand_id", brandId)
                    .addFormDataPart("user_id", userId)
                    .addFormDataPart("img_front_view", "photo.jpeg", RequestBody.create(MediaType.parse("image/jpeg"), byteArrayOutputStream.toByteArray()))
                    .build();

            HomeHelper.postImageFrontView(requestBody, new Callback<ApiResponse<CounterResponse>>() {
                @Override
                public void onResponse(Call<ApiResponse<CounterResponse>> call, Response<ApiResponse<CounterResponse>> response) {
                    Loading.hide(context);
                    try {
                        CounterResponse counterResponse = Objects.requireNonNull(response.body()).getData();
                        counterId = counterResponse.getId();
                        Toast.makeText(context, "Sukses mengirim foto", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(context, PhotoCounterActivity.class);
                        intent.putExtra(Constanta.FLAG_COUNTER, Constanta.FOTO_RAK_GANTUNG);
                        intent.putExtra("counterId", counterId);
                        startActivity(intent);

                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(context, response.message(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ApiResponse<CounterResponse>> call, Throwable t) {
                    Loading.hide(context);
                    Log.d("TAG FAILURE", Objects.requireNonNull(t.getMessage()));
                }
            });
        } else {
            Loading.hide(context);
            Toast.makeText(context, "Silahkan ambil foto", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @OnClick(R.id.toolbar_back)
    void onClickBack(){
        onBackPressed();
    }
}

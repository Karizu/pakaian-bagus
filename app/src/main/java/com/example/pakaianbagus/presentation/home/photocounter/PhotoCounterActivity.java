package com.example.pakaianbagus.presentation.home.photocounter;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.FileUtils;
import android.provider.ContactsContract;
import android.provider.MediaStore;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pakaianbagus.MainActivity;
import com.example.pakaianbagus.R;
import com.example.pakaianbagus.api.HomeHelper;
import com.example.pakaianbagus.models.ApiResponse;
import com.example.pakaianbagus.models.CounterResponse;
import com.example.pakaianbagus.models.PlaceWorkModel;
import com.example.pakaianbagus.models.StandHanger;
import com.example.pakaianbagus.presentation.home.photocounter.adapter.PhotoAdapter;
import com.example.pakaianbagus.util.Constanta;
import com.example.pakaianbagus.util.LinePagerIndicatorDecoration;
import com.example.pakaianbagus.util.SessionManagement;
import com.example.pakaianbagus.util.dialog.Loading;
import com.rezkyatinnov.kyandroid.session.Session;
import com.rezkyatinnov.kyandroid.session.SessionNotFoundException;

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

public class PhotoCounterActivity extends AppCompatActivity {

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

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
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
    @BindView(R.id.btnSelesai)
    Button btnSelesai;
    @BindView(R.id.btnAmbilPhoto)
    Button btnAmbilPhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_counter);
        ButterKnife.bind(this);
        context = this;
        picturesList = new ArrayList<>();
        arrayList = new ArrayList<>();
        parts = new ArrayList<>();
        layoutPreview.setVisibility(View.GONE);
        Intent intent = getIntent();

        try {
            if (intent.getStringExtra(Constanta.FLAG_COUNTER) != null) {
                toolbar_title.setText(intent.getStringExtra(Constanta.FLAG_COUNTER));
                header_title.setText(intent.getStringExtra(Constanta.FLAG_COUNTER));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (toolbar_title.getText().toString().equals(Constanta.FOTO_TAMPAK_DEPAN)) {
            btnSelesai.setVisibility(View.GONE);
        }

        try {
            if (intent.getStringExtra("counterId") != null) {
                counterId = intent.getStringExtra("counterId");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            roleId = Session.get(Constanta.ROLE_ID).getValue();
            userId = Session.get(Constanta.USER_ID).getValue();
            isSpg = roleId.equals(SessionManagement.ROLE_SPG);
            if (isSpg) {
                placeId = Session.get(Constanta.TOKO).getValue();
                brandId = Session.get(Constanta.BRAND).getValue();
            } else {
                placeId = Objects.requireNonNull(intent).getStringExtra("id_toko");
                brandId = Objects.requireNonNull(intent).getStringExtra("id_brand");
            }
        } catch (SessionNotFoundException e) {
            e.printStackTrace();
            placeId = "1";
            brandId = "1";
        }

        if (picturesList.size() >= 1) {
            imgNoData.setVisibility(View.GONE);
        }

        @SuppressLint("WrongConstant") LinearLayoutManager mLayoutManager = new LinearLayoutManager(this, LinearLayout.HORIZONTAL, false);
        recyclerView.setLayoutManager(mLayoutManager);
        SnapHelper helper = new LinearSnapHelper();
        helper.attachToRecyclerView(recyclerView);

    }

    @OnClick(R.id.btnLanjut)
    void onClickLanjut() {
        if (toolbar_title.getText().toString().equals(Constanta.FOTO_TAMPAK_DEPAN)) {
            postImageFrontView();
        } else if (toolbar_title.getText().toString().equals(Constanta.FOTO_RAK_GANTUNG)) {
            postImageStandHanger();
        } else if (toolbar_title.getText().toString().equals(Constanta.BACKWALL)) {
            postUpdateImageCounter("img_backwall", Constanta.MEJA_DISPLAY);
        } else if (toolbar_title.getText().toString().equals(Constanta.MEJA_DISPLAY)) {
            postUpdateImageCounter("img_display_table", Constanta.TWO_WAY);
        } else if (toolbar_title.getText().toString().equals(Constanta.TWO_WAY)) {
            postImageTwoWay();
        } else if (toolbar_title.getText().toString().equals(Constanta.RACK)) {
            postImageRack();
        } else if (toolbar_title.getText().toString().equals(Constanta.WAGON_JAUH)) {
            postImageRack();
        } else if (toolbar_title.getText().toString().equals(Constanta.WAGON_DEKAT)) {
            postUpdateImageCounter("img_wagon_detail", Constanta.IMAGE_OTHER);
        } else if (toolbar_title.getText().toString().equals(Constanta.IMAGE_OTHER)) {
            postUpdateImageCounter("img_other", "null");
        }
    }

    @OnClick(R.id.imgClose)
    void onClickClose() {
        layoutPreview.setVisibility(View.GONE);
    }

    @OnClick(R.id.toolbar_back)
    void onClickToolbarBack() {
        onBackPressed();
    }

    @OnClick(R.id.btnAmbilPhoto)
    void onClickBtnPhoto() {
        if (ContextCompat.checkSelfPermission(Objects.requireNonNull(PhotoCounterActivity.this), Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(PhotoCounterActivity.this,
                    new String[]{Manifest.permission.CAMERA}, REQEUST_CAMERA);
        } else {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent, REQEUST_CAMERA);
        }
    }

    @OnClick(R.id.btnSelesai)
    void onClickBtnSelesai(){
        Toast.makeText(context, "Photo counter berhasil dikirim", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(PhotoCounterActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQEUST_CAMERA && resultCode == RESULT_OK) {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            photoImage = (Bitmap) Objects.requireNonNull(data.getExtras()).get("data");
            Objects.requireNonNull(photoImage).compress(Bitmap.CompressFormat.JPEG, 100, stream);
//            layoutPreview.setVisibility(View.VISIBLE);
//            layout.setVisibility(View.GONE);
//            selectedImageWork.setImageBitmap(photoImage);
            PlaceWorkModel placeWorkModel = new PlaceWorkModel(); // the model between activity and adapter
            placeWorkModel.setImg(photoImage);  // here i pass the photo
            picturesList.add(placeWorkModel);
            imgNoData.setVisibility(View.GONE);

            if (toolbar_title.getText().toString().equals(Constanta.FOTO_TAMPAK_DEPAN) ||
                    toolbar_title.getText().toString().equals(Constanta.MEJA_DISPLAY) ||
                    toolbar_title.getText().toString().equals(Constanta.BACKWALL) ||
                    toolbar_title.getText().toString().equals(Constanta.WAGON_DEKAT) ||
                    toolbar_title.getText().toString().equals(Constanta.WAGON_JAUH) ||
                    toolbar_title.getText().toString().equals(Constanta.IMAGE_OTHER)) {
                if (picturesList.size() == 1) {
                    btnAmbilPhoto.setVisibility(View.GONE);
                }
            }

            parts.add(prepareFilePart("attachment[]", photoImage));

            mAdapter = new PhotoAdapter(picturesList, context);
            recyclerView.addItemDecoration(new LinePagerIndicatorDecoration());
            recyclerView.setAdapter(mAdapter);
//            mAdapter.notifyDataSetChanged();
            try {
                File outputDir = getCacheDir();
                imageCheck = File.createTempFile("photo", "jpeg", outputDir);
                yourUri = Uri.fromFile(imageCheck);

                RequestBody fileReqBody = RequestBody.create(MediaType.parse("image/*"), imageCheck);
                requestBodyList.add(fileReqBody);

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

    private void postImageFrontView() {
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

    private void postImageStandHanger() {
        Loading.show(context);

        parts.add(prepareParts("counter_id", counterId));

        if (picturesList.size() < 1) {
            Loading.hide(context);
            Toast.makeText(context, "Silahkan ambil foto terlebih dahulu", Toast.LENGTH_SHORT).show();
        } else {
            HomeHelper.postImageStandHanger(parts, new Callback<ApiResponse<CounterResponse>>() {
                @Override
                public void onResponse(Call<ApiResponse<CounterResponse>> call, Response<ApiResponse<CounterResponse>> response) {
                    Loading.hide(context);
                    try {
                        Toast.makeText(context, "Sukses mengirim foto", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(context, PhotoCounterActivity.class);
                        intent.putExtra(Constanta.FLAG_COUNTER, Constanta.BACKWALL);
                        intent.putExtra("counterId", counterId);
                        startActivity(intent);
                    } catch (Exception e) {
                        e.printStackTrace();
//                    Toast.makeText(context, "Gagal mengirim foto", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ApiResponse<CounterResponse>> call, Throwable t) {
                    Loading.hide(context);
                    t.printStackTrace();
                }
            });
        }
    }

    private void postImageTwoWay() {
        Loading.show(context);

        parts.add(prepareParts("counter_id", counterId));

        if (picturesList.size() < 1) {
            Loading.hide(context);
            Toast.makeText(context, "Silahkan ambil foto terlebih dahulu", Toast.LENGTH_SHORT).show();
        } else {
            HomeHelper.postImageTwoWay(parts, new Callback<ApiResponse<CounterResponse>>() {
                @Override
                public void onResponse(Call<ApiResponse<CounterResponse>> call, Response<ApiResponse<CounterResponse>> response) {
                    Loading.hide(context);
                    try {
                        Toast.makeText(context, "Sukses mengirim foto", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(context, PhotoCounterActivity.class);
                        intent.putExtra(Constanta.FLAG_COUNTER, Constanta.RACK);
                        intent.putExtra("counterId", counterId);
                        startActivity(intent);
                    } catch (Exception e) {
                        e.printStackTrace();
//                    Toast.makeText(context, "Gagal mengirim foto", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ApiResponse<CounterResponse>> call, Throwable t) {
                    Loading.hide(context);
                    t.printStackTrace();
                }
            });
        }
    }

    private void postImageRack() {
        Loading.show(context);

        parts.add(prepareParts("counter_id", counterId));

        if (picturesList.size() < 1) {
            Loading.hide(context);
            Toast.makeText(context, "Silahkan ambil foto terlebih dahulu", Toast.LENGTH_SHORT).show();
        } else {
            HomeHelper.postImageRack(parts, new Callback<ApiResponse<CounterResponse>>() {
                @Override
                public void onResponse(Call<ApiResponse<CounterResponse>> call, Response<ApiResponse<CounterResponse>> response) {
                    Loading.hide(context);
                    try {
                        if (toolbar_title.getText().toString().equals(Constanta.WAGON_JAUH)){
                            Toast.makeText(context, "Sukses mengirim foto", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(context, PhotoCounterActivity.class);
                            intent.putExtra(Constanta.FLAG_COUNTER, Constanta.WAGON_DEKAT);
                            intent.putExtra("counterId", counterId);
                            startActivity(intent);
                        } else {
                            Toast.makeText(context, "Sukses mengirim foto", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(context, PhotoCounterActivity.class);
                            intent.putExtra(Constanta.FLAG_COUNTER, Constanta.WAGON_JAUH);
                            intent.putExtra("counterId", counterId);
                            startActivity(intent);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
//                    Toast.makeText(context, "Gagal mengirim foto", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ApiResponse<CounterResponse>> call, Throwable t) {
                    Loading.hide(context);
                    t.printStackTrace();
                }
            });
        }
    }

    private void postUpdateImageCounter(String param, String nextFlagCounter) {
        Loading.show(context);
        if (imageCheck != null) {

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            photoImage.compress(Bitmap.CompressFormat.JPEG, 20, byteArrayOutputStream);

            RequestBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart(param, "photo.jpeg", RequestBody.create(MediaType.parse("image/jpeg"), byteArrayOutputStream.toByteArray()))
                    .build();

            HomeHelper.postUpdateImageCounter(counterId, requestBody, new Callback<ApiResponse<CounterResponse>>() {
                @Override
                public void onResponse(Call<ApiResponse<CounterResponse>> call, Response<ApiResponse<CounterResponse>> response) {
                    Loading.hide(context);
                    try {
                        if (toolbar_title.getText().toString().equals(Constanta.IMAGE_OTHER)) {
                            Toast.makeText(context, "Photo counter berhasil dikirim", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(PhotoCounterActivity.this, MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        } else {
                            Toast.makeText(context, "Sukses mengirim foto", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(context, PhotoCounterActivity.class);
                            intent.putExtra(Constanta.FLAG_COUNTER, nextFlagCounter);
                            intent.putExtra("counterId", counterId);
                            startActivity(intent);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
//                        Toast.makeText(context, "Gagal mengirim data", Toast.LENGTH_SHORT).show();
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

    @NonNull
    private MultipartBody.Part prepareFilePart(String partName, Bitmap photoImage) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        photoImage.compress(Bitmap.CompressFormat.JPEG, 20, byteArrayOutputStream);
        RequestBody requestFile = RequestBody.create(MediaType.parse("image/jpeg"), byteArrayOutputStream.toByteArray());

        return MultipartBody.Part.createFormData(partName, "photo.jpeg", requestFile);
    }

    @NonNull
    private MultipartBody.Part prepareParts(String partName, String counterId) {
        return MultipartBody.Part.createFormData(partName, counterId);
    }


    public String convertImage2Base64() {
        Bitmap bitmap = ((BitmapDrawable) selectedImageWork.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 90, stream);
        byte[] image = stream.toByteArray();
        return ("data:image/jpeg;base64," + Base64.encodeToString(image, 0));
    }

    public String getPath(Uri uri) {
        // just some safety built in
        if (uri == null) {
            // TODO perform some logging or show user feedback
            return null;
        }
        // try to retrieve the image from the media store first
        // this will only work for images selected from gallery
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        if (cursor != null) {
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            String path = cursor.getString(column_index);
            cursor.close();
            return path;
        }
        // this is our fallback here
        return uri.getPath();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}

package com.example.pakaianbagus.presentation.home.photocounter;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.provider.MediaStore;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pakaianbagus.R;
import com.example.pakaianbagus.models.PlaceWorkModel;
import com.example.pakaianbagus.presentation.home.photocounter.adapter.PhotoAdapter;
import com.example.pakaianbagus.util.LinePagerIndicatorDecoration;

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

public class PhotoCounterActivity extends AppCompatActivity {

    private final int REQEUST_CAMERA = 1;
    private Bitmap photoImage;
    private File imageCheck;
    private List<PlaceWorkModel> picturesList;
    private PhotoAdapter mAdapter;
    private Context context;
    private Uri yourUri;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_counter);
        ButterKnife.bind(this);
        context = this;
        picturesList = new ArrayList<>();
        layoutPreview.setVisibility(View.GONE);

        try {
            Intent intent = getIntent();
            if (intent.getStringExtra("flag") != null){
                toolbar_title.setText(intent.getStringExtra("flag"));
                header_title.setText(intent.getStringExtra("flag"));
            }
        } catch (Exception e){
            e.printStackTrace();
        }

        if (picturesList.size() >= 1){
            imgNoData.setVisibility(View.GONE);
        }

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this, LinearLayout.HORIZONTAL, false);
        recyclerView.setLayoutManager(mLayoutManager);
        SnapHelper helper = new LinearSnapHelper();
        helper.attachToRecyclerView(recyclerView);

    }

    @OnClick(R.id.btnLanjut)
    void onClickLanjut(){
        if (!toolbar_title.getText().toString().equals("FOTO RAK GANTUNG")){
            Intent intent = new Intent(context, PhotoCounterActivity.class);
            intent.putExtra("flag", "FOTO RAK GANTUNG");
            startActivity(intent);
        } else {
            Toast.makeText(context, "On Progress", Toast.LENGTH_SHORT).show();
        }
    }

    @OnClick(R.id.imgClose)
    void onClickClose(){
        layoutPreview.setVisibility(View.GONE);
    }

    @OnClick(R.id.toolbar_back)
    void onClickToolbarBack(){
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
            placeWorkModel.setImg(photoImage);  // here i pass the photogggg
            picturesList.add(placeWorkModel);
            imgNoData.setVisibility(View.GONE);
            Log.d("TAG PHOTO", String.valueOf(photoImage));
            mAdapter = new PhotoAdapter(picturesList, context);
            recyclerView.addItemDecoration(new LinePagerIndicatorDecoration());
            recyclerView.setAdapter(mAdapter);
//            mAdapter.notifyDataSetChanged();
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

    public String convertImage2Base64() {
        Bitmap  bitmap = ((BitmapDrawable) selectedImageWork.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 90, stream);
        byte[] image = stream.toByteArray();
        return ("data:image/jpeg;base64," + Base64.encodeToString(image, 0));
    }

    public String getPath(Uri uri) {
        // just some safety built in
        if( uri == null ) {
            // TODO perform some logging or show user feedback
            return null;
        }
        // try to retrieve the image from the media store first
        // this will only work for images selected from gallery
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        if( cursor != null ){
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

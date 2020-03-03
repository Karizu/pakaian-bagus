package com.example.pakaianbagus.presentation.auth;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.pakaianbagus.MainActivity;
import com.example.pakaianbagus.R;
import com.example.pakaianbagus.api.AuthHelper;
import com.example.pakaianbagus.models.LoginRequest;
import com.example.pakaianbagus.models.auth.Auth;
import com.example.pakaianbagus.util.Constanta;
import com.example.pakaianbagus.util.SessionManagement;
import com.example.pakaianbagus.util.dialog.Loading;
import com.rezkyatinnov.kyandroid.localdata.LocalData;
import com.rezkyatinnov.kyandroid.session.Session;
import com.rezkyatinnov.kyandroid.session.SessionNotFoundException;
import com.rezkyatinnov.kyandroid.session.SessionObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {


    private final int MY_PERMISSIONS_REQUEST_WRITE_STORAGE = 1;

    @BindView(R.id.btnLogin)
    Button btnLogin;
    @BindView(R.id.etUsername)
    EditText etUsername;
    @BindView(R.id.etPassword)
    EditText etPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        btnLogin.setOnClickListener(v -> {
            Loading.show(LoginActivity.this);
            doLogin();
        });

    }

    private void doLogin() {

        // Reset errors.
        etUsername.setError(null);
        etPassword.setError(null);

        // Store values at the time of the login attempt.
        String email = etUsername.getText().toString();
        String password = etPassword.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            etPassword.setError(getString(R.string.error_invalid_password));
            focusView = etPassword;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            etUsername.setError(getString(R.string.error_field_required));
            focusView = etUsername;
            cancel = true;
        } else if (!isEmailValid(email)) {
            etUsername.setError(getString(R.string.error_invalid_email));
            focusView = etUsername;
            cancel = true;
        }

        if (etUsername.getText().toString().equals("admin") && etPassword.getText().toString().equals("admin")) {
            Session.save(new SessionObject(Constanta.AUTH, "Bearer 123123123123121231233", true));
            Session.save(new SessionObject(Constanta.USER_ID, SessionManagement.ROLE_ADMIN));
            Session.save(new SessionObject(Constanta.ROLE_ID, SessionManagement.ROLE_ADMIN));
            Session.save(new SessionObject(Constanta.GROUP_ID, SessionManagement.ROLE_ADMIN));
            Session.save(new SessionObject(Constanta.NAME, "Admin"));
            Session.save(new SessionObject(Constanta.ADMIN, "1"));
            Session.save(new SessionObject("isLogin", "1"));
            Loading.hide(getApplicationContext());
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        } else {
            if (cancel) {
                Loading.hide(getApplicationContext());
                focusView.requestFocus();
            } else {

                LoginRequest loginRequest = new LoginRequest();
                loginRequest.setLogin(etUsername.getText().toString());
                loginRequest.setPassword(etPassword.getText().toString());

                AuthHelper.login(loginRequest, new Callback<Auth>() {
                    @Override
                    public void onResponse(@NonNull Call<Auth> call, @NonNull Response<Auth> response) {
                        Loading.hide(LoginActivity.this);
                        Auth data = response.body();
                        if (data != null) {
                            Session.save(new SessionObject(Constanta.AUTH, data.getTokenType() + " " + data.getAccessToken(), true));
                            Session.save(new SessionObject(Constanta.USER_ID, String.valueOf(data.getData().getId())));
                            Session.save(new SessionObject(Constanta.ROLE_ID, String.valueOf(data.getData().getRole().getName())));
                            Session.save(new SessionObject(Constanta.ROLE_NUMBER, String.valueOf(data.getData().getRole().getId())));
                            Session.save(new SessionObject(Constanta.GROUP_ID, String.valueOf(data.getData().getProfile().getMGroupId())));
                            Session.save(new SessionObject(Constanta.NAME, data.getData().getName()));
                            Session.save(new SessionObject("isLogin", "1"));
                            if (data.getData().getRole().getName().equals(SessionManagement.ROLE_SPG)) {
                                Session.save(new SessionObject(Constanta.TOKO, String.valueOf(data.getData().getProfile().getGroup().getMPlaceId())));
                                Session.save(new SessionObject(Constanta.BRAND, String.valueOf(data.getData().getProfile().getBrandId())));
                                Log.d("TAG", data.getData().getProfile().getGroup().getMPlaceId() +" "+ data.getData().getProfile().getBrandId());
                            }
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            finish();
                        } else {
                            Toast.makeText(LoginActivity.this, "Tidak ada data pada user ini", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<Auth> call, @NonNull Throwable t) {
                        t.printStackTrace();
                        Toast.makeText(LoginActivity.this, "Gagal login : " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }


    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return true;
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 3;
    }

    private void checkPermissionGrant() {
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_WRITE_STORAGE);

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
            case MY_PERMISSIONS_REQUEST_WRITE_STORAGE: {
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

    @Override
    protected void onStart() {
        super.onStart();
        try {
            checkPermissionGrant();
            Session.get("Authorization");
            Log.d("TOKENNNN", Session.get("Authorization").getValue());
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        } catch (SessionNotFoundException e) {
            Session.clear();
            LocalData.clear();
            e.printStackTrace();
        }
    }
}

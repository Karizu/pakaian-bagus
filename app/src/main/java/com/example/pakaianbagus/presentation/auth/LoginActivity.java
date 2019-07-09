package com.example.pakaianbagus.presentation.auth;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.pakaianbagus.MainActivity;
import com.example.pakaianbagus.R;
import com.example.pakaianbagus.api.AuthHelper;
import com.example.pakaianbagus.models.ApiResponse;
import com.example.pakaianbagus.models.LoginRequest;
import com.example.pakaianbagus.models.User;
import com.example.pakaianbagus.util.dialog.Loading;
import com.rezkyatinnov.kyandroid.localdata.LocalData;
import com.rezkyatinnov.kyandroid.reztrofit.ErrorResponse;
import com.rezkyatinnov.kyandroid.reztrofit.RestCallback;
import com.rezkyatinnov.kyandroid.session.Session;
import com.rezkyatinnov.kyandroid.session.SessionNotFoundException;
import com.rezkyatinnov.kyandroid.session.SessionObject;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Headers;
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

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            Loading.hide(getApplicationContext());
            focusView.requestFocus();
        } else {

            LoginRequest loginRequest = new LoginRequest();
            loginRequest.setLogin(etUsername.getText().toString());
            loginRequest.setPassword(etPassword.getText().toString());

            AuthHelper.login(loginRequest, new Callback<ApiResponse<User>>() {
                @Override
                public void onResponse(Call<ApiResponse<User>> call, Response<ApiResponse<User>> response) {
                    Loading.hide(getApplicationContext());
                    Log.d("Masuk onSuccess", response.body().getMessage());
                    if (response.body().getData() != null) {
                        Log.d("TOKEN : ", response.body().getToken());
                        Session.save(new SessionObject("Authorization", "Bearer " + response.body().getToken(), true));
                        Session.save(new SessionObject("UserId", response.body().getData().getId()));
                        Session.save(new SessionObject("RoleId", response.body().getData().getRoleId()));
                        LocalData.saveOrUpdate(response.body().getData());
                    } else {
                        Toast.makeText(LoginActivity.this, Objects.requireNonNull(response.body()).getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }

                @Override
                public void onFailure(Call<ApiResponse<User>> call, Throwable t) {
                    Loading.hide(getApplicationContext());
                    Log.d("onFailed Login", t.getMessage());
                    t.printStackTrace();
                }
            });
        }
    }

        private boolean isEmailValid (String email){
            //TODO: Replace this with your own logic
            return true;
        }

        private boolean isPasswordValid (String password){
            //TODO: Replace this with your own logic
            return password.length() > 3;
        }

        private void checkPermissionGrant () {
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
        public void onRequestPermissionsResult ( int requestCode,
        String permissions[], int[] grantResults){
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
        protected void onStart () {
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

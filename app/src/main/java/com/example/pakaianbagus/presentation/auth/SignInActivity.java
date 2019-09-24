package com.example.pakaianbagus.presentation.auth;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
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
import com.example.pakaianbagus.util.SessionManagement;
import com.example.pakaianbagus.util.dialog.Loading;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignInActivity extends AppCompatActivity {

    private final int MY_PERMISSIONS_REQUEST_WRITE_STORAGE = 1;

    @BindView(R.id.btnLogin)
    Button btnLogin;
    @BindView(R.id.etUsername)
    EditText etUsername;
    @BindView(R.id.etPassword)
    EditText etPassword;
    private SessionManagement sessionManagement;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        ButterKnife.bind(this);

        sessionManagement = new SessionManagement(getApplicationContext());
        sessionManagement.checkLogin();
    }

    @OnClick(R.id.btnLogin)
    public void onClickLogin() {
        Loading.show(SignInActivity.this);
        doLogin();
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

            AuthHelper.doLogin(etUsername.getText().toString(), etPassword.getText().toString(), new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    Loading.hide(getApplicationContext());
                    if (response.body() != null) {
                        try {
                            JSONObject jsonRESULTS = new JSONObject(response.body().string());
                            if (jsonRESULTS.getString("status").equals("1")) {
                                String userId = jsonRESULTS.getJSONObject("data").getString("id");
                                String roleid = jsonRESULTS.getJSONObject("data").getString("role_id");
                                String token = jsonRESULTS.getString("token");
                                sessionManagement.createLoginSession(userId,roleid,"Bearer "+token);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        Intent intent = new Intent(SignInActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(SignInActivity.this, Objects.requireNonNull(response).message(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Loading.hide(getApplicationContext());
                    Log.d("Error", t.getMessage());
                }
            });
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
            HashMap<String, String> user = sessionManagement.getUserDetails();
            Log.d("Token", user.get(SessionManagement.KEY_BEARER_TOKEN));
            Intent intent = new Intent(SignInActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        } catch (Exception e){
            e.getMessage();
        }

    }

}

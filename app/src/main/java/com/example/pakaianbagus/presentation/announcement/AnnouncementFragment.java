package com.example.pakaianbagus.presentation.announcement;


import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.pakaianbagus.R;
import com.example.pakaianbagus.api.AnnouncementHelper;
import com.example.pakaianbagus.models.AnnouncementDetailResponse;
import com.example.pakaianbagus.models.ApiResponse;
import com.example.pakaianbagus.presentation.home.HomeFragment;
import com.example.pakaianbagus.presentation.home.notification.NotificationFragment;
import com.example.pakaianbagus.util.RoundedCornersTransformation;
import com.example.pakaianbagus.util.dialog.Loading;
import com.rezkyatinnov.kyandroid.reztrofit.ErrorResponse;
import com.rezkyatinnov.kyandroid.reztrofit.RestCallback;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Headers;

import static com.example.pakaianbagus.presentation.home.photocounter.adapter.PhotoAdapter.sCorner;
import static com.example.pakaianbagus.presentation.home.photocounter.adapter.PhotoAdapter.sMargin;

/**
 * A simple {@link Fragment} subclass.
 */
public class AnnouncementFragment extends Fragment {

    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.tvDeskripsi)
    TextView tvDeskripsi;
    @BindView(R.id.imgAnnouncement)
    ImageView image;

    private String id;
    private String fragment;

    public AnnouncementFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.announcement_fragment, container, false);
        ButterKnife.bind(this, v);

        id = Objects.requireNonNull(getArguments()).getString("announcement_id");
        fragment = Objects.requireNonNull(getArguments()).getString("fragment");

        getData();

        return v;
    }

    private void getData() {
        Loading.show(getContext());
        AnnouncementHelper.getDetailAnnouncement(id, new RestCallback<ApiResponse<AnnouncementDetailResponse>>() {
            @Override
            public void onSuccess(Headers headers, ApiResponse<AnnouncementDetailResponse> body) {
                Loading.hide(getContext());
                AnnouncementDetailResponse detailResponse = body.getData();

                tvTitle.setText(detailResponse.getTitle());
                tvDeskripsi.setText(detailResponse.getDescription());

                Glide.with(Objects.requireNonNull(getContext()))
                        .applyDefaultRequestOptions(
                                new RequestOptions()
                                        .placeholder(R.drawable.jeans)
                                        .error(R.drawable.jeans))
                        .load(detailResponse.getImage())
                        .apply(RequestOptions.bitmapTransform(
                                new RoundedCornersTransformation(getContext(), sCorner, sMargin)))
                        .into(image);
            }

            @Override
            public void onFailed(ErrorResponse error) {
                Loading.hide(getContext());
                Toast.makeText(getContext(), "Gagal mengambil detail : " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCanceled() {

            }
        });
    }

    @OnClick(R.id.toolbar_back)
    public void toolbarBack() {
        Fragment fr = new HomeFragment();
        if (fragment.equals("home")) {
            fr = new HomeFragment();
        } else if (fragment.equals("notif")) {
            fr = new NotificationFragment();
        }
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = Objects.requireNonNull(fm).beginTransaction();
        ft.setCustomAnimations(R.animator.fade_in, R.animator.fade_out);
        ft.replace(R.id.baseLayout, fr);
        ft.commit();
    }

}

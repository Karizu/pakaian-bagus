package com.example.pakaianbagus.presentation.home.notification;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.pakaianbagus.R;
import com.example.pakaianbagus.api.HomeHelper;
import com.example.pakaianbagus.models.AnnouncementResponse;
import com.example.pakaianbagus.models.ApiResponse;
import com.example.pakaianbagus.models.News;
import com.example.pakaianbagus.presentation.announcement.AnnouncementFragment;
import com.example.pakaianbagus.presentation.home.HomeFragment;
import com.example.pakaianbagus.presentation.home.notification.adapter.NotificationAdapter;
import com.rezkyatinnov.kyandroid.reztrofit.ErrorResponse;
import com.rezkyatinnov.kyandroid.reztrofit.RestCallback;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Headers;

public class NotificationFragment extends Fragment {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    private NotificationAdapter notificationAdapter;
    private List<News> newsPager = new ArrayList<>();

    public NotificationFragment() {
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.notification_fragment, container, false);
        ButterKnife.bind(this, rootView);

        notificationAdapter = new NotificationAdapter(newsPager, NotificationFragment.this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayout.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(notificationAdapter);

        getData();

        return rootView;
    }

    private void getData() {
        HomeHelper.getAnnouncement(new RestCallback<ApiResponse<List<AnnouncementResponse>>>() {
            @Override
            public void onSuccess(Headers headers, ApiResponse<List<AnnouncementResponse>> body) {
                if (body.getData() != null) {
                    List<AnnouncementResponse> responses = body.getData();
                    for (int i = 0; i < responses.size(); i++) {
                        AnnouncementResponse announcementResponse = responses.get(i);
                        newsPager.add(new News(announcementResponse.getId(),
                                announcementResponse.getTitle(),
                                announcementResponse.getDescription(),
                                announcementResponse.getImage()));
                    }
                    notificationAdapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onFailed(ErrorResponse error) {
                Log.d("TAG", error.getMessage());
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
        HomeFragment homeFragment = new HomeFragment();
        ft.setCustomAnimations(R.animator.fade_in, R.animator.fade_out);
        ft.replace(R.id.baseLayout, homeFragment);
        ft.commit();
    }

    public void goDetail(String id) {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = Objects.requireNonNull(fm).beginTransaction();

        Bundle bundle = new Bundle();
        bundle.putString("announcement_id", id);
        bundle.putString("fragment", "notif");

        AnnouncementFragment fragment = new AnnouncementFragment();
        fragment.setArguments(bundle);

        ft.setCustomAnimations(R.animator.fade_in, R.animator.fade_out);
        ft.replace(R.id.baseLayout, fragment);
        ft.commit();
    }
}

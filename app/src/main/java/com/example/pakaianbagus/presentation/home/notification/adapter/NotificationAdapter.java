package com.example.pakaianbagus.presentation.home.notification.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.pakaianbagus.R;
import com.example.pakaianbagus.models.News;
import com.example.pakaianbagus.presentation.home.notification.NotificationFragment;

import java.util.List;

/**
 * Created by alfianhpratama on 18/09/2019.
 * Organization: UTeam
 */
public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {

    private List<News> newsPager;
    private Fragment fragment;

    public NotificationAdapter(List<News> newsPager, Fragment fragment) {
        this.newsPager = newsPager;
        this.fragment = fragment;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.content_item_notification, parent, false);
        return new NotificationAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int i) {
        News news = newsPager.get(i);
        holder.title.setText(news.getTitle());
        holder.date.setText(news.getCreatedAt());
        holder.layout.setOnClickListener(v ->
                ((NotificationFragment) fragment).goDetail(news.getId()));
    }

    @Override
    public int getItemCount() {
        return newsPager.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private LinearLayout layout;
        private TextView title;
        private TextView date;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            layout = itemView.findViewById(R.id.layoutNotif);
            title = itemView.findViewById(R.id.tvTitle);
            date = itemView.findViewById(R.id.tvDate);
        }
    }
}

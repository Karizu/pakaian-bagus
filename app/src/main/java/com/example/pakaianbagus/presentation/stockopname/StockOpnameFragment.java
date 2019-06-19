package com.example.pakaianbagus.presentation.stockopname;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.PopupMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pakaianbagus.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class StockOpnameFragment extends Fragment {

    @BindView(R.id.spinnerStock)
    Spinner stockSpinner;

    View rootView;

    public StockOpnameFragment() {

    }

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.stock_opname_fragment, container, false);
        ButterKnife.bind(this, rootView);

        TextView toolbarTitle = rootView.findViewById(R.id.toolbar_title);
        toolbarTitle.setText("STOCK OPNAME");
        ImageView toolbarHistory = rootView.findViewById(R.id.toolbar_history);
        toolbarHistory.setVisibility(View.GONE);

        settingGenderSpinner();

        return rootView;
    }

    @OnClick(R.id.btnMore)
    public void btnMore() {
        View v = rootView.findViewById(R.id.btnMore);
        PopupMenu pm = new PopupMenu(Objects.requireNonNull(getActivity()), v);
        pm.getMenuInflater().inflate(R.menu.menu_options, pm.getMenu());
        pm.setOnMenuItemClickListener(menuItem -> {
            switch (menuItem.getItemId()) {
                case R.id.navigation_ubah:
                    Toast.makeText(getActivity(), String.valueOf(menuItem.getTitle()), Toast.LENGTH_SHORT).show();
                    break;
            }
            return true;
        });
        pm.show();
    }


    private void settingGenderSpinner() {
        // Spinner Drop down elements
        List<String> filter = new ArrayList<>();
        filter.add("ARTIKEL");
        filter.add("KATEGORI");

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(Objects.requireNonNull(getActivity()), R.layout.spinner_item, filter);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(R.layout.spinner_stock_dropdown_item);

        // attaching data adapter to spinner
        stockSpinner.setAdapter(dataAdapter);
    }
}

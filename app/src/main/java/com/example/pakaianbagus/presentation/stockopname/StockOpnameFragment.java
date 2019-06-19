package com.example.pakaianbagus.presentation.stockopname;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.PopupMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

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
    Dialog dialog;

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
                    showDialog();
                    ImageView imgClose = dialog.findViewById(R.id.imgClose);
                    imgClose.setOnClickListener(v1 -> dialog.dismiss());
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

    private void showDialog() {
        dialog = new Dialog(Objects.requireNonNull(getActivity()));
        //set content
        dialog.setContentView(R.layout.dialog_ubah_qty);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(Objects.requireNonNull(dialog.getWindow()).getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }
}

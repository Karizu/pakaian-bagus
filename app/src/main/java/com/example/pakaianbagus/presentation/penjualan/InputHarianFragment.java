package com.example.pakaianbagus.presentation.penjualan;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pakaianbagus.R;
import com.example.pakaianbagus.api.PenjualanHelper;
import com.example.pakaianbagus.models.ApiResponse;
import com.example.pakaianbagus.models.PenjualanResponse;
import com.example.pakaianbagus.models.SalesReportModel;
import com.example.pakaianbagus.presentation.penjualan.adapter.PenjualanAdapter;
import com.example.pakaianbagus.presentation.penjualan.adapter.SalesReportAdapter;
import com.example.pakaianbagus.util.dialog.Loading;
import com.rezkyatinnov.kyandroid.reztrofit.ErrorResponse;
import com.rezkyatinnov.kyandroid.reztrofit.RestCallback;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Headers;

public class InputHarianFragment extends Fragment {

    View rootView;
    private List<SalesReportModel> salesReportModels;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.tvDate)
    TextView tvDate;

    int limit = 10;
    int offset = 0;
    Dialog dialog;
    Calendar myCalendar;
    EditText startDate;
    EditText endDate;
    String id;

    public InputHarianFragment() {

    }

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.input_harian_fragment, container, false);
        ButterKnife.bind(this, rootView);

        id = Objects.requireNonNull(getArguments()).getString("id");

        salesReportModels = new ArrayList<>();
        getPenjualan();
        getCurrentDateChecklist();

        return rootView;
    }

    private void getPenjualan(){
        Loading.show(getContext());
        PenjualanHelper.getPenjualan(id, limit, offset, new RestCallback<ApiResponse<List<PenjualanResponse>>>() {
            @Override
            public void onSuccess(Headers headers, ApiResponse<List<PenjualanResponse>> body) {
                Loading.hide(getContext());
                if (body.getData() != null){
                    List<PenjualanResponse> res = body.getData();
                    for (int i = 0; i < res.size(); i++){
                        PenjualanResponse penjualanResponse = res.get(i);
                        salesReportModels.add(new SalesReportModel(penjualanResponse.getTransaksi_detail().get(i).getId_transaksi(),
                                penjualanResponse.getTanggal(),
                                penjualanResponse.getTransaksi_detail().get(i).getQty(),
                                penjualanResponse.getTransaksi_detail().get(i).getDiscount(),
                                penjualanResponse.getTransaksi_detail().get(i).getHarga(),
                                penjualanResponse.getTotal_harga()));
                    }

                    SalesReportAdapter adapter = new SalesReportAdapter(salesReportModels, getContext());
                    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),
                            LinearLayout.VERTICAL,
                            false));
                    recyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onFailed(ErrorResponse error) {
                Loading.hide(getContext());
                Log.d("TAG ERROR", error.getMessage());
            }

            @Override
            public void onCanceled() {

            }
        });
    }

    private void getCurrentDateChecklist() {
        Date c = Calendar.getInstance().getTime();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat df = new SimpleDateFormat("yyyy MMMM dd");
        String formattedDate = df.format(c);
        tvDate.setText(formattedDate);
    }

    @OnClick(R.id.toolbar_back)
    public void toolbarBack(){
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = Objects.requireNonNull(fm).beginTransaction();
        PenjualanListTokoFragment penjualanListTokoFragment = new PenjualanListTokoFragment();
        ft.setCustomAnimations(R.animator.fade_in, R.animator.fade_out);
        ft.replace(R.id.baseLayoutInputHarian, penjualanListTokoFragment);
        ft.commit();
    }

    @SuppressLint("SetTextI18n")
    @OnClick(R.id.toolbar_input)
    public void onClickToolbarInput(){
        showDialog();
        myCalendar = Calendar.getInstance();
        startDate = dialog.findViewById(R.id.etDialogStartDate);
        DatePickerDialog.OnDateSetListener date = (view, year, month, dayOfMonth) -> {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, month);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();
        };
        startDate.setOnClickListener(v ->
            new DatePickerDialog(Objects.requireNonNull(getActivity()), date, myCalendar
                    .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                    myCalendar.get(Calendar.DAY_OF_MONTH)).show()
        );
        endDate = dialog.findViewById(R.id.etDialogEndDate);
        DatePickerDialog.OnDateSetListener date2 = (view, year, month, dayOfMonth) -> {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, month);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel2();
        };
        endDate.setOnClickListener(v ->
            new DatePickerDialog(Objects.requireNonNull(getActivity()), date2, myCalendar
                    .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                    myCalendar.get(Calendar.DAY_OF_MONTH)).show()
        );
        Button btnOK = dialog.findViewById(R.id.btnDialogTambah);
        btnOK.setText("OK");
        btnOK.setOnClickListener(v -> {
            if (startDate.getText().toString().length() >= 1 && endDate.getText().toString().length() >= 1) {
                dialog.dismiss();

            } else {
                Snackbar.make(rootView, "Field Tidak Boleh Kosong", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    private void updateLabel() {
        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        startDate.setText(sdf.format(myCalendar.getTime()));
    }

    private void updateLabel2() {
        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        endDate.setText(sdf.format(myCalendar.getTime()));
    }

    @OnClick(R.id.tabPenjualan)
    public void tabPenjualan() {
        Bundle bundle = new Bundle();
        bundle.putString("id", id);
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = Objects.requireNonNull(fm).beginTransaction();
        PenjualanKompetitorFragment penjualanFragment = new PenjualanKompetitorFragment();
        penjualanFragment.setArguments(bundle);
        ft.setCustomAnimations(R.animator.fade_in, R.animator.fade_out);
        ft.replace(R.id.baseLayoutInputHarian, penjualanFragment);
        ft.commit();
    }

    @OnClick(R.id.btnMore)
    public void btnMore() {
        View v = rootView.findViewById(R.id.btnMore);
        PopupMenu pm = new PopupMenu(Objects.requireNonNull(getActivity()), v);
        pm.getMenuInflater().inflate(R.menu.menu_options, pm.getMenu());
        pm.setOnMenuItemClickListener(menuItem -> {
            if (menuItem.getItemId() == R.id.navigation_ubah) {
                Toast.makeText(getActivity(), String.valueOf(menuItem.getTitle()), Toast.LENGTH_SHORT).show();
            }
            return true;
        });
        pm.show();
    }

    private void showDialog() {
        dialog = new Dialog(Objects.requireNonNull(getActivity()));
        //set content
        dialog.setContentView(R.layout.dialog_filter_penjualan);
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

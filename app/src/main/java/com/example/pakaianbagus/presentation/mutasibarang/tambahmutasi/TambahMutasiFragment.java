package com.example.pakaianbagus.presentation.mutasibarang.tambahmutasi;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.pakaianbagus.R;
import com.example.pakaianbagus.models.BrandResponse;
import com.example.pakaianbagus.models.Kunjungan;
import com.example.pakaianbagus.models.MutasiBarang;
import com.example.pakaianbagus.models.MutasiBarangModel;
import com.example.pakaianbagus.presentation.home.HomeFragment;
import com.example.pakaianbagus.presentation.mutasibarang.MutasiBarangFragment;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TambahMutasiFragment extends Fragment {

    private Dialog dialog;
    private List<String> tipeMutsaiList, mutasiIdList;
    private List<String> tokoList, tokoIdList;
    private List<MutasiBarang> barangList;
    private String mutasiId, tokoId;
    private final int REQEUST_CAMERA = 1;
    private File imageCheck;
    private Bitmap photoImage;
    private ImageView imgPhoto;

    @BindView(R.id.spinnerTipeMutasi)
    Spinner spinnerTipeMutasi;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    public TambahMutasiFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.tambah_mutasi_barang_fragment, container, false);
        ButterKnife.bind(this, rootView);

        tipeMutsaiList = new ArrayList<>();
        barangList = new ArrayList<>();
        mutasiIdList = new ArrayList<>();

        getListMutasi();

        return rootView;
    }

    private void getListMutasi() {
        tipeMutsaiList.add("Tipe Mutasi");
        mutasiIdList.add("null");
        for (int i = 0; i < 5; i++) {
            tipeMutsaiList.add("Brand "+i);
            mutasiIdList.add("200995 "+i);
        }

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(Objects.requireNonNull(getActivity()), R.layout.layout_spinner_text, tipeMutsaiList) {
            @Override
            public boolean isEnabled(int position) {
                return position != 0;
            }
        };

        dataAdapter.setDropDownViewResource(R.layout.layout_spinner_dropdown);
        spinnerTipeMutasi.setAdapter(dataAdapter);
        spinnerTipeMutasi.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

//                            setSpinnerNestedKatgori(position);

                if (position != 0) {
                    mutasiId = mutasiIdList.get(position);
                } else {
                    mutasiId = null;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @OnClick(R.id.toolbar_back)
    public void toolbarBack(){
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = Objects.requireNonNull(fm).beginTransaction();
        MutasiBarangFragment mutasiFragment = new MutasiBarangFragment();
        ft.setCustomAnimations(R.animator.fade_in, R.animator.fade_out);
        ft.replace(R.id.baseLayoutTambahMutasi, mutasiFragment);
        ft.commit();
    }


    @OnClick(R.id.btnAddMutasi)
    public void onCLickBtnAddBarang(){
        showDialog();
        EditText nama = dialog.findViewById(R.id.etDialogNamaBarang);
        EditText qty = dialog.findViewById(R.id.etDialogQuantity);
        Button tambah = dialog.findViewById(R.id.btnDialogTambah);
        tambah.setOnClickListener(v -> {
            if (nama.getText().toString().equals("") || qty.getText().toString().equals("")){
                Toast.makeText(getActivity(), "Silahkan isi semua field", Toast.LENGTH_SHORT).show();
            } else {
                String uniqueID = UUID.randomUUID().toString();
                MutasiBarang barang = new MutasiBarang();
                barang.setId(uniqueID);
                barang.setName(nama.getText().toString());
                barang.setQty(qty.getText().toString());
                barangList.add(barang);
                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayout.VERTICAL, false));
                TambahMutasiBarangAdapter adapter = new TambahMutasiBarangAdapter(barangList, getActivity());
                recyclerView.setAdapter(adapter);
                dialog.dismiss();
            }
        });
        ImageView imgClose = dialog.findViewById(R.id.imgClose);
        imgClose.setOnClickListener(v -> dialog.dismiss());
    }

    private void showDialog() {
        dialog = new Dialog(Objects.requireNonNull(getActivity()));
        //set content
        dialog.setContentView(R.layout.dialog_tambah_barang);
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

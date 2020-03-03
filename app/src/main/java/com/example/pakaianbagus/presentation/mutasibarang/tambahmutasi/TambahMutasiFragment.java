package com.example.pakaianbagus.presentation.mutasibarang.tambahmutasi;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
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

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pakaianbagus.R;
import com.example.pakaianbagus.api.MutasiHelper;
import com.example.pakaianbagus.api.SpgHelper;
import com.example.pakaianbagus.models.ApiResponse;
import com.example.pakaianbagus.models.MutasiBarang;
import com.example.pakaianbagus.models.MutationRequest;
import com.example.pakaianbagus.models.api.mutation.Mutation;
import com.example.pakaianbagus.models.api.mutation.detail.Detail;
import com.example.pakaianbagus.models.api.mutation.detail.Expedition;
import com.example.pakaianbagus.models.auth.Group;
import com.example.pakaianbagus.models.auth.Place;
import com.example.pakaianbagus.models.mutation.MutationResponse;
import com.example.pakaianbagus.models.stock.Stock;
import com.example.pakaianbagus.presentation.mutasibarang.MutasiBarangFragment;
import com.example.pakaianbagus.util.Constanta;
import com.example.pakaianbagus.util.DateUtils;
import com.example.pakaianbagus.util.dialog.Loading;
import com.google.gson.Gson;
import com.rezkyatinnov.kyandroid.reztrofit.ErrorResponse;
import com.rezkyatinnov.kyandroid.reztrofit.RestCallback;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Headers;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TambahMutasiFragment extends Fragment {

    private Dialog dialog;
    private List<String> tipeMutsaiList, mutasiIdList;
    private List<String> fromPlaceList, fromPlaceIdList, toPlaceList, toPlaceIdList, stockList, stockIdList, stockQtyList, stockPriceList, stockBrandIdList;
    private List<Detail> barangList;
    private String tipeMutasiId, toPlaceId, fromPlaceId, stockId, stockQty, stockPrice, stockName, brandId, store_id, brand_id, flagMutasi;
    private int mTotalQty, mTotalAmount;
    private final int REQEUST_CAMERA = 1;
    private File imageCheck;
    private Bitmap photoImage;
    private ImageView imgPhoto;
    private int limit = 10;
    private int offset = 0;

    @BindView(R.id.spinnerTipeMutasi)
    Spinner spinnerTipeMutasi;
    @BindView(R.id.spinnerFromPlace)
    Spinner spinnerFromPlace;
    @BindView(R.id.spinnerToPlace)
    Spinner spinnerToPlace;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.etDeskripsi)
    EditText etDeskripsi;

    public TambahMutasiFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tambah_mutasi_barang_fragment, container, false);
        ButterKnife.bind(this, rootView);

        try {
            store_id = Objects.requireNonNull(getArguments()).getString("store_id");
            brand_id = Objects.requireNonNull(getArguments()).getString("brand_id");
            flagMutasi = Objects.requireNonNull(getArguments()).getString(Constanta.FLAG_MUTASI);
            Log.d("TAG", stockId + " " + brandId + " " + flagMutasi);
        } catch (Exception e) {
            e.printStackTrace();
        }

        tipeMutsaiList = new ArrayList<>();
        barangList = new ArrayList<>();
        mutasiIdList = new ArrayList<>();
        fromPlaceList = new ArrayList<>();
        fromPlaceIdList = new ArrayList<>();
        toPlaceList = new ArrayList<>();
        toPlaceIdList = new ArrayList<>();
        stockIdList = new ArrayList<>();
        stockList = new ArrayList<>();
        stockPriceList = new ArrayList<>();
        stockQtyList = new ArrayList<>();
        stockBrandIdList = new ArrayList<>();

        getListExpeditions();
        getListFromPlace();
        getListToPlace();

        return rootView;
    }

    private void createMutation() {
        Loading.show(getContext());

        if (tipeMutasiId == null || fromPlaceId == null || toPlaceId == null || brandId == null ||
                barangList.size() < 1 || etDeskripsi.getText().toString().equals("")) {
            Loading.hide(getContext());
            Toast.makeText(getContext(), "Silahkan lengkapi form isian", Toast.LENGTH_SHORT).show();
        } else {
            MutationRequest mutationRequest = new MutationRequest();
            mutationRequest.setmExpeditionId(Integer.parseInt(tipeMutasiId));
            mutationRequest.setNo(getMutationNumber());
            mutationRequest.setDate(getCurrentDate());
            mutationRequest.setFrom(Integer.parseInt(fromPlaceId));
            mutationRequest.setTo(Integer.parseInt(toPlaceId));
            mutationRequest.setType("D");

            for (int i = 0; i < barangList.size(); i++) {
                Detail detail = barangList.get(i);
                mTotalQty += detail.getQty();
                mTotalAmount += detail.getPrice();
            }

            mutationRequest.setTotalQty(mTotalQty);
            mutationRequest.setTotalPrice(mTotalAmount);
            mutationRequest.setDescription(etDeskripsi.getText().toString());
            mutationRequest.setReceiptNo(getMutationNumber());
            mutationRequest.setReceiptNote(getReceiptNote());
            mutationRequest.setReceiptProof(getReceiptProof());
            mutationRequest.setM_brand_id(Integer.parseInt(brandId));
            mutationRequest.setDetails(barangList);

            MutasiHelper.postMutasi(mutationRequest, new RestCallback<ApiResponse<MutationResponse>>() {
                @Override
                public void onSuccess(Headers headers, ApiResponse<MutationResponse> body) {
                    try {
                        Log.d("VERIFY", "Masuk post mutasi");
                        MutationResponse mutation = body.getData();
                        verifyMutationOne(mutation.getId() + "");
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(getContext(), body.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailed(ErrorResponse error) {
                    Loading.hide(getContext());
                    Toast.makeText(getContext(), "Gagal mengirimkan data mutasi", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onCanceled() {

                }
            });
        }
    }

    private void deleteMutation(String mutasiId, String msg){
        MutasiHelper.deleteMutation(mutasiId, new RestCallback<ApiResponse<MutationResponse>>() {
            @Override
            public void onSuccess(Headers headers, ApiResponse<MutationResponse> body) {
                Loading.hide(getContext());
                Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailed(ErrorResponse error) {
                Loading.hide(getContext());
                error.getMessage();
            }

            @Override
            public void onCanceled() {

            }
        });
    }

    private void verifyMutationOne(String mutasiId) {
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("status", "1")
                .build();

        MutasiHelper.verifyMutation(mutasiId, requestBody, new RestCallback<ApiResponse<MutationResponse>>() {
            @Override
            public void onSuccess(Headers headers, ApiResponse<MutationResponse> body) {
                try {
                    Log.d("VERIFY", "Masuk verify 1");
                    verifyMutationThree(mutasiId);
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getContext(), body.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailed(ErrorResponse error) {
                deleteMutation(mutasiId, error.getMessage());
            }

            @Override
            public void onCanceled() {

            }
        });
    }

    private void verifyMutationThree(String mutasiId) {
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("status", "3")
                .build();

        MutasiHelper.verifyMutation(mutasiId, requestBody, new RestCallback<ApiResponse<MutationResponse>>() {
            @Override
            public void onSuccess(Headers headers, ApiResponse<MutationResponse> body) {
                Loading.hide(getContext());
                try {
                    Log.d("VERIFY", "Masuk verify 3");
                    Toast.makeText(getContext(), "Berhasil Tambah Mutasi", Toast.LENGTH_SHORT).show();

                    Bundle bundle = new Bundle();
                    bundle.putString("store_id", store_id);
                    bundle.putString("brand_id", brand_id);
                    bundle.putString(Constanta.FLAG_MUTASI, flagMutasi);
                    FragmentManager fm = getFragmentManager();
                    FragmentTransaction ft = Objects.requireNonNull(fm).beginTransaction();
                    Fragment fragment = new MutasiBarangFragment();
                    fragment.setArguments(bundle);
                    ft.setCustomAnimations(R.animator.fade_in, R.animator.fade_out);
                    ft.replace(R.id.baseLayoutMutasi, fragment);
                    ft.commit();

                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getContext(), body.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailed(ErrorResponse error) {
                deleteMutation(mutasiId, error.getMessage());
            }

            @Override
            public void onCanceled() {

            }
        });
    }

    private void getListExpeditions() {
        Loading.show(getContext());
        MutasiHelper.getListExpeditions(new RestCallback<ApiResponse<List<Expedition>>>() {
            @Override
            public void onSuccess(Headers headers, ApiResponse<List<Expedition>> body) {
                Loading.hide(getContext());
                try {
                    List<Expedition> res = body.getData();
                    tipeMutsaiList.add("Pilih Expedisi");
                    mutasiIdList.add("null");
                    for (int i = 0; i < res.size(); i++) {
                        Expedition expedition = res.get(i);
                        tipeMutsaiList.add(expedition.getName());
                        mutasiIdList.add(expedition.getId() + "");
                    }

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
                            if (position != 0) {
                                tipeMutasiId = mutasiIdList.get(position);
                            } else {
                                tipeMutasiId = null;
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailed(ErrorResponse error) {
                Loading.hide(getContext());
                error.getMessage();
            }

            @Override
            public void onCanceled() {

            }
        });
    }

    public void getListFromPlace() {
        Loading.show(getActivity());
        SpgHelper.getListPlace(new RestCallback<ApiResponse<List<Place>>>() {
            @Override
            public void onSuccess(Headers headers, ApiResponse<List<Place>> listApiResponse) {
                Loading.hide(getActivity());
                try {
                    List<Place> res = listApiResponse.getData();
                    fromPlaceList.add("Pilih Toko Asal");
                    fromPlaceIdList.add("null");
                    for (int i = 0; i < res.size(); i++) {
                        Place response = res.get(i);
                        fromPlaceList.add(response.getName());
                        fromPlaceIdList.add(response.getId() + "");
                    }

                    // Creating adapter for spinner
                    ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(Objects.requireNonNull(getActivity()), R.layout.layout_spinner_text, fromPlaceList) {
                        @Override
                        public boolean isEnabled(int position) {
                            return position != 0;
                        }
                    };

                    dataAdapter.setDropDownViewResource(R.layout.layout_spinner_dropdown);
                    spinnerFromPlace.setAdapter(dataAdapter);
                    spinnerFromPlace.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

//                            setSpinnerNestedKatgori(position);

                            if (position != 0) {
                                fromPlaceId = fromPlaceIdList.get(position);
                            } else {
                                fromPlaceId = null;
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });

                } catch (Exception e) {
                    Log.d("TAG EXCEPTION", Objects.requireNonNull(e.getMessage()));
                }
            }

            @Override
            public void onFailed(ErrorResponse errorResponse) {
                Loading.hide(getContext());
                Log.d("TAG onFialed", errorResponse.getMessage());
            }

            @Override
            public void onCanceled() {

            }
        });
    }

    public void getListToPlace() {
        Loading.show(getActivity());
        SpgHelper.getListPlace(new RestCallback<ApiResponse<List<Place>>>() {
            @Override
            public void onSuccess(Headers headers, ApiResponse<List<Place>> listApiResponse) {
                Loading.hide(getActivity());
                try {
                    List<Place> res = listApiResponse.getData();
                    toPlaceList.add("Pilih Toko Tujuan");
                    toPlaceIdList.add("null");
                    for (int i = 0; i < res.size(); i++) {
                        Place response = res.get(i);
                        toPlaceList.add(response.getName());
                        toPlaceIdList.add(response.getId() + "");
                    }

                    // Creating adapter for spinner
                    ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(Objects.requireNonNull(getActivity()), R.layout.layout_spinner_text, toPlaceList) {
                        @Override
                        public boolean isEnabled(int position) {
                            return position != 0;
                        }
                    };

                    dataAdapter.setDropDownViewResource(R.layout.layout_spinner_dropdown);
                    spinnerToPlace.setAdapter(dataAdapter);
                    spinnerToPlace.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

//                            setSpinnerNestedKatgori(position);

                            if (position != 0) {
                                toPlaceId = toPlaceIdList.get(position);
                            } else {
                                toPlaceId = null;
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });

                } catch (Exception e) {
                    Log.d("TAG EXCEPTION", Objects.requireNonNull(e.getMessage()));
                }
            }

            @Override
            public void onFailed(ErrorResponse errorResponse) {
                Loading.hide(getContext());
                Log.d("TAG onFialed", errorResponse.getMessage());
            }

            @Override
            public void onCanceled() {

            }
        });
    }

    @OnClick(R.id.toolbar_back)
    public void toolbarBack() {
        Bundle bundle = new Bundle();
        bundle.putString("store_id", store_id);
        bundle.putString("brand_id", brand_id);
        bundle.putString(Constanta.FLAG_MUTASI, flagMutasi);
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = Objects.requireNonNull(fm).beginTransaction();
        MutasiBarangFragment mutasiFragment = new MutasiBarangFragment();
        mutasiFragment.setArguments(bundle);
        ft.setCustomAnimations(R.animator.fade_in, R.animator.fade_out);
        ft.replace(R.id.baseLayoutTambahMutasi, mutasiFragment);
        ft.commit();
    }

    @OnClick(R.id.btnTambah)
    void onClickTambah() {
        createMutation();
    }

    @SuppressLint("WrongConstant")
    @OnClick(R.id.btnAddMutasi)
    void onCLickBtnAddBarang() {
        showDialog();
        Spinner spinnerStock = dialog.findViewById(R.id.spinnerStock);
        getStockByPlace(spinnerStock);
        EditText qty = dialog.findViewById(R.id.etDialogQuantity);
        Button tambah = dialog.findViewById(R.id.btnDialogTambah);
        tambah.setOnClickListener(v -> {
            if (stockId == null || qty.getText().toString().equals("")) {
                Toast.makeText(getActivity(), "Silahkan isi semua field", Toast.LENGTH_SHORT).show();
            } else {

                Detail detail = new Detail();
                detail.setStockId(Integer.parseInt(stockId));
                detail.setPrice(Integer.parseInt(stockPrice));
                detail.setQty(Integer.parseInt(qty.getText().toString()));
                detail.setCreatedAt(stockName);
                barangList.add(detail);

                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayout.VERTICAL, false));
                TambahMutasiBarangAdapter adapter = new TambahMutasiBarangAdapter(barangList, getActivity(), TambahMutasiFragment.this);
                recyclerView.setAdapter(adapter);

                dialog.dismiss();
            }
        });
        ImageView imgClose = dialog.findViewById(R.id.imgClose);
        imgClose.setOnClickListener(v -> dialog.dismiss());
    }

    private void getStockByPlace(Spinner spinnerStock) {
        Loading.show(getContext());
        MutasiHelper.getListStock(fromPlaceId, new Callback<ApiResponse<List<Stock>>>() {
            @SuppressLint("WrongConstant")
            @Override
            public void onResponse(Call<ApiResponse<List<Stock>>> call, Response<ApiResponse<List<Stock>>> response) {
                Loading.hide(getContext());
                try {
                    List<Stock> res = Objects.requireNonNull(response.body()).getData();
                    stockList.add("Pilih Item");
                    stockIdList.add("null");
                    stockQtyList.add("null");
                    stockPriceList.add("null");
                    stockBrandIdList.add("null");
                    for (int i = 0; i < res.size(); i++) {
                        Stock stock = res.get(i);
                        stockList.add(stock.getItem().getName());
                        stockIdList.add(stock.getId() + "");
                        stockPriceList.add(stock.getPrice() + "");
                        stockQtyList.add(stock.getQty() + "");
                        stockBrandIdList.add(stock.getItem().getBrand().getId() + "");
                    }

                    // Creating adapter for spinner
                    ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(Objects.requireNonNull(getActivity()), R.layout.layout_spinner_text, stockList) {
                        @Override
                        public boolean isEnabled(int position) {
                            return position != 0;
                        }
                    };

                    dataAdapter.setDropDownViewResource(R.layout.layout_spinner_dropdown);
                    spinnerStock.setAdapter(dataAdapter);
                    spinnerStock.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

//                            setSpinnerNestedKatgori(position);

                            if (position != 0) {
                                stockId = stockIdList.get(position);
                                stockName = stockList.get(position);
                                stockQty = stockQtyList.get(position);
                                stockPrice = stockPriceList.get(position);
                                brandId = stockBrandIdList.get(position);
                            } else {
                                stockId = null;
                                stockName = null;
                                stockQty = null;
                                stockPrice = null;
                                brandId = null;
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getContext(), response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<Stock>>> call, Throwable t) {
                Loading.hide(getContext());
                t.printStackTrace();
            }
        });
    }

    private static String getMutationNumber() {
        Date c = Calendar.getInstance().getTime();
        return "D" + new DateUtils().formatDateToString(c, "yyyyMMdd") + "AA/" + new DateUtils().formatDateToString(c, "mmss");
    }

    private static String getReceiptNote() {
        Date c = Calendar.getInstance().getTime();
        return "BO" + new DateUtils().formatDateToString(c, "mmss");
    }

    private static String getReceiptProof() {
        Date c = Calendar.getInstance().getTime();
        return "BLO" + new DateUtils().formatDateToString(c, "mmss");
    }

    private static String getCurrentDate() {
        Date c = Calendar.getInstance().getTime();
        return new DateUtils().formatDateToString(c, "yyyy-MM-dd");
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

    public void updateData(List<Detail> newList) {
        barangList = new ArrayList<>();
        barangList.addAll(newList);
        Gson gson = new Gson();
        String json = gson.toJson(barangList);
        Log.d("MUTASI", json);
    }

}

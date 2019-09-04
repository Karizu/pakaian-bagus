package com.example.pakaianbagus.presentation.home.inputpenjualan;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.pakaianbagus.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class SalesReportFragment extends Fragment {


    public SalesReportFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.input_penjualan_sales_report_fragment, container, false);
    }

}

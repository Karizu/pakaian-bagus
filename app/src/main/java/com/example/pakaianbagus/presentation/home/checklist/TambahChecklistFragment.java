package com.example.pakaianbagus.presentation.home.checklist;

import android.annotation.SuppressLint;
import android.app.Dialog;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.pakaianbagus.R;
import com.example.pakaianbagus.models.Checklist;
import com.example.pakaianbagus.models.RoleChecklist;
import com.example.pakaianbagus.presentation.home.HomeFragment;
import com.example.pakaianbagus.presentation.home.checklist.adapter.TambahChecklistAdapter;
import com.example.pakaianbagus.util.IOnBackPressed;
import com.example.pakaianbagus.util.SessionChecklist;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TambahChecklistFragment extends Fragment implements IOnBackPressed {


    private List<RoleChecklist> checklists;
    private Dialog dialog;
    private TambahChecklistAdapter checklistAdapter;
    private SessionChecklist sessionChecklist;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.tvNoData)
    TextView tvNoData;

    public TambahChecklistFragment() {
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.tambah_checklist_fragment, container, false);
        ButterKnife.bind(this, rootView);

        checklists = new ArrayList<>();
        sessionChecklist = new SessionChecklist(Objects.requireNonNull(getActivity()));

        return rootView;
    }

    @OnClick(R.id.btnAddChecklist)
    public void tambahChecklist2() {
        showDialog();
        EditText etChecklist = dialog.findViewById(R.id.etDialogChecklist);
        ImageView btnClose = dialog.findViewById(R.id.imgClose);
        btnClose.setOnClickListener(v -> dialog.dismiss());
        Button btnTambah = dialog.findViewById(R.id.btnDialogTambah);
        btnTambah.setOnClickListener(v -> {
            if (etChecklist.getText().length() < 1) {
                etChecklist.setError("Field ini harus diisi");
            } else {

                String uniqueID = UUID.randomUUID().toString();
                @SuppressLint("SimpleDateFormat") SimpleDateFormat s1 = new SimpleDateFormat("yyyy-MM-dd");
                String date = s1.format(new Date());
                RoleChecklist myObject = new RoleChecklist();
                myObject.setId(uniqueID);
                myObject.setChecklist_id(null);
                myObject.setCreated_at(date);
                Checklist checklist = new Checklist();
                checklist.setId(uniqueID);
                checklist.setName(etChecklist.getText().toString());
                myObject.setChecklist(checklist);
                checklists.add(myObject);

                checklistAdapter = new TambahChecklistAdapter(checklists, getActivity(), 0);
                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),
                        LinearLayout.VERTICAL,
                        false));
                recyclerView.setAdapter(checklistAdapter);
                tvNoData.setVisibility(View.GONE);
                dialog.dismiss();
            }
        });
    }

    @OnClick(R.id.btnOK)
    public void btnOK() {

        if (sessionChecklist.getArrayListChecklist() != null) {
            JSONArray jsonArray;
            try {
                jsonArray = new JSONArray(sessionChecklist.getArrayListChecklist());
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    JSONObject jsonObjeckChecklist = jsonObject.getJSONObject("checklist");

//                        if (!String.valueOf(jsonObject.get("created_at")).equals(date)) {
//                            sessionChecklist.logoutUser();
//                            getActivity().finish();
//                            startActivity(getActivity().getIntent());
//                        }

                    RoleChecklist myObject1 = new RoleChecklist();
                    myObject1.setId(String.valueOf(jsonObject.get("id")));

                    Checklist checklist = new Checklist();
                    checklist.setId(String.valueOf(jsonObjeckChecklist.get("id")));
                    checklist.setName(String.valueOf(jsonObjeckChecklist.get("name")));
                    myObject1.setChecklist(checklist);

                    checklists.add(myObject1);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        sessionChecklist.setArraylistChecklist(checklists);
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = Objects.requireNonNull(fm).beginTransaction();
        HomeFragment homeFragment = new HomeFragment();
        ft.setCustomAnimations(R.animator.fade_in, R.animator.fade_out);
        ft.replace(R.id.baseLayoutTambahChecklist, homeFragment);
        ft.commit();
    }

    private void showDialog() {
        dialog = new Dialog(Objects.requireNonNull(getActivity()));
        //set content
        dialog.setContentView(R.layout.dialog_tambah_checklist);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(Objects.requireNonNull(dialog.getWindow()).getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }

    @Override
    public boolean onBackPressed() {
        return false;
    }
}

package com.example.pakaianbagus.presentation.home.checklist.adapter;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pakaianbagus.R;
import com.example.pakaianbagus.models.RoleChecklist;
import com.example.pakaianbagus.models.RoleChecklistModel;
import com.example.pakaianbagus.util.SessionManagement;

import java.util.List;
import java.util.Objects;

public class TambahChecklistAdapter extends RecyclerView.Adapter<TambahChecklistAdapter.ViewHolder> {
    private List<RoleChecklistModel> roleChecklistModels;
    private List<RoleChecklist> roleChecklists;
    private Context context;
    private String checklist = "";
    private Dialog dialog;
    private SparseBooleanArray itemStateArray = new SparseBooleanArray();

    public TambahChecklistAdapter(List<RoleChecklistModel> roleChecklistModels, Context context) {
        this.roleChecklistModels = roleChecklistModels;
        this.context = context;
    }

    public TambahChecklistAdapter(List<RoleChecklist> roleChecklists, Context context, int i) {
        this.roleChecklists = roleChecklists;
        this.context = context;
    }

    @NonNull
    @Override
    public TambahChecklistAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.content_item_tambah_checklist, parent, false);

        return new TambahChecklistAdapter.ViewHolder(v);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull TambahChecklistAdapter.ViewHolder holder, int position) {
//        if (data != null){
//            SessionManagement session = new SessionManagement(Objects.requireNonNull(context));
//            String name = null, checklistId = null;
//            try {
//                String str = "{\"data\":"+data+"}";
//                JSONArray checklistData = new JSONObject(str).getJSONArray("data");
//                Log.d("Length", String.valueOf(checklistData.length()));
//                for (int i = 0; i < checklistData.length(); i++){
//                    JSONObject text = checklistData.getJSONObject(position);
//                    name = text.getJSONObject("checklist").getString("name");
//                    checklistId = text.getString("checklist_id");
//                }
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//            holder.textViewName.setText(name);
//
//            if (session.getChecklist(checklistId) == 0) {
//                holder.checkBox.setChecked(false);
//            } else if (session.getChecklist(checklistId) == 1) {
//                holder.checkBox.setChecked(true);
//            } else {
//                holder.checkBox.setChecked(true);
//            }
//
//            String finalChecklistId = checklistId;
//            holder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
//                if (isChecked) {
//                    session.setChecklist(finalChecklistId, 1);
//                } else {
//                    session.setChecklist(finalChecklistId, 0);
//                }
//            });
//        }

        if (roleChecklists != null) {
            final RoleChecklist roleChecklistModel = roleChecklists.get(position);
            SessionManagement session = new SessionManagement(Objects.requireNonNull(context));
            final String id = roleChecklistModel.getId();
//            final String checklistId = roleChecklistModel.getChecklist_id();
            String name = "Lorem Ipsum Lorem Ipsum";
            if (roleChecklistModel.getChecklist().getName() != null) {
                name = roleChecklistModel.getChecklist().getName();
            } else {
                name = "Lorem Ipsum Lorem Ipsum";
            }

            holder.textViewName.setText(name);

        }

        if (roleChecklistModels != null) {
            final RoleChecklistModel roleChecklistModel = roleChecklistModels.get(position);
            SessionManagement session = new SessionManagement(Objects.requireNonNull(context));
            final String id = roleChecklistModel.getId();
            final String checklistId = roleChecklistModel.getChecklist_id();
            String name = "Lorem Ipsum Lorem Ipsum";
            if (roleChecklistModel.getName() != null) {
                name = roleChecklistModel.getName();
            } else if (roleChecklistModel.getChecklist() != null && roleChecklistModel.getName() == null) {
                name = roleChecklistModel.getChecklist().getName();
            } else {
                name = "Lorem Ipsum Lorem Ipsum";
            }

            holder.textViewName.setText(name);
        }
    }

    @Override
    public int getItemCount() {
        if (roleChecklistModels != null) {
            return roleChecklistModels.size();
        } else {
//            String str = "{\"data\":"+data+"}";
//            JSONArray checklistData = null;
//            try {
//                checklistData = new JSONObject(str).getJSONArray("data");
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//            return Objects.requireNonNull(checklistData).length();
            return roleChecklists.size();
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewName;
        LinearLayout cvChecklist;

        ViewHolder(View v) {
            super(v);
            this.setIsRecyclable(false);
            textViewName = v.findViewById(R.id.etCheckIn);
            cvChecklist = v.findViewById(R.id.cvChecklist);
        }
    }
}

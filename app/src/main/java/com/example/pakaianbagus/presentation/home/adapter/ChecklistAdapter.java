package com.example.pakaianbagus.presentation.home.adapter;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.pakaianbagus.R;
import com.example.pakaianbagus.models.Checklist;
import com.example.pakaianbagus.models.ChecklistResponse;
import com.example.pakaianbagus.models.RoleChecklist;
import com.example.pakaianbagus.models.RoleChecklistModel;
import com.example.pakaianbagus.presentation.home.HomeFragment;
import com.example.pakaianbagus.util.SessionManagement;

import java.util.List;
import java.util.Objects;

public class ChecklistAdapter extends RecyclerView.Adapter<ChecklistAdapter.ViewHolder> {
    private List<RoleChecklistModel> roleChecklistModels;
    private List<RoleChecklist> roleChecklists;
    private List<ChecklistResponse> checklistResponses;
    private List<Checklist> checklistList;
    private Context context;
    private String checklist = "";
    private Fragment fragment;
    private Dialog dialog;
    private SparseBooleanArray itemStateArray = new SparseBooleanArray();

    public ChecklistAdapter(List<RoleChecklistModel> roleChecklistModels, Context context) {
        this.roleChecklistModels = roleChecklistModels;
        this.context = context;
    }

    public ChecklistAdapter(List<RoleChecklist> roleChecklists, Context context, int i) {
        this.roleChecklists = roleChecklists;
        this.context = context;
    }

    public ChecklistAdapter(List<RoleChecklist> roleChecklists, Context context, int i, Fragment fragment) {
        this.roleChecklists = roleChecklists;
        this.context = context;
        this.fragment = fragment;
    }

    public ChecklistAdapter(List<Checklist> checklistList, Context context, Fragment fragment) {
        this.checklistList = checklistList;
        this.context = context;
        this.fragment = fragment;
    }

    @NonNull
    @Override
    public ChecklistAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.content_item_checklist, parent, false);

        return new ChecklistAdapter.ViewHolder(v);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ChecklistAdapter.ViewHolder holder, int position) {
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

        if (checklistList != null) {
            if (checklistList.get(position).getName() != null) {
                holder.textViewName.setText(checklistList.get(position).getName());
            }
            holder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    checklistList.get(position).setChecked(true);
                    checklist += checklistList.get(position).getName() + ", ";
                } else {
                    checklistList.get(position).setChecked(false);
                    checklist = checklist.replaceAll(checklistList.get(position).getName() + ", ", "");
                }
                Log.d("LIST CHECKLIST", checklist);
                ((HomeFragment) fragment).setChecklistItem(checklist);
            });
            if (checklistList.get(position).isChecked()) {
                holder.checkBox.setChecked(true);
                holder.checkBox.setEnabled(false);
            }

            int sum =0;

        } else if (roleChecklists != null) {
            final RoleChecklist roleChecklistModel = roleChecklists.get(position);
            SessionManagement session = new SessionManagement(Objects.requireNonNull(context));
            final String id = roleChecklistModel.getId();
//            final String checklistId = roleChecklistModel.getChecklist_id();
            String name;
            if (roleChecklistModel.getChecklist().getName() != null) {
                name = roleChecklistModel.getChecklist().getName();
            } else {
                name = "Lorem Ipsum Lorem Ipsum";
            }

            holder.textViewName.setText(name);

            if (session.getChecklist(id) == 0) {
                holder.checkBox.setChecked(false);
            } else if (session.getChecklist(id) == 1) {
                holder.checkBox.setChecked(true);
            } else {
                holder.checkBox.setChecked(true);
            }

            holder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    session.setChecklist(id, 1);
                    checklist += roleChecklistModel.getChecklist().getName() + ", ";
                } else {
                    session.setChecklist(id, 0);
                    checklist = checklist.replaceAll(roleChecklistModel.getChecklist().getName() + ", ", "");
                }
                ((HomeFragment) fragment).setChecklistItem(checklist);
            });

            holder.cvChecklist.setOnClickListener(v -> {
                /*if (!checklist.equals("")) {
                    int i = checklist.length() - 3;
                    int n = checklist.length();
                    checklist = checklist.substring(i, n);
                    Log.d("TAG i", i+ " " + checklist);
                }
                Toast.makeText(context, checklist, Toast.LENGTH_SHORT).show();*/
            });
        } else if (roleChecklistModels != null) {
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

            if (session.getChecklist(checklistId) == 0) {
                holder.checkBox.setChecked(false);
            } else if (session.getChecklist(checklistId) == 1) {
                holder.checkBox.setChecked(true);
            } else {
                holder.checkBox.setChecked(true);
            }
        }
    }

    @Override
    public int getItemCount() {
        if (checklistList != null) {
            return checklistList.size();
        } else if (roleChecklistModels != null) {
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
        CheckBox checkBox;
        CardView cvChecklist;

        ViewHolder(View v) {
            super(v);
            this.setIsRecyclable(false);
            textViewName = v.findViewById(R.id.tvNameChecklist);
            checkBox = v.findViewById(R.id.checkBox);
            cvChecklist = v.findViewById(R.id.cvChecklist);
        }
    }
}

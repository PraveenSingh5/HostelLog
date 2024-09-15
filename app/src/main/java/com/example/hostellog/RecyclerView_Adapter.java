package com.example.hostellog;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RecyclerView_Adapter extends RecyclerView.Adapter<Recycler_View_Holder> {

    Context context;
    List<Card_modal_item> items;
    public RecyclerView_Adapter(Context context, List<Card_modal_item> items) {
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public Recycler_View_Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Recycler_View_Holder(LayoutInflater.from(context).inflate(R.layout.custom_listview,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull Recycler_View_Holder holder, @SuppressLint("RecyclerView") int position) {
        holder. college_id_item.setText(items.get(position).getC_email());
        holder. room_id_item.setText(items.get(position).getC_room());
        holder. complain_id_item.setText(items.get(position).getC_complain());
        holder. complain_type_id_item.setText(items.get(position).getC_complain_type());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }





}

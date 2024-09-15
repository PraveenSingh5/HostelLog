package com.example.hostellog;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

public class Recycler_View_Holder extends RecyclerView.ViewHolder{

    TextView college_id_item,room_id_item,complain_id_item,complain_type_id_item;
    ConstraintLayout constraintLayout;

    public Recycler_View_Holder(@NonNull View itemView) {
        super(itemView);
        college_id_item = itemView.findViewById(R.id.college_id_item);
        room_id_item = itemView.findViewById(R.id.room_id_item);
        complain_id_item = itemView.findViewById(R.id.complain_id_item);
        complain_type_id_item = itemView.findViewById(R.id.complain_type_id_item);
        constraintLayout = itemView.findViewById(R.id.card_layout);
    }


}

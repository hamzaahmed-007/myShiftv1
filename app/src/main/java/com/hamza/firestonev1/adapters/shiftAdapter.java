package com.hamza.firestonev1.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.hamza.firestonev1.R;
import com.hamza.firestonev1.activities.ShiftConfirm;
import com.hamza.firestonev1.models.Shifts;

public class shiftAdapter extends FirestoreRecyclerAdapter<Shifts, shiftAdapter.shiftHolder> {
 Context mContext;
String ShiftID;
    public shiftAdapter(@NonNull FirestoreRecyclerOptions<Shifts> options, Context mContext) {
        super(options);
            this.mContext = mContext;
    }


    @NonNull
    @Override
    public shiftHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.home_items,viewGroup,false);

        return new shiftHolder(v);
    }

    @Override
    protected void onBindViewHolder(@NonNull shiftHolder holder, int position, @NonNull Shifts model) {
        holder.title.setText(model.getName());
        holder.date.setText(model.getDate());
        holder.id.setText(model.getId());
        holder.shiftID.setText(model.getShiftID());
    }

    class shiftHolder extends RecyclerView.ViewHolder
    {
        TextView title, date, id, shiftID;


        public shiftHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.homeText1);
            date = itemView.findViewById(R.id.homeText2);
            id = itemView.findViewById(R.id.homeText3);
            shiftID = itemView.findViewById(R.id.shiftShiftID);
            shiftID.setVisibility(View.INVISIBLE);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, ShiftConfirm.class);


                    intent.putExtra("ShiftID",shiftID.getText().toString());


                    mContext.startActivity(intent);

                }
            });



        }
    }
}

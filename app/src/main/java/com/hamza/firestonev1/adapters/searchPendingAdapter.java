package com.hamza.firestonev1.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.hamza.firestonev1.R;
import com.hamza.firestonev1.activities.ShiftConfirm;

import com.hamza.firestonev1.models.pendingShifts;

public class searchPendingAdapter extends FirestoreRecyclerAdapter<pendingShifts, searchPendingAdapter.shiftHolder> {
 Context mContext;

    public searchPendingAdapter(@NonNull FirestoreRecyclerOptions<pendingShifts> options, Context mContext ) {
        super(options);
        this.mContext = mContext;

    }


    @NonNull
    @Override
    public shiftHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.pendingitems,viewGroup,false);

        return new shiftHolder(v);
    }

    @Override
    protected void onBindViewHolder(@NonNull shiftHolder holder, int position, @NonNull pendingShifts model) {
        holder.title.setText(model.getShiftName());
        holder.location.setText(model.getLocation());
        holder.date.setText(model.getDate());
        holder.id.setText(model.getUserID());

        holder.id.setVisibility(View.INVISIBLE);

        holder.status.setText(model.getStatus());
    }

    class shiftHolder extends RecyclerView.ViewHolder
    {
        TextView title,location, date, id, status;
        ImageView delete;


        public shiftHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.pendingText1);
            location = itemView.findViewById(R.id.pendingText2);
            date = itemView.findViewById(R.id.pendingText3);
            id = itemView.findViewById(R.id.pendingID);
            status = itemView.findViewById(R.id.pendingstatus);
            delete = itemView.findViewById(R.id.pendingDelete);

            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getSnapshots().getSnapshot(getAdapterPosition()).getReference().delete();
                }
            });


        }
    }
}

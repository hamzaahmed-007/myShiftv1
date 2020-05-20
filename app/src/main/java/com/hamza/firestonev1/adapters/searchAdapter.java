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
import com.hamza.firestonev1.models.Shifts;

public class searchAdapter extends FirestoreRecyclerAdapter<Shifts, searchAdapter.shiftHolder> {
 Context mContext;

    public searchAdapter(@NonNull FirestoreRecyclerOptions<Shifts> options, Context mContext ) {
        super(options);
        this.mContext = mContext;

    }


    @NonNull
    @Override
    public shiftHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.example_item,viewGroup,false);

        return new shiftHolder(v);
    }

    @Override
    protected void onBindViewHolder(@NonNull shiftHolder holder, int position, @NonNull Shifts model) {
        holder.title.setText(model.getName());
        holder.location.setText(model.getLocation());
        holder.date.setText(model.getDate());
        holder.id.setText(model.getId());

       // holder.id.setVisibility(View.INVISIBLE);
    }

    class shiftHolder extends RecyclerView.ViewHolder
    {
        TextView title,location, date, id;
        ImageView delete;


        public shiftHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.searchText1);
            location = itemView.findViewById(R.id.searchText2);
            date = itemView.findViewById(R.id.searchText3);
            id = itemView.findViewById(R.id.exampleitemID);
            id.setVisibility(View.INVISIBLE);
            delete = itemView.findViewById(R.id.exampleDelete);

            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getSnapshots().getSnapshot(getAdapterPosition()).getReference().delete();


                }
            });


        }
    }
}

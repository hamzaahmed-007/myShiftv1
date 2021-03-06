package com.hamza.firestonev1.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hamza.firestonev1.R;
import com.hamza.firestonev1.models.Shifts;

import java.util.ArrayList;

public class homeAdapter extends RecyclerView.Adapter<homeAdapter.myViewHolder> {

    private ArrayList<Shifts> mShiftList;
    private onItemClickListener mListener;
    public interface onItemClickListener
    {

        void onItemClick(int position);

    }

    public void setOnItemClickListener(onItemClickListener listener)
    {

        mListener = listener;


    }

    public static class myViewHolder extends RecyclerView.ViewHolder
    {

        public TextView mtextView1, mtextView2;

        public myViewHolder(@NonNull View itemView, final onItemClickListener listener) {
            super(itemView);
            mtextView1 = itemView.findViewById(R.id.homeText1);
            mtextView2 = itemView.findViewById(R.id.homeText2);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener != null)
                    {
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION)
                        {
                            listener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }

    public homeAdapter(ArrayList<Shifts> myShifts)
    {

        mShiftList = myShifts;
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_items, parent,false);
        myViewHolder vh = new myViewHolder(v, mListener);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder myViewHolder, int position) {

        Shifts currentShift = mShiftList.get(position);
        myViewHolder.mtextView1.setText(currentShift.getName());
        myViewHolder.mtextView2.setText(currentShift.getDate());



    }

    @Override
    public int getItemCount() {
        return mShiftList.size();
    }
}

package com.hamza.firestonev1.activities;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.hamza.firestonev1.R;
import com.hamza.firestonev1.models.Shifts;

import java.util.List;

public class myAdapter extends RecyclerView.Adapter<myAdapter.myViewHolder> {

    Context mContext;
    List<Shifts> mList;

    FirebaseUser mUser;
    FirebaseAuth mAuth;


    public myAdapter(Context mContext, List<Shifts> mList) {
        this.mContext = mContext;
        this.mList = mList;

        mAuth = FirebaseAuth.getInstance();
        mUser  = mAuth.getCurrentUser();
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View layout;
        layout = LayoutInflater.from(mContext).inflate(R.layout.home_items,viewGroup,false);

        return new myViewHolder(layout);
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder myViewHolder, int i)
    {

        myViewHolder.card_title.setText(mList.get(i).getName());
        myViewHolder.card_content.setText(mList.get(i).getDate());




    }

    @Override
    public int getItemCount()
    {
        return mList.size();

    }

    public class myViewHolder extends RecyclerView.ViewHolder
    {

        TextView card_title, card_content;


        public myViewHolder(@NonNull View itemView) {
            super(itemView);

            card_title = itemView.findViewById(R.id.homeText1);
            card_content = itemView.findViewById(R.id.homeText2);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, ShiftConfirm.class);
                    int position = getAdapterPosition();

                    intent.putExtra("title", mList.get(position).getName());
                    intent.putExtra("content" ,mList.get(position).getDate());
                    intent.putExtra("ShiftID", mList.get(position).getId());
                    mContext.startActivity(intent);
                }
            });


        }

    }
    private void showMessage(String message)
    {

        Toast.makeText(mContext, message, Toast.LENGTH_LONG).show();
    }


}

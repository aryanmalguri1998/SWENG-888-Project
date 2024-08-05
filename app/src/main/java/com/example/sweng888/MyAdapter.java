package com.example.sweng888;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {

    private Context context;
    private List<DataClass> dataList;

    public MyAdapter(Context context, List<DataClass> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        DataClass dataClass = dataList.get(position);

        Glide.with(context).load(dataClass.getDataImage()).into(holder.recImage);
        holder.recTitle.setText(dataClass.getDataTitle());
        holder.recDesc.setText(dataClass.getDataDesc());
        holder.recLang.setText(dataClass.getDataLang());

        holder.likeCount.setText(String.valueOf(dataClass.getLikeCount()));
        holder.commentCount.setText(String.valueOf(dataClass.getCommentCount()));

        holder.recCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, DetailActivity.class);
                intent.putExtra("Image", dataClass.getDataImage());
                intent.putExtra("Description", dataClass.getDataDesc());
                intent.putExtra("Title", dataClass.getDataTitle());
                intent.putExtra("Key", dataClass.getKey());
                intent.putExtra("Language", dataClass.getDataLang());
                context.startActivity(intent);
            }
        });

        holder.likeIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Android Uploads").child(dataClass.getKey());
                dataClass.setLikeCount(dataClass.getLikeCount() + 1);
                reference.child("likeCount").setValue(dataClass.getLikeCount());
                notifyDataSetChanged();
            }
        });

        holder.commentIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, CommentActivity.class);
                intent.putExtra("Key", dataClass.getKey());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public void searchDataList(ArrayList<DataClass> searchList){
        dataList = searchList;
        notifyDataSetChanged();
    }
}

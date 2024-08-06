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

    // Constructor to initialize the context and the list of data
    public MyAdapter(Context context, List<DataClass> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the layout for each item in the RecyclerView
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        // Get the current data item from the list
        DataClass dataClass = dataList.get(position);

        // Use Glide to load the image into the ImageView
        Glide.with(context).load(dataClass.getDataImage()).into(holder.recImage);
        
        // Set the text for the title, description, and language
        holder.recTitle.setText(dataClass.getDataTitle());
        holder.recDesc.setText(dataClass.getDataDesc());
        holder.recLang.setText(dataClass.getDataLang());

        // Set the text for like and comment counts
        holder.likeCount.setText(String.valueOf(dataClass.getLikeCount()));
        holder.commentCount.setText(String.valueOf(dataClass.getCommentCount()));

        // Set an onClickListener for the card to open DetailActivity with the data
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

        // Set an onClickListener for the like icon to increment the like count
        holder.likeIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Reference to the specific item in the Firebase database
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Android Uploads").child(dataClass.getKey());
                
                // Increment the like count
                dataClass.setLikeCount(dataClass.getLikeCount() + 1);
                
                // Update the like count in the database
                reference.child("likeCount").setValue(dataClass.getLikeCount());
                
                // Notify the adapter to refresh the UI
                notifyDataSetChanged();
            }
        });

        // Set an onClickListener for the comment icon to open CommentActivity
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
        // Return the total number of items in the list
        return dataList.size();
    }

    // Method to update the list with the search results and notify the adapter
    public void searchDataList(ArrayList<DataClass> searchList){
        dataList = searchList;
        notifyDataSetChanged();
    }
}

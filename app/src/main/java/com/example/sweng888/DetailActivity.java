package com.example.sweng888;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.github.clans.fab.FloatingActionButton;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;

public class DetailActivity extends AppCompatActivity {

    TextView detailDesc, detailTitle, detailLang, likeCountText;
    ImageView detailImage;
    FloatingActionButton deleteButton, editButton;
    Button likeButton, addCommentButton;
    EditText commentInput;
    String key = "";
    String imageUrl = "";
    int likeCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        detailDesc = findViewById(R.id.detailDesc);
        detailImage = findViewById(R.id.detailImage);
        detailTitle = findViewById(R.id.detailTitle);
        deleteButton = findViewById(R.id.deleteButton);
        editButton = findViewById(R.id.editButton);
        detailLang = findViewById(R.id.detailLang);
        likeCountText = findViewById(R.id.likeCountText);
        likeButton = findViewById(R.id.likeButton);
        addCommentButton = findViewById(R.id.addCommentButton);
        commentInput = findViewById(R.id.commentInput);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            detailDesc.setText(bundle.getString("Description"));
            detailTitle.setText(bundle.getString("Title"));
            detailLang.setText(bundle.getString("Language"));
            key = bundle.getString("Key");
            imageUrl = bundle.getString("Image");
            Glide.with(this).load(bundle.getString("Image")).into(detailImage);
            likeCount = bundle.getInt("LikeCount", 0); // Retrieve like count
            likeCountText.setText("Likes: " + likeCount);
        }

        // Like button functionality
        likeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                likeCount++;
                likeCountText.setText("Likes: " + likeCount);
                updateLikeCount();
            }
        });

        // Add comment functionality
        addCommentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String comment = commentInput.getText().toString();
                if (!comment.isEmpty()) {
                    addComment(comment);
                    commentInput.setText(""); // Clear input field
                } else {
                    Toast.makeText(DetailActivity.this, "Comment cannot be empty", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Delete button functionality
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Android Uploads");
                FirebaseStorage storage = FirebaseStorage.getInstance();
                StorageReference storageReference = storage.getReferenceFromUrl(imageUrl);
                storageReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        reference.child(key).removeValue();
                        Toast.makeText(DetailActivity.this, "Deleted", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        finish();
                    }
                });
            }
        });

        // Edit button functionality
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DetailActivity.this, UpdateActivity.class)
                        .putExtra("Title", detailTitle.getText().toString())
                        .putExtra("Description", detailDesc.getText().toString())
                        .putExtra("Language", detailLang.getText().toString())
                        .putExtra("Image", imageUrl)
                        .putExtra("Key", key);
                startActivity(intent);
            }
        });
    }

    private void updateLikeCount() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Android Uploads").child(key);
        Map<String, Object> updates = new HashMap<>();
        updates.put("likeCount", likeCount);
        reference.updateChildren(updates);
    }

    private void addComment(String comment) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Android Uploads").child(key);
        reference.child("comments").push().setValue(comment);
    }
}

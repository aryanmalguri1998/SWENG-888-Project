package com.example.sweng888;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CommentActivity extends AppCompatActivity {

    EditText commentInput;
    Button postButton;
    RecyclerView commentRecyclerView;
    CommentAdapter commentAdapter;
    ArrayList<Comment> commentList;
    DatabaseReference databaseReference;
    String postKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        commentInput = findViewById(R.id.commentInput);
        postButton = findViewById(R.id.postButton);
        commentRecyclerView = findViewById(R.id.commentRecyclerView);

        commentList = new ArrayList<>();
        commentAdapter = new CommentAdapter(this, commentList);
        commentRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        commentRecyclerView.setAdapter(commentAdapter);

        postKey = getIntent().getStringExtra("Key");
        databaseReference = FirebaseDatabase.getInstance().getReference("Android Uploads").child(postKey).child("Comments");

        postButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String commentText = commentInput.getText().toString().trim();
                if (!commentText.isEmpty()) {
                    String commentId = databaseReference.push().getKey();
                    Comment comment = new Comment(commentId, commentText);
                    databaseReference.child(commentId).setValue(comment);
                    commentInput.setText("");
                } else {
                    Toast.makeText(CommentActivity.this, "Comment cannot be empty", Toast.LENGTH_SHORT).show();
                }
            }
        });

        loadComments();
    }

    private void loadComments() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                commentList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Comment comment = dataSnapshot.getValue(Comment.class);
                    commentList.add(comment);
                }
                commentAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(CommentActivity.this, "Failed to load comments", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

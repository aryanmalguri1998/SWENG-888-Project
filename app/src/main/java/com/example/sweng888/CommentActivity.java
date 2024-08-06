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

    // Declare UI elements and variables
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

        // Initialize UI elements
        commentInput = findViewById(R.id.commentInput);
        postButton = findViewById(R.id.postButton);
        commentRecyclerView = findViewById(R.id.commentRecyclerView);

        // Initialize comment list and adapter
        commentList = new ArrayList<>();
        commentAdapter = new CommentAdapter(this, commentList);
        commentRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        commentRecyclerView.setAdapter(commentAdapter);

        // Get the post key from the intent
        postKey = getIntent().getStringExtra("Key");

        // Initialize Firebase database reference
        databaseReference = FirebaseDatabase.getInstance().getReference("Android Uploads").child(postKey).child("Comments");

        // Set onClick listener for the post button
        postButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the comment text from the input field
                String commentText = commentInput.getText().toString().trim();
                if (!commentText.isEmpty()) {
                    // Generate a unique ID for the comment and create a Comment object
                    String commentId = databaseReference.push().getKey();
                    Comment comment = new Comment(commentId, commentText);

                    // Add the comment to the Firebase database
                    databaseReference.child(commentId).setValue(comment);

                    // Clear the input field
                    commentInput.setText("");
                } else {
                    // Show a toast message if the comment is empty
                    Toast.makeText(CommentActivity.this, "Comment cannot be empty", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Load existing comments from the Firebase database
        loadComments();
    }

    // Method to load comments from the Firebase database
    private void loadComments() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // Clear the existing comment list
                commentList.clear();

                // Iterate through each comment snapshot and add it to the comment list
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Comment comment = dataSnapshot.getValue(Comment.class);
                    commentList.add(comment);
                }

                // Notify the adapter that the data set has changed
                commentAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Show a toast message if loading comments fails
                Toast.makeText(CommentActivity.this, "Failed to load comments", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

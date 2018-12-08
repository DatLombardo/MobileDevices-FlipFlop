package ca.uoit.flip_flop.flipflop;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.TreeMap;

public class AddPostActivity extends AppCompatActivity {

    public EditText postTitle;
    public EditText postContent;
    public Button cancelBtn;
    public Button postBtn;
    private int postCount;
    protected DatabaseReference postTable;

    private int userId;

    /**
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);

        userId = getIntent().getIntExtra("user_id", 0);

        cancelBtn = (Button)findViewById(R.id.cancelBtn);
        postBtn = (Button)findViewById(R.id.postBtn);
        postTitle = (EditText)findViewById(R.id.add_post_title);
        postContent = (EditText)findViewById(R.id.add_post_content);
        postTable = FirebaseDatabase.getInstance().getReference().child("Posts");

        postTable.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot userSnapshot) {
                // get all of the children at this level.
                Iterable<DataSnapshot> posts = userSnapshot.getChildren();

                for (DataSnapshot currPost : posts) {
                    postCount = Integer.parseInt(currPost.getKey());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    /**
     * addPost
     * Submits post to database
     *
     * @param view
     */
    public void addPost(View view){
        String postIdStr = Integer.toString(postCount + 1);
        String postTitleStr = postTitle.getText().toString();
        String postContentStr = postContent.getText().toString();

        TreeMap<String, Object> map = new TreeMap<>();
        map.put("title", postTitleStr);
        map.put("contents", postContentStr);
        map.put("reputation", 0);
        map.put("user_id", userId);

        postTable.child(postIdStr).setValue(map);
        Toast.makeText(this, "Post submitted", Toast.LENGTH_SHORT).show();
        finish();
    }

    /**
     * cancelPost
     * Returns to previous screen
     *
     * @param view
     */
    public void cancelPost(View view) {finish();}
}

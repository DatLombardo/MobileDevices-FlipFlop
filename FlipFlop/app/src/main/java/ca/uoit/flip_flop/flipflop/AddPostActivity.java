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

public class AddPostActivity extends AppCompatActivity {

    public EditText postTitle;
    public EditText postContent;
    public Button cancelBtn;
    public Button postBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);

        cancelBtn = (Button)findViewById(R.id.cancelBtn);
        postBtn = (Button)findViewById(R.id.postBtn);
        postTitle = (EditText)findViewById(R.id.add_post_title);
        postContent = (EditText)findViewById(R.id.add_post_content);
    }

    /**
     * addPost
     * Submits post to database
     *
     * @param view
     */
    public void addPost(View view){
        String addPostTitle = postTitle.getText().toString();
        String addPostContent = postContent.getText().toString();

        int userId = getIntent().getIntExtra("user_id", 0);
        Intent data = new Intent();
        data.putExtra("post_title", addPostTitle);
        data.putExtra("post_content", addPostContent);
        data.putExtra("user_id", userId);

        setResult(RESULT_OK, data);
    }

    /**
     * cancelPost
     * Returns to previous screen
     *
     * @param view
     */
    public void cancelPost(View view) {finish();}
}

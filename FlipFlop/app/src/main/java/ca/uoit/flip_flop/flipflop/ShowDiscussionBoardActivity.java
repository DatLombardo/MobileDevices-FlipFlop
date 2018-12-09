package ca.uoit.flip_flop.flipflop;

import android.content.Context;
import android.media.MediaPlayer;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.TreeMap;

public class ShowDiscussionBoardActivity extends AppCompatActivity {
    private ArrayList<Comment> comments = new ArrayList<Comment>();
    private ArrayList<User> userList = new ArrayList<User>();
    private TextView postTitle;
    private TextView postContent;
    private TextView posterName;
    private EditText commentText;
    private int postId;
    private int userId;
    protected DatabaseReference commentTable;

    private MediaPlayer addCommentSound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_discussion_board);

        Post post = (Post)getIntent().getSerializableExtra("post");
        String poster = getIntent().getStringExtra("poster");
        comments = (ArrayList<Comment>)getIntent().getSerializableExtra("comments");

        postTitle = (TextView)findViewById(R.id.post_title);
        postContent = (TextView)findViewById(R.id.post_content);
        posterName = (TextView)findViewById(R.id.poster_name);
        commentText = (EditText)findViewById(R.id.add_comment);

        postTitle.setText(post.getTitle());
        postContent.setText(post.getContents());
        posterName.setText(poster);
        postId = getIntent().getIntExtra("post_id", 0);
        userId = getIntent().getIntExtra("user_id", -1);

        DatabaseReference userTable = FirebaseDatabase.getInstance().getReference("Users");
        commentTable = FirebaseDatabase.getInstance().getReference().child("Comments");

        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.recyclerview_comments);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        final CommentAdapter commentAdapter = new ShowDiscussionBoardActivity.CommentAdapter(comments);
        recyclerView.setAdapter(commentAdapter);

        userTable.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot userSnapshot) {
                Iterable<DataSnapshot> users = userSnapshot.getChildren();

                userList.clear();
                for (DataSnapshot currUser : users) {
                    int id = Integer.parseInt(currUser.getKey());
                    String username = currUser.child("username").getValue(String.class);
                    String password = currUser.child("password").getValue(String.class);
                    String dateCreated = currUser.child("dateCreated").getValue(String.class);

                    User user = new User();
                    user.setUserId(id);
                    user.setUsername(username);
                    user.setPassword(password);
                    user.setDateCreated(dateCreated);
                    userList.add(user);

                }
                fillUsernames();
                commentAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        commentTable.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot userSnapshot) {
                // get all of the children at this level.
                DataSnapshot allComments = userSnapshot.child
                        (Integer.toString(postId));

                comments.clear();
                for (DataSnapshot comment : allComments.getChildren()){
                    Comment temp = new Comment();
                    int commentNum = Integer.parseInt(comment.getKey());
                    String contents = comment.child("contents").getValue(String.class);
                    int userId = comment.child("user_id").getValue(Integer.class);
                    //long userId = Integer.parseInt(comment.child("user_id").getValue(String.class));
                    temp.setCommentId(postId);
                    temp.setComment(contents);
                    temp.setUserId(userId);
                    temp.setCommentNumber(commentNum);
                    comments.add(temp);
                }

                //reverse list
                Collections.reverse(comments);

                fillUsernames();
                commentAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    /**
     * bring the user back to the main activity if cancel is pressed
     * @param view
     */
    public void cancelComment(View view){finish();}

    /**
     * allows users to post comment
     * checks if user is logged in, if not displays toast
     * if user is logged in then add comment data to the DB
     * @param view
     */
    public void postComment(View view){
        addCommentSound = MediaPlayer.create(this, R.raw.button_click);
        if (userId == -1) {
            Toast.makeText(this, "Must be logged in to post comments", Toast.LENGTH_SHORT).show();
        } else {
            String postIdStr = Integer.toString(postId);
            String commentIdStr = Integer.toString(getCommentCount(comments) + 1);
            String commentContent = commentText.getText().toString();

            TreeMap<String, Object> map = new TreeMap<>();
            map.put("contents", commentContent);
            map.put("user_id", userId);

            commentTable.child(postIdStr).child(commentIdStr).setValue(map);

            addCommentSound.start();
            Toast.makeText(this, "Comment submitted", Toast.LENGTH_SHORT).show();
            commentText.setText("");

        }

        // Hide keyboard
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(commentText.getWindowToken(), 0);

    }


    public class CommentAdapter extends
            RecyclerView.Adapter<ShowDiscussionBoardActivity.CommentAdapter.ViewHolder>{
        private ArrayList<Comment> comments = new ArrayList<Comment>();

        CommentAdapter(ArrayList<Comment> comments){
            this.comments = comments;
        }

        class ViewHolder extends RecyclerView.ViewHolder{
            View customView;
            TextView commenterName;
            TextView commentContent;

            ViewHolder(View view){
                super(view);
                customView = view;
                commenterName = view.findViewById(R.id.commenter_name);
                commentContent = view.findViewById(R.id.comment_content);
            }
        }

        @Override
        public ShowDiscussionBoardActivity.CommentAdapter.ViewHolder
        onCreateViewHolder(ViewGroup parent, int viewType) {
            LinearLayout layout = (LinearLayout) LayoutInflater.from(parent.getContext()).inflate
                    (R.layout.comment_card, parent, false);
            return new ViewHolder(layout);
        }

        @Override
        public void onBindViewHolder(ShowDiscussionBoardActivity.CommentAdapter.ViewHolder holder, int index) {
            holder.commenterName.setText(comments.get(index).getCommenterName());
            holder.commentContent.setText(comments.get(index).getComment());
        }

        @Override
        public int getItemCount() {
            return comments.size();
        }
    }

    /**
     * gets the username when a user_id is given
     * @param userId
     * @return
     */
    public String getUsername(int userId) {
        for(User user : userList){
            if (user.getUserId() == userId){
                return user.getUsername();
            }
        }
        return null;
    }

    /**
     * gets usernames of users who comment
     */
    public void fillUsernames(){
        for(Comment comment : comments){
            String username = getUsername(comment.getUserId());
            comment.setCommenterName(username);
        }
    }

    /**
     * getCommentCount
     * gets the total comment count for every post
     *
     * @param commList
     * @return
     */
    public int getCommentCount(ArrayList<Comment> commList){
        int count = 0;
        if (commList.isEmpty()){
            return count;
        }
        else{
            for (Comment currComment : comments) {
                if (currComment.getCommentNumber() > count){
                    count = currComment.getCommentNumber();
                }
            }
            return count;
        }

    }

}

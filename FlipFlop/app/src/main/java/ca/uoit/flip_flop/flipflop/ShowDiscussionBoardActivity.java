package ca.uoit.flip_flop.flipflop;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ShowDiscussionBoardActivity extends AppCompatActivity {
    private ArrayList<Comment> comments = new ArrayList<Comment>();
    private ArrayList<User> userList = new ArrayList<User>();
    private TextView postTitle;
    private TextView postContent;
    private TextView posterName;

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

        postTitle.setText(post.getTitle());
        postContent.setText(post.getContents());
        posterName.setText(poster);

        DatabaseReference userTable = FirebaseDatabase.getInstance().getReference("Users");

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
    }

    public void cancelComment(View view){finish();}

    public void postComment(View view){

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

    public String getUsername(int userId) {
        for(User user : userList){
            if (user.getUserId() == userId){
                System.out.println(user.getUsername());
                return user.getUsername();
            }
        }
        return null;
    }

    public void fillUsernames(){
        for(Comment comment : comments){
            String username = getUsername(comment.getUserId());
            comment.setCommenterName(username);
            //System.out.println(username);
        }
    }

}

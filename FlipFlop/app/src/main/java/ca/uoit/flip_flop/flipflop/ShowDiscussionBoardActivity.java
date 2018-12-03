package ca.uoit.flip_flop.flipflop;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class ShowDiscussionBoardActivity extends AppCompatActivity {
    private ArrayList<Comment> comments = new ArrayList<Comment>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_discussion_board);
        Comment comm1 = new Comment();
        Comment comm2 = new Comment();
        comm1.setCommentId(1);
        comm1.setCommenterName("danny");
        comm1.setComment("This is comment 1");
        comm1.setCommentId(2);
        comm1.setCommenterName("Rish");
        comm1.setComment("This is comment 2");

        comments.add(comm1);
        comments.add(comm2);
        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.recyclerview_comments);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(new ShowDiscussionBoardActivity.CommentAdapter(comments));
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
            holder.commentContent.setText(comments.get(index).getComment());
            holder.commenterName.setText(comments.get(index).getCommenterName());
        }

        @Override
        public int getItemCount() {
            return comments.size();
        }
    }
}

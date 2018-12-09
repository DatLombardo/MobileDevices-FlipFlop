package ca.uoit.flip_flop.flipflop;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class Profile extends AppCompatActivity {
    public BoardDBHelper dbHelper = new BoardDBHelper(this);
    public ArrayList<Post> postList = new ArrayList<Post>();
    public ArrayList<User> userList = new ArrayList<User>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        int uId = getIntent().getIntExtra("user_id", 0);

        postList = (ArrayList<Post>) getIntent().getSerializableExtra("post_list");
        userList = (ArrayList<User>) getIntent().getSerializableExtra("user_list");

        TextView userLabel = (TextView)findViewById(R.id.user_label);
        TextView dateLabel = (TextView)findViewById(R.id.date_label);

        if (uId == 0){
            userLabel.setText("Anonymous");
            dateLabel.setText("Not Registered");
            return;
        }

        // Find user
        User user = null;
        for (User u : userList) {
            if (u.getUserId() == uId) {
                user = u;
                break;
            }
        }

        // Update labels
        userLabel.setText(user.getUsername());
        dateLabel.setText(user.getDateCreated());

        // Create lists of likes / dislikes
        ArrayList<Post> likesList = getPosts(dbHelper.getUserLikes(uId));
        ArrayList<Post> dislikesList = getPosts(dbHelper.getUserDislikes(uId));

        // Apply to both listView adapters.
        PostAdapter likesAdapater = new PostAdapter(this, likesList);
        ListView likesView = (ListView) findViewById(R.id.likes);
        likesView.setAdapter(likesAdapater);

        PostAdapter dislikesAdapter = new PostAdapter(this, dislikesList);
        ListView dislikesView = (ListView) findViewById(R.id.dislikes);
        dislikesView.setAdapter(dislikesAdapter);
    }

    /**
     * getPosts
     *
     * Used to get all posts based on a list of integers returned from SQL call for likes / dislikes
     * @param postIds
     * @return
     */
    public ArrayList<Post> getPosts(List<Integer> postIds) {
        ArrayList<Post> posts = new ArrayList<>();
        for (Integer id : postIds) {
            for (Post p : postList) {
                if (p.getPostId() == id) {
                    posts.add(p);
                    break;
                }
            }
        }

        return posts;
    }

    /**
     * PostAdapter
     *
     * Class used to create simple listView elements to populate like and dislike views.
     */
    public class PostAdapter extends ArrayAdapter<Post> {
        public PostAdapter(Context context, ArrayList<Post> posts) {
            super(context, 0, posts);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // Get the data item for this position
            Post currPost = getItem(position);
            // Check if an existing view is being reused, otherwise inflate the view
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.post_preview_card, parent, false);
            }
            // Lookup view for data population
            TextView title = (TextView) convertView.findViewById(R.id.post_title);
            TextView contents = (TextView) convertView.findViewById(R.id.post_content);
            // Populate the data into the template view using the data object
            title.setText(currPost.getTitle());
            contents.setText(currPost.getContents());
            // Return the completed view to render on screen
            return convertView;
        }
    }

}

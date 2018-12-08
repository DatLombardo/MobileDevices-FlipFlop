package ca.uoit.flip_flop.flipflop;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CountDownLatch;

public class MainActivity extends AppCompatActivity {
    public BoardDBHelper dbHelper = new BoardDBHelper(this);
    private List<User> users;
    private ArrayList<User> userList = new ArrayList<User>();
    private ArrayList<Post> postList = new ArrayList<Post>();
    private ArrayList<Comment> commentList = new ArrayList<Comment>();
    private User currentUser;

    protected DatabaseReference userTable;
    protected DatabaseReference postTable;
    protected DatabaseReference commentTable;

    private final int ADD_POST_CODE = 1000;
    private final int ADD_COMMENT_CODE = 2000;
    private final int LOGIN_CODE = 1001;
    private final int REGISTRATION_CODE = 1002;

    public int userCount;
    public int postCount;
    public int commentCount;

    TextView username;

    public int[] imageIds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // store image ids
        imageIds = new int[] {
                R.drawable.p1,
                R.drawable.p2,
                R.drawable.p3,
                R.drawable.p4,
                R.drawable.p5,
                R.drawable.p6,
                R.drawable.p7,
                R.drawable.p8
        };

        //recycler view stuff
        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerview_boards);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        final BoardAdapter boardAdapter = new BoardAdapter(postList);

        //Database stuff
        dbHelper.deleteAllUsers();

        addFillerUsers();
        addFillerLikes();
        addFillerDislikes();
        List<Integer> likes;
        //User testUserRish = this.users.get(0);
        //likes = dbHelper.getUserLikes(testUserRish.getUserId());
        /*
        for(Integer el : likes){
            System.out.println(el);
        }*/

        userTable = FirebaseDatabase.getInstance().getReference("Users");
        commentTable = FirebaseDatabase.getInstance().getReference().child("Comments");
        postTable = FirebaseDatabase.getInstance().getReference().child("Posts");

        recyclerView.setAdapter(boardAdapter);

        userTable.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot userSnapshot) {
                // get all of the children at this level.
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

                    userCount = id;
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        postTable.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot userSnapshot) {
                // get all of the children at this level.
                Iterable<DataSnapshot> posts = userSnapshot.getChildren();

                postList.clear();
                for (DataSnapshot currPost : posts) {
                    int id = Integer.parseInt(currPost.getKey());
                    String title = currPost.child("title").getValue(String.class);
                    String contents = currPost.child("contents").getValue(String.class);
                    int reputation = currPost.child("reputation").getValue(Integer.class);
                    int userId = currPost.child("user_id").getValue(Integer.class);

                    Post post = new Post();
                    post.setPostId(id);
                    post.setTitle(title);
                    post.setContents(contents);
                    post.setReputation(reputation);
                    post.setUserId(userId);
                    postList.add(post);

                    postCount = id;
                }
                //reverse posts
                Collections.reverse(postList);
                boardAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        commentTable.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot userSnapshot) {
                // get all of the children at this level.
                Iterable<DataSnapshot> allComments = userSnapshot.getChildren();

                commentList.clear();
                for (DataSnapshot comments : allComments) {
                    //System.out.println(currComment);
                    int commentId = Integer.parseInt(comments.getKey());
                    for (DataSnapshot comment : comments.getChildren()) {
                        int commentNum = Integer.parseInt(comment.getKey());
                        String contents = comment.child("contents").getValue(String.class);
                        int userId = comment.child("user_id").getValue(Integer.class);
                        //long userId = Integer.parseInt(comment.child("user_id").getValue(String.class));
                        Comment comm = new Comment();
                        comm.setCommentId(commentId);
                        comm.setComment(contents);
                        comm.setUserId(userId);
                        comm.setCommentNumber(commentNum);

                        commentList.add(comm);

                    }

                    commentCount = commentId;
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    //TODO: take this out
    public void testDatabase(View view) {
        userTable = FirebaseDatabase.getInstance().getReference("Users");
        userTable.push().setValue("5");
        System.out.println("Test");
        User testUser = new User();
        testUser.setUsername("Testing Mans");
        testUser.setPassword("sdsdsd");
        testUser.setDateCreated("2018-11-25");

        createUser(testUser);

        ArrayList<Post> testUPosts = getUserPosts(1);

        ArrayList<Comment> testComments = getPostComments(1);

        for (Post item : testUPosts) {
            System.out.println(item.getTitle() + "\t" + item.getContents());
        }

        for (Comment item : testComments) {
            System.out.println(Integer.toString(item.getCommentNumber()) + "\t" + item.getComment());
        }

        int commCount = getCommentCount(testComments);

        //createPost(String title, String contents, int rep, int uId)

        //createComment(int postId, int commCount, String contents, int uId)

        createPost("Test Item", "This is a test", 0, 1);
        createComment(1, commCount, "Test Comment, Delete", 1);

    }

    /**
     * creates a new user and adds it to the DB
     * @param user
     */
    public void createUser(User user) {
        userTable.child(Integer.toString(userCount + 1))
                .child("username").setValue(user.getUsername());
        userTable.child(Integer.toString(userCount + 1))
                .child("password").setValue(user.getPassword());
        userTable.child(Integer.toString(userCount + 1))
                .child("dateCreated").setValue(user.getDateCreated());

        System.out.println("User Created");
    }

    /**
     * gets posts linked to the user_id
     * @param userId
     * @return
     */
    public ArrayList<Post> getUserPosts(int userId) {
        ArrayList<Post> userPosts = new ArrayList();
        for (Post currPost : postList) {
            if (currPost.getUserId() == userId) {
                userPosts.add(currPost);
            }
        }
        return userPosts;
    }

    /**
     * gets comments related to the post_id
     * @param postId
     * @return
     */
    public ArrayList<Comment> getPostComments(int postId) {
        ArrayList<Comment> comments = new ArrayList<Comment>();
        for (Comment currComment : commentList) {
            if (currComment.getCommentId() == postId) {
                comments.add(currComment);
            }
        }
        return comments;
    }

    //TODO: remove this
    public int getCommentCount(ArrayList<Comment> commList) {
        int count = 0;
        if (commList.isEmpty()) {
            return count;
        } else {
            for (Comment currComment : commentList) {
                if (currComment.getCommentNumber() > count) {
                    count = currComment.getCommentNumber();
                }
            }
            return count;
        }

    }

    /**
     * adds post to the DB, and increments the post count
     * @param title
     * @param contents
     * @param rep
     * @param uId
     */
    public void createPost(String title, String contents, int rep, int uId) {

        //postCount
        postTable.child(Integer.toString(postCount + 1))
                .child("title").setValue(title);
        postTable.child(Integer.toString(postCount + 1))
                .child("contents").setValue(contents);
        postTable.child(Integer.toString(postCount + 1))
                .child("reputation").setValue(rep);
        postTable.child(Integer.toString(postCount + 1))
                .child("user_id").setValue(uId);

        System.out.println("Post Created");
    }

    /**
     * adds comment content and user_id to DB
     * @param postId
     * @param commCount
     * @param contents
     * @param uId
     */
    public void createComment(int postId, int commCount, String contents, int uId) {

        //postCount
        commentTable.child(Integer.toString(postId)).child(Integer.toString(commCount))
                .child("contents").setValue(contents);
        commentTable.child(Integer.toString(postId)).child(Integer.toString(commCount))
                .child("user_id").setValue(Integer.toString(uId));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mainmenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_Login:
                launchLogin();
                return true;
            case R.id.action_Register:
                launchRegistration();
                return true;
            case R.id.action_Logout:
                logout();
                return true;
            case R.id.action_Settings:
                //settings();
                return true;
            case R.id.action_Add:
                launchAddPost();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * gets the data from the db and fills the ShowDiscussionBoardActivity
     * @param post
     */
    public void openPost(Post post) {
        ArrayList<Comment> comments = getPostComments(post.getPostId());
        Intent intent = new Intent(this, ShowDiscussionBoardActivity.class);
        intent.putExtra("count", userCount);
        intent.putExtra("poster", getUsername(post.getUserId()));
        if (currentUser != null)
            intent.putExtra("user_id", currentUser.getUserId());
        intent.putExtra("post", post);
        intent.putExtra("comments", comments);
        intent.putExtra("post_id", post.getPostId());
        startActivity(intent);
    }

    /**
     * gets the username by user_id
     * @param userId
     * @return
     */
    public String getUsername(int userId) {
        String username = "";

        for (User user : userList) {
            if (user.getUserId() == userId) {
                username = user.getUsername();
                break;
            }
        }

        return username;
    }

    /**
     * adds post to the main activity, if user is registered and logged in, if not display toast
     */
    public void launchAddPost() {
        if (currentUser != null) {
            Intent intent = new Intent(this, AddPostActivity.class);

            intent.putExtra("user_id", currentUser.getUserId());
            intent.putExtra("post_id", postCount);
            startActivityForResult(intent, ADD_POST_CODE);

        } else {
            Toast.makeText(this, "Must be logged in to post", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * start login activity
     */
    public void launchLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivityForResult(intent, LOGIN_CODE);
    }

    /**
     * start registration activity
     */
    public void launchRegistration() {
        if (currentUser != null) {
            Toast.makeText(this, "User already registered", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent intent = new Intent(this, RegisterActivity.class);
        intent.putExtra("user_count", userCount);
        intent.putExtra("user_list", userList);
        startActivity(intent);
    }

    //TODO: remove this if not necessary
    public void addFillerUsers() {
        dbHelper.createUser("Danny", "yoyo", "red", "9/11/2018");
        dbHelper.createUser("Lachlan", "yoyo", "red", "9/11/2018");
        users = dbHelper.getAllUsers();
    }

    //TODO: remove this if not necessary
    public void addFillerLikes() {
        dbHelper.deleteAllLikes();
        User testUser1 = this.users.get(0);
        User testUser2 = this.users.get(1);
        dbHelper.createLike(1, testUser1.getUserId());
        dbHelper.createLike(2, testUser1.getUserId());
        dbHelper.createLike(1, testUser2.getUserId());
        dbHelper.createLike(3, testUser2.getUserId());
    }

    //TODO: remove this if not necessary
    public void addFillerDislikes() {
        dbHelper.deleteAllDislikes();
        User testUser1 = this.users.get(0);
        User testUser2 = this.users.get(1);
        dbHelper.createDislike(3, testUser1.getUserId());
        dbHelper.createDislike(2, testUser2.getUserId());
    }

    /**
     * Logout user if user is logged in
     */
    public void logout() {
        currentUser = null;
        System.out.println(currentUser);
        username = (TextView) findViewById(R.id.username);
        username.setText(R.string.anon_user);
    }

    /**
     * sets the results for activities such as login
     * @param requestCode
     * @param resultCode
     * @param data
     */
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ADD_POST_CODE) {
            if (resultCode == RESULT_OK) {
                //TODO: db stuff
                String postContent = data.getStringExtra("post_title");
                String postTitle = data.getStringExtra("post_content");
                int userId = data.getIntExtra("user_id", 0);
                createPost(postTitle, postContent, 0, userId);
            }
        } else if (requestCode == LOGIN_CODE) {
            if (resultCode == RESULT_OK) {
                String id = data.getStringExtra("id");
                if (id == null) return; // user didnt log in

                // get user
                for (User u : userList) {
                    if (u.getUserId() == Integer.parseInt(id)) {
                        currentUser = u;
                        break;
                    }
                }

                // logged in
                username = (TextView) findViewById(R.id.username);
                username.setText(currentUser.getUsername());
                Toast.makeText(MainActivity.this, "Logged In", Toast.LENGTH_SHORT).show();


            }
        } else if (requestCode == ADD_COMMENT_CODE) {
            if (resultCode == RESULT_OK) {
                //TODO: db stuff
            }
        }
    }

    /**
     * to load random images
     * @param min
     * @param max
     * @return
     */
    static int randomInt(int min, int max) {
        return new Random().nextInt(max + 1 - min) + min;
    }

    public class BoardAdapter extends RecyclerView.Adapter<BoardAdapter.ViewHolder> {
        private ArrayList<Post> posts = new ArrayList<Post>();

        BoardAdapter(ArrayList<Post> postList) {
            posts = postList;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LinearLayout layout = (LinearLayout) LayoutInflater.from(parent.getContext()).inflate
                    (R.layout.board_preview_card, parent, false);
            return new ViewHolder(layout);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int index) {
            holder.index = index;
            holder.post = posts.get(index);

            // set profile pic
            holder.profileImage.setImageDrawable(
                    getDrawable(imageIds[randomInt(0, imageIds.length - 1)]));

            holder.postTitle.setText(posts.get(index).getTitle());
            String username = getUsername(posts.get(index).getUserId());
            holder.posterName.setText(username);
            holder.postContent.setText(posts.get(index).getContents());
            String rep = String.valueOf(posts.get(index).getReputation());
            holder.repCounter.setText(rep);
        }

        @Override
        public int getItemCount() {
            return posts.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            Post post;
            View customView;
            CardView cardView;
            ImageView profileImage;
            TextView postTitle;
            TextView posterName;
            TextView postContent;
            TextView repCounter;
            ImageButton upVote;
            ImageButton downVote;

            int index = 0;

            ViewHolder(View view) {
                super(view);
                customView = view;
                cardView = view.findViewById(R.id.board_card_view);
                profileImage = view.findViewById(R.id.profile_image);
                postTitle = view.findViewById(R.id.preview_title);
                posterName = view.findViewById(R.id.poster_name);
                postContent = view.findViewById(R.id.preview_content);
                repCounter = view.findViewById(R.id.reputation_counter);
                upVote = view.findViewById(R.id.upVote);
                downVote = view.findViewById(R.id.downVote);

                upVote.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        post.setReputation(post.getReputation() + 1);
                        DatabaseReference ref = postTable.child(Integer.toString(post.getPostId()));
                        ref.child("reputation").setValue(post.getReputation());
                    }
                });

                downVote.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        post.setReputation(post.getReputation() - 1);
                        DatabaseReference ref = postTable.child(Integer.toString(post.getPostId()));
                        ref.child("reputation").setValue(post.getReputation());
                    }
                });


                View.OnClickListener viewClickListener = new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        openPost(post);
                    }
                };

                cardView.setOnClickListener(viewClickListener);
                postTitle.setOnClickListener(viewClickListener);
            }
        }
    }
}
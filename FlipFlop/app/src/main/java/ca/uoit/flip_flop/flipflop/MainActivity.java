package ca.uoit.flip_flop.flipflop;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class MainActivity extends AppCompatActivity {
    private ArrayList<PostPreview> postPreviews = new ArrayList<PostPreview>();
    public BoardDBHelper dbHelper = new BoardDBHelper(this);
    private List<User> users;
    private ArrayList<User> userList = new ArrayList<User>();
    private ArrayList<Post> postList = new ArrayList<Post>();
    private ArrayList<Comment> commentList = new ArrayList<Comment>();

    protected DatabaseReference userTable;
    protected DatabaseReference postTable;
    protected DatabaseReference commentTable;

    public int userCount;
    public int postCount;
    public int commentCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        PostPreview preview = new PostPreview(1, "spew", "lombo", "lombobobombo", 3);
        postPreviews.add(new PostPreview(1, "spew", "lombo", "lombobobombo", 14));
        postPreviews.add(new PostPreview(1, "spew1", "lombo", "lombobobombo", 2));
        postPreviews.add(new PostPreview(1, "spew2", "lombo", "lombobobombo", 7));
        postPreviews.add(new PostPreview(1, "spew3", "lombo", "lombobobombo", 1));

        //recycler view stuff
        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.recyclerview_boards);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(new BoardAdapter(postPreviews));

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
                //See the output from the database read
                /*
                for (int i = 0; i < userList.size(); i++) {
                    System.out.println(userList.get(i).getUsername());
                }
                */

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
                //See the output from the database read
                /*
                for (int i = 0; i < postList.size(); i++) {
                    System.out.println(postList.get(i).getTitle());
                }*/

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
                    for (DataSnapshot comment : comments.getChildren()){
                        int commentNum = Integer.parseInt(comment.getKey());
                        String contents = comment.child("contents").getValue(String.class);
                        int userId = comment.child("user_id").getValue(Integer.class);
                        Comment comm = new Comment();
                        comm.setCommentId(commentId);
                        comm.setComment(contents);
                        comm.setCommentNumber(commentNum);
                        /**
                         * TODO::
                         * CREATE A FUNCTION TO GET USER NAME GIVEN USERID
                         */
                        //comm.setCommenterName(??????);
                        commentList.add(comm);

                    }

                    commentCount = commentId;
                }
                //See the output from the database read
                /*
                for (int i = 0; i < commentList.size(); i++) {
                    System.out.println(commentList.get(i).getComment() + " " +commentList.get(i).getCommentId());
                }*/

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

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



    public void createUser(User user){
        userTable.child(Integer.toString(userCount + 1))
                .child("username").setValue(user.getUsername());
        userTable.child(Integer.toString(userCount + 1))
                .child("password").setValue(user.getPassword());
        userTable.child(Integer.toString(userCount + 1))
                .child("dateCreated").setValue(user.getDateCreated());

        System.out.println("User Created");
    }

    public ArrayList<Post> getUserPosts(int userId){
        ArrayList<Post> userPosts = new ArrayList();
        for (Post currPost : postList) {
            //System.out.println(currPost.getUserId());
            if(currPost.getUserId() == userId){
                userPosts.add(currPost);
            }
        }
        return userPosts;
    }


    public ArrayList<Comment> getPostComments(int postId){
        ArrayList<Comment> comments = new ArrayList();
        for (Comment currComment : commentList) {
            //System.out.println(currComment.getCommentId());
            if(currComment.getCommentId() == postId){
                comments.add(currComment);
            }
        }
        return comments;
    }

    public int getCommentCount(ArrayList<Comment> commList){
        int count = 0;
        if (commList.isEmpty()){
            return count;
        }
        else{
            for (Comment currComment : commentList) {
                if (currComment.getCommentNumber() > count){
                    count++;
                }
            }
            return count;
        }

    }

    public void createPost(String title, String contents, int rep, int uId){

        //postCount
        postTable.child(Integer.toString(postCount + 1))
                .child("title").setValue(title);
        postTable.child(Integer.toString(postCount + 1))
                .child("contents").setValue(contents);
        postTable.child(Integer.toString(postCount + 1))
                .child("reputation").setValue(Integer.toString(rep));
        postTable.child(Integer.toString(postCount + 1))
                .child("user_id").setValue(Integer.toString(uId));

        System.out.println("Post Created");
    }

    public void createComment(int postId, int commCount, String contents, int uId){

        //postCount
        commentTable.child(Integer.toString(postId)).child(Integer.toString(commCount))
                .child("contents").setValue(contents);
        commentTable.child(Integer.toString(postId)).child(Integer.toString(commCount))
                .child("user_id").setValue(Integer.toString(uId));
    }




    public class BoardAdapter extends RecyclerView.Adapter<BoardAdapter.ViewHolder>{
        private ArrayList<PostPreview> posts = new ArrayList<PostPreview>();

        BoardAdapter(ArrayList<PostPreview> postPreviews){
            posts = postPreviews;
        }

        class ViewHolder extends RecyclerView.ViewHolder{
            View customView;
            TextView postTitle;
            TextView posterName;
            TextView postContent;
            TextView repCounter;

            ViewHolder(View view){
                super(view);
                customView = view;
                postTitle = view.findViewById(R.id.post_title);
                posterName = view.findViewById(R.id.poster_name);
                postContent = view.findViewById(R.id.post_content);
                repCounter = view.findViewById(R.id.reputation_counter);
            }
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LinearLayout layout = (LinearLayout) LayoutInflater.from(parent.getContext()).inflate
                    (R.layout.board_preview_card, parent, false);
            return new ViewHolder(layout);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int index) {
            holder.postTitle.setText(postPreviews.get(index).getTitle());
            holder.posterName.setText(postPreviews.get(index).getPosterName());
            holder.postContent.setText(postPreviews.get(index).getContents());
            String rep = String.valueOf(postPreviews.get(index).getReputation());
            holder.repCounter.setText(rep);
        }

        @Override
        public int getItemCount() {
            return posts.size();
        }
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
                //login();
                return true;
            case R.id.action_Register:
                //register();
                return true;
            case R.id.action_Logout:
                //logout();
                return true;
            case R.id.action_settings:
                //settings();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void openPost(View view) {
        Intent intent = new Intent(this, ShowDiscussionBoardActivity.class);
        intent.putExtra("count", userCount);
        startActivity(intent);
    }

    public void addFillerUsers(){
        dbHelper.createUser("Danny", "yoyo", "red", "9/11/2018");
        dbHelper.createUser("Lachlan", "yoyo", "red", "9/11/2018");
        users = dbHelper.getAllUsers();
    }

    public void addFillerLikes(){
        dbHelper.deleteAllLikes();
        User testUser1 = this.users.get(0);
        User testUser2= this.users.get(1);
        dbHelper.createLike(1, testUser1.getUserId());
        dbHelper.createLike(2, testUser1.getUserId());
        dbHelper.createLike(1, testUser2.getUserId());
        dbHelper.createLike(3, testUser2.getUserId());
    }

    public void addFillerDislikes(){
        dbHelper.deleteAllDislikes();
        User testUser1 = this.users.get(0);
        User testUser2= this.users.get(1);
        dbHelper.createDislike(3, testUser1.getUserId());
        dbHelper.createDislike(2, testUser2.getUserId());
    }
}

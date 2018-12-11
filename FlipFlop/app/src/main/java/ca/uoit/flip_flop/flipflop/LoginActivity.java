package ca.uoit.flip_flop.flipflop;

import android.content.Intent;
import android.media.MediaPlayer;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    private EditText usernameField;
    private EditText passwordField;

    private MediaPlayer loginBtnSound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usernameField = (EditText)findViewById(R.id.username_login);
        passwordField = (EditText)findViewById(R.id.password_login);

    }

    /**
     * Cancel Login, ends the activity and brings user back to the main activity
     * @param view
     */
    public void cancelLogin(View view){
        setResult(RESULT_CANCELED);
        finish();
    }

    /**
     * Login click handler, checks if username and password match from the DB
     *
     * @param view
     */
    public void doLogin(View view) {
        final String username = usernameField.getText().toString(); // TODO: Error check
        final String password = passwordField.getText().toString();
        loginBtnSound = MediaPlayer.create(this, R.raw.button_click);

        // attempt to find user with specified information
        DatabaseReference userTable = FirebaseDatabase.getInstance().getReference().child("Users");
        userTable.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> users = dataSnapshot.getChildren();
                boolean found = false;
                for (DataSnapshot currUser : users) {
                    String id = currUser.getKey();
                    String u_username = currUser.child("username").getValue(String.class);
                    String u_password = currUser.child("password").getValue(String.class);

                    if (u_username.equals(username)) {
                        byte[] u_salt = PasswordHandler.
                                hexStringToBytes(currUser.child("salt").getValue(String.class));
                        String passwordTest = PasswordHandler.
                                securePassword(password.toCharArray(), u_salt);

                        if (u_password.equals(passwordTest)) {
                            // SUCCESS
                            Intent intent = new Intent();
                            intent.putExtra("id", id);
                            setResult(RESULT_OK, intent);
                            loginBtnSound.start();
                            finish();
                        } else {
                            // INVALID PASSWORD
                            passwordField.setText("");
                            Toast.makeText(LoginActivity.this, "Invalid password entered", Toast.LENGTH_SHORT).show();
                        }

                        found = true;
                    }
                }

                if (!found || username == "" || password == "") {
                    // USER NOT FOUND
                    usernameField.setText("");
                    usernameField.setError("Try entering username again!");
                    passwordField.setText("");
                    passwordField.setError("Try entering password again!");
                    Toast.makeText(LoginActivity.this, "Username not found!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        Intent intent = new Intent();
        intent.putExtra("id", "0");
    }
}

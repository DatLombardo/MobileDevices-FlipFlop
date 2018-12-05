package ca.uoit.flip_flop.flipflop;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import java.util.ArrayList;


public class RegisterActivity extends AppCompatActivity {

    EditText usernameField;
    EditText passwordField;
    EditText confirmPassField;

    protected DatabaseReference userTable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        usernameField = (EditText)findViewById(R.id.username_register);
        passwordField = (EditText)findViewById(R.id.password_register);
        confirmPassField = (EditText)findViewById(R.id.password_confirm_register);

        userTable = FirebaseDatabase.getInstance().getReference("Users");
    }

    /**
     * cancelRegistration
     * Returns to previous screen
     *
     * @param view
     */
    public void cancelRegistration(View view) {finish();}

    /**
     * doRegistration
     * Submits user registration to database
     *
     * @param view
     */
    public void doRegistration(View view) {

        String username = usernameField.getText().toString();
        String password = passwordField.getText().toString();
        String secondPass = confirmPassField.getText().toString();
        boolean userExists = false;

        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password) || TextUtils.isEmpty(secondPass)){
            usernameField.setError("Username cannot be empty");
            passwordField.setError("Password cannot be empty");
            return;
        } else if (password.equals(secondPass)) {
            ArrayList<User> userList =
                    (ArrayList<User>) getIntent().getSerializableExtra("user_list");

            for (User user : userList) {
                if (username.equals(user.getUsername())) {
                    Toast.makeText
                            (this, "Username already exists", Toast.LENGTH_SHORT).show();
                    userExists = true;
                    break;
                }
            }

            if (!userExists) {
                int userCount = getIntent().getIntExtra("user_count", 0);
                String newCount = String.valueOf(userCount + 1);

                userTable.child(newCount).child("username").setValue(username);
                userTable.child(newCount).child("password").setValue(password);
                Toast.makeText(this, "Registration Complete", Toast.LENGTH_SHORT).show();
                finish();
            }
        } else {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
        }
    }
}

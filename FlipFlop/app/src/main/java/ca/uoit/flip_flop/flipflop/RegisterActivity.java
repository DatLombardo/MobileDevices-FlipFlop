package ca.uoit.flip_flop.flipflop;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


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

        if (password.equals(secondPass)) {
            int userCount = getIntent().getIntExtra("user_count", 0);

//        Intent intent = new Intent();
//        intent.putExtra("username", username);
//        intent.putExtra("password", password);

            String newCount = String.valueOf(userCount + 1);

            userTable.child(newCount).child("username").setValue(username);
            userTable.child(newCount).child("password").setValue(password);
            Toast.makeText(this, "Registration Complete", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
        }
    }
}

package ca.uoit.flip_flop.flipflop;

import android.content.Intent;
import android.graphics.Paint;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import javax.net.ssl.HttpsURLConnection;


public class RegisterActivity extends AppCompatActivity {

    EditText usernameField;
    EditText passwordField;
    EditText confirmPassField;

    private MediaPlayer registerBtnSound;

    protected DatabaseReference userTable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        usernameField = (EditText)findViewById(R.id.username_register);
        passwordField = (EditText)findViewById(R.id.password_register);
        confirmPassField = (EditText)findViewById(R.id.password_confirm_register);

        userTable = FirebaseDatabase.getInstance().getReference("Users");

        TextView lblAgreement = (TextView)findViewById(R.id.agreement);
        lblAgreement.setPaintFlags(lblAgreement.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
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

        registerBtnSound = MediaPlayer.create(this, R.raw.button_click);

        String username = usernameField.getText().toString();
        String password = passwordField.getText().toString();
        String secondPass = confirmPassField.getText().toString();

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-mm-dd");
        Date todayDate = Calendar.getInstance().getTime();
        String todayString = formatter.format(todayDate);

        boolean userExists = false;

        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password) || TextUtils.isEmpty(secondPass)){
            usernameField.setError("Username cannot be empty");
            passwordField.setError("Password cannot be empty");
            confirmPassField.setError("Field can't be empty");
            return;
        } else if (password.equals(secondPass)) {
            ArrayList<User> userList =
                    (ArrayList<User>) getIntent().getSerializableExtra("user_list");

            for (User user : userList) {
                if (username.equals(user.getUsername())) {
                    usernameField.setText("");
                    usernameField.setError("Pick a different username");
                    Toast.makeText
                            (this, "Username already exists", Toast.LENGTH_SHORT).show();
                    userExists = true;
                    break;
                }
            }

            if (!userExists) {
                byte[] salt = PasswordHandler.generateSalt();
                password = PasswordHandler.securePassword(password.toCharArray(), salt);

                int userCount = getIntent().getIntExtra("user_count", 0);
                String newCount = String.valueOf(userCount + 1);

                userTable.child(newCount).child("username").setValue(username);
                userTable.child(newCount).child("password").setValue(password);
                userTable.child(newCount).child("dateCreated").setValue(todayString);
                userTable.child(newCount).child("salt").setValue(PasswordHandler.bytesToHexString(salt));

                registerBtnSound.start();
                Toast.makeText(this, "Registration Complete", Toast.LENGTH_SHORT).show();
                finish();

            }
        } else {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
        }
    }

    public void showAgreement(View view) {

        AsyncTask<String, Void, String> task = new AsyncTask<String, Void, String>() {
            @Override
            protected String doInBackground(String... urls) {
                try {
                    URL url = new URL(urls[0]);
                    HttpsURLConnection connection = (HttpsURLConnection)url.openConnection();
                    int responseCode = connection.getResponseCode();
                    if (responseCode == HttpsURLConnection.HTTP_OK) {
                        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                        StringBuilder buffer = new StringBuilder();
                        String line;
                        while ((line = reader.readLine()) != null) {
                            buffer.append(line);
                            buffer.append("\r\n");
                        }
                        return buffer.toString();
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

                return null;
            }

            @Override
            protected void onPostExecute(String s) {
                Intent intent = new Intent(getApplicationContext(), License_agrmnt.class);
                intent.putExtra("license_agreement_key", s);
                startActivity(intent);
            }
        };
        task.execute("https://www.gnu.org/licenses/gpl.txt");

    }
}

package ca.uoit.flip_flop.flipflop;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class LoginActivity extends AppCompatActivity {

    private EditText usernameField;
    private EditText passwordField;
    private Button loginBtn;
    private Button cancelBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usernameField = (EditText)findViewById(R.id.username_login);
        passwordField = (EditText)findViewById(R.id.password_login);
        loginBtn = (Button)findViewById(R.id.login_button);
        cancelBtn = (Button)findViewById(R.id.login_cancel_button);

    }

    public void cancelLogin(View view){finish();}

    public void doLogin(View view) {}
}

package ca.uoit.flip_flop.flipflop;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class License_agrmnt extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_license_agreement);
        Intent intent = getIntent();
        String license = intent.getExtras().getString("license_agreement_key");
        ((TextView)findViewById(R.id.license)).setText(license);
    }
    public void close(View view){
        finish();
    }
}

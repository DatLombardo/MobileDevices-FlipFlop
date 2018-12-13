package ca.uoit.flip_flop.flipflop;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.TreeMap;

public class AddPostActivity extends AppCompatActivity implements LocationListener {
    private static final int PERMISSION_REQUEST_LOCATION = 100;

    private static final int LOCATION_REQUEST_DELAY = 2500;
    private static final int LOCATION_REQUEST_DISTANCE = 25; // km

    public EditText postTitle;
    public EditText postContent;
    public Button cancelBtn;
    public Button postBtn;
    private int postCount;
    protected DatabaseReference postTable;

    private int userId;

    private LocationManager locationManager;
    private String cityLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);

        userId = getIntent().getIntExtra("user_id", 0);

        cancelBtn = (Button)findViewById(R.id.cancelBtn);
        postBtn = (Button)findViewById(R.id.postBtn);
        postTitle = (EditText)findViewById(R.id.add_post_title);
        postContent = (EditText)findViewById(R.id.add_post_content);
        postTable = FirebaseDatabase.getInstance().getReference().child("Posts");

        postTable.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot userSnapshot) {
                // get all of the children at this level.
                Iterable<DataSnapshot> posts = userSnapshot.getChildren();

                for (DataSnapshot currPost : posts) {
                    postCount = Integer.parseInt(currPost.getKey());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
    }

    /**
     * addPost
     * Submits post to database
     *
     * @param view
     */
    public void addPost(View view){
        String postIdStr = Integer.toString(postCount + 1);
        String postTitleStr = postTitle.getText().toString();
        String postContentStr = postContent.getText().toString();

        TreeMap<String, Object> map = new TreeMap<>();
        map.put("title", postTitleStr);
        map.put("contents", postContentStr);
        map.put("reputation", 0);
        map.put("user_id", userId);
        map.put("location", cityLocation);
        postTable.child(postIdStr).setValue(map);
        Toast.makeText(this, "Post submitted", Toast.LENGTH_SHORT).show();
        finish();
    }

    /**
     * cancelPost
     * Returns to previous screen
     *
     * @param view
     */
    public void cancelPost(View view) {finish();}

    @Override
    protected void onResume() {
        super.onResume();
        verifyGeolocationPermission();
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopLocationUpdates();
    }

    private void verifyGeolocationPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            String[] perms = new String[] { Manifest.permission.ACCESS_FINE_LOCATION };
            ActivityCompat.requestPermissions(this, perms, PERMISSION_REQUEST_LOCATION);
        } else {
            requestLocationUpdates();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_LOCATION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // check if geolocation is enabled in settings
                if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    // Request location updates
                    requestLocationUpdates();
                } else {
                    // Show the settings app to let the user enable it
                    String locationSettings = Settings.ACTION_LOCATION_SOURCE_SETTINGS;
                    Intent enableGeoloc = new Intent(locationSettings);
                    startActivity(enableGeoloc);
                }
            }
        }
    }

    private void requestLocationUpdates() {
        try {
            // Get last known location if it is available
            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (location != null)
                onLocationChanged(location);

            // Get location updates using the GPS
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                    LOCATION_REQUEST_DELAY, LOCATION_REQUEST_DISTANCE, this);
        } catch (SecurityException ex) {
            ex.printStackTrace();

            // Notify user that this application requires location permission
            Toast.makeText(this, "Needs location permissions", Toast.LENGTH_LONG).show();
        }
    }

    private void stopLocationUpdates() {
        try {
            locationManager.removeUpdates(this);
        } catch (SecurityException ex) {
            ex.printStackTrace();

            // Notify user that this application requires location permission
            Toast.makeText(this, "Needs location permissions", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        // Remember the coordinates for the map activity's backup location
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();

        if (Geocoder.isPresent()) {
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());

            try {
                List<Address> results = geocoder.getFromLocation(latitude, longitude, 1);

                // Get first result
                if (results.size() > 0) {
                    Address address = results.get(0);

                    cityLocation = address.getLocality();
                }
            } catch (IOException ex) {
                ex.printStackTrace();

                // Notify user that this application requires location permission
                Toast.makeText(this, "Unable to geocode!", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.i("ShowLocation", "Provider (" + provider + ") status changed: " + status);
    }

    @Override
    public void onProviderEnabled(String provider) {
        Log.i("ShowLocation", "Provider enabled: " + provider);
    }

    @Override
    public void onProviderDisabled(String provider) {
        Log.i("ShowLocation", "Provider disabled: " + provider);
    }
}

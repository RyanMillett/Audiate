package edu.orangecoastcollege.cs273.rmillett.audiate;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * The <code>GoogleMapsActivity</code> allows the user to find their place
 * on the map and from that place play the ratio by getting their current
 * latitude and longitude.
 *
 * @author bwegener
 * @version 1.0
 *          <p>
 *          Created by Brian Wegener (with help from Ryan Millett) on 11/28/2017
 */
public class GoogleMapsActivity extends AppCompatActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    /**
     * This gets the rough location from the user.
     */
    public static final int COARSE_LOCATION_REQUEST_CODE = 100;

    private ChordScale mChordScale = new ChordScale("myLocationSound");

    private SoundObjectPlayer mSoundObjectPlayer;

    private GoogleMap mMap;

    // Google API client is "fused" services for all apps on the device (location, maps, play store)
    private GoogleApiClient mGoogleApiClient;

    // Last Location is the last latitude and longitude reported
    private Location mLastLocation;

    // Location requests are made every x seconds
    private LocationRequest mLocationRequest;

    /**
     * This adds a chord member, instantiates a soundObjectPlayer, builds the GoogleAPIClient,
     * then requests a location, as well as setting up the mapFragment in the layout.
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_maps);

        // mChordScale.addChordMember(new Note("fundamental"));

        // mChordScale.addChordMember(new Note("interval"));

        mSoundObjectPlayer = new SoundObjectPlayer();

        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

        mLocationRequest = mLocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY)
                .setInterval(30 * 1000)
                .setFastestInterval(1 * 1000);


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.googleMapsFragment);
        mapFragment.getMapAsync(this);
    }

    /**
     * This connects the user to the Google API client whenever the app is started.
     */
    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    /**
     * This disconnects the user from the Google API client whenever the app is stopped.
     */
    @Override
    protected void onStop() {
        super.onStop();
        mGoogleApiClient.disconnect();
    }

    /**
     * This gets the map ready.
     *
     * @param googleMap
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
    }

    /**
     * This handles the new location that the user might be in, by checking for the last
     * latitude and longitude and updating if different.
     *
     * @param newLocation
     */
    private void handleNewLocation(Location newLocation) {
        mLastLocation = newLocation;
        mMap.clear();

        LatLng myCoordinate = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
        mMap.addMarker(new MarkerOptions()
                .position(myCoordinate)
                .title("Current Location")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.my_marker)));
        CameraPosition cameraPosition = new CameraPosition.Builder().target(myCoordinate).zoom(15.0f).build();
        CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
        mMap.moveCamera(cameraUpdate);
    }

    /**
     * The <code>onConnected</code> method checks to make sure that everything is connected
     * properly and asks to see if the permissions are done.
     *
     * @param bundle
     */
    @Override
    public void onConnected(@Nullable Bundle bundle) {

        // Get the last location from Google Services
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // DONE: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

            // Don't have either COARSE or FINE permissions, so request them:
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, COARSE_LOCATION_REQUEST_CODE);
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);

        mLastLocation = LocationServices.FusedLocationApi
                .getLastLocation(mGoogleApiClient);

        handleNewLocation(mLastLocation);
    }

    /**
     * The <code>onRequestPermissionsResult</code> sends a request code to the user
     * to see if they can have access to the user's fine / coarse location.
     * If the user rejects the request then the latitude and longitude are both
     * set to 0.0, or the middle of the ocean.
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == COARSE_LOCATION_REQUEST_CODE) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                mLastLocation = new Location("");
                mLastLocation.setLatitude(0.0);
                mLastLocation.setLongitude(0.0);
            } else {
                LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
                mLastLocation = LocationServices.FusedLocationApi
                        .getLastLocation(mGoogleApiClient);
            }
            handleNewLocation(mLastLocation);
        }
    }

    /**
     * This is what happens when the connection is suspended, aka nothing.
     *
     * @param i
     */
    @Override
    public void onConnectionSuspended(int i) {

    }

    /**
     * This is what happens when the connection fails, we log an error message letting
     * the programmer know that it was because of the connection failing that the code
     * crashed.
     *
     * @param connectionResult
     */
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.e("Audiate", "Connection to Location Services failed: " + connectionResult.getErrorMessage());
    }

    /**
     * This is what happens when the location changes, it handles the new location.
     *
     * @param location
     */
    @Override
    public void onLocationChanged(Location location) {
        handleNewLocation(location);
    }

    /**
     * This plays the location by getting the interval from the ChordScale
     * and getting the pitch frequency by multiplying the dividend of mLastLocation.getLongitude
     * divided by mLastLocation.getLatitude.
     *
     * @param view
     */
    public void playLocation(View view) {
        // Calls onLocationChanged
        onLocationChanged(mLastLocation);

        // creates a new Chord Scale with a new Chord Member.

        if (mLastLocation.getLongitude() > mLastLocation.getLatitude())
            mChordScale.getChordMemberAtPos(1).setRatio(Music.convertDecimalToRatio(mLastLocation.getLongitude() / mLastLocation.getLatitude()));
        else
            mChordScale.getChordMemberAtPos(1).setRatio(Music.convertDecimalToRatio(mLastLocation.getLatitude() / mLastLocation.getLongitude()));


        // Adds notes to the mChordScale
        // mChordScale.addChordMember(new Note("interval", mChordScale.getChordMemberAtPos(1).getPitchFrequency() * interval));
        // mChordScale.addChordMember(new Note("interval", mChordScale.getChordMemberAtPos(2).getPitchFrequency() * interval));
        // mChordScale.addChordMember(new Note("interval", mChordScale.getChordMemberAtPos(3).getPitchFrequency() * interval));


        Log.i("Google Maps Activity", ("frequency of the interval" + mChordScale.getChordMemberAtPos(1).getPitchFrequency()));

        TextView logTV = (TextView) findViewById(R.id.logITV);

        logTV.setText("Frequency of the interval: " + (String.valueOf(Math.abs(mChordScale.getChordMemberAtPos(1).getPitchFrequency()))));

        // Sets the PlayBackMode to an Arpeggiator
        mChordScale.setPlayBackMode(ChordScale.PLAYBACK_MODE_CHORDSCALE_BLOCK_CLUSTER);

        // Sets the duration in milliseconds for how long each note plays
        mChordScale.setDurationMilliseconds(500);

        // plays the sound object with the chord scale.
        mSoundObjectPlayer.playSoundObject(mChordScale);
    }

}

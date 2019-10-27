package kalong.loo;

/*import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

//import android.support.annotation.NonNull;
//import android.support.annotation.Nullable;
//import android.support.v7.app.AppCompatActivity;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback,
        GoogleMap.OnInfoWindowClickListener {

    public void onMapReady (GoogleMap googleMap) {
        Toast.makeText(this, "Map is Ready", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "onMapReady: Map is Ready!");
        myMap = googleMap;

        if(permissionGranted){
            getDeviceLocation(googleMap);
            if(ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED){
                return;
            }
            myMap.setMyLocationEnabled(true);
            myMap.setOnInfoWindowClickListener(this);

            //Soda Hall Test
            googleMap.addMarker(new MarkerOptions()
                    .position(new LatLng(37.8756, -122.2588))
                    .visible(true)
                    .alpha(1.0f)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.mymarker))
                    .snippet("Determine Route to Soda Hall ?")
                    .title("Soda Hall"));
            //Wheeler Hall Test
            googleMap.addMarker(new MarkerOptions()
                    .position(new LatLng(37.8713, -122.2591))
                    .visible(true)
                    .alpha(1.0f)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.mymarker))
                    .snippet("Determine Route to Wheeler Hall ?")
                    .title("Wheeler Hall"));
            //Sather Tower Test
            googleMap.addMarker(new MarkerOptions()
                    .position(new LatLng(37.8721, -122.2578))
                    .visible(true)
                    .alpha(1.0f)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.mymarker))
                    .snippet("Determine Route to Sather Tower ?")
                    .title("Sather Tower"));
        }

    }
    private static final String TAG = "MapActivity";
    //private GeoApiContext mGeoApiContext;

    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COURSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    private static final float DEFAULT_ZOOM = 15f;
    private Location mUserPosition;
    private GoogleMap currMap;
    private String customLocation;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private boolean permissionGranted = false;
    private GoogleMap myMap;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //creates the map (based on the layout in activity_map
        setContentView(R.layout.activity_map);
        getLocationPermission();
        Button back = findViewById(R.id.button);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent backIntent = new Intent(MapActivity.this, HomeScreen.class);
                startActivity(backIntent);
            }
        });
    }
    private void button(final Location curr){
        Button mapButton = (Button) findViewById(R.id.btnNext);
        mapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Navigates to map on click.
                Intent intent = new Intent(MapActivity.this, addToiletActivity.class);
                String lat = Double.toString(curr.getLatitude());
                String lon = Double.toString(curr.getLongitude());
                intent.putExtra("lat", lat);
                intent.putExtra("lon", lon);
                //goes to Map
                startActivity(intent);

            }
        });
    }
    private void getDeviceLocation(final GoogleMap googleMap){
        Log.d(TAG, "getDeviceLocation: Gets device current location.");
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        try{
            if(permissionGranted){
                //when permission is given
                final Task location = mFusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if(task.isSuccessful()){
                            Log.d(TAG, "onComplete: Found Location!");
                            Location currentLoc = (Location) task.getResult();
                            setMap(googleMap);
                            moveCamera(new LatLng(currentLoc.getLatitude(),currentLoc.getLongitude()), DEFAULT_ZOOM);

                            button(currentLoc);

                        } else {
                            Log.d(TAG, "onComplete: Current Location is Null");
                            Toast.makeText(MapActivity.this, "Unable to Get Current Location", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        } catch (SecurityException e) {
            Log.e(TAG, "getDeviceLocation: Security Exception: " + e.getMessage());
        }
    }

    private void moveCamera(LatLng latLng, float zoom){
        Log.d(TAG, "moveCamera: Moving Camera to: latitude: " + latLng.latitude + ", longitude: " + latLng.longitude);
        myMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
    }

    private void initMap(){
        Log.d(TAG, "initMap: Initializing Map");
        final SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

        mapFragment.getMapAsync(MapActivity.this);

    }

    private void getLocationPermission(){
        Log.d(TAG, "getLocationPermission: Getting Location Permision");
        //checks for permission
        String[] permission = {Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION};
        //checks if permission is granted
        if(ContextCompat.checkSelfPermission(this.getApplicationContext(),
                FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            if(ContextCompat.checkSelfPermission(this.getApplicationContext(),
                    COURSE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                permissionGranted = true;
                initMap();
            } else {
                //asks for permission
                ActivityCompat.requestPermissions(this, permission, LOCATION_PERMISSION_REQUEST_CODE);
            }

        } else {
            //asks for permission
            ActivityCompat.requestPermissions(this, permission, LOCATION_PERMISSION_REQUEST_CODE);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        permissionGranted = false;

        switch (requestCode) {
            case LOCATION_PERMISSION_REQUEST_CODE: {
                //checks if granted
                if(grantResults.length > 0){
                    for(int i = 0; i < grantResults.length; i++){
                        if (grantResults[i] != PackageManager.PERMISSION_GRANTED){
                            Log.d(TAG, "onRequestPermissionsResult: Permission Failed");
                            permissionGranted = false;
                            return;
                        }
                    }
                    Log.d(TAG, "onRequestPermissionsResult: Permission Granted");
                    permissionGranted = true;
                    //initialize map here
                    initMap();
                }
            }
        }
    }

    @Override
    public void onInfoWindowClick(final Marker marker){
        marker.hideInfoWindow();
        //final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        Intent intentList = new Intent(MapActivity.this, ListReview.class);
        startActivity(intentList);
    }

    private void setMap(GoogleMap g){
        currMap = g;
    }

    public GoogleMap getCurrMap (){
        return currMap;
    }

    private void setLoc(Location loc){
        mUserPosition = loc;
    }

    public Location getLoc(){
        return mUserPosition;
    }


} */

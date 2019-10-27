package kalong.loo;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

//import android.support.annotation.NonNull;
//import android.support.annotation.Nullable;
//import android.support.v7.app.AppCompatActivity;

public class InitialMap extends AppCompatActivity implements OnMapReadyCallback,
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
    private static final String TAG = "InitialMap";
    //private GeoApiContext mGeoApiContext;

    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COURSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    private static final float DEFAULT_ZOOM = 15f;
    private Location mUserPosition;
    private GoogleMap currMap;
    public String customLocation = "";
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private boolean permissionGranted = false;
    private GoogleMap myMap;
    private String dir;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //creates the map (based on the layout in activity_map
        setContentView(R.layout.activity_initial_map);
        getLocationPermission();
        Button back = findViewById(R.id.button);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent backIntent = new Intent(InitialMap.this, HomeScreen.class);
                startActivity(backIntent);
            }
        });
    }
    private void button(final Location curr){
        Button mapButton = (Button) findViewById(R.id.btnNext2);
        mapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Navigates to map on click.
                Intent intent = new Intent(InitialMap.this, addToiletActivity.class);
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
                            setLoc(currentLoc);
                            moveCamera(new LatLng(currentLoc.getLatitude(),currentLoc.getLongitude()), DEFAULT_ZOOM);
                            //FIXME
                            button(currentLoc);
                            Intent intent = getIntent();
                            setDir(intent.getStringExtra("dir"));
                            DatabaseReference location = FirebaseDatabase.getInstance().getReference();
                            location.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    for (DataSnapshot child : dataSnapshot.getChildren()){
                                        String temp = child.child("Name").getValue().toString();
                                        String curname = "";
                                        for(int i = 0; i < temp.length(); i++){
                                            if(Character.isLetter(temp.charAt(i))){
                                                curname+=temp.charAt(i);
                                            } else {
                                                break;
                                            }

                                        }
                                        try {
                                            Double currLat = Double.parseDouble(child.child("Latitude").getValue().toString());
                                            Double currLon = Double.parseDouble(child.child("Longitude").getValue().toString());
                                            addLoo(curname, currLat, currLon);
                                        } catch (NullPointerException e) {
                                            continue;
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });

                        } else {
                            Log.d(TAG, "onComplete: Current Location is Null");
                            Toast.makeText(InitialMap.this, "Unable to Get Current Location", Toast.LENGTH_SHORT).show();
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

        mapFragment.getMapAsync(InitialMap.this);

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
        Intent intent = new Intent(InitialMap.this, ReviewList.class);
        String lat = Double.toString(marker.getPosition().latitude);
        String lon = Double.toString(marker.getPosition().longitude);
        intent.putExtra("lat", lat);
        intent.putExtra("lon", lon);
        intent.putExtra("dir",getDir());

        startActivity(intent);
        /*
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(marker.getSnippet())
                .setCancelable(true)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        String latitude = String.valueOf(marker.getPosition().latitude);
                        String longitude = String.valueOf(marker.getPosition().longitude);
                        Uri gmmIntentUri = Uri.parse("google.navigation:q=" + latitude + "," + longitude);
                        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                        mapIntent.setPackage("com.google.android.apps.maps");

                        try{
                            if (mapIntent.resolveActivity(InitialMap.this.getPackageManager()) != null) {
                                startActivity(mapIntent);
                            }
                        }catch (NullPointerException e){
                            Log.e(TAG, "onClick: NullPointerException: Couldn't open map." + e.getMessage() );
                            Toast.makeText(InitialMap.this, "Couldn't open map", Toast.LENGTH_SHORT).show();
                        }

                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
         */
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
    public void setDir(String d){
        dir = d;

    }
    public String getDir(){
        return dir;
    }


    public void addLoo(String name, Double lat, Double lon) {
        getCurrMap().addMarker(new MarkerOptions()
                .position(new LatLng(lat,lon))
                .visible(true)
                .alpha(1.0f)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.mymarker))
                .snippet("Determine Route to " + name + " ?") //Add custom later
                .title(name)); //Add custom later

    }

}

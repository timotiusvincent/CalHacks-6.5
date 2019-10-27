package kalong.loo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EdgeEffect;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Random;

public class addToiletActivity extends AppCompatActivity implements View.OnClickListener {
    public EditText mEditText;
    public String lat;
    public String lon;
    public String name;
    public String directory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_toilet);

        mEditText = (EditText) findViewById(R.id.editText);
        findViewById(R.id.button).setOnClickListener(this);
        Intent intent = getIntent();
        lat = intent.getStringExtra("lat");
        lon = intent.getStringExtra("lon");
    }

    @Override
    public void onClick(View v) {
        String theString = mEditText.getText().toString();
        name = theString;
        Intent intent = new Intent(this, InitialMap.class);

        Random r = new Random();
        int index = r.nextInt();
        String sIndex = Integer.toString(index);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference toilet = database.getReference().child("Location" + sIndex);
        final DatabaseReference tname = database.getReference().child("Location" + sIndex).child("Name");
        final DatabaseReference latitude = database.getReference().child("Location" + sIndex).child("Latitude");
        final DatabaseReference longitude = database.getReference().child("Location" + sIndex).child("Longitude");
        tname.setValue(name);
        latitude.setValue(lat);
        longitude.setValue(lon);
        //SEND TO DATABASE (LAT,LON,NAME)
        intent.putExtra("dir","Location"+sIndex);

        startActivity(intent);
    }
}
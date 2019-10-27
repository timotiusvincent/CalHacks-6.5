package kalong.loo;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class ReviewList extends AppCompatActivity {
    String lat, lon, dir;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton review = (FloatingActionButton) findViewById(R.id.addReviewButton);

        FloatingActionButton direction = findViewById(R.id.directionButton);
        Intent intent = getIntent();
        lat = intent.getStringExtra("lat");
        lon = intent.getStringExtra("lon");
        dir = intent.getStringExtra("dir");
        direction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String latitude = String.valueOf(lat);
                String longitude = String.valueOf(lon);
                Uri gmmIntentUri = Uri.parse("google.navigation:q=" + latitude + "," + longitude);
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);
            }
        });
        review.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToReviewScreen();
            }
        });

    }
    public void goToReviewScreen() {
        Intent intentLogin = new Intent(this, RatingScreen.class);
        intentLogin.putExtra("dir",dir);
        intentLogin.putExtra("lon",lon);
        intentLogin.putExtra("lat",lat);
        startActivity(intentLogin);
    }
}
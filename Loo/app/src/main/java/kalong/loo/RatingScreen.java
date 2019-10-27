package kalong.loo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.text.DecimalFormat;

public class RatingScreen extends AppCompatActivity {
    private Button imageButton;
    private Button cancelButton;
    private Button postButton;

    private ImageButton star1;
    private ImageButton star2;
    private ImageButton star3;
    private ImageButton star4;
    private ImageButton star5;

    String lat;
    String lon;
    String dir;

    String comment;

    String name = "";
    EditText commentField;


    /*FirebaseDatabase database = FirebaseDatabase.getInstance();
    final DatabaseReference mRef = database.getReference().child("Id").child("Rating").child("Anon");*/
    FirebaseDatabase database = FirebaseDatabase.getInstance();

    private static DecimalFormat df = new DecimalFormat("0.00");
    GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating_screen);
        star1 = findViewById(R.id.star1);
        star1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setCount(1);
                star1.setSelected(!star1.isPressed());

                if (star1.isPressed()) {
                    star1.setImageResource(R.drawable.star1highlight);
                }
                else {
                    star1.setImageResource(R.drawable.star1);
                }
            }
        });

        star2 = findViewById(R.id.star2);
        star2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setCount(2);
                star2.setSelected(!star2.isPressed());

                if (star2.isPressed()) {
                    star2.setImageResource(R.drawable.star2highlight);
                }
                else {
                    star2.setImageResource(R.drawable.star2);
                }
            }
        });

        star3 = findViewById(R.id.star3);
        star3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setCount(3);
                star3.setSelected(!star3.isPressed());

                if (star3.isPressed()) {
                    star3.setImageResource(R.drawable.star3highlight);
                }
                else {
                    star3.setImageResource(R.drawable.star3);
                }
            }
        });

        star4 = findViewById(R.id.star4);
        star4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setCount(4);
                star4.setSelected(!star4.isPressed());

                if (star4.isPressed()) {
                    star4.setImageResource(R.drawable.star4highlight);
                }
                else {
                    star4.setImageResource(R.drawable.star4);
                }
            }
        });

        star5 = findViewById(R.id.star5);
        star5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setCount(5);
                star5.setSelected(!star5.isPressed());

                if (star5.isPressed()) {
                    star5.setImageResource(R.drawable.star5highlight);
                }
                else {
                    star5.setImageResource(R.drawable.star5);
                }
            }
        });


        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);



        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if(account != null) {
            name = account.getGivenName() + account.getFamilyName();
        }

        Intent intent = getIntent();
        lat = intent.getStringExtra("lat");
        lon = intent.getStringExtra("lon");
        dir = intent.getStringExtra("dir");

        commentField = findViewById(R.id.CommentField);

        postButton = findViewById(R.id.postButton);
        postButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference location = FirebaseDatabase.getInstance().getReference();
                location.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        comment = commentField.getText().toString();
                        for (DataSnapshot child : dataSnapshot.getChildren()){
                            String temp = child.child("Name").getValue().toString();
                            Double currLat = Double.parseDouble(child.child("Latitude").getValue().toString());
                            Double currLon = Double.parseDouble(child.child("Longitude").getValue().toString());
                            Double pointLat = Double.parseDouble(lat);
                            Double pointLon = Double.parseDouble(lon);
                            boolean one = df.format(currLat).equals(df.format(pointLat));
                            boolean two = df.format(currLon).equals(df.format(pointLon));
                            if (one && two) {
                                final DatabaseReference rating = database.getReference().child(dir).child("Rating").child(name);
                                rating.setValue(getCount());
                            }
                            showToast(comment);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

        imageButton = findViewById(R.id.uploadButton);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openImageScreen();
            }
        });

        cancelButton = findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gobacktoScreen();
            }
        });




        hideNavigationBar();
    }
    @Override
    protected void onResume() {
        super.onResume();
        hideNavigationBar();
    }

    public void openImageScreen() {
        Intent imageFunction = new Intent(this, ImageFunction.class);
        startActivity(imageFunction);
    }

    public void gobacktoScreen() {
        Intent intentLogin = new Intent(this, ReviewList.class);
        startActivity(intentLogin);
    }

    private void showToast(String text) {
        Toast.makeText(RatingScreen.this,text,Toast.LENGTH_SHORT).show();
    }

    private void hideNavigationBar() {
        this.getWindow().getDecorView()
                .setSystemUiVisibility(
                        View.SYSTEM_UI_FLAG_FULLSCREEN |
                                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY |
                                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |
                                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                );
    }
    private int count;
    private int rating;

    public int getRating() {
        return rating;
    }

    public void setRating(int r) {rating = r; }

    public void setCount(int c) {
        count = c;
    }

    public int getCount() {
        return count;
    }
}
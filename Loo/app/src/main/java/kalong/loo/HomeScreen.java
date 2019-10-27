package kalong.loo;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.os.Bundle;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

public class HomeScreen extends AppCompatActivity {
    private Button settingButton;
    private Button profileButton;
    private static final String TAG = "HomeScreen";
    GoogleSignInClient mGoogleSignInClient;
    private static final int ERROR_DIALOG_REQUEST = 9001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);

        if(matchServices()){
            init(); //initializes if services works
        }

        settingButton = findViewById(R.id.settingButton);
        settingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openSettingScreen();
            }
        });

        profileButton = findViewById(R.id.profileButton);
        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openProfileScreen();
            }
        });
    }

    private void init() {
        Button mapButton = findViewById(R.id.mapButton);
        mapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Navigates to map on click.
                Intent intent = new Intent(HomeScreen.this, InitialMap.class);
                //goes to Map
                startActivity(intent);
            }
        });
    }

    private boolean matchServices() {
        Log.d(TAG, "matchServices: checking google services version");
        int avail = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(HomeScreen.this);
        if (avail == ConnectionResult.SUCCESS){
            //everything works, and map request is available.
            Log.d(TAG, "matchServices: Google Play Services Works!");
            return true;
        } else if (GoogleApiAvailability.getInstance().isUserResolvableError(avail)){
            //fixable error
            Log.d(TAG, "matchServices: an error occured, but it's fixable");
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(HomeScreen.this,avail,ERROR_DIALOG_REQUEST);
            dialog.show(); //Google shows us what error
        } else {
            Toast.makeText(this, "Map request is not available", Toast.LENGTH_SHORT).show();
        }
        return false;
    }


    public void openSettingScreen() {
        Intent intentLogin = new Intent(this, Settings.class);
        startActivity(intentLogin);
    }

    public void openProfileScreen() {
        Intent intentLogin = new Intent(this, Settings.class);
        startActivity(intentLogin);
    }
}
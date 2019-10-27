package kalong.loo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.view.View;
import android.widget.ImageButton;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity {
    private ImageButton buttonLogin;
    private ImageButton buttonRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonLogin = findViewById(R.id.login);
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openLoginScreen();
            }
        });

//        buttonRegister = findViewById(R.id.register);
//        buttonRegister.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                openRegisterScreen();
//            }
//        });

    }

    public void openLoginScreen() {
        Intent intentLogin = new Intent(MainActivity.this, SignInActivity.class);
        startActivity(intentLogin);
    }
}

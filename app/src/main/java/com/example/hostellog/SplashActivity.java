package com.example.hostellog;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_splash);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        getWindow().getAttributes().windowAnimations = R.style.Fade;

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent;
                SharedPreferences preferences = getSharedPreferences("MyPrefs1", MODE_PRIVATE);
                SharedPreferences preferences1 = getSharedPreferences("MyPrefs2", MODE_PRIVATE);
                boolean check = preferences.getBoolean("flag",false);
                boolean check1 = preferences1.getBoolean("flag1",false);
                if (check && check1){
                    intent = new Intent(SplashActivity.this, LocationCheckingActivity.class);
                }else {
                    intent = new Intent(SplashActivity.this, MainActivity.class);
                }

                startActivity(intent);
                finish();

            }
        },1000);
    }
}
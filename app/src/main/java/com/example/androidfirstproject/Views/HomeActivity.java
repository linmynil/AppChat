package com.example.androidfirstproject.Views;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.androidfirstproject.R;
import com.example.androidfirstproject.Views.Auth.PhoneInputActivity;
import com.google.firebase.auth.FirebaseAuth;

public class HomeActivity extends AppCompatActivity {
    private static int SPLASH_SCREEN = 5000;

    FirebaseAuth mAuth;

    Animation topAnim, bottomAnim;
    ImageView image;
    TextView logo, slogan;
    Button btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_home);

        //Animations
        topAnim = AnimationUtils.loadAnimation(this,R.anim.top_animation);
        bottomAnim = AnimationUtils.loadAnimation(this,R.anim.bottom_animation);

        // ánh xạ
        image = (ImageView) findViewById(R.id.imageView);
        logo = (TextView) findViewById(R.id.textView);
        slogan = (TextView) findViewById(R.id.textView2);
        btnLogout = (Button) findViewById(R.id.btnLogout);

        mAuth = FirebaseAuth.getInstance();

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                startActivity(new Intent(HomeActivity.this, PhoneInputActivity.class));
                finish();
            }
        });

        // Animate
        image.setAnimation(topAnim);
        logo.setAnimation(bottomAnim);
        slogan.setAnimation(bottomAnim);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(HomeActivity.this, PhoneInputActivity.class);
                startActivity(intent);
                finish();
            }
        },SPLASH_SCREEN);
    }
}
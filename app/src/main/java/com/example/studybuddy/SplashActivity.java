package com.example.studybuddy;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Apply saved theme preference at startup
        ThemeManager.applyTheme(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        LinearLayout splashContent = findViewById(R.id.splashContent);
        TextView tvLoadingDots     = findViewById(R.id.tvLoadingDots);

        Animation fadeIn  = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        Animation slideUp = AnimationUtils.loadAnimation(this, R.anim.slide_up);

        AnimationSet animSet = new AnimationSet(true);
        animSet.addAnimation(fadeIn);
        animSet.addAnimation(slideUp);
        animSet.setFillAfter(true);

        splashContent.setAlpha(1f);
        splashContent.startAnimation(animSet);

        Handler dotsHandler = new Handler();
        String[] dots = { "●  ○  ○", "●  ●  ○", "●  ●  ●", "○  ○  ○" };
        final int[] index = {0};
        Runnable dotsRunnable = new Runnable() {
            @Override
            public void run() {
                tvLoadingDots.setText(dots[index[0] % dots.length]);
                index[0]++;
                dotsHandler.postDelayed(this, 400);
            }
        };
        dotsHandler.post(dotsRunnable);

        new Handler().postDelayed(() -> {
            dotsHandler.removeCallbacksAndMessages(null);
            if (Session.isLoggedIn(SplashActivity.this)) {
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
            } else {
                startActivity(new Intent(SplashActivity.this, LoginActivity.class));
            }
            finish();
        }, 2500);
    }
}
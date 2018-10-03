package fr.wildcodeschool.mesclubs;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class SplashActivity extends AppCompatActivity {
    private static int SPLASH_TIME_OUT = 3000;

    ImageView run;
    Animation fade;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        run = (ImageView) findViewById(R.id.runningMan);
        fade = AnimationUtils.loadAnimation(this,R.anim.fade);

        run.setAnimation(fade);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent homeIntent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(homeIntent);
                finish();
            }
        },SPLASH_TIME_OUT);
    }
}
package fr.wildcodeschool.mesclubs;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class SplashActivity extends AppCompatActivity {
    private static int SPLASH_TIME_OUT = 4000;

    public boolean isPressed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        ImageView run = (ImageView) findViewById(R.id.runningMan);
        Animation fade = AnimationUtils.loadAnimation(this, R.anim.fade);
        final TextView enterApp = findViewById(R.id.text_go);

        run.setAnimation(fade);

        enterApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent homeIntent = new Intent(SplashActivity.this, MainActivity.class);
                isPressed = true;
                startActivity(homeIntent);
            }
        });

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!isPressed) {
                    enterApp.performClick();
                }
            }
        }, SPLASH_TIME_OUT);
    }
}

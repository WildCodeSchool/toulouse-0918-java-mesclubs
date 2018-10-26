package fr.wildcodeschool.mesclubs;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final ImageView imageMap = findViewById(R.id.map_icon);
        final ImageView imageList = findViewById(R.id.list_logo);
        ImageView imageLogo = findViewById(R.id.image_logo);

        Singleton singleton = Singleton.getInstance();
        singleton.loadClubs(new ClubListener() {
            @Override
            public void onResponse(boolean success) {
                if (!success) {
                    Toast.makeText(MainActivity.this, R.string.echec_connexion, Toast.LENGTH_SHORT).show();
                } else {
                    imageMap.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent goToMap = new Intent(MainActivity.this, MapsActivity.class);
                            MainActivity.this.startActivity(goToMap);
                            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                        }
                    });

                    imageList.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent goToList = new Intent(MainActivity.this, ListActivity.class);
                            MainActivity.this.startActivity(goToList);
                            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                        }
                    });
                }
            }
        });

        Animation fade = AnimationUtils.loadAnimation(this, R.anim.fade);
        imageMap.setAnimation(fade);
        imageList.setAnimation(fade);
        imageLogo.setAnimation(fade);
    }

    boolean shouldAllowBack = false;
    @Override
    public void onBackPressed() {
        if (!shouldAllowBack) {

        } else {
            super.onBackPressed();
        }
    }
}

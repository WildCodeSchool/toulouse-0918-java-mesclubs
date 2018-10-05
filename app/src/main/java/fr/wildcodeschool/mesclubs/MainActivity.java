package fr.wildcodeschool.mesclubs;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageView imageMap = findViewById(R.id.mapIcon);
        ImageView imageList = findViewById(R.id.listP);

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

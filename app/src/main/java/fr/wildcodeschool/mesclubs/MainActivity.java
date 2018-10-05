package fr.wildcodeschool.mesclubs;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {
    private DrawerLayout mDrawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageView imageMap = findViewById(R.id.image_map);
        ImageView imageList = findViewById(R.id.image_list);

        imageMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goToMap = new Intent(MainActivity.this, MapsActivity.class);
                MainActivity.this.startActivity(goToMap);
            }
        });

        imageList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goToList = new Intent(MainActivity.this, ListActivity.class);
                MainActivity.this.startActivity(goToList);
            }
        });


    }
}

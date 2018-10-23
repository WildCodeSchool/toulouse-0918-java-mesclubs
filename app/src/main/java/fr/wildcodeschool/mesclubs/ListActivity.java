package fr.wildcodeschool.mesclubs;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;

public class ListActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    NavigationView navigationView;
    private DrawerLayout mDrawerLayout;
    private Toolbar toolbar;
    private ListView mListTrip;
    private FirebaseAuth mAuth;
    ImageView photo;
    View hedeaderLayout;
    Menu menu;
    Menu menu2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        //Affichage du Menu
        this.configureToolBar();
        this.configureDrawerLayout();
        this.configureNavigationView();
        mAuth = FirebaseAuth.getInstance();
        hedeaderLayout = navigationView.getHeaderView(0);
        photo = hedeaderLayout.findViewById(R.id.image_header);
        getClubs();
    }

    public void getClubs() {
        ListAdapter adapter = new ListAdapter(ListActivity.this, Singleton.getInstance().getListClub());
        mListTrip = findViewById(R.id.list_club);
        mListTrip.setAdapter(adapter);
    }

    //GESTION DE L'AFFICHAGE DU MENU
    private void configureToolBar() {
        this.toolbar = (Toolbar) findViewById(R.id.toolbar4);
        setSupportActionBar(toolbar);
    }

    private void configureDrawerLayout() {
        this.mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }

    private void configureNavigationView() {
        this.navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        }

    @Override

    public void onBackPressed() {
        if (this.mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            this.mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
            }
    }

    public boolean onNavigationItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.connection:
                startActivity(new Intent(this, LoginActivity.class));
                break;

            case R.id.déconnection:
                //check si connecté
                //si oui logout
                //si non toast
                FirebaseAuth.getInstance().signOut();
                updateUI(null);
                Toast.makeText(ListActivity.this, "Vous n'êtes pas connecté", Toast.LENGTH_LONG);
                break;

            case R.id.profile:
                startActivity(new Intent(ListActivity.this,ProfilActivity.class));
                break;

            case R.id.map:
                startActivity(new Intent(this, MapsActivity.class));
                break;
        }
        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    void updateUI(FirebaseUser user) {
        if (user != null) {
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference userfirebase = database.getReference("User");
            userfirebase.child(mAuth.getUid()).child("picture").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String value = dataSnapshot.getValue(String.class);
                    if (value != null && !value.isEmpty()) {
                        Glide.with(ListActivity.this)
                                .load(value)
                                .apply(RequestOptions.circleCropTransform())
                                .into(photo);
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });
            TextView pseudo = hedeaderLayout.findViewById(R.id.et_pseudo);
            pseudo.setText(user.getEmail());
            menu = navigationView.getMenu();
            MenuItem target = menu.findItem(R.id.connection);
            target.setVisible(false);
        } else {
            menu = navigationView.getMenu();
            MenuItem target = menu.findItem(R.id.connection);
            target.setVisible(true);
            menu2 = navigationView.getMenu();
            MenuItem target2 = menu2.findItem(R.id.profile);
            target2.setVisible(false);
        }
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}

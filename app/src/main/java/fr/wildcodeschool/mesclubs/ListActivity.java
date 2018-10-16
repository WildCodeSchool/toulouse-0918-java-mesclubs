package fr.wildcodeschool.mesclubs;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        //Affichage du Menu
        this.configureToolBar();
        this.configureDrawerLayout();
        this.configureNavigationView();
        getClubs();
    }

    public void getClubs() {
        final ArrayList<Club> arrayClub = new ArrayList<>();

        //firebase
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference clubRef = database.getReference("club");
        clubRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot clubSnapshot : dataSnapshot.getChildren()) {
                    Club club = clubSnapshot.getValue(Club.class);//transform JSON en objet club
                    arrayClub.add(club);
                }
                ListAdapter adapter = new ListAdapter(ListActivity.this, arrayClub);
                mListTrip = findViewById(R.id.list_club);
                mListTrip.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
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
                startActivity(new Intent(this, ProfilActivity.class));
                break;
            case R.id.déconnection:
                //check si connecté
                //si oui logout
                //si non toast
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(ListActivity.this, "Vous n'êtes pas connecté", Toast.LENGTH_LONG);
            case R.id.liste:
                Toast.makeText(ListActivity.this, "Vous êtes déjà sur la liste.", Toast.LENGTH_LONG);
                break;
        }
        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
    //GESTION DE LA LISTE

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}

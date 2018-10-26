package fr.wildcodeschool.mesclubs;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
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

import static fr.wildcodeschool.mesclubs.Singleton.getImages;

public class ListActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    NavigationView navigationView;
    ImageView photo;
    View headerLayout;
    Menu connection;
    Menu profil;
    private DrawerLayout mDrawerLayout;
    Menu list;
    Menu filtreDis;
    Menu filtreHand;
    Menu filtreSport;
    Menu dec;
    private Toolbar toolbar;
    private ListView mListTrip;
    private FirebaseAuth mAuth;
    private ArrayList<Club> listClub = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        //Affichage du Menu
        this.configureToolBar();
        this.configureDrawerLayout();
        this.configureNavigationView();
        mAuth = FirebaseAuth.getInstance();
        headerLayout = navigationView.getHeaderView(0);
        photo = headerLayout.findViewById(R.id.image_header);
        getClubs();
        list = navigationView.getMenu();
        MenuItem target = list.findItem(R.id.liste);
        target.setVisible(false);
        filtreDis = navigationView.getMenu();
        MenuItem target2 = filtreDis.findItem(R.id.filtre_distance);
        target2.setVisible(false);
    }

    public void getClubs() {
        ListAdapter adapter = new ListAdapter(ListActivity.this, Singleton.getInstance().getListClub());
        mListTrip = findViewById(R.id.list_club);
        mListTrip.setAdapter(adapter);
    }

    //GESTION DE L'AFFICHAGE DU MENU
    private void configureToolBar() {
        this.toolbar = (Toolbar) findViewById(R.id.toolbar);
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
                FirebaseAuth.getInstance().signOut();
                updateUI(null);
                startActivity(new Intent(ListActivity.this, MainActivity.class));
                Toast.makeText(ListActivity.this, "Vous n'êtes pas connecté", Toast.LENGTH_LONG);
                break;

            case R.id.profile:
                startActivity(new Intent(ListActivity.this, ProfilActivity.class));
                break;

            case R.id.map:
                startActivity(new Intent(this, MapsActivity.class));
                break;


            case R.id.filtre_hand:
                getClubsByHand();
                break;

            case R.id.filtre_sport:
                final TextView tvFiltre = findViewById(R.id.tv_filtre);
                final TextView tvFiltreAlpinisme = findViewById(R.id.tv_filter_alpinisme);
                final TextView tvFiltreAviron = findViewById(R.id.tv_filter_aviron);
                final TextView tvFiltreCanoe = findViewById(R.id.tv_filter_canoe);
                final TextView tvFiltreCanyonisme = findViewById(R.id.tv_filter_canyonisme);
                final TextView tvFiltreCourse = findViewById(R.id.tv_filter_course);
                final TextView tvFiltreEcalade = findViewById(R.id.tv_filter_ecalade);
                final TextView tvFiltreNatation = findViewById(R.id.tv_filter_natation);
                final TextView tvFiltreVoile = findViewById(R.id.tv_filter_voile);
                final TextView tvFiltreRando = findViewById(R.id.tv_filter_randonne);
                final TextView tvFiltreSpeleo = findViewById(R.id.tv_filter_speleo);
                final TextView tvFiltreYoga = findViewById(R.id.tv_filter_yoga);
                final TextView tvFiltrePlonge = findViewById(R.id.tv_filter_plonge);
                final TextView tvNotFiltre = findViewById(R.id.tv_filtre_remove);

                showFilters(tvFiltre, tvFiltreAlpinisme, tvFiltreAviron, tvFiltreCanoe
                        , tvFiltreCanyonisme, tvFiltreCourse, tvFiltreEcalade, tvFiltreNatation
                        , tvFiltreVoile, tvFiltreRando, tvFiltreSpeleo, tvFiltreYoga, tvFiltrePlonge, tvNotFiltre);

                tvFiltreAlpinisme.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final String sport = tvFiltreAlpinisme.getText().toString();
                        getClubsBySport(sport);
                        dontShowFilters(tvFiltre, tvFiltreAlpinisme, tvFiltreAviron, tvFiltreCanoe
                                , tvFiltreCanyonisme, tvFiltreCourse, tvFiltreEcalade, tvFiltreNatation
                                , tvFiltreVoile, tvFiltreRando, tvFiltreSpeleo, tvFiltreYoga, tvFiltrePlonge, tvNotFiltre);
                    }
                });

                tvFiltreAviron.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final String sport = tvFiltreAviron.getText().toString();
                        getClubsBySport(sport);
                        dontShowFilters(tvFiltre, tvFiltreAlpinisme, tvFiltreAviron, tvFiltreCanoe
                                , tvFiltreCanyonisme, tvFiltreCourse, tvFiltreEcalade, tvFiltreNatation
                                , tvFiltreVoile, tvFiltreRando, tvFiltreSpeleo, tvFiltreYoga, tvFiltrePlonge, tvNotFiltre);
                    }
                });

                tvFiltreCanoe.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final String sport = tvFiltreCanoe.getText().toString();
                        getClubsBySport(sport);
                        dontShowFilters(tvFiltre, tvFiltreAlpinisme, tvFiltreAviron, tvFiltreCanoe
                                , tvFiltreCanyonisme, tvFiltreCourse, tvFiltreEcalade, tvFiltreNatation
                                , tvFiltreVoile, tvFiltreRando, tvFiltreSpeleo, tvFiltreYoga, tvFiltrePlonge, tvNotFiltre);
                    }
                });

                tvFiltreCanyonisme.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final String sport = tvFiltreCanyonisme.getText().toString();
                        getClubsBySport(sport);
                        dontShowFilters(tvFiltre, tvFiltreAlpinisme, tvFiltreAviron, tvFiltreCanoe
                                , tvFiltreCanyonisme, tvFiltreCourse, tvFiltreEcalade, tvFiltreNatation
                                , tvFiltreVoile, tvFiltreRando, tvFiltreSpeleo, tvFiltreYoga, tvFiltrePlonge, tvNotFiltre);
                    }
                });

                tvFiltreCourse.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final String sport = tvFiltreCourse.getText().toString();
                        getClubsBySport(sport);
                        dontShowFilters(tvFiltre, tvFiltreAlpinisme, tvFiltreAviron, tvFiltreCanoe
                                , tvFiltreCanyonisme, tvFiltreCourse, tvFiltreEcalade, tvFiltreNatation
                                , tvFiltreVoile, tvFiltreRando, tvFiltreSpeleo, tvFiltreYoga, tvFiltrePlonge, tvNotFiltre);
                    }
                });

                tvFiltreEcalade.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final String sport = tvFiltreEcalade.getText().toString();
                        getClubsBySport(sport);
                        dontShowFilters(tvFiltre, tvFiltreAlpinisme, tvFiltreAviron, tvFiltreCanoe
                                , tvFiltreCanyonisme, tvFiltreCourse, tvFiltreEcalade, tvFiltreNatation
                                , tvFiltreVoile, tvFiltreRando, tvFiltreSpeleo, tvFiltreYoga, tvFiltrePlonge, tvNotFiltre);
                    }
                });

                tvFiltreNatation.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final String sport = tvFiltreNatation.getText().toString();
                        getClubsBySport(sport);
                        dontShowFilters(tvFiltre, tvFiltreAlpinisme, tvFiltreAviron, tvFiltreCanoe
                                , tvFiltreCanyonisme, tvFiltreCourse, tvFiltreEcalade, tvFiltreNatation
                                , tvFiltreVoile, tvFiltreRando, tvFiltreSpeleo, tvFiltreYoga, tvFiltrePlonge, tvNotFiltre);
                    }
                });

                tvFiltreVoile.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final String sport = tvFiltreVoile.getText().toString();
                        getClubsBySport(sport);
                        dontShowFilters(tvFiltre, tvFiltreAlpinisme, tvFiltreAviron, tvFiltreCanoe
                                , tvFiltreCanyonisme, tvFiltreCourse, tvFiltreEcalade, tvFiltreNatation
                                , tvFiltreVoile, tvFiltreRando, tvFiltreSpeleo, tvFiltreYoga, tvFiltrePlonge, tvNotFiltre);
                    }
                });

                tvFiltreRando.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final String sport = tvFiltreRando.getText().toString();
                        getClubsBySport(sport);
                        dontShowFilters(tvFiltre, tvFiltreAlpinisme, tvFiltreAviron, tvFiltreCanoe
                                , tvFiltreCanyonisme, tvFiltreCourse, tvFiltreEcalade, tvFiltreNatation
                                , tvFiltreVoile, tvFiltreRando, tvFiltreSpeleo, tvFiltreYoga, tvFiltrePlonge, tvNotFiltre);
                    }
                });

                tvFiltreSpeleo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final String sport = tvFiltreSpeleo.getText().toString();
                        getClubsBySport(sport);
                        dontShowFilters(tvFiltre, tvFiltreAlpinisme, tvFiltreAviron, tvFiltreCanoe
                                , tvFiltreCanyonisme, tvFiltreCourse, tvFiltreEcalade, tvFiltreNatation
                                , tvFiltreVoile, tvFiltreRando, tvFiltreSpeleo, tvFiltreYoga, tvFiltrePlonge, tvNotFiltre);
                    }
                });

                tvFiltreYoga.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final String sport = tvFiltreYoga.getText().toString();
                        getClubsBySport(sport);
                        dontShowFilters(tvFiltre, tvFiltreAlpinisme, tvFiltreAviron, tvFiltreCanoe
                                , tvFiltreCanyonisme, tvFiltreCourse, tvFiltreEcalade, tvFiltreNatation
                                , tvFiltreVoile, tvFiltreRando, tvFiltreSpeleo, tvFiltreYoga, tvFiltrePlonge, tvNotFiltre);
                    }
                });

                tvFiltrePlonge.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final String sport = tvFiltrePlonge.getText().toString();
                        getClubsBySport(sport);
                        dontShowFilters(tvFiltre, tvFiltreAlpinisme, tvFiltreAviron, tvFiltreCanoe
                                , tvFiltreCanyonisme, tvFiltreCourse, tvFiltreEcalade, tvFiltreNatation
                                , tvFiltreVoile, tvFiltreRando, tvFiltreSpeleo, tvFiltreYoga, tvFiltrePlonge, tvNotFiltre);
                    }
                });

                tvNotFiltre.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getClubs();
                        dontShowFilters(tvFiltre, tvFiltreAlpinisme, tvFiltreAviron, tvFiltreCanoe
                                , tvFiltreCanyonisme, tvFiltreCourse, tvFiltreEcalade, tvFiltreNatation
                                , tvFiltreVoile, tvFiltreRando, tvFiltreSpeleo, tvFiltreYoga, tvFiltrePlonge, tvNotFiltre);
                    }
                });
        }
        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    public void showFilters(TextView tvFiltre, TextView tvFiltreAlpinisme, TextView tvFiltreAviron, TextView tvFiltreCanoe
            , TextView tvFiltreCanyonisme, TextView tvFiltreCourse, TextView tvFiltreEcalade, TextView tvFiltreNatation
            , TextView tvFiltreVoile, TextView tvFiltreRando, TextView tvFiltreSpeleo, TextView tvFiltreYoga
            , TextView tvFiltrePlonge, TextView tvNotFiltre) {
        tvFiltre.setVisibility(View.VISIBLE);
        tvFiltreAlpinisme.setVisibility(View.VISIBLE);
        tvFiltreAviron.setVisibility(View.VISIBLE);
        tvFiltreCanoe.setVisibility(View.VISIBLE);
        tvFiltreCanyonisme.setVisibility(View.VISIBLE);
        tvFiltreCourse.setVisibility(View.VISIBLE);
        tvFiltreEcalade.setVisibility(View.VISIBLE);
        tvFiltreNatation.setVisibility(View.VISIBLE);
        tvFiltreVoile.setVisibility(View.VISIBLE);
        tvFiltreRando.setVisibility(View.VISIBLE);
        tvFiltreSpeleo.setVisibility(View.VISIBLE);
        tvFiltreYoga.setVisibility(View.VISIBLE);
        tvFiltrePlonge.setVisibility(View.VISIBLE);
        tvNotFiltre.setVisibility(View.VISIBLE);
    }

    public void dontShowFilters(TextView tvFiltre, TextView tvFiltreAlpinisme, TextView tvFiltreAviron, TextView tvFiltreCanoe
            , TextView tvFiltreCanyonisme, TextView tvFiltreCourse, TextView tvFiltreEcalade, TextView tvFiltreNatation
            , TextView tvFiltreVoile, TextView tvFiltreRando, TextView tvFiltreSpeleo, TextView tvFiltreYoga
            , TextView tvFiltrePlonge, TextView tvNotFiltre) {
        tvFiltre.setVisibility(View.INVISIBLE);
        tvFiltreAlpinisme.setVisibility(View.INVISIBLE);
        tvFiltreAviron.setVisibility(View.INVISIBLE);
        tvFiltreCanoe.setVisibility(View.INVISIBLE);
        tvFiltreCanyonisme.setVisibility(View.INVISIBLE);
        tvFiltreCourse.setVisibility(View.INVISIBLE);
        tvFiltreEcalade.setVisibility(View.INVISIBLE);
        tvFiltreNatation.setVisibility(View.INVISIBLE);
        tvFiltreVoile.setVisibility(View.INVISIBLE);
        tvFiltreRando.setVisibility(View.INVISIBLE);
        tvFiltreSpeleo.setVisibility(View.INVISIBLE);
        tvFiltreYoga.setVisibility(View.INVISIBLE);
        tvFiltrePlonge.setVisibility(View.INVISIBLE);
        tvNotFiltre.setVisibility(View.INVISIBLE);
    }

    public void getClubsBySport(String sport) {
        //firebase
        listClub.clear();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference clubRef = database.getReference("club");
        clubRef.orderByChild("sport").equalTo(sport)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot clubSnapshot : dataSnapshot.getChildren()) {
                            Club club = clubSnapshot.getValue(Club.class);//transform JSON en objet club
                            club.setImage(getImages(club.getSport()));
                            club.setId(clubSnapshot.getKey());
                            listClub.add(club);
                        }
                        ListAdapter adapter = new ListAdapter(ListActivity.this, listClub);
                        mListTrip = findViewById(R.id.list_club);
                        mListTrip.setAdapter(adapter);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
    }

    public void getClubsByHand() {
        listClub.clear();
        //firebase
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference clubRef = database.getReference("club");
        clubRef.orderByChild("handicapped").equalTo(true)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot clubSnapshot : dataSnapshot.getChildren()) {
                            Club club = clubSnapshot.getValue(Club.class);//transform JSON en objet club
                            club.setImage(getImages(club.getSport()));
                            club.setId(clubSnapshot.getKey());
                            listClub.add(club);
                        }
                        ListAdapter adapter = new ListAdapter(ListActivity.this, listClub);
                        mListTrip = findViewById(R.id.list_club);
                        mListTrip.setAdapter(adapter);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
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
            TextView pseudo = headerLayout.findViewById(R.id.etPseudo);
            pseudo.setText(user.getEmail());
            connection = navigationView.getMenu();
            MenuItem target = connection.findItem(R.id.connection);
            target.setVisible(false);
        } else {
            connection = navigationView.getMenu();
            MenuItem target = connection.findItem(R.id.connection);
            target.setVisible(true);
            profil = navigationView.getMenu();
            MenuItem target2 = profil.findItem(R.id.profile);
            target2.setVisible(false);
            filtreHand = navigationView.getMenu();
            MenuItem target3 = filtreHand.findItem(R.id.filtre_hand);
            target3.setVisible(false);
            filtreSport = navigationView.getMenu();
            MenuItem target4 = filtreSport.findItem(R.id.filtre_sport);
            target4.setVisible(false);
            dec = navigationView.getMenu();
            MenuItem target6 = dec.findItem(R.id.déconnection);
            target6.setVisible(false);
        }
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}

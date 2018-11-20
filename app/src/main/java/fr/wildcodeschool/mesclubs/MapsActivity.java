package fr.wildcodeschool.mesclubs;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListPopupWindow;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

import static fr.wildcodeschool.mesclubs.Singleton.getImages;


public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, NavigationView.OnNavigationItemSelectedListener {

    final static int POPUP_POSITION_X = 0;
    final static int POPUP_POSITION_Y = 0;
    LocationManager mLocationManager = null;
    boolean moveCam = false;
    Location userLocation = null;
    NavigationView navigationView;
    ClipData.Item map;
    Menu connection;
    Menu profil;
    Menu carte;
    Menu filtreDis;
    Menu filtreHand;
    Menu filtreSport;
    Menu dec;
    private int MARKER_WIDTH = 100;
    private int MARKER_HEIGHT = 100;
    private FirebaseAuth mAuth;
    private GoogleMap mMap;
    private DrawerLayout mDrawerLayout;
    private Toolbar toolbar;
    private PopupWindow popUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        mAuth = FirebaseAuth.getInstance();
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        this.configureToolBar();
        this.configureDrawerLayout();
        this.configureNavigationView();
        carte = navigationView.getMenu();
        MenuItem target = carte.findItem(R.id.map);
        target.setVisible(false);
    }

    //GESTION DU MENU
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

    //ONBACK PRESS METHODE
    @Override

    public void onBackPressed() {

        if (this.mDrawerLayout.isDrawerOpen(GravityCompat.START)) {

            this.mDrawerLayout.closeDrawer(GravityCompat.START);
            connection = navigationView.getMenu();
            MenuItem target = connection.findItem(R.id.connection);
            target.setVisible(true);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.connection:
                startActivity(new Intent(this, LoginActivity.class));
                break;
            case R.id.déconnection:
                FirebaseAuth.getInstance().signOut();
                updateUI(null);
                startActivity(new Intent(MapsActivity.this, MainActivity.class));

                break;
            case R.id.liste:
                startActivity(new Intent(this, ListActivity.class));
                break;

            case R.id.profile:
                startActivity(new Intent(MapsActivity.this, ProfilActivity.class));
                break;

            case R.id.filtre_distance:
                mMap.clear();
                getClubsByDistance();
                break;

            case R.id.filtre_hand:
                mMap.clear();
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

    public void getClubs() {
        Singleton singleton = Singleton.getInstance();
        ArrayList<Club> clubs = singleton.getListClub();
        for (Club club : clubs) {
            Bitmap initialMarkerIcon = BitmapFactory.decodeResource(getResources(), club.getImage());
            Bitmap markerIcon = Bitmap.createScaledBitmap(initialMarkerIcon, MARKER_WIDTH, MARKER_HEIGHT, false);
            Marker marker = mMap.addMarker(new MarkerOptions().position(new LatLng(club.getLatitude(), club.getLongitude()))
                    .icon(BitmapDescriptorFactory.fromBitmap(markerIcon)));
            marker.setTag(club);
        }
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                popupBuilder(marker);
                return false;
            }
        });
    }

    public void getClubsBySport(String sport) {
        mMap.clear();
        //firebase
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference clubRef = database.getReference("club");
        clubRef.orderByChild("sport").equalTo(sport)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot clubSnapshot : dataSnapshot.getChildren()) {
                            Club club = clubSnapshot.getValue(Club.class);//transform JSON en objet club
                            club.setImage(getImages(club.getSport()));
                            Bitmap initialMarkerIcon = BitmapFactory.decodeResource(getResources(), club.getImage());
                            Bitmap markerIcon = Bitmap.createScaledBitmap(initialMarkerIcon, MARKER_WIDTH, MARKER_HEIGHT, false);
                            Marker marker = mMap.addMarker(new MarkerOptions().position(new LatLng(club.getLatitude(), club.getLongitude()))
                                    .icon(BitmapDescriptorFactory.fromBitmap(markerIcon)));
                            marker.setTag(club);
                        }
                        // generer les marqueurs a partir de la liste
                        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                            @Override
                            public boolean onMarkerClick(Marker marker) {
                                popupBuilder(marker);
                                return false;
                            }
                        });
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
    }

    public void getClubsByHand() {
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
                            Bitmap initialMarkerIcon = BitmapFactory.decodeResource(getResources(), club.getImage());
                            Bitmap markerIcon = Bitmap.createScaledBitmap(initialMarkerIcon, MARKER_WIDTH, MARKER_HEIGHT, false);
                            Marker marker = mMap.addMarker(new MarkerOptions().position(new LatLng(club.getLatitude(), club.getLongitude()))
                                    .icon(BitmapDescriptorFactory.fromBitmap(markerIcon)));
                            marker.setTag(club);
                        }
                        // generer les marqueurs a partir de la liste
                        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                            @Override
                            public boolean onMarkerClick(Marker marker) {
                                popupBuilder(marker);
                                return false;
                            }
                        });
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
    }

    public void getClubsByDistance() {
        final ArrayList<Club> clubList = new ArrayList<>();
        //firebase
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference clubRef = database.getReference("club");
        clubRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                clubList.clear();
                for (DataSnapshot clubSnapshot : dataSnapshot.getChildren()) {
                    Club club = clubSnapshot.getValue(Club.class);//transform JSON en objet club
                    club.setImage(getImages(club.getSport()));
                    Location distanceClub = new Location(LocationManager.GPS_PROVIDER);
                    distanceClub.setLatitude(club.getLatitude());
                    distanceClub.setLongitude(club.getLongitude());
                    if (userLocation != null) {
                        double distance = userLocation.distanceTo(distanceClub);
                        club.setDistance(distance);
                    }
                    clubList.add(club);
                }

                for (int i = 0; i < 9; i++) {
                    for (int j = i; j <= clubList.size() - 1; j++) {
                        if (clubList.get(j).getDistance() < clubList.get(i).getDistance()) {
                            Collections.swap(clubList, i, j);
                        }
                    }
                }

                for (int i = 0; i < 9; i++) {
                    Club club = clubList.get(i);
                    Bitmap initialMarkerIcon = BitmapFactory.decodeResource(getResources(), club.getImage());
                    Bitmap markerIcon = Bitmap.createScaledBitmap(initialMarkerIcon, MARKER_WIDTH, MARKER_HEIGHT, false);
                    Marker marker = mMap.addMarker(new MarkerOptions().position(new LatLng(club.getLatitude(), club.getLongitude()))
                            .icon(BitmapDescriptorFactory.fromBitmap(markerIcon)));
                    marker.setTag(club);
                }

                // generer les marqueurs a partir de la liste
                mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(Marker marker) {
                        popupBuilder(marker);
                        return false;
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    @SuppressLint("MissingPermission")
    private void initLocation() {

        getClubs();
        mMap.setMyLocationEnabled(true);

        //récupérartion dernier position connue
        FusedLocationProviderClient mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mFusedLocationClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    moveCameraOnUser(location);
                }
            }
        });

        //modification position utilisateur déplacement
        mLocationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        LocationListener locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                if (moveCam) {
                    moveCameraOnUser(location);
                    moveCam = true;
                }
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            public void onProviderEnabled(String provider) {
            }

            public void onProviderDisabled(String provider) {
            }
        };
        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0,
                0, locationListener);
        mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0,
                0, locationListener);
    }

    private void checkPermission() {
        // vérification de l'autorisation d'accéder à la position GPS
        if (ContextCompat.checkSelfPermission(MapsActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // l'autorisation n'est pas acceptée
            if (ActivityCompat.shouldShowRequestPermissionRationale(MapsActivity.this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                // l'autorisation a été refusée précédemment, on peut prévenir l'utilisateur ici
            } else {
                // l'autorisation n'a jamais été réclamée, on la demande à l'utilisateur
                ActivityCompat.requestPermissions(MapsActivity.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        100);
            }
        } else {
            initLocation();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 100: {
                // cas de notre demande d'autorisation
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    initLocation();
                } else {
                    // l'autorisation a été refusée :(
                    checkPermission();
                }
                return;
            }
        }
    }

    public void moveCameraOnUser(Location location) {

        LatLng userLatLong = new LatLng(location.getLatitude(), location.getLongitude());
        userLocation = location;
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(userLatLong, 13.5f));
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        checkPermission();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    public void updateUI(FirebaseUser user) {
        final View headerLayout = navigationView.getHeaderView(0);
        if (user != null) {
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference userfirebase = database.getReference("User");
            userfirebase.child(mAuth.getUid()).child("picture").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String value = dataSnapshot.getValue(String.class);
                    if (value != null && !value.isEmpty()) {
                        ImageView photo = headerLayout.findViewById(R.id.image_header);
                        Glide.with(MapsActivity.this)
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
            MenuItem target3 = filtreHand.findItem(R.id.filtre_distance);
            target3.setVisible(false);
            filtreSport = navigationView.getMenu();
            MenuItem target4 = filtreSport.findItem(R.id.filtre_sport);
            target4.setVisible(false);
            filtreDis = navigationView.getMenu();
            MenuItem target5 = filtreDis.findItem(R.id.filtre_hand);
            target5.setVisible(false);
            dec = navigationView.getMenu();
            MenuItem target6 = dec.findItem(R.id.déconnection);
            target6.setVisible(false);
        }
    }

    private void popupBuilder(Marker marker) {

        Display display = getWindowManager().getDefaultDisplay();

        Point size = new Point();
        display.getSize(size);
        int width = (int) Math.round(size.x * 0.8);

        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        final View popUpView = inflater.inflate(R.layout.item_marker, null);

        //creation fenetre popup
        boolean focusable = true;
        popUp = new PopupWindow(popUpView, width, ListPopupWindow.WRAP_CONTENT, focusable);

        //show popup
        popUp.showAtLocation(popUpView, Gravity.CENTER, POPUP_POSITION_X, POPUP_POSITION_Y);
        final Club club = (Club) marker.getTag();
        final TextView markerName = popUpView.findViewById(R.id.marker_name);
        ImageView markerImage = popUpView.findViewById(R.id.marker_image);
        ImageView markerHandicap = popUpView.findViewById(R.id.image_handicap);
        final TextView markerSport = popUpView.findViewById(R.id.text_sport);
        final TextView markeurWeb = popUpView.findViewById(R.id.text_web);
        final ImageView ivLike = popUpView.findViewById(R.id.iv_like);
        ImageView ivShare = popUpView.findViewById(R.id.image_share);
        ImageView markerItinerary = popUpView.findViewById(R.id.iv_itinerary);
        final TextView tvCounter = popUpView.findViewById(R.id.tv_counter);

        markerName.setText(club.getClubName());
        markerSport.setText(club.getSport());
        markeurWeb.setText(club.getWebsite());
        markerImage.setImageDrawable(MapsActivity.this.getResources().getDrawable(club.getImage()));
        ivLike.setImageDrawable(MapsActivity.this.getResources().getDrawable(R.drawable.like_off));
        tvCounter.setText(String.valueOf(club.getCounter()));

        if (club.isHandicapped()) {
            markerHandicap.setImageDrawable(MapsActivity.this.getResources().getDrawable(R.drawable.handicapicon));
        }

        //Bouton Share
        ivShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message = getString(R.string.sahreBody);
                String sport = markerSport.getText().toString();
                String clubName = markerName.getText().toString();
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                String shareText = message + " " + sport + " " + clubName;
                shareIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Look'n'Sport");
                shareIntent.putExtra(Intent.EXTRA_TEXT, shareText);
                startActivity(Intent.createChooser(shareIntent, "Share via"));
            }
        });

        //Bouton itinéraire
        markerItinerary.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                assert club != null;
                intent.setData(Uri.parse("http://maps.google.com/maps?.34&daddr=" + club.getLatitude() + "," + club.getLongitude()));
                startActivity(intent);
            }
        });

        //Click on like
        SharedPreferences sharedPref = MapsActivity.this.getSharedPreferences("clubid", Context.MODE_PRIVATE);
        boolean isLiked = sharedPref.getBoolean(club.getId(), false);
        if (isLiked) {
            ivLike.setImageDrawable(MapsActivity.this.getResources().getDrawable(R.drawable.like));
        } else {
            ivLike.setImageDrawable(MapsActivity.this.getResources().getDrawable(R.drawable.like_off));
        }

        ivLike.setTag(isLiked); // set favorite off
        ivLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isliked = ((boolean) ivLike.getTag());
                if (!isliked) {
                    ivLike.setImageDrawable(MapsActivity.this.getResources().getDrawable(R.drawable.like));
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    final DatabaseReference clubRef = database.getReference("club").child(club.getId());
                    clubRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Club thisClub = dataSnapshot.getValue(Club.class);
                            int counter = thisClub.getCounter();
                            counter++;
                            thisClub.setCounter(counter);
                            clubRef.setValue(thisClub);
                            tvCounter.setText(String.valueOf(thisClub.getCounter()));
                            likePreferences(dataSnapshot.getKey(), true);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                } else {
                    ivLike.setImageDrawable(MapsActivity.this.getResources().getDrawable(R.drawable.like_off));
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    final DatabaseReference clubRef = database.getReference("club").child(club.getId());
                    clubRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Club thisClub = dataSnapshot.getValue(Club.class);
                            int counter = thisClub.getCounter();
                            counter--;
                            thisClub.setCounter(counter);
                            clubRef.setValue(thisClub);
                            tvCounter.setText(String.valueOf(thisClub.getCounter()));
                 likePreferences(dataSnapshot.getKey(), false);
                            likePreferences(dataSnapshot.getKey(), false);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
                ivLike.setTag(!isliked);
            }
        });
    }

    public void likePreferences(String clubId, boolean isLiked) {
        SharedPreferences sharedPref = MapsActivity.this.getSharedPreferences("clubid", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(clubId, isLiked);
        editor.commit();
    }
}




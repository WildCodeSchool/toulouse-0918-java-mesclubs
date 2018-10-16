package fr.wildcodeschool.mesclubs;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, NavigationView.OnNavigationItemSelectedListener {

    LocationManager mLocationManager = null;
    boolean moveCam = false;

    NavigationView navigationView;
    ClipData.Item map;
    private int MARKER_WIDTH = 100;
    private int MARKER_HEIGHT = 100;

    private GoogleMap mMap;
    private DrawerLayout mDrawerLayout;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        this.configureToolBar();
        this.configureDrawerLayout();
        this.configureNavigationView();
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
        } else {
            super.onBackPressed();
        }
    }

    @Override

    public boolean onNavigationItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.connection:
                startActivity(new Intent(this, ProfilActivity.class));
                break;
            case R.id.déconnection:
                FirebaseAuth.getInstance().signOut();

                break;
            case R.id.liste:
                startActivity(new Intent(this, ListActivity.class));
                break;
        }
        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    public void getClubs() {
        //firebase
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference clubRef = database.getReference("club");
        clubRef.addListenerForSingleValueEvent(new ValueEventListener() {
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
                CustomMarkerAdapter customInfoWindow = new CustomMarkerAdapter(MapsActivity.this);
                mMap.setInfoWindowAdapter(customInfoWindow);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    public int getImages(String sport) {
        int image;

        switch (sport) {
            case "ALPINISME":
                image = R.drawable.alpinisme;
                break;

            case "AVIRON":
                image = R.drawable.aviron;
                break;
            case "CANOE-KAYAK":
                image = R.drawable.canoe;
                break;

            case "CANYONISME":
                image = R.drawable.canyon;
                break;
            case "COURSE A PIED":
            case "COURSE D'ORIENTATION":
            case "marche":
                image = R.drawable.course;
                break;
            case "ESCALADE":
                image = R.drawable.escalade;
                break;
            case "NATATION":
                image = R.drawable.natation;
                break;
            case "plongée":
                image = R.drawable.plonge;
                break;
            case "randonnée":
                image = R.drawable.rando;
                break;
            case "spéléologie":
                image = R.drawable.speleo;
                break;
            case "VOILE":
            case "planche à voile":
                image = R.drawable.voile;
                break;
            case "YOGA":
                image = R.drawable.yoga;
                break;
            default:
                image = R.drawable.ic_android_black_24dp;
        }
        return image;
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

        LatLng userLocation = new LatLng(location.getLatitude(), location.getLongitude());
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 15.0f));
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
}

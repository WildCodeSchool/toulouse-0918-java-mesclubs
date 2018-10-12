package fr.wildcodeschool.mesclubs;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;

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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    LocationManager mLocationManager = null;
    boolean moveCam = false;
    private GoogleMap mMap;
    private DrawerLayout mDrawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
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
                    Marker marker = mMap.addMarker(new MarkerOptions().position(new LatLng(club.getLatitude(), club.getLongitude()))
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE)));
                    club = getImages(club);
                    marker.setTag(club);
                    //TODO récup la photo a partir de l'adresse (streetview)
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

    public Club getImages(Club club) {
        String sport = club.getSport();
        switch (sport) {
            case "ALPINISME":
                club.setImage(R.drawable.alpinisme);
                return club;

            case "AVIRON":
                club.setImage(R.drawable.aviron);
                return club;

            case "CANOE_KAYAK":
                club.setImage(R.drawable.canoe);
                return club;

            case "CANYONISME":
                club.setImage(R.drawable.canyon);
                return club;

            case "COURSE A PIED":
            case "COURSE D'ORIENTATION":
            case "marche":
                club.setImage(R.drawable.course);
                return club;

            case "ESCALADE":
                club.setImage(R.drawable.escalade);
                return club;

            case "NATATION":
                club.setImage(R.drawable.natation);
                return club;

            case "plongée":
                club.setImage(R.drawable.plonge);
                return club;

            case "randonnée":
                club.setImage(R.drawable.rando);
                return club;

            case "spéléologie":
                club.setImage(R.drawable.speleo);
                return club;

            case "VOILE":
            case "planche à voile":
                club.setImage(R.drawable.voile);
                return club;

            case "YOGA":
                club.setImage(R.drawable.yoga);
                return club;
        }
        return club;
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
    public void onRequestPermissionsResult (int requestCode,
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

    public void moveCameraOnUser (Location location) {

        LatLng userLocation = new LatLng(location.getLatitude(), location.getLongitude());
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 15.0f));
    }

    @Override
    public void onMapReady (GoogleMap googleMap) {
        mMap = googleMap;

        checkPermission();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }
}

package fr.wildcodeschool.mesclubs;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
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

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {


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


    @SuppressLint("MissingPermission")
    private void initLocation() {
        initMarkers();

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

        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
    }

    private void initMarkers() {
        // création d'un marqueur d'exemple
        Marker wcs = mMap.addMarker(new MarkerOptions().position(new LatLng(43.5998979, 1.4431481)));
        Marker skiAlpin = mMap.addMarker(new MarkerOptions().position(new LatLng(43.604268, 1.441019))
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE)));
        Marker tuc = mMap.addMarker(new MarkerOptions().position(new LatLng(43.774297, 1.686036))
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
        Marker ascm = mMap.addMarker(new MarkerOptions().position(new LatLng(43.586849, 1.435147))
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)));
        Marker gym = mMap.addMarker(new MarkerOptions().position(new LatLng(43.590233, 1.436469))
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET)));
        Marker natation = mMap.addMarker(new MarkerOptions().position(new LatLng(43.60593, 1.453138))
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));

        //todo faire les dessins par sport
        // on crée les informations liées au marqueur
        MarkerInfos wcsInfos = new MarkerInfos("Wild Code School", "32 rue des marchards", R.drawable.ic_android_black_24dp);
        MarkerInfos skiAlpinInfo = new MarkerInfos("CLUB ALPIN FRANCAIS DE TOULOUSE", "adresse",R.drawable.ic_android_black_24dp);
        MarkerInfos tucInfo = new MarkerInfos("TUC section ESCALADE", "adresse",R.drawable.ic_android_black_24dp);
        MarkerInfos ascmInfo = new MarkerInfos("ASCM - ASSOCIATION SPORTIVE ET CULTURELLE MONTAUDRAN", "adresse",R.drawable.ic_android_black_24dp);
        MarkerInfos gymInfo = new MarkerInfos("INSTITUT GYMNIQUE DE TOULOUSE", "adresse",R.drawable.ic_android_black_24dp);
        MarkerInfos natationInfo = new MarkerInfos("STADE TOULOUSAIN NATATION", "adresse",R.drawable.ic_android_black_24dp);

        // on associe les informations au marqueur
        wcs.setTag(wcsInfos);
        skiAlpin.setTag(skiAlpinInfo);
        tuc.setTag(tucInfo);
        ascm.setTag(ascmInfo);
        gym.setTag(gymInfo);
        natation.setTag(natationInfo);

        // création de l'adapter et association de ce dernier à la map
        CustomMarkerAdapter customInfoWindow = new CustomMarkerAdapter(this);
        mMap.setInfoWindowAdapter(customInfoWindow);
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


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
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

package fr.wildcodeschool.mesclubs;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.text.ParseException;
import java.util.ArrayList;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private static final LatLng Club1 = new LatLng(43.604268, 1.441019);;
    private static final LatLng Club2 = new LatLng(43.774297	, 1.686036);
    private static final LatLng Club3  = new LatLng(43.586849, 1.435147);
    private static final LatLng Club4  = new LatLng(43.590233, 1.436469);
    private static final LatLng Club5  = new LatLng(43.60593,  1.453138);



    private Marker mClub1;
    private Marker mClub2;
    private Marker mClub3;
    private Marker mClub4;
    private Marker mClub5;




    private GoogleMap mMap;
    LocationManager mLocationManager = null;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


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

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));

        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

        //MARQUEURS CINQ PREMIERS CLUBS+ COULEURS ASSSOCIEES

        mClub1 = mMap.addMarker(new MarkerOptions().position(Club1).title("CLUB ALPIN FRANCAIS DE TOULOUSE")
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
        mClub2 = mMap.addMarker(new MarkerOptions().position(Club2).title("TUC section ESCALADE")
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
        mClub3 = mMap.addMarker(new MarkerOptions().position(Club3).title("ASCM - ASSOCIATION SPORTIVE ET CULTURELLE MONTAUDRAN")
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)));
        mClub4 = mMap.addMarker(new MarkerOptions().position(Club4).title("INSTITUT GYMNIQUE DE TOULOUSE")
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET)));
        mClub5 = mMap.addMarker(new MarkerOptions().position(Club5).title("STADE TOULOUSAIN NATATION")
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));


        







    }






}


package fr.wildcodeschool.mesclubs;


import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.text.style.BackgroundColorSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

public class CustomMarkerAdapter implements GoogleMap.InfoWindowAdapter {


    private Context context;

    public CustomMarkerAdapter(Context context){
        this.context = context;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        // on récupère les infos associées au tag
        Club club = (Club) marker.getTag();

        View view = ((Activity) context).getLayoutInflater()
                .inflate(R.layout.item_marker, null);

        TextView markerName = view.findViewById(R.id.marker_name);
        ImageView markerImage = view.findViewById(R.id.marker_image);
        ImageView markerHandicap = view.findViewById(R.id.image_handicap);
        TextView markerSport = view.findViewById(R.id.text_sport);
        TextView markeurWeb = view.findViewById(R.id.text_web);

        markerName.setText(club.getClubName());
        markerSport.setText(club.getSport());
        markeurWeb.setText(club.getWebsite());
        markerImage.setImageDrawable(context.getResources().getDrawable(club.getImage()));


        if (club.isHandicapped()){
            markerHandicap.setImageDrawable(context.getResources().getDrawable(R.drawable.handicap));
        }
        return view;
    }
}
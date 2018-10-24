package fr.wildcodeschool.mesclubs;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ListAdapter extends ArrayAdapter<Club> {


    public ListAdapter(Context context, ArrayList<Club> list) {
        super(context, 0, list);
    }

    public void likePreferences(final String clubId, boolean isLiked) {
        SharedPreferences sharedPref = getContext().getSharedPreferences("clubid", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(clubId, isLiked);
        editor.commit();
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        ListViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.row_item_list, parent, false);
            viewHolder = new ListViewHolder();
            viewHolder.clubName = convertView.findViewById(R.id.clubName);
            viewHolder.sport = convertView.findViewById(R.id.sport);
            viewHolder.sportColor = convertView.findViewById(R.id.sportColor);
            viewHolder.popUpButton = convertView.findViewById(R.id.popUpButton);
            viewHolder.drawerInfo = convertView.findViewById(R.id.drawerInfo);
            viewHolder.iv_like = convertView.findViewById(R.id.iv_like);
            viewHolder.iv_fav = convertView.findViewById(R.id.iv_fav);
            viewHolder.tv_address = convertView.findViewById(R.id.tv_address);
            viewHolder.tv_website = convertView.findViewById(R.id.tv_website);
            viewHolder.iv_share = convertView.findViewById(R.id.iv_share);
            viewHolder.iv_map = convertView.findViewById(R.id.iv_map);
            viewHolder.tv_map = convertView.findViewById(R.id.tv_map);
            convertView.setTag(viewHolder);
        }


        final Club club = getItem(position);

        viewHolder = (ListViewHolder) convertView.getTag();
        viewHolder.clubName.setText(club.getClubName());
        viewHolder.sport.setText(club.getSport());
        viewHolder.tv_address.setText(club.getAddress());
        viewHolder.sportColor.setImageDrawable(getContext().getResources().getDrawable(club.getImage()));
        viewHolder.tv_website.setText(club.getWebsite());
        if (viewHolder.tv_website.getText().length() == 0) {
            viewHolder.tv_website.setText(R.string.tvWebsite);
        }

        final ListViewHolder finalViewHolder = viewHolder;
        viewHolder.popUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (finalViewHolder.drawerInfo.getVisibility() == View.VISIBLE) {
                    finalViewHolder.drawerInfo.setVisibility(View.GONE);
                } else {
                    finalViewHolder.drawerInfo.setVisibility(View.VISIBLE);
                }
            }
        });

        final ListViewHolder finalViewHolder1 = viewHolder;
        viewHolder.iv_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message = getContext().getResources().getString(R.string.sahreBody);
                String sport = finalViewHolder1.sport.getText().toString();
                String clubName = finalViewHolder1.clubName.getText().toString();
                String webSite = finalViewHolder1.tv_website.getText().toString();
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                String shareBodyText = message + sport + " " + clubName + " " + webSite;
                shareIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "My App");
                shareIntent.putExtra(Intent.EXTRA_TEXT, shareBodyText);
                getContext().startActivity(Intent.createChooser(shareIntent, "Share via"));
            }
        });


        final ImageView ivLike = convertView.findViewById(R.id.iv_like);
        final TextView tvCounter = convertView.findViewById(R.id.tv_counter);
        final ImageView likeImg = viewHolder.iv_like;


        SharedPreferences sharedPref = getContext().getSharedPreferences("clubid", Context.MODE_PRIVATE);

        boolean isLiked = sharedPref.getBoolean(club.getId(), false);
        if (isLiked) {
            ivLike.setImageDrawable(ListAdapter.this.getContext().getResources().getDrawable(R.drawable.like));
        } else {
            ivLike.setImageDrawable(ListAdapter.this.getContext().getResources().getDrawable(R.drawable.like_off));
        }

        likeImg.setTag(false); // set favorite off
        viewHolder.iv_like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isliked = ((boolean) ivLike.getTag());
                if (!isliked) {
                    ivLike.setImageDrawable(ListAdapter.this.getContext().getResources().getDrawable(R.drawable.like));
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
                    ivLike.setImageDrawable(ListAdapter.this.getContext().getResources().getDrawable(R.drawable.like_off));
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
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
                ivLike.setTag(!isliked);
            }
        });

        final double lat = club.getLatitude();
        final double lon  =club.getLongitude();
        viewHolder.iv_map.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intentmap = new Intent();
                intentmap.setAction(android.content.Intent.ACTION_VIEW);
                intentmap.setData(Uri.parse("http://maps.google.com/maps?.34&daddr=" + club.getLatitude() + "," + club.getLongitude()));
                getContext().startActivity(intentmap);
            }
        });

        viewHolder.tv_map.setText(R.string.go);
        viewHolder.tv_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intentiti = new Intent();
                intentiti.setAction(android.content.Intent.ACTION_VIEW);
                intentiti.setData(Uri.parse("http://maps.google.com/maps?.34&daddr=" + list.getLatitude() + "," + list.getLongitude()));
                getContext().startActivity(intentiti);

            }
        });


        final Drawable starOn = convertView.getResources().getDrawable(R.drawable.btn_star_big_on);
        final Drawable starOff = convertView.getResources().getDrawable(R.drawable.btn_star_big_off);
        final ImageView starImg = viewHolder.iv_fav;
        starImg.setTag(false); // set favorite off
        viewHolder.iv_fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isFavorite = ((boolean) starImg.getTag());
                if (!isFavorite) {
                    starImg.setImageDrawable(starOn);
                } else {
                    starImg.setImageDrawable(starOff);
                }
                starImg.setTag(!isFavorite);
            }
        });
        return convertView;
    }

}

class ListViewHolder {
    public TextView clubName;
    public TextView sport;
    public TextView phone;
    public TextView tv_address;
    public TextView tv_website;
    public ImageView sportColor;
    public ConstraintLayout drawerInfo;
    public ImageButton      popUpButton;
    public ImageView        iv_like;
    public ImageView        iv_fav;
    public ImageView        iv_share;
    public ImageView        iv_map;
    public TextView         tv_map;
}




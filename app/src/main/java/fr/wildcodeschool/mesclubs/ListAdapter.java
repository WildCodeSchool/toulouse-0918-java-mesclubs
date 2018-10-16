package fr.wildcodeschool.mesclubs;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.constraint.ConstraintLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class ListAdapter extends ArrayAdapter <Club> {


    public ListAdapter (Context context,ArrayList<Club> list){
        super(context, 0, list);
    }

    public View getView ( int position, View convertView, ViewGroup parent) {

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
            convertView.setTag(viewHolder);
        }

        Club list = getItem(position);
        viewHolder = (ListViewHolder) convertView.getTag();
        viewHolder.clubName.setText(list.getClubName());
        viewHolder.sport.setText(list.getSport());
        viewHolder.tv_address.setText(list.getAddress());
        viewHolder.sportColor.setImageDrawable(getContext().getResources().getDrawable(list.getImage()));
        viewHolder.tv_website.setText(list.getWebsite());
        if(viewHolder.tv_website.getText().length() == 0){
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
                String message  = getContext().getResources().getString(R.string.sahreBody);
                String sport    = finalViewHolder1.sport.getText().toString();
                String clubName = finalViewHolder1.clubName.getText().toString();
                String webSite  = finalViewHolder1.tv_website.getText().toString();
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                String shareBodyText =  message + sport + " " + clubName + " " + webSite;
                shareIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,"My App");
                shareIntent.putExtra(Intent.EXTRA_TEXT,shareBodyText);
                getContext().startActivity(Intent.createChooser(shareIntent,"Share via"));
            }
        });

        final Drawable likeOn = convertView.getResources().getDrawable(R.drawable.like);
        final Drawable likeOff = convertView.getResources().getDrawable(R.drawable.like_off);
        final ImageView likeImg = viewHolder.iv_like;
        likeImg.setTag(false); // set favorite off
        viewHolder.iv_like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isLiked = ((boolean) likeImg.getTag());
                if (!isLiked) {
                    likeImg.setImageDrawable(likeOn);
                } else {
                    likeImg.setImageDrawable(likeOff);
                }
                likeImg.setTag(!isLiked);
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
    public TextView         clubName;
    public TextView         sport;
    public TextView         phone;
    public TextView         tv_address;
    public TextView         tv_website;
    public ImageView        sportColor;
    public ConstraintLayout drawerInfo;
    public ImageButton      popUpButton;
    public ImageView        iv_like;
    public ImageView        iv_fav;
    public ImageView        iv_share;
}




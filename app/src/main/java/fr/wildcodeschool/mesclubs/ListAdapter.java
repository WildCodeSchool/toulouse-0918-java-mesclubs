package fr.wildcodeschool.mesclubs;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
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
            viewHolder.address = convertView.findViewById(R.id.address);
            viewHolder.popUpButton = convertView.findViewById(R.id.popUpButton);
            viewHolder.drawerInfo = convertView.findViewById(R.id.drawerInfo);
            viewHolder.iv_like = convertView.findViewById(R.id.iv_like);
            viewHolder.iv_fav = convertView.findViewById(R.id.iv_fav);
            convertView.setTag(viewHolder);
        }

        Club list = getItem(position);
        viewHolder = (ListViewHolder) convertView.getTag();
        viewHolder.clubName.setText(list.getClubName());
        viewHolder.sport.setText(list.getSport());
      // viewHolder.sportColor.setImageDrawable(new ColorDrawable(getContext().getResources().getColor(list.getColor())));

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
    public ImageView        sportColor;
    public TextView         address;
    public TextView         phone;
    public ConstraintLayout drawerInfo;
    public ImageButton      popUpButton;
    public ImageView        iv_like;
    public ImageView        iv_fav;
}




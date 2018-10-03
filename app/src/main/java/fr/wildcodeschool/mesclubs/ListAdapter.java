package fr.wildcodeschool.mesclubs;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;

public class ListAdapter extends ArrayAdapter <ClubModel>{

    public ListAdapter(Context context,ArrayList<ClubModel> list){
        super(context, 0, list);
    }

    public View getView ( int position, View convertView, ViewGroup parent){

        ListViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.row_item_list, parent, false);
            viewHolder = new ListViewHolder();
            viewHolder.clubName=  convertView.findViewById(R.id.clubName);
            viewHolder.sport =  convertView.findViewById(R.id.sport);
            viewHolder.sportColor = convertView.findViewById(R.id.sportColor);
            convertView.setTag(viewHolder);
        }

        ClubModel list = getItem(position);
        viewHolder = (ListViewHolder) convertView.getTag();
        viewHolder.clubName.setText(list.getClubName());
        viewHolder.sport.setText(list.getSport());
        viewHolder.sportColor.setImageDrawable(new ColorDrawable(getContext().getResources().getColor(list.getColor())));

        return convertView;
    }
}

class ListViewHolder{
    public TextView clubName;
    public TextView sport;
    public ImageView sportColor;
}




import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

import fr.wildcodeschool.mesclubs.ListActivity;
import fr.wildcodeschool.mesclubs.R;

public class ListAdapter extends ArrayAdapter <>{
    public ListAdapter(Context context, 0 ,ArrayList<> list){
        super(context, 0, list);
    }
}
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        ListActivity clubs = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.activity_list, parent, false);
        }
        // Lookup view for data population
         = () convertView.findViewById(R.id.);
          = () convertView.findViewById(R.id.);
        // Populate the data into the template view using the data object
        .setText(.name);
        .setText(.hometown);
        // Return the completed view to render on screen
        return convertView;
    }

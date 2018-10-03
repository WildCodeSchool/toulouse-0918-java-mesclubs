package fr.wildcodeschool.mesclubs;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import java.text.ParseException;
import java.util.ArrayList;

public class ListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        ListView listTrip = findViewById(R.id.list_club);
        ArrayList<ClubModel> results = genererClubList();
        ListAdapter adapter = new ListAdapter(this, results);
        listTrip.setAdapter(adapter);
    }
    private ArrayList<ClubModel> genererClubList() {
        ArrayList<ClubModel> results = new ArrayList<>();

        results.add(new ClubModel(R.color.alpinism,"ASPTT GRAND TOULOUSE OMNISPORT", "ALPINISME"));
        results.add(new ClubModel(R.color.alpinism,"ASSOCIATION PYRENEES CLUB", "ALPINISME"));
        results.add(new ClubModel(R.color.escalade,"ESCAPADE CLUB", "ESCALADE"));
        results.add(new ClubModel(R.color.escalade,"LOISIRS ET MONTAGNE", "ESCALADE"));
        results.add(new ClubModel(R.color.marche,"ASSOCIATION SPORTIVE MONTAUDRAN", "MARCHE"));
        results.add(new ClubModel(R.color.natation,"INSTITUT GYMNIQUE DE TOULOUSE", "NATATION"));
        results.add(new ClubModel(R.color.natation,"STADE TOULOUSAIN NATATION", "NATATION"));
        return results;
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
    }
}

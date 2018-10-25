package fr.wildcodeschool.mesclubs;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

class Singleton {
    private static final Singleton ourInstance = new Singleton();
    private ArrayList<Club> listClub = new ArrayList<>();

    private Singleton() {
    }

    static Singleton getInstance() {

        return ourInstance;
    }

    public void loadClubs(final ClubListener listener) {
        //firebase
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference clubRef = database.getReference("club");


        clubRef.orderByChild("counter").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listClub.clear();

                for (DataSnapshot clubSnapshot : dataSnapshot.getChildren()) {
                    Club club = clubSnapshot.getValue(Club.class);

                    //transform JSON en objet club
                    club.setId(clubSnapshot.getKey());
                    club.setImage(getImages(club.getSport()));

                    listClub.add(club);
                }
                Collections.reverse(listClub);
                listener.onResponse(true);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                listener.onResponse(false);
            }
        });
    }

    public static int getImages(String sport) {
        int image;

        switch (sport) {
            case "ALPINISME":
                image = R.drawable.alpinisme;
                break;

            case "AVIRON":
                image = R.drawable.aviron;
                break;
            case "CANOE-KAYAK":
                image = R.drawable.canoe;
                break;

            case "CANYONISME":
                image = R.drawable.canyon;
                break;
            case "COURSE A PIED":
            case "COURSE D'ORIENTATION":
            case "marche":
                image = R.drawable.course;
                break;
            case "ESCALADE":
                image = R.drawable.escalade;
                break;
            case "NATATION":
                image = R.drawable.natation;
                break;
            case "PLONGEE":
                image = R.drawable.plonge;
                break;
            case "RANDONNEE":
                image = R.drawable.rando;
                break;
            case "SPELEOLOGIE":
                image = R.drawable.speleo;
                break;
            case "VOILE":
            case "planche à voile":
                image = R.drawable.voile;
                break;
            case "YOGA":
                image = R.drawable.yoga;
                break;
            default:
                image = R.drawable.ic_android_black_24dp;
        }
        return image;
    }
    public ArrayList<Club> getListClub() {
        return listClub;
    }
}

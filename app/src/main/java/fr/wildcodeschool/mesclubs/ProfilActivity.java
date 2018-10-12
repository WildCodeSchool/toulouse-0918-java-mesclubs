package fr.wildcodeschool.mesclubs;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ProfilActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    NavigationView navigationView;

    private DrawerLayout mDrawerLayout;
    private Toolbar toolbar;
    private FirebaseAuth mAuth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil);
        this.configureToolBar();
        this.configureDrawerLayout();
        this.configureNavigationView();
        mAuth = FirebaseAuth.getInstance();
        Button sendlog=findViewById(R.id.sendlog);
        Button send = findViewById(R.id.send);


        //jem'inscris
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth = FirebaseAuth.getInstance();
                final EditText emailog = findViewById(R.id.editText_pseudo);
                final EditText passwordlog = findViewById(R.id.password);
                String semailog = emailog.getText().toString();
                String spassword = passwordlog.getText().toString();
                if (semailog.isEmpty() || spassword.isEmpty()) {
                    Toast.makeText(ProfilActivity.this, "PLEASE FILL YOUR FORM",
                            Toast.LENGTH_SHORT).show();

                }
                else {
                   singup();
                }
            }
            });

       //jemeconnecte
        sendlog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth = FirebaseAuth.getInstance();
                final EditText emailog = findViewById(R.id.editText_pseudosingin);
                final EditText passwordlog = findViewById(R.id.passwordnew);
                String email = emailog.getText().toString();
                String password = passwordlog.getText().toString();
                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(ProfilActivity.this, "PLEASE FILL YOUR FORM",
                            Toast.LENGTH_SHORT).show();

                }
                else{
                    login();
                }
            }
        });






    }
    private void configureToolBar() {

        this.toolbar = (Toolbar) findViewById(R.id.toolbar2);

        setSupportActionBar(toolbar);

    }

    private void configureDrawerLayout() {
        this.mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }

    private void configureNavigationView() {

        this.navigationView = (NavigationView) findViewById(R.id.nav_view);

        navigationView.setNavigationItemSelectedListener(this);

    }


    //ONBACK PRESS METHODE
    @Override

    public void onBackPressed() {

        if (this.mDrawerLayout.isDrawerOpen(GravityCompat.START)) {

            this.mDrawerLayout.closeDrawer(GravityCompat.START);

        } else {

            super.onBackPressed();

        }

    }
    //This method will trigger on item Click of navigation menu

   // @Override

    public boolean onNavigationItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.connection:
                startActivity(new Intent(this, ProfilActivity.class));
                break;
            case R.id.déconnection:
                //check si connecté
                //si oui logout
                //si non toast
                Toast.makeText(ProfilActivity.this,"Vous n'êtes pas connecté", Toast.LENGTH_LONG);



        }
        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;




    }

    private void singup(){
        final EditText emailog = findViewById(R.id.editText_pseudo);
        final EditText passwordlog = findViewById(R.id.password);
        String semail = emailog.getText().toString();
        String spassword = passwordlog.getText().toString();

                mAuth.createUserWithEmailAndPassword(semail,spassword )
                        .addOnCompleteListener(ProfilActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Toast.makeText(ProfilActivity.this, "Inscription reussi",
                                            Toast.LENGTH_SHORT).show();

                                    FirebaseUser user = mAuth.getCurrentUser();
                                    updateUI(user);

                                } else {
                                    // If sign in fails, display a message to the user.

                                    Toast.makeText(ProfilActivity.this, "Inscription échouée",
                                            Toast.LENGTH_SHORT).show();
                                    updateUI(null);
                                }

                                // ...
                            }
                        });



            }




        private void login(){
            final EditText emailog = findViewById(R.id.editText_pseudosingin);
            final EditText passwordlog = findViewById(R.id.passwordnew);
            String email = emailog.getText().toString();
            String password = passwordlog.getText().toString();



                mAuth.signInWithEmailAndPassword(email,password )
                        .addOnCompleteListener(ProfilActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Toast.makeText(ProfilActivity.this, "Vous êtes connecté",
                                            Toast.LENGTH_SHORT).show();

                                    FirebaseUser user = mAuth.getCurrentUser();

                                } else {
                                    // If sign in fails, display a message to the user.

                                    Toast.makeText(ProfilActivity.this, "404 ERROR",
                                            Toast.LENGTH_SHORT).show();
                                    //updateUI(null);
                                }

                                // ...
                            }
                        });



            }




    //@Override POSEPROBLEME AU MOMENT DE LA RELANCE DE LA MAP
   /* public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        //updateUI(currentUser);
    }*/




     void updateUI(FirebaseUser user) {
        if (user != null) {

            startActivity(new Intent(ProfilActivity.this, MapsActivity.class));
        }
        else {Toast.makeText(ProfilActivity.this, "Vous n'êtes pas connecté",
                Toast.LENGTH_SHORT).show();}

    }




}
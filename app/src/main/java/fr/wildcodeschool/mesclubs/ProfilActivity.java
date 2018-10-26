package fr.wildcodeschool.mesclubs;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;

import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;

public class ProfilActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private static final int CAMERA_REQUEST = 101;
    NavigationView navigationView;
    ImageView mImageViewpage;
    ImageView mImageView;
    EditText etPseudo;
    CircularProgressButton loadingMe;
    View hedeaderLayout;
    Uri uriProfileImage;
    Uri photoStringLink;
    Menu menu;
    Menu dec;
    ImageView photos;
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
        mImageView = findViewById(R.id.userImage);
        mImageViewpage = findViewById(R.id.image_header);
        loadingMe = (CircularProgressButton) findViewById(R.id.send);
        hedeaderLayout = navigationView.getHeaderView(0);
        photos = hedeaderLayout.findViewById(R.id.image_header);
        menu = navigationView.getMenu();
        MenuItem filtreDistance = menu.findItem(R.id.filtre_distance);
        filtreDistance.setVisible(false);
        MenuItem filtreSport = menu.findItem(R.id.filtre_sport);
        filtreSport.setVisible(false);
        MenuItem filtreHand = menu.findItem(R.id.filtre_hand);
        filtreHand.setVisible(false);

        //Prendre la photo
        loadingMe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                @SuppressLint("StaticFieldLeak") AsyncTask<String, String, String> demoLogin = new AsyncTask<String, String, String>() {
                    @Override
                    protected String doInBackground(String... params) {
                        try {
                            Thread.sleep(3000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        return "done";
                    }

                    @Override
                    protected void onPostExecute(String s) {
                        if (s.equals("done")) {
                            showImageChooser();
                        }
                    }
                };

                loadingMe.startAnimation();
                demoLogin.execute();
            }
        });

    }

    private void showImageChooser() {
        Intent cameraIntent = new Intent(Intent.ACTION_GET_CONTENT);
        cameraIntent.setType("image/*");
        startActivityForResult(Intent.createChooser(cameraIntent, "Select Profile Image"), CAMERA_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            uriProfileImage = data.getData();
            try {
                Bitmap imageBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uriProfileImage);
                mImageView.setImageBitmap(imageBitmap);
                photos.setImageBitmap(imageBitmap);
                uploadImageToFirebaseStorage();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void uploadImageToFirebaseStorage() {
        final StorageReference profileImageRef = FirebaseStorage.getInstance().
                getReference("profilepics/" + System.currentTimeMillis() + ".jpg");
        if (uriProfileImage != null) {
            profileImageRef.putFile(uriProfileImage)
                    .continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                        @Override
                        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                            if (!task.isSuccessful()) {
                                throw task.getException();
                            }
                            return profileImageRef.getDownloadUrl();
                        }
                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        photoStringLink = task.getResult();
                        saveUserInformation();
                    }
                }
            });

        }
    }

    private void saveUserInformation() {
        //se connecter a firebase
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference userfirebase = database.getReference("User");
        userfirebase.child(mAuth.getUid()).child("picture").setValue(photoStringLink.toString());
        FirebaseUser user = mAuth.getCurrentUser();
        UserProfileChangeRequest profil = new UserProfileChangeRequest.Builder()
                .setPhotoUri(photoStringLink)
                .build();
        user.updateProfile(profil)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(ProfilActivity.this, R.string.photo_updated,
                                    Toast.LENGTH_SHORT).show();
                            finish();
                            startActivity(new Intent(ProfilActivity.this, ProfilActivity.class));
                            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                        }
                    }
                });
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    void updateUI(FirebaseUser user) {
        if (user != null) {
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference userfirebase = database.getReference("User");
            userfirebase.child(mAuth.getUid()).child("picture").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String value = dataSnapshot.getValue(String.class);
                    if (value != null && !value.isEmpty()) {
                        ImageView photo = hedeaderLayout.findViewById(R.id.image_header);
                        Glide.with(photo.getContext())
                                .load(value)
                                .apply(RequestOptions.circleCropTransform())
                                .into(photo);
                        Glide.with(photo.getContext())
                                .load(value)
                                .apply(RequestOptions.circleCropTransform())
                                .into(mImageView);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });
            TextView pseudo = hedeaderLayout.findViewById(R.id.etPseudo);
            pseudo.setText(user.getEmail());
            TextView mail = findViewById(R.id.etMail);
            mail.setText(user.getEmail());
            menu = navigationView.getMenu();
            MenuItem target = menu.findItem(R.id.connection);
            target.setVisible(false);
            dec = navigationView.getMenu();
            MenuItem target6 = dec.findItem(R.id.déconnection);
            target6.setVisible(true);
        } else {
            menu = navigationView.getMenu();
            MenuItem target = menu.findItem(R.id.connection);
            target.setVisible(true);
        }
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
        this.navigationView = findViewById(R.id.nav_view);
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

    public boolean onNavigationItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.connection:
                startActivity(new Intent(this, ProfilActivity.class));
                break;
            case R.id.déconnection:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(this, MainActivity.class));
                updateUI(null);
                Toast.makeText(ProfilActivity.this, R.string.deconnection, Toast.LENGTH_LONG).show();
                break;

            case R.id.liste:
                startActivity(new Intent(this, ListActivity.class));
                break;

            case R.id.map:
                startActivity(new Intent(this, MapsActivity.class));
                break;
        }
        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}

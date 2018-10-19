package fr.wildcodeschool.mesclubs;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;

public class ProfilActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private static final int CAMERA_REQUEST = 101;
    private DrawerLayout mDrawerLayout;
    private Toolbar toolbar;
    private FirebaseAuth mAuth;

    NavigationView  navigationView;
    ImageView       mImageViewpage;
    ImageView       mImageView;
    EditText        et_pseudo;
    EditText        emailLog;
    EditText        password;
    EditText        passwordLog;
    Button          photo;
    View            hedeaderLayout;
    Uri             uriProfileImage;
    Uri             photoStringLink;
    Menu            menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil);
        this.configureToolBar();
        this.configureDrawerLayout();
        this.configureNavigationView();

        mAuth           = FirebaseAuth.getInstance();
        Button sendlog  = findViewById(R.id.sendlog);
        Button send     = findViewById(R.id.send);
        mImageView      = findViewById(R.id.imageView);
        mImageViewpage  = findViewById(R.id.image_header);
        emailLog        = findViewById(R.id.et_pseudosingin);
        passwordLog     = findViewById(R.id.et_passwordSignIn);
        et_pseudo       = findViewById(R.id.et_pseudo);
        password        = findViewById(R.id.et_password);
        photo           = findViewById(R.id.bTakephoto);
        hedeaderLayout  = navigationView.getHeaderView(0);

        //jem'inscris
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth = FirebaseAuth.getInstance();
                String semailog = et_pseudo.getText().toString();
                String spassword = password.getText().toString();
                if (semailog.isEmpty() || spassword.isEmpty()) {
                    Toast.makeText(ProfilActivity.this, "PLEASE FILL YOUR FORM",
                            Toast.LENGTH_SHORT).show();
                } else {
                    singup();
                }
            }
        });

        //jemeconnecte
        sendlog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth = FirebaseAuth.getInstance();
                String email = emailLog.getText().toString();
                String passwordField = passwordLog.getText().toString();
                if (email.isEmpty() || passwordField.isEmpty()) {
                    Toast.makeText(ProfilActivity.this, "PLEASE FILL YOUR FORM",
                            Toast.LENGTH_SHORT).show();
                } else {
                    login();
                    emailLog.setText(email);
                }
            }
        });
        //Prendre la photo
        photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showImageChooser();
            }
        });
    }

    private void singup() {
        String semail = et_pseudo.getText().toString().trim();
        String spassword = password.getText().toString().trim();
        mAuth.createUserWithEmailAndPassword(semail, spassword)
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
                    }
                });
    }

    private void login() {
        String email = emailLog.getText().toString();
        String passwordField = passwordLog.getText().toString();
        mAuth.signInWithEmailAndPassword(email, passwordField)
                .addOnCompleteListener(ProfilActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Toast.makeText(ProfilActivity.this, "Vous êtes connecté",
                                    Toast.LENGTH_SHORT).show();
                            finish();
                            overridePendingTransition(0, 0);
                            startActivity(getIntent());
                            overridePendingTransition(0, 0);
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(ProfilActivity.this, "404 ERROR",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
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
        ImageView photo = hedeaderLayout.findViewById(R.id.image_header);
            if(requestCode == CAMERA_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null){
            uriProfileImage = data.getData();
            try {
                Bitmap imageBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uriProfileImage);
                mImageView.setImageBitmap(imageBitmap);
                photo.setImageBitmap(imageBitmap);
                uploadImageToFirebaseStorage();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void uploadImageToFirebaseStorage() {
        final StorageReference profileImageRef = FirebaseStorage.getInstance().
                getReference("profilepics/" + System.currentTimeMillis() + ".jpg" );
        if (uriProfileImage != null) {
            profileImageRef.putFile(uriProfileImage)
                    .continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                        @Override
                        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                            if (!task.isSuccessful()){
                                throw task.getException();
                            }
                            return profileImageRef.getDownloadUrl();
                        }
                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()){
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
        String displayName = et_pseudo.getText().toString();
        FirebaseUser user = mAuth.getCurrentUser();
        UserProfileChangeRequest profil = new UserProfileChangeRequest.Builder()
                .setDisplayName(displayName)
                .setPhotoUri(photoStringLink)
                .build();
        user.updateProfile(profil)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(ProfilActivity.this, "Profile Updated",
                                    Toast.LENGTH_SHORT).show();
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
            userfirebase.child(mAuth.getUid()).child("picture").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String value = dataSnapshot.getValue(String.class);
                    if (value != null && !value.isEmpty()) {
                        photo.setVisibility(View.VISIBLE);
                        mImageView.setVisibility(View.VISIBLE);
                        ImageView photo = hedeaderLayout.findViewById(R.id.image_header);
                        Glide.with(ProfilActivity.this)
                                .load(value)
                                .apply(RequestOptions.circleCropTransform())
                                .into(photo);
                        Glide.with(ProfilActivity.this)
                                .load(value)
                                .into(mImageView);
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {}
            });
            TextView pseudo = hedeaderLayout.findViewById(R.id.et_pseudo);
            pseudo.setText(user.getEmail());
            //startActivity(new Intent(ProfilActivity.this, MainActivity.class));
            //mDrawerLayout.findViewById(R.id.connection);
            //mDrawerLayout.setVisibility(View.INVISIBLE);
            menu = navigationView.getMenu();
            MenuItem target = menu.findItem(R.id.connection);
            target.setVisible(false);
        }else {
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
                updateUI(null);

                Toast.makeText(ProfilActivity.this, "Vous n'êtes pas connecté", Toast.LENGTH_LONG);
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

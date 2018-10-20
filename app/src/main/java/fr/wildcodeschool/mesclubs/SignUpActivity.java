package fr.wildcodeschool.mesclubs;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SignUpActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    EditText et_pseudo;
    EditText et_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        Button send = findViewById(R.id.send);
        et_pseudo = findViewById(R.id.et_pseudo);
        et_password = findViewById(R.id.et_password);
        mAuth = FirebaseAuth.getInstance();

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth = FirebaseAuth.getInstance();
                String semailog = et_pseudo.getText().toString();
                String spassword = et_password.getText().toString();
                if (semailog.isEmpty() || spassword.isEmpty()) {
                    Toast.makeText(SignUpActivity.this, "PLEASE FILL YOUR FORM",
                            Toast.LENGTH_SHORT).show();
                } else {
                    singup();
                }
            }
        });
    }

    private void singup() {
        String semail = et_pseudo.getText().toString().trim();
        String spassword = et_password.getText().toString().trim();
        mAuth.createUserWithEmailAndPassword(semail, spassword)
                .addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Toast.makeText(SignUpActivity.this, "Inscription reussi",
                                    Toast.LENGTH_SHORT).show();
                            Intent goToMain = new Intent(SignUpActivity.this, ProfilActivity.class);
                            SignUpActivity.this.startActivity(goToMain);
                            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(SignUpActivity.this, "Inscription échouée",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

}

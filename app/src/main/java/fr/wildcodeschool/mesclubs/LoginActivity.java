package fr.wildcodeschool.mesclubs;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    EditText emailLog;
    EditText        passwordLog;
    private FirebaseAuth mAuth;
    Button sendlog;
    TextView et_sinscrire;
    FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        et_sinscrire = findViewById(R.id.et_sinscrire);
        mAuth = FirebaseAuth.getInstance();
        passwordLog = findViewById(R.id.et_password);
        emailLog        = findViewById(R.id.et_pseudo);
        sendlog  = findViewById(R.id.send);


        currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);

        sendlog.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                mAuth = FirebaseAuth.getInstance();
                String email = emailLog.getText().toString();
                String passwordField = passwordLog.getText().toString();
                if (email.isEmpty() || passwordField.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "PLEASE FILL YOUR FORM",
                            Toast.LENGTH_SHORT).show();
                } else {
                    login();
                    emailLog.setText(email);
                }
            }
        });

        et_sinscrire.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent goToSignUp = new Intent(LoginActivity.this, SignUpActivity.class);
                LoginActivity.this.startActivity(goToSignUp);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

    }


    private void login() {
        String email = emailLog.getText().toString();
        String passwordField = passwordLog.getText().toString();
        mAuth.signInWithEmailAndPassword(email, passwordField)
                .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Toast.makeText(LoginActivity.this, "Vous êtes connecté",
                                    Toast.LENGTH_SHORT).show();
                            Intent goToMain = new Intent(LoginActivity.this, MainActivity.class);
                            LoginActivity.this.startActivity(goToMain);
                            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(LoginActivity.this, "404 ERROR",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    void updateUI(FirebaseUser user) {
        if (user != null) {
        Intent goToMain = new Intent(LoginActivity.this, MainActivity.class);
            LoginActivity.this.startActivity(goToMain);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }
    }

}

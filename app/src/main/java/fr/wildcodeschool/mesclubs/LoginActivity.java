package fr.wildcodeschool.mesclubs;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;

public class LoginActivity extends AppCompatActivity {

    EditText emailLog;
    EditText passwordLog;
    TextView etSinscrire;
    FirebaseUser currentUser;
    CircularProgressButton loadingMe;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        etSinscrire = findViewById(R.id.etSinscrire);
        mAuth = FirebaseAuth.getInstance();
        passwordLog = findViewById(R.id.etPassword);
        emailLog = findViewById(R.id.etPseudo);
        loadingMe = (CircularProgressButton) findViewById(R.id.send);

        currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);

        loadingMe.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                mAuth = FirebaseAuth.getInstance();
                final String email = emailLog.getText().toString();
                String passwordField = passwordLog.getText().toString();
                if (email.isEmpty() || passwordField.isEmpty()) {
                    Toast.makeText(LoginActivity.this, R.string.replir_champ,
                            Toast.LENGTH_SHORT).show();
                } else {
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
                                login();
                                emailLog.setText(email);
                            }
                        }
                    };
                    loadingMe.startAnimation();
                    demoLogin.execute();
                }
            }
        });

        etSinscrire.setOnClickListener(new View.OnClickListener() {

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
                            Toast.makeText(LoginActivity.this, R.string.connecté,
                                    Toast.LENGTH_SHORT).show();
                            Intent goToMain = new Intent(LoginActivity.this, MainActivity.class);
                            LoginActivity.this.startActivity(goToMain);
                            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                            finish();
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(LoginActivity.this, R.string.email_password,
                                    Toast.LENGTH_SHORT).show();
                            finish();
                            startActivity(new Intent(LoginActivity.this, LoginActivity.class));
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

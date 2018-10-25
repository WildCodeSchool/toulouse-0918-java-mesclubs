package fr.wildcodeschool.mesclubs;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
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


import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;

public class SignUpActivity extends AppCompatActivity {

    EditText etPseudo;
    EditText etPassword;
    TextView etCeConnecte;
    CircularProgressButton loadingMe;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        etPseudo = findViewById(R.id.etPseudo);
        etPassword = findViewById(R.id.etPassword);
        mAuth = FirebaseAuth.getInstance();
        loadingMe = findViewById(R.id.send);
        etCeConnecte = findViewById(R.id.et_ce_connecter);

        etCeConnecte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignUpActivity.this,LoginActivity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        loadingMe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth = FirebaseAuth.getInstance();
                String semailog = etPseudo.getText().toString();
                String spassword = etPassword.getText().toString();
                if (semailog.isEmpty() || spassword.isEmpty()) {
                    Toast.makeText(SignUpActivity.this, R.string.replir_champ,
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
                                singup();
                            }
                        }
                    };
                    loadingMe.startAnimation();
                    demoLogin.execute();
                }
            }
        });
    }

    private void singup() {
        String semail = etPseudo.getText().toString().trim();
        String spassword = etPassword.getText().toString().trim();
        mAuth.createUserWithEmailAndPassword(semail, spassword)
                .addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Toast.makeText(SignUpActivity.this, R.string.reussite,
                                    Toast.LENGTH_SHORT).show();
                            Intent goToMain = new Intent(SignUpActivity.this, ProfilActivity.class);
                            SignUpActivity.this.startActivity(goToMain);
                            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                            finish();
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(SignUpActivity.this, R.string.echec,
                                    Toast.LENGTH_SHORT).show();
                            finish();
                            startActivity(new Intent(SignUpActivity.this, SignUpActivity.class));
                        }
                    }
                });
    }

}

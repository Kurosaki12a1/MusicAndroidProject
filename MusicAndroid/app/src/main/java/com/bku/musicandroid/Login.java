package com.bku.musicandroid;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import static android.content.ContentValues.TAG;

public class Login extends Activity {
    private EditText email_txt;
    private EditText pass_txt;
    private TextView forgot_password;
    private TextView sign_up;
    private Button login_btn;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.setContentView(R.layout.activity_login);
        email_txt = findViewById(R.id.email_txt);
        pass_txt = findViewById(R.id.pass_txt);
        login_btn=findViewById(R.id.login_btn);
        forgot_password=findViewById(R.id.forgot_pass_txt);
        sign_up=findViewById(R.id.signup_txt);
        mAuth = FirebaseAuth.getInstance();
        forgot_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, Forget_Password.class);
                startActivity(intent);
            }
        });
        sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, Sign_up.class);
                startActivity(intent);
            }
        });
        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn(email_txt.getText().toString().trim(),pass_txt.getText().toString().trim());
            }
        });
    }
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        //updateUI(currentUser);
    }
    private void signIn(String email, String password) {
        Log.d(TAG, "signIn:" + email);
        if (!validateForm()) {
            return;
        }

        // [START sign_in_with_email]
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(getApplicationContext(), "Login success",Toast.LENGTH_SHORT).show();
                            Intent mainIntent = new Intent(Login.this, MainActivity.class);
                            startActivity(mainIntent);
                            //   updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(getApplicationContext(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            // updateUI(null);
                        }

                        // [START_EXCLUDE]
                      //  if (!task.isSuccessful()) {
                      //      Toast.makeText(getApplicationContext(), "Error. Cannot Authentication",
                      //              Toast.LENGTH_SHORT).show();
                            // mStatusTextView.setText(R.string.auth_failed);
                      //  }
                        //   hideProgressDialog();
                        // [END_EXCLUDE]
                    }
                });
        // [END sign_in_with_email]
    }
    private boolean validateForm() {
        boolean valid = true;

        String email = email_txt.getText().toString();
        if (TextUtils.isEmpty(email)) {
            email_txt.setError("Required.");
            valid = false;
        } else {
            email_txt.setError(null);
        }

        String password = pass_txt.getText().toString();
        if (TextUtils.isEmpty(password)) {
            pass_txt.setError("Required.");
            valid = false;
        }
        else {
            pass_txt.setError(null);
        }

        return valid;
    }

}

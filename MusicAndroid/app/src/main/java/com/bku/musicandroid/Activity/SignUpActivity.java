package com.bku.musicandroid.Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.bku.musicandroid.R;
import com.bku.musicandroid.Model.Users;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.Thread.sleep;

/**
 * Created by Administrator on 3/21/2018.
 */

public class SignUpActivity extends Activity {
    private static final String TAG = "abc";
    private String email;
    private String password;
    private String confirmpass;
    public String username;
    private EditText emailTxt;
    private EditText passwordTxt;
    private EditText confirmpassTxt;
    private EditText usernameTxt;
    private Button signUpBtn;
    private FirebaseAuth auth;
    private boolean validFlag;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.setContentView(R.layout.activity_sign_up);
        signUpBtn = findViewById(R.id.sign_up_btn);
        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUpBtn = findViewById(R.id.sign_up_btn);
                emailTxt = findViewById(R.id.email_txt);
                passwordTxt = findViewById(R.id.password_txt);
                confirmpassTxt = findViewById(R.id.confirmpass_txt);
                usernameTxt = findViewById(R.id.username_txt);
                email = emailTxt.getText().toString().trim();
                password = passwordTxt.getText().toString().trim();
                confirmpass = confirmpassTxt.getText().toString().trim();
                username = usernameTxt.getText().toString().trim();
                progressDialog = new ProgressDialog(SignUpActivity.this);
                progressDialog.setTitle("Sign up");
                progressDialog.setMessage("Signing in, please wait...");
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDialog.show();
                progressDialog.setCancelable(false);
                validateForm();
            }
        });
    }

    private void signUp(final String email, String password, String confirmpass, final String fullname) {
        auth = FirebaseAuth.getInstance();
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {
                            Log.d("1abc", "success");
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = auth.getCurrentUser();
                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                            DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                            DatabaseReference usersRef = ref.child("All_Users_Info_Database").child("users");
                            String userId = usersRef.push().getKey();
                            final Map<String, Object> dataMap = new HashMap<String, Object>();
                            String defaultAvatarURL = "https://firebasestorage.googleapis.com/v0/b/android-music-app-player.appspot.com/o/user_default.png?alt=media&token=adf8a1cb-e636-47d2-93de-31275e4024ed";
                            Users temp = new Users(username, email, "Default_fullname", defaultAvatarURL, "Default_Dateofbirth", "");
                            usersRef.child(user.getUid()).setValue(temp);
                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(username).build();
                            user.updateProfile(profileUpdates);

                            final FirebaseUser firebaseuser = auth.getCurrentUser();
                            firebaseuser.sendEmailVerification()
                                    .addOnCompleteListener(SignUpActivity.this, new OnCompleteListener() {
                                        @Override
                                        public void onComplete(@NonNull Task task) {
                                            if (task.isSuccessful()) {
                                                Log.e("1abc", "Email send done");
                                                progressDialog.dismiss();
                                                Toast.makeText(SignUpActivity.this,
                                                        "Verification email sent to " + firebaseuser.getEmail() + ". Please verifying in your email.",
                                                        Toast.LENGTH_SHORT).show();
                                            } else {
                                                progressDialog.dismiss();
                                                Log.e("1abc", "sendEmailVerification", task.getException());
                                                Toast.makeText(getApplicationContext(),
                                                        "Failed to send verification email.",
                                                        Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                            Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                            startActivity(intent);
                            auth.signOut();

                            //        updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("1abc", "createUserWithEmail:failure", task.getException());
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), "Sign up failed." + task.getException(),
                                    Toast.LENGTH_SHORT).show();
                            emailTxt.setError("The email address is already in use by another account.");
                            //     updateUI(null);
                        }

                        // [START_EXCLUDE]
                        //         hideProgressDialog();
                        // [END_EXCLUDE]
                    }
                });
    }

    private void validateForm() {
        validFlag = true;
        emailTxt = findViewById(R.id.email_txt);
        passwordTxt = findViewById(R.id.password_txt);
        confirmpassTxt = findViewById(R.id.confirmpass_txt);
        usernameTxt = findViewById(R.id.username_txt);
        email = emailTxt.getText().toString().trim();
        password = passwordTxt.getText().toString().trim();
        confirmpass = confirmpassTxt.getText().toString().trim();
        username = usernameTxt.getText().toString().trim();
        Log.d("1abc", "pass: " + password + " Confirm: " + confirmpass + " email: " + email + " username: " + username);
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference();
        ref.child("All_Users_Info_Database").child("users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (TextUtils.isEmpty(email)) {
                    emailTxt.setError("Required.");
                    validFlag = false;
                } else {
                    emailTxt.setError(null);
                }

                if (TextUtils.isEmpty(password) || password.length() < 6) {
                    passwordTxt.setError("Too short");
                    validFlag = false;
                } else {
                    passwordTxt.setError(null);
                }
                if (confirmpass.length() < 6 || !confirmpass.equals(password)) {
                    confirmpassTxt.setError("Not right");
                    Log.d("1abc", "toi day" + confirmpass + " " + password + "1");
                    validFlag = false;
                } else {
                    confirmpassTxt.setError(null);
                }
                Log.d("1abc", "1");
                Pattern p = Pattern.compile("[^A-Za-z0-9_]", Pattern.CASE_INSENSITIVE);
                Matcher m = p.matcher(username);
                boolean b = m.find();

                if (username.length() < 6 || b) {
                    usernameTxt.setError("Username is too short or contains special character");
                    validFlag = false;
                } else {
                    usernameTxt.setError(null);
                }
                //   Users user = dataSnapshot.getValue(Users.class);
                for (DataSnapshot imageSnapshot : dataSnapshot.getChildren()) {
                    Users user = imageSnapshot.getValue(Users.class);
                    if (user.userName.equals(username)) {
                        usernameTxt.setError("Username already existed");
                        validFlag = false;
                        Log.w("1abc", "Valid Flag: " + validFlag);
                        break;
                    }
                    Log.w("1abc", "Username of Username: " + user.userName);
                }
                if (validFlag == true) {
                    signUp(email, password, confirmpass, username);

                } else {
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("1abc", "Failed to read value.", error.toException());
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), "Error: " + error.toException(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}

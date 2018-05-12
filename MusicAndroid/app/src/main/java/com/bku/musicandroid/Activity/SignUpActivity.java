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
    private EditText email_txt;
    private EditText password_txt;
    private EditText confirmpass_txt;
    private EditText username_txt;
    private Button signup_btn;
    private FirebaseAuth auth;
    private boolean valid_flag;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.setContentView(R.layout.activity_sign_up);
        signup_btn=findViewById(R.id.sign_up_btn);
        Log.d("2abc","check: "+email+password+"20"+confirmpass);
        signup_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup_btn=findViewById(R.id.sign_up_btn);
                email_txt=findViewById(R.id.email_txt);
                password_txt = findViewById(R.id.password_txt);
                confirmpass_txt = findViewById(R.id.confirmpass_txt);
                username_txt=findViewById(R.id.username_txt);
                email=email_txt.getText().toString().trim();
                password=password_txt.getText().toString().trim();
                confirmpass=confirmpass_txt.getText().toString().trim();
                username=username_txt.getText().toString().trim();
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
    private void sign_up(final String email, String password, String confirmpass, final String fullname) {
        auth = FirebaseAuth.getInstance();
        Log.d(TAG, "createAccount:" + email);
     //   validateForm();

      //  if (valid_flag==false) {
      //      progressDialog.dismiss();
      //      return;
      //  }
      //  Log.d("1abc","qua valid"+valid_flag+email+password);

        // [START create_user_with_email]
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {
                            Log.d("1abc","success");
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = auth.getCurrentUser();
                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                            DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                            DatabaseReference usersRef = ref.child("All_Users_Info_Database").child("users");
                            String userId = usersRef.push().getKey();
                            final Map<String, Object> dataMap = new HashMap<String, Object>();
                            Users temp=new Users(username, email, "Default_fullname","Default_avatarURL","Default_Dateofbirth");
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
                                                        "Verification email sent to " + firebaseuser.getEmail()+". Please verifying in your email.",
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
                            Toast.makeText(getApplicationContext(), "Sign up failed."+task.getException(),
                                    Toast.LENGTH_SHORT).show();
                            email_txt.setError("The email address is already in use by another account.");
                            //     updateUI(null);
                        }

                        // [START_EXCLUDE]
                        //         hideProgressDialog();
                        // [END_EXCLUDE]
                    }
                });
    }

    private void validateForm() {
        valid_flag = true;
        email_txt=findViewById(R.id.email_txt);
        password_txt = findViewById(R.id.password_txt);
        confirmpass_txt = findViewById(R.id.confirmpass_txt);
        username_txt=findViewById(R.id.username_txt);
        email=email_txt.getText().toString().trim();
        password=password_txt.getText().toString().trim();
        confirmpass=confirmpass_txt.getText().toString().trim();
        username=username_txt.getText().toString().trim();
        Log.d("1abc", "pass: "+password+" Confirm: "+confirmpass+" email: "+email+" username: "+username);
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference();
        ref.child("All_Users_Info_Database").child("users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (TextUtils.isEmpty(email)) {
                    email_txt.setError("Required.");
                    valid_flag = false;
                } else {
                    email_txt.setError(null);
                }

                if (TextUtils.isEmpty(password)||password.length()<6) {
                    password_txt.setError("Too short");
                    valid_flag = false;
                }
                else {
                    password_txt.setError(null);
                }
                if (confirmpass.length()<6 || !confirmpass.equals(password)) {
                    confirmpass_txt.setError("Not right");
                    Log.d("1abc","toi day"+confirmpass+" "+password+"1");
                    valid_flag = false;
                }
                else {
                    confirmpass_txt.setError(null);
                }
                Log.d("1abc","1");
                Pattern p = Pattern.compile("[^A-Za-z0-9_]", Pattern.CASE_INSENSITIVE);
                Matcher m = p.matcher(username);
                boolean b = m.find();

                if (username.length()<6 || b ) {
                    username_txt.setError("Username is too short or contains special character");
                    valid_flag = false;
                }
                else {
                    username_txt.setError(null);
                }
                //   Users user = dataSnapshot.getValue(Users.class);
                for (DataSnapshot imageSnapshot: dataSnapshot.getChildren()) {
                    Users user = imageSnapshot.getValue(Users.class);
                    if(user.userName.equals(username)){
                        username_txt.setError("Username already existed");
                        valid_flag=false;
                        Log.w("1abc", "Valid Flag: " + valid_flag);
                        break;
                    }
                    Log.w("1abc", "Username of Username: " + user.userName);
                }
                if(valid_flag==true){
                    sign_up(  email,  password,  confirmpass,  username);

                }else {
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

        Log.d("1abc","2");

        Log.d("1abc","3"+valid_flag);
        //Check if username is available
        /*
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        ref.child("users").child("username").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    username_txt.setError("Username is not available");
                    valid_flag = false;
                } else {
                    username_txt.setError(null);
                    valid_flag=true;
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        }); */
     //   return valid_flag;
    }


}

package com.bku.musicandroid;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bku.musicandroid.Forget_Password;
import com.bku.musicandroid.MainScreenActivity;
import com.bku.musicandroid.R;
import com.bku.musicandroid.Sign_up;
import com.bku.musicandroid.Users;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.content.ContentValues.TAG;

public class Login extends Activity {
    public static String ROLES;
    private EditText email_txt;
    private EditText pass_txt;
    private TextView forgot_password;
    private TextView sign_up;
    private Button login_btn;
    private SignInButton login_google_btn;
    private FirebaseAuth mAuth;
    private ProgressDialog progressDialog;
    private Dialog dialog;
    private Dialog usernamedialog;
    private GoogleSignInClient mGoogleSignInClient;
    private static final int RC_SIGN_IN = 9001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.setContentView(R.layout.activity_login);
        email_txt = findViewById(R.id.email_txt);
        pass_txt = findViewById(R.id.pass_txt);
        login_btn = findViewById(R.id.login_btn);
        forgot_password = findViewById(R.id.forgot_pass_txt);
        sign_up = findViewById(R.id.signup_txt);
        login_google_btn = findViewById(R.id.login_google_btn);
        //Google login
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        // [END config_signin]

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        ///////////////////////////////////////
        // [START initialize_auth]
        mAuth = FirebaseAuth.getInstance();
        Log.d("1abc", "Check: " + mAuth.getCurrentUser());
        if (mAuth.getCurrentUser() != null)
            Log.d("1abc", "Check: " + mAuth.getCurrentUser() + " " + mAuth.getCurrentUser().getUid());
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
                //Begin to initialize the Progress dialog
                progressDialog = new ProgressDialog(Login.this);
                progressDialog.setTitle("Log in");
                progressDialog.setMessage("Logging in, please wait...");
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDialog.show();
                progressDialog.setCancelable(false);
                Log.d("1abc", "Chua click button" + mAuth.getCurrentUser());
                if (mAuth.getCurrentUser() == null) {
                    Log.d("1abc", "Dang nhap fail chua click button");
                }
                if (mAuth.getCurrentUser() != null) {
                    Toast.makeText(getApplicationContext(), "Co nguoi da login", Toast.LENGTH_SHORT).show();
                }
                signIn(email_txt.getText().toString().trim(), pass_txt.getText().toString().trim());
            }
        });
        login_google_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent login_google=new Intent(Login.this,Login_Google.class);
                // startActivity(login_google);
                progressDialog = new ProgressDialog(Login.this);
                progressDialog.setTitle("Log in");
                progressDialog.setMessage("Logging in, please wait...");
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDialog.show();
                progressDialog.setCancelable(false);
                signInGoogle();
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
            if (mAuth.getCurrentUser() == null) {
            }
            Log.d("1abc", "Dang nhap fail ahihi validate");

            Log.d("1abc", "Dang nhap fail" + mAuth.getCurrentUser());
            progressDialog.dismiss(); //Log in done, close the progress dialog
            return;
        }

        // [START sign_in_with_email]
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success" + mAuth.getCurrentUser().isEmailVerified());
                            FirebaseUser user = mAuth.getCurrentUser();
                            progressDialog.dismiss(); //Log in done, close the progress dialog
                            if (user.isEmailVerified() == false) {
                                showDialog();
                            } else {
                                Toast.makeText(getApplicationContext(), "Login success", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(), MainScreenActivity.class);
                                startActivity(intent);
                            }
                            ////Success login
                            //   Intent mainIntent = new Intent(Login.this, MainScreenActivity.class);
                            //  startActivity(mainIntent);
                        } else {
                            // If sign in fails, display a message to the user.
                            progressDialog.dismiss(); //Log in done, close the progress dialog
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
        } else {
            pass_txt.setError(null);
        }

        return valid;
    }

    public void showDialog() {
        dialog = new Dialog(Login.this);
        dialog.setTitle("Tài khoản chưa xác nhận");
        dialog.setContentView(R.layout.dialog_not_verify_email);
        dialog.show();
        Button cancel;
        Button resend;
        cancel = (Button) dialog.findViewById(R.id.cancel_btn);
        resend = (Button) dialog.findViewById(R.id.resend_email_btn);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sign_out();
                dialog.dismiss();
            }
        });
        resend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resend_email_verification();
                sign_out();
                dialog.dismiss();
            }
        });
        dialog.setOnCancelListener(
                new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        //When you touch outside of dialog bounds,
                        //the dialog gets canceled and this method executes.
                        sign_out();
                    }
                }
        );

    }

    public void verifyUsername() {
        usernamedialog = new Dialog(Login.this);
        usernamedialog.setTitle("Thêm Username");
        usernamedialog.setContentView(R.layout.dialog_add_username);
        final EditText usernameGoogle_txt = usernamedialog.findViewById(R.id.usernamegg_txt);
        usernamedialog.show();
        Button accept;
        accept = (Button) usernamedialog.findViewById(R.id.accept_btn);
        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog = new ProgressDialog(Login.this);
                progressDialog.setTitle("Check username");
                progressDialog.setMessage("Checking, please wait...");
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDialog.show();
                progressDialog.setCancelable(false);
                final FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference ref = database.getReference();
                ref.child("All_Users_Info_Database").child("users").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Boolean flag_gg = true;
                        String usernamegg = usernameGoogle_txt.getText().toString();
                        Log.d("1abc", "1");
                        Pattern p = Pattern.compile("[^A-Za-z0-9_]", Pattern.CASE_INSENSITIVE);
                        Matcher m = p.matcher(usernamegg);
                        boolean b = m.find();
                        Log.d("1abc","Username checking add: "+usernamegg);
                        if (usernamegg.length() < 6 || b) {
                            usernameGoogle_txt.setError("Username is too short or contains special character");
                            flag_gg = false;
                        } else {
                            usernameGoogle_txt.setError(null);
                        }
                        for (DataSnapshot imageSnapshot : dataSnapshot.getChildren()) {
                            Users user = imageSnapshot.getValue(Users.class);
                            if (user.userName.equals(usernamegg)) {
                                usernameGoogle_txt.setError("Username already existed");
                                flag_gg = false;
                                Log.w("1abc", "Flag: " + flag_gg);
                                break;
                            }
                            Log.w("1abc", "Username of Username: " + user.userName);
                        }
                        if (flag_gg == true) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                            DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                            DatabaseReference usersRef = ref.child("All_Users_Info_Database").child("users").child(user.getUid()).child("userName");
                            usersRef.setValue(usernamegg);
                            Intent intent = new Intent(getApplicationContext(), MainScreenActivity.class);
                            startActivity(intent);
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
        });

        usernamedialog.setOnCancelListener(
                new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        //When you touch outside of dialog bounds,
                        //the dialog gets canceled and this method executes.
                        signOutGoogle();
                    }
                }
        );

    }

    public void resend_email_verification() {
        final FirebaseUser user = mAuth.getCurrentUser();
        user.sendEmailVerification()
                .addOnCompleteListener(Login.this, new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(Login.this,
                                    "Verification email sent to " + user.getEmail(),
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Log.e(TAG, "sendEmailVerification", task.getException());
                            Toast.makeText(getApplicationContext(),
                                    "Failed to send verification email.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void sign_out() {
        FirebaseUser user = mAuth.getCurrentUser();
        mAuth.getInstance().signOut();
        if (mAuth.getCurrentUser() == null) {
            Log.d("1abc", "sign_out");
        }
        Log.d("1abc", "Sign_Out" + mAuth.getCurrentUser());

    }

    //////////////////Google Authentication/////////////////////////////////
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.w("1abc", "Googlesignin1");
                firebaseAuthWithGoogle(account);
                Log.w("1abc", "Googlesignin2");
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(),"Google Sign In Failed: "+e.getMessage(),Toast.LENGTH_SHORT).show();
                Log.w("1abc", "Google sign in failed", e);
                // [START_EXCLUDE]
                //        updateUI(null);
                // [END_EXCLUDE]
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());
        // [START_EXCLUDE silent]
        // showProgressDialog();
        // [END_EXCLUDE]
        Log.w("1abc", "Googlesignin Auth withgg");

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "signInWithCredential:success");
                            Toast.makeText(Login.this, "Login with Google Success, go to main page.", Toast.LENGTH_SHORT).show();
                            boolean isNew = task.getResult().getAdditionalUserInfo().isNewUser();
                            Log.d("1abc", "onComplete: " + isNew);
                            if (isNew == true) {
                                FirebaseUser user = mAuth.getCurrentUser();
                                FirebaseDatabase database = FirebaseDatabase.getInstance();
                                DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                                DatabaseReference usersRef = ref.child("All_Users_Info_Database").child("users");
                                String userId = usersRef.push().getKey();
                                final Map<String, Object> dataMap = new HashMap<String, Object>();
                                String fullname = user.getDisplayName();
                                String email = user.getEmail();
                                Log.d("1abc", fullname + " " + email);
                                Users temp;
                                temp = new Users("-1", user.getEmail(), user.getDisplayName(), "Default_avatarURL", "Default_Dateofbirth");
                                usersRef.child(user.getUid()).setValue(temp);
                            }
                            FirebaseUser user1 = mAuth.getCurrentUser();
                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                            DatabaseReference ref = database.getReference();
                            progressDialog.dismiss();
                            ref.child("All_Users_Info_Database").child("users").child(user1.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    String username = (String) dataSnapshot.child("userName").getValue();
                                    Log.d("1abc","Check username: "+username);
                                    if (username.equals("-1")) {
                                        verifyUsername();
                                    }
                                    else{
                                        Intent intent = new Intent(getApplicationContext(), MainScreenActivity.class);
                                        startActivity(intent);
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

                            FirebaseUser user = mAuth.getCurrentUser();
                            Log.d(TAG, "signInWithCredential: ID " + user.getUid());
                            //          updateUI(user);
                        } else {
                            progressDialog.dismiss();
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(getApplicationContext(), "Sign In Fails", Toast.LENGTH_SHORT).show();
                            //            Snackbar.make(findViewById(R.id.main_layout), "Authentication Failed.", Snackbar.LENGTH_SHORT).show();
                            //        updateUI(null);
                        }

                        // [START_EXCLUDE]
                        //       hideProgressDialog();
                        // [END_EXCLUDE]
                    }
                });
    }
    // [END auth_with_google]

    // [START signin]
    public void signInGoogle() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    // [END signin]
    //Success at signout google
    private void signOutGoogle() {
        // Firebase sign out
        mAuth.signOut();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        // Google sign out
        mGoogleSignInClient.signOut().addOnCompleteListener(this,
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                    }
                });
    }
    ///////////////////////////End of Google Authentication//////////////////////////////////////

}

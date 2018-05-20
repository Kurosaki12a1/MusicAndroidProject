package com.bku.musicandroid.Activity;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatCheckBox;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bku.musicandroid.R;
import com.bku.musicandroid.Model.Users;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
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
import com.sdsmdg.tastytoast.TastyToast;


import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.content.ContentValues.TAG;

public class LoginActivity extends Activity {
    public static String ROLES;
    private EditText emailTxt;
    private EditText passTxt;
    private TextView forgotPassword;
    private TextView signUp;
    private Button loginBtn;
    private SignInButton loginGoogleBtn;
    private FirebaseAuth mAuth;
    private ProgressDialog progressDialog;
    private Dialog dialog;
    private Dialog usernameDialog;
    private GoogleSignInClient mGoogleSignInClient;
    private GoogleApiClient mGoogleApiClient;
    private SharedPreferences sharedPreferences;
    private AppCompatCheckBox chkRememberPassword;
    private static final int RC_SIGN_IN = 9001;

    public static boolean isAtLogin = false;

    /**
     * Function: onResume
     * Created by: SonPhan 26/04/2018
     * Purpose: 1. Restore email and password from sharedpreferences
     * Description:
     */
    @Override
    protected void onResume() {
        super.onResume();
        sharedPreferences = getSharedPreferences("RememberPassword", MODE_PRIVATE);
        emailTxt.setText(sharedPreferences.getString("Email", ""));
        passTxt.setText(sharedPreferences.getString("Password", ""));
        chkRememberPassword.setChecked(sharedPreferences.getBoolean("IsChecked", false));

        isAtLogin = true;

    }

    /**
     * Function: onResume
     * Created by: SonPhan 26/04/2018
     * Purpose: 1. Save email and password into sharedpreferences
     * Description:
     */
    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if (chkRememberPassword.isChecked()) {
            editor.putBoolean("IsChecked", true);
            editor.putString("Email", emailTxt.getText().toString());
            editor.putString("Password", passTxt.getText().toString());
        } else {
            editor.putBoolean("IsChecked", false);
            editor.putString("Email", "");
            editor.putString("Password", "");
        }
        editor.apply();

        isAtLogin = false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.setContentView(R.layout.activity_login);
        checkAlreadyLogin();
        emailTxt = findViewById(R.id.email_txt);
        passTxt = findViewById(R.id.pass_txt);
        chkRememberPassword = findViewById(R.id.chkRememberPassword);
        loginBtn = findViewById(R.id.login_btn);
        forgotPassword = findViewById(R.id.forgot_pass_txt);
        signUp = findViewById(R.id.signup_txt);
        loginGoogleBtn = findViewById(R.id.login_google_btn);
        //Google login
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        // [END config_signin]
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        mGoogleApiClient.connect();

        ///////////////////////////////////////
        // [START initialize_auth]
        mAuth = FirebaseAuth.getInstance();
        Log.d("1abc", "Check: " + mAuth.getCurrentUser());
        if (mAuth.getCurrentUser() != null)
            Log.d("1abc", "Check: " + mAuth.getCurrentUser() + " " + mAuth.getCurrentUser().getUid());
        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, ForgetPasswordActivity.class);
                startActivity(intent);
            }
        });
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Begin to initialize the Progress dialog
                progressDialog = new ProgressDialog(LoginActivity.this);
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
                    TastyToast.makeText(getApplicationContext(), "Co nguoi da login", TastyToast.LENGTH_SHORT, TastyToast.WARNING).show();
                }
                signIn(emailTxt.getText().toString().trim(), passTxt.getText().toString().trim());
            }
        });
        loginGoogleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent login_google=new Intent(LoginActivity.this,Login_Google.class);
                // startActivity(login_google);
                progressDialog = new ProgressDialog(LoginActivity.this);
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
                                TastyToast.makeText(getApplicationContext(), "LoginActivity success", TastyToast.LENGTH_SHORT, TastyToast.SUCCESS).show();
                                Intent intent = new Intent(getApplicationContext(), MainScreenActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        } else {
                            // If sign in fails, display a message to the user.
                            progressDialog.dismiss(); //Log in done, close the progress dialog
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            TastyToast.makeText(getApplicationContext(), "Authentication failed.",
                                    TastyToast.LENGTH_SHORT, TastyToast.ERROR).show();
                        }
                    }
                });
    }

    private boolean validateForm() {
        boolean valid = true;

        String email = emailTxt.getText().toString();
        if (TextUtils.isEmpty(email)) {
            emailTxt.setError("Required.");
            valid = false;
        } else {
            emailTxt.setError(null);
        }

        String password = passTxt.getText().toString();
        if (TextUtils.isEmpty(password)) {
            passTxt.setError("Required.");
            valid = false;
        } else {
            passTxt.setError(null);
        }

        return valid;
    }

    public void showDialog() {
        dialog = new Dialog(LoginActivity.this);
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
                signOut();
                dialog.dismiss();
            }
        });
        resend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resendEmailVerification();
                signOut();
                dialog.dismiss();
            }
        });
        dialog.setOnCancelListener(
                new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        //When you touch outside of dialog bounds,
                        //the dialog gets canceled and this method executes.
                        signOut();
                    }
                }
        );

    }

    public void verifyUsername() {
        usernameDialog = new Dialog(LoginActivity.this);
        usernameDialog.setTitle("Thêm Username");
        usernameDialog.setContentView(R.layout.dialog_add_username);
        final EditText usernameGoogle_txt = usernameDialog.findViewById(R.id.usernamegg_txt);
        usernameDialog.show();
        Button accept;
        accept = (Button) usernameDialog.findViewById(R.id.accept_btn);
        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog = new ProgressDialog(LoginActivity.this);
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
                        Pattern p = Pattern.compile("[^A-Za-z0-9_]", Pattern.CASE_INSENSITIVE);
                        Matcher m = p.matcher(usernamegg);
                        boolean b = m.find();
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
                            finish();
                        } else {
                            progressDialog.dismiss();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        // Failed to read value
                        progressDialog.dismiss();
                        TastyToast.makeText(getApplicationContext(), "Error: " + error.toException(), TastyToast.LENGTH_SHORT, TastyToast.ERROR).show();
                    }
                });
            }
        });

        usernameDialog.setOnCancelListener(
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

    public void resendEmailVerification() {
        final FirebaseUser user = mAuth.getCurrentUser();
        user.sendEmailVerification()
                .addOnCompleteListener(LoginActivity.this, new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()) {
                            TastyToast.makeText(LoginActivity.this,
                                    "Verification email sent to " + user.getEmail(),
                                    TastyToast.LENGTH_SHORT, TastyToast.INFO).show();
                        } else {
                            Log.e(TAG, "sendEmailVerification", task.getException());
                            TastyToast.makeText(getApplicationContext(),
                                    "Failed to send verification email.",
                                    TastyToast.LENGTH_SHORT, TastyToast.INFO).show();
                        }
                    }
                });
    }

    public void signOut() {
        FirebaseUser user = mAuth.getCurrentUser();
        mAuth.getInstance().signOut();
        if (mAuth.getCurrentUser() == null) {
        }

    }

    //////////////////Google Authentication/////////////////////////////////
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
                mGoogleApiClient.clearDefaultAccountAndReconnect();
            }
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
                TastyToast.makeText(getApplicationContext(), "Google Sign In Failed: " + e.getMessage(), TastyToast.LENGTH_SHORT, TastyToast.ERROR).show();
                Log.w("1abc", "Google sign in failed", e);
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        // [START_EXCLUDE silent]
        // showProgressDialog();
        // [END_EXCLUDE]

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "signInWithCredential:success");
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
                                temp = new Users("-1", user.getEmail(), user.getDisplayName(), "Default_avatarURL", "Default_Dateofbirth", "");
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
                                    Log.d("1abc", "Check username: " + username);
                                    if (username.equals("-1")) {
                                        verifyUsername();
                                    } else {
                                        Intent intent = new Intent(getApplicationContext(), MainScreenActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError error) {
                                    // Failed to read value
                                    Log.w("1abc", "Failed to read value.", error.toException());
                                    progressDialog.dismiss();
                                    TastyToast.makeText(getApplicationContext(), "Error: " + error.toException(), TastyToast.LENGTH_SHORT, TastyToast.ERROR).show();
                                }
                            });

                            FirebaseUser user = mAuth.getCurrentUser();
                            //          updateUI(user);
                        } else {
                            progressDialog.dismiss();
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            TastyToast.makeText(getApplicationContext(), "Sign In Fails", TastyToast.LENGTH_SHORT, TastyToast.ERROR).show();
                        }

                    }
                });
    }

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

    public void checkAlreadyLogin() {
        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() != null) {
            Intent intent = new Intent(LoginActivity.this, MainScreenActivity.class);
            startActivity(intent);
            finish();
        }
    }
}

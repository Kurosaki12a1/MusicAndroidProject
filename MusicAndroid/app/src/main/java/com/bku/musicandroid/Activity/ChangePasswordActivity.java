package com.bku.musicandroid.Activity;


import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
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
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import static android.content.ContentValues.TAG;

public class ChangePasswordActivity extends Activity {
    private EditText oldpassTxt;
    private EditText newpassTxt;
    private EditText confirmNewPassTxt;
    private Button changePass_btn;
    private SignInButton login_google_btn;
    private FirebaseAuth mAuth;
    private ProgressDialog progressDialog;
    private Dialog dialog;
    private GoogleSignInClient mGoogleSignInClient;
    private Boolean validFlag;
    private static final int RC_SIGN_IN = 9001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.setContentView(R.layout.activity_change_password);
        changePass_btn = findViewById(R.id.changePass_btn);
        Log.d("1abc","vo change password");
        changePass_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog = new ProgressDialog(ChangePasswordActivity.this);
                progressDialog.setTitle("Đổi mật khẩu");
                progressDialog.setMessage("Đang xử lý...");
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDialog.show();
                progressDialog.setCancelable(false);
                changePassword();
            }
        });
    }

    private boolean changePassword() {
        final String oldpass, newpass, confirmnewpass;
        mAuth = FirebaseAuth.getInstance();
        final FirebaseUser user = mAuth.getCurrentUser();
        validFlag = true;
        oldpassTxt = findViewById(R.id.oldpass_txt);
        newpassTxt = findViewById(R.id.newpass_txt);
        confirmNewPassTxt = findViewById(R.id.confirmnewpass_txt);
        oldpass = oldpassTxt.getText().toString();
        newpass = newpassTxt.getText().toString();
        confirmnewpass = confirmNewPassTxt.getText().toString();
        AuthCredential credential;
        credential = EmailAuthProvider
                .getCredential(user.getEmail(), oldpass);
        validFlag = true;
// Prompt the user to re-provide their sign-in credentials
        user.reauthenticate(credential)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            if (TextUtils.isEmpty(newpass) || newpass.length() < 6) {
                                newpassTxt.setError("Too short");
                                validFlag = false;
                            } else {
                                newpassTxt.setError(null);
                            }
                            if (confirmnewpass.length() < 6 || !confirmnewpass.equals(newpass)) {
                                confirmNewPassTxt.setError("Not same as the new password");
                                validFlag = false;
                            } else {
                                confirmNewPassTxt.setError(null);
                            }
                            if (validFlag == true) {
                                user.updatePassword(newpass).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        progressDialog.dismiss();
                                        if (task.isSuccessful()) {
                                            Toast.makeText(getApplicationContext(), "Changing passsword successfully.", Toast.LENGTH_SHORT).show();
                                            finish();
                                        } else {
                                            Toast.makeText(getApplicationContext(), "Error occured, changing password failed.", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }else{
                                progressDialog.dismiss();
                            }
                        } else {
                            progressDialog.dismiss();
                            Log.d(TAG, "Error auth failed");
                            oldpassTxt.setError("Type your old password");
                            Toast.makeText(getApplicationContext(), "Verify old password failed. If you logged in by Google account, please change password on account.google.com.", Toast.LENGTH_SHORT).show();
                        }

                    }
                });


        return validFlag;
    }
}

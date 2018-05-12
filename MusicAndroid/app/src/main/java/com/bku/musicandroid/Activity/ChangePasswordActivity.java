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
    private EditText oldpass_txt;
    private EditText newpass_txt;
    private EditText confirmnewpass_txt;
    private Button changePass_btn;
    private SignInButton login_google_btn;
    private FirebaseAuth mAuth;
    private ProgressDialog progressDialog;
    private Dialog dialog;
    private GoogleSignInClient mGoogleSignInClient;
    private Boolean valid_flag;
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
        valid_flag = true;
        oldpass_txt = findViewById(R.id.oldpass_txt);
        newpass_txt = findViewById(R.id.newpass_txt);
        confirmnewpass_txt = findViewById(R.id.confirmnewpass_txt);
        oldpass = oldpass_txt.getText().toString();
        newpass = newpass_txt.getText().toString();
        confirmnewpass = confirmnewpass_txt.getText().toString();
        AuthCredential credential;
        Log.d("1abc", "check change password verify old pass: " + user.getEmail() + " " + oldpass);
        credential = EmailAuthProvider
                .getCredential(user.getEmail(), oldpass);
        valid_flag = true;
// Prompt the user to re-provide their sign-in credentials
        user.reauthenticate(credential)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            if (TextUtils.isEmpty(newpass) || newpass.length() < 6) {
                                newpass_txt.setError("Quá ngắn");
                                valid_flag = false;
                            } else {
                                newpass_txt.setError(null);
                            }
                            if (confirmnewpass.length() < 6 || !confirmnewpass.equals(newpass)) {
                                confirmnewpass_txt.setError("Không đúng");
                                valid_flag = false;
                            } else {
                                confirmnewpass_txt.setError(null);
                            }
                            if (valid_flag == true) {
                                user.updatePassword(newpass).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        progressDialog.dismiss();
                                        if (task.isSuccessful()) {
                                            Toast.makeText(getApplicationContext(), "Mật khẩu đã được cập nhật.", Toast.LENGTH_SHORT).show();
                                            Log.d(TAG, "Password updated");
                                        } else {
                                            Toast.makeText(getApplicationContext(), "Lỗi xảy ra, mật khẩu chưa cập nhật.", Toast.LENGTH_SHORT).show();
                                            Log.d(TAG, "Error password not updated");
                                        }
                                    }
                                });
                            }else{
                                progressDialog.dismiss();
                            }
                        } else {
                            progressDialog.dismiss();
                            Log.d(TAG, "Error auth failed");
                            oldpass_txt.setError("Nhập lại mật khẩu cũ");
                            Toast.makeText(getApplicationContext(), "Xác thực mật khẩu cũ thất bại.", Toast.LENGTH_SHORT).show();
                        }

                    }
                });


        return valid_flag;
    }
}

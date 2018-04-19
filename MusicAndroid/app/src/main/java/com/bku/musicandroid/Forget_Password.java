package  com.bku.musicandroid;

import android.os.Bundle;
import android.app.Activity;
import android.support.annotation.NonNull;
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
import com.google.firebase.auth.FirebaseAuth;

import org.w3c.dom.Text;

/**
 * Created by Administrator on 3/21/2018.
 */

public class Forget_Password extends Activity {
    private EditText email_txt;
    private Button forget_btn;
    private TextView result_txt;
    private String emailAddress;
    private FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.setContentView(R.layout.activity_forget_password);
        email_txt=findViewById(R.id.email_txt);
        forget_btn=findViewById(R.id.forget_btn);
        result_txt=findViewById(R.id.result_txt);
        auth = FirebaseAuth.getInstance();
        Log.d("1abc","forget pass: "+emailAddress);
        forget_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                emailAddress = email_txt.getText().toString().trim();
                if(TextUtils.isEmpty(emailAddress)) {
                    Toast.makeText(getApplicationContext(), "Please type your Email!", Toast.LENGTH_SHORT).show();
                }
                else
                forget_password();
            }
        });

    }
    protected void forget_password(){
                auth.sendPasswordResetEmail(emailAddress)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    result_txt.setText("An verify mail was sent to your email. Please check that mail to reset password.");
                                }
                                else{
                                    result_txt.setText("Error occurs. Please make sure type your email correctly.");
                                }
                            }
                        });
    }
}

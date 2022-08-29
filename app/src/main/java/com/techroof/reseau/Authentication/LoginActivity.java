package com.techroof.reseau.Authentication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.techroof.reseau.Creator.CreatorHomeActivity;
import com.techroof.reseau.Supporter.SupporterHomeActivity;
import com.techroof.reseau.R;

import io.paperdb.Paper;

public class LoginActivity extends AppCompatActivity {

    private String userType;
    private EditText emailEt, passwordEt;
    private Button loginBtn;
    private TextView forgetText, noAccText;
    private FirebaseAuth mAuth;
    private ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Paper.init(this);


        userType = Paper.book("userType").read("type").toString();

        mAuth = FirebaseAuth.getInstance();

        pd=new ProgressDialog(this);
        pd.setCanceledOnTouchOutside(false);
        pd.setMessage("Please wait...");

        emailEt = findViewById(R.id.email_et);
        passwordEt = findViewById(R.id.password_et);
        loginBtn = findViewById(R.id.login_btn);
        forgetText = findViewById(R.id.forget_pass_text);
        noAccText = findViewById(R.id.no_account_text);

        noAccText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent reg=new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(reg);
                finish();

            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email = emailEt.getText().toString();
                String password = passwordEt.getText().toString();

                if (TextUtils.isEmpty(email)) {

                    emailEt.setError("Enter Email");
                }
                if (TextUtils.isEmpty(password)) {

                    passwordEt.setError("Enter Password");

                }

                if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {

                    userLogin(email, password);

                }


            }
        });

    }

    private void userLogin(String email, String password) {

        pd.show();

        mAuth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()){

                    if (mAuth.getCurrentUser().isEmailVerified()) {

                        if (userType.equals("supporter")) {

                            Intent home = new Intent(LoginActivity.this,
                                    SupporterHomeActivity.class);
                            startActivity(home);
                            finish();

                            pd.dismiss();

                        } else if (userType.equals("creator")) {

                            Intent home = new Intent(LoginActivity.this,
                                    CreatorHomeActivity.class);
                            startActivity(home);
                            finish();

                            pd.dismiss();

                        }

                    }else{
                        Toast.makeText(getApplicationContext(), "Your Email is not Verified", Toast.LENGTH_SHORT).show();
                        pd.dismiss();
                    }
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        Toast.makeText(LoginActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                        pd.dismiss();
                    }
                });
    }
}
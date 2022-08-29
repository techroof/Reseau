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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.techroof.reseau.Creator.CreatorHomeActivity;
import com.techroof.reseau.R;
import com.techroof.reseau.Supporter.SupporterHomeActivity;

import java.util.HashMap;

import io.paperdb.Paper;

public class RegisterActivity extends AppCompatActivity {

    private TextView loginText;
    private Button regBtn;
    private EditText userNameEt,emailEt,dobET, passEt, confirmPassEt;
    private String name, regEmail, regPass, imgURL, confirmPass, userType, dob;
    private FirebaseAuth mAuth;
    private ProgressDialog pd;
    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        userType = Paper.book("userType").read("type").toString();

        regBtn = findViewById(R.id.create_acc_btn);
        emailEt = findViewById(R.id.reg_email_et);
        passEt = findViewById(R.id.reg_password_et);
        confirmPassEt = findViewById(R.id.confirm_pass_et);
        userNameEt = findViewById(R.id.username_et);
        dobET = findViewById(R.id.dob_et);
        loginText=findViewById(R.id.login_text);

        mAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        pd = new ProgressDialog(this);
        pd.setCanceledOnTouchOutside(false);
        pd.setMessage("Creating your account...");

        loginText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent reg = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(reg);
                finish();
            }
        });

        regBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                name = userNameEt.getText().toString();
                regEmail = emailEt.getText().toString();
                regPass = passEt.getText().toString();
                confirmPass = confirmPassEt.getText().toString();
                dob = dobET.getText().toString();

                dob="dob";

                if (TextUtils.isEmpty(name)) {
                    userNameEt.setError("Enter Name");
                }
                if (TextUtils.isEmpty(regEmail)) {
                    emailEt.setError("Enter Email");
                }
                if (TextUtils.isEmpty(regPass)) {
                    passEt.setError("Enter Password");
                }
                if (TextUtils.isEmpty(confirmPass)) {
                    confirmPassEt.setError("Confirm Password");

                }
                if (TextUtils.isEmpty(dob)) {
                    dobET.setError("Enter Date of Birth");

                }

                if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(regEmail) && !TextUtils.isEmpty(regPass)
                        && !TextUtils.isEmpty(confirmPass) && !TextUtils.isEmpty(dob))
                {

                    if (regPass.equals(confirmPass)) {

                        pd.show();
                        register(name, regEmail, regPass, dob);

                    } else {
                        confirmPassEt.setError("Password doesn't match");
                    }

                }
            }
        });
    }

    private void register(final String name, final String email, final String password, final String dob) {

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        // pd.show();
                        if (task.isSuccessful()) {

                            pd.dismiss();
                            imgURL = "https://firebasestorage.googleapis.com/v0/b/reseau-12cdf.appspot.com/o/avatar-1.jpeg?alt=media&token=a55a503f-4975-477f-8aa8-0e79a20c50a9";
                            final HashMap<String, String> userMap = new HashMap();
                            userMap.put("name", name);
                            userMap.put("email", email);
                            userMap.put("password", password);
                            userMap.put("userId", mAuth.getCurrentUser().getUid());
                            userMap.put("user_type", userType);
                            userMap.put("level", "N/A");
                            userMap.put("reviews", "N/A");
                            userMap.put("ratings", "0.0");
                            userMap.put("image", imgURL);
                            userMap.put("dob", dob);
                            userMap.put("shippingAddress", "N/A");

                            firestore.collection("users")
                                    .document(mAuth.getCurrentUser().getUid())
                                    .set(userMap)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {

                                            if (task.isSuccessful()){

                                                verificationEmail();

                                            }
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(RegisterActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                            pd.dismiss();
                                        }
                                    });

                        }
                    }
                });
    }

    private void verificationEmail() {

        final FirebaseUser user = mAuth.getCurrentUser();

        user.sendEmailVerification().addOnCompleteListener(this, new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                // Re-enable button
                //findViewById(R.id.verify_email_button).setEnabled(true);

                if (task.isSuccessful()) {

                    pd.dismiss();
                    Toast.makeText(getApplicationContext(),
                            "Verification email sent to " + user.getEmail(),
                            Toast.LENGTH_SHORT).show();

                    Intent home = new Intent(RegisterActivity.this, LoginActivity.class);
                    startActivity(home);
                    finish();

                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                pd.dismiss();
                Toast.makeText(getApplicationContext(),
                        "Failed to send verification email.",
                        Toast.LENGTH_SHORT).show();
            }
        });

    }
}
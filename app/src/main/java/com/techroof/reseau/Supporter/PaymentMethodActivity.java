package com.techroof.reseau.Supporter;

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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.techroof.reseau.Model.PaymentDetails;
import com.techroof.reseau.R;

import java.util.HashMap;
import java.util.Map;

public class PaymentMethodActivity extends AppCompatActivity {

    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private EditText cardNumEt,cardNameEt,expireDateEt,cvvEt;
    private Button saveBtn;
    private TextView toolbarTitle;
    private ImageView backBtn;
    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_method);

        saveBtn=findViewById(R.id.save_card_btn);
        cardNameEt=findViewById(R.id.card_holder_name_et);
        cardNumEt=findViewById(R.id.card_num_et);
        expireDateEt=findViewById(R.id.card_expiry_et);
        cvvEt=findViewById(R.id.card_cvv_et);
        backBtn=findViewById(R.id.back_btn);
        toolbarTitle=findViewById(R.id.toolbar_title);

        toolbarTitle.setText("Payment Method");

        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");
        progressDialog.setCanceledOnTouchOutside(false);

        db=FirebaseFirestore.getInstance();
        mAuth=FirebaseAuth.getInstance();

        getPaymentDetails();

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String cardNum=cardNumEt.getText().toString();
                String cardName=cardNameEt.getText().toString();
                String cvv=cvvEt.getText().toString();
                String expireDate=expireDateEt.getText().toString();

                if (TextUtils.isEmpty(cardName)){
                    cardNameEt.setError("Enter Name");
                }

                if (TextUtils.isEmpty(cardNum)){
                    cardNumEt.setError("Enter Card Number");
                }

                if (TextUtils.isEmpty(cvv)){
                    cvvEt.setError("Enter CVV Code");
                }

                if (TextUtils.isEmpty(expireDate)){
                    expireDateEt.setError("Enter Card Expire Date");
                }

                if (!TextUtils.isEmpty(cardName) &&
                        !TextUtils.isEmpty(cardNum)&&
                        !TextUtils.isEmpty(cvv) &&
                        !TextUtils.isEmpty(expireDate)){

                    addPaymentInfo(cardName,cardNum,cvv,expireDate);

                }

            }
        });



    }

    private void addPaymentInfo(String cardName, String cardNum, String cvv, String expireDate) {

        progressDialog.show();

        Map<String,Object> paymentMap=new HashMap<>();
        paymentMap.put("paymentMethodName","Credit/Debit Card");
        paymentMap.put("paymentMethodTitle",cardName);
        paymentMap.put("cardNum",cardNum);
        paymentMap.put("cardHolderName",cardName);
        paymentMap.put("cvv",cvv);
        paymentMap.put("expiryDate",expireDate);
        paymentMap.put("uId",mAuth.getCurrentUser().getUid());

        db.collection("users")
                .document(mAuth.getCurrentUser().getUid())
                .collection("paymentMethod")
                .document("paymentMethod")
                .set(paymentMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (task.isSuccessful()){
                            Intent checkout=new Intent(PaymentMethodActivity.this,CheckOutActivity.class);
                            checkout.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(checkout);
                            progressDialog.dismiss();
                            finish();
                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        Toast.makeText(PaymentMethodActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();

                    }
                });

    }

    private void getPaymentDetails() {

        db.collection("users")
                .document(mAuth.getCurrentUser().getUid())
                .collection("paymentMethod")
                .document("paymentMethod")
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                        if (task.isSuccessful()){

                            if (task.getResult().exists()){

                                PaymentDetails paymentDetails=task.getResult().toObject(PaymentDetails.class);

                                cardNameEt.setText(""+paymentDetails.getCardHolderName());
                                cardNumEt.setText(""+paymentDetails.getCardNum());
                                cvvEt.setText(""+paymentDetails.getCvv());
                                expireDateEt.setText(""+paymentDetails.getExpiryDate());

                            }

                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        Toast.makeText(PaymentMethodActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });

    }
}
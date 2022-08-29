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
import com.techroof.reseau.Model.ShippingAddress;
import com.techroof.reseau.R;

import java.util.HashMap;
import java.util.Map;

public class ShippingDetailsActivity extends AppCompatActivity {

    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private EditText shippingNameEt,shippingPhoneEt,shippingAddressEt;
    private Button saveBtn;
    private TextView toolbarTitle;
    private ImageView backBtn;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shipping_details);

        backBtn=findViewById(R.id.back_btn);
        toolbarTitle=findViewById(R.id.toolbar_title);
        shippingNameEt=findViewById(R.id.shipping_name_et);
        shippingPhoneEt=findViewById(R.id.shipping_phone_et);
        shippingAddressEt=findViewById(R.id.shipping_full_address_et);
        saveBtn=findViewById(R.id.add_shipping_details_btn);

        toolbarTitle.setText("Shipping Details");

        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");
        progressDialog.setCanceledOnTouchOutside(false);

        db=FirebaseFirestore.getInstance();
        mAuth=FirebaseAuth.getInstance();

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String shippingName=shippingNameEt.getText().toString();
                String shippingAddress=shippingAddressEt.getText().toString();
                String shippingPhone=shippingPhoneEt.getText().toString();

                if (TextUtils.isEmpty(shippingName)){
                    shippingNameEt.setError("Enter Name");
                }

                if (TextUtils.isEmpty(shippingPhone)){
                    shippingPhoneEt.setError("Enter Phone Number");
                }

                if (TextUtils.isEmpty(shippingAddress)){
                    shippingAddressEt.setError("Enter Shipping Address");
                }

                if (!TextUtils.isEmpty(shippingName) &&
                        !TextUtils.isEmpty(shippingPhone)&&
                        !TextUtils.isEmpty(shippingAddress)){

                    addPaymentInfo(shippingName,shippingPhone,shippingAddress);

                }
            }
        });

        getShippingDetails();
    }

    private void addPaymentInfo(String name, String phone, String address) {

        progressDialog.show();

        Map<String,Object> paymentMap=new HashMap<>();
        paymentMap.put("name",name);
        paymentMap.put("address",phone);
        paymentMap.put("phone",address);
        paymentMap.put("uId",mAuth.getCurrentUser().getUid());

        db.collection("users")
                .document(mAuth.getCurrentUser().getUid())
                .collection("shippingDetails")
                .document("shippingDetails")
                .set(paymentMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (task.isSuccessful()){
                            Intent checkout=new Intent(ShippingDetailsActivity.this,CheckOutActivity.class);
                            checkout.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(checkout);
                            progressDialog.dismiss();
                            finish();
                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        Toast.makeText(ShippingDetailsActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();

                    }
                });

    }

    private void getShippingDetails() {

        db.collection("users")
                .document(mAuth.getCurrentUser().getUid())
                .collection("shippingDetails")
                .document("shippingDetails")
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                        if (task.isSuccessful()){

                            if (task.getResult().exists()){

                                ShippingAddress shippingAddress=task.getResult().toObject(ShippingAddress.class);

                                shippingNameEt.setText(""+shippingAddress.getName());
                                shippingAddressEt.setText(""+shippingAddress.getAddress());
                                shippingPhoneEt.setText(""+shippingAddress.getPhone());

                            }

                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        Toast.makeText(ShippingDetailsActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });

    }

}
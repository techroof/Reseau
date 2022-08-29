package com.techroof.reseau.Creator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.techroof.reseau.Adapter.Creator.AllProductsAdapter;
import com.techroof.reseau.Model.Products;
import com.techroof.reseau.R;

import java.util.ArrayList;

public class AllProductsActivity extends AppCompatActivity {

    private RecyclerView allProdsRv;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<Products> productsArrayList;
    private AllProductsAdapter adapter;
    private ProgressDialog progressDialog;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private String uId;
    private TextView addNewProductText;
    private ImageView backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_products);

        allProdsRv=findViewById(R.id.all_products_rv);
        addNewProductText=findViewById(R.id.add_new_products_text);
        backBtn=findViewById(R.id.back_btn);

        layoutManager=new LinearLayoutManager(this);
        allProdsRv.setLayoutManager(layoutManager);
        allProdsRv.setHasFixedSize(true);

        productsArrayList=new ArrayList<>();

        progressDialog=new ProgressDialog(this);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        db=FirebaseFirestore.getInstance();
        mAuth=FirebaseAuth.getInstance();

        uId=mAuth.getCurrentUser().getUid();

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        addNewProductText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent newProduct=new Intent(AllProductsActivity.this,AddProductsActivity.class);
                startActivity(newProduct);

            }
        });

        db.collection("products")
                .whereEqualTo("creator",uId)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        for (DocumentSnapshot documentSnapshot:queryDocumentSnapshots.getDocuments()){

                            Products products=documentSnapshot.toObject(Products.class);
                            productsArrayList.add(products);

                        }

                        adapter=new AllProductsAdapter(AllProductsActivity.this,productsArrayList);
                        allProdsRv.setAdapter(adapter);
                        progressDialog.dismiss();

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        progressDialog.dismiss();
                        Toast.makeText(AllProductsActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });

    }
}
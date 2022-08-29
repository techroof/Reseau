package com.techroof.reseau.Supporter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.techroof.reseau.Adapter.Creator.AllProductsAdapter;
import com.techroof.reseau.Adapter.Supporter.PreviousOrdersAdapter;
import com.techroof.reseau.Model.Orders;
import com.techroof.reseau.Model.Products;
import com.techroof.reseau.R;

import java.util.ArrayList;

public class SupporterOrdersActivity extends AppCompatActivity {

    private RecyclerView ordersRv;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<Orders> ordersArrayList;
    private PreviousOrdersAdapter adapter;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private String uId;
    private TextView toolbarTitle;
    private ImageView backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supporter_orders);

        toolbarTitle=findViewById(R.id.toolbar_title);
        backBtn=findViewById(R.id.back_btn);
        ordersRv=findViewById(R.id.orders_rv);

        mAuth=FirebaseAuth.getInstance();
        db=FirebaseFirestore.getInstance();

        toolbarTitle.setText("My Orders");

        layoutManager=new LinearLayoutManager(this);

        ordersArrayList=new ArrayList<>();

        ordersRv.setHasFixedSize(true);
        ordersRv.setLayoutManager(layoutManager);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        db.collection("orders")
                .whereEqualTo("supporter",mAuth.getCurrentUser().getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        if (task.isSuccessful()){

                            for (QueryDocumentSnapshot queryDocumentSnapshot:task.getResult()){
                                Orders orders=queryDocumentSnapshot.toObject(Orders.class);
                                ordersArrayList.add(orders);
                            }

                            adapter=new PreviousOrdersAdapter(SupporterOrdersActivity.this,ordersArrayList);
                            ordersRv.setAdapter(adapter);

                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        Toast.makeText(SupporterOrdersActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });

    }
}
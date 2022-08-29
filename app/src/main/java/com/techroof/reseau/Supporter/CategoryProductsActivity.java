package com.techroof.reseau.Supporter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.smarteist.autoimageslider.SliderView;
import com.techroof.reseau.Adapter.SliderAdapter;
import com.techroof.reseau.Adapter.Supporter.CategoryAdapter;
import com.techroof.reseau.Adapter.Supporter.ProductsAdapter;
import com.techroof.reseau.Adapter.Supporter.SuperDealsProductsAdapter;
import com.techroof.reseau.Model.Category;
import com.techroof.reseau.Model.Images;
import com.techroof.reseau.Model.Products;
import com.techroof.reseau.R;

import java.util.ArrayList;

public class CategoryProductsActivity extends AppCompatActivity {

    private RecyclerView categoryProductsRv;
    private RecyclerView.LayoutManager categoriesProductsLayoutManager;
    private ArrayList<Products> productsArrayList;
    private FirebaseFirestore db;
    private ProductsAdapter productsAdapter;
    private String catId,catName;
    private ImageView backBtn;
    private TextView toolbarTitle,noProductsText;
    private ShimmerFrameLayout shimmerFrameLayout;
    private ConstraintLayout allProdsCl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_products);

        categoryProductsRv=findViewById(R.id.category_products_layout);
        backBtn=findViewById(R.id.back_btn);
        toolbarTitle=findViewById(R.id.toolbar_title);
        noProductsText=findViewById(R.id.no_prod_text);
        allProdsCl=findViewById(R.id.all_prods_cl);
        shimmerFrameLayout=findViewById(R.id.shimmer_view_container);

        shimmerFrameLayout.startShimmer();

        catId=getIntent().getStringExtra("category_id");
        catName=getIntent().getStringExtra("category_name");

        toolbarTitle.setText(""+catName);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        productsArrayList=new ArrayList<>();
        db=FirebaseFirestore.getInstance();


        categoriesProductsLayoutManager=new GridLayoutManager(CategoryProductsActivity.this,2);
        categoryProductsRv.setLayoutManager(categoriesProductsLayoutManager);
        categoryProductsRv.setHasFixedSize(true);

        allProducts();

    }

    private void allProducts() {

        db.collection("products")
                .whereEqualTo("productCategoryId",catId)
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                if (queryDocumentSnapshots.isEmpty()){

                    allProdsCl.setVisibility(View.VISIBLE);
                    noProductsText.setVisibility(View.VISIBLE);
                    shimmerFrameLayout.setVisibility(View.GONE);


                }else{

                    for (DocumentSnapshot documentSnapshot:queryDocumentSnapshots.getDocuments()){

                        Products products=documentSnapshot.toObject(Products.class);
                        productsArrayList.add(products);

                    }

                    productsAdapter=new ProductsAdapter(CategoryProductsActivity.this,productsArrayList);
                    categoryProductsRv.setAdapter(productsAdapter);
                    noProductsText.setVisibility(View.GONE);

                    shimmerFrameLayout.setVisibility(View.GONE);
                    allProdsCl.setVisibility(View.VISIBLE);


                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Toast.makeText(CategoryProductsActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }
}
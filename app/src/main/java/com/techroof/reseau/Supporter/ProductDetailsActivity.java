package com.techroof.reseau.Supporter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.skydoves.expandablelayout.ExpandableLayout;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;
import com.techroof.reseau.Adapter.Supporter.SliderAdapterProducts;
import com.techroof.reseau.LocalDB.DatabaseHelper;
import com.techroof.reseau.Model.CustomerReviews;
import com.techroof.reseau.Model.Products;
import com.techroof.reseau.R;

import java.util.ArrayList;

import io.paperdb.Paper;

public class ProductDetailsActivity extends AppCompatActivity {

    private TextView productPriceText, discountPercentText, discountAmountText, productTitleText,
            productRatingsText, totalOrdersText, productDetailsText, totalRatingsText, totalReviewsText,
            quantityText, addToCartText, buyNowText;
    private int quantity=1;
    private ImageButton addBtn, minBtn;
    private ImageView favImg,backBtn;
    private RecyclerView customerReviewRv;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<CustomerReviews> customerReviewsArrayList;
    private ArrayList<Products> productsArrayList;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private ExpandableLayout expandableLayout;
    private boolean expanded = false;
    private SliderView sliderView;
    private SliderAdapterProducts sliderAdapter;
    private String productId;
    private DatabaseHelper dbHelper;
    private Products products;
    private ConstraintLayout detailsCl;
    private ShimmerFrameLayout shimmerFrameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        productDetailsText = findViewById(R.id.products_details_text);
        productPriceText = findViewById(R.id.price_text);
        discountPercentText = findViewById(R.id.discount_text);
        discountAmountText = findViewById(R.id.orignal_price_text);
        productTitleText = findViewById(R.id.title_text);
        productRatingsText = findViewById(R.id.ratings_text);
        totalOrdersText = findViewById(R.id.total_orders_text);
        totalRatingsText = findViewById(R.id.ratings_text2);
        totalReviewsText = findViewById(R.id.customer_reviews_text);
        quantityText = findViewById(R.id.quantity_num_text);
        addToCartText = findViewById(R.id.cart_text);
        buyNowText = findViewById(R.id.buy_now_text);
        addBtn = findViewById(R.id.quantity_add_btn);
        minBtn = findViewById(R.id.quantity_min_btn);
        favImg = findViewById(R.id.fav_btn);
        customerReviewRv = findViewById(R.id.reviews_rv);
        expandableLayout = findViewById(R.id.expandable_specification_layout);
        sliderView = findViewById(R.id.imageSlider);
        backBtn=findViewById(R.id.back_btn);
        detailsCl=findViewById(R.id.products_details_cl);
        shimmerFrameLayout=findViewById(R.id.shimmer_view_container);

        shimmerFrameLayout.startShimmer();

        customerReviewsArrayList = new ArrayList<>();
        productsArrayList = new ArrayList<>();

        layoutManager = new LinearLayoutManager(this);
        customerReviewRv.setLayoutManager(layoutManager);
        customerReviewRv.setHasFixedSize(true);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        dbHelper = new DatabaseHelper(this);

        productId = getIntent().getStringExtra("productId");

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                onBackPressed();

            }
        });

        expandableLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (expanded == false) {
                    expandableLayout.expand();

                    expanded = true;

                } else {
                    expandableLayout.collapse();

                    expanded = false;
                }
            }
        });

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                quantity = quantity + 1;
                quantityText.setText("" + quantity);

            }
        });

        minBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                quantity = quantity - 1;

                if (quantity <= 1) {
                    quantity = 1;
                }

                quantityText.setText("" + quantity);

            }
        });

        addToCartText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                addToCart();

            }
        });

        buyNowText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent checkout=new Intent(ProductDetailsActivity.this,CheckOutActivity.class);
                startActivity(checkout);

            }
        });

        productDetails();

    }

    private void productDetails() {

        db.collection("products")
                .document(productId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                        if (task.isSuccessful()) {

                            products = task.getResult().toObject(Products.class);

                            productsArrayList.add(products);

                            sliderAdapter = new SliderAdapterProducts(ProductDetailsActivity.this, productsArrayList);
                            sliderView.setSliderAdapter(sliderAdapter);
                            sliderView.setIndicatorAnimation(IndicatorAnimationType.WORM); //set indicator animation by using IndicatorAnimationType. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
                            sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
                            sliderView.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);
                            sliderView.setIndicatorSelectedColor(Color.WHITE);
                            sliderView.setIndicatorUnselectedColor(Color.GRAY);
                            sliderView.setScrollTimeInSec(4); //set scroll delay in seconds :
                            sliderView.startAutoCycle();

                            productTitleText.setText(products.getProductTitle());
                            productPriceText.setText("$" + products.getFinalPrice());
                            productDetailsText.setText(products.getProductDesc());
                            discountAmountText.setText("$" + products.getProductPrice());
                            discountPercentText.setText("-" + products.getProductDiscount() + "%");
                            productRatingsText.setText("4.0");
                            totalOrdersText.setText("154");
                            totalRatingsText.setText("N/A");
                            totalReviewsText.setText("Customer Reviews (N/A)");


                            shimmerFrameLayout.setVisibility(View.GONE);
                            detailsCl.setVisibility(View.VISIBLE);


                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        Toast.makeText(ProductDetailsActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });
    }

    private void addToCart() {

        if (checkCart().equals("update")){

            boolean isInserted = dbHelper.updateData(
                    String.valueOf(productId),
                    products.getProductTitle(),
                    products.getFinalPrice(),
                    String.valueOf(quantity),
                    products.getProductImage(),
                    products.getProductCategoryId(),
                    products.getCreator(),"pending");

            if (isInserted) {

                Toast.makeText(getApplicationContext(), "Product updated to cart", Toast.LENGTH_LONG).show();
                //Intent addProduct = new Intent(ProductDetailsActivity.this, HomeActivity.class);
                //startActivity(addProduct);

            } else {
                Toast.makeText(getApplicationContext(), "Error while updating product to cart", Toast.LENGTH_LONG).show();
            }

        }else if(checkCart().equals("add")){

            boolean isInserted = dbHelper.insertData(
                    String.valueOf(productId),
                    products.getProductTitle(),
                    products.getFinalPrice(),
                    String.valueOf(quantity),
                    products.getProductImage(),
                    products.getProductCategoryId(),
                    products.getCreator(),"pending");

            if (isInserted) {

                Toast.makeText(getApplicationContext(), "Product added to cart", Toast.LENGTH_LONG).show();
                //Intent addProduct = new Intent(ProductDetailsActivity.this, HomeActivity.class);
                //startActivity(addProduct);

            } else {
                Toast.makeText(getApplicationContext(), "Error while adding product to cart", Toast.LENGTH_LONG).show();
            }

        }else if(checkCart()==null){
            Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
        }

    }


    public String checkCart(){

        Cursor res = dbHelper.getProductId();

        while (res.moveToNext()) {

            if (res.getString(0).equals(String.valueOf(productId))) {

                return "update";

            }else{

            }

        }
        return "add";
    }
}
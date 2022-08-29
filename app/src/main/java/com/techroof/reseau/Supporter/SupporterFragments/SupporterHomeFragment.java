package com.techroof.reseau.Supporter.SupporterFragments;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
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

import io.paperdb.Paper;

public class SupporterHomeFragment extends Fragment {

    private RecyclerView superDealsRv,categoriesRv,allProductsRv;
    private SliderView sliderView;
    private SliderAdapter sliderAdapter;
    private RecyclerView.LayoutManager superDealLayoutManager,categoriesLayoutManager,allProdsLayoutManager;
    private ArrayList<Category> categoryArrayList;
    private ArrayList<Products> productsArrayList,superDealsArrayList;
    private ArrayList<Images> sliderImagesArrayList;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private SuperDealsProductsAdapter superDealsProductsAdapter;
    private CategoryAdapter categoryAdapter;
    private ProductsAdapter productsAdapter;
    private ConstraintLayout homeCl;
    private ShimmerFrameLayout shimmerFrameLayout;

    public SupporterHomeFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_supporter_home, container, false);

        superDealsRv=view.findViewById(R.id.super_deals_rv);
        categoriesRv=view.findViewById(R.id.categories_rv);
        allProductsRv=view.findViewById(R.id.best_products_rv);
        sliderView=view.findViewById(R.id.imageSlider);
        homeCl=view.findViewById(R.id.home_cl);
        shimmerFrameLayout=view.findViewById(R.id.shimmer_view_container);

        shimmerFrameLayout.startShimmer();

        categoryArrayList=new ArrayList<>();
        productsArrayList=new ArrayList<>();
        superDealsArrayList=new ArrayList<>();
        sliderImagesArrayList=new ArrayList<>();

        mAuth=FirebaseAuth.getInstance();
        db=FirebaseFirestore.getInstance();

        allProdsLayoutManager=new GridLayoutManager(getContext(),2);
        allProductsRv.setLayoutManager(allProdsLayoutManager);
        allProductsRv.setHasFixedSize(true);

        superDealLayoutManager=new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false);
        superDealsRv.setLayoutManager(superDealLayoutManager);
        superDealsRv.setHasFixedSize(true);

        categoriesLayoutManager=new GridLayoutManager(getContext(),4);
        categoriesRv.setLayoutManager(categoriesLayoutManager);
        categoriesRv.setHasFixedSize(true);

        allCategories();
        superDealsProducts();
        allProducts();
        sliderImages();
        return view;
    }

    private void allCategories() {

        db.collection("category").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                for (DocumentSnapshot documentSnapshot:queryDocumentSnapshots.getDocuments()){

                    Category category=documentSnapshot.toObject(Category.class);
                    categoryArrayList.add(category);

                }

                categoryAdapter=new CategoryAdapter(getContext(),categoryArrayList);
                categoriesRv.setAdapter(categoryAdapter);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Toast.makeText(getContext(), ""+e.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

    }

    private void superDealsProducts() {

        db.collection("products").whereEqualTo("superDeal","yes").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                for (DocumentSnapshot documentSnapshot:queryDocumentSnapshots.getDocuments()){

                    Products products=documentSnapshot.toObject(Products.class);
                    superDealsArrayList.add(products);

                }

                superDealsProductsAdapter=new SuperDealsProductsAdapter(getContext(),superDealsArrayList);
                superDealsRv.setAdapter(superDealsProductsAdapter);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Toast.makeText(getContext(), ""+e.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

    }

    private void allProducts() {

        db.collection("products").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                for (DocumentSnapshot documentSnapshot:queryDocumentSnapshots.getDocuments()){

                    Products products=documentSnapshot.toObject(Products.class);
                    productsArrayList.add(products);

                }

                productsAdapter=new ProductsAdapter(getContext(),productsArrayList);
                allProductsRv.setAdapter(productsAdapter);

                shimmerFrameLayout.setVisibility(View.GONE);
                homeCl.setVisibility(View.VISIBLE);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Toast.makeText(getContext(), ""+e.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }


    private void sliderImages(){

        db.collection("sliderImages")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        if (!queryDocumentSnapshots.isEmpty()) {

                            for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()) {

                                Images sliderItems = documentSnapshot.toObject(Images.class);
                                sliderImagesArrayList.add(sliderItems);

                            }

                            sliderAdapter = new SliderAdapter(getContext(), sliderImagesArrayList);
                            sliderView.setSliderAdapter(sliderAdapter);
                            sliderView.setIndicatorAnimation(IndicatorAnimationType.WORM); //set indicator animation by using IndicatorAnimationType. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
                            sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
                            sliderView.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);
                            sliderView.setIndicatorSelectedColor(Color.WHITE);
                            sliderView.setIndicatorUnselectedColor(Color.GRAY);
                            sliderView.setScrollTimeInSec(4); //set scroll delay in seconds :
                            sliderView.startAutoCycle();

                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        Toast.makeText(getContext(), "" + e.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });
    }
}
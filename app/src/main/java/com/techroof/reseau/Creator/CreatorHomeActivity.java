package com.techroof.reseau.Creator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.techroof.reseau.Adapter.Creator.CreatorHomeItemsAdapter;
import com.techroof.reseau.Authentication.LoginActivity;
import com.techroof.reseau.Creator.CreatorFragment.CreatorHomeFragment;
import com.techroof.reseau.Creator.CreatorFragment.CreatorNewsfeedFragment;
import com.techroof.reseau.Model.CreatorHomeItems;
import com.techroof.reseau.R;
import com.techroof.reseau.StartActivity;
import com.techroof.reseau.Supporter.SupporterHomeActivity;

import java.util.ArrayList;

public class CreatorHomeActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private BottomNavigationView btmNav;
    private Fragment fragment;
    private ProgressDialog pd;
    private CreatorHomeFragment homeFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creator_home);

        mAuth= FirebaseAuth.getInstance();

        FirebaseUser user=mAuth.getCurrentUser();

        if (user==null){

            Intent login=new Intent(CreatorHomeActivity.this, LoginActivity.class);
            startActivity(login);
            finish();

        }else{

            btmNav = findViewById(R.id.btm_nav_view);

            pd=new ProgressDialog(this);
            pd.setMessage("Please wait...");
            pd.setCanceledOnTouchOutside(false);
            pd.show();

            homeFragment=new CreatorHomeFragment();


            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.main_fragment, new CreatorHomeFragment()).commit();

            pd.dismiss();


            btmNav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {


                    switch (item.getItemId()) {

                        case R.id.home:
                            fragment = new CreatorHomeFragment();
                            break;

                        case R.id.newsfeed:
                            fragment = new CreatorNewsfeedFragment();
                            break;


                    }

                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.main_fragment, fragment)
                            .commit();

                    return true;

                }
            });

        }

        }
    }

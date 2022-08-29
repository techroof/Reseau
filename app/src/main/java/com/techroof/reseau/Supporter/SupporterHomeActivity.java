package com.techroof.reseau.Supporter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.techroof.reseau.Authentication.LoginActivity;
import com.techroof.reseau.Creator.CreatorFragment.CreatorHomeFragment;
import com.techroof.reseau.Model.Cart;
import com.techroof.reseau.R;
import com.techroof.reseau.StartActivity;
import com.techroof.reseau.Supporter.SupporterFragments.CartFragment;
import com.techroof.reseau.Supporter.SupporterFragments.SupporterAccountFragment;
import com.techroof.reseau.Supporter.SupporterFragments.SupporterConversationsFragment;
import com.techroof.reseau.Supporter.SupporterFragments.SupporterHomeFragment;
import com.techroof.reseau.Supporter.SupporterFragments.SupporterNewsFeedFragment;

public class SupporterHomeActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private BottomNavigationView btmNav;
    private Fragment fragment;
    private ProgressDialog pd;
    private SupporterHomeFragment homeFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supporter_home);

        mAuth=FirebaseAuth.getInstance();

        FirebaseUser user=mAuth.getCurrentUser();

        if (user==null){

            Intent login=new Intent(SupporterHomeActivity.this, LoginActivity.class);
            startActivity(login);
            finish();

        }else{

            btmNav = findViewById(R.id.btm_nav_view);

            pd=new ProgressDialog(this);
            pd.setMessage("Please wait...");
            pd.setCanceledOnTouchOutside(false);
            pd.show();

            homeFragment=new SupporterHomeFragment();


            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.main_fragment, new SupporterHomeFragment()).commit();

            pd.dismiss();


            btmNav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {


                    switch (item.getItemId()) {

                        case R.id.home:
                            fragment = new SupporterHomeFragment();
                            break;

                        case R.id.cart:
                            fragment = new CartFragment();
                            break;

                        case R.id.newsfeed:
                            fragment = new SupporterNewsFeedFragment();
                            break;

                        case R.id.messages:
                            fragment = new SupporterConversationsFragment();
                            break;

                        case R.id.profile:
                            fragment = new SupporterAccountFragment();
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
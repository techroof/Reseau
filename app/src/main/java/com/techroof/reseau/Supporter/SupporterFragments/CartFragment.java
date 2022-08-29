package com.techroof.reseau.Supporter.SupporterFragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.techroof.reseau.Adapter.Supporter.CartAdapter;
import com.techroof.reseau.LocalDB.DatabaseHelper;
import com.techroof.reseau.Model.Cart;
import com.techroof.reseau.R;
import com.techroof.reseau.Supporter.CheckOutActivity;

import java.util.ArrayList;


public class CartFragment extends Fragment {

    private RecyclerView cartRecyclerView;
    private CartAdapter cartItemsAdapter;
    private ArrayList<Cart> cartArrayList;
    private RecyclerView.LayoutManager layoutManager;
    private Button chkOutBtn;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private ProgressDialog pd;
    private TextView emptyCart, totalText;
    private DatabaseHelper dbHelper;
    private Cursor res;
    private int totalPrice = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view= inflater.inflate(R.layout.fragment_cart, container, false);

        chkOutBtn = view.findViewById(R.id.checkout_btn);
        cartRecyclerView = view.findViewById(R.id.cart_rv);
        emptyCart = view.findViewById(R.id.no_items_text);
        totalText = view.findViewById(R.id.total_text);

        pd = new ProgressDialog(getContext());
        pd.setMessage("Loading...");
        pd.setCanceledOnTouchOutside(false);
        pd.show();

        cartRecyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        cartRecyclerView.setLayoutManager(layoutManager);

        cartArrayList = new ArrayList<Cart>();

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        chkOutBtn.setVisibility(View.INVISIBLE);

        dbHelper = new DatabaseHelper(getContext());

        chkOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent chkOut = new Intent(getContext(), CheckOutActivity.class);
                startActivity(chkOut);
            }
        });

        updateCartData();

        return view;
    }

    public void updateCartData() {

        res = dbHelper.getCartProducts();

        if (res.getCount() == 0) {

            emptyCart.setVisibility(View.VISIBLE);
            chkOutBtn.setVisibility(View.INVISIBLE);
            totalText.setVisibility(View.GONE);

            pd.dismiss();
            return;

        } else {

            int sum = 0;
            totalPrice = 0;

            int sumItems = 0, temp = 0;

            emptyCart.setVisibility(View.GONE);
            chkOutBtn.setVisibility(View.VISIBLE);

            while (res.moveToNext()) {

                cartArrayList.add(new Cart(res.getInt(0),
                        res.getString(1),
                        res.getString(2),
                        res.getString(3),
                        res.getString(4),
                        res.getString(5),
                        res.getString(6),
                        res.getString(7),
                        res.getString(8)));

                temp = Integer.parseInt(res.getString(4));
                sumItems = sumItems + temp;

                sum = Integer.parseInt(res.getString(3)) * Integer.parseInt(res.getString(4));
                totalPrice = totalPrice + sum;

            }
            cartItemsAdapter = new CartAdapter(getContext(), cartArrayList,CartFragment.this);
            cartRecyclerView.setAdapter(cartItemsAdapter);

            //priceText.setText("Rs. " + totalPrice);
            totalText.setText("TOTAL: $" + totalPrice);

            pd.dismiss();

        }

    }
}
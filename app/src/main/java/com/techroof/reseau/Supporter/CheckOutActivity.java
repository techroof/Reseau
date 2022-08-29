package com.techroof.reseau.Supporter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.techroof.reseau.Adapter.Supporter.OrderSummaryProductsAdapter;
import com.techroof.reseau.LocalDB.DatabaseHelper;
import com.techroof.reseau.Model.Cart;
import com.techroof.reseau.Model.PaymentDetails;
import com.techroof.reseau.Model.ShippingAddress;
import com.techroof.reseau.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class CheckOutActivity extends AppCompatActivity {

    private TextView shippingNameText, shippingAddressText, shippingPhoneText, paymentMethodName, paymentMethodTitle, creatorNameText, subTotalText, shippingText, totalText, toolbarTitle;
    private RecyclerView orderSummaryRv;
    private ImageView editShippingAddressBtn, editPaymentBtn, backBtn;
    private Button payNowBtn;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private RecyclerView.LayoutManager layoutManager;
    private DatabaseHelper dbHelper;
    private Cursor res;
    private int subTotalPrice = 0, total = 0;
    private OrderSummaryProductsAdapter orderSummaryProductsAdapter;
    private ArrayList<Cart> cartArrayList;
    private ProgressDialog pd;
    private ConstraintLayout cl;
    private ShimmerFrameLayout shimmerFrameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_out);

        toolbarTitle = findViewById(R.id.toolbar_title);
        shippingNameText = findViewById(R.id.user_name_shipping);
        shippingAddressText = findViewById(R.id.user_address_shipping);
        shippingPhoneText = findViewById(R.id.user_phone_shipping);
        paymentMethodName = findViewById(R.id.payment_method_name);
        paymentMethodTitle = findViewById(R.id.payment_method_title);
        creatorNameText = findViewById(R.id.creator_name_text);
        subTotalText = findViewById(R.id.sub_total_text);
        shippingText = findViewById(R.id.shipping_text);
        totalText = findViewById(R.id.total_text);
        orderSummaryRv = findViewById(R.id.summary_products_rv);
        editShippingAddressBtn = findViewById(R.id.shipping_address_edit_change);
        editPaymentBtn = findViewById(R.id.payment_edit_change);
        payNowBtn = findViewById(R.id.pay_now_btn);
        backBtn = findViewById(R.id.back_btn);
        cl=findViewById(R.id.cl);
        shimmerFrameLayout=findViewById(R.id.shimmer_view_container);

        shimmerFrameLayout.startShimmer();

        toolbarTitle.setText("Checkout");

        pd=new ProgressDialog(this);
        pd.setCanceledOnTouchOutside(false);
        pd.setMessage("Placing Order");

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        layoutManager = new LinearLayoutManager(this);
        orderSummaryRv.setHasFixedSize(true);
        orderSummaryRv.setLayoutManager(layoutManager);

        dbHelper = new DatabaseHelper(this);

        cartArrayList = new ArrayList<>();

        getOrderSummary();
        getShippingDetails();
        getPaymentDetails();

        editPaymentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent paymentDetails = new Intent(CheckOutActivity.this, PaymentMethodActivity.class);
                startActivity(paymentDetails);

            }
        });

        editShippingAddressBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent shippingAddress = new Intent(CheckOutActivity.this, ShippingDetailsActivity.class);
                startActivity(shippingAddress);

            }
        });

        payNowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                placeOrder();

            }
        });

    }

    private void placeOrder() {

        pd.show();

        String orderId = db.collection("orders")
                .document()
                .getId();

        String currentTime, currentDate;
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat date = new SimpleDateFormat("MMM dd,yyyy");
        currentDate = date.format(calendar.getTime());

        SimpleDateFormat time = new SimpleDateFormat("HH:mm:ss");
        currentTime = time.format(calendar.getTime());

        Map<String, Object> orderMap = new HashMap<>();

        orderMap.put("orderId", orderId);
        orderMap.put("products", cartArrayList);
        orderMap.put("supporterOrderTotal", String.valueOf(total));
        orderMap.put("supporter", mAuth.getCurrentUser().getUid());
        orderMap.put("time", currentTime);
        orderMap.put("date", currentDate);
        orderMap.put("status", "pending");
        orderMap.put("timestamp", Timestamp.now());

        db.collection("orders")
                .document(orderId)
                .set(orderMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        dbHelper.emptyCart();

                        Intent home=new Intent(CheckOutActivity.this,SupporterHomeActivity.class);
                        home.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(home);
                        Toast.makeText(CheckOutActivity.this, "Order Placed ", Toast.LENGTH_SHORT).show();
                        pd.dismiss();

                    }
                });

    }

    private void getOrderSummary() {

        res = dbHelper.getCartProducts();

        db.collection("shippingPrice")
                .document("shippingPrice")
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                        if (task.isSuccessful()) {
                            String shippingPrice = task.getResult().get("shippingPrice").toString();

                            int sum = 0;
                            subTotalPrice = 0;

                            int sumItems = 0, temp = 0;

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
                                subTotalPrice = subTotalPrice + sum;

                            }
                            orderSummaryProductsAdapter = new OrderSummaryProductsAdapter(CheckOutActivity.this, cartArrayList);
                            orderSummaryRv.setAdapter(orderSummaryProductsAdapter);

                            total = Integer.parseInt(shippingPrice) + subTotalPrice;

                            shippingText.setText("$" + shippingPrice);
                            totalText.setText("$" + total);
                            subTotalText.setText("$" + subTotalPrice);

                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

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

                        if (task.isSuccessful()) {

                            if (task.getResult().exists()) {

                                ShippingAddress shippingAddress = task.getResult().toObject(ShippingAddress.class);

                                shippingNameText.setText("" + shippingAddress.getName());
                                shippingPhoneText.setText("" + shippingAddress.getPhone());
                                shippingAddressText.setText("" + shippingAddress.getAddress());

                            }

                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        Toast.makeText(CheckOutActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();

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

                        if (task.isSuccessful()) {

                            if (task.getResult().exists()) {

                                PaymentDetails paymentDetails = task.getResult().toObject(PaymentDetails.class);

                                paymentMethodName.setText("" + paymentDetails.getPaymentMethodName());
                                paymentMethodTitle.setText("" + paymentDetails.getPaymentMethodTitle());



                            }

                            cl.setVisibility(View.VISIBLE);
                            shimmerFrameLayout.setVisibility(View.GONE);
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        Toast.makeText(CheckOutActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });
    }


}
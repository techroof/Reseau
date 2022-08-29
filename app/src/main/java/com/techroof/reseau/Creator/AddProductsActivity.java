package com.techroof.reseau.Creator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.techroof.reseau.Model.Category;
import com.techroof.reseau.R;

import java.util.ArrayList;
import java.util.HashMap;

public class AddProductsActivity extends AppCompatActivity {

    private ImageView addProdImg, backBtn;
    private EditText productTitleEt, productDescEt, productPriceEt, productDiscountEt, productCatEt,
            productQtyEt;
    private Button saveBtn;
    private String productImageUrl, categoryId;
    private FirebaseAuth mAuth;
    private FirebaseFirestore mDB;
    private ProgressDialog pd;
    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri mImageUri;
    private StorageReference mStorageRef;
    private StorageTask mUploadTask;
    private ArrayList<Category> categoryArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_products);

        backBtn = findViewById(R.id.back_btn);
        addProdImg = findViewById(R.id.upload_img);
        productCatEt = findViewById(R.id.product_category_et);
        productTitleEt = findViewById(R.id.product_title_et);
        productDescEt = findViewById(R.id.product_desc_et);
        productPriceEt = findViewById(R.id.product_price_et);
        productDiscountEt = findViewById(R.id.product_discount_et);
        productQtyEt = findViewById(R.id.product_qty_et);
        saveBtn = findViewById(R.id.save_btn);

        mAuth = FirebaseAuth.getInstance();
        mDB = FirebaseFirestore.getInstance();
        mStorageRef = FirebaseStorage.getInstance().getReference("productImages");

        pd = new ProgressDialog(this);
        pd.setCanceledOnTouchOutside(false);

        categoryArrayList = new ArrayList<>();

        AlertDialog.Builder builderSingle = new AlertDialog.Builder(AddProductsActivity.this);
        builderSingle.setTitle("Select Category");

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(AddProductsActivity.this, android.R.layout.select_dialog_singlechoice);

        mDB.collection("category").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if (task.isSuccessful()) {

                    for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {

                        Category category = queryDocumentSnapshot.toObject(Category.class);
                        categoryArrayList.add(category);

                        arrayAdapter.add(category.getName());

                        builderSingle.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });

                    }

                }

            }
        });

        productCatEt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        String categoryName = arrayAdapter.getItem(which);
                        categoryId = categoryArrayList.get(which).getId();
                        productCatEt.setText(categoryName);

                        dialog.dismiss();

                    }
                });
                builderSingle.show();

            }
        });

        addProdImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, PICK_IMAGE_REQUEST);
            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String title = productTitleEt.getText().toString();
                String desc = productDescEt.getText().toString();
                String price = productPriceEt.getText().toString();
                String discount = productDiscountEt.getText().toString();
                String categoryName = productCatEt.getText().toString();
                String quantity = productQtyEt.getText().toString();

                if (TextUtils.isEmpty(title)) {
                    productTitleEt.setError("Enter Product Title");
                }

                if (TextUtils.isEmpty(desc)) {
                    productDescEt.setError("Enter Product Description");
                }

                if (TextUtils.isEmpty(price)) {
                    productPriceEt.setError("Enter Product Price");
                }

                if (TextUtils.isEmpty(discount)) {
                    productDiscountEt.setError("Enter Product Discount");
                }

                if (TextUtils.isEmpty(categoryName)) {
                    productCatEt.setError("Select Product Category");
                }

                if (TextUtils.isEmpty(productImageUrl)) {
                    Toast.makeText(AddProductsActivity.this, "Upload Product Image", Toast.LENGTH_SHORT).show();
                }

                if (TextUtils.isEmpty(quantity)) {
                    productQtyEt.setError("Enter Product Quantity");
                }

                if (!TextUtils.isEmpty(title)
                        && !TextUtils.isEmpty(desc)
                        && !TextUtils.isEmpty(price)
                        && !TextUtils.isEmpty(discount)
                        && !TextUtils.isEmpty(categoryName)
                        && !TextUtils.isEmpty(categoryId)
                        && !TextUtils.isEmpty(quantity)
                        && !TextUtils.isEmpty(productImageUrl)) {

                    addProduct(title, desc, price, discount, categoryName, productImageUrl, quantity, categoryId);

                }


            }
        });

    }

    private void addProduct(String title, String desc, String price, String discount,
                            String categoryName, String productImageUrl, String productQty, String categoryId) {

        pd.setMessage("Adding Product...");
        pd.show();

        final String productId = mDB
                .collection("products")
                .document()
                .getId();
        //.collection("property").document().getId();

        int discountAmount = (Integer.parseInt(price) * Integer.parseInt(discount))/100;
        int finalPrice=Integer.parseInt(price)-discountAmount;

        final HashMap<String, String> productMap = new HashMap();
        productMap.put("productTitle", title);
        productMap.put("productDesc", desc);
        productMap.put("productImage", productImageUrl);
        productMap.put("creator", mAuth.getCurrentUser().getUid());
        productMap.put("productCategoryName", categoryName);
        productMap.put("productCategoryId", categoryId);
        productMap.put("productPrice", price);
        productMap.put("finalPrice", String.valueOf(finalPrice));
        productMap.put("productDiscount", discount);
        productMap.put("productId", productId);
        productMap.put("productQuantity", productQty);

        mDB.collection("products")
                .document(productId)
                .set(productMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        Intent products = new Intent(AddProductsActivity.this, AllProductsActivity.class);
                        startActivity(products);
                        finish();
                        pd.dismiss();

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(AddProductsActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                        pd.dismiss();
                    }
                });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            mImageUri = data.getData();
            addProdImg.setImageURI(mImageUri);
            uploadFile();

        }
    }

    private void uploadFile() {

        if (mImageUri != null) {

            final StorageReference fileReference = mStorageRef.child(mImageUri.getLastPathSegment()
                    + "_" + System.currentTimeMillis());

            mUploadTask = fileReference.putFile(mImageUri)
                    .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                            if (task.isSuccessful()) {

                                Task<Uri> uriTask =
                                        mUploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                                            @Override
                                            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                                                if (task.isSuccessful()) {
                                                    pd.dismiss();
                                                    return fileReference.getDownloadUrl();

                                                } else {
                                                    pd.dismiss();
                                                    throw task.getException();
                                                }
                                            }
                                        }).addOnCompleteListener(new OnCompleteListener() {
                                            @Override
                                            public void onComplete(@NonNull Task task) {
                                                pd.dismiss();
                                                productImageUrl = task.getResult().toString();
                                            }
                                        });

                            } else {
                                Toast.makeText(AddProductsActivity.this, "Error", Toast.LENGTH_SHORT).show();
                                pd.dismiss();
                            }

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            Toast.makeText(AddProductsActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            pd.dismiss();

                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                            pd.setMessage((int) progress + "% Uploaded");
                            pd.show();

                        }
                    });
        } else {
            Toast.makeText(this, "No file selected", Toast.LENGTH_SHORT).show();
        }
    }

}
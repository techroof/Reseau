package com.techroof.reseau.Adapter.Supporter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.techroof.reseau.Model.Products;
import com.techroof.reseau.R;
import com.techroof.reseau.Supporter.ProductDetailsActivity;

import java.util.ArrayList;

public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.AllProductsViewHolder> {

    private Context context;
    private ArrayList<Products> productsArrayList;

    public ProductsAdapter(Context context, ArrayList<Products> productsArrayList) {
        this.context = context;
        this.productsArrayList = productsArrayList;
    }

    @NonNull
    @Override
    public AllProductsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.products_layout, parent, false);

        AllProductsViewHolder viewHolder = new AllProductsViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final AllProductsViewHolder holder, final int position) {

       int discount=(Integer.parseInt(productsArrayList.get(position).getProductPrice())
                *Integer.parseInt(productsArrayList.get(position).getProductDiscount()))/100;

        int discountedAmount=Integer.parseInt(productsArrayList.get(position).getProductPrice())-discount;

        Glide.with(context).load(productsArrayList.get(position).getProductImage()).into(
                holder.productsImg);
        holder.productTitle.setText(productsArrayList.get(position).getProductTitle());
        holder.productPrice.setText("$"+productsArrayList.get(position).getFinalPrice());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent productDetails = new Intent(context, ProductDetailsActivity.class);
                productDetails.putExtra("productId", productsArrayList.get(position).getProductId());
                productDetails.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(productDetails);


            }
        });
    }

    @Override
    public int getItemCount() {
        return productsArrayList.size();
    }

    public static class AllProductsViewHolder extends RecyclerView.ViewHolder {

        TextView productPrice, productTitle;
        ImageView productsImg;

        public AllProductsViewHolder(@NonNull View itemView) {
            super(itemView);

            productTitle = itemView.findViewById(R.id.products_title);
            productPrice = itemView.findViewById(R.id.products_price);
            productsImg=itemView.findViewById(R.id.products_img);

        }
    }
}

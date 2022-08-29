package com.techroof.reseau.Adapter.Creator;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.techroof.reseau.Model.CreatorHomeItems;
import com.techroof.reseau.Model.Products;
import com.techroof.reseau.R;

import java.util.ArrayList;

public class AllProductsAdapter extends RecyclerView.Adapter<AllProductsAdapter.AllProductsViewHolder> {

    private Context context;
    private ArrayList<Products> productsArrayList;

    public AllProductsAdapter(Context context, ArrayList<Products> productsArrayList) {
        this.context = context;
        this.productsArrayList = productsArrayList;
    }

    @NonNull
    @Override
    public AllProductsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.all_products_layout, parent, false);

        AllProductsViewHolder viewHolder = new AllProductsViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final AllProductsViewHolder holder, final int position) {

        Glide.with(context).load(productsArrayList.get(position).getProductImage()).into(
                holder.productsImg);
        holder.productTitle.setText(productsArrayList.get(position).getProductTitle());
        holder.productPrice.setText("$"+productsArrayList.get(position).getProductPrice());

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

package com.techroof.reseau.Adapter.Supporter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.techroof.reseau.LocalDB.DatabaseHelper;
import com.techroof.reseau.Model.Cart;
import com.techroof.reseau.R;
import com.techroof.reseau.Supporter.ProductDetailsActivity;
import com.techroof.reseau.Supporter.SupporterFragments.CartFragment;

import java.util.ArrayList;

public class OrderSummaryProductsAdapter extends RecyclerView.Adapter<OrderSummaryProductsAdapter.CartItemViewHolder> {
    private Context context;
    private ArrayList<Cart> cart;
    private DatabaseHelper db;

    public OrderSummaryProductsAdapter(Context context, ArrayList<Cart> cart) {
        this.context = context;
        this.cart = cart;

        db=new DatabaseHelper(context);

    }


    @NonNull
    @Override
    public CartItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.order_summary_products_layout, parent, false);
        CartItemViewHolder viewHolder = new CartItemViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final CartItemViewHolder holder, final int position) {

        Picasso.get().load(cart.get(position).getIMAGE()).into(holder.cartItemImg);

        holder.cartItemName.setText(cart.get(position).getNAME());
        holder.cartItemPrice.setText("$"+cart.get(position).getPRICE());
        holder.cartItemQuantity.setText("Quantity: "+cart.get(position).getQTY());


    }

    @Override
    public int getItemCount() {
        return cart.size();
    }

    public static class CartItemViewHolder extends RecyclerView.ViewHolder {
        ImageView cartItemImg;
        TextView cartItemName, cartItemPrice, cartItemQuantity;

        public CartItemViewHolder(View itemView) {
            super(itemView);

            cartItemImg = itemView.findViewById(R.id.products_img);
            cartItemName = itemView.findViewById(R.id.products_title);
            cartItemPrice = itemView.findViewById(R.id.products_price);
            cartItemQuantity = itemView.findViewById(R.id.products_qty);


        }
    }


}
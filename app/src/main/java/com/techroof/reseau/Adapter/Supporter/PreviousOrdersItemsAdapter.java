package com.techroof.reseau.Adapter.Supporter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.techroof.reseau.Model.Orders;
import com.techroof.reseau.Model.Products;
import com.techroof.reseau.R;

import java.util.ArrayList;
import java.util.List;

public class PreviousOrdersItemsAdapter extends RecyclerView.Adapter<PreviousOrdersItemsAdapter.OrderItemsItemViewHolder> {
    private Context context;
    private List<Products> productsArrayList;

    public PreviousOrdersItemsAdapter(Context context, List<Products> productsArrayList) {
        this.context = context;
        this.productsArrayList = productsArrayList;

    }

    @NonNull
    @Override
    public OrderItemsItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.orders_rv_items_layout, parent, false);

        OrderItemsItemViewHolder viewHolder = new OrderItemsItemViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final OrderItemsItemViewHolder holder, final int position) {

        holder.orderStatus.setText(productsArrayList.get(position).getStatus());
        holder.productName.setText(productsArrayList.get(position).getName());
        holder.productPrice.setText(productsArrayList.get(position).getPrice());
        Picasso.get().load(productsArrayList.get(position).getImage()).into(holder.productImg);

    }

    @Override
    public int getItemCount() {
        return productsArrayList.size();
    }

    public static class OrderItemsItemViewHolder extends RecyclerView.ViewHolder {
        ImageView productImg;
        TextView productName, productPrice, orderStatus;

        public OrderItemsItemViewHolder(View itemView) {
            super(itemView);

            productImg = itemView.findViewById(R.id.products_img);
            productName = itemView.findViewById(R.id.products_title);
            productPrice = itemView.findViewById(R.id.products_price);
            orderStatus = itemView.findViewById(R.id.status_text);


        }
    }


}
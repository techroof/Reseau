package com.techroof.reseau.Adapter.Supporter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.techroof.reseau.LocalDB.DatabaseHelper;
import com.techroof.reseau.Model.Orders;
import com.techroof.reseau.Model.Products;
import com.techroof.reseau.R;

import java.util.ArrayList;
import java.util.List;

public class PreviousOrdersAdapter extends RecyclerView.Adapter<PreviousOrdersAdapter.OrderItemViewHolder> {
    private Context context;
    private ArrayList<Orders> ordersArrayList;
    private List<Products> productsArrayList;
    private RecyclerView.LayoutManager layoutManager;
    private PreviousOrdersItemsAdapter adapter;

    public PreviousOrdersAdapter(Context context, ArrayList<Orders> ordersArrayList) {
        this.context = context;
        this.ordersArrayList = ordersArrayList;

    }

    @NonNull
    @Override
    public OrderItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.orders_rv_layout, parent, false);

        OrderItemViewHolder viewHolder = new OrderItemViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final OrderItemViewHolder holder, final int position) {

        layoutManager=new LinearLayoutManager(context);
        productsArrayList=new ArrayList<>();

        holder.ordersItemsRv.setLayoutManager(layoutManager);
        holder.ordersItemsRv.setHasFixedSize(true);

        productsArrayList=ordersArrayList.get(position).getProducts();

        holder.orderNum.setText("Order Id: "+ordersArrayList.get(position).getOrderId());

        adapter=new PreviousOrdersItemsAdapter(context,productsArrayList);
        holder.ordersItemsRv.setAdapter(adapter);


    }

    @Override
    public int getItemCount() {
        return ordersArrayList.size();
    }

    public static class OrderItemViewHolder extends RecyclerView.ViewHolder {
        TextView orderNum;
        RecyclerView ordersItemsRv;

        public OrderItemViewHolder(View itemView) {
            super(itemView);

            orderNum = itemView.findViewById(R.id.order_num);
            ordersItemsRv=itemView.findViewById(R.id.orders_items_rv);


        }
    }


}
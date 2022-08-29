package com.techroof.reseau.Adapter.Creator;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.techroof.reseau.Creator.AllProductsActivity;
import com.techroof.reseau.Model.CreatorHomeItems;
import com.techroof.reseau.R;


import java.util.ArrayList;

public class CreatorHomeItemsAdapter extends RecyclerView.Adapter<CreatorHomeItemsAdapter.CreatorHomeItemsViewHolder> {

    private Context context;
    private ArrayList<CreatorHomeItems> creatorHomeItemsArrayList;

    public CreatorHomeItemsAdapter(Context context, ArrayList<CreatorHomeItems> creatorHomeItemsArrayList) {
        this.context = context;
        this.creatorHomeItemsArrayList = creatorHomeItemsArrayList;
    }

    @NonNull
    @Override
    public CreatorHomeItemsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.creator_home_items_layout, parent, false);

        CreatorHomeItemsViewHolder creatorHomeItemsViewHolder = new CreatorHomeItemsViewHolder(view);
        return creatorHomeItemsViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final CreatorHomeItemsViewHolder holder, final int position) {

        holder.itemIcon.setImageResource(creatorHomeItemsArrayList.get(position).getIcon());
        holder.itemArrow.setImageResource(creatorHomeItemsArrayList.get(position).getForwardIcon());
        holder.itemText.setText(creatorHomeItemsArrayList.get(position).getItemName());
        holder.itemDetails.setText(creatorHomeItemsArrayList.get(position).getItemDetail());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (creatorHomeItemsArrayList.get(position).getItemName().equals("All Products")){

                    Intent allProducts=new Intent(context, AllProductsActivity.class);
                    allProducts.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(allProducts);

                }

            }
        });

    }

    @Override
    public int getItemCount() {
        return creatorHomeItemsArrayList.size();
    }

    public static class CreatorHomeItemsViewHolder extends RecyclerView.ViewHolder {

        TextView itemText, itemDetails;
        ImageView itemIcon,itemArrow;

        public CreatorHomeItemsViewHolder(@NonNull View itemView) {
            super(itemView);

            itemText = itemView.findViewById(R.id.item_text);
            itemDetails = itemView.findViewById(R.id.item_num_text);
            itemIcon=itemView.findViewById(R.id.item_img);
            itemArrow=itemView.findViewById(R.id.item_arrow_img);


        }
    }
}

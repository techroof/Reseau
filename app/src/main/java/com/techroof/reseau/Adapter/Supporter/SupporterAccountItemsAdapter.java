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

import com.techroof.reseau.Creator.AllProductsActivity;
import com.techroof.reseau.Model.CreatorHomeItems;
import com.techroof.reseau.Model.SupporterAccItems;
import com.techroof.reseau.R;

import java.util.ArrayList;

public class SupporterAccountItemsAdapter extends RecyclerView.Adapter<SupporterAccountItemsAdapter.SupporterAccountItemsViewHolder> {

    private Context context;
    private ArrayList<SupporterAccItems> supporterAccItemsArrayList;

    public SupporterAccountItemsAdapter(Context context, ArrayList<SupporterAccItems> supporterAccItemsArrayList) {
        this.context = context;
        this.supporterAccItemsArrayList = supporterAccItemsArrayList;
    }

    @NonNull
    @Override
    public SupporterAccountItemsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.supporter_account_items_layout, parent, false);

        SupporterAccountItemsViewHolder supporterAccountItemsViewHolder = new SupporterAccountItemsViewHolder(view);
        return supporterAccountItemsViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final SupporterAccountItemsViewHolder holder, final int position) {

        holder.itemIcon.setImageResource(supporterAccItemsArrayList.get(position).getIcon());
        holder.itemArrow.setImageResource(supporterAccItemsArrayList.get(position).getForwardIcon());
        holder.itemText.setText(supporterAccItemsArrayList.get(position).getItemName());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (supporterAccItemsArrayList.get(position).getItemName().equals("All Products")){

                    Intent allProducts=new Intent(context, AllProductsActivity.class);
                    allProducts.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(allProducts);

                }

            }
        });

    }

    @Override
    public int getItemCount() {
        return supporterAccItemsArrayList.size();
    }

    public static class SupporterAccountItemsViewHolder extends RecyclerView.ViewHolder {

        TextView itemText;
        ImageView itemIcon,itemArrow;

        public SupporterAccountItemsViewHolder(@NonNull View itemView) {
            super(itemView);

            itemText = itemView.findViewById(R.id.item_text);
            itemIcon=itemView.findViewById(R.id.item_img);
            itemArrow=itemView.findViewById(R.id.item_arrow_img);


        }
    }
}

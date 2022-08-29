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
import com.techroof.reseau.Model.Category;
import com.techroof.reseau.Model.Products;
import com.techroof.reseau.R;
import com.techroof.reseau.Supporter.CategoryProductsActivity;

import java.util.ArrayList;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {

    private Context context;
    private ArrayList<Category> categoryArrayList;

    public CategoryAdapter(Context context, ArrayList<Category> categoryArrayList) {
        this.context = context;
        this.categoryArrayList = categoryArrayList;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.home_categories_layout, parent, false);

        CategoryViewHolder viewHolder = new CategoryViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final CategoryViewHolder holder, final int position) {

        Glide.with(context).load(categoryArrayList.get(position).getImgUrl()).into(
                holder.categoryImg);
        holder.categoryName.setText(categoryArrayList.get(position).getName());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent categoryProducts=new Intent(context, CategoryProductsActivity.class);
                categoryProducts.putExtra("category_id",categoryArrayList.get(position).getId());
                categoryProducts.putExtra("category_name",categoryArrayList.get(position).getName());
                categoryProducts.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(categoryProducts);

            }
        });

    }

    @Override
    public int getItemCount() {
        return categoryArrayList.size();
    }

    public static class CategoryViewHolder extends RecyclerView.ViewHolder {

        TextView categoryName;
        ImageView categoryImg;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);

            categoryName = itemView.findViewById(R.id.category_name_text);
            categoryImg=itemView.findViewById(R.id.category_img);

        }
    }
}

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

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartItemViewHolder> {
    private Context context;
    private ArrayList<Cart> cart;
    private DatabaseHelper db;
    private CartFragment cartFragment;
    private ProgressDialog pd;

    public CartAdapter(Context context, ArrayList<Cart> cart,CartFragment cartFragment) {
        this.context = context;
        this.cart = cart;
        this.cartFragment=cartFragment;

        db=new DatabaseHelper(context);

        pd = new ProgressDialog(context);
        pd.setMessage("Deleting item...");
        pd.setCanceledOnTouchOutside(false);


    }


    @NonNull
    @Override
    public CartItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cart_products_layout, parent, false);
        CartItemViewHolder viewHolder = new CartItemViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final CartItemViewHolder holder, final int position) {

        Picasso.get().load(cart.get(position).getIMAGE()).into(holder.cartItemImg);

        holder.cartItemName.setText(cart.get(position).getNAME());
        holder.cartItemPrice.setText("$"+cart.get(position).getPRICE());
        holder.cartItemQuantity.setText("Quantity: "+cart.get(position).getQTY());

        holder.editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent details=new Intent(context, ProductDetailsActivity.class);
                details.putExtra("productId",cart.get(position).getPID());
                details.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(details);

            }
        });

        holder.deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Integer deleteProduct= db.deleteProduct(cart.get(position).getPID());

                if (deleteProduct>0){
                    cart.remove(position);
                    cart.clear();
                    notifyDataSetChanged();
                    cartFragment.updateCartData();

                    Toast.makeText(context, "Product deleted from cart", Toast.LENGTH_SHORT).show();

                pd.dismiss();

            } else {
                Toast.makeText(context, "Error while deleting product", Toast.LENGTH_SHORT).show();
                pd.dismiss();
            }

            }
        });

    }

    @Override
    public int getItemCount() {
        return cart.size();
    }

    public static class CartItemViewHolder extends RecyclerView.ViewHolder {
        ImageView cartItemImg,deleteBtn,editBtn;
        TextView cartItemName, cartItemPrice, cartItemQuantity;

        public CartItemViewHolder(View itemView) {
            super(itemView);

            cartItemImg = itemView.findViewById(R.id.products_img);
            cartItemName = itemView.findViewById(R.id.products_title);
            cartItemPrice = itemView.findViewById(R.id.products_price);
            cartItemQuantity = itemView.findViewById(R.id.products_qty);
            deleteBtn = itemView.findViewById(R.id.cart_del_btn);
            editBtn = itemView.findViewById(R.id.cart_edit_btn);


        }
    }


}
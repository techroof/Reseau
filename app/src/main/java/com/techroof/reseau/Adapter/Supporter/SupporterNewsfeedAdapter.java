package com.techroof.reseau.Adapter.Supporter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.techroof.reseau.Model.Newsfeed;
import com.techroof.reseau.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SupporterNewsfeedAdapter extends RecyclerView.Adapter<SupporterNewsfeedAdapter.NewsfeedViewHolder> {

    private Context context;
    private ArrayList<Newsfeed> newsfeedArrayList;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private List<String> supportsArrayList;

    public SupporterNewsfeedAdapter(Context context, ArrayList<Newsfeed> newsfeedArrayList) {
        this.context = context;
        this.newsfeedArrayList = newsfeedArrayList;

        db=FirebaseFirestore.getInstance();
        mAuth=FirebaseAuth.getInstance();
    }

    @NonNull
    @Override
    public NewsfeedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.newsfeeds_layout, parent, false);

        NewsfeedViewHolder viewHolder = new NewsfeedViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final NewsfeedViewHolder holder, final int position) {

        supportsArrayList=new ArrayList<>();

        if (newsfeedArrayList.get(position).getNewsfeedImg() == null) {
            holder.newsfeedImg.setVisibility(View.GONE);

        } else {

            Glide.with(context).load(newsfeedArrayList.get(position).getNewsfeedImg()).into(
                    holder.newsfeedImg);
        }

        Glide.with(context).load(newsfeedArrayList.get(position).getCreatorImg()).into(
                holder.creatorImg);

        holder.creatorName.setText(newsfeedArrayList.get(position).getCreatorName());
        holder.newsfeedText.setText(newsfeedArrayList.get(position).getNewsfeedText());
        holder.totalSupportsText.setText(""+supportsArrayList.size());

        try{

            SimpleDateFormat formatter = new SimpleDateFormat("HH:mm dd-MM");
            String dateString = formatter.format(new Date(newsfeedArrayList.get(position).getTimestamp()));
            holder.postTimeText.setText(dateString);

        }catch (Exception e) {
        }


        holder.supportsText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                holder.supportThumbImg.setImageResource(R.drawable.ic_baseline_thumb_up_24_2);
                holder.supportThumbImg.setColorFilter(ContextCompat.getColor
                        (context, R.color.golden), android.graphics.PorterDuff.Mode.MULTIPLY);

                int totalSupports=supportsArrayList.size()+1;
                holder.totalSupportsText.setText(""+totalSupports);

                supportsArrayList.add(mAuth.getCurrentUser().getUid());

                Map<String,Object> supportsMap=new HashMap<>();
                supportsMap.put("supports",supportsArrayList);

                db.collection("newsfeeds")
                        .document(newsfeedArrayList.get(position).getNewsfeedId())
                        .update(supportsMap);

            }
        });

        holder.commentsText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        holder.followText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

    }

    @Override
    public int getItemCount() {
        return newsfeedArrayList.size();
    }

    public static class NewsfeedViewHolder extends RecyclerView.ViewHolder {

        TextView creatorName, newsfeedText, totalSupportsText, supportsText, commentsText, postTimeText,followText;
        ImageView creatorImg, newsfeedImg,supportThumbImg;

        public NewsfeedViewHolder(@NonNull View itemView) {
            super(itemView);

            creatorName = itemView.findViewById(R.id.creator_name);
            newsfeedText = itemView.findViewById(R.id.newsfeed_description);
            totalSupportsText = itemView.findViewById(R.id.total_supports_text);
            supportsText = itemView.findViewById(R.id.support_text);
            commentsText = itemView.findViewById(R.id.comment_text);
            creatorImg = itemView.findViewById(R.id.creator_img);
            newsfeedImg = itemView.findViewById(R.id.newsfeed_img);
            postTimeText = itemView.findViewById(R.id.post_time_text);
            followText = itemView.findViewById(R.id.follow_text);
            supportThumbImg = itemView.findViewById(R.id.support_img);

        }
    }
}

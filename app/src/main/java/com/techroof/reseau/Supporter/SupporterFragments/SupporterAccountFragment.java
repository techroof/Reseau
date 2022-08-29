package com.techroof.reseau.Supporter.SupporterFragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.techroof.reseau.Adapter.Creator.CreatorHomeItemsAdapter;
import com.techroof.reseau.Adapter.Supporter.SupporterAccountItemsAdapter;
import com.techroof.reseau.Model.CreatorHomeItems;
import com.techroof.reseau.Model.SupporterAccItems;
import com.techroof.reseau.R;
import com.techroof.reseau.Supporter.SupporterOrdersActivity;

import java.util.ArrayList;

public class SupporterAccountFragment extends Fragment {

    private ArrayList<SupporterAccItems> supporterAccItemsArrayList;
    private String[] menuItemsText={"Account Info","Help & Support","Settings","Sign Out"};
    private int[] menuItemIcon={R.drawable.account_info_icon,R.drawable.help_and_support_icon,
            R.drawable.settings_icon,R.drawable.signout_icon};
    private RecyclerView accItemsRv;
    private RecyclerView.LayoutManager layoutManager;
    private SupporterAccountItemsAdapter adapter;
    private ImageView ordersImg;

    public SupporterAccountFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_supporter_account, container, false);

        supporterAccItemsArrayList=new ArrayList<>();

        accItemsRv=view.findViewById(R.id.supporter_menu_rv);
        ordersImg=view.findViewById(R.id.orders_img);

        layoutManager=new LinearLayoutManager(getContext());
        accItemsRv.setHasFixedSize(true);
        accItemsRv.setLayoutManager(layoutManager);

        for (int i=0;i<menuItemsText.length;i++){

            supporterAccItemsArrayList.add(new SupporterAccItems(menuItemIcon[i],
                    R.drawable.ic_round_keyboard_arrow_right_24,
                    menuItemsText[i]));
        }

        adapter=new SupporterAccountItemsAdapter(getContext(), supporterAccItemsArrayList);
        accItemsRv.setAdapter(adapter);

        ordersImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent orders=new Intent(getContext(), SupporterOrdersActivity.class);
                startActivity(orders);

            }
        });

        return view;
    }
}
package com.techroof.reseau.Creator.CreatorFragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.techroof.reseau.Adapter.Creator.CreatorHomeItemsAdapter;
import com.techroof.reseau.Creator.CreatorHomeActivity;
import com.techroof.reseau.Model.CreatorHomeItems;
import com.techroof.reseau.R;

import java.util.ArrayList;

public class CreatorHomeFragment extends Fragment {

    private ArrayList<CreatorHomeItems> creatorHomeItemsArrayList;
    private String[] menuItemsText={"Orders","All Products","Insights","Settings"};
    private int[] menuItemIcon={R.drawable.truck_icon,R.drawable.products_icon,
            R.drawable.insights_icon,R.drawable.settings_icon};

    private String[] menuItemsDetails={"150","10","",""};
    private RecyclerView homeItemsRv;
    private RecyclerView.LayoutManager layoutManager;
    private CreatorHomeItemsAdapter adapter;

    public CreatorHomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_creator_home, container, false);

        creatorHomeItemsArrayList=new ArrayList<>();

        homeItemsRv=view.findViewById(R.id.creator_menu_rv);

        layoutManager=new LinearLayoutManager(getContext());

        homeItemsRv.setHasFixedSize(true);
        homeItemsRv.setLayoutManager(layoutManager);

        for (int i=0;i<menuItemsText.length;i++){

            creatorHomeItemsArrayList.add(new CreatorHomeItems(menuItemIcon[i],
                    R.drawable.ic_round_keyboard_arrow_right_24,
                    menuItemsText[i],
                    menuItemsDetails[i]));
        }

        adapter=new CreatorHomeItemsAdapter(getContext(), creatorHomeItemsArrayList);
        homeItemsRv.setAdapter(adapter);

        return view;
    }
}
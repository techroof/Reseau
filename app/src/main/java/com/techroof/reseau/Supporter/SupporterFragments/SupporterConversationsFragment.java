package com.techroof.reseau.Supporter.SupporterFragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.techroof.reseau.R;

public class SupporterConversationsFragment extends Fragment {

    public SupporterConversationsFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_supporter_conversations, container, false);
        return view;
    }
}
package com.kamenov.martin.gosportbg.messages;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kamenov.martin.gosportbg.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class MessagesFragment extends Fragment {


    public MessagesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_messages, container, false);
    }

}

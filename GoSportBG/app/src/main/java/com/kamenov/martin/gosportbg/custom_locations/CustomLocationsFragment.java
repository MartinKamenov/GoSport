package com.kamenov.martin.gosportbg.custom_locations;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kamenov.martin.gosportbg.R;
import com.kamenov.martin.gosportbg.base.contracts.BaseContracts;
import com.kamenov.martin.gosportbg.models.CustomLocation;

/**
 * A simple {@link Fragment} subclass.
 */
public class CustomLocationsFragment extends Fragment implements CustomLocationsContracts.ICustomLocationsView {
    private CustomLocationsContracts.ICustomLocationsPresenter mPresenter;
    private View root;
    private LinearLayout mLocationsContainer;

    public CustomLocationsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_custom_locations, container, false);
        mLocationsContainer = root.findViewById(R.id.locations_container);
        return root;
    }

    @Override
    public void setPresenter(BaseContracts.Presenter presenter) {
        this.mPresenter = (CustomLocationsContracts.ICustomLocationsPresenter)presenter;
    }

    @Override
    public void showCustomLocationsOnUIThread(final CustomLocation[] customLocations) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                hideProgressBar();
                for(int i = 0; i < customLocations.length; i++) {
                    CustomLocation location = customLocations[i];
                    TextView textView = new TextView(getActivity());
                    textView.setText(location.name);
                    mLocationsContainer.addView(textView);
                }
            }
        });
    }

    @Override
    public void selectCustomLocation() {

    }

    @Override
    public void showMessageOnUIThread(final String message) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void hideProgressBar() {
        root.findViewById(R.id.locations_progressbar).setVisibility(View.GONE);
    }
}

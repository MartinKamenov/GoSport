package com.kamenov.martin.gosportbg.custom_locations;

import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.kamenov.martin.gosportbg.R;
import com.kamenov.martin.gosportbg.base.contracts.BaseContracts;
import com.kamenov.martin.gosportbg.constants.Constants;
import com.kamenov.martin.gosportbg.internet.DownloadImageTask;
import com.kamenov.martin.gosportbg.models.CustomLocation;
import com.kamenov.martin.gosportbg.models.optimizators.ImageCachingService;

import de.hdodenhof.circleimageview.CircleImageView;

public class CustomLocationsFragment extends Fragment implements CustomLocationsContracts.ICustomLocationsView {
    private CustomLocationsContracts.ICustomLocationsPresenter mPresenter;
    private View root;
    private LinearLayout mLocationsContainer;
    private ImageCachingService imageCachingService;

    public CustomLocationsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_custom_locations, container, false);
        imageCachingService = ImageCachingService.getInstance();
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
                    View img;
                    if(location.pictureUrl != null && !imageCachingService.hasBitmap(location.pictureUrl)) {
                        img = new ProgressBar(getActivity());
                        new DownloadImageTask((ProgressBar)img, getActivity())
                                .execute(location.pictureUrl);
                    } else if(location.pictureUrl == null) {
                        img = new ProgressBar(getActivity());
                        new DownloadImageTask((ProgressBar)img, getActivity())
                                .execute(Constants.DOMAIN + "/static/images/locations/default.jpg");
                    } else {
                        img = new CircleImageView(getActivity());
                        ((CircleImageView)img).setImageBitmap(imageCachingService.getBitmap(location.pictureUrl));
                    }

                    // mLocationsContainer.addView(img);
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
        mLocationsContainer.setVisibility(View.VISIBLE);
    }
}

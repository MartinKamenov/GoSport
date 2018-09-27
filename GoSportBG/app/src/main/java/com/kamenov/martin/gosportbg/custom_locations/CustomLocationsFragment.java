package com.kamenov.martin.gosportbg.custom_locations;

import android.graphics.Typeface;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
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
                LinearLayout outsideContainer = new LinearLayout(getActivity());
                for(int i = 0; i < customLocations.length; i++) {
                    if(i % 2 == 0) {
                        outsideContainer = new LinearLayout(getActivity());
                        mLocationsContainer.addView(outsideContainer);
                        outsideContainer.setOrientation(LinearLayout.HORIZONTAL);
                    }


                    CustomLocation location = customLocations[i];
                    addCustomLocationToContainer(outsideContainer, location);
                }
            }
        });
    }

    private void addCustomLocationToContainer(LinearLayout outsideContainer,CustomLocation location) {
        CardView cardView = new CardView(getActivity());
        LinearLayout cardContainer = new LinearLayout(getActivity());
        TextView name = new TextView(getActivity());
        TextView address = new TextView(getActivity());
        name.setTextColor(Constants.CARDCOLOR);
        address.setTextColor(Constants.CARDCOLOR);

        name.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        address.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

        name.setTypeface(Typeface.create("sans-serif-condensed", Typeface.NORMAL));
        address.setTypeface(Typeface.create("sans-serif-condensed", Typeface.NORMAL));

        View img;

        cardContainer.setOrientation(LinearLayout.VERTICAL);

        cardView.setCardBackgroundColor(Constants.CARDCOLOR);
        name.setTextColor(Constants.CARDTEXTCOLOR);
        address.setTextColor(Constants.CARDTEXTCOLOR);

        name.setText(location.name);
        address.setText(location.address);

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

        cardContainer.addView(img);
        LinearLayout.LayoutParams imgParams = (LinearLayout.LayoutParams)img.getLayoutParams();
        imgParams.width = Constants.SCREEN_WIDTH / 4;
        imgParams.height = Constants.SCREEN_WIDTH / 4;
        img.setLayoutParams(imgParams);

        cardContainer.addView(name);
        cardContainer.addView(address);

        cardView.addView(cardContainer);
        outsideContainer.addView(cardView);

        LinearLayout.LayoutParams cardParams = (LinearLayout.LayoutParams)cardView.getLayoutParams();
        cardParams.width = Constants.SCREEN_WIDTH / 2;
        cardView.setLayoutParams(cardParams);
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

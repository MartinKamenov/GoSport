package com.kamenov.martin.gosportbg.custom_locations;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.CardView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.kamenov.martin.gosportbg.R;
import com.kamenov.martin.gosportbg.base.contracts.BaseContracts;
import com.kamenov.martin.gosportbg.constants.Constants;
import com.kamenov.martin.gosportbg.internet.DownloadImageTask;
import com.kamenov.martin.gosportbg.models.CustomLocation;
import com.kamenov.martin.gosportbg.models.Location;
import com.kamenov.martin.gosportbg.models.engine.ImageBorderService;
import com.kamenov.martin.gosportbg.models.optimizators.ImageCachingService;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class CustomLocationsFragment extends Fragment implements CustomLocationsContracts.ICustomLocationsView, View.OnClickListener, TextWatcher {
    private CustomLocationsContracts.ICustomLocationsPresenter mPresenter;
    private View root;
    private LinearLayout mLocationsContainer;
    private ImageCachingService imageCachingService;
    private TextView mFoundResultsTxt;
    private int marginBetweenCardsVertical;
    private int marginBetweenCardsHorizontal;
    private CustomLocation[] mLastFoundLocations;
    private ArrayList<CustomLocation> mLastSearchedLocations;
    private EditText mSearchText;
    private boolean locationsFromWeb;

    public CustomLocationsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_custom_locations, container, false);
        locationsFromWeb = true;
        marginBetweenCardsVertical = 10;
        marginBetweenCardsHorizontal = 15;
        mSearchText = root.findViewById(R.id.search_text);
        mSearchText.addTextChangedListener(this);
        imageCachingService = ImageCachingService.getInstance();
        mLocationsContainer = root.findViewById(R.id.locations_container);
        mFoundResultsTxt = root.findViewById(R.id.result_count);
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
                if(locationsFromWeb) {
                    mLastFoundLocations = customLocations;
                }
                hideProgressBar();
                mLocationsContainer.removeAllViews();
                mFoundResultsTxt.setText("Брой намерени места: " + customLocations.length);
                
                LinearLayout outsideContainer = new LinearLayout(getActivity());
                for(int i = 0; i < customLocations.length; i++) {
                    if(i % 2 == 0) {
                        outsideContainer = new LinearLayout(getActivity());
                        mLocationsContainer.addView(outsideContainer);
                        LinearLayout.LayoutParams outsideContainerParams = (LinearLayout.LayoutParams)outsideContainer.getLayoutParams();
                        outsideContainerParams.topMargin = marginBetweenCardsVertical;
                        outsideContainer.setLayoutParams(outsideContainerParams);
                        outsideContainer.setOrientation(LinearLayout.HORIZONTAL);
                    }

                    CustomLocation location = customLocations[i];
                    addCustomLocationToContainer(outsideContainer, location);
                }
            }
        });
    }

    private void addCustomLocationToContainer(LinearLayout outsideContainer,CustomLocation location) {
        int sideMargin = 15;
        CardView cardView = new CardView(getActivity());
        cardView.setId(location.id);
        cardView.setOnClickListener(this);
        cardView.setCardElevation(20);
        LinearLayout cardContainer = new LinearLayout(getActivity());
        TextView name = new TextView(getActivity());
        TextView address = new TextView(getActivity());

        name.setTextSize(20);
        address.setTextSize(15);
        name.setTextColor(Constants.CARDCOLOR);
        address.setTextColor(Constants.CARDCOLOR);

        name.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        address.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

        name.setTypeface(Typeface.create("sans-serif-condensed", Typeface.NORMAL), Typeface.BOLD_ITALIC);
        address.setTypeface(Typeface.create("sans-serif-condensed", Typeface.NORMAL));

        RelativeLayout imageViewContainer = new RelativeLayout(getActivity());

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
            ImageBorderService.addBorders(((CircleImageView)img));
            ((CircleImageView)img).setImageBitmap(imageCachingService.getBitmap(location.pictureUrl));
        }

        imageViewContainer.addView(img);

        cardContainer.addView(imageViewContainer);

        RelativeLayout.LayoutParams imgParams = (RelativeLayout.LayoutParams)img.getLayoutParams();
        imgParams.width = (Constants.SCREEN_WIDTH / 8) * 3;
        imgParams.height = (Constants.SCREEN_WIDTH / 8) * 3;
        imgParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        imgParams.setMargins(0, 20, 0, 20);
        img.setLayoutParams(imgParams);

        cardContainer.addView(name);

        LinearLayout.LayoutParams nameParams = (LinearLayout.LayoutParams) name.getLayoutParams();
        nameParams.setMargins(sideMargin, 10, sideMargin, 10);
        name.setLayoutParams(nameParams);

        cardContainer.addView(address);

        LinearLayout.LayoutParams addressParams = (LinearLayout.LayoutParams) address.getLayoutParams();
        addressParams.setMargins(sideMargin, 10, sideMargin, 10);
        address.setLayoutParams(addressParams);

        cardView.addView(cardContainer);

        outsideContainer.addView(cardView);

        LinearLayout.LayoutParams cardParams = (LinearLayout.LayoutParams)cardView.getLayoutParams();
        cardParams.width = (Constants.SCREEN_WIDTH / 2) - marginBetweenCardsHorizontal;
        cardParams.setMargins(marginBetweenCardsHorizontal / 2, 0, marginBetweenCardsHorizontal / 2, 0);
        cardView.setLayoutParams(cardParams);
        cardView.setRadius(30);
    }

    @Override
    public void selectCustomLocation(CustomLocation location) {
        Intent result = new Intent();
        result.putExtra("longitude", location.longitude);
        result.putExtra("latitude", location.latitude);
        result.putExtra("place", location.address);
        getActivity().setResult(Activity.RESULT_OK, result);
        getActivity().finish();
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

    @Override
    public void onClick(View view) {
        for(int i = 0; i < this.mLastFoundLocations.length; i++) {
            if(view.getId() == this.mLastFoundLocations[i].id) {
                ((CardView)view).setCardBackgroundColor(Constants.CLICKEDCARDCOLOR);
                selectCustomLocation(this.mLastFoundLocations[i]);
                break;
            }
        }
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        locationsFromWeb = false;
        if(charSequence.length() == 0) {
            showCustomLocationsOnUIThread(mLastFoundLocations);
            return;
        }

        mLastSearchedLocations = new ArrayList<>();
        for(int j = 0; j < mLastFoundLocations.length; j++) {
            if(mLastFoundLocations[j].name.toLowerCase().contains(charSequence.toString().toLowerCase()) ||
                    mLastFoundLocations[j].address.toLowerCase().contains(charSequence.toString().toLowerCase())) {
                mLastSearchedLocations.add(mLastFoundLocations[j]);
            }
        }

        CustomLocation[] locationsArr = new CustomLocation[mLastSearchedLocations.size()];
        showCustomLocationsOnUIThread(mLastSearchedLocations.toArray(locationsArr));
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }
}

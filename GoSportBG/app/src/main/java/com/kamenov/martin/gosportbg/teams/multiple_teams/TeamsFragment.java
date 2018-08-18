package com.kamenov.martin.gosportbg.teams.multiple_teams;


import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Base64;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.kamenov.martin.gosportbg.GoSportApplication;
import com.kamenov.martin.gosportbg.R;
import com.kamenov.martin.gosportbg.base.contracts.BaseContracts;
import com.kamenov.martin.gosportbg.constants.Constants;
import com.kamenov.martin.gosportbg.internet.DownloadImageTask;
import com.kamenov.martin.gosportbg.models.Team;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class TeamsFragment extends Fragment implements TeamsContracts.ITeamsView, View.OnClickListener {

    private static final int SELECT_PHOTO = 1;

    private LinearLayout mTeamsContainer;
    private TeamsContracts.ITeamsPresenter mPresenter;
    private View root;
    private EditText mNameTxt;
    private Spinner mSportSpinner;
    private Bitmap profileImageBitmap;

    public TeamsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_teams, container, false);
        mTeamsContainer = root.findViewById(R.id.teams_container);
        Button newTeamButton = root.findViewById(R.id.new_team_btn);
        newTeamButton.setOnClickListener(this);
        Button showAllTeamsButton = root.findViewById(R.id.show_teams_btn);
        Button createTeamBtn = root.findViewById(R.id.create_team_btn);
        Button changeTeamLogoButton = root.findViewById(R.id.profile_image_button);
        changeTeamLogoButton.setOnClickListener(this);
        createTeamBtn.setOnClickListener(this);
        mNameTxt = root.findViewById(R.id.name_txt);
        showAllTeamsButton.setOnClickListener(this);
        mSportSpinner = root.findViewById(R.id.sport_type_spinner);
        mSportSpinner.setAdapter(getSportsAdapter());
        return root;
    }

    @Override
    public ArrayAdapter<String> getSportsAdapter() {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                getActivity(),
                android.R.layout.simple_spinner_dropdown_item,
                mPresenter.getAllSports()
        );

        return adapter;
    }

    @Override
    public void setPresenter(BaseContracts.Presenter presenter) {
        this.mPresenter = (TeamsContracts.ITeamsPresenter) presenter;
    }

    @Override
    public GoSportApplication getGoSportApplication() {
        return (GoSportApplication) getActivity().getApplication();
    }

    @Override
    public void showTeamsOnUITread(final Team[] teams) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                updateTeams(teams.length);
                mTeamsContainer.removeAllViews();
                int margin = 10;

                for(int i = 0; i < teams.length; i++) {
                    Team team = teams[i];
                    CardView cardView = new CardView(getActivity());
                    cardView.setCardBackgroundColor(Constants.CARDCOLOR);
                    cardView.setId(team.id);
                    cardView.setRadius(50);
                    cardView.setOnClickListener(TeamsFragment.this);
                    cardView.setCardElevation(10);

                    LinearLayout linearLayoutContainer = new LinearLayout(getActivity());
                    linearLayoutContainer.setOrientation(LinearLayout.HORIZONTAL);
                    linearLayoutContainer.setWeightSum(2);

                    CircleImageView img = new CircleImageView(getActivity());
                    if(team.pictureUrl != null && !team.pictureUrl.contains("default.jpg")) {
                        String url = Constants.DOMAIN + team.pictureUrl;
                        new DownloadImageTask(img)
                                .execute(url);
                    } else {
                        img.setImageResource(R.drawable.default_team_avatar);
                    }

                    linearLayoutContainer.addView(img);

                    LinearLayout.LayoutParams imageLayoutParams = (LinearLayout.LayoutParams) img.getLayoutParams();
                    imageLayoutParams.height = 150;
                    imageLayoutParams.width = 150;
                    imageLayoutParams.setMargins(10, 0, 10, 0);
                    imageLayoutParams.gravity = Gravity.CENTER;
                    img.setLayoutParams(imageLayoutParams);

                    LinearLayout linearLayout = new LinearLayout(getActivity());
                    linearLayout.setOrientation(LinearLayout.VERTICAL);

                    linearLayoutContainer.addView(linearLayout);

                    LinearLayout.LayoutParams lParams = (LinearLayout.LayoutParams) linearLayout.getLayoutParams();
                    lParams.width = LinearLayout.LayoutParams.MATCH_PARENT;
                    lParams.gravity = Gravity.CENTER;
                    linearLayout.setLayoutParams(lParams);

                    cardView.addView(linearLayoutContainer);

                    TextView description = new TextView(getActivity());
                    description.setTextColor(Constants.SECONDCOLOR);
                    description.setText(team.name);
                    description.setGravity(Gravity.CENTER_HORIZONTAL);
                    description.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                    description.setTextSize(24);
                    description.setTypeface(Typeface.create("sans-serif-condensed", Typeface.NORMAL), Typeface.BOLD_ITALIC);
                    linearLayout.addView(description);
                    TextView sport = new TextView(getActivity());
                    sport.setTextColor(Constants.SECONDCOLOR);
                    sport.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                    sport.setText(team.sport);
                    sport.setGravity(Gravity.CENTER_HORIZONTAL);
                    sport.setTypeface(Typeface.create("sans-serif-condensed", Typeface.NORMAL));
                    linearLayout.addView(sport);
                    linearLayout.setPadding(50, 50, 50, 50);
                    mTeamsContainer.addView(cardView);
                    LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) cardView.getLayoutParams();
                    lp.setMargins(margin, margin, margin, margin);
                }
            }
        });
    }

    @Override
    public void refreshView() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                hideProgressBar();
                getActivity().finish();
                startActivity(getActivity().getIntent());
            }
        });
    }

    @Override
    public void hideProgressBar() {
        root.findViewById(R.id.progressbar_form).setVisibility(View.GONE);
        root.findViewById(R.id.new_team_form).setVisibility(View.GONE);
        root.findViewById(R.id.show_teams_form).setVisibility(View.VISIBLE);
    }

    @Override
    public void updateTeams(int count) {
        root.findViewById(R.id.event_list_progressbar).setVisibility(View.GONE);
        ((TextView)root.findViewById(R.id.result_count)).setText("Брой намерени събития: " + count);
    }

    @Override
    public void showProgressBar() {
        root.findViewById(R.id.new_team_form).setVisibility(View.GONE);
        root.findViewById(R.id.show_teams_form).setVisibility(View.GONE);
        root.findViewById(R.id.progressbar_form).setVisibility(View.VISIBLE);
    }

    @Override
    public void showNewTeamForm() {
        root.findViewById(R.id.show_teams_form).setVisibility(View.GONE);
        root.findViewById(R.id.new_team_form).setVisibility(View.VISIBLE);
    }

    @Override
    public void createTeamBtnPressed() {
        String name = mNameTxt.getText().toString();
        String sport = mSportSpinner.getSelectedItem().toString();
        String picture = null;
        if(profileImageBitmap != null) {
            picture = bitMapToString(profileImageBitmap);
        }

        mPresenter.createTeam(name, sport, picture);
    }

    @Override
    public void selectPicture() {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, SELECT_PHOTO);
    }

    @Override
    public void showMessageOnUIThread(final String message) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                hideProgressBar();
                Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == SELECT_PHOTO) {
            if (resultCode == RESULT_OK) {
                if (data != null) {
                    // Get the URI of the selected file
                    final Uri uri = data.getData();
                    profileImageBitmap = decodeUriToBitmap(getActivity(), uri);
                    ((ImageView)root.findViewById(R.id.logo_image)).setImageBitmap(profileImageBitmap);
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public String bitMapToString(Bitmap bitmap){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte [] b = baos.toByteArray();
        String temp = Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
    }

    private static Bitmap decodeUriToBitmap(Context mContext, Uri sendUri) {
        Bitmap getBitmap = null;
        try {
            InputStream image_stream;
            try {
                image_stream = mContext.getContentResolver().openInputStream(sendUri);
                getBitmap = BitmapFactory.decodeStream(image_stream);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return getBitmap;
    }

    @Override
    public void showAllTeamsForm() {
        root.findViewById(R.id.new_team_form).setVisibility(View.GONE);
        root.findViewById(R.id.show_teams_form).setVisibility(View.VISIBLE);
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.requestTeams();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.new_team_btn:
                showNewTeamForm();
                break;
            case R.id.show_teams_btn:
                showAllTeamsForm();
                break;
            case R.id.create_team_btn:
                createTeamBtnPressed();
                break;
            case R.id.profile_image_button:
                selectPicture();
                break;
            default:
                int id = view.getId();
                ((CardView) view).setCardBackgroundColor(Color.parseColor("#aaaaaa"));
                mPresenter.navigateToTeam(id);
        }
    }
}

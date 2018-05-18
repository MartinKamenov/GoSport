package com.kamenov.martin.gosportbg.menu;


import android.app.Fragment;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;
import android.widget.VideoView;

import com.kamenov.martin.gosportbg.GoSportApplication;
import com.kamenov.martin.gosportbg.R;
import com.kamenov.martin.gosportbg.base.contracts.BaseContracts;
import com.kamenov.martin.gosportbg.login.LoginActivity;
import com.kamenov.martin.gosportbg.new_event.NewEventActivity;

import static android.media.MediaCodec.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING;

/**
 * A simple {@link Fragment} subclass.
 */
public class MenuFragment extends Fragment implements MenuContracts.IMenuView, View.OnClickListener {


    private View root;
    private VideoView mVideo;
    private MenuContracts.IMenuPresenter presenter;

    public MenuFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.root = inflater.inflate(R.layout.fragment_menu, container, false);
        presenter.subscribe(this);
        mVideo = root.findViewById(R.id.menu_video);
        Uri uri = Uri.parse("android.resource://" + getActivity().getPackageName() + "/" + R.raw.clip);
        mVideo.setVideoURI(uri);
        mVideo.start();
        mVideo.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                mediaPlayer.setVolume(0f, 0f);
                mediaPlayer.setLooping(true);
            }
        });
        setListeners();
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        mVideo.start();
    }

    @Override
    public void onDestroy() {
        presenter.unsubscribe();
        super.onDestroy();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.new_event:
                newEventButtonPressed();
                break;
            case R.id.incoming_event:
                showEventsButtonPressed();
                break;
            case R.id.logout_btn:
                logoutButtonPressed();
                break;
        }

    }

    private void setListeners() {
        Button newEventButton = root.findViewById(R.id.new_event);
        newEventButton.setOnClickListener(this);

        Button incomingEventButton = root.findViewById(R.id.incoming_event);
        incomingEventButton.setOnClickListener(this);

        Button logoutButton = root.findViewById(R.id.logout_btn);
        logoutButton.setOnClickListener(this);
    }

    @Override
    public void setPresenter(BaseContracts.Presenter presenter) {
        this.presenter = (MenuContracts.IMenuPresenter) presenter;
    }

    @Override
    public GoSportApplication getGoSportApplication() {
        return (GoSportApplication)getActivity().getApplication();
    }

    @Override
    public void newEventButtonPressed() {
        presenter.navigateToCreateNewEvents();
    }

    @Override
    public void showEventsButtonPressed() {
        presenter.navigateToShowEvents();
    }

    @Override
    public void logoutButtonPressed() {
        presenter.logout();
    }

    @Override
    public void navigateToLogin() {
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}

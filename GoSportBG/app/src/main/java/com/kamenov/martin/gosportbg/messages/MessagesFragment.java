package com.kamenov.martin.gosportbg.messages;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kamenov.martin.gosportbg.GoSportApplication;
import com.kamenov.martin.gosportbg.R;
import com.kamenov.martin.gosportbg.base.contracts.BaseContracts;
import com.kamenov.martin.gosportbg.messenger.MessengerContracts;
import com.kamenov.martin.gosportbg.messenger.MessengerFragment;
import com.kamenov.martin.gosportbg.models.MessageCollection;
import com.kamenov.martin.gosportbg.models.MessengerWrapper;

/**
 * A simple {@link Fragment} subclass.
 */
public class MessagesFragment extends Fragment implements MessagesContracts.IMessagesView {


    private MessagesContracts.IMessagesPresenter mPresenter;
    private View root;
    private LinearLayout mMessageWrapperContainer;

    public MessagesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_messages, container, false);
        mMessageWrapperContainer = root.findViewById(R.id.message_wrappers_container);
        mPresenter.getUserMesseges();
        return root;
    }

    @Override
    public void setPresenter(BaseContracts.Presenter presenter) {
        this.mPresenter = (MessagesContracts.IMessagesPresenter)presenter;
    }

    @Override
    public GoSportApplication getGoSportApplication() {
        return (GoSportApplication)getActivity().getApplication();
    }

    @Override
    public void showMessagesOnUIThread(final MessengerWrapper[] messageCollections) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                for(int i = 0; i < messageCollections.length; i++) {
                    addMessengerWrapper(mMessageWrapperContainer, messageCollections[i]);
                }
            }
        });
    }

    private void addMessengerWrapper(LinearLayout container, MessengerWrapper messengerWrapper) {
        TextView textView = new TextView(getActivity());
        textView.setText(messengerWrapper.getTitle());
        container.addView(textView);
    }
}

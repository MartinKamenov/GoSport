package com.kamenov.martin.gosportbg.messages;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.gson.Gson;
import com.kamenov.martin.gosportbg.R;
import com.kamenov.martin.gosportbg.internet.HttpRequester;
import com.kamenov.martin.gosportbg.messenger.MessengerActivity;
import com.kamenov.martin.gosportbg.messenger.MessengerFragment;
import com.kamenov.martin.gosportbg.navigation.ActivityNavigationCommand;

public class MessagesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);

        MessagesFragment messagesFragment = new MessagesFragment();
        MessagesPresenter presenter = new MessagesPresenter(new HttpRequester(), new Gson(),
                new ActivityNavigationCommand(this, MessengerActivity.class));
        messagesFragment.setPresenter(presenter);
        presenter.subscribe(messagesFragment);

        getFragmentManager()
                .beginTransaction()
                .replace(R.id.container, messagesFragment)
                .commit();
    }
}

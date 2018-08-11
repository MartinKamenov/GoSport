package com.kamenov.martin.gosportbg.messenger;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.gson.Gson;
import com.kamenov.martin.gosportbg.R;
import com.kamenov.martin.gosportbg.internet.HttpRequester;

public class MessengerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messenger);

        MessengerFragment messengerFragment = new MessengerFragment();

        int id = getIntent().getIntExtra("id", 2);

        MessengerPresenter messengerPresenter = new MessengerPresenter(new HttpRequester(),
                new Gson(), id);

        messengerFragment.setPresenter(messengerPresenter);
        messengerPresenter.subscribe(messengerFragment);
        messengerPresenter.startChat();

        getFragmentManager()
                .beginTransaction()
                .replace(R.id.container, messengerFragment)
                .commit();
    }
}

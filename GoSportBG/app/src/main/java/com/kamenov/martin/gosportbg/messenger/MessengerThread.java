package com.kamenov.martin.gosportbg.messenger;

import com.kamenov.martin.gosportbg.constants.Constants;
import com.kamenov.martin.gosportbg.event.EventPresenter;
import com.kamenov.martin.gosportbg.internet.HttpRequester;

/**
 * Created by Martin on 11.8.2018 г..
 */

public class MessengerThread  extends Thread {
    private final MessengerPresenter presenter;
    private final HttpRequester requester;
    private boolean running;
    private boolean finished;
    private static int numberOfTries = 10;
    private int id;
    private int tries;

    public MessengerThread(MessengerPresenter presenter, HttpRequester requester, int id) {
        this.presenter = presenter;
        this.requester = requester;
        this.id = id;
        finished = true;
        tries = 0;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    public void setId(int id) { this.id = id; }


    @Override
    public void run() {
        super.run();

        while (running) {
            if(finished) {
                tries = 1;
                finished = false;
                requester.get(presenter, Constants.DOMAIN + "/messages/" + id);
            } else {
                tries++;
                if(tries % numberOfTries == 0) {
                    finished = true;
                }
            }
            try {
                sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

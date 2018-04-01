package com.kamenov.martin.gosportbg.internet.contracts;

import okhttp3.Response;

/**
 * Created by Martin on 1.4.2018 г..
 */

public interface HttpHandler {
    void handleGet(Response response);

    void handlePost(Response response);
}

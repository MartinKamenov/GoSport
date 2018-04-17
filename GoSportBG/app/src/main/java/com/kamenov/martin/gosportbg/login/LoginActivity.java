package com.kamenov.martin.gosportbg.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.kamenov.martin.gosportbg.R;
import com.kamenov.martin.gosportbg.constants.Constants;
import com.kamenov.martin.gosportbg.internet.HttpRequester;
import com.kamenov.martin.gosportbg.internet.contracts.HttpHandler;
import com.kamenov.martin.gosportbg.menu.MenuActivity;

import java.io.IOException;
import java.util.Arrays;

import okhttp3.Response;

public class LoginActivity extends Activity implements HttpHandler, View.OnClickListener {


    private static final String EMAIL = "email";

    private CallbackManager callbackManager;
    private LoginButton loginButton;
    private HttpRequester httpRequester;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        Constants.SCREEN_WIDTH = dm.widthPixels;
        Constants.SCREEN_HEIGHT = dm.heightPixels;
        httpRequester = new HttpRequester(this);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        callbackManager = CallbackManager.Factory.create();
        setContentView(R.layout.activity_login);

        setListeners();

        loginButton = findViewById(R.id.login_button);
        loginButton.setReadPermissions(Arrays.asList(EMAIL));
        // If you are using in a fragment, call loginButton.setFragment(this);

        // Callback registration
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Toast.makeText(LoginActivity.this, "Logged", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(LoginActivity.this, MenuActivity.class);
                startActivity(intent);
            }

            @Override
            public void onCancel() {
                Toast.makeText(LoginActivity.this, "Canceled", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException exception) {
                Toast.makeText(LoginActivity.this, "Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void handleGet(Response response) {

    }

    @Override
    public void handlePost(final Response response) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    String result = response.body().string();
                    Toast.makeText(LoginActivity.this, result.toString(), Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    Toast.makeText(LoginActivity.this, "something went wrong!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void setListeners() {
        Button loginUserButton = findViewById(R.id.login_user);
        Button registerUserButton =  findViewById(R.id.register_user);
        Button showRegisterFormButton = findViewById(R.id.show_register);
        loginUserButton.setOnClickListener(this);
        registerUserButton.setOnClickListener(this);
        showRegisterFormButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.login_user:
                TextView usernameTxt = findViewById(R.id.username_txt);
                TextView passwordTxt = findViewById(R.id.password_txt);
                String username = usernameTxt.getText().toString();
                String password = passwordTxt.getText().toString();
                String body = String.format("{\"username\":\"%s\",\"password\":\"%s\"}", username, password);
                httpRequester.post("https://gosport.herokuapp.com/login", body);
                break;
            case R.id.show_register:
                LinearLayout loginForm = findViewById(R.id.login_form);
                loginForm.setVisibility(View.GONE);
                LinearLayout registerForm =  findViewById(R.id.register_form);
                registerForm.setVisibility(View.VISIBLE);
                break;
            case R.id.register_user:
                String emailTxt = ((TextView)findViewById(R.id.email_txt)).getText().toString();
                String usernameTxtView = ((TextView)findViewById(R.id.username_txt1)).getText().toString();
                String cityTxt = ((TextView)findViewById(R.id.city_txt)).getText().toString();
                String password1Txt = ((TextView)findViewById(R.id.password_txt1)).getText().toString();
                String password2Txt = ((TextView)findViewById(R.id.password_txt2)).getText().toString();
                if(emailTxt.length() == 0 || usernameTxtView.length() == 0 || cityTxt.length() == 0 ||
                        password1Txt.length() == 0 || password2Txt.length() == 0) {
                    Toast.makeText(this, "Моля попълнете всички полета", Toast.LENGTH_SHORT).show();
                    break;
                }
                if(!password1Txt.equals(password2Txt)) {
                    Toast.makeText(this, "Паролите не съвпадат", Toast.LENGTH_SHORT).show();
                    break;
                }
                String registerBody = String.format("{\"email\":\"%s\",\"username\":\"%s\""+
                                ",\"city\":\"%s\",\"password\":\"%s\"}",
                        emailTxt, usernameTxtView, cityTxt, password1Txt);
                httpRequester.post("https://gosport.herokuapp.com/register", registerBody);
        }
    }
}

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
import com.kamenov.martin.gosportbg.base.contracts.BaseContracts;
import com.kamenov.martin.gosportbg.constants.Constants;
import com.kamenov.martin.gosportbg.internet.HttpRequester;
import com.kamenov.martin.gosportbg.menu.MenuActivity;
import com.kamenov.martin.gosportbg.navigation.ActivityNavigationCommand;
import com.kamenov.martin.gosportbg.navigation.NavigationCommand;

import java.util.Arrays;

public class LoginActivity extends Activity implements LoginContracts.ILoginView, View.OnClickListener {


    private static final String EMAIL = "email";

    private CallbackManager callbackManager;
    private LoginButton loginButton;
    private LoginContracts.ILoginPresenter mPresenter;
    private TextView loginUsernameTextView;
    private TextView loginPasswordTextView;
    private TextView registerUsernameTextView;
    private TextView registerEmailTextView;
    private TextView registerPasswordTextView;
    private TextView registerPassword2TextView;
    private TextView registerCityTextView;
    private Button loginUserButton;
    private Button registerUserButton;
    private Button showRegisterFormButton;
    private View loginForm;
    private View registerForm;
    private View progressBarForm;
    private String lastForm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        Constants.SCREEN_WIDTH = dm.widthPixels;
        Constants.SCREEN_HEIGHT = dm.heightPixels;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        callbackManager = CallbackManager.Factory.create();
        NavigationCommand menuNavigationCommand = new ActivityNavigationCommand(this, MenuActivity.class);
        HttpRequester requester = new HttpRequester();
        LoginPresenter presenter = new LoginPresenter(requester, menuNavigationCommand);
        setPresenter(presenter);
        presenter.subscribe(this);
        setContentView(R.layout.activity_login);
        selectSizes();
        setListeners();
        lastForm = "login";

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

    private void setListeners() {
        loginUserButton.setOnClickListener(this);
        registerUserButton.setOnClickListener(this);
        showRegisterFormButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.login_user:
                loginButtonPressed();
                break;
            case R.id.show_register:
                LinearLayout loginForm = findViewById(R.id.login_form);
                loginForm.setVisibility(View.GONE);
                LinearLayout registerForm =  findViewById(R.id.register_form);
                registerForm.setVisibility(View.VISIBLE);
                lastForm = "register";
                break;
            case R.id.register_user:
                registerButtonPressed();
        }
    }

    @Override
    public void setPresenter(BaseContracts.Presenter presenter) {
        this.mPresenter = (LoginContracts.ILoginPresenter) presenter;
    }

    @Override
    public void selectSizes() {
        loginUsernameTextView = findViewById(R.id.username_txt);
        loginPasswordTextView = findViewById(R.id.password_txt);
        registerUsernameTextView = findViewById(R.id.username_txt1);
        registerEmailTextView = findViewById(R.id.email_txt);
        registerPasswordTextView = findViewById(R.id.password_txt1);
        registerPassword2TextView = findViewById(R.id.password_txt2);
        registerCityTextView = findViewById(R.id.city_txt);
        loginUserButton = findViewById(R.id.login_user);
        registerUserButton =  findViewById(R.id.register_user);
        showRegisterFormButton = findViewById(R.id.show_register);
        loginForm = findViewById(R.id.login_form);
        registerForm = findViewById(R.id.register_form);
        progressBarForm = findViewById(R.id.progress_bar_form);
    }

    @Override
    public void loginButtonPressed() {
        String username = loginUsernameTextView.getText().toString();
        String password = loginPasswordTextView.getText().toString();
        mPresenter.login(username, password);
    }

    @Override
    public void registerButtonPressed() {
        String emailTxt = registerEmailTextView.getText().toString();
        String usernameTxtView = registerUsernameTextView.getText().toString();
        String cityTxt = registerCityTextView.getText().toString();
        String password1Txt = registerPasswordTextView.getText().toString();
        String password2Txt = registerPassword2TextView.getText().toString();
        if(emailTxt.length() == 0 || usernameTxtView.length() == 0 || cityTxt.length() == 0 ||
                password1Txt.length() == 0 || password2Txt.length() == 0) {
            Toast.makeText(this, "Моля попълнете всички полета", Toast.LENGTH_SHORT).show();
            return;
        }
        if(!password1Txt.equals(password2Txt)) {
            Toast.makeText(this, "Паролите не съвпадат", Toast.LENGTH_SHORT).show();
            return;
        }

        mPresenter.register(emailTxt, usernameTxtView, password1Txt, cityTxt);
    }

    @Override
    public void notifyUserOnMainTread(final String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Toast.makeText(LoginActivity.this, "something went wrong!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void showProgressBar() {
        loginForm.setVisibility(View.GONE);
        registerForm.setVisibility(View.GONE);
        progressBarForm.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgressBar() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    if(lastForm.equals("login")) {
                        progressBarForm.setVisibility(View.GONE);
                        loginForm.setVisibility(View.VISIBLE);
                    } else {
                        progressBarForm.setVisibility(View.GONE);
                        registerForm.setVisibility(View.VISIBLE);
                    }
                } catch (Exception e) {
                    Toast.makeText(LoginActivity.this, "something went wrong!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}

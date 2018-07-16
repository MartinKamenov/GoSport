package com.kamenov.martin.gosportbg.login;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.gson.Gson;
import com.kamenov.martin.gosportbg.GoSportApplication;
import com.kamenov.martin.gosportbg.R;
import com.kamenov.martin.gosportbg.base.contracts.BaseContracts;
import com.kamenov.martin.gosportbg.constants.Constants;
import com.kamenov.martin.gosportbg.internet.HttpRequester;
import com.kamenov.martin.gosportbg.menu.MenuActivity;
import com.kamenov.martin.gosportbg.navigation.ActivityNavigationCommand;
import com.kamenov.martin.gosportbg.navigation.NavigationCommand;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Arrays;

import de.hdodenhof.circleimageview.CircleImageView;

public class LoginActivity extends Activity implements LoginContracts.ILoginView, View.OnClickListener {


    private static final String EMAIL = "email";
    private static final int SELECT_PHOTO = 1;

    private CallbackManager callbackManager;
    private LoginButton loginButton;
    private LoginContracts.ILoginPresenter mPresenter;
    private TextView loginUsernameTextView;
    private TextView loginPasswordTextView;
    private TextView registerUsernameTextView;
    private TextView registerEmailTextView;
    private TextView registerPasswordTextView;
    private TextView registerPassword2TextView;
    private Spinner registerCitySpinner;
    private Button loginUserButton;
    private Button registerUserButton;
    private Button showRegisterFormButton;
    private Button showLoginFormButton;
    private View loginForm;
    private View registerForm;
    private View progressBarForm;
    private TextView progressBarTxt;
    private String lastForm;
    private CircleImageView profileImage;
    private Button profileImageBtn;
    private Bitmap profileImageBitmap;

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
        Gson gson = new Gson();
        LoginPresenter presenter = new LoginPresenter(requester, gson, menuNavigationCommand);
        setPresenter(presenter);
        presenter.subscribe(this);
        setContentView(R.layout.activity_login);
        selectSizes();
        setListeners();
        lastForm = "login";

        loginButton = findViewById(R.id.login_button);
        loginButton.setReadPermissions(Arrays.asList(
                "public_profile", "email", "user_birthday"));
        mPresenter.tryLoginAuthomaticly();
        // If you are using in a fragment, call loginButton.setFragment(this);

        // Callback registration
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                try {
                                    String email = object.getString("email");
                                    String username = object.getString("first_name") + " " +
                                            object.getString("last_name");
                                    String id = object.getString("id");
                                    String picture = "https://graph.facebook.com/" + id + "/picture?type=large";
                                    mPresenter.facebookLogin(email, username, picture);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,first_name,last_name,email,gender,birthday");
                request.setParameters(parameters);
                request.executeAsync();
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
        if(requestCode == SELECT_PHOTO) {
            if (resultCode == RESULT_OK) {
                if (data != null) {
                    // Get the URI of the selected file
                    final Uri uri = data.getData();
                    profileImageBitmap = decodeUriToBitmap(this, uri);
                    profileImage.setImageBitmap(profileImageBitmap);
                }
            }
        } else {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
        super.onActivityResult(requestCode, resultCode, data);
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

    private void setListeners() {
        loginUserButton.setOnClickListener(this);
        registerUserButton.setOnClickListener(this);
        showRegisterFormButton.setOnClickListener(this);
        showLoginFormButton.setOnClickListener(this);
        profileImageBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.login_user:
                loginButtonPressed();
                break;
            case R.id.show_register:
                showRegister();
                break;
            case R.id.show_login:
                showLogin();
                break;
            case R.id.register_user:
                registerButtonPressed();
                break;
            case R.id.profile_image_button:
                changePictureButtonPressed();
                break;
        }
    }

    private void changePictureButtonPressed() {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, SELECT_PHOTO);
    }

    @Override
    public void setPresenter(BaseContracts.Presenter presenter) {
        this.mPresenter = (LoginContracts.ILoginPresenter) presenter;
    }

    @Override
    public GoSportApplication getGoSportApplication() {
        return (GoSportApplication) getApplication();
    }

    @Override
    public void selectSizes() {
        loginUsernameTextView = findViewById(R.id.username_txt);
        loginPasswordTextView = findViewById(R.id.password_txt);
        registerUsernameTextView = findViewById(R.id.username_txt1);
        registerEmailTextView = findViewById(R.id.email_txt);
        registerPasswordTextView = findViewById(R.id.password_txt1);
        registerPassword2TextView = findViewById(R.id.password_txt2);
        registerCitySpinner = findViewById(R.id.city_spinner);
        registerCitySpinner.setAdapter(getCityAdapter());
        loginUserButton = findViewById(R.id.login_user);
        registerUserButton =  findViewById(R.id.register_user);
        showRegisterFormButton = findViewById(R.id.show_register);
        loginForm = findViewById(R.id.login_form);
        registerForm = findViewById(R.id.register_form);
        progressBarForm = findViewById(R.id.progress_bar_form);
        showLoginFormButton = findViewById(R.id.show_login);
        progressBarTxt = findViewById(R.id.progress_txt);
        profileImage = findViewById(R.id.profile_image);
        profileImageBtn = findViewById(R.id.profile_image_button);
    }

    @Override
    public void loginButtonPressed() {
        String username = loginUsernameTextView.getText().toString();
        String password = loginPasswordTextView.getText().toString();
        if(username.length() == 0 || password.length() == 0) {
            Toast.makeText(this, "Моля попълнете всички полета", Toast.LENGTH_SHORT).show();
            return;
        }
        mPresenter.login(username, password);
    }

    public void hideKeyboardFrom() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(loginForm.getWindowToken(), 0);
    }

    @Override
    public ArrayAdapter<String> getCityAdapter() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                mPresenter.getAllCities()
        );

        return adapter;
    }

    @Override
    public void registerButtonPressed() {
        String emailTxt = registerEmailTextView.getText().toString();
        String usernameTxtView = registerUsernameTextView.getText().toString();
        String cityTxt = registerCitySpinner.getSelectedItem().toString();
        String password1Txt = registerPasswordTextView.getText().toString();
        String password2Txt = registerPassword2TextView.getText().toString();
        String pictureString = null;
        if(profileImageBitmap != null) {
            pictureString = bitMapToString(profileImageBitmap);
        }
        if(emailTxt.length() == 0 || usernameTxtView.length() == 0 || cityTxt.length() == 0 ||
                password1Txt.length() == 0 || password2Txt.length() == 0) {
            Toast.makeText(this, "Моля попълнете всички полета", Toast.LENGTH_SHORT).show();
            return;
        }
        if(!password1Txt.equals(password2Txt)) {
            Toast.makeText(this, "Паролите не съвпадат", Toast.LENGTH_SHORT).show();
            return;
        }

        mPresenter.register(emailTxt, usernameTxtView, password1Txt, cityTxt, pictureString);
    }

    public String bitMapToString(Bitmap bitmap){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte [] b = baos.toByteArray();
        String temp = Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
    }

    @Override
    public void showLogin() {
        lastForm = "login";

        LinearLayout registerForm =  findViewById(R.id.register_form);
        registerForm.setVisibility(View.GONE);
        LinearLayout loginForm = findViewById(R.id.login_form);
        loginForm.setVisibility(View.VISIBLE);
        progressBarTxt.setText("Влизане...");
    }

    @Override
    public void showRegister() {
        lastForm = "register";

        LinearLayout loginForm = findViewById(R.id.login_form);
        loginForm.setVisibility(View.GONE);
        LinearLayout registerForm =  findViewById(R.id.register_form);
        registerForm.setVisibility(View.VISIBLE);
        progressBarTxt.setText("Регистриране...");
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
        hideKeyboardFrom();
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

    @Override
    public void showMessageOnUIThread(final String message) {
        runOnUiThread(
                new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
                    }
                }
        );
    }
}

package com.kamenov.martin.gosportbg.messenger;


import android.app.Activity;
import android.app.Fragment;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.text.method.ScrollingMovementMethod;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.kamenov.martin.gosportbg.GoSportApplication;
import com.kamenov.martin.gosportbg.R;
import com.kamenov.martin.gosportbg.base.contracts.BaseContracts;
import com.kamenov.martin.gosportbg.constants.Constants;
import com.kamenov.martin.gosportbg.event.EventActivity;
import com.kamenov.martin.gosportbg.internet.DownloadImageTask;
import com.kamenov.martin.gosportbg.messages.MessagesActivity;
import com.kamenov.martin.gosportbg.models.Message;
import com.kamenov.martin.gosportbg.models.optimizators.PictureSavior;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 */
public class MessengerFragment extends Fragment implements MessengerContracts.IMessengerView, View.OnClickListener, View.OnKeyListener {


    private View root;
    private MessengerContracts.IMessengerPresenter mPresenter;

    private ScrollView scrollView;
    private String lastMessageString;
    private LinearLayout messageContainer;
    private Message lastMessage;
    private EditText message;
    private Button submitButton;
    private int messageLinesCount;
    private LinearLayout messageTextContainer;
    private PictureSavior pictureSavior;

    public MessengerFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        this.root = inflater.inflate(R.layout.fragment_messenger, container, false);
        pictureSavior = PictureSavior.getInstance();
        root.findViewById(R.id.messenger_container).setBackgroundColor(Constants.MAINCOLOR);
        messageLinesCount = 1;
        submitButton = root.findViewById(R.id.submit);
        messageTextContainer = root.findViewById(R.id.message_txt_container);
        submitButton.setOnClickListener(this);
        scrollView = root.findViewById(R.id.scrollView);
        messageContainer = root.findViewById(R.id.messages_container);
        message = root.findViewById(R.id.message);
        message.setTextColor(Constants.SECONDCOLOR);
        message.setOnKeyListener(this);
        message.setMovementMethod(new ScrollingMovementMethod());
        // getActivity().getWindow().setBackgroundDrawableResource(R.drawable.messenger_background);
        return root;
    }

    @Override
    public void setPresenter(BaseContracts.Presenter presenter) {
        this.mPresenter = (MessengerContracts.IMessengerPresenter)presenter;
    }

    @Override
    public GoSportApplication getGoSportApplication() {
        return (GoSportApplication)getActivity().getApplication();
    }

    @Override
    public void showMessageOnUITread(final String message) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void hideKeyboardFrom() {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(message.getWindowToken(), 0);
    }

    @Override
    public void changeMessageContainerSize() {
        int messageContainerHeight = messageTextContainer.getHeight();
        RelativeLayout.LayoutParams scrollParams = (RelativeLayout.LayoutParams)scrollView.getLayoutParams();
        scrollParams.bottomMargin = messageContainerHeight;
        scrollView.setLayoutParams(scrollParams);
    }

    @Override
    public void addMessageButtonPressed() {
        String messageText = message.getText().toString();
        message.setText("");
        hideKeyboardFrom();
        mPresenter.addMessage(messageText);
    }

    @Override
    public void addMessagesOnUIThread(final Message[] messages) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(lastMessage != null && lastMessage.username.equals(messages[messages.length - 1].username)
                        && lastMessage.text.equals(messages[messages.length - 1].text)
                        && lastMessage.dateTime.minute == messages[messages.length - 1].dateTime.minute) {
                    lastMessage = messages[messages.length - 1];
                    return;
                }
                if(messages.length > 0) {
                    lastMessage = messages[messages.length - 1];
                }
                int margin = 15;
                String currentUserUsername = mPresenter.getLocalUser().getUsername();
                messageContainer.removeAllViews();
                String lastUser = "";
                for(int i = 0; i < messages.length; i++) {
                    RelativeLayout relativeLayout = new RelativeLayout(getActivity());
                    boolean shouldBeRight = false;
                    if(!lastUser.equals(messages[i].username)) {
                        lastUser = messages[i].username;
                        LinearLayout linearLayout = new LinearLayout(getActivity());
                        linearLayout.setOrientation(LinearLayout.VERTICAL);
                        TextView usernameTextView = new TextView(getActivity());
                        usernameTextView.setTextColor(Color.WHITE);
                        usernameTextView.setText(messages[i].username);
                        usernameTextView.setTypeface(Typeface.create("sans-serif-condensed", Typeface.NORMAL), Typeface.BOLD);
                        usernameTextView.setGravity(Gravity.CENTER);
                        usernameTextView.setLayoutParams(new LinearLayout.LayoutParams(Constants.SCREEN_WIDTH / 2, ViewGroup.LayoutParams.WRAP_CONTENT));
                        View img;
                        String url;

                        if(messages[i].profileImg != null && messages[i].profileImg.startsWith("https://graph.facebook")) {
                            url = messages[i].profileImg;
                        }
                        else {
                            url = Constants.DOMAIN + messages[i].profileImg;

                        }
                        if(!pictureSavior.hasBitmap(url)) {
                            img = new ProgressBar(getActivity());
                            new DownloadImageTask((ProgressBar) img, getActivity())
                                    .execute(url);
                        } else {
                            img = new CircleImageView(getActivity());
                            ((CircleImageView)img).setImageBitmap(pictureSavior.getBitmap(url));
                        }
                        linearLayout.addView(img);
                        img.getLayoutParams().height = 150;
                        linearLayout.addView(usernameTextView);
                        relativeLayout.addView(linearLayout);
                        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) linearLayout.getLayoutParams();
                        lp.setMargins(margin, margin, margin, margin);
                        if(currentUserUsername.equals(messages[i].username)) {
                            lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                        }
                        linearLayout.setLayoutParams(lp);
                        messageContainer.addView(relativeLayout);
                        i--;
                        continue;
                    }
                    CardView cardView = new CardView(getActivity());
                    cardView.setPreventCornerOverlap(true);
                    cardView.setRadius(45);
                    // TO DO:
                    // Fix for older versions
                    // cardView.setElevation(10);

                    TextView textView = new TextView(getActivity());
                    textView.setPadding(20, 20, 20 , 20);
                    textView.setText(messages[i].text);
                    textView.setGravity(Gravity.CENTER);
                    textView.setTypeface(Typeface.create("sans-serif-condensed", Typeface.NORMAL), Typeface.BOLD);
                    textView.setLayoutParams(new LinearLayout.LayoutParams(Constants.SCREEN_WIDTH / 2, ViewGroup.LayoutParams.WRAP_CONTENT));

                    if(currentUserUsername.equals(messages[i].username)) {
                        textView.setBackgroundResource(R.drawable.back);
                        textView.setTextColor(Constants.SECONDCOLOR);
                        shouldBeRight = true;
                    } else {
                        textView.setBackgroundResource(R.drawable.others);
                        textView.setTextColor(Color.parseColor("#3d3d3d"));
                    }

                    cardView.addView(textView);
                    relativeLayout.addView(cardView);
                    messageContainer.addView(relativeLayout);
                    RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) cardView.getLayoutParams();
                    lp.setMargins(margin, margin, margin, 0);
                    if(shouldBeRight) {
                        lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                    }
                    cardView.setLayoutParams(lp);
                }
                if(lastMessageString == null) {
                    scrollView.post(new Runnable() {
                        @Override
                        public void run() {
                            scrollView.fullScroll(View.FOCUS_DOWN);
                        }
                    });
                } else if(messages.length > 0 &&!lastMessageString.equals(messages[messages.length - 1].text)) {
                    scrollView.post(new Runnable() {
                        @Override
                        public void run() {
                            scrollView.fullScroll(View.FOCUS_DOWN);
                        }
                    });
                }

                if(messages.length != 0) {
                    lastMessageString = messages[messages.length - 1].text;
                }

                mPresenter.finishQuery();
            }
        });
    }

    private int fetchAccentColor() {
        TypedValue typedValue = new TypedValue();

        TypedArray a = getActivity().obtainStyledAttributes(typedValue.data, new int[] { R.attr.colorAccent });
        int color = a.getColor(0, 0);

        a.recycle();

        return color;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.submit:
                addMessageButtonPressed();
                break;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        mPresenter.onPause();
    }

    @Override
    public boolean onKey(View view, int i, KeyEvent keyEvent) {
        if(message.getLineCount() != messageLinesCount) {
            messageLinesCount = message.getLineCount();
            changeMessageContainerSize();
        }
        return false;
    }
}

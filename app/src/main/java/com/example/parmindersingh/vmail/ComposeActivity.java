package com.example.parmindersingh.vmail;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.text.Layout;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;


public class ComposeActivity extends AppCompatActivity implements TextToSpeech.OnUtteranceCompletedListener {

    private TextToSpeech tts;
    private TextView status;
    private TextView To, Subject, Message;
    private int numberOfClicks;
    private Layout linearLayout;
    Intent speechIntent;
    private TextView type;
    private Button sendButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);

        tts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    int result = tts.setLanguage(Locale.US);
                    if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Log.e("TTS", "This Language is not supported");
                    }
                    speak("Composing a new mail. Tell me the mail address to whom you want to send mail?");

                } else {
                    Log.e("TTS", "Initilization Failed!");
                }
            }
        });

        status = (TextView) findViewById(R.id.status);
        To = (TextView) findViewById(R.id.to);
        Subject = (TextView) findViewById(R.id.subject);
        Message = (TextView) findViewById(R.id.message);
        sendButton = (Button) findViewById(R.id.send);
        //linearLayout layout = (LinearLayout) findViewById(linearLayout);
        numberOfClicks = 0;

        speechIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        speechIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        speechIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        if (speechIntent.resolveActivity(getPackageManager()) != null) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    startActivityForResult(speechIntent, 1);
                }
            }, 5000);

        } else {
            speak("Your Device Don't Support Speech Input");
        }
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layoutClicked();
            }
        });
    }

    public void type(View view) {

        type = (TextView) findViewById(R.id.edtType);
        String Type = type.getText().toString();

        if (Type.equals("TYPE")) {
            sendButton.setOnClickListener(null);
            type.setText("SPEAK");
            Toast.makeText(this, "Manual Typing Enabled!", Toast.LENGTH_SHORT).show();
        } else {
            sendButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    layoutClicked();
                }
            });
            type.setText("TYPE");
        }

    }

    private void speak(String text) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);
        } else {
            tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
        }
    }

    @Override
    public void onDestroy() {
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
        super.onDestroy();
    }

    public void speech(View view) {

        //layoutClicked();
    }

    // Tap outside of components to remove focus
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (v instanceof EditText) {
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int) event.getRawX(), (int) event.getRawY())) {
                    v.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(
                            Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    layoutClicked();
                }
            }
        }
        return super.dispatchTouchEvent(event);
    }

    public void layoutClicked() {
        speak("tell me the email ID to whom you want to send mail");
        speechIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        speechIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        speechIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        if (speechIntent.resolveActivity(getPackageManager()) != null) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    startActivityForResult(speechIntent, 1);
                }
            }, 4000);

        } else {
            speak("your device does not support speech input");
        }
    }

    private void listen() {
        Intent i = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        i.putExtra(RecognizerIntent.EXTRA_PROMPT, "Say something");

        try {
            startActivityForResult(i, 100);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(this, "Your device doesn't support Speech Recognition", Toast.LENGTH_SHORT).show();
        }
    }

    private void sendEmail() {
        //Getting content for email
        String email = To.getText().toString().trim();
        String subject = Subject.getText().toString().trim();
        String message = Message.getText().toString().trim();

        // Optimal solution: Whenever user login or signup
        // Save user's email and password in local storage using concept "Shared Preferences"

        // And get those user Email and user Password from Shared Preferences
        // And Pass them to this SendMail method


        SharedPreferences sp1 = this.getSharedPreferences("Login", 0);
        String unm = sp1.getString("Unm", null);
        String pass = sp1.getString("Psw", null);
        // Otherwise:
        // Stupid but quick way is to pass Email and Password through Intents
        // and retrieve them into above strings
        String userEmail = unm;
        String userPassword = pass;


        //Creating SendMail object
        SendMail sm = new SendMail(this, userEmail, userPassword, email, subject, message);

        //Executing sendmail to send email
        sm.execute();
    }

    private void exitFromApp() {
        this.finishAffinity();
    }

    public void restartActivity() {
        Intent mIntent = getIntent();
        finish();
        startActivity(mIntent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && null != data) {
            ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            if (result.get(0).equals("cancel")) {
                speak("Cancelled , going back to dashboard");
                Intent i = new Intent(this, DashActivity.class);
                startActivity(i);
            } else if (result.get(0).equals("restart")) {
                speak("restarting");
                restartActivity();
            } else {
                switch (requestCode) {
                    case 1:
                        String to;
                        to = result.get(0).replaceAll("underscore", "_");
                        to = to.replaceAll("\\s+", "");
                        //to= to + "@gmail.com";
                        To.setText(to);
                        //To.setText(((result.get(0)).replace(" ", "")).trim());
                        status.setText("Subject?");
                        speak("The email you entered is " + result.get(0));
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                speak("What should be the subject? ");
                            }
                        }, 5000);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                startActivityForResult(speechIntent, 2);
                            }
                        }, 7000);
                        break;
                    case 2:
                        Subject.setText(result.get(0));
                        status.setText("Message?");
                        speak("The subject is " + result.get(0));
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                speak("Give me message");
                            }
                        }, 4000);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                startActivityForResult(speechIntent, 3);
                            }
                        }, 5000);
                        break;
                    case 3:
                        Message.setText(result.get(0));
                        status.setText("Confirm?");

                        HashMap<String, String> hashParam = new HashMap<String, String>();
                        hashParam.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "MESSAGE_DONE");
                        tts.speak("Please Confirm the mail\n To : " + To.getText().toString() +
                                        "\nSubject : " + Subject.getText().toString() + "\nMessage : " +
                                        Message.getText().toString() + "\nSpeak Yes to confirm OR No to compose the mail again",
                                TextToSpeech.QUEUE_FLUSH, hashParam);


                        break;
                    case 4:
                        if (result.get(0).equals("yes")) {
                            status.setText("Sending");
                            speak("Sending the mail");
                            sendEmail();
                        } else if (result.get(0).equals("No")) {
                            status.setText("TO");
                            speak("Tell me the mail address to whom yo want to send mail");
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    startActivityForResult(speechIntent, 1);
                                }
                            }, 4000);
                        }
                }
            }
        }

    }

    @Override
    public void onUtteranceCompleted(String utteranceId) {

        if (utteranceId.equals("MESSAGE_DONE")) {
            startActivityForResult(speechIntent, 4);
            Log.i("Check 1","Successful");
        }
        Log.i("Check 2","Successful");
    }
}

package com.example.parmindersingh.vmail;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Handler;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.Locale;

public class DashActivity extends AppCompatActivity {

    private TextToSpeech tts;
    Intent speechIntent;

    public  void Compose(View view){

        compose();
    }

    public void compose() {

        Intent intent = new Intent(this, ComposeActivity.class);
        startActivity(intent);

    }

   /* SharedPreferences sp1 = this.getSharedPreferences("Register",0);
    String username = sp1.getString("username", null);*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash);

        tts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    int result = tts.setLanguage(Locale.US);
                    if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        speak("This Language is not supported");
                    }
                    speak("Welcome. what would you ike to do?. compose,or check inbox");

                } else {
                    speak("Initilization Failed!");
                }
            }
        });

        speechIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        speechIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        speechIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        if (speechIntent.resolveActivity(getPackageManager()) != null) {
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    startActivityForResult(speechIntent, 1);
                }
            }, 7000);

        } else {
            speak("Your Device Don't Support Speech Input");
        }

    }

    private void speak(String text) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);
        } else {
            tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
        }
    }

    private void exitFromApp() {
        this.finishAffinity();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && null != data) {
            ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            if (result.get(0).equals("cancel")) {
                speak("Cancelled!");
                exitFromApp();
            } else {
                switch (requestCode) {
                    case 1:
                        if (result.get(0).equals("compose")) {
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    compose();
                                }
                            }, 2000);


                        } else if (result.get(0).equals("register")) {

                            speak("starting new registration of an account. What is your full name?");
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    startActivityForResult(speechIntent, 20);
                                }
                            }, 5000);

                        } else if (result.get(0).equals("")) {

                            startActivityForResult(speechIntent, 1);
                        }
                        break;


                }
            }
        }
    }
}
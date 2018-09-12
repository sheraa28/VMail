package com.example.parmindersingh.vmail;

import android.graphics.Paint;
import android.os.Build;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Locale;


public class LoginActivity extends AppCompatActivity {

    Intent manualInput;
    Intent manual2Input;
    Intent speechIntent;
    TextToSpeech toSpeech;
    EditText editText;
    String text;
    TextView textView;
    int result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        /*MediaPlayer mp = MediaPlayer.create(this, R.raw.click_anywhere_v);
        mp.start();*/
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        toSpeech = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    int result = toSpeech.setLanguage(Locale.UK);

                    if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        //Log.e("TTS", "This Language is not supported");
                        speak("This language is not supported");
                    }
                    speak("Welcome to voice mail. What would you like to do. Login or Register?");

                } else {
                    speak("Voice feature is not supported in your device");
                }
            }
        });
        TextView textView = (TextView) findViewById(R.id.textView2);
        textView.setPaintFlags(textView.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

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
        
        manual2Input = new Intent(this, RegisterActivity.class);

    }
    private void speak(String text){

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);
        }else{
           toSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null);
        }
    }


    public void getSpeechInput(View view) {
        /*final MediaPlayer mp = MediaPlayer.create(this, R.raw.enter_email);
        mp.start();*/
        speak("Login or register?");
        speechIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        speechIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        speechIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        if (speechIntent.resolveActivity(getPackageManager()) != null) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    startActivityForResult(speechIntent, 1);
                }
            }, 2000);

        } else {
            speak("your device does not support speech input");
        }
        editText=(EditText) findViewById(R.id.emailEditText);
        toSpeech=new TextToSpeech(LoginActivity.this, new TextToSpeech.OnInitListener() {
        @Override
        public void onInit(int status) {
            if(status==TextToSpeech.SUCCESS)
            {
                result=toSpeech.setLanguage(Locale.UK);
            }
            else
            {
                Toast.makeText(getApplicationContext(),"Feature not supported in your device",Toast.LENGTH_SHORT).show();
            }
        }
    });

}

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (toSpeech!=null)
        {
            toSpeech.stop();
            toSpeech.shutdown();
        }
        super.onDestroy();

    }
    private void exitFromApp()
    {
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
                            if (result.get(0).equals("login")) {
                                speak("tell me your email I D");
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        startActivityForResult(speechIntent, 10);
                                    }
                                }, 2000);


                            } else if (result.get(0).equals("register")) {

                                speak("starting new registration of an account. What is your full name?");
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        startActivityForResult(speechIntent,20);
                                    }
                                }, 5000);

                            } else if (result.get(0) == null) {

                                startActivityForResult(speechIntent,1);
                            }
                                break;
                        case 10:
                            if (resultCode == RESULT_OK && data != null) {
                                String input = ((result.get(0)).replace(" ", "")).trim();
                                manualInput = new Intent(this, ManLoginActivity.class);
                                manualInput.putExtra("username", input);
                                toSpeech.speak("your email is " + input, TextToSpeech.QUEUE_FLUSH, null);

                                Handler handler = new Handler();
                                //final MediaPlayer mp = MediaPlayer.create(this, R.raw.enter_passwordv);
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {

                                        speak("what is your password");
                                       // mp.start();
                                    }
                                }, 5000);

                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        startActivityForResult(speechIntent, 11);
                                    }
                                }, 8000);
                            }

                            break;
                        case 11:
                            if (resultCode == RESULT_OK && data != null) {
                                String input = ((result.get(0)).replace(" ", "")).trim();

                                manualInput.putExtra("password", input);

                                startActivity(manualInput);
                            }
                            break;
                        case 20:
                            if (resultCode == RESULT_OK && data != null) {
                                final String input = (result.get(0));
                                manual2Input.putExtra("Full name", input);
                                toSpeech.speak("the name is  " + input , TextToSpeech.QUEUE_FLUSH, null);

                                Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {

                                      speak("what is your email ID" );
                                    }
                                }, 2000);

                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        startActivityForResult(speechIntent, 21);
                                    }
                                }, 4000);
                            }
                            break;
                        case 21:
                            if (resultCode == RESULT_OK && data != null) {
                                String input = (result.get(0));
                                manual2Input.putExtra("email", input);
                                toSpeech.speak("the email is  " + input , TextToSpeech.QUEUE_FLUSH, null);

                                Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {

                                        speak("what is your contact number");
                                    }
                                }, 2000);

                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        startActivityForResult(speechIntent, 22);
                                    }
                                }, 4000);
                            }
                            break;

                        case 22:
                            if (resultCode == RESULT_OK && data != null) {
                                String input = (result.get(0));
                                manual2Input.putExtra("contact", input);
                                toSpeech.speak("the contact number is  " + input , TextToSpeech.QUEUE_FLUSH, null);

                                Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {

                                        speak("what is your password");
                                    }
                                }, 3000);

                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        startActivityForResult(speechIntent, 23);
                                    }
                                }, 5000);
                            }
                            break;
                        case 23:
                            if (resultCode == RESULT_OK && data != null) {
                                String input = (result.get(0));
                                manual2Input.putExtra("password", input);
                                //toSpeech.speak("the name you entered is  " + input , TextToSpeech.QUEUE_FLUSH, null);

                                Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {

                                        speak("say the password again to confirm");
                                    }
                                }, 2000);

                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        startActivityForResult(speechIntent, 24);
                                    }
                                }, 5000);
                            }
                            break;
                        case 24:
                            if (resultCode == RESULT_OK && data != null) {
                                String input = (result.get(0));
                                manual2Input.putExtra("cnfpassword", input);
                                //toSpeech.speak("the name you entered is  " + input , TextToSpeech.QUEUE_FLUSH, null);

                                Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {

                                        speak("say 'YES' to continue or 'NO' to enter the details again");
                                    }
                                }, 2000);

                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        startActivityForResult(speechIntent, 25);
                                    }
                                }, 5000);
                            }
                            break;
                        case 25:
                            if (result.get(0).equals("yes")) {
                                speak("registering your new account");

                                startActivity(manual2Input);


                            } else if (result.get(0).equals("no")) {

                                speak("Enter the details again. What is your full name");
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        startActivityForResult(speechIntent,20);
                                    }
                                }, 2000);

                            }
                            break;

                    }

                }
            }
    }
    public void orType(View view)
    {
        Intent intent = new Intent(this, ManLoginActivity.class);
        startActivity(intent);
    }
}

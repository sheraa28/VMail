package com.example.parmindersingh.vmail.activity;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.parmindersingh.vmail.R;

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
    ImageView imageView;
    int result;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        TextView textView = (TextView) findViewById(R.id.textView);
        TextView textView2 = (TextView) findViewById(R.id.textView2);
        textView.setPaintFlags(textView.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);


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

    private void speak(String text) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);
        } else {
            toSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null);
        }
    }


    public void getSpeechInput(View view) {

        imageView = (ImageView) findViewById(R.id.imageView);

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
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (toSpeech != null) {
            toSpeech.stop();
            toSpeech.shutdown();
        }
        super.onDestroy();

    }

    private void exitFromApp() {
        this.finishAffinity();
    }

    public void restartActivity() {
        Intent mIntent = getIntent();
        finish();
        startActivity(mIntent);
    }

    String passCnf = null;
    String cnfPass = null;

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 8;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && null != data) {
            ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            if (result.get(0).equals("exit")) {
                speak("Exiting from the application");
                exitFromApp();
            } else if (result.get(0).equals("restart")) {
                speak("restarting");
                restartActivity();
            } else if (result.get(0).equals("Restart login")) {
                speak("Restarting login, tell me your email ID");
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startActivityForResult(speechIntent, 10);
                    }
                }, 3000);
            } else if (result.get(0).equals("Restart registration")) {
                speak("Restarting the registration, What is your full name");
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startActivityForResult(speechIntent, 20);
                    }
                }, 3000);
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
                                    startActivityForResult(speechIntent, 20);
                                }
                            }, 5000);

                        } else if (result.get(0).equals("")) {

                            startActivityForResult(speechIntent, 1);
                        }
                        break;
                    case 10:
                        if (resultCode == RESULT_OK && data != null) {
                            String input = ((result.get(0)).replace(" ", "")).trim();
                            manualInput = new Intent(this, ManLoginActivity.class);
                            manualInput.putExtra("username", input);
                            toSpeech.speak("your email is " + input, TextToSpeech.QUEUE_FLUSH, null);

                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {

                                    speak("Say YES to continue or No to enter your email again");
                                    // mp.start();
                                }
                            }, 5000);

                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    startActivityForResult(speechIntent, 30);
                                }
                            }, 9000);
                        }

                        break;
                    case 30:
                        if (result.get(0).equals("yes")) {
                            speak("what is your password?");
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    startActivityForResult(speechIntent, 11);
                                }
                            }, 2000);


                        } else if (result.get(0).equals("no")) {

                            speak("Enter the email again");
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    startActivityForResult(speechIntent, 10);
                                }
                            }, 2000);

                        }/*else {
                                startActivityForResult(speechIntent, 30);
                            }*/
                        break;

                    case 11:
                        if (resultCode == RESULT_OK && data != null) {
                            String input = ((result.get(0)).toLowerCase().replace(" ", "")).trim();

                            manualInput.putExtra("password", input);

                            startActivity(manualInput);
                        }
                        break;
                    case 20:
                        if (resultCode == RESULT_OK && data != null) {
                            String input = result.get(0);
                            Log.d("Debug", "Full Name: " + input);
                            manual2Input.putExtra("fullname", input);
                            toSpeech.speak("the name is  " + input, TextToSpeech.QUEUE_FLUSH, null);

                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {

                                    speak("what is your email ID");
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
                            String input = ((result.get(0)).replace(" ", "")).trim();
                            manual2Input.putExtra("email", input);
                            toSpeech.speak("the email is  " + input, TextToSpeech.QUEUE_FLUSH, null);

                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {

                                    speak("what should be the password ");
                                }
                            }, 5000);

                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    startActivityForResult(speechIntent, 22);
                                }
                            }, 2000);
                        }
                        break;

                    case 22:
                        if (resultCode == RESULT_OK && data != null) {
                            String input = ((result.get(0)).toLowerCase().replace(" ", "")).trim();
                            manual2Input.putExtra("password", input);
                            passCnf = input.toString();


                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {

                                    speak("say the password again to confirm");
                                }
                            }, 5000);

                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    startActivityForResult(speechIntent, 23);
                                }
                            }, 7000);
                        }
                        break;
                    case 23:
                        if (resultCode == RESULT_OK && data != null) {
                            String input = ((result.get(0)).toLowerCase().replace(" ", "")).trim();
                            manual2Input.putExtra("cnfpassword", input);
                            cnfPass = input.toString();
                            if (cnfPass.equals(passCnf)) {
                                Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {

                                        speak("say YES to continue or 'NO' to enter the details again");
                                    }
                                }, 4000);

                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        startActivityForResult(speechIntent, 24);
                                    }
                                }, 5000);
                            } else {
                                speak("Passwords did not match. Please say the password again");

                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        startActivityForResult(speechIntent, 22);
                                    }
                                }, 3000);


                            }

                        }
                        break;

                    case 24:
                        if (result.get(0).equals("yes")) {
                            speak("registering your new account");

                            startActivity(manual2Input);


                        } else if (result.get(0).equals("no")) {

                            speak("Enter the details again. What is your full name");
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    startActivityForResult(speechIntent, 20);
                                }
                            }, 2000);

                        } else {
                            startActivityForResult(speechIntent, 24);
                        }
                        break;

                }

            }
        }
    }

    public void orType(View view) {
        Intent intent = new Intent(this, ManLoginActivity.class);
        startActivity(intent);
    }
}

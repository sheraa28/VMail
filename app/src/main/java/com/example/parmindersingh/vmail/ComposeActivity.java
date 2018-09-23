package com.example.parmindersingh.vmail;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Handler;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Locale;


public class ComposeActivity extends AppCompatActivity {

    private TextToSpeech tts;
    private TextView status;
    private TextView To,Subject,Message;
    private int numberOfClicks;
    Intent speechIntent;
    private LinearLayout layout;
    private Button send;
    private TextView type;

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

        status = (TextView)findViewById(R.id.status);
        To = (TextView) findViewById(R.id.to);
        Subject  =(TextView)findViewById(R.id.subject);
        Message = (TextView) findViewById(R.id.message);
        layout = (LinearLayout) findViewById(R.id.linearLayout);
        send = (Button) findViewById(R.id.send);
        type = (TextView) findViewById(R.id.edtType);


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
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layoutClicked();
            }
        });
        send.setOnClickListener(null);
    }

    public void type (View view){
        String edtTxt = type.getText().toString();

        if(edtTxt.equals("TYPE") ) {
            layout.setOnClickListener(null);
            send.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sendEmail();
                }
            });
            type.setText("SPEAK");


            Toast.makeText(ComposeActivity.this, "Manual Typing Enabled!", Toast.LENGTH_SHORT).show();
        }else {
            type.setText("TYPE");
            layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    layoutClicked();
                    Toast.makeText(ComposeActivity.this, "Voice Interaction Enabled!", Toast.LENGTH_SHORT).show();

                }
            });


        }

    }

    private void speak(String text){

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);
        }else{
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

    public void layoutClicked()
    {
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

    private void sendEmail() {
        //Getting content for email
        String email = To.getText().toString().trim();
        String subject = Subject.getText().toString().trim();
        String message = Message.getText().toString().trim();

        // Optimal solution: Whenever user login or signup
        // Save user's email and password in local storage using concept "Shared Preferences"

        // And get those user Email and user Password from Shared Preferences
        // And Pass them to this SendMail method


        SharedPreferences sp1=this.getSharedPreferences("Login",0);
        String unm=sp1.getString("Unm", null);
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

    private void exitFromApp()
    {
        this.finishAffinity();
    }

    public void restartActivity(){
        Intent mIntent = getIntent();
        finish();
        startActivity(mIntent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
            if (resultCode == RESULT_OK && null != data) {
                ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                if(result.get(0).equals("cancel"))
                {
                    speak("Cancelled");
                    restartActivity();
                }
                else {
                    switch (requestCode) {
                        case 1:
                            String to;
                            to= result.get(0).replaceAll("underscore","_");
                            to = to.replaceAll("\\s+","");
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
                            }, 3000);
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    startActivityForResult(speechIntent, 2);
                                }
                            }, 5000);
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
                            speak("Please Confirm the mail\n To : " + To.getText().toString() + "\nSubject : " + Subject.getText().toString() + "\nMessage : " + Message.getText().toString() + "\nSpeak Yes to confirm OR No to compose the mail again");
                            startActivityForResult( speechIntent,4);
                            break;
                        case 4:
                            if(result.get(0).equals("yes"))
                            {
                                status.setText("Sending");
                                speak("Sending the mail");
                                sendEmail();
                            }else if (result.get(0).equals("No"))
                            {
                                status.setText("TO");
                                speak("Tell me the mail address to whom yo want to send mail");
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        startActivityForResult(speechIntent,1);
                                    }
                                }, 4000);
                            }
                    }
                }
            }

    }
}

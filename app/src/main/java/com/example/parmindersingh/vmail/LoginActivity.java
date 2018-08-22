package com.example.parmindersingh.vmail;

import android.media.MediaPlayer;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Locale;


public class LoginActivity extends AppCompatActivity {

    Intent manualInput;
    Intent speechIntent;
    TextToSpeech toSpeech;
    EditText editText;
    String text;
    int result;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        MediaPlayer mp = MediaPlayer.create(this, R.raw.click_anywhere_v);
        mp.start();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }
    public void getSpeechInput(View view) {
        final MediaPlayer mp = MediaPlayer.create(this, R.raw.enter_email);
        mp.start();
        speechIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        speechIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        speechIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        if (speechIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(speechIntent, 10);
        } else {
            Toast.makeText(this, "Your Device Don't Support Speech Input", Toast.LENGTH_SHORT).show();
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

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 10:
                if (resultCode == RESULT_OK && data != null) {
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    String input = ((result.get(0)).replace(" ", "")).trim();
                    manualInput = new Intent(this, ManLoginActivity.class);
                    manualInput.putExtra("username", input);
                    toSpeech.speak( "your email is " + input,TextToSpeech.QUEUE_FLUSH,null);

                     startActivityForResult(speechIntent, 11);
                    MediaPlayer mp = MediaPlayer.create(this, R.raw.enter_passwordv);
                    mp.start();
                }

                break;
            case 11:
                if (resultCode == RESULT_OK && data != null) {
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    String input = ((result.get(0)).replace(" ", "")).trim();

                    manualInput.putExtra("password", input);

                    startActivity(manualInput);
                }
                break;
        }

    }
    Button button;
    public void orType(View view)
    {
        Intent intent = new Intent(this, ManLoginActivity.class);
        startActivity(intent);
    }
}
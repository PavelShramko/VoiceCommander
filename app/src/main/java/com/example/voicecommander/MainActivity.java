package com.example.voicecommander;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.TargetApi;
import android.content.Intent;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognitionService;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private TextView textText;
    private ImageView inMain;private SoundPool sounds;
    private int sound_sirena;
    private TextToSpeech textToSpeech;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init(){
        textToSpeech = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR)
                {
                    textToSpeech.setLanguage(Locale.getDefault());
                }
            }
        });
        textText = findViewById(R.id.textTest);
        inMain = findViewById(R.id.imageView);
        createSoundPool();
        loadSounds();
    }

    public void onClickMic(View view){
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        startActivityForResult(intent, 10);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && data != null){
            switch (requestCode){
                case 10:
                    ArrayList<String> text = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    textText.setText(text.get(0));
                    textCommand(text.get(0));
                    break;
            }
        }
    }

    private void textCommand(String text) {
        switch (text){
            case "яблоко":
                inMain.setImageResource(R.drawable.manzana_lista);
            break;

            case "дыня":
                inMain.setImageResource(R.drawable.melon_listo);
                break;

            case "арбуз":
                inMain.setImageResource(R.drawable.sandia_listo);
                break;

            case "корова":
                inMain.setImageResource(R.drawable.vaca_lista);
                break;

            case "протокол самоуничтожения":
                protocolDist();
                break;

            case "Скажи что-нибудь":
                textToSpeech.speak("Привем Мир" ,
                        TextToSpeech.QUEUE_FLUSH, null);
                break;
            case "конец":
                textToSpeech.speak("Пока-пока",
                        TextToSpeech.QUEUE_FLUSH, null);
                break;
        }
    }

    private void protocolDist(){
        sounds.play(sound_sirena, 1.0f, 1.0f, 1, 0, 1);
        new Thread(new Runnable() {
            @Override
            public void run() {
                for(int i = 0; i < 5; i++){
                    try {
                        Thread.sleep(1000);
                    }catch (InterruptedException e){
                        e.printStackTrace();
                    }
                    final int finalI = i+1;
                    textText.post(new Runnable() {
                        @Override
                        public void run() {
                            textText.setText(String.valueOf(finalI));
                        }
                    });
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        inMain.setImageResource(R.drawable.calavera);
                    }
                });
            }
        }).start();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    protected void createNewSoundPool(){
        AudioAttributes attributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_GAME)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build();
        sounds = new SoundPool.Builder()
                .setAudioAttributes(attributes)
                .build();
    }

    @SuppressWarnings("deprecation")
    protected void createOldSoundPool(){

        sounds = new SoundPool(5, AudioManager.STREAM_MUSIC,0);
    }
    protected void createSoundPool() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            createNewSoundPool();
        } else {
            createOldSoundPool();
        }
    }
    private void loadSounds()
    {
        sound_sirena = sounds.load(this,R.raw.sirena, 1);
    }
}
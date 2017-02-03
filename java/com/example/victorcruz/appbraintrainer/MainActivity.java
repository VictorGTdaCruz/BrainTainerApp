package com.example.victorcruz.appbraintrainer;

import android.content.SharedPreferences;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.Button;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private Button startButton, optionsButton, optionsSpinnerSetTheme, backFromGameButton, playAgainButton, button1, button2, button3, button4;
    private TextView optionsTimerTextView, questionTextView, scoreTextView, messageTextView, timerTextView;
    private RelativeLayout mainRelativeLayout, gameRelativeLayout, optionsRelativeLayout;
    private SeekBar optionsTimerSeekBar;
    private CountDownTimer countDownTimer;
    private Spinner optionsThemeSpinner;

    private SharedPreferences sharedPreferences;

    private Random random;

    private int theme,
                spinnerSelectedTheme = 2,
                timerValue = 30000,
                rightAnswer,
                totalAnswer,
                correctAnswer,
                locationOfCorrectAnswer,
                b;

    private String[] arithmeticOp = {"+", "-"};

    private ArrayList<Integer> answers = new ArrayList<>();



    public void openOptions(View view){

        mainRelativeLayout.setVisibility(RelativeLayout.INVISIBLE);
        optionsRelativeLayout.setVisibility(RelativeLayout.VISIBLE);

    }

    public void backFromOptions(View view){

        optionsRelativeLayout.setVisibility(RelativeLayout.INVISIBLE);
        mainRelativeLayout.setVisibility(RelativeLayout.VISIBLE);

    }

    public void backFromGame(View view) {

        countDownTimer.cancel();
        gameRelativeLayout.setVisibility(RelativeLayout.INVISIBLE);
        mainRelativeLayout.setVisibility(RelativeLayout.VISIBLE);

    }

    public void start(View view){

        mainRelativeLayout.setVisibility(RelativeLayout.INVISIBLE);
        gameRelativeLayout.setVisibility(RelativeLayout.VISIBLE);

        reset(view);

    }

    public void reset(View view){

        messageTextView.setVisibility(TextView.INVISIBLE);
        playAgainButton.setVisibility(Button.INVISIBLE);

        rightAnswer = 0;
        totalAnswer = 0;
        refreshScore();
        setQuestion();
        setOptions();
        startTimer();

        button1.setClickable(true);
        button2.setClickable(true);
        button3.setClickable(true);
        button4.setClickable(true);

    }

    public void setQuestion(){

        int a = random.nextInt(1000);
            b = random.nextInt(1000);

        questionTextView.setText(Integer.toString(a) + " " + arithmeticOp[random.nextInt(2)] + " " + Integer.toString(b));

        if(Objects.equals(Integer.toString(a) + " " + arithmeticOp[0] + " " + Integer.toString(b), questionTextView.getText())){

            correctAnswer = a + b;

        } else{

            correctAnswer = a - b;

        }

    }

    public void setOptions(){

        locationOfCorrectAnswer = random.nextInt(4);//selects a tag to place the correct answer

        int i;
        for(i = 0; i < 4; i++) {

            if(i == locationOfCorrectAnswer){//when one of the four tags (the correct one) happens, put the correct answer there

                answers.add(i, correctAnswer);

            } else{//place 3 other random answers and put them in those 3 other buttons

                //the next 3 ifs just make the IA a little bit smarter selecting options "next" to the correct answer

                if(correctAnswer > 0 && correctAnswer <= 1000) b = random.nextInt(1000);
                else if(correctAnswer > 1000) b = random.nextInt(1000) + 1000;
                else if(correctAnswer < 0) b = random.nextInt(1000) - 1000;

                while (b == correctAnswer){

                    if(correctAnswer > 0 && correctAnswer <= 1000) b = random.nextInt(1000);
                    else if(correctAnswer >= 0 && correctAnswer > 1000) b = random.nextInt(1000) + 1000;
                    else if(correctAnswer < 0) b = random.nextInt(1000) - 1000;

                }

                answers.add(i, b);

            }

        }

        button1.setText(Integer.toString(answers.get(0)));
        button2.setText(Integer.toString(answers.get(1)));
        button3.setText(Integer.toString(answers.get(2)));
        button4.setText(Integer.toString(answers.get(3)));

    }

    public void startTimer(){
        countDownTimer = new CountDownTimer(timerValue + 100, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                timerTextView.setText(String.valueOf(millisUntilFinished / 1000) + "s");
            }

            @Override
            public void onFinish() {
                timerTextView.setText("0s");
                messageTextView.setText("Your score: " + Integer.toString(rightAnswer) + "/" + Integer.toString(totalAnswer));
                messageTextView.setVisibility(TextView.VISIBLE);
                playAgainButton.setVisibility(Button.VISIBLE);
                button1.setClickable(false);
                button2.setClickable(false);
                button3.setClickable(false);
                button4.setClickable(false);
            }
        }.start();
    }

    public void checkAnswer(View view){

        if(view.getTag().toString().equals(Integer.toString(locationOfCorrectAnswer))){

            rightAnswer++;
            totalAnswer++;
            refreshScore();
            messageTextView.setText("Correct!");
            messageTextView.setVisibility(TextView.VISIBLE);
            setQuestion();
            setOptions();

        } else {

            totalAnswer++;
            refreshScore();
            messageTextView.setText("Wrong!");
            messageTextView.setVisibility(TextView.VISIBLE);
            setQuestion();
            setOptions();

        }

    }

    public void refreshScore(){

        scoreTextView.setText(rightAnswer + "/" + totalAnswer);

    }

    public void setThemeButton(View view){

        if (spinnerSelectedTheme != theme) {
            sharedPreferences.edit().putInt("theme", spinnerSelectedTheme).apply();
            recreate();
        } else Toast.makeText(this, "Você já está usando esse tema!", Toast.LENGTH_SHORT).show();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedPreferences = this.getSharedPreferences("com.example.victorcruz.appbraintrainer", MODE_PRIVATE);
        theme = sharedPreferences.getInt("theme", 1);

        /*
            1 = Padrão
            2 = personalizado
            3 = cru
         */

        if (theme == 2 || theme == 3) setTheme(android.R.style.Theme_DeviceDefault_Light_DarkActionBar);

        setContentView(R.layout.activity_main);

        mainRelativeLayout = (RelativeLayout) findViewById(R.id.mainRelativeLayout);
        startButton = (Button) findViewById(R.id.startButton);
        //optionsButton = (Button) findViewById(R.id.optionsButton);
        optionsRelativeLayout = (RelativeLayout) findViewById(R.id.optionsRelativeLayout);
        optionsTimerTextView = (TextView) findViewById(R.id.optionsTimerTextView);
        optionsTimerSeekBar = (SeekBar) findViewById(R.id.optionsTimerSeekBar);
        optionsThemeSpinner = (Spinner) findViewById(R.id.optionsThemeSpinner);
        //optionsSpinnerSetTheme = (Button) findViewById(R.id.optionsSpinnerSetTheme);
        gameRelativeLayout = (RelativeLayout) findViewById(R.id.gameUI);
        timerTextView = (TextView) findViewById(R.id.timerTextView);
        questionTextView = (TextView) findViewById(R.id.question);
        scoreTextView = (TextView) findViewById(R.id.score);
        messageTextView = (TextView) findViewById(R.id.message);
        button1 = (Button) findViewById(R.id.button);
        button2 = (Button) findViewById(R.id.button2);
        button3 = (Button) findViewById(R.id.button3);
        button4 = (Button) findViewById(R.id.button4);
        if (theme == 2) {

            startButton.setBackgroundColor(startButton.getContext().getResources().getColor(android.R.color.holo_green_light, null));
            button1.setBackgroundColor(button1.getResources().getColor(android.R.color.holo_blue_bright, null));
            button2.setBackgroundColor(button2.getResources().getColor(R.color.colorAccent, null));
            button3.setBackgroundColor(button3.getResources().getColor(android.R.color.holo_green_light, null));
            button4.setBackgroundColor(button4.getResources().getColor(R.color.colorPrimary, null));
            scoreTextView.setBackgroundColor(scoreTextView.getResources().getColor(android.R.color.holo_orange_dark, null));

        }
        playAgainButton = (Button) findViewById(R.id.playAgainButton);
        //backFromGameButton = (Button) findViewById(R.id.backFromGameButton);
        random = new Random();


        optionsThemeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spinnerSelectedTheme = position + 1;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        optionsTimerSeekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progress = progress + 10;
                timerValue = progress * 1000;
                if (seekBar == optionsTimerSeekBar) optionsTimerTextView.setText("Set timer: " + progress + "s/90s");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }
/*
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        spinnerSelectedTheme = Integer.parseInt(parent.getItemAtPosition(position).toString());
        Toast.makeText(parent.getContext(), "OnItemSelectedListener: " + parent.getItemAtPosition(position).toString(), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }*/
}
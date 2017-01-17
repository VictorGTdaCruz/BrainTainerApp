package com.example.victorcruz.appbraintrainer;

import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private Button startButton, playAgainButton, button1, button2, button3, button4;
    private TextView questionTextView, scoreTextView, messageTextView, timerTextView;
    private RelativeLayout gameRelativeLayout;

    private Random random;

    private int rightAnswer,
                totalAnswer,
                correctAnswer,
                locationOfCorrectAnswer,
                b;

    private String[] arithmeticOp = {"+", "-"};

    private ArrayList<Integer> answers = new ArrayList<>();





    public void start(View view){

        startButton.setVisibility(View.INVISIBLE);
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
        new CountDownTimer(30100, 1000) {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startButton = (Button) findViewById(R.id.startButton);
        gameRelativeLayout = (RelativeLayout) findViewById(R.id.gameUI);
        timerTextView = (TextView) findViewById(R.id.timerTextView);
        questionTextView = (TextView) findViewById(R.id.question);
        scoreTextView = (TextView) findViewById(R.id.score);
        messageTextView = (TextView) findViewById(R.id.message);
        button1 = (Button) findViewById(R.id.button);
        button2 = (Button) findViewById(R.id.button2);
        button3 = (Button) findViewById(R.id.button3);
        button4 = (Button) findViewById(R.id.button4);
        playAgainButton = (Button) findViewById(R.id.playAgainButton);
        random = new Random();

    }
}
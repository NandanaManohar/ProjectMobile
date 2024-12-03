package com.example.memorygame;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.gridlayout.widget.GridLayout;

import java.util.ArrayList;
import java.util.Collections;

public class

MainActivity extends AppCompatActivity implements View.OnClickListener {


    private final int[] drawables = {
      R.drawable.car1, R.drawable.car2, R.drawable.car3, R.drawable.car4,
        R.drawable.car5, R.drawable.car6, R.drawable.car7, R.drawable.car8,
        R.drawable.car9, R.drawable.car10
    };
    private Drawable placeholderImage;

    private int player1Score = 0;
    private int player2Score = 0;
    private boolean isPlayer1Turn = true;

    private boolean isPocessingInput = false;

    int[] shuffledNumbers = new int[20];
    ImageButton[] buttons = new ImageButton[20];
    ImageButton lastClickedButton = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // loads placeholder image
        placeholderImage = ContextCompat.getDrawable(this,R.drawable.placeholder);

        // Gets the GridLayout from the xml and sets the rows and column count
        GridLayout grid = findViewById(R.id.gridLayout);
        grid.setRowCount(5);
        grid.setColumnCount(4);

        ArrayList<Integer> numbers = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            numbers.add(i);
            numbers.add(i);
        }
        Collections.shuffle(numbers);

        for (int i = 0; i < 20; i++) {
            shuffledNumbers[i] = numbers.get(i);

            ImageButton imageButton = new ImageButton(this);
            imageButton.setImageDrawable(placeholderImage);
            imageButton.setOnClickListener(this);

            grid.addView(imageButton);

            buttons[i] = imageButton;
        }


    }

    @Override
    public void onClick(View v) {

        if(isPocessingInput)
            return;

        ImageButton clickedButton = (ImageButton) v;

        if(clickedButton.getDrawable() != placeholderImage)
            return;

        clickedButton.setImageResource(drawables[getButtonShuffledNumber(clickedButton)]);


        if(lastClickedButton == null){
            lastClickedButton = clickedButton;
        }
        else {

            if(clickedButton.equals(lastClickedButton))
                return;

            if(getButtonShuffledNumber(clickedButton) == getButtonShuffledNumber(lastClickedButton)){

                if(isPlayer1Turn)
                    player1Score ++;
                else
                    player2Score ++;

                if(player1Score + player2Score == 10){
                    isPocessingInput = true;
                    TextView winText = findViewById(R.id.turnTextView);
                    if(player1Score == player2Score)
                        winText.setText("Draw!");
                    else if(player1Score > player2Score)
                        winText.setText("Player 1 Wins!");
                    else
                        winText.setText("Player 2 Wins!");

                    return;
                }

                lastClickedButton = null;

                updateLayout();
            }
            else {
                isPocessingInput = true;
                Handler handler = new Handler();
                handler.postDelayed(() ->
                {
                    lastClickedButton.setImageDrawable(placeholderImage);
                    clickedButton.setImageDrawable(placeholderImage);

                    isPlayer1Turn = !isPlayer1Turn;
                    lastClickedButton = null;

                    isPocessingInput = false;

                    updateLayout();
                },1000);

            }

        }

    }

    private int getButtonShuffledNumber(ImageButton button){
        for (int i = 0; i < buttons.length; i++) {
            if(buttons[i].equals(button)){
                return shuffledNumbers[i];
            }
        }
        return 0;
    }

    private void updateLayout(){
        ((TextView)findViewById(R.id.turnTextView)).setText((isPlayer1Turn ? "Player 1" : "Player 2") + "'s Turn");
        ((TextView)findViewById(R.id.player1ScoreTextView)).setText("Player 1: "+player1Score);
        ((TextView)findViewById(R.id.player2ScoreTextView)).setText("Player 2: "+player2Score);
    }
}
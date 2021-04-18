package com.zybooks.lightsout;

import android.content.Intent;
import android.os.Bundle;
import android.os.Debug;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class MainActivity extends AppCompatActivity {

    private final String GAME_STATE = "gameState";
    private final int REQUEST_CODE_COLOR = 0;
    private LightsOutGame mGame;
    private Button[][] mButtons;
    private int mOnColor;
    private int mOffColor;
    private int mCheat;
    private int mOnColorId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mOnColor = ContextCompat.getColor(this, R.color.yellow);
        mOffColor = ContextCompat.getColor(this, R.color.black);

        mButtons = new Button[LightsOutGame.NUM_ROWS][LightsOutGame.NUM_COLS];

        GridLayout gridLayout = findViewById(R.id.light_grid);
        int childIndex = 0;
        for (int row = 0; row < LightsOutGame.NUM_ROWS; row++) {
            for (int col = 0; col < LightsOutGame.NUM_COLS; col++) {
                mButtons[row][col] = (Button) gridLayout.getChildAt(childIndex);
                childIndex++;
            }
        }

        mOnColorId = R.color.yellow;
        mCheat = 0;
        mGame = new LightsOutGame();
        if (savedInstanceState == null) {
            startGame();
        } else {
            String gameState = savedInstanceState.getString(GAME_STATE);
            mGame.setState(gameState);
            setButtonColors();
        }
    }

    private void startGame() {
        mGame.newGame();
        setButtonColors();
        Toast.makeText(this, R.string.congrats, Toast.LENGTH_SHORT).show();
    }

    public void onHelpClick(View view) {
        Intent intent = new Intent(this, HelpActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(GAME_STATE, mGame.getState());
    }

    public void onLightButtonClick(View view) {
        // Find the row and col selected
        boolean buttonFound = false;
        for (int row = 0; row < LightsOutGame.NUM_ROWS && !buttonFound; row++) {
            for (int col = 0; col < LightsOutGame.NUM_COLS && !buttonFound; col++) {
                if (view == mButtons[row][col]) {
                    mGame.selectLight(row, col);
                    buttonFound = true;
                    // Increment mCheat if button is (0,0). Else, reset mCheat.
                    if (row == 0 && col == 0) {
                        mCheat++;
                    } else {
                        mCheat = 0;
                    }
                }
            }
        }

        setButtonColors();
        // Check if mCheat is 5. If so, turn the lights off.
        if (mCheat == 5) {
            mGame.allLightsOff();
        }

        // Congratulate the user if the game is over
        if (mGame.isGameOver()) {
            Toast.makeText(this, R.string.congrats, Toast.LENGTH_SHORT).show();
        }
    }

    private void setButtonColors() {

        // Set all buttons' background color
        for (int row = 0; row < LightsOutGame.NUM_ROWS; row++) {
            for (int col = 0; col < LightsOutGame.NUM_COLS; col++) {
                if (mGame.isLightOn(row, col)) {
                    mButtons[row][col].setBackgroundColor(mOnColor);
                } else {
                    mButtons[row][col].setBackgroundColor(mOffColor);
                }
            }
        }
    }

    public void onChangeColorClick(View view) {
        Intent intent = new Intent(this, ColorActivity.class);
        startActivityForResult(intent, REQUEST_CODE_COLOR);
        intent.putExtra(ColorActivity.EXTRA_COLOR, mOnColorId);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE_COLOR) {
            int colorId = data.getIntExtra(ColorActivity.EXTRA_COLOR, R.color.yellow);
            mOnColor = ContextCompat.getColor(this, colorId);
            mOnColorId = data.getIntExtra(ColorActivity.EXTRA_COLOR, R.color.yellow);
            mOnColor = ContextCompat.getColor(this, mOnColorId);
            setButtonColors();
        }
    }

    public void onNewGameClick(View view) {
        startGame();
    }
}
package edu.msu.singhk12.steampunked;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;

public class GameActivity extends AppCompatActivity {
    /**
     * The name for player1
     */
    private String PlayerOneName;
    /**
     * The name for player2
     */
    private String PlayerTwoName;
    /**
     * The text for the timer
     */
    private TextView timerText;
    /**
     * The text for the CountDownTimer
     */
    private CountDownTimer timerTime;
    /**
     * How long each turn is
     */
    private long millisecondTimeLeft = 30000; // 30 sec (31 sec to make it look nicer)

    /**
     * What happens when the game/Activity is created
     * @param savedInstanceState the bundle of the game
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        PlayerOneName = getIntent().getStringExtra(MainActivity.player1);
        PlayerTwoName = getIntent().getStringExtra(MainActivity.player2);
        int size = getIntent().getIntExtra(MainActivity.gameSize,0);
        getGameView().setPlayer1(PlayerOneName);
        getGameView().setPlayer2(PlayerTwoName);
        getGameView().setSize(size);

        // Timer setting
        timerText = findViewById(R.id.timer_text);
        timerSwitch();

        if(savedInstanceState != null) {
            // We have saved state

            getGameView().loadInstanceState(savedInstanceState);
            playerTurn = getGameView().getTurn();
        }

        turnInfo();
    }

    /**
     * Gets the gameview
     * @return the gameview
     */
    private GameView getGameView() {
        return (GameView) findViewById(R.id.gameView);
    }

    /**
     * Gets the TextView
     * @return the TextView
     */
    private TextView getTurnText() {
        return (TextView) findViewById(R.id.turnInfo);
    }
    /**
     * Gets the ClockView
     * @return the ClockView
     */
    private ClockView getClockView() {return (ClockView) findViewById(R.id.clockView); }

    /**
     * The current turn for the player
     */
    private Integer playerTurn = 1;

    /**
     * Sets the info for the turn
     */
    public void turnInfo() {
        TextView turnInfo = getTurnText();
        String turn = "";
        if (playerTurn == 1)
            turn = PlayerOneName + "'s Turn";
        else
            turn = PlayerTwoName + "'s Turn";
        turnInfo.setText(turn);
    }
    /**
     * Changes the turn
     */
    public void changeTurn() {
        this.playerTurn *= -1;
        turnInfo();
        getGameView().changeTurn();
        getClockView().reset();

        // Stop old timer, reset to 30 sec (31 sec to make it look nicer)
        millisecondTimeLeft = 31000;
        timerTime.cancel();
        timerSwitch();
    }

    /**
     * Function to send the game to the final end game screen.
     */
    public void onGameWin(View view) {
        Intent intent = new Intent(this, EndActivity.class);

        intent.putExtra("PlayerOneName1", PlayerOneName);
        intent.putExtra("PlayerTwoName1", PlayerTwoName);
        intent.putExtra("playerTurn",playerTurn);

        startActivity(intent);
    }

    /**
     * Function to surrender the game when the surrender button is pressed
     * @param view the endview
     */
    public void onGameSurrender(View view){
        changeTurn();
        onGameWin(view);
    }

    /**
     * Function to start the timer as well as switch it on player turn/no time left
     */
    public void timerSwitch() {
        timerTime = new CountDownTimer(millisecondTimeLeft, 1000) {
            @Override
            public void onTick(long l) {
                millisecondTimeLeft = l;
                updateTimer();
            }

            @Override
            public void onFinish() {
                changeTurn();

                millisecondTimeLeft = 31000;
                timerTime.cancel();
                timerSwitch();
            }
        }.start();
    }

    /**
     * Function to update the timer text to tick down
     */
    public void updateTimer() {
        int minutes = (int) millisecondTimeLeft / 60000;
        int seconds = (int) millisecondTimeLeft % 60000 / 1000;

        String timeLeftText;

        timeLeftText = "" + minutes;
        timeLeftText += ":";
        if (seconds < 10) timeLeftText += "0";
        timeLeftText += seconds;

        getClockView().increaseAngle();
        timerText.setText(timeLeftText);
    }

    /**
     * Function to install the current pipe in play.
     * Checks if current pipe and pipeline belong to same player.
     * Checks if current pipe and pipeline pipe has same open side.
     * @param view the playing area view
     */
    public void Install(View view) {
        Pipe currentPipe = getGameView().getGame().GetLastDraggedPipe(); //Current pipe in playingArea

        if (currentPipe!= null && currentPipe.GetSnapped()) {
//            if (getGameView().getGame().getPipe((int) currentPipe.getX(), (int) currentPipe.getY()) == null)
            getGameView().getGame().add(currentPipe, (int) currentPipe.getX(), (int) currentPipe.getY());
//            else {
//                Pipe existPipe = getGameView().getGame().getPipe((int) currentPipe.getX(), (int) currentPipe.getY());
//                getGameView().getGame().getPipe()
//            }
            getGameView().getGame().shuffle();
            currentPipe.SetInstalled();
            changeTurn();
        } else {
            // Instantiate a dialog box builder
            AlertDialog.Builder builder =
                    new AlertDialog.Builder(view.getContext());

            // Parameterize the builder
            //builder.setTitle(R.string.hurrah);
            builder.setMessage(R.string.PipeError);

            // Create the dialog box and show it
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }
    }
    /**
     * Handle a Discard button press
     * @param view the playing area view
     */
    public void onDiscard(View view) {
        getGameView().getGame().discard();
        changeTurn();
        getGameView().invalidate();
    }

    public void onOpenValve(View view) {
        if (playerTurn == 1){
            getGameView().getGame().setClose1(false);
            if (!getGameView().getGame().search(getGameView().getGame().getStartPipe1()) || !getGameView().getGame().getEndPipe1().beenVisited()) {
                changeTurn();
            }
        }
        else if (playerTurn == -1){
            getGameView().getGame().setClose2(false);
            if (!getGameView().getGame().search(getGameView().getGame().getStartPipe2()) || !getGameView().getGame().getEndPipe2().beenVisited()) {
                changeTurn();
            }
        }

        // Thread.sleep alternative to keep timer running and not freeze program
        // https://stackoverflow.com/questions/21680608/alternative-to-thread-sleep-method-in-android
        new Handler().postDelayed(new Runnable() {
            public void run () {
                onGameWin(view);
            }
        }, 2000L);
    }

    /**
     * Save the instance state into a bundle
     * @param bundle the bundle to save into
     */
    @Override
    protected void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);

        getGameView().saveInstanceState(bundle);
    }

    @Override
    public void onBackPressed() {
        // Disables back button
    }
}



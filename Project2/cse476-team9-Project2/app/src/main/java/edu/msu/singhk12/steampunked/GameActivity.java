package edu.msu.singhk12.steampunked;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import edu.msu.singhk12.steampunked.cloud.Cloud;

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
    private int inactiveCount = 0;
    /**
     * How long each turn is
     */
    private long millisecondTimeLeft = 30000; // 30 sec (31 sec to make it look nicer)

    private final static String USER= "username";
    private final static String PASSWORD = "password";
    public static final String THISPLAYER = "thisplayer";

    String user;
    String password;
    String thisplayerturn;

    int currentTurn = 1;


    /**
     * What happens when the game/Activity is created
     * @param savedInstanceState the bundle of the game
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        PlayerOneName = getIntent().getStringExtra(WaitingActivity.player1);
        PlayerTwoName = getIntent().getStringExtra(WaitingActivity.player2);
        int size = getIntent().getIntExtra(WaitingActivity.gameSize,0);

        user = getIntent().getStringExtra(WaitingActivity.USER);
        password = getIntent().getStringExtra(PASSWORD);
        thisplayerturn = getIntent().getStringExtra(WaitingActivity.THISPLAYER);

        getGameView().setPlayer1(PlayerOneName);
        getGameView().setPlayer2(PlayerTwoName);
        getGameView().setSize(size);

        if (thisplayerturn.equals("1"))
            getGameView().setPlayer(1);
        else
            getGameView().setPlayer(-1);

        // Timer setting
        timerText = findViewById(R.id.timer_text);
        timerSwitch();

        if(savedInstanceState != null) {
            // We have saved state

            getGameView().loadInstanceState(savedInstanceState);
            playerTurn = getGameView().getTurn();
        }

        getGameView().setUser(user);
        getGameView().setPassword(password);

//        if ((playerTurn == 1 && thisplayerturn.equals("1")) || (playerTurn == -1 && thisplayerturn.equals("2"))){
//            checkForInactivity();
//        }

        checkForTurn();
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
    int playerTurn = 1;

    private void checkForTurn() {
        final Timer timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                final Cloud cloud = new Cloud();
                String ok = cloud.getTurnInfo(user, password);
                Log.i("current turn", ok);


                if (ok.equals("1")) {
                    playerTurn = 1;
                    currentTurn = playerTurn;
                } else {
                    playerTurn = -1;
                    currentTurn = playerTurn;
                }

                if (thisplayerturn.equals(Integer.toString(currentTurn)) ){
//                    timerTime.cancel();
//                    millisecondTimeLeft = 31000;
//                    timerSwitch();
                }

                turnInfo();
                LoadPipe();
            }
        };
        timer.schedule(timerTask, 0, 500);
    }

    private void checkForInactivity() {
        final Timer timer2 = new Timer();
        TimerTask timerTask2 = new TimerTask() {
            @Override
            public void run() {
//                inactiveCount ++;
//                if(inactiveCount == 3){
//                    onInactive();
//                }
            }
        };
        timer2.schedule(timerTask2, 0, 10000);

    }

    private void onInactive(){
        //getGameView().changeTurn();
        Intent intent = new Intent(this, EndActivity.class);

        intent.putExtra("PlayerOneName1", PlayerTwoName);
        intent.putExtra("PlayerTwoName1", PlayerOneName);
        intent.putExtra("playerTurn",playerTurn);

        startActivity(intent);
    }

    private void changeTurn() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                final Cloud cloud = new Cloud();
                String ok = cloud.changeTurn(user, password);
                if (ok.equals("yes"))
                {
                    Log.i("sssssss", "turn changed");
                }
            }
        }).start();
        getGameView().changeTurn();
        getClockView().reset();

        inactiveCount = 0;
        timerTime.cancel();
        millisecondTimeLeft = 31000;
        timerSwitch();
    }

    private void LoadPipe() {
        getGameView().loadPipe();
    }

    /**
     * Sets the info for the turn
     */
    public void turnInfo() {
        TextView turntext = getTurnText();
        String turn = "";
        if (playerTurn == 1)
            turn = PlayerOneName + "'s Turn";
        else
            turn = PlayerTwoName + "'s Turn";
        turntext.setText(turn);
    }
//    /**
//     * Changes the turn
//     */
//    public void changeTurn() {
//        this.playerTurn *= -1;
//        turnInfo();
//        getGameView().changeTurn();
//        getClockView().reset();
//        loadGridPipe();
//        // Stop old timer, reset to 30 sec (31 sec to make it look nicer)
//        millisecondTimeLeft = 31000;
//        timerTime.cancel();
//        timerSwitch();
//    }

    /**
     * Function to send the game to the final end game screen.
     */
    public void onGameWin(View view) {
        Intent intent = new Intent(this, EndActivity.class);

        intent.putExtra("PlayerOneName1", PlayerOneName);
        intent.putExtra("PlayerTwoName1", PlayerTwoName);
        intent.putExtra("playerTurn",playerTurn);

        intent.putExtra(USER, user);
        intent.putExtra(PASSWORD, password);

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
                if ((int) millisecondTimeLeft % 60000 / 1000 == 15){
                    getGameView().post(new Runnable(){
                        @Override
                        public void run() {
                            Toast.makeText(getGameView().getContext(),
                                    R.string.timeoutWarning,
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                if ((int) millisecondTimeLeft % 60000 / 1000 == 10){
                    onInactive();
                }
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

        if ((playerTurn == 1 && thisplayerturn.equals("1")) || (playerTurn == -1 && thisplayerturn.equals("2"))) {
            if (!findViewById(R.id.installButton).isEnabled()){
                timerTime.cancel();
                millisecondTimeLeft = 31000;
                timerSwitch();
                getClockView().reset();

                findViewById(R.id.installButton).setEnabled(true);
                findViewById(R.id.discardButton).setEnabled(true);
                findViewById(R.id.buttonOpenValve).setEnabled(true);
            }
        }
        else {
            if (findViewById(R.id.installButton).isEnabled()){
                findViewById(R.id.installButton).setEnabled(false);
                findViewById(R.id.discardButton).setEnabled(false);
                findViewById(R.id.buttonOpenValve).setEnabled(false);
            }
        }
    }

    /**
     * Function to install the current pipe in play.
     * Checks if current pipe and pipeline belong to same player.
     * Checks if current pipe and pipeline pipe has same open side.
     * @param view the playing area view
     */
    public void Install(View view) throws IOException {
        if ((playerTurn == 1 && thisplayerturn.equals("1")) || (playerTurn == -1 && thisplayerturn.equals("2")))
        {
            Pipe currentPipe = getGameView().getGame().GetLastDraggedPipe(); //Current pipe in playingArea

            if (currentPipe != null && currentPipe.GetSnapped()) {
//                //            if (getGameView().getGame().getPipe((int) currentPipe.getX(), (int) currentPipe.getY()) == null)
                getGameView().getGame().add(currentPipe, (int) currentPipe.getX(), (int) currentPipe.getY());
//                //            else {
//                //                Pipe existPipe = getGameView().getGame().getPipe((int) currentPipe.getX(), (int) currentPipe.getY());
//                //                getGameView().getGame().getPipe()
//                //            }
                getGameView().getGame().shuffle();
                currentPipe.SetInstalled();
//                saveGridPipe((int) currentPipe.getX(), (int) currentPipe.getY(), currentPipe.getId());
//                changeTurn();
                getGameView().savePipeAreaXml();
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
//    private void loadGridPipe(){
//        final GameActivity activity = GameActivity.this;
//        final GameView view = (GameView) activity.findViewById(R.id.gameView);
//
//        /*
//         * Create a thread to load the hatting from the cloud
//         */
//        new Thread(new Runnable() {
//
//            @Override
//            public void run() {
//                // Create a cloud object and get the XML
//                Cloud cloud = new Cloud();
//                PipeCloud[] pipe = cloud.LoadPipeFromCloud();
//
//                // hat loaded successfully
//                if(pipe != null) {
//                    view.loadPipe(pipe);
//                    return;
//                }else{
//                    view.post(new Runnable() {
//                        @Override
//                        public void run() {
//                            Toast.makeText(activity, R.string.load_fail, Toast.LENGTH_SHORT).show();
//                        }
//                    });
//                }
//            }
//        }).start();
//
//    }

    /**
     //     * Actually save the PipeArea
     //     * @param x
     //     * @param y
     //     * @param id
     //     */
//    private void saveGridPipe(final int x, final int y, final int id) {
//
//        final GameActivity activity = GameActivity.this;
//        final GameView view = (GameView) activity.findViewById(R.id.gameView);
//        final Cloud cloud = new Cloud();
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                //Cloud cloud = new Cloud();
//                boolean ok;
//                try {
//                    ok = cloud.saveGridPipeToCloud(view, x, y, id);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                    ok = false;
//                }
//                if(!ok) {
//                    /*
//                     * If we fail to save, display a toast
//                     */
//                    view.post(new Runnable() {
//                        @Override
//                        public void run() {
//                            Toast.makeText(activity, R.string.save_fail, Toast.LENGTH_SHORT).show();
//                        }
//                    });
//
//
//                }
//
//            }
//        }).start();
//
//    }
}


// Add save() function like the one in SaveDlg() fromstep 5/6. should be called on install
//small change somewhere
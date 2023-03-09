package edu.msu.singhk12.steampunked;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

import edu.msu.singhk12.steampunked.cloud.Cloud;

public class WaitingActivity extends AppCompatActivity {

    public final static String USER= "username";
    private final static String PASSWORD = "password";

    public static final String player1 = "Player1";
    public static final String player2 = "Player2";
    public static final String gameSize = "gameSize";
    public static final String THISPLAYER = "thisplayer";

    String user;
    String password;
    String p1name;
    String p2name;
    String size;
    String thisplayerturn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiting);

        user = getIntent().getStringExtra(USER);
        password = getIntent().getStringExtra(PASSWORD);
        thisplayerturn = getIntent().getStringExtra(THISPLAYER);

        checkForPlayerTwo();
    }

    private void startGame(){
        Intent intent = new Intent(this, GameActivity.class);

        intent.putExtra(USER, user);
        intent.putExtra(PASSWORD, password);
        intent.putExtra(player1, p1name);
        intent.putExtra(player2, p2name);
        intent.putExtra(gameSize, Integer.valueOf(size));
        intent.putExtra(THISPLAYER, thisplayerturn);

        startActivity(intent);
    }

    private void checkForPlayerTwo(){
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                final Cloud cloud = new Cloud();
//                String ok = cloud.checkRoom(user, password).getStatus();
//                if (!ok.equals("yes")){
//                    try {
//                        Thread.sleep(1000);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                    checkForPlayerTwo();
//                }
//                else{
//                    startGame();
//                }
//            }
//        }).start();
        final Timer timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                final Cloud cloud = new Cloud();
                String ok = cloud.checkRoom(user, password).getStatus();
                Log.i("aaaaaaaaa", ok);
                if (ok.equals("yes"))
                {
                    Log.i("????", "??????????");
                    //stop timer
                    timer.cancel();
                    timer.purge();
                    getRoomInfo();
                }
                else
                {

                }
            }
        };
        timer.schedule(timerTask, 0,1000);
    }

    public void getRoomInfo()
    {
        new Thread(new Runnable() {
            @Override
            public void run() {
                final Cloud cloud = new Cloud();
                p1name = cloud.getRoomInfo(user, password, "1");
                p2name = cloud.getRoomInfo(user, password, "2");
                size = cloud.getRoomInfo(user, password, "3");
                startGame();
            }
        }).start();
    }
}
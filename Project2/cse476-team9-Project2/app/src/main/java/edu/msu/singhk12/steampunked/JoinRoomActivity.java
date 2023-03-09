package edu.msu.singhk12.steampunked;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import edu.msu.singhk12.steampunked.cloud.Cloud;

public class JoinRoomActivity extends AppCompatActivity {
    public static final String player = "player";
    public final static String USER= "username";
    private final static String PASSWORD = "password";
    public static final String THISPLAYER = "thisplayer";

    String user;
    String roomName;
    String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_room);

        user = getIntent().getExtras().getString(player);
        password = getIntent().getStringExtra(PASSWORD);
    }

    public void onJoinButton(View view) {
        roomName = ((TextView)findViewById(R.id.roomID)).getText().toString();
        new Thread(new Runnable() {
            @Override
            public void run() {
                final Cloud cloud = new Cloud();
                final boolean ok = cloud.joinRoom(user, roomName, password);
                if(!ok)
                {
                    view.post(new Runnable(){
                        @Override
                        public void run() {
                            Toast.makeText(view.getContext(),
                                    R.string.join,
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                else
                {
                    enterWaiting();
                }
            }
        }).start();
    }

    private void enterWaiting() {
        Intent intent = new Intent(this, WaitingActivity.class);
        intent.putExtra(USER,user);
        intent.putExtra(PASSWORD, password);
        intent.putExtra(THISPLAYER, "2");
        startActivity(intent);
    }
}

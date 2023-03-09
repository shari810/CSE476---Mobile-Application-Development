package edu.msu.singhk12.steampunked;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import edu.msu.singhk12.steampunked.cloud.Cloud;
import edu.msu.singhk12.steampunked.cloud.model.User;

public class lobbyActivity extends AppCompatActivity {
    public static final String player = "player";
    private final static String PASSWORD = "password";

    String user;
    String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lobby);
        user = getIntent().getExtras().getString(player);
        password = getIntent().getExtras().getString(PASSWORD);
    }

    public void onCreateButton(View view) {
        Intent intent = new Intent(this, CreateRoomActivity.class);
        intent.putExtra(player,user);
        intent.putExtra(PASSWORD, password);
        startActivity(intent);
    }

    public void onJoinButton(View view) {
        Intent intent = new Intent(this, JoinRoomActivity.class);
        intent.putExtra(player,user);
        intent.putExtra(PASSWORD, password);
        startActivity(intent);
    }
}
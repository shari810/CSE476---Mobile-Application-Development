package edu.msu.singhk12.steampunked;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import org.w3c.dom.Text;

public class EndActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end);

        /**
         * Name for player1
         */
        String playerOneName = getIntent().getExtras().getString("PlayerOneName1");
        /**
         * Name for player2
         */
        String playerTwoName = getIntent().getExtras().getString("PlayerTwoName1");
        /**
         * The player turn
         */
        Integer playerTurn = getIntent().getExtras().getInt("playerTurn");

        TextView winText = (TextView)findViewById(R.id.winnerPlayer);

        /// Code for when Player class is used instead of just a static string for names in MainActivity
        /// Whomever's the final turn is, is the winner.
         if (playerTurn == 1)
         {
             String win = "Winner: " + playerOneName;
             winText.setText(win);
         }
         else{
             String win = "Winner: " + playerTwoName;
             winText.setText(win);
         }
    }

    @Override
    public void onBackPressed() {
        // Disables back button
    }

    public void restartGame(View view){
        finish();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
package edu.msu.singhk12.steampunked;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;


public class MainActivity extends AppCompatActivity {

    public static final String player1 = "Player1";
    public static final String player2 = "Player2";
    public static final String gameSize = "gameSize";

    public int size;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.gameSize, android.R.layout.simple_spinner_item);

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        getSpinner().setAdapter(adapter);

        getSpinner().setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View view,
                                       int pos, long id) {
                size = pos;
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
    }

    public void onStartPuzzle(View view) {
        Intent intent = new Intent(this, GameActivity.class);
        // pass in player names to GameActivity
        EditText playerOneText = (EditText)findViewById(R.id.PlayerOneName);
        EditText playerTwoText = (EditText)findViewById(R.id.PlayerTwoName);

        String playerNameOne = playerOneText.getText().toString();
        String playerNameTwo = playerTwoText.getText().toString();

        if (!playerNameOne.equals("") && !playerNameTwo.equals("")) {
            intent.putExtra(player1, playerOneText.getText().toString());
            intent.putExtra(player2, playerTwoText.getText().toString());
            intent.putExtra(gameSize, size);

            startActivity(intent);
        } else {
            // Instantiate a dialog box builder
            AlertDialog.Builder builder =
                    new AlertDialog.Builder(view.getContext());

            // Parameterize the builder
            //builder.setTitle(R.string.hurrah);
            builder.setMessage(R.string.playerNameError);

            // Create the dialog box and show it
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }

    }

    /**
     * The hat choice spinner
     */
    private Spinner getSpinner() {
        return (Spinner) findViewById(R.id.spinnerSize);
    }

    @Override
    public void onBackPressed() {
        // Disables back button
    }

    public void onHowToPlay(View view) {
        // Instantiate a dialog box builder
        AlertDialog.Builder builder =
                new AlertDialog.Builder(view.getContext());

        // Parameterize the builder
        //builder.setTitle(R.string.hurrah);
        // Parameterize the builder
        builder.setTitle(R.string.howToPlay);
        builder.setMessage(R.string.howToPlayMessage);
        builder.setPositiveButton(android.R.string.ok, null);

        // Create the dialog box and show it
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}

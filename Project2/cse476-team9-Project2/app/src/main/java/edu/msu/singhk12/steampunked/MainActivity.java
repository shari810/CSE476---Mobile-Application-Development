package edu.msu.singhk12.steampunked;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import edu.msu.singhk12.steampunked.cloud.Cloud;
import edu.msu.singhk12.steampunked.cloud.model.User;


public class MainActivity extends AppCompatActivity {

    public static final String player = "player";

    public int size;

    private String username = "";
    private String password = "";
    private boolean remember = false;

    public void onRemember(View view) {
        remember = !remember;
        saveState();
    }

    private SharedPreferences settings = null;

    private final static String USERNAME = "username";
    private final static String PASSWORD = "password";
    private final static String REMEMBER = "remember";

    private boolean loggedIn = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        settings = PreferenceManager.getDefaultSharedPreferences(this);
        username = settings.getString(USERNAME, "");
        password = settings.getString(PASSWORD, "");
        remember = settings.getBoolean(REMEMBER, false);
        setUI();
    }

    private void setUI() {
        EditText Textusername = (EditText) findViewById(R.id.username);
        EditText Textpassword = (EditText) findViewById(R.id.password);
        CheckBox rememberCheck = (CheckBox) findViewById(R.id.rememberMeCheckBox);
        if (remember){
            Textusername.setText(username);
            Textpassword.setText(password);
            rememberCheck.setChecked(remember);
        }
    }

    public void onStartPuzzle(View view) {
        if (!loggedIn) {
            Toast.makeText(view.getContext(), R.string.needLogin, Toast.LENGTH_SHORT).show();
        }
        else {
            enterLobby();
        }
    }

    public void onLogin(View view) {
        loggedIn = false;
        saveState();
        new Thread(new Runnable() {
            @Override
            public void run() {
                final Cloud cloud = new Cloud();
                final User result = cloud.login(((EditText) findViewById(R.id.username)).getText().toString(), ((EditText) findViewById(R.id.password)).getText().toString());
                Log.i("11111111", result.getMessage());
                if(result.getStatus().equals("no"))
                {
                    view.post(new Runnable(){
                        @Override
                        public void run() {
                            Toast.makeText(view.getContext(),
                                    R.string.loginerror,
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                else
                {
                    loggedIn = true;
                    view.post(new Runnable(){
                        @Override
                        public void run() {
                            Toast.makeText(view.getContext(),
                                    R.string.loginsuccess,
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        }).start();
    }

    private void enterLobby() {
        Intent intent = new Intent(this, lobbyActivity.class);
        intent.putExtra(player,username);
        intent.putExtra(PASSWORD, password);
        startActivity(intent);
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

    public void onRigisterNew(View view) {
        Intent intent = new Intent(this, registerActivity.class);
        startActivity(intent);
    }

    private void saveState() {
        SharedPreferences.Editor editor = settings.edit();

        editor.putString(USERNAME, ((EditText) findViewById(R.id.username)).getText().toString());
        editor.putString(PASSWORD, ((EditText) findViewById(R.id.password)).getText().toString());
        editor.putBoolean(REMEMBER, remember);
        editor.apply();
    }
}

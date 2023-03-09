package edu.msu.singhk12.steampunked;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import edu.msu.singhk12.steampunked.cloud.Cloud;

public class CreateRoomActivity extends AppCompatActivity {
    public static final String player = "player";
    public static final String THISPLAYER = "thisplayer";
    public final static String USER= "username";
    private final static String PASSWORD = "password";

    String user;
    int size = 0;
    String roomName;
    String password;

    /**
     * The hat choice spinner
     */
    private Spinner getSpinner() {
        return (Spinner) findViewById(R.id.spinnerSize);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_room);

        user = getIntent().getExtras().getString(player);
        password = getIntent().getStringExtra(PASSWORD);

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

    public void onCreateButton(View view) {
        roomName = ((TextView)findViewById(R.id.joinRoomName)).getText().toString();
        new Thread(new Runnable() {
            @Override
            public void run() {
                final Cloud cloud = new Cloud();
                //size = getIntent().getIntExtra(gameSize,0);
                boolean delete_ok = cloud.deleteRoom(user, password);
                if (!delete_ok)
                {
                    Log.i("delete room", "fails");
                }
                int displaySize = 0;
                if (size == 0) {
                    displaySize = 5;
                }
                if (size == 1) {
                    displaySize = 10;
                }
                if (size == 2) {
                    displaySize = 15;
                }

                final boolean ok = cloud.createRoom(user, String.valueOf(displaySize), roomName, password);
                if(!ok)
                {
                    view.post(new Runnable(){
                        @Override
                        public void run() {
                            Toast.makeText(view.getContext(),
                                    R.string.createR,
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
        intent.putExtra(THISPLAYER, "1");
        startActivity(intent);
    }
}
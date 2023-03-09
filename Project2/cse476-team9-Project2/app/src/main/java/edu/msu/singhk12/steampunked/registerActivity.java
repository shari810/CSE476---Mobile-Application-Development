package edu.msu.singhk12.steampunked;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import edu.msu.singhk12.steampunked.cloud.Cloud;
import edu.msu.singhk12.steampunked.cloud.model.User;

public class registerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
    }

    public TextView getUsername() {
        return (TextView) findViewById(R.id.usernameRegister);
    }
    public TextView getpassword1() {
        return (TextView) findViewById(R.id.textPassword1);
    }
    public TextView getpassword2() {
        return (TextView) findViewById(R.id.textPassword2);
    }

    public void onRegister(View view) {
        String username = getUsername().getText().toString();
        String password1 = getpassword1().getText().toString();
        String password2 = getpassword2().getText().toString();

        if (!password1.equals(password2)) {
            Toast.makeText(view.getContext(), R.string.passwordnotmatch, Toast.LENGTH_SHORT).show();
        }
        else {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    final Cloud cloud = new Cloud();
                    final boolean ok = cloud.registerNew(username, password1);
                    if (!ok) {
                        view.post(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(view.getContext(),
                                        R.string.registerError,
                                        Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                    else {
                        finish();
                    }
                }
            }).start();
        }
    }
}
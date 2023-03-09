package edu.msu.sharinic.step1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Button;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    public void onClick(View view) {

        Button button = (Button) findViewById(R.id.button);
        TextView greet = (TextView) findViewById(R.id.greeting);
        String btext= getResources().getString(R.string.button_text);

        if (button.getText() == btext)
        {
            button.setText(R.string.greeting);
        }
        else{
            button.setText(R.string.button_text);
        }

    }
}
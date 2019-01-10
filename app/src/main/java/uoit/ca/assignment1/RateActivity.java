package uoit.ca.assignment1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

public class RateActivity extends AppCompatActivity implements View.OnClickListener {
    String sessionId = "";
    SeekBar sk = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate);

        // get restaurant name passed from first activity
        sessionId = getIntent().getStringExtra("restaurant_name");

        // set title of activity
        setTitle(sessionId);

        // get back button
        final Button btn = findViewById(R.id.button);

        // get seek bar event listener and change text view text on seeker value
        sk = findViewById(R.id.seekBar);
        final TextView tv = findViewById(R.id.textView);

        // seek bar seek event listener
        sk.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,boolean fromUser) {
                // TODO Auto-generated method stub
                tv.setText(progress + "/5");
            }
        });

        // get button click event listener
        btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent myIntent = new Intent();
        myIntent.putExtra("restaurant_reviewed", sessionId);
        myIntent.putExtra("rating", sk.getProgress());
        setResult(RESULT_OK, myIntent);
        finish();
    }
}

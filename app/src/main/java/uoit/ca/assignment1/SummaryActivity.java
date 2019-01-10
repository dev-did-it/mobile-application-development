package uoit.ca.assignment1;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import java.util.HashMap;

public class SummaryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary);

        // set title
        setTitle("Ratings Summary");

        // get ratings hashmap from MainActivity
        HashMap<String,Integer> ratings = (HashMap<String, Integer>) getIntent().getSerializableExtra("all_ratings");

        // get text view objects
        TextView highRatedTextView = findViewById(R.id.textView2);
        TextView lowRatedTextView = findViewById(R.id.textView3);

        // iterate all ratings and write to text view
        for(HashMap.Entry<String, Integer> entry : ratings.entrySet()) {
            String name = entry.getKey();
            int value = entry.getValue();

            if (value > 3.5) {
                highRatedTextView.append(name + " - " + value + "\n");
            } else {
                lowRatedTextView.append(name + " - " + value + "\n");
            }
        }
    }
}

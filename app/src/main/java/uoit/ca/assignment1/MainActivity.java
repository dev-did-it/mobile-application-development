package uoit.ca.assignment1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    // array of restaurants
    String[] restaurants = new String[]{"McDonalds", "Burger King", "Swiss Chalet", "Chipotle", "KFC"};

    // hash map of restaurants reviewed by user already
    HashMap<String, Boolean> reviewedRestaurants = new HashMap();

    // hash map of all restaurant ratings
    HashMap<String, Integer> ratings = new HashMap();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // populate restaurants reviewed as false
        reviewedRestaurants.put("McDonalds", false);
        reviewedRestaurants.put("Burger King", false);
        reviewedRestaurants.put("Swiss Chalet", false);
        reviewedRestaurants.put("Chipotle", false);
        reviewedRestaurants.put("KFC", false );

        // set title of activity
        setTitle("Restaurant Reviews");

        Button rateButton = findViewById(R.id.rate_button);
        rateButton.setOnClickListener(MainActivity.this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(this, RateActivity.class);

        // check which restaurant hasn't been reviewed yet
        for(int i=0;i<restaurants.length;i++) {
            // get restaurant name
            String name = restaurants[i];

            // get user review status
            Boolean didReview = reviewedRestaurants.get(name);

            // see if user reviewed restaurant already
            if (!didReview) {
                intent.putExtra("restaurant_name", name);
                startActivityForResult(intent,1);
                break;
            }
        }

        // check if all restaurants have been rated yet
        for(HashMap.Entry<String, Boolean> entry : reviewedRestaurants.entrySet()) {
            boolean value = entry.getValue();

            if (!value) {
                // there exists a restaurant that wasn't rated so we return without going to summary activity
                return;
            }
        }
        // show summary activity
        Intent summaryIntent = new Intent(this, SummaryActivity.class);
        summaryIntent.putExtra("all_ratings", ratings);
        startActivity(summaryIntent);
    }

    // get ratings from next activity
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {
            if(resultCode == RESULT_OK) {
                // get restaurant rated
                String ratedRestaurant = data.getStringExtra("restaurant_reviewed");

                // get user rating
                int rating = (Integer) data.getIntExtra("rating", 0);

                // mark restaurant as rated
                reviewedRestaurants.put(ratedRestaurant, true);

                // add rating to hashmap
                ratings.put(ratedRestaurant, rating);
            }
        }
    }
}

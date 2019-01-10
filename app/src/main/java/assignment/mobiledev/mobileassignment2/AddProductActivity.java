/**
 * Author: Devante Wilson - 100554361
 *
 * Class handles invoking the database function to
 * save or insert values into the database, clearing text fields,
 * and stopping the activity (returning to BrowseProductsActivity)
 */

package assignment.mobiledev.mobileassignment2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class AddProductActivity extends AppCompatActivity
{
    // define instance variables
    EditText productId, name, description, price;
    ProductDBHelper db;

    // create a new activity view
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_product_activity);

        productId = findViewById(R.id.product_id_val);
        name = findViewById(R.id.product_name_val);
        description = findViewById(R.id.description_val);
        price = findViewById(R.id.price_cad_val);
        db = new ProductDBHelper(this);
    }

    // receive the values from the view and add them into the database once save button is clicked
    public void saveValues(View view)
    {
        // define local variables
        int productIdInteger = Integer.parseInt(productId.getText().toString());
        float priceVal = Float.parseFloat(price.getText().toString());
        Product product = new Product(productIdInteger,name.getText().toString(),description.getText().toString(),priceVal);
        Boolean state = db.addProduct(product);

        if(state)
        {
            resetValues();
            finish();
        }
        else
        {
            Toast.makeText(this, "An error occured when trying to add a new product.\n",
                    Toast.LENGTH_LONG).show();
        }

    }

    public void cancelValues(View view)
    {
        // reset values when cancel is clicked
        resetValues();
        // return back to caller activity
        finish();
    }

    // reset all the views on add product activity
    public void resetValues()
    {
        productId.setText("");
        name.setText("");
        description.setText("");
        price.setText("");
    }
}


/**
 * Author: Devante Wilson - 100554361
 *
 * Class allows the user to input items into the database
 * and to display the available products along with their
 * price in Bitcoin.
 */

package assignment.mobiledev.mobileassignment2;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import org.w3c.dom.Text;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.concurrent.ExecutionException;

import javax.net.ssl.HttpsURLConnection;

public class BrowseProductsActivity extends AppCompatActivity
{
    // define instance variables
    ProductDBHelper db;
    Cursor c;
    TextView productId_view, name_view, description_view, price_view, bitcoin_view;
    Button prevBtn, deleteBtn, nextBtn;
    FloatingActionButton fab;
    int index = 0;
    int length;

    // create a new activity view
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.browse_products_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // initialize objects (view and database)
        db = new ProductDBHelper(this);

        productId_view = (TextView) findViewById(R.id.product_id_view);
        name_view = (TextView) findViewById(R.id.product_name_view);
        description_view = (TextView) findViewById(R.id.desciption_view);
        price_view = (TextView) findViewById(R.id.price_cad_view);
        bitcoin_view = (TextView) findViewById(R.id.price_btc_view);
        prevBtn = (Button) findViewById(R.id.prevBtn);
        deleteBtn = (Button) findViewById(R.id.delBtn);
        nextBtn = (Button)  findViewById(R.id.nextBtn);
        fab = (FloatingActionButton) findViewById(R.id.fab);

        // get values for the database
        resetDB();
    }

    // retrieve the newest values for the database
    public void resetDB()
    {
        // define and initialize local variables
        index = 0;
        c = db.getValues();

        // verify if database is empty to either show a product or disable button
        if(c.getCount() == 0)
        {

            Toast.makeText(this, "The database has no values", Toast.LENGTH_LONG);
            nextBtn.setEnabled(false);
            prevBtn.setEnabled(false);
            deleteBtn.setEnabled(false);
        }
        else
        {
            length = c.getCount();
            deleteBtn.setEnabled(true);
            showProduct(index);
        }
    }

    // add product to the database
    public void addProduct(View view)
    {
        // create a new intent to switch to AddProductActivity
        Intent addProductIntent = new Intent(this, AddProductActivity.class);
        addProductIntent.putExtra("ID", length);
        startActivityForResult(addProductIntent, 1);
    }

    // receive the next product
    public void nextProduct(View view)
    {
        // update the index to show the next product
        index++;
        showProduct(index);
    }


    // check with the user if they want to delete the product
    public void delProduct(View view)
    {
        // initialize local variable
        final String idVal = productId_view.getText().toString();

        // if there is no id present, return nothing
        if(idVal == "")
        {
            return;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to do delete this product?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id)
                    {
                        //If user wants to delete product
                        db.deleteProduct(Integer.parseInt(idVal)); //We call the Delete method form DB
                        resetDB(); //Reset Values from DB
                        showProduct(index); //Show the first value again
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    // get the previous product
    public void prevProduct(View view)
    {
        index--;
        showProduct(index);
    }

    // display the product on the layout view - enable and disable the buttons too
    public void showProduct(int index)
    {
        if(index <= 0)
        {
            // disable the prev button
            prevBtn.setEnabled(false);
            // enable the floating action button (fab)
            fab.show();
        }
        else
        {
            // enable the prev button
            prevBtn.setEnabled(true);
            // disable the floating action button (fab)
            fab.hide();
        }

        if(index+1 >= length)
            // disable the next button
            nextBtn.setEnabled(false);
        else
            // enable the next button
            nextBtn.setEnabled(true);

        // exit function if the cursor cannot move to the next position
        if(!c.moveToPosition(index))
        {
            return;
        }

        // receive cursor value
        String product_id = c.getString(0);
        String name = c.getString(1);
        String  description = c.getString(2);
        String price = c.getString(3);
        try
        {
            // get the Bitcoin price from the given currency
            String btc_val= new PriceTask(this).execute("https://blockchain.info/tobtc?currency=CAD&value="+ price).get();
            bitcoin_view.setText(btc_val);
        }
        catch (ExecutionException e)
        {
            e.printStackTrace();
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }

        // set view data
        productId_view.setText(product_id + "");
        name_view.setText(name);
        description_view.setText(description);
        price_view.setText(price + "");
    }

    // reset database if new product was added
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == 1)
        {
            resetDB();
            showProduct(index);
        }
    }

    // add items to the action bar if present.
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_browse_products, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        /* handle action bar item clicks. The action bar will
           automatically handle clicks on the Home/Up button, so long
           as a parent activity is specified in AndroidManifest.xml.
        */
        int id = item.getItemId();

        if (id == R.id.action_settings)
        {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

/* class handles sending the product value in the specified currency
   to the web service, and retrieves the value in Bitcoin
 */
class PriceTask extends AsyncTask<String,Void,String>
{
    // define instance variable
    Context context;

    // constructor
    public PriceTask(Context c)
    {
        // initialize variable
        context = c;
    }

    // perform web service actions in background
    @Override
    protected String doInBackground(String[] string)
    {
        // define local variables
        String urlString = string[0];
        HttpsURLConnection urlConnection = null;
        URL url;

        try
        {
            // set up a new connection with the web service
            url= new URL(urlString);
            urlConnection = (HttpsURLConnection) url.openConnection();
            InputStream in = urlConnection.getInputStream();
            InputStreamReader isw = new InputStreamReader(in);

            int data = isw.read();
            String output = "";
            while (data != -1) {
                char current = (char) data;
                data = isw.read();
                output += current;
            }
            return output;

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            // disconnect from the web service
            if (urlConnection != null)
            {
                urlConnection.disconnect();
            }
        }
        //get url data and return it
        return null;
    }
}

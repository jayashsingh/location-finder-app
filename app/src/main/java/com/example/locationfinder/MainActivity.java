package com.example.locationfinder;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    Button search;
    EditText searchAddress;
    TextView results;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button nextFrag;

        //changes to next fragment when edit data button is clicked
        nextFrag = findViewById(R.id.editData);
        nextFrag.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, EditActivity.class));
            }
        });

        Database db = new Database(this);
        //instantiates variables by pointing to the specific element ids
        searchAddress = findViewById(R.id.searchAddress);
        search = findViewById(R.id.searchData);
        results = findViewById(R.id.addressResults);

        //database search method is called when user clicks the search button
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String address = searchAddress.getText().toString();
                Cursor getData = db.getQuery(address);

                //checks if there are any results from the query, and if so they are displayed on the screen
                if (getData.getCount()>0) {
                    //iterates through all possible results matching the user query
                    while (getData.moveToNext()) {
                        results.setText("Address: " + getData.getString(1) + "\nLatitude: " + getData.getString(2) + "\nLongitude: " + getData.getString(3) + "\n");
                    }
                }
                //otherwise the user is notified via toast that there are no results
                else {
                    Toast.makeText(MainActivity.this, "This address does not exist within the database.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
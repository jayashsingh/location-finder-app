package com.example.locationfinder;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class EditActivity extends AppCompatActivity {

    EditText mainAddress;
    EditText oldAddress;
    EditText newAddress;
    Button add;
    Button delete;
    Button update;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        //instantiates fields
        mainAddress = findViewById(R.id.address);
        oldAddress = findViewById(R.id.oldAddress);
        newAddress = findViewById(R.id.newAddress);

        //instantiates buttons
        add = findViewById(R.id.addAddress);
        delete = findViewById(R.id.deleteAddress);
        update = findViewById(R.id.updateAddress);

        Database db = new Database(this);

        //adds data to the database
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String addressValue = mainAddress.getText().toString();
                //gets longitude/latitude of specific address and parses coordinates from double to string
                double[] coords = getCoordinates(addressValue);
                //inserts new data into database by calling Database insert method
                Boolean addData = db.insertDb(addressValue, String.valueOf(coords[0]), String.valueOf(coords[1]));
                //check error conditions and displays toast accordingly
                if (addData) {
                    Toast.makeText(EditActivity.this, "Address insertion was successful.", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(EditActivity.this, "Address insertion was not successful.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String addressValue = mainAddress.getText().toString();
                //deletes data from database by calling Database delete method
                Boolean deleteData = db.deleteDb(addressValue);
                //check conditions and displays toast accordingly
                if (deleteData) {
                    Toast.makeText(EditActivity.this, "Address deletion was successful.", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(EditActivity.this, "Address deletion was not successful.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String oldAddressValue = oldAddress.getText().toString();
                String newAddressValue = newAddress.getText().toString();

                //gets longitude/latitude of specific address  and parses coordinates from double to string
                double[] coords = getCoordinates(newAddressValue);
                //sends update to database with new address and coordinates by calling Database update method
                Boolean updateData = db.updateDb(oldAddressValue, newAddressValue, String.valueOf(coords[0]), String.valueOf(coords[1]));
                //check conditions and displays toast accordingly
                if (updateData) {
                    Toast.makeText(EditActivity.this, "Address update was successful.", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(EditActivity.this, "Address update was not successful.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //user Geocoder class to get longitude and latitude and returns double array to method calls above
    public double[] getCoordinates(String addressValue) {

        //creates new Geocoder object
        Geocoder c = new Geocoder(getApplicationContext());
        //gets address data
        List<Address> address;

        try {
            //searches with given address
            address = c.getFromLocationName(addressValue, 1);
            if (address == null) {
                return null;
            }
            Address location = address.get(0);
            //gets latitude
            double latitude = location.getLatitude();
            //gets longitude
            double longitude = location.getLongitude();

            //returns double array of longitude and latitude
            return new double[] {latitude, longitude};
        }
        catch (Exception e) {
            return null;
        }
    }
}

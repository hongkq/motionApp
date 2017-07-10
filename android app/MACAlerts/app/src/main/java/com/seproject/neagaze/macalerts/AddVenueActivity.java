package com.seproject.neagaze.macalerts;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Spinner;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.seproject.neagaze.models.SportingEvent;
import com.seproject.neagaze.types.Server;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class AddVenueActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    private static String URL_SPORT_LIST =  Server.ADDR + "getSport";
    private static String URL_ADD_VENUE =  Server.ADDR + "addVenue";
    private String selectedSport;

    private Spinner sprtSpinner;
    private AutoCompleteTextView tvVenue;
    private Button addVenueButton;
    private ArrayAdapter<String> dataAdapter;
    private RequestQueue MyRequestQueue;
    private ArrayList<String> sportsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_venue);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        MyRequestQueue = Volley.newRequestQueue(AddVenueActivity.this);

        sprtSpinner = (Spinner) findViewById(R.id.sport_spinner);
        tvVenue = (AutoCompleteTextView) findViewById(R.id.venue_name);
        addVenueButton = (Button) findViewById(R.id.add_venue_button);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        sprtSpinner.setOnItemSelectedListener(this);
        addVenueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO update venue
                if(selectedSport != null)
                    updateVenue();
            }
        });
        getData();
    }


    /**
     * Get the data from server and populate RecyclerView
     * **/
    public void updateVenue(){
        HashMap<String, String> param = new HashMap<String, String>();
        param.put("sportname", selectedSport);
        param.put("venue", tvVenue.getText().toString());
        JsonObjectRequest addVenueReq = new JsonObjectRequest(Request.Method.POST, URL_ADD_VENUE, new JSONObject(param),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            VolleyLog.e("Response:%n %s", response.toString(4));
                            if(response.getBoolean("status")) {
                                new AlertDialog.Builder(AddVenueActivity.this)
                                        .setTitle("Venue added Successful")
                                        .setMessage("You have added the venue to the " + selectedSport)
                                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                // continue with add venue
                                                finish();
                                            }
                                        })
                                        .setIcon(android.R.drawable.ic_dialog_alert)
                                        .show();
                            } else {
                                new AlertDialog.Builder(AddVenueActivity.this)
                                        .setTitle("Error adding venue")
                                        .setMessage("Oops! Something went wrong. Please try again later")
                                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {

                                            }
                                        })
                                        .setIcon(android.R.drawable.ic_dialog_alert)
                                        .show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e("Error: ", error.getMessage());
            }
        });

        MyRequestQueue.add(addVenueReq);
    }


    /**
     * Get the data from server and populate RecyclerView
     * **/
    public void getData() {
        JsonArrayRequest getReq = new JsonArrayRequest(Request.Method.GET, URL_SPORT_LIST, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            VolleyLog.e("Response:%n %s", response.toString(4));

                            sportsList = new ArrayList<>();
                            for(int i = 0; i < response.length(); i++){
                                JSONObject obj = response.getJSONObject(i);
                                sportsList.add(obj.getString("sportname"));
                            }

                            dataAdapter = new ArrayAdapter<String>(AddVenueActivity.this,
                                    android.R.layout.simple_spinner_item, sportsList);
                            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            sprtSpinner.setAdapter(dataAdapter);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e("Error: ", error.getMessage());
            }
        });

        MyRequestQueue.add(getReq);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        selectedSport = adapterView.getItemAtPosition(i).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}

package com.seproject.neagaze.macalerts;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Spinner;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.seproject.neagaze.types.Server;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class CreateSprtEvtActivity extends AppCompatActivity implements View.OnClickListener{

    private AutoCompleteTextView mSportName, maxTeamSize, minTeamSize, manager;
    private Button btAddMgr, registerButton;
    private Spinner mgrSpinner;

    private ArrayList<String> managerList;
    private ArrayAdapter<String> dataAdapter;

    private RequestQueue MyRequestQueue;

    private static String URL =  Server.ADDR + "createSport";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_sprt_evt);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        managerList = new ArrayList<String>();

        mSportName = (AutoCompleteTextView)findViewById(R.id.sport_name);
        maxTeamSize = (AutoCompleteTextView)findViewById(R.id.max_team_size);
        minTeamSize = (AutoCompleteTextView)findViewById(R.id.min_team_size);
        manager = (AutoCompleteTextView)findViewById(R.id.manager);
        mgrSpinner = (Spinner) findViewById(R.id.mgr_spinner);
        btAddMgr = (Button) findViewById(R.id.bt_mgr_add);
        registerButton = (Button) findViewById(R.id.create_sprt_evt_button);

        btAddMgr.setOnClickListener(this);

        MyRequestQueue = Volley.newRequestQueue(CreateSprtEvtActivity.this);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                HashMap<String, String> param = new HashMap<String, String>();
                param.put("sportname", mSportName.getText().toString());
                param.put("maxteamsize", maxTeamSize.getText().toString());
                param.put("minteamsize", minTeamSize.getText().toString());

                JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, URL, new JSONObject(param),
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    VolleyLog.e("Response:%n %s", response.toString(4));

                                    if(response.getBoolean("status")) {
                                        new AlertDialog.Builder(CreateSprtEvtActivity.this)
                                                .setTitle("Create Sporting Event Success")
                                                .setMessage("You have successfully Created a sporting Event")
                                                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        // continue with delete
                                                        finish();
                                                    }
                                                })
                                                .setIcon(android.R.drawable.ic_dialog_alert)
                                                .show();

                                    } else{
                                        new AlertDialog.Builder(CreateSprtEvtActivity.this)
                                                .setTitle("Error Create Sporting Event")
                                                .setMessage("Oops! Something went wrong. Try again please")
                                                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        // continue with delete
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

                MyRequestQueue.add(req);


            }
        });
        setAdapter();
    }

    @Override
    public void onClick(View view) {
        if(view == btAddMgr){
            managerList.add(manager.getText().toString());
            dataAdapter.notifyDataSetChanged();
            setAdapter();
        }
    }

    public void setAdapter(){

        dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, managerList);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mgrSpinner.setAdapter(dataAdapter);
    }
}

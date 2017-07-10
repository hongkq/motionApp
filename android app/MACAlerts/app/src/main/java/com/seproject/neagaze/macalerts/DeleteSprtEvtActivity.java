package com.seproject.neagaze.macalerts;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

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
import java.util.List;

public class DeleteSprtEvtActivity extends AppCompatActivity {


    private static String URL_DELETE_SPORT =  Server.ADDR + "removeSport";
    private static String URL_SPORT_LIST =  Server.ADDR + "getSport";

    private RecyclerView recList;
    private RequestQueue MyRequestQueue;
    private ArrayList<SportingEvent> sportsList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_sprt_evt);
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

        MyRequestQueue = Volley.newRequestQueue(DeleteSprtEvtActivity.this);

        // recyclerview
        recList = (RecyclerView) findViewById(R.id.sportEventList);
        recList.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recList.setLayoutManager(llm);

        getData();
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

                                SportingEvent a3 = new SportingEvent();
                                a3.sportName = obj.getString("sportname");
                                a3.minTeamSize = "Min Team Size : " + obj.getString("minteamsize");
                                a3.maxTeamSize = "Max Team Size : " + obj.getString("maxteamsize");
                                sportsList.add(a3);
                            }

                            DeleteSprtEvtActivity.SprtEventAdapter ca = new DeleteSprtEvtActivity.SprtEventAdapter(sportsList);
                            recList.setAdapter(ca);
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

    // View Results Adapter
    public class SprtEventAdapter extends RecyclerView.Adapter<SprtEventAdapter.SportingEventViewHolder> {

        private List<SportingEvent> sportingList;

        public SprtEventAdapter(List<SportingEvent> sportingList) {
            this.sportingList = sportingList;
        }


        @Override
        public int getItemCount() {
            return sportingList.size();
        }

        @Override
        public void onBindViewHolder(SportingEventViewHolder sportEventViewHolder, final int i) {
            final SportingEvent ci = sportingList.get(i);
            sportEventViewHolder.tvSportName.setText(ci.sportName);
            sportEventViewHolder.tvMinTeamSize.setText(ci.minTeamSize);
            sportEventViewHolder.tvMaxTeamSize.setText(ci.maxTeamSize);


            sportEventViewHolder.btDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    new AlertDialog.Builder(DeleteSprtEvtActivity.this)
                            .setTitle("Confirm Delete")
                            .setMessage("Are you sure you want to Delete " + ci.sportName + "?")
                            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {

                                    // continue with delete
                                    HashMap<String, String> param = new HashMap<String, String>();
                                    param.put("sportname", ci.sportName);

                                    JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, URL_DELETE_SPORT, new JSONObject(param),
                                            new Response.Listener<JSONObject>() {
                                                @Override
                                                public void onResponse(JSONObject response) {
                                                    try {
                                                        VolleyLog.e("Response:%n %s", response.toString(4));
                                                        if (response.getBoolean("status")) {
                                                            new AlertDialog.Builder(DeleteSprtEvtActivity.this)
                                                                    .setTitle("Sporting Event Delete Successful")
                                                                    .setMessage("You have successfully deleted " + ci.sportName)
                                                                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                                                        public void onClick(DialogInterface dialog, int which) {
                                                                            // deleted
                                                                           // DeleteSprtEvtActivity.this.startActivity(new Intent(DeleteSprtEvtActivity.this, DeleteSprtEvtActivity.class));
                                                                           // DeleteSprtEvtActivity.this.finish();
                                                                            getData();
                                                                        }
                                                                    })
                                                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                                                    .show();

                                                        } else {
                                                            new AlertDialog.Builder(DeleteSprtEvtActivity.this)
                                                                    .setTitle("Error Deleting Sporting Event")
                                                                    .setMessage("Oops! Something went wrong. Try again please")
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
                                    MyRequestQueue.add(req);
                                }
                            })
                            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // refrain from delete

                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                }
            });
        }

        @Override
        public SportingEventViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View itemView = LayoutInflater.
                    from(viewGroup.getContext()).
                    inflate(R.layout.listview_sprt_evt, viewGroup, false);

            return new SportingEventViewHolder(itemView);
        }

        public class SportingEventViewHolder extends RecyclerView.ViewHolder {

            protected TextView tvSportName, tvMinTeamSize, tvMaxTeamSize;
            protected ListView lvReferees, lvVenues;
            protected Button btDelete;

            public SportingEventViewHolder(View v) {
                super(v);
                tvSportName = (TextView) v.findViewById(R.id.tv_sport_name);
                tvMinTeamSize = (TextView) v.findViewById(R.id.tv_min_team_size);
                tvMaxTeamSize = (TextView) v.findViewById(R.id.tv_max_team_size);
                lvReferees = (ListView) v.findViewById(R.id.lv_referees);
                lvVenues = (ListView) v.findViewById(R.id.lv_venues);
                btDelete = (Button) v.findViewById(R.id.bt_confirm_sport_delete);
            }
        }
    }

    // dummy list
    private List<SportingEvent> createDummyList() {

        List<SportingEvent> result = new ArrayList<>();

        // 1
        SportingEvent ci = new SportingEvent();
        ci.sportName = "Men's Baseball";
        ci.minTeamSize = "Min Team Size: 7";
        ci.maxTeamSize = "Max Team Size: 14";
        result.add(ci);

        // 2
        SportingEvent a2 = new SportingEvent();
        a2.sportName = "Men's basketball";
        a2.minTeamSize = "Min Team Size: 5";
        a2.maxTeamSize = "Max Team Size: 10";
        result.add(a2);

        // 3
        SportingEvent a3 = new SportingEvent();
        a3.sportName = "Men's Soccer";
        a3.minTeamSize = "Min Team Size: 11";
        a3.maxTeamSize = "Max Team Size: 18";
        result.add(a3);

        return result;
    }

}

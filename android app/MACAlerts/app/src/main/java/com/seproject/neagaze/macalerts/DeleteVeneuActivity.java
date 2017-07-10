package com.seproject.neagaze.macalerts;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.seproject.neagaze.models.SportingEvent;

import java.util.Arrays;
import java.util.List;

public class DeleteVeneuActivity extends AppCompatActivity {

    private List arr;
    private DeleteVeneuActivity.DeleteVenueAdapter ca;
    private RecyclerView recList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_veneu);
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

        // recyclerview
        recList = (RecyclerView) findViewById(R.id.venue_list);
        recList.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recList.setLayoutManager(llm);
        arr = Arrays.asList(getResources().getStringArray(R.array.venue_arrays));
        ca = new DeleteVeneuActivity.DeleteVenueAdapter(arr);
        recList.setAdapter(ca);
    }


    // View Results Adapter
    public class DeleteVenueAdapter extends RecyclerView.Adapter<DeleteVenueAdapter.SportingEventViewHolder> {

        private List venueList;

        public DeleteVenueAdapter(List venueList) {
            this.venueList = venueList;
        }


        @Override
        public int getItemCount() {
            return venueList.size();
        }

        @Override
        public void onBindViewHolder(DeleteVenueAdapter.SportingEventViewHolder sportEventViewHolder, final int i) {
            final String ci = (String)venueList.get(i);
            sportEventViewHolder.tvVenueName.setText(ci+"");


            sportEventViewHolder.btDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new AlertDialog.Builder(DeleteVeneuActivity.this)
                            .setTitle("Confirm Delete")
                            .setMessage("Are you sure you want to Delete " + ci +"?")
                            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // continue with delete
                                    arr = Arrays.asList(getResources().getStringArray(R.array.after_delete_venue_arrays));
                                    ca = new DeleteVeneuActivity.DeleteVenueAdapter(arr);
                                    recList.setAdapter(ca);

                                    new AlertDialog.Builder(DeleteVeneuActivity.this)
                                            .setTitle("Venue Delete Successful")
                                            .setMessage("You have successfully deleted " + ci)
                                            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {
                                                    // continue with delete
                                                }
                                            })
                                            .setIcon(android.R.drawable.ic_dialog_alert)
                                            .show();
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
        public DeleteVenueAdapter.SportingEventViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View itemView = LayoutInflater.
                    from(viewGroup.getContext()).
                    inflate(R.layout.listitem_venue, viewGroup, false);

            return new DeleteVenueAdapter.SportingEventViewHolder(itemView);
        }

        public class SportingEventViewHolder extends RecyclerView.ViewHolder {

            protected TextView tvVenueName;
            protected Button btDelete;

            public SportingEventViewHolder(View v) {
                super(v);
                tvVenueName = (TextView) v.findViewById(R.id.tv_delete_venue);
                btDelete = (Button) v.findViewById(R.id.bt_delete_venue);
            }
        }
    }


}

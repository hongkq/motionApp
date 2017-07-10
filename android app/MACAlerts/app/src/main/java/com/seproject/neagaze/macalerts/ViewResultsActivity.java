package com.seproject.neagaze.macalerts;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.seproject.neagaze.models.Schedule;
import com.seproject.neagaze.types.UserType;

import java.util.ArrayList;
import java.util.List;

public class ViewResultsActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener  {

    private int userType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewresults);
        userType = UserType.MANAGER;

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

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        Menu navMenu = navigationView.getMenu();
        MenuItem menuItemAppointMgr = navMenu.findItem(R.id.appoint_manager);
        MenuItem menuItemCreateSport = navMenu.findItem(R.id.create_sporting_event);
        MenuItem menuItemDeleteSport = navMenu.findItem(R.id.delete_sporting_event);
        MenuItem menuItemAddVenues = navMenu.findItem(R.id.add_venues);
        MenuItem menuItemDeleteVenues = navMenu.findItem(R.id.delete_venues);
        MenuItem menuItemAdminPanel = navMenu.findItem(R.id.admin_panel);
        // if(userType != UserType.ADMIN){
        menuItemAppointMgr.setVisible(false);
        menuItemCreateSport.setVisible(false);
        menuItemDeleteSport.setVisible(false);
        menuItemAddVenues.setVisible(false);
        menuItemDeleteVenues.setVisible(false);
        menuItemAdminPanel.setVisible(false);
        // }

        // recyclerview
        RecyclerView recList = (RecyclerView) findViewById(R.id.matchResultList);
        recList.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recList.setLayoutManager(llm);
        ViewResultsActivity.ContactAdapter ca = new ViewResultsActivity.ContactAdapter(createList(30));
        recList.setAdapter(ca);
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.homepage, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.view_schedule) {

        } else if (id == R.id.view_results) {
            // Handle the schedule view action
            startActivity(new Intent(this, ViewResultsActivity.class));
            this.finish();
        } else if (id == R.id.update_profile) {

            startActivity(new Intent(this, CreateProfileActivity.class));
        } else if (id == R.id.logoff) {

            startActivity(new Intent(this, LoginActivity.class));
            finish();
        } else if (id == R.id.appoint_manager) {

            startActivity(new Intent(this, AppointManagerActivity.class));
        } else if (id == R.id.create_sporting_event) {

            startActivity(new Intent(this, CreateSprtEvtActivity.class));
        } else if (id == R.id.delete_sporting_event) {

            startActivity(new Intent(this, DeleteSprtEvtActivity.class));
        } else if (id == R.id.add_venues) {

            startActivity(new Intent(this, AddVenueActivity.class));
        } else if (id == R.id.delete_venues) {

            startActivity(new Intent(this, DeleteVeneuActivity.class));
        } else if (id == R.id.register_team) {

            startActivity(new Intent(this, RegisterTeamActivity.class));
        } else if (id == R.id.view_team) {

            startActivity(new Intent(this, ViewTeamActivity.class));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    // View Results Adapter
    public class ContactAdapter extends RecyclerView.Adapter<ViewResultsActivity.ContactAdapter.ContactViewHolder> {

        private List<Schedule> contactList;

        public ContactAdapter(List<Schedule> contactList) {
            this.contactList = contactList;
        }


        @Override
        public int getItemCount() {
            return contactList.size();
        }

        @Override
        public void onBindViewHolder(final ViewResultsActivity.ContactAdapter.ContactViewHolder contactViewHolder, int i) {
            Schedule ci = contactList.get(i);
            contactViewHolder.tvSport.setText(ci.Sport);
            contactViewHolder.tvMatch.setText(ci.Match);
            contactViewHolder.tvDate.setText(ci.Date);
            contactViewHolder.tvTime.setText(ci.Time);
            contactViewHolder.tvVenue.setText(ci.Venue);
            contactViewHolder.tvWinner.setText(ci.Winner);
            contactViewHolder.tvScore.setText(ci.Score);

            if(userType == UserType.MANAGER) contactViewHolder.btUpdateScore.setVisibility(View.VISIBLE);
            else contactViewHolder.btUpdateScore.setVisibility(View.GONE);

            // TODO remove this
            contactViewHolder.btUpdateScore.setVisibility(View.GONE);

            contactViewHolder.btUpdateScore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final View innerView =  getLayoutInflater().inflate(R.layout.view_update_score, null, true);
                    AlertDialog.Builder dial = new AlertDialog.Builder(ViewResultsActivity.this);
                    dial.setView(innerView)
                            .setTitle("Update Score")
                            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // continue with update score
                                    new AlertDialog.Builder(ViewResultsActivity.this)
                                            .setTitle("Score Updated Successful")
                                            .setMessage("You have successfully updated score ")
                                            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {
                                                    // continue with update
                                                    EditText etScore = (EditText) innerView.findViewById(R.id.etScore);
                                                    Spinner spTeam = (Spinner) innerView.findViewById(R.id.sp_select_winning_team);
                                                    contactViewHolder.tvScore.setText(etScore.getText().toString());
                                                    contactViewHolder.tvWinner.setText(spTeam.getSelectedItem().toString());
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
        public ViewResultsActivity.ContactAdapter.ContactViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View itemView = LayoutInflater.
                    from(viewGroup.getContext()).
                    inflate(R.layout.listitem_schedule, viewGroup, false);

            return new ViewResultsActivity.ContactAdapter.ContactViewHolder(itemView);
        }

        public class ContactViewHolder extends RecyclerView.ViewHolder {

            protected TextView tvSport, tvMatch, tvDate, tvTime, tvVenue, tvWinner, tvScore;
            protected Button btUpdateScore;

            public ContactViewHolder(View v) {
                super(v);
                tvSport = (TextView) v.findViewById(R.id.tv_sport);
                tvMatch = (TextView) v.findViewById(R.id.tv_match);
                tvDate = (TextView) v.findViewById(R.id.tv_venue);
                tvTime = (TextView) v.findViewById(R.id.tv_date);
                tvVenue = (TextView) v.findViewById(R.id.tv_time);
                tvWinner = (TextView) v.findViewById(R.id.tv_winner);
                tvScore = (TextView) v.findViewById(R.id.tv_score);
                btUpdateScore = (Button) v.findViewById(R.id.bt_update_score);

                tvWinner.setVisibility(View.VISIBLE);
                tvScore.setVisibility(View.VISIBLE);
                btUpdateScore.setVisibility(View.VISIBLE);
            }
        }
    }


    private List<Schedule> createList(int size) {

        List<Schedule> result = new ArrayList<>();

        // 5
        Schedule ci1 = new Schedule();
        ci1.Match = "Team Mavericks vs. Team batman ";
        ci1.Sport = "Men's Basketball";
        ci1.Date = "25 Oct, 2016";
        ci1.Time = "01:00 pm";
        ci1.Venue = "Intramural fields";
        ci1.Winner = "Team batman";
        ci1.Score = "4-1";
        result.add(ci1);

// 1
        Schedule ci = new Schedule();
        ci.Match = "Team batman vs. Team Mavericks";
        ci.Sport = "Men's Basketball";
        ci.Date = "14 Oct, 2016";
        ci.Time = "01:00 pm";
        ci.Venue = "Intramural fields";
        ci.Winner = "Team batman";
        ci.Score = "9-2";
        result.add(ci);

        // 4
        Schedule a4 = new Schedule();
        a4.Match = "New Barcelona vs. New Madrid";
        a4.Sport = "Men's Soccer";
        a4.Date = "22 Oct, 2016";
        a4.Time = "11:00 pm";
        a4.Venue = "Stubhub Center";
        a4.Winner = "New Madrid";
        a4.Score = "2-3";
        result.add(a4);

        // 3
        Schedule a3 = new Schedule();
        a3.Match = "El Matador vs. Com'On Gals";
        a3.Sport = "Women's Volleyball\"";
        a3.Date = "22 Oct, 2016";
        a3.Time = "09:00 pm";
        a3.Venue = "Staples Center";
        a3.Winner = "El Matador";
        a3.Score = "100-101";
        result.add(a3);

        // 2
        Schedule a2 = new Schedule();
        a2.Match = "Invincibles vs. Team Chronicles";
        a2.Sport = "Men's Pro Football";
        a2.Date = "21 Oct, 2016";
        a2.Time = "07:00 pm";
        a2.Venue = "AT&T Stadium";
        a2.Winner = "Invincibles";
        a2.Score = "27-12";
        result.add(a2);

        return result;
    }

}

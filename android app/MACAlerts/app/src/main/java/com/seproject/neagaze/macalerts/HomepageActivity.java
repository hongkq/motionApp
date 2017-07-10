package com.seproject.neagaze.macalerts;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.seproject.neagaze.models.Schedule;
import com.seproject.neagaze.types.Server;
import com.seproject.neagaze.types.UserType;

import java.util.ArrayList;
import java.util.List;

public class HomepageActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {


    private static int userType;
    private RecyclerView recList;
    private List<Schedule> schedules;

    private static String URL = Server.ADDR + "getSchedule";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);
        Bundle b = getIntent().getExtras();

        if(b != null) {
            String utaid = b.getString("utaid");
            String desg = b.getString("designation");
            if (desg.equals(UserType.USER_STR)) userType = UserType.COMMON_USER;
            else if (desg.equals(UserType.MGR_STR)) userType = UserType.MANAGER;
            else if (desg.equals(UserType.ADMIN_STR)) userType = UserType.ADMIN;
            else userType = UserType.COMMON_USER;
        }
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        if(userType == UserType.ADMIN) fab.setVisibility(View.VISIBLE);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Create a Match Event", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

                newMatchList();
                ContactAdapter ca = new ContactAdapter(schedules);
                recList.setAdapter(ca);

                startActivity(new Intent(HomepageActivity.this, CreateMatchActivity.class));
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        Menu navMenu = navigationView.getMenu();
        MenuItem menuItemAppointMgr = navMenu.findItem(R.id.appoint_manager);
        MenuItem menuItemCreateSport = navMenu.findItem(R.id.create_sporting_event);
        MenuItem menuItemDeleteSport = navMenu.findItem(R.id.delete_sporting_event);
        MenuItem menuItemAddVenues = navMenu.findItem(R.id.add_venues);
        MenuItem menuItemDeleteVenues = navMenu.findItem(R.id.delete_venues);
        MenuItem menuItemAdminPanel = navMenu.findItem(R.id.admin_panel);

        if(userType != UserType.ADMIN){
            menuItemAppointMgr.setVisible(false);
            menuItemCreateSport.setVisible(false);
            menuItemDeleteSport.setVisible(false);
            menuItemAddVenues.setVisible(false);
            menuItemDeleteVenues.setVisible(false);
            menuItemAdminPanel.setVisible(false);
        }
        navigationView.setNavigationItemSelectedListener(this);


        // recyclerview
        recList = (RecyclerView) findViewById(R.id.matchScheduleList);
        recList.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recList.setLayoutManager(llm);
        schedules = createList(4);
        ContactAdapter ca = new ContactAdapter(schedules);
        recList.setAdapter(ca);
    }

    @Override
    public void onClick(View view) {
        int itemPosition = recList.getChildLayoutPosition(view);
        int item = (int)view.getTag();
        Toast.makeText(this, "Schedule ID : " + item, Toast.LENGTH_SHORT).show();
        new AlertDialog.Builder(HomepageActivity.this)
                .setTitle("Assign Referee")
                .setMessage("You have successfully added a referee")
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO: add referee now
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
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

    public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ContactViewHolder> {

        private List<Schedule> contactList;

        public ContactAdapter(List<Schedule> contactList) {
            this.contactList = contactList;
        }


        @Override
        public int getItemCount() {
            return contactList.size();
        }

        @Override
        public void onBindViewHolder(ContactViewHolder contactViewHolder, int i) {
            Schedule ci = contactList.get(i);
            contactViewHolder.tvSport.setText(ci.Sport + "");
            contactViewHolder.tvMatch.setText(ci.Match + "");
            contactViewHolder.tvDate.setText(ci.Date + "");
            contactViewHolder.tvTime.setText(ci.Time + "");
            contactViewHolder.tvVenue.setText(ci.Venue);
        }

        @Override
        public ContactViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View itemView = LayoutInflater.
                    from(viewGroup.getContext()).
                    inflate(R.layout.listitem_schedule, viewGroup, false);

            if(userType == UserType.MANAGER) {
                itemView.setOnClickListener(HomepageActivity.this);
                int id = contactList.get(i).scheduleId;
                itemView.setTag(id);
                Log.e("TAG","tagged : " + i);
            }
            return new ContactViewHolder(itemView);
        }

        public class ContactViewHolder extends RecyclerView.ViewHolder {

            protected TextView tvSport;
            protected TextView tvMatch;
            protected TextView tvDate;
            protected TextView tvTime;
            protected TextView tvVenue;

            public ContactViewHolder(View v) {
                super(v);
                tvSport = (TextView) v.findViewById(R.id.tv_sport);
                tvMatch = (TextView) v.findViewById(R.id.tv_match);
                tvDate = (TextView) v.findViewById(R.id.tv_venue);
                tvTime = (TextView) v.findViewById(R.id.tv_date);
                tvVenue = (TextView) v.findViewById(R.id.tv_time);
            }
        }
    }


    /**
     * Dummy data
     ***/
    private List<Schedule> createList(int size) {

        List<Schedule> result = new ArrayList<>();

        // 1
        Schedule ci = new Schedule();
        ci.scheduleId = 1;
        ci.Match = "Invincibles vs. Team Chronicles";
        ci.Sport = "Men's Pro Football";
        ci.Date = "1 Dec, 2016";
        ci.Time = "01:00 pm";
        ci.Venue = "Intramural fields";
        result.add(ci);

        // 2
        Schedule a2 = new Schedule();
        ci.scheduleId = 2;
        a2.Match = "Team Mavericks vs. Team batman";
        a2.Sport = "Men's BasketBall";
        a2.Date = "1 Dec, 2016";
        a2.Time = "07:00 pm";
        a2.Venue = "AT&T Stadium";
        result.add(a2);

        // 3
        Schedule a3 = new Schedule();
        ci.scheduleId = 3;
        a3.Match = "El Matador vs. Com'On Gals";
        a3.Sport = "Women's Volleyball";
        a3.Date = "5 Dec, 2016";
        a3.Time = "09:00 pm";
        a3.Venue = "Staples Center";
        result.add(a3);

        // 4
        Schedule a4 = new Schedule();
        ci.scheduleId = 4;
        a4.Match = "New Barcelona vs. New Madrid";
        a4.Sport = "Men's Soccer";
        a4.Date = "5 Dec, 2016";
        a4.Time = "11:00 pm";
        a4.Venue = "Stubhub Center";
        result.add(a4);

        return result;
    }

    /**
     * Dummy data
     ***/
    private void newMatchList() {

        Schedule ci = new Schedule();
        ci.scheduleId = 1;
        ci.Match = "New Madrid vs. New Liverpool";
        ci.Sport = "Men's Pro Football";
        ci.Date = "6 Dec, 2016";
        ci.Time = "05:30 pm";
        ci.Venue = "Intramural fields";
        schedules.add(ci);
    }
}

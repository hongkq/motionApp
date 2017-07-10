package com.seproject.neagaze.macalerts;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.seproject.neagaze.models.Schedule;
import com.seproject.neagaze.models.SportingEvent;
import com.seproject.neagaze.models.Team;
import com.seproject.neagaze.types.Server;
import com.seproject.neagaze.types.UserType;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ViewTeamActivity extends AppCompatActivity {

    private int MAX_MEMBER_COUNT = 5;
    private static String URL_GET_TEAM =  Server.ADDR + "getTeam";

    private RecyclerView recList;
    private ArrayList<Team> teamList;
    private RequestQueue MyRequestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_team);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // recyclerview
        recList = (RecyclerView) findViewById(R.id.matchResultList);
        recList.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recList.setLayoutManager(llm);
        teamList = (ArrayList<Team>) createList(4);

        ViewTeamActivity.ContactAdapter ca = new ViewTeamActivity.ContactAdapter(teamList);
        recList.setAdapter(ca);

     //   getData();
    }

    /**
     * TODO I have lost the motivation to complete this
     * Get the data from server and populate RecyclerView
     * **/
    public void getData() {
        JsonArrayRequest getReq = new JsonArrayRequest(Request.Method.GET, URL_GET_TEAM, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            VolleyLog.e("Response:%n %s", response.toString(4));

                            teamList = new ArrayList<>();
                            for(int i = 0; i < response.length(); i++){
                                JSONObject obj = response.getJSONObject(i);

                                Team a3 = new Team();
                                a3.SportName = obj.getString("sportname");
                                a3.isApproved = obj.getBoolean("isapproved");
                                a3.members.add(obj.getString("member1"));
                                a3.members.add(obj.getString("member2"));
                                a3.members.add(obj.getString("member3"));
                                a3.members.add(obj.getString("member4"));
                                a3.members.add(obj.getString("member5"));
                                teamList.add(a3);
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

        MyRequestQueue.add(getReq);
    }

    @Override
    public void onBackPressed() {
        this.finish();
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

    // View Team Adapter
    public class ContactAdapter extends RecyclerView.Adapter<ViewTeamActivity.ContactAdapter.ContactViewHolder> {

        private List<Team> contactList;

        public ContactAdapter(List<Team> contactList) {
            this.contactList = contactList;
        }


        @Override
        public int getItemCount() {
            return contactList.size();
        }

        @Override
        public void onBindViewHolder(final ViewTeamActivity.ContactAdapter.ContactViewHolder contactViewHolder, int i) {
            final Team ci = contactList.get(i);
            contactViewHolder.tvSport.setText(ci.SportName);
            contactViewHolder.tvTeam.setText(ci.Name);
            int memberCount = ci.members.size();

            if(memberCount == MAX_MEMBER_COUNT - 1){
                contactViewHolder.tvMember1.setText(ci.members.get(0));
                contactViewHolder.tvMember2.setText(ci.members.get(1));
                contactViewHolder.tvMember3.setText(ci.members.get(2));
                contactViewHolder.tvMember4.setText(ci.members.get(3));
                contactViewHolder.tvMember5.setVisibility(View.GONE);
                contactViewHolder.btRemoveMember5.setVisibility(View.GONE);

            } else if(memberCount == MAX_MEMBER_COUNT - 2){
                contactViewHolder.tvMember1.setText(ci.members.get(0));
                contactViewHolder.tvMember2.setText(ci.members.get(1));
                contactViewHolder.tvMember3.setText(ci.members.get(2));
                contactViewHolder.tvMember4.setVisibility(View.GONE);
                contactViewHolder.tvMember5.setVisibility(View.GONE);
                contactViewHolder.btRemoveMember4.setVisibility(View.GONE);
                contactViewHolder.btRemoveMember5.setVisibility(View.GONE);

            } else if(memberCount == MAX_MEMBER_COUNT - 3){
                contactViewHolder.tvMember1.setText(ci.members.get(0));
                contactViewHolder.tvMember2.setText(ci.members.get(1));
                contactViewHolder.tvMember3.setVisibility(View.GONE);
                contactViewHolder.tvMember4.setVisibility(View.GONE);
                contactViewHolder.tvMember5.setVisibility(View.GONE);
                contactViewHolder.btRemoveMember3.setVisibility(View.GONE);
                contactViewHolder.btRemoveMember4.setVisibility(View.GONE);
                contactViewHolder.btRemoveMember5.setVisibility(View.GONE);

            } else if(memberCount == MAX_MEMBER_COUNT - 4){
                contactViewHolder.tvMember1.setText(ci.members.get(0));
                contactViewHolder.tvMember2.setVisibility(View.GONE);
                contactViewHolder.tvMember3.setVisibility(View.GONE);
                contactViewHolder.tvMember4.setVisibility(View.GONE);
                contactViewHolder.tvMember5.setVisibility(View.GONE);
                contactViewHolder.btRemoveMember2.setVisibility(View.GONE);
                contactViewHolder.btRemoveMember3.setVisibility(View.GONE);
                contactViewHolder.btRemoveMember4.setVisibility(View.GONE);
                contactViewHolder.btRemoveMember5.setVisibility(View.GONE);

            } else if(memberCount == MAX_MEMBER_COUNT - 5){
                contactViewHolder.tvMember1.setVisibility(View.GONE);
                contactViewHolder.tvMember2.setVisibility(View.GONE);
                contactViewHolder.tvMember3.setVisibility(View.GONE);
                contactViewHolder.tvMember4.setVisibility(View.GONE);
                contactViewHolder.tvMember5.setVisibility(View.GONE);
                contactViewHolder.btRemoveMember1.setVisibility(View.GONE);
                contactViewHolder.btRemoveMember2.setVisibility(View.GONE);
                contactViewHolder.btRemoveMember3.setVisibility(View.GONE);
                contactViewHolder.btRemoveMember4.setVisibility(View.GONE);
                contactViewHolder.btRemoveMember5.setVisibility(View.GONE);
            }

            if(!ci.isApproved) {
                contactViewHolder.btApprove.setVisibility(View.VISIBLE);
                contactViewHolder.tvTeam.setBackgroundColor(getResources().getColor(R.color.yellow));
            } else {
                contactViewHolder.btApprove.setVisibility(View.GONE);
                contactViewHolder.tvTeam.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            }

            // TODO remove this
            contactViewHolder.btApprove.setVisibility(View.GONE);

            contactViewHolder.btApprove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    boolean approveSuccess = true;
                    if(approveSuccess) {
                        new AlertDialog.Builder(ViewTeamActivity.this)
                                .setTitle("Approve Team Successful")
                                .setMessage("You have successfully approved the team ")
                                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        // continue with delete
                                    }
                                })
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .show();

                        contactViewHolder.btApprove.setVisibility(View.GONE);
                        contactViewHolder.tvTeam.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    }
                }
            });

            // remove players
            contactViewHolder.btRemoveMember1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new DialogDelete(ViewTeamActivity.this, ci.members.get(0)).showDial();
                }
            });
            contactViewHolder.btRemoveMember2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new DialogDelete(ViewTeamActivity.this, ci.members.get(1)).showDial();
                }
            });
            contactViewHolder.btRemoveMember3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new DialogDelete(ViewTeamActivity.this, ci.members.get(2)).showDial();
                }
            });
            contactViewHolder.btRemoveMember4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new DialogDelete(ViewTeamActivity.this, ci.members.get(3)).showDial();
                }
            });
            contactViewHolder.btRemoveMember5.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new DialogDelete(ViewTeamActivity.this, ci.members.get(4)).showDial();
                }
            });
            contactViewHolder.btAddMember.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new DialogDelete(ViewTeamActivity.this).addMember();
                }
            });

        }

        @Override
        public ViewTeamActivity.ContactAdapter.ContactViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View itemView = LayoutInflater.
                    from(viewGroup.getContext()).
                    inflate(R.layout.listitem_view_team, viewGroup, false);

            return new ViewTeamActivity.ContactAdapter.ContactViewHolder(itemView);
        }

        public class ContactViewHolder extends RecyclerView.ViewHolder {

            protected TextView tvSport, tvTeam, tvMember1, tvMember2, tvMember3, tvMember4, tvMember5;
            protected Button btApprove, btRemoveMember1, btRemoveMember2, btRemoveMember3, btRemoveMember4,
                    btRemoveMember5, btAddMember;

            public ContactViewHolder(View v) {
                super(v);
                tvSport = (TextView) v.findViewById(R.id.tv_sport);
                tvTeam = (TextView) v.findViewById(R.id.tv_team);
                tvMember1 = (TextView) v.findViewById(R.id.tv_member1);
                tvMember2 = (TextView) v.findViewById(R.id.tv_member2);
                tvMember3 = (TextView) v.findViewById(R.id.tv_member3);
                tvMember4 = (TextView) v.findViewById(R.id.tv_member4);
                tvMember5 = (TextView) v.findViewById(R.id.tv_member5);
                btApprove = (Button) v.findViewById(R.id.bt_approve_team);
                btRemoveMember1 = (Button) v.findViewById(R.id.bt_remove_member1);
                btRemoveMember2 = (Button) v.findViewById(R.id.bt_remove_member2);
                btRemoveMember3 = (Button) v.findViewById(R.id.bt_remove_member3);
                btRemoveMember4 = (Button) v.findViewById(R.id.bt_remove_member4);
                btRemoveMember5 = (Button) v.findViewById(R.id.bt_remove_member5);
                btAddMember = (Button) v.findViewById(R.id.bt_add_team_member);
            }
        }

        /**
         * A common class for showing prompt to delete members from team
         * **/
        private class DialogDelete extends AlertDialog.Builder{

            private String name = "";

            public DialogDelete(@NonNull Context context) {
                super(context);
            }

            public DialogDelete(@NonNull Context context, String name) {
                super(context);
                this.name = name;
            }

            void showDial(){
                this.setTitle("Confirm Delete")
                        .setMessage("Are you sure you want to Remove " + name +"?")
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // continue with add member

                                teamList = (ArrayList<Team>) createDeletedList(4);

                                ViewTeamActivity.ContactAdapter ca = new ViewTeamActivity.ContactAdapter(teamList);
                                recList.setAdapter(ca);

                                new AlertDialog.Builder(ViewTeamActivity.this)
                                        .setTitle("Member Deleted Successful")
                                        .setMessage("You have successfully deleted " + name)
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

            void addMember(){
                final EditText input = new EditText(ViewTeamActivity.this);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);
                input.setLayoutParams(lp);
                this.setView(input);

                this.setTitle("Add Member")
                        .setMessage("Enter the email below ")
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // continue with add
                                String email = input.getText().toString();
                                String title = "", msg = "";
                                if(isEmailValid(email)){
                                    title = "Member Added Successful";
                                    msg = "You have successfully added " + email;

                                    teamList = (ArrayList<Team>) newMemberList(4);
                                    ViewTeamActivity.ContactAdapter ca = new ViewTeamActivity.ContactAdapter(teamList);
                                    recList.setAdapter(ca);
                                } else {
                                    title = "Invalid";
                                    msg = "Member Email not valid";
                                }

                                new AlertDialog.Builder(ViewTeamActivity.this)
                                        .setTitle(title)
                                        .setMessage(msg)
                                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {

                                            }
                                        })
                                        .setIcon(android.R.drawable.ic_dialog_alert)
                                        .show();
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // refrain from adding new member

                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }

            /**
             * method is used for checking valid email id format.
             *
             * @param email
             * @return boolean true for valid false for invalid
             */
            public boolean isEmailValid(String email) {
                boolean isValid = false;

                String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
                CharSequence inputStr = email;

                Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
                Matcher matcher = pattern.matcher(inputStr);
                if (matcher.matches()) {
                    isValid = true;
                }
                return isValid;
            }
        }
    }


    private List<Team> createList(int size) {

        List<Team> result = new ArrayList<>();

        // 1
        Team ci = new Team();
        ci.Name = "Team Mavericks";
        ci.SportName = "Men's BasketBall";
        ci.members = new ArrayList<String>();
        ci.members.add("Kevin Durant");
        ci.members.add("Sha'quille O'Neill");
        ci.members.add("LeBron James");
        ci.members.add("Tim Duncan");
        ci.members.add("Ishwor Timilsina");
        ci.isApproved = true;
        result.add(ci);

        Team ci1 = new Team();
        ci1.Name = "Team batman";
        ci1.SportName = "Men's BasketBall";
        ci1.members = new ArrayList<String>();
        ci1.members.add("Clark Kent");
        ci1.members.add("Bruce Wayne");
        ci1.members.add("Ra's Al Ghul");
        ci1.members.add("Dennis O'Neill");
        ci.members.add("Ishwor Timilsina");
        ci1.isApproved = true;
        result.add(ci1);

        // 2
        Team c2 = new Team();
        c2.Name = "Invincibles";
        c2.SportName = "Men's Pro Football";
        c2.members = new ArrayList<String>();
        c2.members.add("Tony Romo");
        c2.members.add("Dak Prescott");
        c2.members.add("Dez Bryant");
        c2.members.add("Ezekiel Ezio");
        ci.members.add("Ishwor Timilsina");
        c2.isApproved = false;
        result.add(c2);
/*
        // 22
        Team c22 = new Team();
        c22.Name = "Team Chronicles";
        c22.SportName = "Men's Pro Football";
        c22.members = new ArrayList<String>();
        c22.members.add("Joe McAleen");
        c22.members.add("Calum GreyJoy");
        c22.members.add("Alyster Johnson");
        c22.members.add("Shawn King");
        c22.isApproved = false;
        result.add(c22);

        // 3
        Team c3 = new Team();
        c3.Name = "El Matador";
        c3.SportName = "Women's Volleyball";
        c3.members = new ArrayList<String>();
        c3.members.add("Danielle Esmeralda");
        c3.members.add("Alyssa Mendez");
        c3.members.add("Penelope Cruz");
        c3.isApproved = true;
        result.add(c3);

        // 31
        Team c31 = new Team();
        c31.Name = "Com'On Gals";
        c31.SportName = "Women's Volleyball";
        c31.members = new ArrayList<String>();
        c31.members.add("Mellina Campbell");
        c31.members.add("Zhou Cheng Liu");
        c31.members.add("Alina Neupane");
        c31.isApproved = true;
        result.add(c31);

        // 4
        Team c4 = new Team();
        c4.Name = "New Barcelona";
        c4.SportName = "Men's Soccer";
        c4.members = new ArrayList<String>();
        c4.members.add("Lionil Messier");
        c4.members.add("Louis Juarez");
        c4.members.add("Nemar");
        c4.members.add("Jordan Alban");
        c4.isApproved = true;
        result.add(c4);


        // 5
        Team c5 = new Team();
        c5.Name = "New Madrid";
        c5.SportName = "Men's Soccer";
        c5.members = new ArrayList<String>();
        c5.members.add("Ronald Christian");
        c5.members.add("Gary Baleth");
        c5.members.add("Segian Romo");
        c5.members.add("Karium Benzeme");
        c5.isApproved = true;
        result.add(c5);
*/
        return result;
    }


    private List<Team> createDeletedList(int size) {

        List<Team> result = new ArrayList<>();

        // 1
        Team ci = new Team();
        ci.Name = "Team Mavericks";
        ci.SportName = "Men's BasketBall";
        ci.members = new ArrayList<String>();
        ci.members.add("Sha'quille O'Neill");
        ci.members.add("LeBron James");
        ci.members.add("Tim Duncan");
        ci.members.add("Ishwor Timilsina");
        ci.isApproved = true;
        result.add(ci);

        Team ci1 = new Team();
        ci1.Name = "Team batman";
        ci1.SportName = "Men's BasketBall";
        ci1.members = new ArrayList<String>();
        ci1.members.add("Clark Kent");
        ci1.members.add("Bruce Wayne");
        ci1.members.add("Ra's Al Ghul");
        ci1.members.add("Dennis O'Neill");
        ci.members.add("Ishwor Timilsina");
        ci1.isApproved = true;
        result.add(ci1);

        // 2
        Team c2 = new Team();
        c2.Name = "Invincibles";
        c2.SportName = "Men's Pro Football";
        c2.members = new ArrayList<String>();
        c2.members.add("Tony Romo");
        c2.members.add("Dak Prescott");
        c2.members.add("Dez Bryant");
        c2.members.add("Ezekiel Ezio");
        ci.members.add("Ishwor Timilsina");
        c2.isApproved = false;
        result.add(c2);
/*
        // 22
        Team c22 = new Team();
        c22.Name = "Team Chronicles";
        c22.SportName = "Men's Pro Football";
        c22.members = new ArrayList<String>();
        c22.members.add("Joe McAleen");
        c22.members.add("Calum GreyJoy");
        c22.members.add("Alyster Johnson");
        c22.members.add("Shawn King");
        c22.isApproved = false;
        result.add(c22);

        // 3
        Team c3 = new Team();
        c3.Name = "El Matador";
        c3.SportName = "Women's Volleyball";
        c3.members = new ArrayList<String>();
        c3.members.add("Danielle Esmeralda");
        c3.members.add("Alyssa Mendez");
        c3.members.add("Penelope Cruz");
        c3.isApproved = true;
        result.add(c3);

        // 3
        Team c31 = new Team();
        c31.Name = "Com'On Gals";
        c31.SportName = "Women's Volleyball";
        c31.members = new ArrayList<String>();
        c31.members.add("Mellina Campbell");
        c31.members.add("Zhou Cheng Liu");
        c31.members.add("Alina Neupane");
        c31.isApproved = true;
        result.add(c31);

        // 4
        Team c4 = new Team();
        c4.Name = "New Barcelona";
        c4.SportName = "Men's Soccer";
        c4.members = new ArrayList<String>();
        c4.members.add("Lionil Messier");
        c4.members.add("Louis Juarez");
        c4.members.add("Nemar");
        c4.members.add("Jordan Alban");
        c4.isApproved = true;
        result.add(c4);


        // 5
        Team c5 = new Team();
        c5.Name = "New Madrid";
        c5.SportName = "Men's Soccer";
        c5.members = new ArrayList<String>();
        c5.members.add("Ronald Christian");
        c5.members.add("Gary Baleth");
        c5.members.add("Segian Romo");
        c5.members.add("Karium Benzeme");
        c5.isApproved = true;
        result.add(c5);
*/
        return result;
    }


    private List<Team> newMemberList(int size) {

        List<Team> result = new ArrayList<>();

        // 1
        Team ci = new Team();
        ci.Name = "Team Mavericks";
        ci.SportName = "Men's BasketBall";
        ci.members = new ArrayList<String>();
        ci.members.add("Sha'quille O'Neill");
        ci.members.add("LeBron James");
        ci.members.add("Tim Duncan");
        ci.members.add("Ishwor Timilsina");
        ci.isApproved = true;
        result.add(ci);

        Team ci1 = new Team();
        ci1.Name = "Team batman";
        ci1.SportName = "Men's BasketBall";
        ci1.members = new ArrayList<String>();
        ci1.members.add("Clark Kent");
        ci1.members.add("Bruce Wayne");
        ci1.members.add("Ra's Al Ghul");
        ci1.members.add("Dennis O'Neill");
        ci.members.add("Ishwor Timilsina");
        ci1.isApproved = true;
        result.add(ci1);

        // 2
        Team c2 = new Team();
        c2.Name = "Invincibles";
        c2.SportName = "Men's Pro Football";
        c2.members = new ArrayList<String>();
        c2.members.add("Tony Romo");
        c2.members.add("Dak Prescott");
        c2.members.add("Dez Bryant");
        c2.members.add("Ezekiel Ezio");
        ci.members.add("Ishwor Timilsina");
        c2.isApproved = false;
        result.add(c2);
/*
        // 22
        Team c22 = new Team();
        c22.Name = "Team Chronicles";
        c22.SportName = "Men's Pro Football";
        c22.members = new ArrayList<String>();
        c22.members.add("Joe McAleen");
        c22.members.add("Calum GreyJoy");
        c22.members.add("Alyster Johnson");
        c22.members.add("Shawn King");
        c22.isApproved = false;
        result.add(c22);

        // 3
        Team c3 = new Team();
        c3.Name = "El Matador";
        c3.SportName = "Women's Volleyball";
        c3.members = new ArrayList<String>();
        c3.members.add("Danielle Esmeralda");
        c3.members.add("Alyssa Mendez");
        c3.members.add("Penelope Cruz");
        c3.isApproved = true;
        result.add(c3);


        // 3
        Team c31 = new Team();
        c31.Name = "Com'On Gals";
        c31.SportName = "Women's Volleyball";
        c31.members = new ArrayList<String>();
        c31.members.add("Mellina Campbell");
        c31.members.add("Zhou Cheng Liu");
        c31.members.add("Alina Neupane");
        c31.isApproved = true;
        result.add(c31);

        // 4
        Team c4 = new Team();
        c4.Name = "New Barcelona";
        c4.SportName = "Men's Soccer";
        c4.members = new ArrayList<String>();
        c4.members.add("Lionil Messier");
        c4.members.add("Louis Juarez");
        c4.members.add("Nemar");
        c4.members.add("Jordan Alban");
        c4.isApproved = true;
        result.add(c4);


        // 5
        Team c5 = new Team();
        c5.Name = "New Madrid";
        c5.SportName = "Men's Soccer";
        c5.members = new ArrayList<String>();
        c5.members.add("Ronald Christian");
        c5.members.add("Gary Baleth");
        c5.members.add("Segian Romo");
        c5.members.add("Karium Benzeme");
        c5.isApproved = true;
        result.add(c5);


        // 5
        Team c6 = new Team();
        c6.Name = "New Liverpool";
        c6.SportName = "Men's Soccer";
        c6.members = new ArrayList<String>();
        c6.members.add("Stevie Gerrand");
        c6.members.add("Phil Coutirridge");
        c6.members.add("Danny Sturrinho");
        c6.members.add("Jordan Hendersson");
        c6.isApproved = true;
        result.add(c6);
*/
        return result;
    }
}

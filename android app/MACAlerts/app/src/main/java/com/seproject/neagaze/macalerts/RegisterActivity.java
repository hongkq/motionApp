package com.seproject.neagaze.macalerts;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

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

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {


    private static String URL =  Server.ADDR + "register";

    private AutoCompleteTextView mUtaID, mEmail, mFirstName, mLastName, mAddress, mMobile;
    private EditText mPwd, mRePwd;
    private Button mRegister;
    private RadioGroup radioSexGroup;
    private RadioButton radioSexButton;

    private RequestQueue MyRequestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mUtaID = (AutoCompleteTextView) findViewById(R.id.utaid);
        mEmail = (AutoCompleteTextView) findViewById(R.id.email);
        mFirstName = (AutoCompleteTextView) findViewById(R.id.firstname);
        mLastName = (AutoCompleteTextView) findViewById(R.id.lastname);
        mAddress = (AutoCompleteTextView) findViewById(R.id.address);
        mMobile = (AutoCompleteTextView) findViewById(R.id.mobile);
        mPwd = (EditText) findViewById(R.id.password);
        mRePwd = (EditText) findViewById(R.id.reenter_password);
        radioSexGroup = (RadioGroup) findViewById(R.id.radioSex);

        MyRequestQueue = Volley.newRequestQueue(RegisterActivity.this);

        mRegister = (Button) findViewById(R.id.email_register_button);

        mRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(true && isEmailValid(mEmail.getText().toString()) /*  && (mPwd.getText().toString() == mRePwd.getText().toString())*/) {
                    int selectedId = radioSexGroup.getCheckedRadioButtonId();

                    // find the radiobutton by returned id
                    radioSexButton = (RadioButton) findViewById(selectedId);

                    HashMap<String, String> par = new HashMap<String, String>();
                    par.put("utaid", mUtaID.getText().toString());
                    par.put("email", mEmail.getText().toString());
                    par.put("first", mFirstName.getText().toString());
                    par.put("last", mLastName.getText().toString());
                    par.put("mobile", mMobile.getText().toString());
                    par.put("address", mAddress.getText().toString());
                    par.put("gender", radioSexButton.getText().toString());
                    par.put("pwd", mPwd.getText().toString());
                    par.put("designation", "user");

                    JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, URL, new JSONObject(par),
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    try {
                                        VolleyLog.e("Response:%n %s", response.toString(4));
                                        showProgress(false);
                                        if(response.getBoolean("status")) {
                                            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                            startActivity(intent);
                                            finish();
                                        } else{

                                            Toast.makeText(RegisterActivity.this, "Please check the params again", Toast.LENGTH_SHORT).show();
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            VolleyLog.e("Error: ", error.getMessage());

                            showProgress(false);
                        }
                    });

                    MyRequestQueue.add(req);

                    new AlertDialog.Builder(RegisterActivity.this)
                            .setTitle("Register Success")
                            .setMessage("You have successfully Registered")
                            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // continue with delete
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                } else {
                    Toast.makeText(RegisterActivity.this, "Please make sure the params are correct", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);
/*
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
            */
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
         //   mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
         //   mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }
}

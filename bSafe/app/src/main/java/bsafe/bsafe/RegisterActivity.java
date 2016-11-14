package bsafe.bsafe;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import bsafe.bsafe.LogIn;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * A registration screen that offers registration via email/password.
 */
public class RegisterActivity extends AppCompatActivity{

    /**
     * Decaring the fields
     */;

    // UI references.
    private AutoCompleteTextView mEmailView;
    private AutoCompleteTextView mFnameView;
    private AutoCompleteTextView mLnameView;
    private EditText mPasswordView;
    private EditText mPasswordView2;
    private View mProgressView;
    private View mLoginFormView;
    private String database ="http://bsafe.azurewebsites.net";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        // Set up the login form.
        mEmailView = (AutoCompleteTextView) findViewById(R.id.Email);
        mFnameView = (AutoCompleteTextView) findViewById(R.id.Fname);
        mLnameView = (AutoCompleteTextView) findViewById(R.id.Lname);
        mPasswordView = (EditText) findViewById(R.id.Password);
        mPasswordView2 = (EditText) findViewById(R.id.Password2);

        Button RegisterButton = (Button) findViewById(R.id.register_button_button);
        RegisterButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Register();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
    }



    /**
     * Attempts to Register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void Register() {
        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);
        mPasswordView2.setError(null);
        mFnameView.setError(null);
        mLnameView.setError(null);
        // Store values at the time of the Register attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();
        String password2= mPasswordView2.getText().toString();
        String [] name={mFnameView.getText().toString(), mLnameView.getText().toString()};

        boolean cancel = false;
        View focusView = null;
        //Check if the User entered his name
        if (TextUtils.isEmpty(name[0]))
        {
            mFnameView.setError(getString(R.string.error_field_required));
            focusView = mFnameView;
            cancel = true;
        }
        else if (TextUtils.isEmpty(name[1]))
        {
            mLnameView.setError(getString(R.string.error_field_required));
            focusView = mLnameView;
            cancel = true;
        }
        else if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }
        // Check for a valid passwords, if the user entered one.
        else if (TextUtils.isEmpty(password2))
        {
            mPasswordView2.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView2;
            cancel = true;
        }
        //Check if the password match
        else if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }
        else if (!password.equals(password2))
        {
            mPasswordView2.setError("Password Doesn't Match");
            focusView = mPasswordView2;
            cancel = true;
        }
        // Check for a valid email address.

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
          //  showProgress(true);
            Map<String,String> JsonPushe=new HashMap<>();
            JsonPushe.put("firstName",name[0]);
            JsonPushe.put("lastName",name[1]);
            JsonPushe.put("email",email);
            JsonPushe.put("password",password);
            JsonObjectRequest jsonRequest = new JsonObjectRequest
                    (Request.Method.POST, database+"/users", new JSONObject(JsonPushe), new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            //the response is already constructed as a JSONObject!
                        }
                    }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            error.printStackTrace();
                        }
                    });
            Volley.newRequestQueue(getApplicationContext()).add(jsonRequest);
            Intent intent = new Intent(RegisterActivity.this, LogIn.class);
            startActivity(intent);
        }
    }

    private boolean isEmailValid(String email) {
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        return password.length() > 4;
    }

    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mEmail;
        private final String mPassword;



        UserLoginTask(String[] name,String email, String password) {
            mEmail = email;
            mPassword = password;

        }

        @Override
        protected Boolean doInBackground(Void... params) {


            return true;
        }


    }
}

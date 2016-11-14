package bsafe.bsafe;
import org.json.*;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import android.content.Intent;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.Response;
import java.util.HashMap;
import java.util.Map;
import bsafe.bsafe.RegisterActivity;
import com.android.volley.*;
/**
 * A login screen that offers login via email/password.
 */
public class LogIn extends AppCompatActivity{

    /**
     * Id to identity READ_CONTACTS permission request.
     */

    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
//    private UserLoginTask mAuthTask = null;

    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private String database ="http://bsafe.azurewebsites.net";
    private boolean sucsess= false;
    private String [] User=new String [3];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        // Set up the login form.
        mEmailView = (AutoCompleteTextView) findViewById(R.id.Username);

        mPasswordView = (EditText) findViewById(R.id.password);
        //Init the Log in Button
        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });
        // Init the Register Button
        Button mRegister = (Button) findViewById(R.id.register_button);
        mRegister.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {attemptRegister(view);
            }
        });

    }
    public void attemptRegister(View view)
    {
       Intent intent = new Intent(LogIn.this, RegisterActivity.class);
       startActivity(intent);
    }
    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Create a JSON Object to pass to the azure Server
            Map <String,String> JsonResponse=new HashMap<>();
            JsonResponse.put("email", email);
            JsonResponse.put("password", password);
            ;
            JsonObjectRequest jsonRequest = new JsonObjectRequest
                    (Request.Method.POST, database+"/users/login", new JSONObject(JsonResponse), new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            // the response is already constructed as a JSONObject!
                            try {
                                User[0] = response.getString("_id");
                                User[1]= response.getString("firstName");
                                User[2]= response.getString("lastName");
                                sucsess=true;
                                antilag();

                            } catch (JSONException e) {
                                e.printStackTrace();

                            }
                        }
                    }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            error.printStackTrace();
                        }
                    });
            Volley.newRequestQueue(getApplicationContext()).add(jsonRequest);




        }
    }
    private void antilag()
    {
        if(sucsess)
        {
            Intent intent = new Intent(LogIn.this, Ride.class);
            intent.putExtra("Name", User[1]+ " "+ User[2]);
            intent.putExtra("ID", User[0]);
            sucsess=false;
            startActivity(intent);
        }
        else
        {
            TextView Error = (TextView) findViewById(R.id.Error);
            Error.setText("The Username/Password Combination is Incorrect");
            sucsess=false;
        }
    }
    private boolean isEmailValid(String email) {
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        return password.length() > 4;
    }

    }


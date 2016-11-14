package bsafe.bsafe;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.widget.TextView;
import java.util.HashMap;
import java.util.Map;



public class mRequest extends Fragment {

    private Button ReqRide;
    public String[] name;
    private String database ="http://bsafe.azurewebsites.net";
    private String [] location;
    private String [] destination=new String[2];
    private String Partner="";
    private Map<String,String> RequestId=new HashMap<>();
    private Double [] MeetUp=new Double[2];
    private boolean control=false;
    private JSONObject debug;
    public mRequest() {
        // Required empty public constructor
    }


    public static mRequest newInstance() {
        mRequest fragment = new mRequest();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    public void attemptRequest (){
        name=getArguments().getStringArray("UserInfo");
        location=getArguments().getStringArray("Location");
        EditText textlat=(EditText) getActivity().findViewById(R.id.Dest1);
        EditText textlong=(EditText) getActivity().findViewById(R.id.Dest2);
        textlat.setError(null);
        textlong.setError(null);
        String strlat=textlat.getText().toString();
        String strlong=textlong.getText().toString();

        if(TextUtils.isEmpty(strlat))
        {
            textlat.setError("Not Valid Input");
        }
        else if(TextUtils.isEmpty(strlong))
        {
            textlong.setError("Not Valid Input");
        }
        else if(ReqRide.getText().equals("Cancel")) {
            ReqRide.setBackgroundColor(Color.LTGRAY);
            ReqRide.setText("Request");
        }
        else
        {
            ReqRide.setBackgroundColor(Color.RED);
            ReqRide.setText("Cancel");
            destination[0]=strlat;
            destination[1]=strlong;
            initial();
            findmatch myTask = new findmatch();
            myTask.execute();



        }}

    public void initial()
    {
        Map<String,Object> JsonResponse=new HashMap<>();
        JsonResponse.put("userId", (String) name[1]);
        JsonResponse.put("originLng",Float.parseFloat(location[1]));
        JsonResponse.put("originLat", Float.parseFloat(location[0]));
        JsonResponse.put("destinationLng", Float.parseFloat(destination[0]));
        JsonResponse.put("destinationLat", Float.parseFloat(destination[1]));
        JsonObjectRequest jsonRequest = new JsonObjectRequest
                (Request.Method.POST, "http://bsafe.azurewebsites.net/requests", new JSONObject(JsonResponse), new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // the response is already constructed as a JSONObject!
                        debug=response;
                        try {

                            RequestId.put("_id",response.getString("id"));


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
        Volley.newRequestQueue(getActivity()).add(jsonRequest);

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootview = inflater.inflate(R.layout.fragment_request, container, false);
        ReqRide = (Button) rootview.findViewById(R.id.Request);
        ReqRide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptRequest();
            }
        });
        return rootview;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    class findmatch extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            JsonObjectRequest jsonRequest2 = new JsonObjectRequest
                    (Request.Method.POST, database + "/matches", new JSONObject(RequestId), new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            // the response is already constructed as a JSONObject!
                            JSONObject temp;
                            try {
                                temp = response;
                                JSONArray temp2 = temp.getJSONArray("requestIds");
                                if (temp2 != null) {
                                    int len = temp2.length();
                                    for (int i = 0; i < len; i++) {
                                        if (!name[1].equals(temp2.get(i).toString()))
                                            Partner = temp2.get(i).toString();

                                    }
                                }
                                MeetUp[0] = temp.getDouble("meetupLat");
                                MeetUp[1] = temp.getDouble("meetupLng");
                                control = true;
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
            Volley.newRequestQueue(getActivity().getApplicationContext()).add(jsonRequest2);
            return null;
        }

        protected void onPostExecute() {
            if(!control)
            {
                SystemClock.sleep(1000);
                doInBackground();
            }
            else
            {
                TextView info= (TextView) getActivity().findViewById(R.id.PartnerName);
                info.setText(name[1]);
                TextView info2=(TextView) getActivity().findViewById(R.id.PartnerMeet);
                info.setText("" + Math.round(MeetUp[0]) + "," + Math.round(MeetUp[1]));

            }
        }
    }

}


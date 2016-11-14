package bsafe.bsafe;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.view.ViewGroup;
import android.media.MediaRecorder;
import java.io.IOException;
import android.os.Environment;
import android.telephony.SmsManager;
import android.content.Intent;
/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link mRecord.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link mRecord#newInstance} factory method to
 * create an instance of this fragment.
 */
public class mRecord extends Fragment {
    //Listner that allows fragment to communicate to Parent
    private OnFragmentInteractionListener mListener;
    private Button RecButton;
    private Button StopRecButton;
    private MediaRecorder mRecorder = null;
    private Button PolCall;
    private Button PolText;
    private Button ErmCall;
    private Button ErmText;
    private String id;
    public mRecord() {
        // Required empty public constructor
    }

    public static mRecord newInstance() {
        mRecord fragment = new mRecord();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    private void startRecording() {
        try {
            mRecorder.prepare();
        } catch (IOException e) {
        }

        mRecorder.start();
    }

    private void stopRecording() {
        mRecorder.stop();
        mRecorder.release();
        mRecorder = null;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_record, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        RecButton=(Button) getActivity().findViewById(R.id.Record);
        RecButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startRecording();
            }
        });
        StopRecButton = (Button) getActivity().findViewById(R.id.Save);
        StopRecButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopRecording();
            }});
        PolCall=(Button) getActivity().findViewById(R.id.Call911);
        PolCall.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent phoneIntent = new Intent(Intent.ACTION_CALL);
                phoneIntent.setData(Uri.parse("tel:281-494-6968"));
                startActivity(phoneIntent);
            }});
        PolText=(Button) getActivity().findViewById(R.id.Text911);
        PolText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage("8326286104", null, "Urgent Assitance Required", null, null);
            }});
        ErmCall=(Button) getActivity().findViewById(R.id.ErmCall);
        ErmCall.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent phoneIntent = new Intent(Intent.ACTION_CALL);
                phoneIntent.setData(Uri.parse("tel:281-494-6968"));
                startActivity(phoneIntent);
            }});
        ErmText=(Button) getActivity().findViewById(R.id.ErmText);
        ErmText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage("8326286104", null, "Urgent Assitance Required", null, null);
            }});
        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        String path=Environment.getExternalStorageDirectory().getAbsolutePath();
        mRecorder.setOutputFile(path + "/"+ System.currentTimeMillis()+"testing.3gp");
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
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
    public interface OnFragmentInteractionListener {

    }
}

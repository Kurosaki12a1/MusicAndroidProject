package com.bku.jobs.Fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.bku.jobs.Activity.JobDetailActivity;
import com.bku.jobs.Activity.MainScreenActivity;
import com.bku.jobs.Adapter.JobsAdapter;
import com.bku.jobs.Models.JobInfo;
import com.bku.jobs.R;
import com.bku.jobs.Util.HttpHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

/**
 * Created by Welcome on 5/21/2018.
 */

public class HomeFragment extends Fragment implements AdapterView.OnItemClickListener{

    private String TAG = HomeFragment.class.getSimpleName();
    private ListView listView;
    private static String url = "https://jobs.github.com/positions.json";
    ArrayList<JobInfo> jobsList;
    public ProgressBar mProgressBar;
    private JobsAdapter jobsAdapter;
    private FragmentManager fragmentManager;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private OnFragmentInteractionListener mListener;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Intent intent = new Intent(getActivity(), JobDetailActivity.class);
        intent.putExtra("jobTitle", jobsList.get(i).getJobTitle());
        intent.putExtra("company", jobsList.get(i).getCompany());
        intent.putExtra("jobType", jobsList.get(i).getType());
        intent.putExtra("location", jobsList.get(i).getLocation());
        intent.putExtra("jobCreated", jobsList.get(i).getJobCreatedAt());
        intent.putExtra("jobDescription", jobsList.get(i).getDescription());
        intent.putExtra("howToApply", jobsList.get(i).getHowToApply());
        startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);

    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        bindView();
        super.onViewCreated(view, savedInstanceState);
        new GetJobs().execute();
        listView.setOnItemClickListener(this);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
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

    public void bindView() {
        jobsList = new ArrayList<>();
        listView = (ListView) getView().findViewById(R.id.jobsList);
        mProgressBar = (ProgressBar) getView().findViewById(R.id.progressBar);
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
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private class GetJobs extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }


        @Override
        protected Void doInBackground(Void... voids) {
            HttpHandler sh = new HttpHandler();
            String jsonStr = sh.makeServiceCall(url);
            Log.e(TAG, "Response from url: " + jsonStr );
            if (jsonStr != null){
                try {
                    JSONArray jobs = new JSONArray(jsonStr);

                    for (int i=0; i<jobs.length();i++){
                        JSONObject j = jobs.getJSONObject(i);
                        JobInfo job = new JobInfo();
                        job.setJobId(j.getString("id"));
                        job.setJobCreatedAt(j.getString("created_at"));
                        job.setJobTitle(j.getString("title"));
                        job.setLocation(j.getString("location"));
                        job.setType(j.getString("type"));
                        job.setDescription(j.getString("description"));
                        job.setCompany(j.getString("company"));
                        job.setCompanyURL(j.getString("company_url"));
                        job.setCompanyLogo(j.getString("company_logo"));
                        job.setURL(j.getString("url"));
                        jobsList.add(i, job);
                    }
                }catch (final JSONException e){
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    getActivity().runOnUiThread(new Runnable(){
                        @Override
                        public void run() {
                            Toast.makeText(getActivity().getApplicationContext(),"Json parsing error: " + e.getMessage(),Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }else {
                Log.e(TAG, "Couldn't get json from server.");
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity().getApplicationContext(),
                                "Couldn't get json from server!",
                                Toast.LENGTH_LONG)
                                .show();
                    }
                });
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            /**
             * Updating parsed JSON data into ListView
             * */

            jobsAdapter = new JobsAdapter(getActivity().getApplicationContext(),jobsList);
            listView.setAdapter(jobsAdapter);
            mProgressBar.setVisibility(View.GONE);
        }

    }

}

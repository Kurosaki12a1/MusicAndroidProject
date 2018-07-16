package com.bku.jobs.Fragment;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.icu.util.TimeUnit;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.bku.jobs.API.APIService;
import com.bku.jobs.Adapter.SearchJobAdapter;
import com.bku.jobs.ModelData.JobData;
import com.bku.jobs.R;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

import static android.content.ContentValues.TAG;

/**
 * Created by Welcome on 5/21/2018.
 */

public class SearchFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Button searchBtn;
    private EditText searchTxt;
    private RecyclerView resultList;
    private SearchJobAdapter searchAdapter;
    private OnFragmentInteractionListener mListener;
    private ArrayList<JobData> jobArrayList;
    private ProgressDialog pDialog;

    String [] splitString;

    APIService apiService;

    String strSearch;
    String url="https://jobs.github.com";


    public SearchFragment() {
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
    public static SearchFragment newInstance(String param1, String param2) {
        SearchFragment fragment = new SearchFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
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
        return inflater.inflate(R.layout.fragment_search, container, false);

    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        bindView();
        super.onViewCreated(view, savedInstanceState);

        Retrofit retrofit=new Retrofit.Builder().baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();

         apiService=retrofit.create(APIService.class);


        setKeyListener();


      /*  searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(searchTxt.getText().toString().isEmpty()){
                    searchTxt.setError("Keyword is empty");
                    return;
                }
                new GetSearchResult().execute();

            }
        });*/
    }

    private void setKeyListener(){
        searchTxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
             // getListSearch(apiService);
              //  getDataLocation(apiService);
            }

            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void afterTextChanged(Editable s) {

                getDataLocation(apiService);
            }
        });
    }

    private void  getDataLocation(APIService apiService) {
     //   jobArrayList=new ArrayList<>();
        if(searchTxt.getText().toString().contains(";")) {
            splitString=searchTxt.getText().toString().split(";");
            strSearch=splitString[0];
        }
        else strSearch=searchTxt.getText().toString();
        apiService.getSearchData(strSearch)
                .flatMap(new Func1<List<JobData>, Observable<JobData>>() {
                    @Override
                    public Observable<JobData> call(List<JobData> jobData) {
                        jobArrayList=new ArrayList<>();
                        return Observable.from(jobData);
                    }
                }).filter(new Func1<JobData, Boolean>() {
             @Override
             public Boolean call(JobData jobData) {
                 return !searchTxt.getText().toString().contains(";")|| jobData.getLocation().contains(splitString[1]);
             }
         })
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
         .subscribe(new Subscriber<JobData>() {

             @Override
             public void onCompleted() {
                 searchAdapter = new SearchJobAdapter(getActivity(), jobArrayList);
                 LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
                 layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                 resultList.setLayoutManager(layoutManager);
                 resultList.setAdapter(searchAdapter);
             }

             @Override
             public void onError(Throwable e) {

             }

             @Override
             public void onNext(JobData jobData) {
                 jobArrayList.add(jobData);
             }
         });
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void getListSearch(APIService apiService){
        Observable<List<JobData>> observable=apiService.getSearchData(searchTxt.getText().toString())
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());

        //delay 100ms mỗi lần seach
        observable.debounce(100, java.util.concurrent.TimeUnit.MILLISECONDS)
                    .subscribe(new Observer<List<JobData>>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            Toast.makeText(getActivity(),"Opps , there is an error ... ",Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onNext(List<JobData> jobData2) {
                            jobArrayList=new ArrayList<>();
                            jobArrayList.addAll(jobData2);
                            searchAdapter = new SearchJobAdapter(getActivity(), jobArrayList);
                            LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
                            layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                            resultList.setLayoutManager(layoutManager);
                            resultList.setAdapter(searchAdapter);
                        }
                    });




    }

    private void getFilter(){

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
        searchBtn = getView().findViewById(R.id.searchBtn);
        searchTxt = getView().findViewById(R.id.searchTxt);
        resultList =getView().findViewById(R.id.result_list);
        jobArrayList = new ArrayList<>();
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


   /* private class GetSearchResult extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            jobArrayList.clear();
            HttpHandler sh = new HttpHandler();

            // Making a request to url and getting response
            String url =  "https://jobs.github.com/positions.json?description="+searchTxt.getText().toString();
            String jsonStr = sh.makeServiceCall(url);

            if (jsonStr != null) {
                try {
                    JobInfo tempJob;
                    JSONArray jsonArray = new JSONArray(jsonStr);
                    for (int i=0;i<jsonArray.length();i++){
                        JSONObject temp = new JSONObject();
                        temp=jsonArray.getJSONObject(i);
                        tempJob = new JobInfo(temp.getString("id"),temp.getString("created_at"),temp.getString("title"),temp.getString("location"), temp.getString("type"), temp.getString("description"),temp.getString("how_to_apply"),temp.getString("company"),temp.getString("company_url"),temp.getString("company_logo"),temp.getString("url"));
                        jobArrayList.add(tempJob);
                        String id = temp.getString("title");
                        Log.d("1abc","id thư: "+i+" "+id);
                    }
                } catch (final JSONException e) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getActivity(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG)
                                    .show();
                        }
                    });

                }
            } else {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity(),
                                "Couldn't get json from server. Check LogCat for possible errors!",
                                Toast.LENGTH_LONG)
                                .show();
                    }
                });

            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            if (pDialog.isShowing())
                pDialog.dismiss();
            if(jobArrayList.size()==0){
                Toast.makeText(getActivity(),"There are no results that match your search",Toast.LENGTH_SHORT).show();
            }else {
                searchAdapter = new SearchJobAdapter(getActivity(), jobArrayList);
                LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
                layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                resultList.setLayoutManager(layoutManager);
                resultList.setAdapter(searchAdapter);
            }
        }

    }*/
}

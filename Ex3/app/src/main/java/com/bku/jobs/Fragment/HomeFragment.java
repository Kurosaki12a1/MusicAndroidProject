package com.bku.jobs.Fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.content.Context;
import android.net.Uri;
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
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bku.jobs.API.APIService;
import com.bku.jobs.Activity.JobDetailActivity;
import com.bku.jobs.Adapter.JobsAdapter;
import com.bku.jobs.Adapter.UserDataAdapter;
import com.bku.jobs.DataZip;
import com.bku.jobs.ModelData.JobData;
import com.bku.jobs.ModelData.UserData.Name;
import com.bku.jobs.ModelData.UserData.Picture;
import com.bku.jobs.ModelData.UserData.ResultsItem;
import com.bku.jobs.ModelData.UserData.UserData;
import com.bku.jobs.R;
import com.bku.jobs.Util.UtilityJob;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.schedulers.Schedulers;

/**
 * Created by Welcome on 5/21/2018.
 */

public class HomeFragment extends Fragment implements AdapterView.OnItemClickListener{

    private String TAG = HomeFragment.class.getSimpleName();
    private ListView listView;
    private static String url = "https://jobs.github.com";
    private static String urlData="https://randomuser.me";
  //  ArrayList<JobInfo> jobsList;
    public ProgressBar mProgressBar;
    private JobsAdapter jobsAdapter;
    private FragmentManager fragmentManager;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    // TODO: Rename and change types of parameters
    private boolean _isAccess;
    private String mParam2;
    private OnFragmentInteractionListener mListener;

    ArrayList<JobData> jobDataLst;

    Retrofit retrofit;

    Retrofit retrofit1;

    ArrayList<ResultsItem> lstResultsItem;

    Observable<List<JobData>> observable;

    Observable<UserData> observableUserData;

    Subscription subscription;

    APIService apiService;

    APIService apiService1; //for UserData

    ArrayList<JobData> jobData;

    Subscription subscription1;

    TextView txtError;

    Button btnRetry;

    RecyclerView recyclerView;

    Subscription subscription2; //this for something more bigger




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
    public static HomeFragment newInstance(boolean param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putBoolean(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Intent intent = new Intent(getActivity(), JobDetailActivity.class);
        intent.putExtra("jobTitle", jobDataLst.get(i).getTitle());
        intent.putExtra("company", jobDataLst.get(i).getCompany());
        intent.putExtra("jobType", jobDataLst.get(i).getType());
        intent.putExtra("location", jobDataLst.get(i).getLocation());
        intent.putExtra("jobCreated", jobDataLst.get(i).getCreatedAt());
        intent.putExtra("jobDescription", jobDataLst.get(i).getDescription());
        intent.putExtra("howToApply",jobDataLst.get(i).getHowToApply());
        UtilityJob utilityJob=UtilityJob.getInstance();
        utilityJob.setJobInfo(jobDataLst.get(i));
        startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            _isAccess = getArguments().getBoolean(ARG_PARAM1);
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

        //Thời gian update adapter  phải lớn hơn thời gian load xong cái update Data đầu tiên đã
        //Nếu không xảy ra lỗi luồng đè lẫn nhau


           /* subscription = Observable.interval(10, TimeUnit.SECONDS).subscribe(new Subscriber<Long>() {
                @Override
                public void onCompleted() {
                    //Không biết khi nào vô đây nữa.....
                }
                @Override
                public void onError(Throwable e) {

                }
                @Override
                public void onNext(Long aLong) {
                    int time = (int) (long) aLong;
                    switch (time) {
                        case 0:
                            updateData(15);
                            break;
                        case 1:
                            updateData(20);
                            break;
                        case 2:
                            updateData(25);
                            break;
                        case 3:
                            updateData(30);
                            break;
                        case 4:
                            updateData(35);
                            break;
                        case 5:
                            subscription.unsubscribe();
                            break;
                        default:
                            break;

                    }
                }
            });*/

        bindRecycleView();
        listView.setOnItemClickListener(this);
        /*btnRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(),"Try again....",Toast.LENGTH_SHORT).show();
                txtError.setVisibility(View.GONE);
                btnRetry.setVisibility(View.GONE);
                listView.setVisibility(View.VISIBLE);
                callSubscriptsTon();
            }
        });*/
    }

    private void bindRecycleView(){
        jobDataLst=new ArrayList<>();
        ArrayList<String> listTypeJob=new ArrayList<>();
        listTypeJob.add("Java Developer");
        listTypeJob.add("Python Developer");
        listTypeJob.add("Android Developer");
        listTypeJob.add("IOS Developer");
        listTypeJob.add("Scala Developer");
        listTypeJob.add("C++ Developer");
        listTypeJob.add("C# Developer");
        listTypeJob.add("C Developer");
        listTypeJob.add("Database Developer");
        listTypeJob.add("Ruby Developer");
        for(int i=0 ; i <10;i++){
            JobData jobData=new JobData();
            jobData.setId("00"+i);
            jobData.setCompany("Tập Đoàn IT thứ "+ i);
            jobData.setUrl("www.google.com.vn");
            jobData.setCompanyUrl("www.google.com.vn");
            jobData.setTitle(listTypeJob.get(i));
            jobData.setCreatedAt("30/02/2018");
            jobData.setCompanyLogo("");
            jobData.setDescription("Công ty chém gió về công nghệ " + listTypeJob.get(i));
            jobData.setHowToApply("www.google.com.vn");
            jobData.setLocation("Thành Phố Hồ Chí Minh");
            jobData.setType("Full Time");
            jobDataLst.add(jobData);
        }

       /* UtilityJob utilityJob=UtilityJob.getInstance();
        utilityJob.setJobData(jobDataLst);*/
        jobsAdapter = new JobsAdapter(getActivity(),jobDataLst);
        listView.setAdapter(jobsAdapter);
        mProgressBar.setVisibility(View.GONE);


    }

    private void callSubscriptsTon(){
        Observable.interval(5, TimeUnit.SECONDS).subscribe(new Subscriber<Long>() {
            @Override
            public void onCompleted() {
                //Không biết khi nào vô đây nữa.....
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Long aLong) {
                int time = (int) (long) aLong;
                switch (time) {
                    case 0:
                        updateData(15);
                        break;
                    case 1:
                        updateData(20);
                        break;
                    case 2:
                        updateData(25);
                        break;
                    case 3:
                        updateData(30);
                        break;
                    case 4:
                        updateData(35);
                        break;
                    case 5:
                        subscription.unsubscribe();
                        break;

                    case 6:
                        updateData(40);
                        break;
                    default:
                        break;

                }
            }
        });

    }

    private void updateData(final int size){
         retrofit=new Retrofit.Builder().baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();

         retrofit1=new Retrofit.Builder().baseUrl(urlData)
                 .addConverterFactory(GsonConverterFactory.create())
                 .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                 .build();



        apiService=retrofit.create(APIService.class);

        apiService1=retrofit1.create(APIService.class);

        observable=apiService.getJobData();

        observableUserData=apiService1.getUserData("10");

/*
        subscription1=observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<JobData>>() {
            @Override
            public void onCompleted() {
                Toast.makeText(getActivity(),"Update new  " + size + " Job ",Toast.LENGTH_SHORT).show();
                setRecycleViewAdapter(size);

            }


            @Override
            public void onError(Throwable e) {
                Toast.makeText(getActivity(),"Check your connection or maybe issue from our server... ",Toast.LENGTH_SHORT).show();
                txtError.setText("SORRY , THERE IS AN ERROR TO LOAD JOBS....");
                txtError.setVisibility(View.VISIBLE);
                btnRetry.setVisibility(View.VISIBLE);
                listView.setVisibility(View.GONE);
                subscription.unsubscribe();
            }

            @Override
            public void onNext(List<JobData> jobData2) {
                    jobDataLst = new ArrayList<>();
                    jobDataLst.addAll(jobData2);

            }
        });
*/

        Observable.zip(observable, observableUserData, new Func2<List<JobData>, UserData, DataZip>() {

            @Override
            public DataZip call(List<JobData> jobData, UserData userData) {
                return new DataZip(jobData,userData);
            }
        }).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<DataZip>() {
            @Override
            public void onCompleted() {
               // Toast.makeText(getActivity(),"Update new  " + size + " Job ",Toast.LENGTH_SHORT).show();
                jobsAdapter = new JobsAdapter(getActivity(),jobDataLst);
                listView.setAdapter(jobsAdapter);
                mProgressBar.setVisibility(View.GONE);
                UserDataAdapter userDataAdapter=new UserDataAdapter(getContext(),lstResultsItem);
                recyclerView.setAdapter(userDataAdapter);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(DataZip dataZip) {
                jobDataLst = new ArrayList<>();
                jobDataLst.addAll(dataZip.getJobData());
                lstResultsItem=new ArrayList<>(dataZip.getUserData().getResults());
            }
        });


    }


   /* private Subscription getUserData(){
        return observableUserData.subscribe(new Observer<UserData>() {
            @Override
            public void onCompleted() {
                UserDataAdapter userDataAdapter=new UserDataAdapter(getContext(),lstResultsItem);
                recyclerView.setAdapter(userDataAdapter);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(UserData userData) {
                lstResultsItem=new ArrayList<>(userData.getResults());
            }
        });
    }*/



    //Hàm này chỉ lấy 5 phần tử
    private void setRecycleViewAdapter(int size){
        jobData=new ArrayList<>();
        //con số limit là ý chỉ lấy số xx phần tử của trang . Có thể lựa chọn trang này nọ <=size
        Observable.from(jobDataLst).limit(size).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<JobData>() {
                    @Override
                    public void onCompleted() {
                        jobsAdapter = new JobsAdapter(getActivity(),jobData);
                        listView.setAdapter(jobsAdapter);
                        mProgressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError(Throwable e) {
                      /* Toast.makeText(getActivity(),"ERROR ON THIS SHIT.... ",Toast.LENGTH_SHORT).show();
                        txtError.setTextColor(getResources().getColor(R.color.colorWhite));*/

                    }

                    @Override
                    public void onNext(JobData jobData1) {
                            jobData.add(jobData1);
                    }
                });
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


    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
    }

    public void bindView() {
        btnRetry=(Button)getView().findViewById(R.id.btnRetry);
        txtError=(TextView)getView().findViewById(R.id.errorText);
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


    /*private class GetJobs extends AsyncTask<Void, Void, Void> {
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
                        job.setHowToApply(j.getString("how_to_apply"));
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
            *//**
             * Updating parsed JSON data into ListView
             * *//*


        }

    }*/

}

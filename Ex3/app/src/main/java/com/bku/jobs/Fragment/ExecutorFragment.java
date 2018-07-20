package com.bku.jobs.Fragment;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.bku.jobs.API.APIService;
import com.bku.jobs.Adapter.FavoriteJobAdapter;
import com.bku.jobs.Adapter.UserDataAdapter;
import com.bku.jobs.CustomThreadExeccutor.CustomCallable;
import com.bku.jobs.CustomThreadExeccutor.CustomHandlerThread;
import com.bku.jobs.CustomThreadExeccutor.CustomRunnable;
import com.bku.jobs.CustomThreadExeccutor.CustomThreadPoolManager;
import com.bku.jobs.CustomThreadExeccutor.UiThreadCallback;
import com.bku.jobs.CustomThreadExeccutor.Util;
import com.bku.jobs.Database.OfflineDatabaseHelper;
import com.bku.jobs.ModelData.JobData;
import com.bku.jobs.ModelData.UserData.ResultsItem;
import com.bku.jobs.ModelData.UserData.UserData;
import com.bku.jobs.R;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Observable;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

import static com.bku.jobs.Fragment.ProfileFragment.TAG;

/**
 * Created by Welcome on 7/18/2018.
 */

public class ExecutorFragment   extends Fragment implements UiThreadCallback {

    // A worker thread which has the same lifecycle with the activity
    // It is created and started in Activity onStart and stopped in Activity onStop
    private CustomHandlerThread mHandlerThread;

    // The handler for the UI thread. Used for handling messages from worker threads.
    private UiHandler mUiHandler;

    // A text view to show messages sent from work threads
    @BindView(R.id.display) TextView mDisplayTextView;
    @BindView(R.id.send_msg_1) Button send_msg_1;
    @BindView(R.id.send_msg_2) Button send_msg_2;
    @BindView(R.id.send_msg_3) Button send_msg_3;
    @BindView(R.id.send_msg_4) Button send_msg_4;
    @BindView(R.id.send_msg_7) Button send_msg_7;
    @BindView(R.id.clear) Button clear;
    // A thread pool manager
    // It is a static singleton instance by design and will survive activity lifecycle
    private CustomThreadPoolManager mCustomThreadPoolManager;


    private static String urlData="https://randomuser.me";

    ArrayList<ResultsItem> listResultRandomUser;


    rx.Observable<UserData> observableUserData;
    private final ThreadPoolExecutor threadPoolExecutor=new ThreadPoolExecutor(1,1,20L, TimeUnit.SECONDS
    ,new LinkedBlockingDeque<Runnable>());

    private FavoriteFragment.OnFragmentInteractionListener mListener;

    public ExecutorFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

   /* private Runnable getRunnable(final int i){
        return new Runnable() {
            @Override
            public void run() {

                Retrofit retrofit=new Retrofit.Builder().baseUrl(urlData)
                            .addConverterFactory(GsonConverterFactory.create())
                            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                            .build();

                APIService apiService=retrofit.create(APIService.class);
                observableUserData=apiService.getUserData("2500");
                observableUserData.subscribeOn(Schedulers.io())
                         .observeOn(AndroidSchedulers.mainThread())
                         .subscribe(new Observer<UserData>() {
                             @Override
                             public void onCompleted() {
                                    switch (i){
                                        case 0 :
                                            Toast.makeText(getActivity(),"Đã Tạo RecycleView lần 1",Toast.LENGTH_SHORT).show();
                                            UserDataAdapter userDataAdapter=new UserDataAdapter(getContext(),listResultRandomUser);
                                            recyclerView1.setLayoutManager(new LinearLayoutManager(getActivity()));
                                            recyclerView1.setAdapter(userDataAdapter);
                                            progressBar1.setVisibility(View.GONE);
                                            break;
                                        case 1:
                                            Toast.makeText(getActivity(),"Đã Tạo RecycleView lần 2",Toast.LENGTH_SHORT).show();
                                            UserDataAdapter userDataAdapter2=new UserDataAdapter(getContext(),listResultRandomUser);
                                            recyclerView2.setLayoutManager(new LinearLayoutManager(getActivity()));
                                            recyclerView2.setAdapter(userDataAdapter2);
                                            progressBar2.setVisibility(View.GONE);
                                            break;
                                        case 2:
                                            Toast.makeText(getActivity(),"Đã Tạo RecycleView lần 3",Toast.LENGTH_SHORT).show();
                                            UserDataAdapter userDataAdapter3=new UserDataAdapter(getContext(),listResultRandomUser);
                                            recyclerView3.setLayoutManager(new LinearLayoutManager(getActivity()));
                                            recyclerView3.setAdapter(userDataAdapter3);
                                            progressBar3.setVisibility(View.GONE);
                                            break;
                                        case 3:
                                            Toast.makeText(getActivity(),"Đã Tạo RecycleView lần 4",Toast.LENGTH_SHORT).show();
                                            UserDataAdapter userDataAdapter4=new UserDataAdapter(getContext(),listResultRandomUser);
                                            recyclerView4.setLayoutManager(new LinearLayoutManager(getActivity()));
                                            recyclerView4.setAdapter(userDataAdapter4);
                                            progressBar4.setVisibility(View.GONE);
                                            break;
                                    }
                             }

                             @Override
                             public void onError(Throwable e) {

                             }

                             @Override
                             public void onNext(UserData userData) {
                                 listResultRandomUser=new ArrayList<>(userData.getResults());
                             }
                         });


            }
            };
        }
*/

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_executor, container, false);
        ButterKnife.bind(this,view);;
        return view;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Initialize the handler for UI thread to handle message from worker threads
        mUiHandler = new UiHandler(Looper.getMainLooper(), mDisplayTextView);

        // create and start a new HandlerThread worker thread
        mHandlerThread = new CustomHandlerThread("HandlerThread");
        mHandlerThread.setUiThreadCallback(this);
        mHandlerThread.start();

        // get the thread pool manager instance
        mCustomThreadPoolManager = CustomThreadPoolManager.getsInstance();
        // CustomThreadPoolManager stores activity as a weak reference. No need to unregister.
        mCustomThreadPoolManager.setUiThreadCallback(this);

    }

    // onClick handler for Send Runnable button
    @OnClick(R.id.send_msg_1)
    public void sendRunnableToHandlerThread(View view){
        // send a runnable to run on the HandlerThread
        CustomRunnable runnable = new CustomRunnable();
        runnable.setUiThreadCallback(this);
        mHandlerThread.postRunnable(runnable);
    }

    // onClick handler for Send Message button
    @OnClick(R.id.send_msg_2)
    public void sendMessageToHandlerThread(View view){
        // add a message to worker thread's message queue
        mHandlerThread.addMessage(1);
    }

    // onClick handler for Send 4 Tasks button
    @OnClick(R.id.send_msg_3)
    public void send4tasksToThreadPool(View view) {
        for(int i = 0; i < 4; i++) {
            CustomCallable callable = new CustomCallable();
            callable.setCustomThreadPoolManager(mCustomThreadPoolManager);
            mCustomThreadPoolManager.addCallable(callable);
        }
    }

    // onClick handler for Send 8 Tasks button
    @OnClick(R.id.send_msg_4)
    public void send8TasksToThreadPool(View view) {
        for(int i = 0; i < 8; i++) {
            CustomCallable callable = new CustomCallable();
            callable.setCustomThreadPoolManager(mCustomThreadPoolManager);
            mCustomThreadPoolManager.addCallable(callable);
        }
    }

    // onClick handler for Stop All Thread button
    @OnClick(R.id.send_msg_7)
    public void cancelAllTasksInThreadPool(View view) {
        mCustomThreadPoolManager.cancelAllTasks();
    }

    // onClick handler for Clear Messages button
    @OnClick(R.id.clear)
    public void clearDisplay(View view) {
        mDisplayTextView.setText("");
    }

    // Send message from worker thread to the UI thread

    @Override
    public void publishToUiThread(Message message) {
        if(mUiHandler != null){
            mUiHandler.sendMessage(message);
        }
    }


    // UI handler class, declared as static so it doesn't have implicit
    // reference to activity context. This helps to avoid memory leak.
    private static class UiHandler extends Handler {
        private WeakReference<TextView> mWeakRefDisplay;

        public UiHandler(Looper looper, TextView display) {
            super(looper);
            this.mWeakRefDisplay = new WeakReference<TextView>(display);
        }

        // This method will run on UI thread
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                // Our communication protocol for passing a string to the UI thread
                case Util.MESSAGE_ID:
                    Bundle bundle = msg.getData();
                    String messsageText = bundle.getString(Util.MESSAGE_BODY, Util.EMPTY_MESSAGE);
                    if(mWeakRefDisplay != null && mWeakRefDisplay.get() != null)
                        mWeakRefDisplay.get().append(Util.getReadableTime() + " " + messsageText + "\n");
                    break;
                default:
                    break;
            }
        }
    }




    @Override
    public void onResume() {
        super.onResume();
        //Restore from db
    }
}

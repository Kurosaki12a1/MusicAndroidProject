package com.bku.jobs.Util;

import com.bku.jobs.ModelData.JobData;
import com.bku.jobs.Models.JobInfo;

import java.util.ArrayList;

/**
 * Created by Welcome on 5/24/2018.
 */

public class UtilityJob {
    private static UtilityJob instance=null;

    private boolean isAccessed=false;

    private JobData jobInfo;

    public void setJobData(ArrayList<JobData> jobData) {
        this.jobData = new ArrayList<>( jobData);
    }

    public ArrayList<JobData> getJobData() {
        return jobData;
    }

    private ArrayList<JobData> jobData=new ArrayList<>();

    public JobData getJobInfo(){
        return this.jobInfo;
    }
    public void setJobInfo(JobData job){
        this.jobInfo=job;
    }

    private UtilityJob(){

    }



    public void setTrue(){isAccessed=true;}

    public void setFalse(){isAccessed=false;}

    public boolean getAccess(){return isAccessed;}

    public static UtilityJob getInstance(){
        if(instance==null){
            instance=new UtilityJob();
        }
        return instance;
    }
}

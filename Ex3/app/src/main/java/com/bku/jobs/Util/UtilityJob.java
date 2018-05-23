package com.bku.jobs.Util;

import com.bku.jobs.Models.JobInfo;

/**
 * Created by Welcome on 5/24/2018.
 */

public class UtilityJob {
    private static UtilityJob instance=null;

    private JobInfo jobInfo;

    public JobInfo getJobInfo(){
        return this.jobInfo;
    }
    public void setJobInfo(JobInfo job){
        this.jobInfo=job;
    }

    private UtilityJob(){

    }

    public static UtilityJob getInstance(){
        if(instance==null){
            instance=new UtilityJob();
        }
        return instance;
    }
}

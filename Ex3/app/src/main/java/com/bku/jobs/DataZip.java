package com.bku.jobs;

import com.bku.jobs.ModelData.JobData;
import com.bku.jobs.ModelData.UserData.UserData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Welcome on 7/15/2018.
 */

public class DataZip {

    public ArrayList<JobData> getJobData() {
        return jobData;
    }

    public UserData getUserData() {
        return userData;
    }

    ArrayList<JobData> jobData;
    UserData userData;

    public DataZip(List<JobData> lst, UserData userData1){
        this.jobData=new ArrayList<>(lst);
        this.userData=userData1;
    }


}

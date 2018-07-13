package com.bku.jobs.API;

import com.bku.jobs.ModelData.JobData;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by Welcome on 7/11/2018.
 */

public interface APIService {

    @GET("/positions.json")
    Observable<List<JobData>> getJobData();

    @GET("/positions.json?")
    Observable<List<JobData>> getSearchData(@Query("description") String keySearch);
}

package com.bku.jobs.Models;



/**
 * Created by Welcome on 5/21/2018.
 */

public class JobInfo {
    private String JobId;
    private String JobCreatedAt;
    private String JobTitle;
    private String Location;
    private String Type;
    private String Description;
    private String HowToApply; //url cua trang dang ky ungtuyen
    private String Company; // ten cong ty
    private String CompanyURL; //url cua trang cong ty
    private String CompanyLogo;
    private String URL; // url cua trang tren github

    public JobInfo(String jobId,String jobCreatedAt,String jobTitle,String location,String Type,
                   String description,String howToApply,String company,String companyURL,
                   String companyLogo,String URL){
        this.JobId=jobId;
        this.JobTitle=jobTitle;
        this.JobCreatedAt=jobCreatedAt;
        this.Location=location;
        this.Type=Type;
        this.Description=description;
        this.HowToApply=howToApply;
        this.Company=company;
        this.CompanyURL=companyURL;
        this.CompanyLogo=companyLogo;
        this.URL=URL;
    }

    public String getJobId(){return JobId;}
    public String getJobTitle(){return JobTitle;}
    public String getJobCreatedAt(){return JobCreatedAt;}
    public String getLocation(){return Location;}
    public String getType(){return Type;}
    public String getDescription(){return Description;}
    public String getHowToApply(){return HowToApply;}
    public String getCompany(){return Company;}
    public String getCompanyURL(){return CompanyURL;}
    public String getCompanyLogo(){return CompanyLogo;}
    public String getURL(){return URL;}

}

package com.bku.jobs.Models;


import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Welcome on 5/21/2018.
 */

public class JobInfo implements Parcelable{

    public static final String TABLE_NAME="Favorite_Job";

    public static final String COLUMN_ID="Id";

    public static final String COLUMN_CreatedAt="createdAt";

    public static final String COLUMN_Title="Title";

    public static final String COLUMN_Location="Location";

    public static final String COLUMN_Type="Type";

    public static final String COLUMN_Description="Description";

    public static final String COLUMN_howToApply="How To Apply";

    public static final String COLUMN_Company="Company Name";

    public static final String COLUMN_CompanyURL="Website of Company";

    public static final String COLUMN_CompanyLogo="Company Logo";

    public static final String COLUMN_URL="URL";


    public void setJobId(String jobId) {
        JobId = jobId;
    }

    private String JobId;

    public void setJobCreatedAt(String jobCreatedAt) {
        JobCreatedAt = jobCreatedAt;
    }

    public void setJobTitle(String jobTitle) {
        JobTitle = jobTitle;
    }

    public void setLocation(String location) {
        Location = location;
    }

    public void setType(String type) {
        Type = type;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public void setHowToApply(String howToApply) {
        HowToApply = howToApply;
    }

    public void setCompany(String company) {
        Company = company;
    }

    public void setCompanyURL(String companyURL) {
        CompanyURL = companyURL;
    }

    public void setCompanyLogo(String companyLogo) {
        CompanyLogo = companyLogo;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }

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

    public JobInfo(){

    }

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.JobId);
        parcel.writeString(this.JobTitle);
        parcel.writeString(this.JobCreatedAt);
        parcel.writeString(this.Location);
        parcel.writeString(this.Type);
        parcel.writeString(this.Description);
        parcel.writeString(this.HowToApply);
        parcel.writeString(this.Company);
        parcel.writeString(this.CompanyURL);
        parcel.writeString(this.CompanyLogo);
        parcel.writeString(this.URL);
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public JobInfo createFromParcel(Parcel in) {
            return new JobInfo(in);
        }

        public JobInfo[] newArray(int size) {
            return new JobInfo[size];
        }
    };
    public JobInfo(Parcel in){
        this.JobId = in.readString();
        this.JobTitle = in.readString();
        this.JobCreatedAt = in.readString();
        this.Location = in.readString();
        this.Type = in.readString();
        this.Description = in.readString();
        this.HowToApply = in.readString();
        this.Company = in.readString();
        this.CompanyURL = in.readString();
        this.CompanyLogo = in.readString();
        this.URL = in.readString();
    }

}

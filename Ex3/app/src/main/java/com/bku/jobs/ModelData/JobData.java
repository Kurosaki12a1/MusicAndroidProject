package com.bku.jobs.ModelData;

public class JobData{
	private String companyLogo;
	private String howToApply;
	private String createdAt;
	private String description;
	private String location;
	private String company;
	private String companyUrl;
	private String id;
	private String title;
	private String type;
	private String url;

	public void setCompanyLogo(String companyLogo){
		this.companyLogo = companyLogo;
	}

	public String getCompanyLogo(){
		return companyLogo;
	}

	public void setHowToApply(String howToApply){
		this.howToApply = howToApply;
	}

	public String getHowToApply(){
		return howToApply;
	}

	public void setCreatedAt(String createdAt){
		this.createdAt = createdAt;
	}

	public String getCreatedAt(){
		return createdAt;
	}

	public void setDescription(String description){
		this.description = description;
	}

	public String getDescription(){
		return description;
	}

	public void setLocation(String location){
		this.location = location;
	}

	public String getLocation(){
		return location;
	}

	public void setCompany(String company){
		this.company = company;
	}

	public String getCompany(){
		return company;
	}

	public void setCompanyUrl(String companyUrl){
		this.companyUrl = companyUrl;
	}

	public String getCompanyUrl(){
		return companyUrl;
	}

	public void setId(String id){
		this.id = id;
	}

	public String getId(){
		return id;
	}

	public void setTitle(String title){
		this.title = title;
	}

	public String getTitle(){
		return title;
	}

	public void setType(String type){
		this.type = type;
	}

	public String getType(){
		return type;
	}

	public void setUrl(String url){
		this.url = url;
	}

	public String getUrl(){
		return url;
	}

	@Override
 	public String toString(){
		return 
			"JobData{" + 
			"company_logo = '" + companyLogo + '\'' + 
			",how_to_apply = '" + howToApply + '\'' + 
			",created_at = '" + createdAt + '\'' + 
			",description = '" + description + '\'' + 
			",location = '" + location + '\'' + 
			",company = '" + company + '\'' + 
			",company_url = '" + companyUrl + '\'' + 
			",id = '" + id + '\'' + 
			",title = '" + title + '\'' + 
			",type = '" + type + '\'' + 
			",url = '" + url + '\'' + 
			"}";
		}
}

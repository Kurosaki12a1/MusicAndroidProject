package com.bku.jobs.ModelData.UserData;

public class ResultsItem{
	private String nat;
	private String gender;
	private String phone;
	private Name name;
	private String cell;
	private String email;
	private Picture picture;

	public void setNat(String nat){
		this.nat = nat;
	}

	public String getNat(){
		return nat;
	}

	public void setGender(String gender){
		this.gender = gender;
	}

	public String getGender(){
		return gender;
	}

	public void setPhone(String phone){
		this.phone = phone;
	}

	public String getPhone(){
		return phone;
	}

	public void setName(Name name){
		this.name = name;
	}

	public Name getName(){
		return name;
	}

	public void setCell(String cell){
		this.cell = cell;
	}

	public String getCell(){
		return cell;
	}

	public void setEmail(String email){
		this.email = email;
	}

	public String getEmail(){
		return email;
	}

	public void setPicture(Picture picture){
		this.picture = picture;
	}

	public Picture getPicture(){
		return picture;
	}

	@Override
 	public String toString(){
		return 
			"ResultsItem{" + 
			"nat = '" + nat + '\'' + 
			",gender = '" + gender + '\'' + 
			",phone = '" + phone + '\'' + 
			",name = '" + name + '\'' + 
			",cell = '" + cell + '\'' + 
			",email = '" + email + '\'' + 
			",picture = '" + picture + '\'' + 
			"}";
		}
}

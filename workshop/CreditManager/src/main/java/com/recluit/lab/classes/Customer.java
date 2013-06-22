package com.recluit.lab.classes;

public class Customer {
	
	String rfc;
	String fName;
	String lName;
	String qualification;
	String date;
	String salary;
	
	public Customer(String rfc, String fName, String lName,
			String qualification, String date, String salary) {
		super();
		this.rfc = rfc;
		this.fName = fName;
		this.lName = lName;
		this.qualification = qualification;
		this.date = date;
		this.salary = salary;
	}
	
	public String getRfc() {
		return rfc;
	}
	public void setRfc(String rfc) {
		this.rfc = rfc;
	}
	public String getfName() {
		return fName;
	}
	public void setfName(String fName) {
		this.fName = fName;
	}
	public String getlName() {
		return lName;
	}
	public void setlName(String lName) {
		this.lName = lName;
	}
	public String getQualification() {
		return qualification;
	}
	public void setQualification(String qualification) {
		this.qualification = qualification;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getSalary() {
		return salary;
	}
	public void setSalary(String salary) {
		this.salary = salary;
	}	
	
}

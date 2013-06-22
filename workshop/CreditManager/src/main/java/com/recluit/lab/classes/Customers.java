package com.recluit.lab.classes;

import java.util.ArrayList;
import java.util.List;


public class Customers {

	List<Customer> allCustomers;
	
	public Customers(){
		allCustomers = new ArrayList<Customer>();
		allCustomers.add(new Customer("ABCDE12345","Yael","Salcedo","GOOD","30/5/2013","20000"));
		allCustomers.add(new Customer("12345ABCDE","Chino","Lova","GOOD","1/6/2013","20000"));
	}

	public List<Customer> getAllCustomers() {
		return allCustomers;
	}

	public void setAllCustomers(List<Customer> allCustomers) {
		this.allCustomers = allCustomers;
	}	
	
}

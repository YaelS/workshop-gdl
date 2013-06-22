package com.recluit.lab.action;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.opensymphony.xwork2.ActionSupport;
import com.recluit.lab.classes.DBConection;
import com.recluit.lab.classes.Loan;
import com.recluit.lab.restclient.RestClient;

public class RFCAction extends ActionSupport{

	private static final long serialVersionUID = -3561538112932319256L;

	private static String rfcQuery;
	private static String name;
	private static String address;
	private String rfcOptions;
	private List<Loan> allLoans;
	private String rfc;

	public String execute() throws Exception{
		
		String result;
		
		System.out.println("RFC: "+rfcQuery);
		System.out.println("Option: "+rfcOptions);
		
		DBConection db = new DBConection();
		Connection conn = db.connectToOracle();
		RestClient restClient = new RestClient();
		Statement stmt = null;
		ResultSet rs = null;
		
		try {
			stmt = conn.createStatement();
			String query = "select * from customer";
			rs = stmt.executeQuery(query);
			System.out.println("Query executed: "+query);
			Loan loan;
			while (rs.next()) {
				result = rs.getString("rfc");
				name = rs.getString("fname");
				address = rs.getString("address");
				System.out.println("RFC DB: "+result+" RFC: "+rfcQuery);
				if(result.equals(rfcQuery)){
					
					switch(rfcOptions){
					
						case "1":	
							return "close";
						case "2":
							System.out.println("Opcion payment...");
							return "payment";
						case "3":
							System.out.println("Opcion get a new loan...");
							
							int qualification = 0;
							List<String> qualifications = new ArrayList<String>();
							stmt = conn.createStatement();
							String queryNew = "select qualification from loans where rfc = '"+rfcQuery+"'";
							rs = stmt.executeQuery(queryNew);	
							System.out.println("Query executed: "+queryNew);
	
							while(rs.next()){
								
								qualifications.add(rs.getString("qualification"));
								
							}
							
							if(qualifications.size() == 0){
								
								qualification = restClient.sendMsg("R:"+rfcQuery);
								
							}
							else{
								Iterator<String> iteratorQualifications = qualifications.iterator();
								while(iteratorQualifications.hasNext()){
									switch(iteratorQualifications.next()){
										case "VERY BAD":
											qualification = qualification - 2;
											break;
										case "BAD":
											qualification = qualification - 1;
											break;
										case "GOOD":
											qualification = qualification + 1;
											break;
										case "VERY GOOD":
											qualification = qualification + 2;
											break;
									}
									
								}
							}
							System.out.println("Qualification: "+qualification);
							if(qualification > 0){
								System.out.println("Credito aceptado.");
								return "new";
							}
							else{
								System.out.println("No se aceptan mas creditos por la mala calificacion.");
								break;
							}
							
						case "4":
							System.out.println("Opcion display...");
							try {
								stmt = conn.createStatement();
								String queryDisplay = "select * from loans where rfc = '"+rfcQuery+"'";
								rs = stmt.executeQuery(queryDisplay);
								System.out.println("Query executed: "+queryDisplay);
								
								allLoans = new ArrayList<Loan>();
								
								while(rs.next()){
							
										loan = new Loan(rs.getString("id"),rs.getString("rfc"),
												rs.getString("loan_amount"),rs.getString("qualification"),
												rs.getString("expiration_date"), rs.getString("status"));
										allLoans.add(loan);

								}
										
							} catch (SQLException e) {
								e.printStackTrace();
							}
							return "display";
						default:
							return ERROR;					
					}
				}
			}
						
			} catch (SQLException e) {
				e.printStackTrace();
			}
			return ERROR;
	}
	
	public String getRfcQuery() {
		return rfcQuery;
	}
	
	public void setRfcQuery(String rfcQuery) {
		this.rfcQuery = rfcQuery;
	}
	
	public String getRfcOptions() {
		return rfcOptions;
	}
	
	public void setRfcOptions(String rfcOptions) {
		this.rfcOptions = rfcOptions;
	}
	
	public List<Loan> getAllLoans() {
		return allLoans;
	}
	
	public void setAllLoans(List<Loan> allLoans) {
		this.allLoans = allLoans;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		RFCAction.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		RFCAction.address = address;
	}
	
}

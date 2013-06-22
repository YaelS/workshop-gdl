package com.recluit.lab.action;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.opensymphony.xwork2.ActionSupport;
import com.recluit.lab.classes.DBConection;
import com.recluit.lab.restclient.RestClient;

public class closeLoanAction extends ActionSupport{

	private static final long serialVersionUID = 6409662521151657289L;
	
	private String loanId;
	
	public String execute() throws Exception{
		
		DBConection db = new DBConection();
		Connection conn = db.connectToOracle();
		RFCAction rfcAction = new RFCAction();
		RestClient restClient = new RestClient();
		
		Statement stmt = null;
		ResultSet rs = null;
		String status = null;
		String amount = null;
		String qualification = null;
		String loanDate = null;
		String row = null;
		int remaining = 0;
		
		
		try{
			stmt = conn.createStatement();
			String selectPayment = new StringBuilder().append("select min(remaining_amount) remaining from payments where loan_id = ")
					.append(loanId).toString();
			rs = stmt.executeQuery(selectPayment);
			System.out.println("Query executed: "+selectPayment);
			
			while(rs.next()){
				
				remaining = rs.getInt("remaining");
				
			}
			System.out.println("Remaining: "+remaining);
			
			if(remaining <= 0){
				
				stmt = conn.createStatement();
				String selectLoans = new StringBuilder().append("select loan_amount, qualification, to_char(expiration_date,'dd-mm-YYYY') date_format, status from loans where id = ")
						.append(loanId).toString();
				rs = stmt.executeQuery(selectLoans);
				System.out.println("Query executed: "+selectLoans);
				
				while(rs.next()){
					
					amount = rs.getString("loan_amount");
					qualification = rs.getString("qualification");
					loanDate = rs.getString("date_format");
					status = rs.getString("status");
					
				}
				System.out.println("Status: "+status);
				
				if(status != "N"){
					stmt = conn.createStatement();
					String updateLoan = new StringBuilder().append("update loans set status = 'N' where id = ")
							.append(loanId).toString();
					
					if(stmt.executeUpdate(updateLoan) > 0){
						System.out.println("Query executed: "+updateLoan);
						row = new StringBuilder().append("E:112$").append(rfcAction.getRfcQuery()).append("$")
								.append(rfcAction.getName()).append("$").append(rfcAction.getAddress())
								.append("$").append(amount).append("$").append(loanDate).append("$")
								.append(qualification).append("$Y").toString();
						
						System.out.println("Row to send: "+row);
						restClient.sendMsg(row);
					}					
					
					return "closeSuccess";
				}
				
			}
			
		}catch(SQLException e){
			e.printStackTrace();
		}
		
		return ERROR;
		
	}

	public String getLoanId() {
		return loanId;
	}

	public void setLoanId(String loanId) {
		this.loanId = loanId;
	}

}

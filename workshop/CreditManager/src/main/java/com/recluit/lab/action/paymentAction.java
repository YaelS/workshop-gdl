package com.recluit.lab.action;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.opensymphony.xwork2.ActionSupport;
import com.recluit.lab.classes.DBConection;
import com.recluit.lab.restclient.RestClient;

public class paymentAction  extends ActionSupport{

	private static final long serialVersionUID = 2104277260833292288L;
	
	private String loanId;
	private int payment;

	public String execute() throws Exception{
		
		DBConection db = new DBConection();
		Connection conn = db.connectToOracle();
		
		Statement stmt = null;
		ResultSet rs = null;
		int loanAmount = 0;
		int remaining = 0;
		
		try{
			stmt = conn.createStatement();
			String queryPay = new StringBuilder().append("select id, loan_amount from loans where id = '")
					.append(loanId).append("'").toString();
			rs = stmt.executeQuery(queryPay);
			System.out.println("Query executed: "+queryPay);
			
			int rowCount = 0;
			while(rs.next()){
				
				loanId = rs.getString("id");
				loanAmount = rs.getInt("loan_amount");
				rowCount++;
				
			}
			System.out.println("LoanID: "+loanId+" Loan amount: "+loanAmount);
			
			if(rowCount <= 0){
				
				return ERROR;
			}
			
			stmt = conn.createStatement();
			String selectPayment = new StringBuilder().append("select min(remaining_amount) remaining from payments where loan_id = ")
					.append(loanId).toString();
			rs = stmt.executeQuery(selectPayment);
			System.out.println("Query executed: "+selectPayment);
			
			while(rs.next()){
				
				remaining = rs.getInt("remaining");
				
			}
			System.out.println("Remaining: "+remaining);
			
			if(remaining == 0){
				remaining = loanAmount;
			}
			
			stmt = conn.createStatement();
			String insertPayment = new StringBuilder().append("insert into payments values(").append(loanId)
					.append(",to_date(sysdate+30,'YYYY-mm-dd'),0,").append(loanAmount).append(",")
					.append(remaining-payment).append(")").toString();
			rs = stmt.executeQuery(insertPayment);
			System.out.println("Query executed: "+insertPayment);
		}catch(SQLException e){
			e.printStackTrace();
		}
		
		return "successPayment";		
		
	}

	public String getLoanId() {
		return loanId;
	}

	public void setLoanId(String loanId) {
		this.loanId = loanId;
	}

	public int getPayment() {
		return payment;
	}

	public void setPayment(int payment) {
		this.payment = payment;
	}

	
	
}

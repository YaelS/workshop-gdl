package com.recluit.lab.action;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.opensymphony.xwork2.ActionSupport;
import com.recluit.lab.classes.DBConection;
import com.recluit.lab.restclient.RestClient;

public class newLoanAction  extends ActionSupport{

	private static final long serialVersionUID = -6377995582418287256L;
	
	private String amount;

	public String execute() throws Exception{
		
		RFCAction rfcAction = new RFCAction();
		RestClient restClient = new RestClient();
		
		DBConection db = new DBConection();
		Connection conn = db.connectToOracle();
		Statement stmt = null;
		ResultSet rs = null;
		String row = null;
		String loanDate = null;
		
		try{
			stmt = conn.createStatement();
			System.out.println("RFC: "+rfcAction.getRfcQuery()+" Amount: "+amount);
			String insert = new StringBuilder().append("insert into loans values(loans_seq.nextval,'")
					.append(rfcAction.getRfcQuery()).append("',").append(amount)
					.append(",'GOOD',sysdate,'Y')").toString();
			
			if(stmt.executeUpdate(insert) > 0){
				System.out.println("Query executed: "+insert);
				
				stmt = conn.createStatement();
				String queryDate = "select to_char(expiration_date,'dd-mm-YYYY') date_format from loans";
				rs = stmt.executeQuery(queryDate);
				System.out.println("Query executed: "+queryDate);
				
				while (rs.next()) {
					loanDate = rs.getString("date_format");
				}
				
				row = new StringBuilder().append("A:112$").append(rfcAction.getRfcQuery()).append("$")
						.append(rfcAction.getName()).append("$").append(rfcAction.getAddress())
						.append("$").append(amount).append("$").append(loanDate).append("$GOOD$Y").toString();
				System.out.println("Row to send: "+row);
				restClient.sendMsg(row);
				
				return SUCCESS;
			}			
			else{
				System.out.println("Query not executed");
				return ERROR;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return ERROR;
		
	}
	
	public String getAmount() {
		return amount;
	}
	
	public void setAmount(String amount) {
		this.amount = amount;
	}
	
}

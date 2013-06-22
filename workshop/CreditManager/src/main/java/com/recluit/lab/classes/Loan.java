package com.recluit.lab.classes;

public class Loan {

	private String LoanId;
	private String rfc;
	private String amount;
	private String qualification;
	private String expirationDate;
	private String status;
	
	public Loan(String loanId, String rfc, String amount, String qualification,
			String expirationDate, String status) {
		super();
		LoanId = loanId;
		this.rfc = rfc;
		this.amount = amount;
		this.qualification = qualification;
		this.expirationDate = expirationDate;
		this.status = status;
	}

	public String getLoanId() {
		return LoanId;
	}

	public void setLoanId(String loanId) {
		LoanId = loanId;
	}

	public String getRfc() {
		return rfc;
	}

	public void setRfc(String rfc) {
		this.rfc = rfc;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getQualification() {
		return qualification;
	}

	public void setQualification(String qualification) {
		this.qualification = qualification;
	}

	public String getExpirationDate() {
		return expirationDate;
	}

	public void setExpirationDate(String expirationDate) {
		this.expirationDate = expirationDate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	
	
}

package project.model;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

// TODO some of these have constraints to check, like positive numbers
final class PaymentSchedule {
	private BigDecimal savingsInterest = new BigDecimal("0.02");
	private BigDecimal savingsCharge = new BigDecimal("5");
	private BigDecimal savingsThreshold = new BigDecimal("1000");
	private final Map<CertificateOfDeposit.Term, BigDecimal> cdInterest;
	private BigDecimal cdMinimum = new BigDecimal("500");
	private BigDecimal checkingCharge = new BigDecimal("8");
	private BigDecimal checkingThreshold = new BigDecimal("2000");
	private BigDecimal overdraftLimit = new BigDecimal("50");
	private BigDecimal overdraftFee = new BigDecimal("40");
	private BigDecimal loanInterest = new BigDecimal("0.05");
	private BigDecimal loanPenalty = new BigDecimal("20");
	private BigDecimal locInterest = new BigDecimal("0.05");
	private BigDecimal locFixedPayment = new BigDecimal("50");
	private BigDecimal locPercentPayment = new BigDecimal("0.02");
	private BigDecimal locPenalty = new BigDecimal("20");

    PaymentSchedule() {
        cdInterest = new HashMap<CertificateOfDeposit.Term, BigDecimal>();
    }
	
	public BigDecimal getSavingsInterest() {
		return savingsInterest;
	}

	public void setSavingsInterest(BigDecimal savingsInterest) {
		this.savingsInterest = savingsInterest;
	}

	public BigDecimal getSavingsCharge() {
		return savingsCharge;
	}

	public void setSavingsCharge(BigDecimal savingsCharge) {
		this.savingsCharge = savingsCharge;
	}

	public BigDecimal getSavingsThreshold() {
		return savingsThreshold;
	}

	public void setSavingsThreshold(BigDecimal savingsThreshold) {
		this.savingsThreshold = savingsThreshold;
	}

	public BigDecimal getCdInterest(CertificateOfDeposit.Term term) {
		return cdInterest.get(term);
	}

	public void setCdInterest(CertificateOfDeposit.Term term, BigDecimal cdInterest) {
		this.cdInterest.put(term,cdInterest);
	}

	public BigDecimal getCdMinimum() {
		return cdMinimum;
	}

	public void setCdMinimum(BigDecimal cdMinimum) {
		this.cdMinimum = cdMinimum;
	}

	public BigDecimal getCheckingCharge() {
		return checkingCharge;
	}

	public void setCheckingCharge(BigDecimal checkingCharge) {
		this.checkingCharge = checkingCharge;
	}

	public BigDecimal getCheckingThreshold() {
		return checkingThreshold;
	}

	public void setCheckingThreshold(BigDecimal checkingThreshold) {
		this.checkingThreshold = checkingThreshold;
	}

	public BigDecimal getOverdraftLimit() {
		return overdraftLimit;
	}

	public void setOverdraftLimit(BigDecimal overdraftLimit) {
		this.overdraftLimit = overdraftLimit;
	}

	public BigDecimal getOverdraftFee() {
		return overdraftFee;
	}

	public void setOverdraftFee(BigDecimal overdraftFee) {
		this.overdraftFee = overdraftFee;
	}

	public BigDecimal getLoanInterest() {
		return loanInterest;
	}

	public void setLoanInterest(BigDecimal loanInterest) {
		this.loanInterest = loanInterest;
	}

	public BigDecimal getLoanPenalty() {
		return loanPenalty;
	}

	public void setLoanPenalty(BigDecimal loanPenalty) {
		this.loanPenalty = loanPenalty;
	}

	public BigDecimal getLocInterest() {
		return locInterest;
	}

	public void setLocInterest(BigDecimal locInterest) {
		this.locInterest = locInterest;
	}

	public BigDecimal getLocFixedPayment() {
		return locFixedPayment;
	}

	public void setLocFixedPayment(BigDecimal locFixedPayment) {
		this.locFixedPayment = locFixedPayment;
	}

	public BigDecimal getLocPercentPayment() {
		return locPercentPayment;
	}

	public void setLocPercentPayment(BigDecimal locPercentPayment) {
		this.locPercentPayment = locPercentPayment;
	}

	public BigDecimal getLocPenalty() {
		return locPenalty;
	}

	public void setLocPenalty(BigDecimal locPenalty) {
		this.locPenalty = locPenalty;
	}
}

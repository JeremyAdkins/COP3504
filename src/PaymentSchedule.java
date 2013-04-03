import java.math.BigDecimal;
import java.util.Map;


public class PaymentSchedule {
	private BigDecimal savingsInterest;
	private BigDecimal savingsCharge;
	private BigDecimal savingsThreshold;
//	private Map<CdTerm, BigDecimal> cdInterest;
	private BigDecimal cdMinimum;
	private BigDecimal checkingCharge;
	private BigDecimal checkingThreshold;
	private BigDecimal overdraftLimit;
	private BigDecimal overdraftFee;
	private BigDecimal loanInterest;
	private BigDecimal loanPenalty;
	private BigDecimal locInterest;
	private BigDecimal locFixedPayment;
	private BigDecimal locPercentPayment;
	private BigDecimal locPenalty;
	
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
//	public Map<CdTerm, BigDecimal> getCdInterest(CdTerm term) {
//		return cdInterest.get(term);
//	}
//	public void setCdInterest(CdTerm term, BigDecimal cdInterest) {
//		this.cdInterest.put(term,cdInterest);
//	}
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

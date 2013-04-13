package project.model;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

final class PaymentSchedule {
	private BigDecimal savingsInterest = new BigDecimal("0.0200");
	private BigDecimal savingsCharge = new BigDecimal("5.00");
	private BigDecimal savingsThreshold = new BigDecimal("1000.00");
	private final Map<CertificateOfDeposit.Term, BigDecimal> cdInterest;
	private BigDecimal cdMinimum = new BigDecimal("500.00");
	private BigDecimal checkingCharge = new BigDecimal("8.00");
	private BigDecimal checkingThreshold = new BigDecimal("2000.00");
	private BigDecimal overdraftLimit = new BigDecimal("50.00");
	private BigDecimal overdraftFee = new BigDecimal("40.00");
	private BigDecimal loanInterest = new BigDecimal("0.0500");
	private BigDecimal loanPenalty = new BigDecimal("20.00");
	private BigDecimal locInterest = new BigDecimal("0.0500");
	private BigDecimal locFixedPayment = new BigDecimal("50.00");
	private BigDecimal locPercentPayment = new BigDecimal("0.0200");
	private BigDecimal locPenalty = new BigDecimal("20.00");

    PaymentSchedule() {
        cdInterest = new HashMap<CertificateOfDeposit.Term, BigDecimal>();
        cdInterest.put(CertificateOfDeposit.Term.SIX_MONTHS, new BigDecimal("0.0250"));
        cdInterest.put(CertificateOfDeposit.Term.ONE_YEAR, new BigDecimal("0.0275"));
        cdInterest.put(CertificateOfDeposit.Term.TWO_YEARS, new BigDecimal("0.0300"));
        cdInterest.put(CertificateOfDeposit.Term.THREE_YEARS, new BigDecimal("0.0325"));
        cdInterest.put(CertificateOfDeposit.Term.FOUR_YEARS, new BigDecimal("0.0350"));
        cdInterest.put(CertificateOfDeposit.Term.FIVE_YEARS, new BigDecimal("0.0375"));
    }
	
	public BigDecimal getSavingsInterest() {
		return savingsInterest;
	}

	public void setSavingsInterest(BigDecimal savingsInterest) throws InvalidInputException {
        if (savingsInterest.compareTo(BigDecimal.ZERO) < 0) {
            throw new InvalidInputException(savingsInterest, "payment schedule values must be non-negative");
        }
		this.savingsInterest = savingsInterest.round(Bank.MATH_CONTEXT);
	}

	public BigDecimal getSavingsCharge() {
		return savingsCharge;
	}

	public void setSavingsCharge(BigDecimal savingsCharge) throws InvalidInputException {
        if (savingsCharge.compareTo(BigDecimal.ZERO) < 0) {
            throw new InvalidInputException(savingsCharge, "payment schedule values must be non-negative");
        }
		this.savingsCharge = savingsCharge.round(Bank.MATH_CONTEXT);
	}

	public BigDecimal getSavingsThreshold() {
		return savingsThreshold;
	}

	public void setSavingsThreshold(BigDecimal savingsThreshold) throws InvalidInputException {
        if (savingsThreshold.compareTo(BigDecimal.ZERO) < 0) {
            throw new InvalidInputException(savingsThreshold, "payment schedule values must be non-negative");
        }
		this.savingsThreshold = savingsThreshold.round(Bank.MATH_CONTEXT);
	}

	public BigDecimal getCdInterest(CertificateOfDeposit.Term term) {
		return cdInterest.get(term);
	}

	public void setCdInterest(CertificateOfDeposit.Term term, BigDecimal cdInterest) throws InvalidInputException {
        if (cdInterest.compareTo(BigDecimal.ZERO) < 0) {
            throw new InvalidInputException(cdInterest, "payment schedule values must be non-negative");
        }
		this.cdInterest.put(term, cdInterest.round(Bank.MATH_CONTEXT));
	}

	public BigDecimal getCdMinimum() {
		return cdMinimum;
	}

	public void setCdMinimum(BigDecimal cdMinimum) throws InvalidInputException {
        if (cdMinimum.compareTo(BigDecimal.ZERO) < 0) {
            throw new InvalidInputException(cdMinimum, "payment schedule values must be non-negative");
        }
		this.cdMinimum = cdMinimum.round(Bank.MATH_CONTEXT);
	}

	public BigDecimal getCheckingCharge() {
		return checkingCharge;
	}

	public void setCheckingCharge(BigDecimal checkingCharge) throws InvalidInputException {
        if (checkingCharge.compareTo(BigDecimal.ZERO) < 0) {
            throw new InvalidInputException(checkingCharge, "payment schedule values must be non-negative");
        }
		this.checkingCharge = checkingCharge.round(Bank.MATH_CONTEXT);
	}

	public BigDecimal getCheckingThreshold() {
		return checkingThreshold;
	}

	public void setCheckingThreshold(BigDecimal checkingThreshold) throws InvalidInputException {
        if (checkingThreshold.compareTo(BigDecimal.ZERO) < 0) {
            throw new InvalidInputException(checkingThreshold, "payment schedule values must be non-negative");
        }
		this.checkingThreshold = checkingThreshold.round(Bank.MATH_CONTEXT);
	}

	public BigDecimal getOverdraftLimit() {
		return overdraftLimit;
	}

	public void setOverdraftLimit(BigDecimal overdraftLimit) throws InvalidInputException {
        if (overdraftLimit.compareTo(BigDecimal.ZERO) < 0) {
            throw new InvalidInputException(overdraftLimit, "payment schedule values must be non-negative");
        }
		this.overdraftLimit = overdraftLimit.round(Bank.MATH_CONTEXT);
	}

	public BigDecimal getOverdraftFee() {
		return overdraftFee;
	}

	public void setOverdraftFee(BigDecimal overdraftFee) throws InvalidInputException {
        if (overdraftFee.compareTo(BigDecimal.ZERO) < 0) {
            throw new InvalidInputException(overdraftFee, "payment schedule values must be non-negative");
        }
		this.overdraftFee = overdraftFee.round(Bank.MATH_CONTEXT);
	}

	public BigDecimal getLoanInterest() {
		return loanInterest;
	}

	public void setLoanInterest(BigDecimal loanInterest) throws InvalidInputException {
        if (loanInterest.compareTo(BigDecimal.ZERO) < 0) {
            throw new InvalidInputException(loanInterest, "payment schedule values must be non-negative");
        }
		this.loanInterest = loanInterest.round(Bank.MATH_CONTEXT);
	}

	public BigDecimal getLoanPenalty() {
		return loanPenalty;
	}

	public void setLoanPenalty(BigDecimal loanPenalty) throws InvalidInputException {
        if (loanPenalty.compareTo(BigDecimal.ZERO) < 0) {
            throw new InvalidInputException(loanPenalty, "payment schedule values must be non-negative");
        }
		this.loanPenalty = loanPenalty.round(Bank.MATH_CONTEXT);
	}

	public BigDecimal getLocInterest() {
		return locInterest;
	}

	public void setLocInterest(BigDecimal locInterest) throws InvalidInputException {
        if (locInterest.compareTo(BigDecimal.ZERO) < 0) {
            throw new InvalidInputException(locInterest, "payment schedule values must be non-negative");
        }
		this.locInterest = locInterest.round(Bank.MATH_CONTEXT);
	}

	public BigDecimal getLocFixedPayment() {
		return locFixedPayment;
	}

	public void setLocFixedPayment(BigDecimal locFixedPayment) throws InvalidInputException {
        if (locFixedPayment.compareTo(BigDecimal.ZERO) < 0) {
            throw new InvalidInputException(locFixedPayment, "payment schedule values must be non-negative");
        }
		this.locFixedPayment = locFixedPayment;
	}

	public BigDecimal getLocPercentPayment() {
		return locPercentPayment;
	}

	public void setLocPercentPayment(BigDecimal locPercentPayment) throws InvalidInputException {
        if (locPercentPayment.compareTo(BigDecimal.ZERO) < 0) {
            throw new InvalidInputException(locPercentPayment, "payment schedule values must be non-negative");
        }
		this.locPercentPayment = locPercentPayment.round(Bank.MATH_CONTEXT);
	}

	public BigDecimal getLocPenalty() {
		return locPenalty;
	}

	public void setLocPenalty(BigDecimal locPenalty) throws InvalidInputException {
        if (locPenalty.compareTo(BigDecimal.ZERO) < 0) {
            throw new InvalidInputException(locPenalty, "payment schedule values must be non-negative");
        }
		this.locPenalty = locPenalty.round(Bank.MATH_CONTEXT);
	}
}

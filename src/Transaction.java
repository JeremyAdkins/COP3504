import hw1.DateTime;

import java.math.BigDecimal;

public final class Transaction {
    private final Type type;

    private final BigDecimal amount;

    private final DateTime timestamp;

    private FraudStatus fraudStatus;

    public static enum Type {
        DEPOSIT, INTEREST, WITHDRAWAL, FEE, CREATION;
    }

    public static enum FraudStatus {
        NOT_FLAGGED, FLAGGED, REVERSED;
    }

    public Transaction(Type type, BigDecimal amount) {
        this.type = type;
        this.amount = amount;
        this.timestamp = new DateTime();
        this.fraudStatus = FraudStatus.NOT_FLAGGED;
    }

    public Type getType() {
        return type;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public DateTime getTimestamp() {
        return timestamp;
    }

    public FraudStatus getFraudStatus() {
        return fraudStatus;
    }

    public void setFraudStatus(FraudStatus fraudStatus) {
        this.fraudStatus = fraudStatus;
    }
}
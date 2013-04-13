package project.model;

import java.math.BigDecimal;
import java.util.Calendar;

public final class Transaction {
    public static enum Type {
        DEPOSIT(true, true), INTEREST(true, false), WITHDRAWAL(false, true), FEE(false, false);

        private final boolean isPositive;

        private final boolean canRepeat;

        private Type(boolean isPositive, boolean canRepeat) {
            this.isPositive = isPositive;
            this.canRepeat = canRepeat;
        }

        public boolean isPositive() {
            return isPositive;
        }

        public boolean canRepeat() {
            return canRepeat;
        }
    }

    public static enum FraudStatus {
        NOT_FLAGGED, FLAGGED, REVERSED;
    }

    private final Type type;

    private final BigDecimal amount;

    private final Calendar timestamp;

    private FraudStatus fraudStatus;

    public Transaction(Type type, BigDecimal amount) {
        this.type = type;
        this.amount = amount;
        this.timestamp = Bank.getInstance().getTime();
        this.fraudStatus = FraudStatus.NOT_FLAGGED;
    }

    public Type getType() {
        return type;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public Calendar getTimestamp() {
        return timestamp;
    }

    public FraudStatus getFraudStatus() {
        return fraudStatus;
    }

    public void setFraudStatus(FraudStatus fraudStatus) {
        this.fraudStatus = fraudStatus;
    }
}

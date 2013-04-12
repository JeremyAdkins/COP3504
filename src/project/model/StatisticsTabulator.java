package project.model;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

final class StatisticsTabulator {
    /**
     * An {@code AccountCategory} is a type of account that has its own statistics. For this purpose, each account type
     * forms its own category, as well as CDs of each term.
     */
    private static final class AccountCategory {
        private final Account.Type baseType;

        private final CertificateOfDeposit.Term term;

        AccountCategory(Account.Type baseType) {
            if (baseType == Account.Type.CD) {
                throw new IllegalArgumentException("CDs must use the other constructor");
            } else {
                this.baseType = baseType;
                this.term = null;
            }
        }

        AccountCategory(Account.Type baseType, CertificateOfDeposit.Term term) {
            if (baseType != Account.Type.CD) {
                throw new IllegalArgumentException("Non-CDs must use the other constructor");
            } else {
                this.baseType = baseType;
                this.term = term;
            }
        }

        boolean isAsset() {
            return baseType.isLoan();
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            AccountCategory that = (AccountCategory) o;

            if (baseType != that.baseType) return false;
            if (term != that.term) return false;

            return true;
        }

        @Override
        public int hashCode() {
            int result = baseType.hashCode();
            result = 31 * result + (term != null ? term.hashCode() : 0);
            return result;
        }

        @Override
        public String toString() {
            if (term != null) {
                return String.format("%s (%s)", baseType, term);
            } else {
                return baseType.toString();
            }
        }
    }

    private final Map<AccountCategory, Integer> accountCount;

    private final Map<AccountCategory, Integer> openCount;

    private final Map<CertificateOfDeposit.Term, Integer> matureCount;

    private final Map<AccountCategory, BigDecimal> totalBalance;

    private BigDecimal totalLocCreditLimit;

    private final Map<User.Role, Integer> employeeCount;

    private int userCount;

    StatisticsTabulator() {
        accountCount = new HashMap<AccountCategory, Integer>();
        openCount = new HashMap<AccountCategory, Integer>();
        matureCount = new HashMap<CertificateOfDeposit.Term, Integer>();
        totalBalance = new HashMap<AccountCategory, BigDecimal>();
        totalLocCreditLimit = BigDecimal.ZERO;
        employeeCount = new HashMap<User.Role, Integer>();
        userCount = 0;
    }

    void tabulateAccount(Account account) {
        AccountCategory category;
        if (account.getType() == Account.Type.CD) {
            CertificateOfDeposit cdAccount = (CertificateOfDeposit) account;
            CertificateOfDeposit.Term term = cdAccount.getTerm();
            category = new AccountCategory(Account.Type.CD, term);
            if (cdAccount.isMature() && !cdAccount.isClosed()) {
                matureCount.put(term, matureCount.get(term) + 1);
            }
        } else {
            category = new AccountCategory(account.getType());
        }

        accountCount.put(category, accountCount.get(category) + 1);
        if (!account.isClosed()) {
            openCount.put(category, openCount.get(category) + 1);
        }
        totalBalance.put(category, totalBalance.get(category).add(account.getBalance()));
        if (account.getType() == Account.Type.LINE_OF_CREDIT) {
            LineOfCredit locAccount = (LineOfCredit) account;
            totalLocCreditLimit.add(locAccount.getCreditLimit());
        }
    }

    void tabulateUser(User user) {
        if (user.getRole() != null) {
            employeeCount.put(user.getRole(), employeeCount.get(user.getRole()) + 1);
        }
        userCount += 1;
    }

    private int sumInteger(Map<?, Integer> stats) {
        int sum = 0;
        for (int value : stats.values()) {
            sum += value;
        }
        return sum;
    }

    private BigDecimal sumBigDecimal(Map<?, BigDecimal> stats) {
        BigDecimal sum = BigDecimal.ZERO;
        for (BigDecimal value : stats.values()) {
            sum = sum.add(value);
        }
        return sum;
    }

    private BigDecimal sumAssets() {
        BigDecimal sum = BigDecimal.ZERO;
        for (Map.Entry<AccountCategory, BigDecimal> entry : totalBalance.entrySet()) {
            if (entry.getKey().isAsset()) {
                sum = sum.add(entry.getValue());
            }
        }
        return sum;
    }

    Map<String, String> produceStatistics() {
        Map<String, String> stats = new LinkedHashMap<String, String>();

        // global statistics
        put(stats, "Number of users", userCount);
        put(stats, "Number of customers", userCount - sumInteger(employeeCount));
        for (User.Role role : User.Role.values()) {
            put(stats, String.format("Number of %ss", role), employeeCount.get(role));
        }
        put(stats, "Sum of assets", sumAssets());
        put(stats, "Sum of liabilities", sumBigDecimal(totalBalance).subtract(sumAssets()));
        put(stats, "Average balance", sumBigDecimal(totalBalance).divide(new BigDecimal(sumInteger(accountCount))));

        // per category statistics
        for (AccountCategory category : accountCount.keySet()) {
            put(stats, "Total " + category, accountCount.get(category));
            put(stats, "Open " + category, openCount.get(category));
            if (category.baseType == Account.Type.CD) {
                put(stats, "Mature " + category, matureCount.get(category.term));
            }
            put(stats, "Closed " + category, accountCount.get(category) - openCount.get(category));
            put(stats, "Balance in " + category, totalBalance.get(category));
            if (category.baseType == Account.Type.LINE_OF_CREDIT) {
                put(stats, "Total credit limit", totalLocCreditLimit);
            }
        }

        return stats;
    }

    private void put(Map<String, String> stats, String name, int value) {
        stats.put(name, Integer.toString(value));
    }

    private void put(Map<String, String> stats, String name, BigDecimal value) {
        stats.put(name, String.format("$%.2f", value)); // TODO: what is the rounding mode here?
    }
}

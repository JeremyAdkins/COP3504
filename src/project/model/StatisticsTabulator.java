package project.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;

final class StatisticsTabulator {
    /**
     * An {@code AccountCategory} is a type of account that has its own statistics. For this purpose, each account type
     * forms its own category, as well as CDs of each term.
     */
    private static final class AccountCategory implements Comparable<AccountCategory> {
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
        public int compareTo(AccountCategory other) {
            int baseCompare = baseType.compareTo(other.baseType);
            if (baseCompare == 0) {
                return term.compareTo(other.term);
            } else {
                return baseCompare;
            }
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
        accountCount = new TreeMap<AccountCategory, Integer>();
        openCount = new TreeMap<AccountCategory, Integer>();
        matureCount = new TreeMap<CertificateOfDeposit.Term, Integer>();
        totalBalance = new TreeMap<AccountCategory, BigDecimal>();
        totalLocCreditLimit = BigDecimal.ZERO;
        employeeCount = new TreeMap<User.Role, Integer>();
        userCount = 0;
    }

    void tabulateAccount(Account account) {
        AccountCategory category;
        if (account.getType() == Account.Type.CD) {
            CertificateOfDeposit cdAccount = (CertificateOfDeposit) account;
            CertificateOfDeposit.Term term = cdAccount.getTerm();
            category = new AccountCategory(Account.Type.CD, term);
        } else {
            category = new AccountCategory(account.getType());
        }
        initializeCategory(category);

        accountCount.put(category, accountCount.get(category) + 1);
        if (!account.isClosed()) {
            openCount.put(category, openCount.get(category) + 1);
        }
        totalBalance.put(category, totalBalance.get(category).add(account.getBalance()));

        if (category.baseType == Account.Type.CD) {
            CertificateOfDeposit cdAccount = (CertificateOfDeposit) account;
            if (cdAccount.isMature() && !cdAccount.isClosed()) {
                matureCount.put(category.term, matureCount.get(category.term) + 1);
            }
        } else if (account.getType() == Account.Type.LINE_OF_CREDIT) {
            LineOfCredit locAccount = (LineOfCredit) account;
            totalLocCreditLimit.add(locAccount.getCreditLimit());
        }
    }

    private void initializeCategory(AccountCategory category) {
        if (!accountCount.containsKey(category)) {
            accountCount.put(category, 0);
        }
        if (!openCount.containsKey(category)) {
            openCount.put(category, 0);
        }
        if (category.term != null && !matureCount.containsKey(category.term)) {
            matureCount.put(category.term, 0);
        }
        if (!totalBalance.containsKey(category)) {
            totalBalance.put(category, BigDecimal.ZERO);
        }
    }

    void tabulateUser(User user) {
        if (user.getRole() != null) {
            User.Role role = user.getRole();
            if (!employeeCount.containsKey(role)) {
                employeeCount.put(role, 0);
            }
            employeeCount.put(role, employeeCount.get(role) + 1);
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

    private int sumAssetInteger(Map<AccountCategory, Integer> stats) {
        int sum = 0;
        for (Map.Entry<AccountCategory, Integer> entry : stats.entrySet()) {
            if (entry.getKey().isAsset()) {
                sum += entry.getValue();
            }
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

    private BigDecimal sumAssetBigDecimal(Map<AccountCategory, BigDecimal> stats) {
        BigDecimal sum = BigDecimal.ZERO;
        for (Map.Entry<AccountCategory, BigDecimal> entry : stats.entrySet()) {
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
        int assetAccounts = sumAssetInteger(accountCount);
        int liabilityAccounts = sumInteger(accountCount) - assetAccounts;
        BigDecimal sumOfAssets = sumAssetBigDecimal(totalBalance);
        BigDecimal sumOfLiabilities = sumBigDecimal(totalBalance).subtract(sumOfAssets);
        put(stats, "Sum of assets", sumOfAssets);
        put(stats, "Sum of liabilities", sumOfLiabilities);
        put(stats, "Average of assets", sumOfAssets.divide(new BigDecimal(assetAccounts), 2, RoundingMode.HALF_UP));
        put(stats, "Average of liabilities", sumOfLiabilities.divide(new BigDecimal(liabilityAccounts), 2, RoundingMode.HALF_UP));
        put(stats, "Average balance", sumOfAssets.add(sumOfLiabilities).divide(new BigDecimal(assetAccounts + liabilityAccounts), 2, RoundingMode.HALF_UP));

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
        stats.put(name, String.format("$%.2f", value));
    }
}

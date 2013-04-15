package project.gui;

import project.Controller;
import project.gui.util.DollarAmountFormatter;
import project.gui.util.PercentageFormatter;
import project.model.Account;
import project.model.CertificateOfDeposit;
import project.model.LineOfCredit;

import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;

public final class AccountInfoPanel extends JPanel {
    private final Controller controller;

    private final Account account;

    private JLabel balanceLabel;

    public AccountInfoPanel(Controller controller, Account account) {
        this.controller = controller;
        this.account = account;
        initComponents();
        update();
    }

    public void update() {
        BigDecimal balance = account.getBalance();
        if (account.getType().isLoan()) {
            balance = balance.negate();
        }
        balanceLabel.setText(new DollarAmountFormatter().valueToString(balance));
    }

    private void initComponents() {
        final Font labelFont = new Font(Font.SANS_SERIF, Font.BOLD, 16);
        int rows = 3;
        if (account.getType() == Account.Type.CD || account.getType() == Account.Type.LINE_OF_CREDIT) {
            rows += 1;
        }
        if (account.isClosed()) {
            rows += 1;
        }
        setLayout(new GridLayout(rows, 2));

        JLabel accountNumberCaption = new JLabel("Account number");
        accountNumberCaption.setFont(labelFont);
        add(accountNumberCaption);
        JLabel accountNumberLabel = new JLabel(Integer.toString(account.getAccountNumber()), JLabel.RIGHT);
        accountNumberLabel.setFont(labelFont);
        add(accountNumberLabel);

        JLabel balanceCaption = new JLabel("Balance");
        balanceCaption.setFont(labelFont);
        add(balanceCaption);
        balanceLabel = new JLabel();
        balanceLabel.setFont(labelFont);
        balanceLabel.setHorizontalAlignment(JLabel.RIGHT);
        add(balanceLabel);

        if (account.getType() == Account.Type.CD) {
            CertificateOfDeposit cdAccount = (CertificateOfDeposit) account;
            int monthsRemaining = cdAccount.getTerm().getLength() - cdAccount.getMonthsElapsed();
            if (monthsRemaining < 0) {
                monthsRemaining = 0;
            }

            JLabel monthsRemainingCaption = new JLabel("Months remaining");
            monthsRemainingCaption.setFont(labelFont);
            add(monthsRemainingCaption);
            JLabel monthsRemainingLabel = new JLabel(Integer.toString(monthsRemaining), JLabel.RIGHT);
            monthsRemainingLabel.setFont(labelFont);
            add(monthsRemainingLabel);
        } else if (account.getType() == Account.Type.LINE_OF_CREDIT) {
            LineOfCredit locAccount = (LineOfCredit) account;
            BigDecimal creditLimit = locAccount.getCreditLimit();

            JLabel creditLimitCaption = new JLabel("Credit limit");
            creditLimitCaption.setFont(labelFont);
            add(creditLimitCaption);
            JLabel creditLimitLabel = new JLabel(new DollarAmountFormatter().valueToString(creditLimit), JLabel.RIGHT);
            creditLimitLabel.setFont(labelFont);
            add(creditLimitLabel);
        }

        JLabel interestRateCaption = new JLabel("Interest rate");
        interestRateCaption.setFont(labelFont);
        add(interestRateCaption);
        JLabel interestRateLabel = new JLabel(new PercentageFormatter().valueToString(account.getInterestRate()), JLabel.RIGHT);
        interestRateLabel.setFont(labelFont);
        add(interestRateLabel);

        if (account.isClosed()) {
            JLabel closedLabel = new JLabel("Closed");
            closedLabel.setFont(labelFont);
            add(closedLabel);
        }
    }
}

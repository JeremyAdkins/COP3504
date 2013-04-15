package project.gui;

import project.Controller;
import project.gui.util.DollarAmountFormatter;
import project.gui.util.PercentageFormatter;
import project.model.Account;

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
        setLayout(new GridLayout(3, 2));

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

        JLabel interestRateCaption = new JLabel("Interest rate");
        interestRateCaption.setFont(labelFont);
        add(interestRateCaption);
        JLabel interestRateLabel = new JLabel(new PercentageFormatter().valueToString(account.getInterestRate()), JLabel.RIGHT);
        interestRateLabel.setFont(labelFont);
        add(interestRateLabel);
    }
}

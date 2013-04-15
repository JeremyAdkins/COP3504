package project.gui;

import project.Controller;
import project.gui.util.DollarAmountFormatter;
import project.model.Account;
import project.model.InsufficientFundsException;
import project.model.InvalidInputException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.text.ParseException;

public final class TellerAccountTab extends JPanel {
    private final Controller controller;

    private final Account account;

    private final AccountInfoPanel infoPanel;

    public TellerAccountTab(Controller controller, Account account) {
        this.controller = controller;
        this.account = account;
        infoPanel = new AccountInfoPanel(controller, account);
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        add(infoPanel, BorderLayout.NORTH);
        add(new JSeparator(), BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new GridLayout(3, 2));

        JButton depositButton = new JButton("Deposit");
        depositButton.setEnabled(account.getType() != Account.Type.CD);
        depositButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String amountStr = JOptionPane.showInputDialog(TellerAccountTab.this, "Deposit amount:", "Deposit",
                        JOptionPane.QUESTION_MESSAGE);
                try {
                    BigDecimal amount = new DollarAmountFormatter().stringToValue(amountStr);
                    account.deposit(amount);
                } catch (ParseException px) {
                    controller.handleException(TellerAccountTab.this, px);
                } catch (InvalidInputException iix) {
                    controller.handleException(TellerAccountTab.this, iix);
                }
            }
        });
        buttonPanel.add(depositButton);

        JButton withdrawButton = new JButton("Withdraw");
        withdrawButton.setEnabled(account.getType() != Account.Type.LOAN);
        withdrawButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String amountStr = JOptionPane.showInputDialog(TellerAccountTab.this, "Withdrawal amount:", "Withdraw",
                        JOptionPane.QUESTION_MESSAGE);
                try {
                    BigDecimal amount = new DollarAmountFormatter().stringToValue(amountStr);
                    account.withdraw(amount);
                } catch (ParseException px) {
                    controller.handleException(TellerAccountTab.this, px);
                } catch (InvalidInputException iix) {
                    controller.handleException(TellerAccountTab.this, iix);
                } catch (InsufficientFundsException ifx) {
                    controller.handleException(TellerAccountTab.this, ifx);
                }
            }
        });
        buttonPanel.add(withdrawButton);

        add(buttonPanel, BorderLayout.SOUTH);
    }
}

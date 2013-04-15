package project.gui;

import project.Controller;
import project.gui.util.DollarAmountFormatter;
import project.gui.util.IntegerFormatter;
import project.model.*;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public final class AccountTab extends javax.swing.JPanel {
    private static final DollarAmountFormatter FORMATTER = new DollarAmountFormatter();

    private final Controller controller;

    private final User accountOwner;

    private final Account account;

    private final AccountInfoPanel infoPanel;

    private JTable historyTable;

    public AccountTab(Controller controller, User accountOwner, Account account) {
        this.controller = controller;
        this.accountOwner = accountOwner;
        this.account = account;
        infoPanel = new AccountInfoPanel(controller, account);
        setName(account.toString());
        initComponents();
        update();
    }

    public Account getAccount() {
        return account;
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        JPanel sidePanel = new JPanel(new BorderLayout());
        sidePanel.add(infoPanel, BorderLayout.NORTH);
        sidePanel.add(new JSeparator(), BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new GridLayout(3, 1));

        JButton spendButton = new JButton("Spend");
        spendButton.setVisible(!account.isClosed() && controller.getCurrentUser() == accountOwner
                && (account.getType() == Account.Type.CHECKING || account.getType() == Account.Type.LINE_OF_CREDIT));
        spendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                spend();
            }
        });
        buttonPanel.add(spendButton);

        JButton transferButton = new JButton("Transfer");
        transferButton.setVisible(!account.isClosed() && controller.getCurrentUser() == accountOwner &&
                !account.getType().isLoan());
        transferButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                transfer();
            }
        });
        buttonPanel.add(transferButton);

        final JButton flagButton = new JButton("Flag transaction");
        flagButton.setEnabled(false);
        flagButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                flag();
            }
        });
        buttonPanel.add(flagButton);

        sidePanel.add(buttonPanel, BorderLayout.SOUTH);

        add(sidePanel, BorderLayout.WEST);

        historyTable = new JTable();
        historyTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        historyTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                flagButton.setEnabled(false);
                if (!account.isClosed() && historyTable.getSelectedRow() >= 0) {
                    Transaction transaction = account.getHistory().get(historyTable.getSelectedRow());
                    if (Bank.getInstance().getCurrentMonth() - transaction.getTimestamp() < 2) {
                        flagButton.setEnabled(true);
                    }
                }
            }
        });
        JScrollPane tablePane = new JScrollPane(historyTable);
        add(tablePane, BorderLayout.CENTER);
    }

    public void update() {
        infoPanel.update();

        Object[][] history = new Object[account.getHistory().size()][5];
        int i = 0;
        for (Transaction t : account.getHistory()) {
            history[i][0] = t.getType();
            BigDecimal amount;
            BigDecimal balance;
            if (account.getType().isLoan()) {
                amount = (t.getType().isPositive() ? t.getAmount().negate() : t.getAmount());
                balance = t.getBalance().negate();
            } else {
                amount = (t.getType().isPositive() ? t.getAmount() : t.getAmount().negate());
                balance = t.getBalance();
            }
            history[i][1] = FORMATTER.valueToString(amount);
            history[i][2] = FORMATTER.valueToString(balance);
            history[i][3] = t.getTimestamp();
            history[i][4] = t.getFraudStatus();
            i++;
        }
        historyTable.setModel(new javax.swing.table.DefaultTableModel(
                history,
                new String[]{
                        "Type", "Amount", "Balance", "Month", "Flag"
                }) {
            @Override
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return false;
            }
        });
        historyTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        historyTable.revalidate();
    }

    private void spend() {
        String amountStr = JOptionPane.showInputDialog(this, "Input an amount to spend:", "Spend", JOptionPane.QUESTION_MESSAGE);
        if (amountStr == null) {
            return;
        }
        String accountNumberStr = JOptionPane.showInputDialog(this, "To what account number? (blank is allowed)", "Spend", JOptionPane.QUESTION_MESSAGE);
        if (accountNumberStr == null) {
            return;
        }
        try {
            BigDecimal amount = FORMATTER.stringToValue(amountStr);
            Account target;
            if (accountNumberStr.isEmpty()) {
                target = null;
            } else {
                int accountNumber = new IntegerFormatter().stringToValue(accountNumberStr);
                target = null;
                for (User user : Bank.getInstance().getUsers()) {
                    for (Account account : user.getAccounts()) {
                        if (account.getAccountNumber() == accountNumber) {
                            target = account;
                        }
                    }
                }
            }
            if (target != null && (target.getType() == Account.Type.CD ||
                    (target.getType().isLoan() && target.getBalance().negate().compareTo(amount) < 0))) {
                throw new InvalidInputException(accountNumberStr, "account does not exist or cannot accept this deposit");
            }
            account.withdraw(amount);
            if (target != null) {
                target.deposit(amount);
            }
            controller.updateBankDisplay();
        } catch (ParseException px) {
            controller.handleException(this, px);
        } catch (InvalidInputException iix) {
            controller.handleException(this, iix);
        } catch (InsufficientFundsException ifx) {
            controller.handleException(this, ifx);
        }
    }

    private void transfer() {
        String amountStr = JOptionPane.showInputDialog(this, "Input an amount to transfer:", "Transfer", JOptionPane.QUESTION_MESSAGE);
        if (amountStr == null) {
            return;
        }
        try {
            BigDecimal amount = FORMATTER.stringToValue(amountStr);
            Set<Account> accounts = new HashSet<Account>(controller.getCurrentUser().getAccounts());
            Iterator<Account> iterator = accounts.iterator();
            while (iterator.hasNext()) {
                Account account = iterator.next();
                if (account.getType() == Account.Type.CD) {
                    iterator.remove();
                } else if (account.getType().isLoan() && account.getBalance().negate().compareTo(amount) < 0) {
                    iterator.remove();
                }
            }
            if (!accounts.isEmpty()) {
                Account account = (Account) JOptionPane.showInputDialog(this, "Choose an account:", "Transfer",
                        JOptionPane.QUESTION_MESSAGE, null, accounts.toArray(), accounts.iterator().next());
                if (account != null) {
                    this.account.withdraw(amount);
                    account.deposit(amount);
                    controller.updateBankDisplay();
                }
            } else {
                JOptionPane.showMessageDialog(this, "No accounts can accept that balance.", "Transfer failed", JOptionPane.ERROR_MESSAGE);
                return;
            }
        } catch (ParseException px) {
            controller.handleException(this, px);
        } catch (InvalidInputException iix) {
            controller.handleException(this, iix);
        } catch (InsufficientFundsException ifx) {
            controller.handleException(this, ifx);
        }
    }

    private void flag() {
        Set<Transaction.FraudStatus> permittedFlags = EnumSet.allOf(Transaction.FraudStatus.class);
        if (controller.getCurrentUser().getRole() != User.Role.AUDITOR) {
            permittedFlags.remove(Transaction.FraudStatus.REVERSED);
        }

        Transaction.FraudStatus status = (Transaction.FraudStatus) JOptionPane.showInputDialog(this, "Flag as:", "Flag",
                JOptionPane.QUESTION_MESSAGE, null, permittedFlags.toArray(), Transaction.FraudStatus.NOT_FLAGGED);
        if (status == null) {
            return;
        }
        try {
            Transaction transaction = account.getHistory().get(historyTable.getSelectedRow());
            transaction.setFraudStatus(status, account);
            update();
        } catch (InvalidInputException iix) {
            controller.handleException(this, iix);
        } catch (InsufficientFundsException ifx) {
            controller.handleException(this, ifx);
        }
    }
}

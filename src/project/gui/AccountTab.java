package project.gui;

import project.Controller;
import project.gui.util.DollarAmountFormatter;
import project.gui.util.PercentageFormatter;
import project.model.Account;
import project.model.InsufficientFundsException;
import project.model.InvalidInputException;
import project.model.Transaction;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public final class AccountTab extends javax.swing.JPanel {
    private static final DollarAmountFormatter FORMATTER = new DollarAmountFormatter();

    private static final PercentageFormatter PERCENTAGE_FORMATTER = new PercentageFormatter();

    private final Controller controller;

    private final Account account;

    private JLabel balanceLabel;

    private JTable historyTable;

    public AccountTab(Controller controller, Account account) {
        this.controller = controller;
        this.account = account;
        setName(account.toString());
        initComponents();
        update();
    }

    public void update() {
        updateLabels();
        updateHistoryTableModel();
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        JPanel sidePanel = new JPanel(new BorderLayout());

        final Font labelFont = new Font(Font.SANS_SERIF, Font.BOLD, 16);
        JPanel labelPanel = new JPanel(new GridLayout(3, 2));

        JLabel accountNumberCaption = new JLabel("Account number");
        accountNumberCaption.setFont(labelFont);
        labelPanel.add(accountNumberCaption);
        JLabel accountNumberLabel = new JLabel(Integer.toString(account.getAccountNumber()), JLabel.RIGHT);
        accountNumberLabel.setFont(labelFont);
        labelPanel.add(accountNumberLabel);

        JLabel balanceCaption = new JLabel("Balance");
        balanceCaption.setFont(labelFont);
        labelPanel.add(balanceCaption);
        balanceLabel = new JLabel();
        balanceLabel.setFont(labelFont);
        balanceLabel.setHorizontalAlignment(JLabel.RIGHT);
        labelPanel.add(balanceLabel);

        JLabel interestRateCaption = new JLabel("Interest rate");
        interestRateCaption.setFont(labelFont);
        labelPanel.add(interestRateCaption);
        JLabel interestRateLabel = new JLabel(PERCENTAGE_FORMATTER.valueToString(account.getInterestRate()), JLabel.RIGHT);
        interestRateLabel.setFont(labelFont);
        labelPanel.add(interestRateLabel);

        sidePanel.add(labelPanel, BorderLayout.NORTH);
        sidePanel.add(new JSeparator(), BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));

        JButton spendButton = new JButton("Spend");
        spendButton.setEnabled(account.getType() == Account.Type.CHECKING || account.getType() == Account.Type.LINE_OF_CREDIT);
        spendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                spend();
            }
        });
        buttonPanel.add(spendButton);

        JButton transferButton = new JButton("Transfer");
        transferButton.setEnabled(!account.getType().isLoan());
        transferButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                transfer();
            }
        });
        buttonPanel.add(transferButton);

        sidePanel.add(buttonPanel, BorderLayout.SOUTH);

        add(sidePanel, BorderLayout.WEST);

        historyTable = new JTable();
        JScrollPane tablePane = new JScrollPane(historyTable);
        add(tablePane, BorderLayout.CENTER);
    }

    private void updateLabels() {
        BigDecimal balance = account.getBalance();
        if (account.getType().isLoan()) {
            balance = balance.negate();
        }
        balanceLabel.setText(FORMATTER.valueToString(balance));
    }

    private void updateHistoryTableModel() {
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
        try {
            BigDecimal amount = FORMATTER.stringToValue(amountStr);
            account.withdraw(amount);
            controller.updateBankDisplay();
        } catch (ParseException px) { // TODO
        } catch (InvalidInputException iix) {
            controller.handleException(this, iix);
        } catch (InsufficientFundsException ifx) { // TODO
        }
    }

    private void transfer() {
        String amountStr = JOptionPane.showInputDialog(this, "Input an amount to transfer:", "Transfer", JOptionPane.QUESTION_MESSAGE);
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
                this.account.withdraw(amount);
                account.deposit(amount);
                controller.updateBankDisplay();
            } else {
                JOptionPane.showMessageDialog(this, "No accounts can accept that balance.", "Transfer failed", JOptionPane.ERROR_MESSAGE);
                return;
            }
            // TODO
        } catch (ParseException px) {
        } catch (InvalidInputException iix) {
        } catch (InsufficientFundsException ifx) {
        }
    }
}

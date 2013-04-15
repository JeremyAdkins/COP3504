package project.gui;

import project.Controller;
import project.model.Account;
import project.model.Transaction;

import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;
import java.math.RoundingMode;

public final class AccountTab extends javax.swing.JPanel {
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

        JPanel labelPanel = new JPanel(new GridLayout(3, 2));
        labelPanel.add(new JLabel("Account number"));
        JLabel accountNumberLabel = new JLabel(Integer.toString(account.getAccountNumber()));
        accountNumberLabel.setHorizontalAlignment(JLabel.RIGHT);
        labelPanel.add(accountNumberLabel);
        labelPanel.add(new JLabel("Balance"));
        balanceLabel = new JLabel();
        balanceLabel.setHorizontalAlignment(JLabel.RIGHT);
        labelPanel.add(balanceLabel);
        labelPanel.add(new JLabel("Interest rate"));
        JLabel interestRateLabel = new JLabel(String.format("%.2f%%", account.getInterestRate().multiply(new BigDecimal("100"))));
        interestRateLabel.setHorizontalAlignment(JLabel.RIGHT);
        labelPanel.add(interestRateLabel);
        add(labelPanel, BorderLayout.WEST);

        historyTable = new JTable();
        JScrollPane tablePane = new JScrollPane(historyTable);
        add(tablePane, BorderLayout.CENTER);
    }

    private void updateLabels() {
        // TODO maybe formatting should be in one place
        balanceLabel.setText("$" + String.valueOf(account.getBalance().setScale(2, RoundingMode.HALF_UP)));
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
            history[i][1] = "$" + amount;
            history[i][2] = "$" + balance;
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
}

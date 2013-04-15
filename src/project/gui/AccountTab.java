package project.gui;

import project.Controller;
import project.gui.util.DollarAmountFormatter;
import project.gui.util.PercentageFormatter;
import project.model.Account;
import project.model.Transaction;

import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;

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
}

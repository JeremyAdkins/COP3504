package project.gui;

import project.Controller;
import project.model.Bank;
import project.model.InvalidInputException;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.math.BigDecimal;
import java.text.ParseException;

/*
 * TODO consider only accessing Bank through Controller
 */
public final class AccountantFrame extends AbstractUserWindow {
    private final JTable statisticTable;

    public AccountantFrame(Controller controller) {
        super(controller);
        statisticTable = new JTable();
        initComponents();
        updateStatisticTable();
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        add(statisticTable, BorderLayout.CENTER);

        JPanel loanCapPanel = new JPanel(new GridLayout(1, 2));
        loanCapPanel.add(new JLabel("Loan cap:"));
        JFormattedTextField loanCapField = new JFormattedTextField(new OperationsManagerFrame.DollarAmountFormatterFactory(), Bank.getInstance().getLoanCap());
        loanCapField.setInputVerifier(new InputVerifier() {
            @Override
            public boolean verify(JComponent input) {
                try {
                    ((JFormattedTextField) input).commitEdit();
                    BigDecimal value = (BigDecimal) ((JFormattedTextField) input).getValue();
                    Bank.getInstance().setLoanCap(value);
                    return true;
                } catch (InvalidInputException iix) {
                    controller.handleException(AccountantFrame.this, iix);
                    return false;
                } catch (ParseException px) {
                    return false;
                }
            }

            @Override
            public boolean shouldYieldFocus(JComponent input) {
                return verify(input);
            }
        });
        loanCapPanel.add(loanCapField);
        add(loanCapPanel, BorderLayout.SOUTH);
    }

    public void updateStatisticTable() {
        statisticTable.setModel(new DefaultTableModel(
                controller.updateAccountantTableView(),
                new String[] { "Statistic", "Value" }) {
            @Override
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return false;
            }
        });
        statisticTable.setTableHeader(new JTableHeader(statisticTable.getColumnModel()));
        statisticTable.revalidate();
        pack();
    }
}

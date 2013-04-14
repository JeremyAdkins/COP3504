package project.gui;

import project.Controller;
import project.model.Bank;
import project.model.InvalidInputException;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
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
        add(new JScrollPane(statisticTable), BorderLayout.CENTER);

        JPanel loanCapPanel = new JPanel(new GridLayout(1, 2));
        loanCapPanel.add(new JLabel("Loan cap:"));
        JFormattedTextField loanCapField = new JFormattedTextField(new JFormattedTextField.AbstractFormatterFactory() {
            @Override
            public JFormattedTextField.AbstractFormatter getFormatter(JFormattedTextField tf) {
                return new JFormattedTextField.AbstractFormatter() {
                    @Override
                    public Object stringToValue(String text) throws ParseException {
                        if (text == null || text.isEmpty()) {
                            return null;
                        } else {
                            try {
                                return new BigDecimal(text);
                            } catch (NumberFormatException nfx) {
                                throw new ParseException(nfx.getMessage(), 0);
                            }
                        }
                    }

                    @Override
                    public String valueToString(Object value) throws ParseException {
                        if (value == null) {
                            return "";
                        } else if (value instanceof BigDecimal) {
                            return String.format("%.2f", value);
                        } else {
                            throw new ParseException("value is not a BigDecimal", 0);
                        }
                    }
                };
            }
        }, Bank.getInstance().getLoanCap());
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
        statisticTable.revalidate();
        pack();
    }
}

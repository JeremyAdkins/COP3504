package project.gui;

import project.Controller;
import project.model.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Collections;
import java.util.Set;

/*
 * TODO consider only accessing Bank through Controller
 */
public class OperationsManagerFrame extends AbstractUserWindow {
    static final class DollarAmountFormatter extends JFormattedTextField.AbstractFormatter {
        @Override
        public Object stringToValue(String text) throws ParseException {
            try {
                return new BigDecimal(text);
            } catch (NumberFormatException nfx) {
                throw new ParseException(nfx.getMessage(), 0);
            }
        }

        @Override
        public String valueToString(Object value) throws ParseException {
            if (value instanceof BigDecimal) {
                return String.format("%.2f", value);
            } else {
                throw new ParseException("value is not a BigDecimal", 0);
            }
        }
    }

    static final class DollarAmountFormatterFactory extends JFormattedTextField.AbstractFormatterFactory {
        @Override
        public JFormattedTextField.AbstractFormatter getFormatter(JFormattedTextField tf) {
            return new DollarAmountFormatter();
        }
    }

    static final class PercentageFormatter extends JFormattedTextField.AbstractFormatter {
        @Override
        public Object stringToValue(String text) throws ParseException {
            try {
                return new BigDecimal(text).divide(new BigDecimal(100), Bank.MATH_CONTEXT);
            } catch (NumberFormatException nfx) {
                throw new ParseException(nfx.getMessage(), 0);
            }
        }

        @Override
        public String valueToString(Object value) throws ParseException {
            if (value instanceof BigDecimal) {
                return String.format("%.2f", ((BigDecimal) value).multiply(new BigDecimal("100")));
            } else {
                throw new ParseException("value is not a BigDecimal", 0);
            }
        }
    }

    static final class PercentageFormatterFactory extends JFormattedTextField.AbstractFormatterFactory {
        @Override
        public JFormattedTextField.AbstractFormatter getFormatter(JFormattedTextField tf) {
            return new PercentageFormatter();
        }
    }

    private abstract class FieldInputVerifier extends InputVerifier {
        @Override
        public boolean verify(JComponent input) {
            try {
                ((JFormattedTextField) input).commitEdit();
                setField((BigDecimal) ((JFormattedTextField) input).getValue());
                return true;
            } catch (InvalidInputException iix) {
                controller.handleException(OperationsManagerFrame.this, iix);
                return false;
            } catch (ParseException px) {
                return false;
            }
        }

        @Override
        public boolean shouldYieldFocus(JComponent input) {
            return verify(input);
        }

        protected abstract void setField(BigDecimal value) throws InvalidInputException;
    }

    public OperationsManagerFrame(Controller controller) {
        super(controller);
        setLayout(new BorderLayout());

        JPanel westLayoutPanel = new JPanel(new BorderLayout());
        westLayoutPanel.add(initInterestComponents(), BorderLayout.NORTH);
        JPanel buttonLayoutPanel = new JPanel(new GridLayout(3, 1));
        westLayoutPanel.add(buttonLayoutPanel, BorderLayout.SOUTH);
        add(westLayoutPanel, BorderLayout.WEST);

        JButton accountStatementButton = new JButton("Account statement");
        accountStatementButton.addActionListener(new StatementGenerator(false));
        buttonLayoutPanel.add(accountStatementButton);

        JButton bankStatementButton = new JButton("Bank statement");
        bankStatementButton.addActionListener(new StatementGenerator(true));
        buttonLayoutPanel.add(bankStatementButton);

        JButton advanceTimeButton = new JButton("Advance time");
        advanceTimeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Bank.getInstance().advanceCurrentMonth();
                int currentMonth = Bank.getInstance().getCurrentMonth();
                JOptionPane.showMessageDialog(OperationsManagerFrame.this, "Advanced time by one month; the simulation month is now " + currentMonth, "Advanced time", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        buttonLayoutPanel.add(advanceTimeButton);

        JPanel eastLayoutPanel = new JPanel(new BorderLayout());
        eastLayoutPanel.add(initFeeComponents(), BorderLayout.NORTH);
        eastLayoutPanel.add(initThresholdComponents(), BorderLayout.SOUTH);
        add(eastLayoutPanel, BorderLayout.EAST);

        pack();
    }
    
    private JPanel initInterestComponents() {
        final PaymentSchedule paymentSchedule = Bank.getInstance().getPaymentSchedule();

        JPanel interestPanel = new JPanel(new GridLayout(10, 2));
        interestPanel.add(new JLabel("Interest rates"));
        interestPanel.add(new JLabel());

        interestPanel.add(new JLabel("Savings"));
        final JFormattedTextField savingsInterestField = new JFormattedTextField(new PercentageFormatterFactory(), paymentSchedule.getSavingsInterest());
        savingsInterestField.setInputVerifier(new FieldInputVerifier() {
            @Override
            protected void setField(BigDecimal value) throws InvalidInputException {
                paymentSchedule.setSavingsInterest(value);
            }
        });
        interestPanel.add(savingsInterestField);

        for (final CertificateOfDeposit.Term term : CertificateOfDeposit.Term.values()) {
            interestPanel.add(new JLabel("CD " + term.toString()));
            JFormattedTextField cdInterestField = new JFormattedTextField(new PercentageFormatterFactory(), paymentSchedule.getCdInterest(term));
            cdInterestField.setInputVerifier(new FieldInputVerifier() {
                @Override
                protected void setField(BigDecimal value) throws InvalidInputException {
                    paymentSchedule.setCdInterest(term, value);
                }
            });
            interestPanel.add(cdInterestField);
        }

        interestPanel.add(new JLabel("Loan"));
        JFormattedTextField loanInterestField = new JFormattedTextField(new PercentageFormatterFactory(), paymentSchedule.getLoanInterest());
        loanInterestField.setInputVerifier(new FieldInputVerifier() {
            @Override
            protected void setField(BigDecimal value) throws InvalidInputException {
                paymentSchedule.setLoanInterest(value);
            }
        });
        interestPanel.add(loanInterestField);

        interestPanel.add(new JLabel("Line of credit"));
        JFormattedTextField locInterestField = new JFormattedTextField(new PercentageFormatterFactory(), paymentSchedule.getLocInterest());
        locInterestField.setInputVerifier(new FieldInputVerifier() {
            @Override
            protected void setField(BigDecimal value) throws InvalidInputException {
                paymentSchedule.setLocInterest(value);
            }
        });
        interestPanel.add(locInterestField);

        return interestPanel;
    }

    private JPanel initFeeComponents() {
        final PaymentSchedule paymentSchedule = Bank.getInstance().getPaymentSchedule();

        JPanel feePanel = new JPanel(new GridLayout(6, 2));
        feePanel.add(new JLabel("Fees"));
        feePanel.add(new JLabel());

        feePanel.add(new JLabel("Savings monthly"));
        JFormattedTextField savingsMonthlyField = new JFormattedTextField(new DollarAmountFormatterFactory(), paymentSchedule.getSavingsCharge());
        savingsMonthlyField.setInputVerifier(new FieldInputVerifier() {
            @Override
            protected void setField(BigDecimal value) throws InvalidInputException {
                paymentSchedule.setSavingsCharge(value);
            }
        });
        feePanel.add(savingsMonthlyField);

        feePanel.add(new JLabel("Checking monthly"));
        JFormattedTextField checkingMonthlyField = new JFormattedTextField(new DollarAmountFormatterFactory(), paymentSchedule.getCheckingCharge());
        checkingMonthlyField.setInputVerifier(new FieldInputVerifier() {
            @Override
            protected void setField(BigDecimal value) throws InvalidInputException {
                paymentSchedule.setCheckingCharge(value);
            }
        });
        feePanel.add(checkingMonthlyField);

        feePanel.add(new JLabel("Checking overdraft"));
        JFormattedTextField checkingOverdraftField = new JFormattedTextField(new DollarAmountFormatterFactory(), paymentSchedule.getOverdraftFee());
        checkingOverdraftField.setInputVerifier(new FieldInputVerifier() {
            @Override
            protected void setField(BigDecimal value) throws InvalidInputException {
                paymentSchedule.setOverdraftFee(value);
            }
        });
        feePanel.add(checkingOverdraftField);

        feePanel.add(new JLabel("Loan penalty"));
        JFormattedTextField loanPenaltyField = new JFormattedTextField(new DollarAmountFormatterFactory(), paymentSchedule.getLoanPenalty());
        loanPenaltyField.setInputVerifier(new FieldInputVerifier() {
            @Override
            protected void setField(BigDecimal value) throws InvalidInputException {
                paymentSchedule.setLoanPenalty(value);
            }
        });
        feePanel.add(loanPenaltyField);

        feePanel.add(new JLabel("LoC penalty"));
        JFormattedTextField locPenaltyField = new JFormattedTextField(new DollarAmountFormatterFactory(), paymentSchedule.getLocPenalty());
        locPenaltyField.setInputVerifier(new FieldInputVerifier() {
            @Override
            protected void setField(BigDecimal value) throws InvalidInputException {
                paymentSchedule.setLocPenalty(value);
            }
        });
        feePanel.add(locPenaltyField);

        return feePanel;
    }
    
    private JPanel initThresholdComponents() {
        final PaymentSchedule paymentSchedule = Bank.getInstance().getPaymentSchedule();
        
        JPanel thresholdPanel = new JPanel(new GridLayout(7, 2));
        thresholdPanel.add(new JLabel("Thresholds and limits"));
        thresholdPanel.add(new JLabel());
        
        thresholdPanel.add(new JLabel("Savings fee threshold"));
        JFormattedTextField savingsThresholdField = new JFormattedTextField(new DollarAmountFormatterFactory(), paymentSchedule.getSavingsThreshold());
        savingsThresholdField.setInputVerifier(new FieldInputVerifier() {
            @Override
            protected void setField(BigDecimal value) throws InvalidInputException {
                paymentSchedule.setSavingsThreshold(value);
            }
        });
        thresholdPanel.add(savingsThresholdField);

        thresholdPanel.add(new JLabel("Checking fee threshold"));
        JFormattedTextField checkingThresholdField = new JFormattedTextField(new DollarAmountFormatterFactory(), paymentSchedule.getCheckingThreshold());
        checkingThresholdField.setInputVerifier(new FieldInputVerifier() {
            @Override
            protected void setField(BigDecimal value) throws InvalidInputException {
                paymentSchedule.setCheckingThreshold(value);
            }
        });
        thresholdPanel.add(checkingThresholdField);

        thresholdPanel.add(new JLabel("Overdraft limit"));
        JFormattedTextField overdraftLimitField = new JFormattedTextField(new DollarAmountFormatterFactory(), paymentSchedule.getOverdraftLimit());
        overdraftLimitField.setInputVerifier(new FieldInputVerifier() {
            @Override
            protected void setField(BigDecimal value) throws InvalidInputException {
                paymentSchedule.setOverdraftLimit(value);
            }
        });
        thresholdPanel.add(overdraftLimitField);

        thresholdPanel.add(new JLabel("CD minimum"));
        JFormattedTextField cdMinimumField = new JFormattedTextField(new DollarAmountFormatterFactory(), paymentSchedule.getCdMinimum());
        cdMinimumField.setInputVerifier(new FieldInputVerifier() {
            @Override
            protected void setField(BigDecimal value) throws InvalidInputException {
                paymentSchedule.setCdMinimum(value);
            }
        });
        thresholdPanel.add(cdMinimumField);

        thresholdPanel.add(new JLabel("LoC fixed minimum payment"));
        JFormattedTextField locFixedPaymentField = new JFormattedTextField(new DollarAmountFormatterFactory(), paymentSchedule.getLocFixedPayment());
        locFixedPaymentField.setInputVerifier(new FieldInputVerifier() {
            @Override
            protected void setField(BigDecimal value) throws InvalidInputException {
                paymentSchedule.setLocFixedPayment(value);
            }
        });
        thresholdPanel.add(locFixedPaymentField);

        thresholdPanel.add(new JLabel("LoC percentage minimum payment"));
        JFormattedTextField locPercentPaymentField = new JFormattedTextField(new PercentageFormatterFactory(), paymentSchedule.getLocPercentPayment());
        locPercentPaymentField.setInputVerifier(new FieldInputVerifier() {
            @Override
            protected void setField(BigDecimal value) throws InvalidInputException {
                paymentSchedule.setLocPercentPayment(value);
            }
        });
        thresholdPanel.add(locPercentPaymentField);

        return thresholdPanel;
    }

    private final class StatementGenerator implements ActionListener {
        private final boolean allAccounts;

        private StatementGenerator(boolean allAccounts) {
            this.allAccounts = allAccounts;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            String username = JOptionPane.showInputDialog(OperationsManagerFrame.this, "Username of customer:",
                    "Customer", JOptionPane.QUESTION_MESSAGE);
            try {
                User user = Bank.getInstance().getUser(username);
                if (user.getAccounts().isEmpty()) {
                    throw new InvalidInputException(username, "this user has no accounts");
                }
                Set<Account> statementAccounts;
                if (allAccounts) {
                    statementAccounts = user.getAccounts();
                } else {
                    Account account = (Account) JOptionPane.showInputDialog(OperationsManagerFrame.this, "Which account?",
                            "Account", JOptionPane.QUESTION_MESSAGE, null, user.getAccounts().toArray(),
                            user.getAccounts().iterator().next());
                    statementAccounts = Collections.singleton(account);
                }
                String statement = "";
                for (Account account : statementAccounts) {
                    statement += account.generateStatement() + "\n\n";
                }

                JFrame statementFrame = new JFrame("Statement");
                statementFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                JTextArea textArea = new JTextArea(user.toString() + "\n\n" + statement);
                textArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
                textArea.setEditable(false);
                statementFrame.add(textArea);
                statementFrame.pack();
                statementFrame.setVisible(true);
            } catch (InvalidInputException iix) {
                OperationsManagerFrame.this.controller.handleException(OperationsManagerFrame.this, iix);
            }
        }
    }
}

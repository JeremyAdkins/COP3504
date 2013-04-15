package project.gui;

import project.Controller;
import project.gui.util.DollarAmountFormatter;
import project.gui.util.FieldInputVerifier;
import project.gui.util.PercentageFormatter;
import project.model.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.Set;

/*
 * TODO consider only accessing Bank through Controller
 */
public class OperationsManagerFrame extends AbstractUserWindow {
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

        // TODO display the number of errors
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
        final JFormattedTextField savingsInterestField = new JFormattedTextField(new PercentageFormatter.Factory(), paymentSchedule.getSavingsInterest());
        savingsInterestField.setInputVerifier(new FieldInputVerifier(this) {
            @Override
            protected void setField(BigDecimal value) throws InvalidInputException {
                paymentSchedule.setSavingsInterest(value);
            }
        });
        interestPanel.add(savingsInterestField);

        for (final CertificateOfDeposit.Term term : CertificateOfDeposit.Term.values()) {
            interestPanel.add(new JLabel("CD " + term.toString()));
            JFormattedTextField cdInterestField = new JFormattedTextField(new PercentageFormatter.Factory(), paymentSchedule.getCdInterest(term));
            cdInterestField.setInputVerifier(new FieldInputVerifier(this) {
                @Override
                protected void setField(BigDecimal value) throws InvalidInputException {
                    paymentSchedule.setCdInterest(term, value);
                }
            });
            interestPanel.add(cdInterestField);
        }

        interestPanel.add(new JLabel("Loan"));
        JFormattedTextField loanInterestField = new JFormattedTextField(new PercentageFormatter.Factory(), paymentSchedule.getLoanInterest());
        loanInterestField.setInputVerifier(new FieldInputVerifier(this) {
            @Override
            protected void setField(BigDecimal value) throws InvalidInputException {
                paymentSchedule.setLoanInterest(value);
            }
        });
        interestPanel.add(loanInterestField);

        interestPanel.add(new JLabel("Line of credit"));
        JFormattedTextField locInterestField = new JFormattedTextField(new PercentageFormatter.Factory(), paymentSchedule.getLocInterest());
        locInterestField.setInputVerifier(new FieldInputVerifier(this) {
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
        JFormattedTextField savingsMonthlyField = new JFormattedTextField(new DollarAmountFormatter.Factory(), paymentSchedule.getSavingsCharge());
        savingsMonthlyField.setInputVerifier(new FieldInputVerifier(this) {
            @Override
            protected void setField(BigDecimal value) throws InvalidInputException {
                paymentSchedule.setSavingsCharge(value);
            }
        });
        feePanel.add(savingsMonthlyField);

        feePanel.add(new JLabel("Checking monthly"));
        JFormattedTextField checkingMonthlyField = new JFormattedTextField(new DollarAmountFormatter.Factory(), paymentSchedule.getCheckingCharge());
        checkingMonthlyField.setInputVerifier(new FieldInputVerifier(this) {
            @Override
            protected void setField(BigDecimal value) throws InvalidInputException {
                paymentSchedule.setCheckingCharge(value);
            }
        });
        feePanel.add(checkingMonthlyField);

        feePanel.add(new JLabel("Checking overdraft"));
        JFormattedTextField checkingOverdraftField = new JFormattedTextField(new DollarAmountFormatter.Factory(), paymentSchedule.getOverdraftFee());
        checkingOverdraftField.setInputVerifier(new FieldInputVerifier(this) {
            @Override
            protected void setField(BigDecimal value) throws InvalidInputException {
                paymentSchedule.setOverdraftFee(value);
            }
        });
        feePanel.add(checkingOverdraftField);

        feePanel.add(new JLabel("Loan penalty"));
        JFormattedTextField loanPenaltyField = new JFormattedTextField(new DollarAmountFormatter.Factory(), paymentSchedule.getLoanPenalty());
        loanPenaltyField.setInputVerifier(new FieldInputVerifier(this) {
            @Override
            protected void setField(BigDecimal value) throws InvalidInputException {
                paymentSchedule.setLoanPenalty(value);
            }
        });
        feePanel.add(loanPenaltyField);

        feePanel.add(new JLabel("LoC penalty"));
        JFormattedTextField locPenaltyField = new JFormattedTextField(new DollarAmountFormatter.Factory(), paymentSchedule.getLocPenalty());
        locPenaltyField.setInputVerifier(new FieldInputVerifier(this) {
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
        JFormattedTextField savingsThresholdField = new JFormattedTextField(new DollarAmountFormatter.Factory(), paymentSchedule.getSavingsThreshold());
        savingsThresholdField.setInputVerifier(new FieldInputVerifier(this) {
            @Override
            protected void setField(BigDecimal value) throws InvalidInputException {
                paymentSchedule.setSavingsThreshold(value);
            }
        });
        thresholdPanel.add(savingsThresholdField);

        thresholdPanel.add(new JLabel("Checking fee threshold"));
        JFormattedTextField checkingThresholdField = new JFormattedTextField(new DollarAmountFormatter.Factory(), paymentSchedule.getCheckingThreshold());
        checkingThresholdField.setInputVerifier(new FieldInputVerifier(this) {
            @Override
            protected void setField(BigDecimal value) throws InvalidInputException {
                paymentSchedule.setCheckingThreshold(value);
            }
        });
        thresholdPanel.add(checkingThresholdField);

        thresholdPanel.add(new JLabel("Overdraft limit"));
        JFormattedTextField overdraftLimitField = new JFormattedTextField(new DollarAmountFormatter.Factory(), paymentSchedule.getOverdraftLimit());
        overdraftLimitField.setInputVerifier(new FieldInputVerifier(this) {
            @Override
            protected void setField(BigDecimal value) throws InvalidInputException {
                paymentSchedule.setOverdraftLimit(value);
            }
        });
        thresholdPanel.add(overdraftLimitField);

        thresholdPanel.add(new JLabel("CD minimum"));
        JFormattedTextField cdMinimumField = new JFormattedTextField(new DollarAmountFormatter.Factory(), paymentSchedule.getCdMinimum());
        cdMinimumField.setInputVerifier(new FieldInputVerifier(this) {
            @Override
            protected void setField(BigDecimal value) throws InvalidInputException {
                paymentSchedule.setCdMinimum(value);
            }
        });
        thresholdPanel.add(cdMinimumField);

        thresholdPanel.add(new JLabel("LoC fixed minimum payment"));
        JFormattedTextField locFixedPaymentField = new JFormattedTextField(new DollarAmountFormatter.Factory(), paymentSchedule.getLocFixedPayment());
        locFixedPaymentField.setInputVerifier(new FieldInputVerifier(this) {
            @Override
            protected void setField(BigDecimal value) throws InvalidInputException {
                paymentSchedule.setLocFixedPayment(value);
            }
        });
        thresholdPanel.add(locFixedPaymentField);

        thresholdPanel.add(new JLabel("LoC percentage minimum payment"));
        JFormattedTextField locPercentPaymentField = new JFormattedTextField(new PercentageFormatter.Factory(), paymentSchedule.getLocPercentPayment());
        locPercentPaymentField.setInputVerifier(new FieldInputVerifier(this) {
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
            if (username == null) {
                return;
            }
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
                    if (account == null) {
                        return;
                    }
                    statementAccounts = Collections.singleton(account);
                }
                if (statementAccounts.isEmpty()) {
                    return;
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

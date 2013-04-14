/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package project.gui;

import project.Controller;
import project.model.Bank;
import project.model.CertificateOfDeposit;
import project.model.InvalidInputException;
import project.model.PaymentSchedule;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import java.awt.*;
import java.math.BigDecimal;

/**
 * 
 * @author Rich
 */
public class OperationsManagerFrame extends AbstractUserWindow {
    private abstract class TextFieldListener implements DocumentListener {
        @Override
        public void insertUpdate(DocumentEvent e) {
            try {
                onUpdate(new BigDecimal(e.getDocument().getText(0, e.getDocument().getLength())));
            } catch (InvalidInputException iix) {
                JOptionPane.showMessageDialog(OperationsManagerFrame.this, iix.getMessage(), iix.getClass().getName(), JOptionPane.ERROR_MESSAGE);
            } catch (BadLocationException blx) {
                throw new AssertionError("should not occur");
            }
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            try {
                onUpdate(new BigDecimal(e.getDocument().getText(0, e.getDocument().getLength())));
            } catch (InvalidInputException iix) {
                JOptionPane.showMessageDialog(OperationsManagerFrame.this, iix.getMessage(), iix.getClass().getName(), JOptionPane.ERROR_MESSAGE);
            } catch (BadLocationException blx) {
                throw new AssertionError("should not occur");
            }
        }

        @Override
        public void changedUpdate(DocumentEvent e) {
            try {
                onUpdate(new BigDecimal(e.getDocument().getText(0, e.getDocument().getLength())));
            } catch (InvalidInputException iix) {
                JOptionPane.showMessageDialog(OperationsManagerFrame.this, iix.getMessage(), iix.getClass().getName(), JOptionPane.ERROR_MESSAGE);
            } catch (BadLocationException blx) {
                throw new AssertionError("should not occur");
            }
        }

        protected abstract void onUpdate(BigDecimal value) throws InvalidInputException;
    }

    public OperationsManagerFrame(Controller controller) {
        super(controller);
        setLayout(new GridLayout(1, 2));
        add(initInterestComponents());
        JPanel layoutPanel = new JPanel(new GridLayout(2, 1));
        layoutPanel.add(initFeeComponents());
        layoutPanel.add(initThresholdComponents());
        add(layoutPanel);
        pack();
    }
    
    private JPanel initInterestComponents() {
        final PaymentSchedule paymentSchedule = Bank.getInstance().getPaymentSchedule();

        JPanel interestPanel = new JPanel(new GridLayout(10, 2));
        interestPanel.add(new JLabel("Interest rates"));
        interestPanel.add(new JLabel());

        interestPanel.add(new JLabel("Savings"));
        String savingsInterest = String.format("%.2f", paymentSchedule.getSavingsInterest().multiply(new BigDecimal("100")));
        JTextField savingsInterestField = new JTextField(savingsInterest);
        savingsInterestField.getDocument().addDocumentListener(new TextFieldListener() {
            @Override
            protected void onUpdate(BigDecimal value) throws InvalidInputException {
                paymentSchedule.setSavingsInterest(value.divide(new BigDecimal("100"), Bank.MATH_CONTEXT));
            }
        });
        interestPanel.add(savingsInterestField);

        for (final CertificateOfDeposit.Term term : CertificateOfDeposit.Term.values()) {
            interestPanel.add(new JLabel("CD " + term.toString()));
            String cdInterest = String.format("%.2f", paymentSchedule.getCdInterest(term).multiply(new BigDecimal("100")));
            JTextField cdInterestField = new JTextField(cdInterest);
            cdInterestField.getDocument().addDocumentListener(new TextFieldListener() {
                @Override
                protected void onUpdate(BigDecimal value) throws InvalidInputException {
                    paymentSchedule.setCdInterest(term, value.divide(new BigDecimal("100"), Bank.MATH_CONTEXT));
                }
            });
            interestPanel.add(cdInterestField);
        }

        interestPanel.add(new JLabel("Loan"));
        String loanInterest = String.format("%.2f", paymentSchedule.getLoanInterest().multiply(new BigDecimal("100")));
        JTextField loanInterestField = new JTextField(loanInterest);
        loanInterestField.getDocument().addDocumentListener(new TextFieldListener() {
            @Override
            protected void onUpdate(BigDecimal value) throws InvalidInputException {
                paymentSchedule.setLoanInterest(value.divide(new BigDecimal("100"), Bank.MATH_CONTEXT));
            }
        });
        interestPanel.add(loanInterestField);

        interestPanel.add(new JLabel("Line of credit"));
        String locInterest = String.format("%.2f", paymentSchedule.getLocInterest().multiply(new BigDecimal("100")));
        JTextField locInterestField = new JTextField(locInterest);
        locInterestField.getDocument().addDocumentListener(new TextFieldListener() {
            @Override
            protected void onUpdate(BigDecimal value) throws InvalidInputException {
                paymentSchedule.setLocInterest(value.divide(new BigDecimal("100"), Bank.MATH_CONTEXT));
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
        String savingsMonthly = String.format("%.2f", paymentSchedule.getSavingsCharge());
        JTextField savingsMonthlyField = new JTextField(savingsMonthly);
        savingsMonthlyField.getDocument().addDocumentListener(new TextFieldListener() {
            @Override
            protected void onUpdate(BigDecimal value) throws InvalidInputException {
                paymentSchedule.setSavingsCharge(value);
            }
        });
        feePanel.add(savingsMonthlyField);

        feePanel.add(new JLabel("Checking monthly"));
        String checkingMonthly = String.format("%.2f", paymentSchedule.getCheckingCharge());
        JTextField checkingMonthlyField = new JTextField(checkingMonthly);
        checkingMonthlyField.getDocument().addDocumentListener(new TextFieldListener() {
            @Override
            protected void onUpdate(BigDecimal value) throws InvalidInputException {
                paymentSchedule.setCheckingCharge(value);
            }
        });
        feePanel.add(checkingMonthlyField);

        feePanel.add(new JLabel("Checking overdraft"));
        String checkingOverdraft = String.format("%.2f", paymentSchedule.getOverdraftFee());
        JTextField checkingOverdraftField = new JTextField(checkingOverdraft);
        checkingOverdraftField.getDocument().addDocumentListener(new TextFieldListener() {
            @Override
            protected void onUpdate(BigDecimal value) throws InvalidInputException {
                paymentSchedule.setOverdraftFee(value);
            }
        });
        feePanel.add(checkingOverdraftField);

        feePanel.add(new JLabel("Loan penalty"));
        String loanPenalty = String.format("%.2f", paymentSchedule.getLoanPenalty());
        JTextField loanPenaltyField = new JTextField(loanPenalty);
        loanPenaltyField.getDocument().addDocumentListener(new TextFieldListener() {
            @Override
            protected void onUpdate(BigDecimal value) throws InvalidInputException {
                paymentSchedule.setLoanPenalty(value);
            }
        });
        feePanel.add(loanPenaltyField);

        feePanel.add(new JLabel("LoC penalty"));
        String locPenalty = String.format("%.2f", paymentSchedule.getLocPenalty());
        JTextField locPenaltyField = new JTextField(locPenalty);
        locPenaltyField.getDocument().addDocumentListener(new TextFieldListener() {
            @Override
            protected void onUpdate(BigDecimal value) throws InvalidInputException {
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
        String savingsThreshold = String.format("%.2f", paymentSchedule.getSavingsThreshold());
        JTextField savingsThresholdField = new JTextField(savingsThreshold);
        savingsThresholdField.getDocument().addDocumentListener(new TextFieldListener() {
            @Override
            protected void onUpdate(BigDecimal value) throws InvalidInputException {
                paymentSchedule.setSavingsThreshold(value);
            }
        });
        thresholdPanel.add(savingsThresholdField);

        thresholdPanel.add(new JLabel("Checking fee threshold"));
        String checkingThreshold = String.format("%.2f", paymentSchedule.getCheckingThreshold());
        JTextField checkingThresholdField = new JTextField(checkingThreshold);
        checkingThresholdField.getDocument().addDocumentListener(new TextFieldListener() {
            @Override
            protected void onUpdate(BigDecimal value) throws InvalidInputException {
                paymentSchedule.setCheckingThreshold(value);
            }
        });
        thresholdPanel.add(checkingThresholdField);

        thresholdPanel.add(new JLabel("Overdraft limit"));
        String overdraftLimit = String.format("%.2f", paymentSchedule.getOverdraftLimit());
        JTextField overdraftLimitField = new JTextField(overdraftLimit);
        overdraftLimitField.getDocument().addDocumentListener(new TextFieldListener() {
            @Override
            protected void onUpdate(BigDecimal value) throws InvalidInputException {
                paymentSchedule.setOverdraftLimit(value);
            }
        });
        thresholdPanel.add(overdraftLimitField);

        thresholdPanel.add(new JLabel("CD minimum"));
        String cdMinimum = String.format("%.2f", paymentSchedule.getCdMinimum());
        JTextField cdMinimumField = new JTextField(cdMinimum);
        cdMinimumField.getDocument().addDocumentListener(new TextFieldListener() {
            @Override
            protected void onUpdate(BigDecimal value) throws InvalidInputException {
                paymentSchedule.setCdMinimum(value);
            }
        });
        thresholdPanel.add(cdMinimumField);

        thresholdPanel.add(new JLabel("LoC fixed minimum payment"));
        String locFixedPayment = String.format("%.2f", paymentSchedule.getLocFixedPayment());
        JTextField locFixedPaymentField = new JTextField(locFixedPayment);
        locFixedPaymentField.getDocument().addDocumentListener(new TextFieldListener() {
            @Override
            protected void onUpdate(BigDecimal value) throws InvalidInputException {
                paymentSchedule.setLocFixedPayment(value);
            }
        });
        thresholdPanel.add(locFixedPaymentField);

        thresholdPanel.add(new JLabel("LoC percentage minimum payment"));
        String locPercentPayment = String.format("%.2f", paymentSchedule.getLocPercentPayment().multiply(new BigDecimal("100")));
        JTextField locPercentPaymentField = new JTextField(locPercentPayment);
        locPercentPaymentField.getDocument().addDocumentListener(new TextFieldListener() {
            @Override
            protected void onUpdate(BigDecimal value) throws InvalidInputException {
                paymentSchedule.setLocPercentPayment(value.divide(new BigDecimal("100"), Bank.MATH_CONTEXT));
            }
        });
        thresholdPanel.add(locPercentPaymentField);

        return thresholdPanel;
    }
}

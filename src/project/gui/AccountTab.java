/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package project.gui;

import project.Controller;
import project.gui.util.DollarAmountFormatter;
import project.model.Account;
import project.model.InsufficientFundsException;
import project.model.InvalidInputException;
import project.model.Transaction;

import javax.swing.*;
import java.math.RoundingMode;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author rich
 */
public final class AccountTab extends javax.swing.JPanel {

    private Account account;
    private static AccountHolderFrame parentFrame;

    public static AccountHolderFrame getParentFrame() {
        return parentFrame;
    }
    private static Controller controller;

    public AccountTab(Account account, AccountHolderFrame parent, Controller controller) {
        this.account = account;
        this.parentFrame = parent;
        this.controller = controller;
        initComponents();
        updateBalanceLabel();
        updateHistoryTableModel();
        this.setName(account.toString());
    }

    public Account getAccount() {
        return account;
    }

    public void updateBalanceLabel() {
        BalanceLabel.setText("Balance: $" + String.valueOf(account.getBalance().setScale(2, RoundingMode.FLOOR)));
    }

    public void updateHistoryTableModel() {
        Object[][] history = new Object[account.getHistory().size()][4];
        int i = 0;
        for (Transaction t : account.getHistory()) {
            history[i][0] = t.getType();
            history[i][1] = "$" + t.getAmount();
            history[i][2] = t.getTimestamp();
            history[i][3] = t.getFraudStatus();
            i++;
        }
        HistoryTable.setModel(new javax.swing.table.DefaultTableModel(
                history,
                new String[]{
            "Type", "Transaction Amount", "Time Stamp", "Flag"
        }) {
            @Override
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return false;
            }
        });
        HistoryTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        HistoryTable.revalidate();
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        WithdrawDialog = new javax.swing.JDialog();
        WithdrawDialogButton = new javax.swing.JButton();
        CancelWithdraw = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        WithdrawAmountField = new javax.swing.JFormattedTextField();
        DepositDialog = new javax.swing.JDialog();
        DepositDialogButton = new javax.swing.JButton();
        CancelDeposit = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        DepositAmountField = new javax.swing.JFormattedTextField();
        AccountNumberLabel = new javax.swing.JLabel();
        historyLabel = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        HistoryTable = new javax.swing.JTable();
        Separator2 = new javax.swing.JSeparator();
        InterestRateLabel = new javax.swing.JLabel();
        BalanceLabel = new javax.swing.JLabel();
        WithdrawButton = new javax.swing.JButton();
        UseTeller = new javax.swing.JButton();
        DepositButton = new javax.swing.JButton();
        Separator1 = new javax.swing.JSeparator();

        WithdrawDialog.setTitle("Withdraw");
        WithdrawDialog.setLocationByPlatform(true);
        WithdrawDialog.setMinimumSize(null);
        WithdrawDialog.setModal(true);
        WithdrawDialog.setResizable(false);

        WithdrawDialogButton.setText("Withdraw");
        WithdrawDialogButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                WithdrawDialogButtonActionPerformed(evt);
            }
        });

        CancelWithdraw.setText("Cancel");
        CancelWithdraw.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CancelWithdrawActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel1.setText("Amount:   $");

        WithdrawAmountField.setFormatterFactory(new DollarAmountFormatter.Factory());

        javax.swing.GroupLayout WithdrawDialogLayout = new javax.swing.GroupLayout(WithdrawDialog.getContentPane());
        WithdrawDialog.getContentPane().setLayout(WithdrawDialogLayout);
        WithdrawDialogLayout.setHorizontalGroup(
            WithdrawDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(WithdrawDialogLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(WithdrawDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(WithdrawDialogLayout.createSequentialGroup()
                        .addComponent(WithdrawDialogButton, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(CancelWithdraw, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(WithdrawDialogLayout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(WithdrawAmountField)))
                .addContainerGap())
        );
        WithdrawDialogLayout.setVerticalGroup(
            WithdrawDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(WithdrawDialogLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(WithdrawDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(WithdrawAmountField))
                .addGap(18, 18, Short.MAX_VALUE)
                .addGroup(WithdrawDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(WithdrawDialogButton)
                    .addComponent(CancelWithdraw))
                .addContainerGap())
        );

        DepositDialog.setTitle("Deposit");
        DepositDialog.setLocationByPlatform(true);
        DepositDialog.setMinimumSize(null);
        DepositDialog.setModal(true);
        DepositDialog.setResizable(false);

        DepositDialogButton.setText("Deposit");
        DepositDialogButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                DepositDialogButtonActionPerformed(evt);
            }
        });

        CancelDeposit.setText("Cancel");
        CancelDeposit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CancelDepositActionPerformed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel2.setText("Amount:   $");

        DepositAmountField.setFormatterFactory(new DollarAmountFormatter.Factory());

        javax.swing.GroupLayout DepositDialogLayout = new javax.swing.GroupLayout(DepositDialog.getContentPane());
        DepositDialog.getContentPane().setLayout(DepositDialogLayout);
        DepositDialogLayout.setHorizontalGroup(
            DepositDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(DepositDialogLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(DepositDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(DepositDialogLayout.createSequentialGroup()
                        .addComponent(DepositDialogButton, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(CancelDeposit, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(DepositDialogLayout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(DepositAmountField)))
                .addContainerGap())
        );
        DepositDialogLayout.setVerticalGroup(
            DepositDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(DepositDialogLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(DepositDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(DepositAmountField))
                .addGap(18, 18, Short.MAX_VALUE)
                .addGroup(DepositDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(DepositDialogButton)
                    .addComponent(CancelDeposit))
                .addContainerGap())
        );

        AccountNumberLabel.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        AccountNumberLabel.setText("Acc. #: " + String.valueOf(account.getAccountNumber()));

        historyLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        historyLabel.setText("Transaction History");

        HistoryTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                HistoryTableMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(HistoryTable);

        Separator2.setOrientation(javax.swing.SwingConstants.VERTICAL);

        InterestRateLabel.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        InterestRateLabel.setText("Interest: " + String.valueOf(controller.getInterestRate(account) + "%"));

        BalanceLabel.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N

        WithdrawButton.setText("Withdraw");
        WithdrawButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                WithdrawButtonActionPerformed(evt);
            }
        });

        UseTeller.setText("Use Teller");
        UseTeller.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                UseTellerActionPerformed(evt);
            }
        });

        DepositButton.setText("Deposit");
        DepositButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                DepositButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(AccountNumberLabel)
                        .addComponent(BalanceLabel)
                        .addComponent(InterestRateLabel)
                        .addComponent(WithdrawButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(DepositButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(UseTeller))
                    .addComponent(Separator1, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(Separator2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(historyLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 363, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(AccountNumberLabel)
                        .addGap(18, 18, 18)
                        .addComponent(BalanceLabel)
                        .addGap(18, 18, 18)
                        .addComponent(InterestRateLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(Separator1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(WithdrawButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(DepositButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(UseTeller)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 208, Short.MAX_VALUE))
                    .addComponent(Separator2, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(historyLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 403, Short.MAX_VALUE)))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void HistoryTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_HistoryTableMouseClicked
    }//GEN-LAST:event_HistoryTableMouseClicked

    private void WithdrawButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_WithdrawButtonActionPerformed
        WithdrawDialog.pack();
        WithdrawDialog.setVisible(true);
    }//GEN-LAST:event_WithdrawButtonActionPerformed

    private void WithdrawDialogButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_WithdrawDialogButtonActionPerformed
        String amount = WithdrawAmountField.getText();
        int selection = JOptionPane.showConfirmDialog(this, "Are you sure you want to withdraw " + amount + "?");
        if (selection==JOptionPane.YES_OPTION) {
            WithdrawDialog.dispose();
            try {
                controller.withdraw(account, amount);
            } catch (InvalidInputException ex) {
                controller.handleException(this, ex);
            } catch (InsufficientFundsException ex) {
                // TODO handle in Controller
                Logger.getLogger(AccountTab.class.getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(parentFrame, "You have withdrawn more than your balance!", "Warning", JOptionPane.WARNING_MESSAGE);
            }
        }
    }//GEN-LAST:event_WithdrawDialogButtonActionPerformed

    private void CancelWithdrawActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CancelWithdrawActionPerformed
        WithdrawDialog.dispose();
    }//GEN-LAST:event_CancelWithdrawActionPerformed

    private void DepositButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_DepositButtonActionPerformed
        DepositDialog.pack();
        DepositDialog.setVisible(true);
    }//GEN-LAST:event_DepositButtonActionPerformed

    private void UseTellerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_UseTellerActionPerformed
        project.gui.LoginWindow temp = new project.gui.LoginWindow("Teller Login", controller);
        temp.confirmTellerLogin();
        temp.setVisible(true);
    }//GEN-LAST:event_UseTellerActionPerformed

    private void DepositDialogButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_DepositDialogButtonActionPerformed
        String amount = DepositAmountField.getText();
        int selection = JOptionPane.showConfirmDialog(this, "Are you sure you want to deposit " + amount + "?");
        if (selection==JOptionPane.YES_OPTION) {
            DepositDialog.dispose();
            try {
                controller.deposit(account, amount);
            } catch (InvalidInputException ex) {
                controller.handleException(this, ex);
            }
        }
    }//GEN-LAST:event_DepositDialogButtonActionPerformed

    private void CancelDepositActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CancelDepositActionPerformed
        DepositDialog.dispose();
    }//GEN-LAST:event_CancelDepositActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel AccountNumberLabel;
    private javax.swing.JLabel BalanceLabel;
    private javax.swing.JButton CancelDeposit;
    private javax.swing.JButton CancelWithdraw;
    private javax.swing.JFormattedTextField DepositAmountField;
    private javax.swing.JButton DepositButton;
    private javax.swing.JDialog DepositDialog;
    private javax.swing.JButton DepositDialogButton;
    private javax.swing.JTable HistoryTable;
    private javax.swing.JLabel InterestRateLabel;
    private javax.swing.JSeparator Separator1;
    private javax.swing.JSeparator Separator2;
    private javax.swing.JButton UseTeller;
    private javax.swing.JFormattedTextField WithdrawAmountField;
    private javax.swing.JButton WithdrawButton;
    private javax.swing.JDialog WithdrawDialog;
    private javax.swing.JButton WithdrawDialogButton;
    private javax.swing.JLabel historyLabel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane jScrollPane2;
    // End of variables declaration//GEN-END:variables
}

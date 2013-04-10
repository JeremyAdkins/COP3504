/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cop3504.project;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 *
 * @author rich
 */
public class AccountTab extends javax.swing.JPanel {

    /**
     * Creates new form CheckingAccountTab
     */
    private Account account;
    
    public AccountTab(Account account){
        this.account = account;
        initComponents();
        AccountNumber.setText("Acc. #: " + String.valueOf(account.getAccountNumber()));
        Balance.setText("Balance: $"+String.valueOf(account.getBalance().setScale(2, RoundingMode.FLOOR)));
        InterestRate.setText("Interest: "+String.valueOf(account.getInterestRate().multiply(BigDecimal.valueOf(100)).setScale(4, RoundingMode.FLOOR))+"%");
        Object[][] history = new Object[account.getHistory().size()][3];
        int i = 0;
        for(Transaction t : account.getHistory()){
            history[i][0] = t.getType();
            history[i][1] = t.getAmount();
            history[i][2] = t.getTimestamp();
            i++;
        }
        HistoryTable.setModel(new javax.swing.table.DefaultTableModel(
            history,
            new String [] {
                "Type", "Amount", "Time Stamp"
            }
        ));
    }

    public Account getAccount(){
        return account;
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
        WithdrawAmountField = new javax.swing.JTextField();
        WithdrawDialogButton = new javax.swing.JButton();
        CancelWithdraw = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        AccountNumber = new javax.swing.JLabel();
        historyLabel = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        HistoryTable = new javax.swing.JTable();
        Separator2 = new javax.swing.JSeparator();
        InterestRate = new javax.swing.JLabel();
        Balance = new javax.swing.JLabel();
        Withdraw = new javax.swing.JButton();
        UseTeller = new javax.swing.JButton();
        Deposit = new javax.swing.JButton();
        Separator1 = new javax.swing.JSeparator();

        WithdrawDialog.setTitle("Withdraw");
        WithdrawDialog.setLocationByPlatform(true);
        WithdrawDialog.setMaximumSize(null);
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

        jLabel1.setText("Amount:   $");

        javax.swing.GroupLayout WithdrawDialogLayout = new javax.swing.GroupLayout(WithdrawDialog.getContentPane());
        WithdrawDialog.getContentPane().setLayout(WithdrawDialogLayout);
        WithdrawDialogLayout.setHorizontalGroup(
            WithdrawDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(WithdrawDialogLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(WithdrawDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(WithdrawDialogLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(WithdrawDialogButton)
                        .addGap(18, 18, 18)
                        .addComponent(CancelWithdraw))
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
                .addGroup(WithdrawDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(WithdrawAmountField))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(WithdrawDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(WithdrawDialogButton)
                    .addComponent(CancelWithdraw))
                .addContainerGap())
        );

        WithdrawAmountField.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if ( ((c < '0') || (c > '9')) && (c != KeyEvent.VK_BACK_SPACE) && (c != '.')) {
                    e.consume();  // ignore event
                }
            }
        });

        AccountNumber.setText("Acc. Number");

        historyLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        historyLabel.setText("Transaction History");

        HistoryTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"Hello", "World", "!", null}
            },
            new String [] {
                "Type", "Amount", "Time Stamp", "Flag"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        HistoryTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                HistoryTableMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(HistoryTable);

        Separator2.setOrientation(javax.swing.SwingConstants.VERTICAL);

        InterestRate.setText("Interest Rate");

        Balance.setText("Balance");

        Withdraw.setText("Withdraw");
        Withdraw.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                WithdrawActionPerformed(evt);
            }
        });

        UseTeller.setText("Use Teller");

        Deposit.setText("Deposit");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 519, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(AccountNumber)
                            .addComponent(Balance)
                            .addComponent(InterestRate)
                            .addComponent(Withdraw, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(Deposit, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(UseTeller))
                        .addComponent(Separator1, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(Separator2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(historyLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 363, Short.MAX_VALUE))
                    .addContainerGap()))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 423, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(AccountNumber)
                            .addGap(18, 18, 18)
                            .addComponent(Balance)
                            .addGap(18, 18, 18)
                            .addComponent(InterestRate)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(Separator1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(18, 18, 18)
                            .addComponent(Withdraw)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(Deposit)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(UseTeller)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 217, Short.MAX_VALUE))
                        .addComponent(Separator2, javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(historyLabel)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)))
                    .addContainerGap()))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void HistoryTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_HistoryTableMouseClicked
        
    }//GEN-LAST:event_HistoryTableMouseClicked

    private void WithdrawActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_WithdrawActionPerformed
        WithdrawDialog.pack();
        WithdrawDialog.setVisible(true);
    }//GEN-LAST:event_WithdrawActionPerformed

    private void CancelWithdrawActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CancelWithdrawActionPerformed
        WithdrawDialog.dispose();
    }//GEN-LAST:event_CancelWithdrawActionPerformed

    private void WithdrawDialogButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_WithdrawDialogButtonActionPerformed
        WithdrawDialog.dispose();
        try {
            account.withdraw(new BigDecimal(Double.parseDouble(WithdrawAmountField.getText())));
        } catch (OverdraftException e) {}
    }//GEN-LAST:event_WithdrawDialogButtonActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel AccountNumber;
    private javax.swing.JLabel Balance;
    private javax.swing.JButton CancelWithdraw;
    private javax.swing.JButton Deposit;
    private javax.swing.JTable HistoryTable;
    private javax.swing.JLabel InterestRate;
    private javax.swing.JSeparator Separator1;
    private javax.swing.JSeparator Separator2;
    private javax.swing.JButton UseTeller;
    private javax.swing.JButton Withdraw;
    private javax.swing.JTextField WithdrawAmountField;
    private javax.swing.JDialog WithdrawDialog;
    private javax.swing.JButton WithdrawDialogButton;
    private javax.swing.JLabel historyLabel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane2;
    // End of variables declaration//GEN-END:variables
}

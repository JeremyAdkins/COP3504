/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package project.gui;

import project.Controller;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

/**
 *
 * @author rich
 */
public abstract class AbstractUserWindow extends JFrame {

    public final Controller controller;
    //GUI commonalities
    public JMenuBar menuBar = new JMenuBar();
    public JMenu FileMenu = new JMenu();
    public JMenuItem LogoutMenuItem = new JMenuItem();

    public AbstractUserWindow(Controller controller) {
        this.controller = controller;
        buildPartialGUI();
    }

    private void buildPartialGUI() {
        //The Frame itself
        setTitle(controller.getCurrentUser().getLastName() + ", " + controller.getCurrentUser().getFirstName()/*+" : "+controller.getCurrentUser().getRole().toString()*/);
        setName("myName");
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setLocationByPlatform(true);
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent evt) {
                logout();
            }
        });

        setJMenuBar(menuBar);
        FileMenu.setText("File");
        menuBar.add(FileMenu);
        LogoutMenuItem.setText("Logout");
        //add Logout menuItem
        FileMenu.add(LogoutMenuItem);
        LogoutMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_L, java.awt.event.InputEvent.CTRL_MASK));
        LogoutMenuItem.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                logout();
            }
        });
//        //add newUserLogin menuItem
//        newLoginMenuItem.setText("New User Login");
//        FileMenu.add(newLoginMenuItem);
//        newLoginMenuItem.addActionListener(new java.awt.event.ActionListener() {
//            @Override
//            public void actionPerformed(java.awt.event.ActionEvent evt) {
//                newLogin();
//            }
//        });
    }

    public void logout() {
        this.dispose();
        controller.newLoginWindow();
    }
}

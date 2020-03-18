package ru.geekbrains.java2.client.view;

import ru.geekbrains.java2.client.controller.ClientController;

import javax.swing.*;
import java.awt.event.*;
import java.io.IOException;

public class AuthDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField loginText;
    private JPasswordField passwordText;

    private ClientController controller;

    public AuthDialog(ClientController controller) {
        this.controller = controller;
        setContentPane(contentPane);
        //setModal(true);
        getRootPane().setDefaultButton(buttonOK);
        setSize(400, 150);
        setTitle("Авторизация");
        setLocationRelativeTo(null);

        buttonOK.addActionListener(e -> onOK());

        buttonCancel.addActionListener(e -> onCancel());

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(e -> onCancel(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
                JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void onOK() {
        // add your code here
        //dispose();
        String login = loginText.getText().trim();
        String password =  String.valueOf(passwordText.getPassword()).trim();

        try {
            controller.sendAuthMsg(login, password);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Ошибка аутентификации");
        }
    }

    private void onCancel() {
        // add your code here if necessary
        System.exit(0);
    }

//    public static void main(String[] args) {
//        AuthDialog dialog = new AuthDialog();
//        dialog.pack();
//        dialog.setVisible(true);
//
//    }
}

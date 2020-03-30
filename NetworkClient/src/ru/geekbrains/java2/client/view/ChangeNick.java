package ru.geekbrains.java2.client.view;

import ru.geekbrains.java2.client.controller.ClientController;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

public class ChangeNick extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField textField1;
    private JTextField textField2;

    private ClientController controller;
//

    public ChangeNick(ClientController controller) {
        this.controller = controller;
        textField1.setText(controller.getNickName());
        textField1.setEditable(false);
        setContentPane(contentPane);
        //setModal(true);
        getRootPane().setDefaultButton(buttonOK);
        setSize(400, 150);
        setTitle("Смена nickName");
        setLocationRelativeTo(null);

        buttonOK.addActionListener(e -> ChangeNick.this.onOK());
        buttonCancel.addActionListener(e -> ChangeNick.this.onCancel());

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
        // dispose();

        String oldNickName = textField1.getText().trim();
        String newNickName = textField2.getText().trim();

        if (newNickName.isEmpty()) {
            dispose();
        }
        try {
            controller.sendChangeNickMsg(newNickName, oldNickName);
            dispose();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Ошибка смены nickName");
        }
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

}


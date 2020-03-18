package ru.geekbrains.java2.client.view;

import ru.geekbrains.java2.client.controller.ClientController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class ClientChat extends JFrame {

    private JPanel mainPanel;
    JList<String> usersList;
    JTextField textField;
    JTextArea textAreaChat;
    JButton sendButton;
    JLabel label;

    private ClientController controller;

    public ClientChat(ClientController controller) {
        this.controller = controller;

        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        textAreaChat = new JTextArea(30, 10);
        textAreaChat.setLineWrap(true);
        textAreaChat.setEditable(false);
        JScrollPane scrollTextChat = new JScrollPane(textAreaChat);

        textField = new JTextField(25);
        label = new JLabel("My massage: ");
        sendButton = new JButton("Send");

        usersList = getUsersList();
        usersList.setVisibleRowCount(30);
        usersList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scrollListChat = new JScrollPane(usersList);

        JPanel panelFieldButton = new JPanel();

        panelFieldButton.add(label);
        panelFieldButton.add(textField);
        panelFieldButton.add(sendButton);

        mainPanel.add(BorderLayout.WEST, scrollListChat);
        mainPanel.add(BorderLayout.SOUTH, panelFieldButton);
        mainPanel.add(BorderLayout.CENTER, scrollTextChat);

        setContentPane(mainPanel);
        addListeners();
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                controller.shutdown();
            }
        });
    }

    private void addListeners() {

        sendButton.addActionListener(e -> ClientChat.this.sendMessage());
        textField.addActionListener(e -> ClientChat.this.sendMessage());
        usersList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                String selection = usersList.getSelectedValue();
                textField.setText("/to " + selection + " ");
            }
        });
    }

    private void sendMessage() {
        String msg = textField.getText().trim();
        System.out.println(msg);
        if (msg.isEmpty()) {
            return;
        }
        appendOwnMsg(msg);
        controller.sendMsg(msg);
        textField.setText("");
        textField.requestFocus();
    }

    private void appendOwnMsg(String msg) {
        appendMsg("Я: " + msg);
    }

    public void appendMsg(String msg) {
        SwingUtilities.invokeLater(() -> {
            textAreaChat.append(msg);
            textAreaChat.append(System.lineSeparator());
        });
    }

    public JList<String> getUsersList() {
        String[] usersList = {"nick1", "nick2", "nick3"};
        return new JList<>(usersList);
    }

    public void setUsersList(JList<String> chatersList) {
        this.usersList = chatersList;
    }

}



package ru.geekbrains.java2.client.view;

import ru.geekbrains.java2.client.controller.ClientController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

public class ClientChat extends JFrame {

    private JPanel mainPanel;
    JList<String> usersList;
    JTextField textField;
    JTextArea textAreaChat;
    JButton sendButton;
    JButton changeNickButton;
    JLabel label;

    private ClientController controller;

    public ClientChat(ClientController controller) {
        this.controller = controller;

        setSize(700, 400);
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
        changeNickButton = new JButton("Change nick");

        usersList = getUsersList();
        usersList.setVisibleRowCount(30);
        usersList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);


        JScrollPane scrollListChat = new JScrollPane(usersList);
        JPanel panelFieldButton = new JPanel();

        panelFieldButton.add(label);
        panelFieldButton.add(textField);
        panelFieldButton.add(sendButton);
        panelFieldButton.add(changeNickButton);

        mainPanel.add(BorderLayout.SOUTH, panelFieldButton);
        mainPanel.add(BorderLayout.CENTER, scrollTextChat);
        mainPanel.add(BorderLayout.WEST, scrollListChat);

        setContentPane(mainPanel);
        addListeners();

    }

    private void addListeners() {

        sendButton.addActionListener(e -> ClientChat.this.sendMessage());
        changeNickButton.addActionListener(e -> {
            ChangeNick changeNick = new ChangeNick(controller);
            changeNick.setVisible(true);
            //  setTitle(controller.getNickName() + " chat");


        });
        textField.addActionListener(e -> ClientChat.this.sendMessage());
        usersList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                String selection = usersList.getSelectedValue();
                textField.setText("to " + selection + " ");
            }
        });
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                controller.sendMsgToAll(controller.getNickName() + " вышел из чата");
                controller.shutdown();
            }
        });
    }

    private void sendMessage() {
        String msg = textField.getText().trim();
        if (msg.isEmpty()) {
            return;
        }
        appendOwnMsg(msg);

        if (usersList.getSelectedIndex() < 1) {
            controller.sendMsgToAll(msg);
        } else {
            String username = usersList.getSelectedValue();
            controller.sendPrivateMsg(username, msg);
        }
        usersList.clearSelection();
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

        return new JList<>();
    }

    public void setUsersList(JList<String> chatersList) {
        this.usersList = chatersList;
    }

    public void showError(String errorMsg) {
        JOptionPane.showMessageDialog(this, errorMsg);
    }

    public void showNewNickNameMsg(String msg) {
        JOptionPane.showMessageDialog(this, msg);
    }

    public void updateUsers(List<String> users) {
        if (users.isEmpty()) {
            setUsersList(new JList<>());
            return;
        }
        SwingUtilities.invokeLater(() -> {
            DefaultListModel<String> model = new DefaultListModel<>();
            users.forEach(model::addElement);
            usersList.setModel(model);
            setUsersList(usersList);
        });
    }
}



package lesson04;

import javax.swing.*;
import java.awt.*;

public class MyChatFrame extends JFrame {
    public MyChatFrame() {
        setTitle("My chat");
        setBounds(800, 400, 500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public static void main(String[] args) {
        JFrame jFrame = new MyChatFrame();

        JTextArea textAreaChat = new JTextArea(30, 10);
        JTextField textField = new JTextField(25);

        String[] usersList = {"Monica", "Joue", "Chandler", "Ross"};
        JList chatersList = new JList(usersList);
        JLabel label = new JLabel("My massage: ");

        String[] phrases = {
                ": Hello, Viktor! How are you?",
                ": Hello, friends! Whats up?",
                ": Hello, Viktor! Are you ready?",
                ": Hola, Viktor! Vamos a la playa?"};

        textAreaChat.setLineWrap(true);
        textAreaChat.setEditable(false);
        JScrollPane scrollTextChat = new JScrollPane(textAreaChat);

        chatersList.setVisibleRowCount(30);
        chatersList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        chatersList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                String selection = (String) chatersList.getSelectedValue();

                textAreaChat.append(selection + phrases[(int) (Math.random() * 4)] + "\n");
                textField.setText("to " + selection + ": ");
            }
        });
        JScrollPane scrollListChat = new JScrollPane(chatersList);

        JPanel panelFieldButton = new JPanel();
        textField.addActionListener(e -> listen(textAreaChat, textField));
        JButton button = new JButton("Send");
        button.addActionListener(e -> listen(textAreaChat, textField));

        panelFieldButton.add(label);
        panelFieldButton.add(textField);
        panelFieldButton.add(button);

        jFrame.add(BorderLayout.WEST, scrollListChat);
        jFrame.add(BorderLayout.SOUTH, panelFieldButton);
        jFrame.add(BorderLayout.CENTER, scrollTextChat);
        jFrame.setVisible(true);
    }

    private static void listen(JTextArea textArea, JTextField textField) {
        textArea.append("Viktor: ");
        textArea.append(textField.getText() + "\n");
        textField.setText("");
        textField.requestFocus();
    }
}



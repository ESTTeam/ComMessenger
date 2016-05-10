package user;

import sun.net.www.content.image.jpeg;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

/**
 * Created by Igor on 09.05.16.
 */
public class UserChatWindow extends JFrame{
    JTextArea chatWindow;

    JTextArea usersWindow;

    JTextField messageWindow;

    JButton disconnectButton;

    JLabel connectStatycLabel;

    JLabel spaceLabel;
    Border border = BorderFactory.createLineBorder(Color.BLACK);
    public UserChatWindow() {
        super("Чат");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        chatWindow = new JTextArea(25, 60);
        usersWindow = new JTextArea(25, 10);
        messageWindow = new JTextField();
        disconnectButton = new JButton("Раздъединить");
        connectStatycLabel = new JLabel("Подключено");
        JButton historyButton = new JButton("История");
        JButton sendButton = new JButton("Послать");
        JButton exitButton = new JButton("Выход");



        messageWindow.setPreferredSize(new Dimension(777, 25));
        JPanel jPanel = new JPanel();
        jPanel.add(chatWindow);
        jPanel.add(usersWindow);
        jPanel.add(messageWindow);
        jPanel.add(disconnectButton);
        jPanel.add(connectStatycLabel);
        jPanel.add(new JLabel("                       " +
                "                             " +
                "              " +
                "                                       "));

        jPanel.add(historyButton);
        jPanel.add(sendButton);
        jPanel.add(exitButton);
        jPanel.setPreferredSize(new Dimension(800, 500));
        jPanel.setBackground(Color.GRAY);
        add(jPanel);
        setResizable(false);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }
}

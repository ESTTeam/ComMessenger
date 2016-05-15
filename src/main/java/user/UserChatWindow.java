package user;

import link.DataLinkLayer;
import link.NoSuchUserException;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Objects;

import static java.lang.Thread.sleep;
import static javax.swing.JOptionPane.showMessageDialog;

/**
 * Created by Igor on 09.05.16.
 */
public class UserChatWindow extends JFrame{
    JTextArea chatWindow;

    JList usersWindow;

    JTextField messageWindow;

    JButton disconnectButton;

    JLabel connectionStatusLabel;

    JPanel jPanel;

    final DefaultListModel model;

    private DataLinkLayer dataLinkLayer;

    String username;

    public UserChatWindow(String UserName, String ComPortSender, String ComPortReceiver) {
        super("Чат - " + UserName );
        DataLinkLayer dataLinkLayer = new DataLinkLayer(new MessageToStation(this),
                UserName, ComPortSender, ComPortReceiver);

        this.dataLinkLayer = dataLinkLayer;
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        chatWindow = new JTextArea(25, 30);
        chatWindow.setColumns(60);
        chatWindow.setLineWrap(true);
        chatWindow.setWrapStyleWord(true);
        chatWindow.setAutoscrolls(true);
        JScrollPane chatWindowPane = new JScrollPane(chatWindow);
        model = new DefaultListModel();
        for(String s:dataLinkLayer.getUsers()){
             if (!Objects.equals(s, UserName)) {
                model.addElement(s);
             }
        }
        username = UserName;
        usersWindow = new JList(model);
        usersWindow.setBackground(Color.WHITE);
        usersWindow.setMinimumSize(new Dimension(500, 5000));
        usersWindow.setVisibleRowCount(25);
        usersWindow.setFixedCellWidth(108);
        JScrollPane scrollPane = new JScrollPane(usersWindow);
        scrollPane.setMinimumSize(new Dimension(50, 500));
        messageWindow = new JTextField();
        disconnectButton = new JButton("Раздъединить");
        ActionListener disconnectActionListener = new DisconnectActionListener();
        disconnectButton.addActionListener(disconnectActionListener);
        connectionStatusLabel = new JLabel("Подключено");
        JButton settingsButoon = new JButton("Настройка");
        if (!Objects.equals(ComPortSender, "COM11")) {
            settingsButoon.setEnabled(false);
        }
        ActionListener settingsActionListener = new SettingsActionListener();
        settingsButoon.addActionListener(settingsActionListener);
        JButton historyButton = new JButton("История");
        JButton sendButton = new JButton("Отправить");
        getRootPane().setDefaultButton(sendButton);
        ActionListener sendMessageActionListener = new SendMessageActionListener();
        sendButton.addActionListener(sendMessageActionListener);
        JButton exitButton = new JButton("Выход");
        ActionListener exitActionListener = new ExitActionListener();
        exitButton.addActionListener(exitActionListener);
        usersWindow.setLayoutOrientation(JList.VERTICAL);
        messageWindow.setPreferredSize(new Dimension(777, 25));
        jPanel = new JPanel();
        jPanel.add(chatWindowPane);
        jPanel.add(scrollPane);
        jPanel.add(messageWindow);
        jPanel.add(disconnectButton);
        jPanel.add(connectionStatusLabel);
        jPanel.add(new JLabel("                       " +
                "                             " +
                "              " +
                "  "));
        jPanel.add(settingsButoon);
        jPanel.add(historyButton);
        jPanel.add(sendButton);
        jPanel.add(exitButton);
        jPanel.setPreferredSize(new Dimension(800, 490));
        jPanel.setBackground(Color.GRAY);
        add(jPanel);
        setResizable(false);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }
    public class DisconnectActionListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            dataLinkLayer.disconnect();
            try {
                sleep(200);
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
            UserFormSettings userFormSettings = new UserFormSettings();
        }
    }
    public class SettingsActionListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            UserPortSettings userPortSettings = new UserPortSettings(dataLinkLayer);
        }
    }
    public class SendMessageActionListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            try {
                if (usersWindow.isSelectionEmpty()) {
                    showMessageDialog(null, "Необходимо выбрать адресата");
                }
                else if (messageWindow.getText().isEmpty()) {
                    showMessageDialog(null, "Введите сообщение");
                }
                else {
                    chatWindow.append("Я" + " > " + messageWindow.getText() + "\n");
                    dataLinkLayer.setDataToSend(usersWindow.getSelectedValue().toString(), username + " > "
                            + messageWindow.getText());
                    messageWindow.setText(null);
                }

            } catch (NoSuchUserException e1) {
                e1.printStackTrace();
            }
        }
    }
    public class ExitActionListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            dataLinkLayer.disconnect();
            try {
                sleep(600);
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
            System.exit(0);
        }
    }

}

package user;

import link.DataLinkLayer;
import link.NoSuchUserException;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

import static java.lang.Thread.sleep;
import static javax.swing.JOptionPane.showMessageDialog;

/**
 * Created by Igor on 09.05.16.
 */
public class UserChatWindow extends JFrame{
    JTextArea chatWindow;

    JTextArea chatWindow2;

    JTextArea chatWindow3;

    JTextArea chatWindow4;

    JTextArea chatWindow5;

    JTextArea chatWindowEmpty;

    JList usersWindow;

    JTextField messageWindow;

    JButton disconnectButton;

    JLabel connectionStatusLabel;

    JPanel jPanel;

    Map<String, JTextArea> userTextAreaMap;

    final DefaultListModel model;

    private DataLinkLayer dataLinkLayer;

    String username;

    int uniqueFlag = 0;

    JScrollPane chatWindowPane;

    Stack stackTextArea;

    public UserChatWindow(String UserName, String ComPortSender, String ComPortReceiver) {
        super("Чат - " + UserName );
        DataLinkLayer dataLinkLayer = new DataLinkLayer(new MessageToStation(this),
                UserName, ComPortSender, ComPortReceiver);
        this.dataLinkLayer = dataLinkLayer;
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        chatWindow = new JTextArea(25, 30);
        chatWindow2 = new JTextArea(25, 30);
        chatWindow3 = new JTextArea(25, 30);
        chatWindow4 = new JTextArea(25, 30);
        chatWindow5 = new JTextArea(25, 30);
        chatWindowEmpty = new JTextArea(25, 30);
        userTextAreaMap = new HashMap<String, JTextArea>();
        stackTextArea = new Stack();
        stackTextArea.add(chatWindow5);
        stackTextArea.add(chatWindow4);
        stackTextArea.add(chatWindow3);
        stackTextArea.add(chatWindow2);
        stackTextArea.add(chatWindow);

        chatWindow.setColumns(60);
        chatWindow.setLineWrap(true);
        chatWindow.setWrapStyleWord(true);
        chatWindow.setAutoscrolls(true);
        chatWindow2.setColumns(60);
        chatWindow2.setLineWrap(true);
        chatWindow2.setWrapStyleWord(true);
        chatWindow2.setAutoscrolls(true);
        chatWindow3.setColumns(60);
        chatWindow3.setLineWrap(true);
        chatWindow3.setWrapStyleWord(true);
        chatWindow3.setAutoscrolls(true);
        chatWindow4.setColumns(60);
        chatWindow4.setLineWrap(true);
        chatWindow4.setWrapStyleWord(true);
        chatWindow4.setAutoscrolls(true);
        chatWindow5.setColumns(60);
        chatWindow5.setLineWrap(true);
        chatWindow5.setWrapStyleWord(true);
        chatWindow5.setAutoscrolls(true);
        chatWindowEmpty.setColumns(60);
        chatWindowEmpty.setLineWrap(true);
        chatWindowEmpty.setWrapStyleWord(true);
        chatWindowEmpty.setAutoscrolls(true);
        chatWindowPane  = new JScrollPane(chatWindowEmpty);
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
        usersWindow.addListSelectionListener(new ListSelectionListener()
        {
            public void valueChanged(ListSelectionEvent ev)
            {
                if (!usersWindow.isSelectionEmpty()) {
                    JTextArea currArea = userTextAreaMap.get(usersWindow.getSelectedValue().toString());
                    chatWindowPane.setViewportView(currArea);
                }

            }
        });
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
                    JTextArea areToSend = userTextAreaMap.get(usersWindow.getSelectedValue().toString());
                    areToSend.append("Я" + " > " + messageWindow.getText() + "\n");
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

    public class ChangeReceiverActionListener implements ActionListener {
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

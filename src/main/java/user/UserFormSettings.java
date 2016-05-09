package user;
import link.DataLinkLayer;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.*;

public class UserFormSettings extends JFrame {

    JLabel comSelectedPortIn;

    JComboBox comPortBox;

    JLabel comSelectedPortOut;

    JTextField userName;

    public UserFormSettings() {

        super("Настройка подключения");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Font font = new Font("Verdana", Font.PLAIN, 14);
        Font fontLabel = new Font("Verdana", Font.PLAIN, 16);
        final JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(font);
        getContentPane().add(tabbedPane);
        JPanel setNickName = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel setComPort1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel setComPort2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel labelNickName = new JLabel("Ник");
        labelNickName.setFont(fontLabel);
        userName = new JTextField(40);
        JButton button = new JButton("Register");
        ActionListener actionListener = new TestActionListener();
        button.addActionListener(actionListener);
        setNickName.add(labelNickName);
        setNickName.add(userName);
        setNickName.setLayout(new GridLayout(3, 2, 1, 1));
        tabbedPane.addTab("Регистрация ", setNickName);
        tabbedPane.addTab("COM In ", setComPort1);
        tabbedPane.addTab("COM Out ", setComPort2);
        JLabel comSelectedPortTitleIn = new JLabel("Выбранная пара портов");
        JLabel comSelectedPortTitleOut = new JLabel("Выбранная пара портов");
        JLabel comPort = new JLabel("COM PORT");
        JLabel comSpeed = new JLabel("Скорость");
        JLabel comBits = new JLabel("Биты данных");
        JLabel comStopBits = new JLabel("Стопы биты");
        JLabel comParityBit = new JLabel("Четность");
        String[] comPortList = {
                "COM11 - COM12", "COM21 - COM22", "COM31 - COM32", "COM41 - COM42", "COM51 - COM52"
        };
        comPortBox = new JComboBox(comPortList);
        comPortBox.setPrototypeDisplayValue("XXXXXXXXXXXXXXX");

        String[] comSpeedList = {
                "300", "600", "1200", "2400", "4800", "9600", "14400", "28800", "36000", "57600", "115000"
        };
        JComboBox comSpeedBox = new JComboBox(comSpeedList);
        comSpeedBox.setMaximumSize(new Dimension(10, 5));
        comSpeedBox.setMaximumRowCount(11);
        comSpeedBox.setPrototypeDisplayValue("XXXXXXXXXXXXXX");

        String[] comBitsList = {
                "5", "6", "7", "8"
        };
        JComboBox comBitsBox = new JComboBox(comBitsList);
        comBitsBox.setPrototypeDisplayValue("XXXXXXXXXXXXXXXX");


        String[] comStopBitsList = {
                "1", "2", "1.5"
        };
        JComboBox comStopBitsBox = new JComboBox(comStopBitsList);
        comStopBitsBox.setPrototypeDisplayValue("XXXXXXXXXXXXXXXXXXXX");

        String[] comParityBitsList = {
                "none", "odd", "even", "mark", "space"
        };
        JComboBox comParityBitsBox = new JComboBox(comParityBitsList);
        comParityBitsBox.setPrototypeDisplayValue("XXXXXXXXXXXXXXXXXX");

        JLabel comSpeed2 = new JLabel("Скорость");
        JLabel comBits2 = new JLabel("Биты данных");
        JLabel comStopBits2 = new JLabel("Стопы биты");
        JLabel comParityBit2 = new JLabel("Четность");
        comSelectedPortIn = new JLabel();
        comSelectedPortOut = new JLabel();

        String[] comSpeedList2 = {
                "300", "600", "1200", "2400", "4800", "9600", "14400", "28800", "36000", "57600", "115000"
        };
        JComboBox comSpeedBox2 = new JComboBox(comSpeedList);
        comSpeedBox2.setMaximumSize(new Dimension(10, 5));
        comSpeedBox2.setMaximumRowCount(11);
        comSpeedBox2.setPrototypeDisplayValue("XXXXXXXXXXXXXX");

        String[] comBitsList2 = {
                "5", "6", "7", "8"
        };
        JComboBox comBitsBox2 = new JComboBox(comBitsList2);
        comBitsBox2.setPrototypeDisplayValue("XXXXXXXXXXXXXXXX");


        String[] comStopBitsList2 = {
                "1", "2", "1.5"
        };
        JComboBox comStopBitsBox2 = new JComboBox(comStopBitsList2);
        comStopBitsBox2.setPrototypeDisplayValue("XXXXXXXXXXXXXXXXXXXX");

        String[] comParityBitsList2 = {
                "none", "odd", "even", "mark", "space"
        };
        JComboBox comParityBitsBox2 = new JComboBox(comParityBitsList2);
        comParityBitsBox2.setPrototypeDisplayValue("XXXXXXXXXXXXXXXXXX");
        tabbedPane.setMaximumSize(new Dimension(400, 400));
        setComPort1.setLayout(new GridLayout(5, 2, 1, 1));
        setNickName.add(comPort);
        setNickName.add(comPortBox);
        setNickName.add(button);
        setComPort1.add(comSelectedPortTitleIn);
        setComPort1.add(comSelectedPortIn);
        setComPort1.add(comSpeed);
        setComPort1.add(comSpeedBox);
        setComPort1.add(comBits);
        setComPort1.add(comBitsBox);
        setComPort1.add(comStopBits);
        setComPort1.add(comStopBitsBox);
        setComPort1.add(comParityBit);
        setComPort1.add(comParityBitsBox);


        setComPort2.setLayout(new GridLayout(5, 2, 1, 1));
        setComPort2.add(comSelectedPortTitleOut);
        setComPort2.add(comSelectedPortOut);
        setComPort2.add(comSpeed2);
        setComPort2.add(comSpeedBox2);
        setComPort2.add(comBits2);
        setComPort2.add(comBitsBox2);
        setComPort2.add(comStopBits2);
        setComPort2.add(comStopBitsBox2);
        setComPort2.add(comParityBit2);
        setComPort2.add(comParityBitsBox2);
        setPreferredSize(new Dimension(520, 440));
        getSelectedComPortValue();
        comPortBox.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent arg0) {
                getSelectedComPortValue();
            }
        });
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }
    public void getSelectedComPortValue() {
        String selectedComPortValue = comPortBox.getSelectedItem().toString();
        comSelectedPortIn.setText(selectedComPortValue);
        comSelectedPortOut.setText(selectedComPortValue);
    }
    public String getUserNameFromRegister() {
        return userName.getText();
    }
    public String getComPortSender() {
        String comPortSender = comPortBox.getSelectedItem().toString().substring(0, 5);
        return comPortSender;
    }
    public String getComPortReceiver() {
        String comPortReceiver = comPortBox.getSelectedItem().toString().substring(8, 13);
        return comPortReceiver;
    }
    public class TestActionListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            DataLinkLayer dataLinkLayer = new DataLinkLayer(new UserLayer() , getUserNameFromRegister(),
            getComPortSender(), getComPortReceiver());
            UserChatWindow userChatWindow = new UserChatWindow();
            dispose();
            userChatWindow.setVisible(true);
        }
    }

}

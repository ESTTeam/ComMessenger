package user;
import link.DataLinkLayer;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.comm.SerialPort;
import javax.swing.*;

import static com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type.Int;

public class UserFormSettings extends JFrame {

    JLabel comSelectedPortIn;

    JComboBox comPortBox;

    JLabel comSelectedPortOut;

    JTextField userName;

    JComboBox comSpeedBox;

    JComboBox comBitsBox;

    JComboBox comStopBitsBox;

    JComboBox comParityBitsBox;

    JComboBox comSpeedBox2;

    JComboBox comBitsBox2;

    JComboBox comStopBitsBox2;

    JComboBox comParityBitsBox2;
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
        JPanel jPanelComPortBox = new JPanel();
        jPanelComPortBox.add(comPortBox);
        jPanelComPortBox.setPreferredSize(new Dimension(220, 20));
        Integer[] comSpeedList = {
                300, 600, 1200, 2400, 4800, 9600, 14400, 28800, 36000, 57600, 115000
        };
        comSpeedBox = new JComboBox(comSpeedList);
        comSpeedBox.setPreferredSize(new Dimension(230, 25));
        JPanel jPanelComSpeedBox = new JPanel();
        jPanelComSpeedBox.add(comSpeedBox);


        comSpeedBox.setPrototypeDisplayValue("XXXXXXXXXXXXXX");


        Integer[] comBitsList = {
                SerialPort.DATABITS_5, SerialPort.DATABITS_6, SerialPort.DATABITS_7, SerialPort.DATABITS_8
        };
        comBitsBox = new JComboBox(comBitsList);
        comBitsBox.setPrototypeDisplayValue("XXXXXXXXXXXXXXXX");
        comBitsBox.setPreferredSize(new Dimension(230, 25));
        JPanel jPanelComBitsBox = new JPanel();
        jPanelComBitsBox.add(comBitsBox);

        String[] comStopBitsList = {
                "1", "2", "1_5"
        };
        comStopBitsBox = new JComboBox(comStopBitsList);
        comStopBitsBox.setPrototypeDisplayValue("XXXXXXXXXXXXXXXXXXXX");
        comStopBitsBox.setPreferredSize(new Dimension(230, 25));
        JPanel jPanelComStopBitsBox = new JPanel();
        jPanelComStopBitsBox.add(comStopBitsBox);
        String[] comParityBitsList = {
                "None", "Odd", "Even", "Mark"
        };
        comParityBitsBox = new JComboBox(comParityBitsList);
        comParityBitsBox.setPrototypeDisplayValue("XXXXXXXXXXXXXXXXXX");
        comParityBitsBox.setPreferredSize(new Dimension(230, 25));
        JPanel jPanelComParityBitsBox = new JPanel();
        jPanelComParityBitsBox.add(comParityBitsBox);
        JLabel comSpeed2 = new JLabel("Скорость");
        JLabel comBits2 = new JLabel("Биты данных");
        JLabel comStopBits2 = new JLabel("Стопы биты");
        JLabel comParityBit2 = new JLabel("Четность");
        comSelectedPortIn = new JLabel();
        comSelectedPortOut = new JLabel();

        String[] comSpeedList2 = {
                "300", "600", "1200", "2400", "4800", "9600", "14400", "28800", "36000", "57600", "115000"
        };
        comSpeedBox2 = new JComboBox(comSpeedList);
        comSpeedBox2.setMaximumSize(new Dimension(10, 5));
        comSpeedBox2.setMaximumRowCount(11);
        comSpeedBox2.setPrototypeDisplayValue("XXXXXXXXXXXXXX");

        Integer[] comBitsList2 = {
                SerialPort.DATABITS_5, SerialPort.DATABITS_6, SerialPort.DATABITS_7, SerialPort.DATABITS_8
        };
        comBitsBox2 = new JComboBox(comBitsList2);
        comBitsBox2.setPrototypeDisplayValue("XXXXXXXXXXXXXXXX");

        String[] comStopBitsList2 = {
                "1", "2", "1_5"
        };

        comStopBitsBox2 = new JComboBox(comStopBitsList2);
        comStopBitsBox2.setPrototypeDisplayValue("XXXXXXXXXXXXXXXXXXXX");

        String[] comParityBitsList2 = {
                "None", "Odd", "Even", "Mark"
        };

        comParityBitsBox2 = new JComboBox(comParityBitsList2);
        comParityBitsBox2.setPrototypeDisplayValue("XXXXXXXXXXXXXXXXXX");
        tabbedPane.setMaximumSize(new Dimension(400, 400));
        JPanel jPanelButtonRegister= new JPanel();
        jPanelButtonRegister.add(button);
        jPanelButtonRegister.setMaximumSize(new Dimension(getWidth(), 15));
        setComPort1.setLayout(new GridLayout(5, 2, 1, 1));
        setNickName.add(comPort);
        setNickName.add(jPanelComPortBox);
        setNickName.add(jPanelButtonRegister);
        setComPort1.add(comSelectedPortTitleIn);
        setComPort1.add(comSelectedPortIn);
        setComPort1.add(comSpeed);
        setComPort1.add(jPanelComSpeedBox);
        setComPort1.add(comBits);
        setComPort1.add(jPanelComBitsBox);
        setComPort1.add(comStopBits);
        setComPort1.add(jPanelComStopBitsBox);
        setComPort1.add(comParityBit);
        setComPort1.add(jPanelComParityBitsBox);

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
            ConnectionParams sendPortParams = new ConnectionParams();
            ConnectionParams receivePortParams = new ConnectionParams();
            System.out.println(SerialPort.PARITY_NONE);
            System.out.println(SerialPort.PARITY_ODD);
            System.out.println(SerialPort.PARITY_MARK);
            System.out.println(SerialPort.PARITY_EVEN);
            receivePortParams.baudRate = (int) comSpeedBox.getSelectedItem();
            receivePortParams.dataBits = (int) comBitsBox.getSelectedItem();
            receivePortParams.stopBits = comStopBitsBox.getSelectedIndex() + 1;
            receivePortParams.parity = comParityBitsBox.getSelectedIndex();
            sendPortParams.baudRate = (int) comSpeedBox2.getSelectedItem();
            sendPortParams.dataBits = (int) comBitsBox2.getSelectedItem();
            sendPortParams.stopBits = comStopBitsBox2.getSelectedIndex() + 1;
            sendPortParams.parity = comParityBitsBox2.getSelectedIndex();
            UserChatWindow userChatWindow = new UserChatWindow(getUserNameFromRegister(),
                    getComPortSender(), getComPortReceiver(), receivePortParams, sendPortParams);
            dispose();
        }
    }

}

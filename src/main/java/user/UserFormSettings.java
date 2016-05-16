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
import static javax.swing.JOptionPane.showMessageDialog;

public class UserFormSettings extends JFrame {

    JComboBox comPortBox;

    JTextField userName;

    public UserFormSettings() {

        super("Настройка подключения");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        GridBagConstraints gbc = new GridBagConstraints();
        Font font = new Font("Verdana", Font.PLAIN, 14);
        Font fontName = new Font("Verdana", Font.PLAIN, 18);
        Font fontLabel = new Font("Verdana", Font.PLAIN, 16);
        final JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(font);
        getContentPane().add(tabbedPane);
        JPanel setNickName = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel labelNickName = new JLabel("Ник");
        labelNickName.setFont(fontLabel);
        userName = new JTextField(10);
        userName.setPreferredSize(new Dimension(10, 25));
        userName.setFont(fontName);
        JPanel jPanelUserName = new JPanel(new GridBagLayout());
        jPanelUserName.add(userName);
        JButton button = new JButton("Зарегестрироваться");
        JButton exitButton = new JButton("Выход");
        ActionListener actionListener = new TestActionListener();
        button.addActionListener(actionListener);
        ActionListener exitActionListener = new ExitActionListener();
        exitButton.addActionListener(exitActionListener);
        setNickName.add(labelNickName);
        setNickName.add(jPanelUserName);
        setNickName.setLayout(new GridLayout(3, 2, 1, 1));
        tabbedPane.addTab("Регистрация ", setNickName);
        String[] comPortList = {
                "COM11 - COM12", "COM21 - COM22", "COM31 - COM32", "COM41 - COM42", "COM51 - COM52"
        };
        JLabel comPort = new JLabel("COM PORT");
        comPortBox = new JComboBox(comPortList);
        comPortBox.setPrototypeDisplayValue("XXXXXXXXXXXXXXX");
        comPortBox.setPreferredSize(new Dimension(180, 25));
        JPanel jPanelComPortBox = new JPanel(new GridBagLayout());
        jPanelComPortBox.add(comPortBox);
        jPanelComPortBox.setPreferredSize(new Dimension(220, 20));
        tabbedPane.setMaximumSize(new Dimension(400, 400));
        JPanel jPanelButtonRegister= new JPanel();
        jPanelButtonRegister.add(button);
        JPanel jPanelExit = new JPanel();
        jPanelExit.add(exitButton);
        getRootPane().setDefaultButton(button);
        jPanelButtonRegister.setMaximumSize(new Dimension(getWidth(), 15));

        setNickName.add(comPort);
        setNickName.add(jPanelComPortBox);
        setNickName.add(jPanelButtonRegister);
        setNickName.add(jPanelExit);

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
            if (getUserNameFromRegister().isEmpty()) {
                showMessageDialog(null, "Поле Ник не может быть пустым!");
            }
            else {
                UserChatWindow userChatWindow = new UserChatWindow(getUserNameFromRegister(),
                        getComPortSender(), getComPortReceiver());
                dispose();
            }
        }
    }

    public class ExitActionListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            System.exit(0);
        }
    }
}

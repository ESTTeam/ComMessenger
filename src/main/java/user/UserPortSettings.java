package user;

import link.DataLinkLayer;
import link.NoSuchUserException;

import javax.comm.SerialPort;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

/**
 * Created by Igor on 14.05.2016.
 */
public class UserPortSettings extends JFrame {

    private DataLinkLayer dataLinkLayer;

    private JComboBox comSpeedBox;

    private JComboBox comBitsBox;

    private JComboBox comStopBitsBox;

    private JComboBox comParityBitsBox;

    private JComboBox comSpeedBox2;

    private JComboBox comBitsBox2;

    private JComboBox comStopBitsBox2;

    private JComboBox comParityBitsBox2;

    private JTabbedPane tabbedPane;

    public UserPortSettings(DataLinkLayer dataLinkLayer) {
        super("Настройка подключения");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.dataLinkLayer = dataLinkLayer;
        tabbedPane = new JTabbedPane();
        getContentPane().add(tabbedPane);
        JPanel setComPort1 = new JPanel(new FlowLayout(FlowLayout.LEFT));

        tabbedPane.addTab("Настройка COM портов ", setComPort1);
        JLabel comSpeed = new JLabel("Скорость");
        JLabel comBits = new JLabel("Биты данных");
        JLabel comStopBits = new JLabel("Стопы биты");
        JLabel comParityBit = new JLabel("Четность");


        Integer[] comSpeedList = {
                300, 600, 1200, 2400, 4800, 9600, 57600
        };
        comSpeedBox = new JComboBox(comSpeedList);
        comSpeedBox.setPreferredSize(new Dimension(230, 25));
        comSpeedBox.setSelectedIndex(5);
        JPanel jPanelComSpeedBox = new JPanel(new GridBagLayout());
        jPanelComSpeedBox.add(comSpeedBox);


        comSpeedBox.setPrototypeDisplayValue("XXXXXXXXXXXXXX");


        Integer[] comBitsList = {
                SerialPort.DATABITS_5, SerialPort.DATABITS_6, SerialPort.DATABITS_7, SerialPort.DATABITS_8
        };
        comBitsBox = new JComboBox(comBitsList);
        comBitsBox.setPrototypeDisplayValue("XXXXXXXXXXXXXXXX");
        comBitsBox.setPreferredSize(new Dimension(230, 25));
        comBitsBox.setSelectedIndex(3);
        JPanel jPanelComBitsBox = new JPanel(new GridBagLayout());
        jPanelComBitsBox.add(comBitsBox);

        String[] comStopBitsList = {
                "1", "2"
        };
        String[] comStopBitsList2 = {
                "1"
        };
        comStopBitsBox = new JComboBox(comStopBitsList);
        comStopBitsBox.setPrototypeDisplayValue("XXXXXXXXXXXXXXXXXXXX");
        comStopBitsBox.setPreferredSize(new Dimension(230, 25));
        JPanel jPanelComStopBitsBox = new JPanel(new GridBagLayout());
        jPanelComStopBitsBox.add(comStopBitsBox);
        String[] comParityBitsList = {
                "None", "Odd", "Even",
        };
        comParityBitsBox = new JComboBox(comParityBitsList);
        comParityBitsBox.setPrototypeDisplayValue("XXXXXXXXXXXXXXXXXX");
        comParityBitsBox.setPreferredSize(new Dimension(230, 25));
        comBitsBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (comBitsBox.getSelectedIndex() != 0) {
                    comStopBitsBox.setModel(new DefaultComboBoxModel<>(comStopBitsList));
                }
                else {
                    comStopBitsBox.setModel(new DefaultComboBoxModel<>(comStopBitsList2));
                }
            }
        });

        JPanel jPanelComParityBitsBox = new JPanel(new GridBagLayout());
        jPanelComParityBitsBox.add(comParityBitsBox);
        tabbedPane.setMaximumSize(new Dimension(400, 400));
        JButton OKbutton = new JButton("Окей");
        JPanel jPanelOKbutton = new JPanel(new GridBagLayout());
        jPanelOKbutton.add(OKbutton);
        ActionListener OkayActionListener = new onAcceptClickListener();
        OKbutton.addActionListener(OkayActionListener);
        JButton CancellButton = new JButton("Отмена");
        JPanel jPanelCancell1 = new JPanel(new GridBagLayout());
        jPanelCancell1.add(CancellButton);
        ActionListener CancellActionListener = new onCancelClickListener();
        CancellButton.addActionListener(CancellActionListener);
        setComPort1.setLayout(new GridLayout(5, 2, 1, 1));
        setComPort1.add(comSpeed);
        setComPort1.add(jPanelComSpeedBox);
        setComPort1.add(comBits);
        setComPort1.add(jPanelComBitsBox);
        setComPort1.add(comStopBits);
        setComPort1.add(jPanelComStopBitsBox);
        setComPort1.add(comParityBit);
        setComPort1.add(jPanelComParityBitsBox);
        setComPort1.add(jPanelOKbutton);
        setComPort1.add(jPanelCancell1);

        setPreferredSize(new Dimension(520, 440));
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public class onAcceptClickListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            ConnectionParams receivePortParams = new ConnectionParams();
            receivePortParams.baudRate = (int) comSpeedBox.getSelectedItem();
            receivePortParams.dataBits = (int) comBitsBox.getSelectedItem();
            receivePortParams.stopBits = comStopBitsBox.getSelectedIndex() + 1;
            receivePortParams.parity = comParityBitsBox.getSelectedIndex();
            dataLinkLayer.setPortParameters(receivePortParams.baudRate, receivePortParams.dataBits, receivePortParams.stopBits,
                    receivePortParams.parity);
            dispose();
        }
    }

    public class onCancelClickListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            dispose();
        }
    }

}

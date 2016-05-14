package user;

import link.DataLinkLayer;

import javax.comm.SerialPort;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
        JPanel setComPort2 = new JPanel(new FlowLayout(FlowLayout.LEFT));

        tabbedPane.addTab("COM In ", setComPort1);
        tabbedPane.addTab("COM Out ", setComPort2);
        JLabel comSpeed = new JLabel("Скорость");
        JLabel comBits = new JLabel("Биты данных");
        JLabel comStopBits = new JLabel("Стопы биты");
        JLabel comParityBit = new JLabel("Четность");


        Integer[] comSpeedList = {
                300, 600, 1200, 2400, 4800, 9600, 14400, 28800, 36000, 57600, 115000
        };
        comSpeedBox = new JComboBox(comSpeedList);
        comSpeedBox.setPreferredSize(new Dimension(230, 25));
        JPanel jPanelComSpeedBox = new JPanel(new GridBagLayout());
        jPanelComSpeedBox.add(comSpeedBox);


        comSpeedBox.setPrototypeDisplayValue("XXXXXXXXXXXXXX");


        Integer[] comBitsList = {
                SerialPort.DATABITS_5, SerialPort.DATABITS_6, SerialPort.DATABITS_7, SerialPort.DATABITS_8
        };
        comBitsBox = new JComboBox(comBitsList);
        comBitsBox.setPrototypeDisplayValue("XXXXXXXXXXXXXXXX");
        comBitsBox.setPreferredSize(new Dimension(230, 25));
        JPanel jPanelComBitsBox = new JPanel(new GridBagLayout());
        jPanelComBitsBox.add(comBitsBox);

        String[] comStopBitsList = {
                "1", "2", "1_5"
        };
        comStopBitsBox = new JComboBox(comStopBitsList);
        comStopBitsBox.setPrototypeDisplayValue("XXXXXXXXXXXXXXXXXXXX");
        comStopBitsBox.setPreferredSize(new Dimension(230, 25));
        JPanel jPanelComStopBitsBox = new JPanel(new GridBagLayout());
        jPanelComStopBitsBox.add(comStopBitsBox);
        String[] comParityBitsList = {
                "None", "Odd", "Even", "Mark"
        };
        comParityBitsBox = new JComboBox(comParityBitsList);
        comParityBitsBox.setPrototypeDisplayValue("XXXXXXXXXXXXXXXXXX");
        comParityBitsBox.setPreferredSize(new Dimension(230, 25));
        JPanel jPanelComParityBitsBox = new JPanel(new GridBagLayout());
        jPanelComParityBitsBox.add(comParityBitsBox);
        JLabel comSpeed2 = new JLabel("Скорость");
        JLabel comBits2 = new JLabel("Биты данных");
        JLabel comStopBits2 = new JLabel("Стопы биты");
        JLabel comParityBit2 = new JLabel("Четность");

        String[] comSpeedList2 = {
                "300", "600", "1200", "2400", "4800", "9600", "14400", "28800", "36000", "57600", "115000"
        };
        comSpeedBox2 = new JComboBox(comSpeedList);
        comSpeedBox2.setMaximumSize(new Dimension(10, 5));
        comSpeedBox2.setMaximumRowCount(11);
        comSpeedBox2.setPrototypeDisplayValue("XXXXXXXXXXXXXX");
        comSpeedBox2.setPreferredSize(new Dimension(230, 25));

        JPanel jPanelComSpeedBox2 = new JPanel(new GridBagLayout());
        jPanelComSpeedBox2.add(comSpeedBox2);

        Integer[] comBitsList2 = {
                SerialPort.DATABITS_5, SerialPort.DATABITS_6, SerialPort.DATABITS_7, SerialPort.DATABITS_8
        };
        comBitsBox2 = new JComboBox(comBitsList2);
        comBitsBox2.setPrototypeDisplayValue("XXXXXXXXXXXXXXXX");
        comBitsBox2.setPreferredSize(new Dimension(230, 25));

        JPanel jPanelComBitsBox2 = new JPanel(new GridBagLayout());
        jPanelComBitsBox2.add(comBitsBox2);
        String[] comStopBitsList2 = {
                "1", "2", "1_5"
        };

        comStopBitsBox2 = new JComboBox(comStopBitsList2);
        comStopBitsBox2.setPrototypeDisplayValue("XXXXXXXXXXXXXXXXXXXX");
        comStopBitsBox2.setPreferredSize(new Dimension(230, 25));

        JPanel jPanelComStopBitsBox2 = new JPanel(new GridBagLayout());
        jPanelComStopBitsBox2.add(comStopBitsBox2);
        String[] comParityBitsList2 = {
                "None", "Odd", "Even", "Mark"
        };

        comParityBitsBox2 = new JComboBox(comParityBitsList2);
        comParityBitsBox2.setPrototypeDisplayValue("XXXXXXXXXXXXXXXXXX");
        comParityBitsBox2.setPreferredSize(new Dimension(230, 25));

        JPanel jPanelComParityBitsBox2 = new JPanel(new GridBagLayout());
        jPanelComParityBitsBox2.add(comParityBitsBox2);
        tabbedPane.setMaximumSize(new Dimension(400, 400));
        JButton button = new JButton("Далее");
        JPanel jPanelButton = new JPanel(new GridBagLayout());
        jPanelButton.add(button);
        ActionListener actionListener = new onNextClickListner();
        button.addActionListener(actionListener);

        JButton OKbutton = new JButton("Окей");
        JPanel jPanelOKbutton = new JPanel(new GridBagLayout());
        jPanelOKbutton.add(OKbutton);
        ActionListener OkayActionListener = new onAcceptClickListner();
        OKbutton.addActionListener(OkayActionListener);
        JButton CancellButton = new JButton("Отмена");
        JButton CancellButton2 = new JButton("Отмена");
        JPanel jPanelCancell1 = new JPanel(new GridBagLayout());
        jPanelCancell1.add(CancellButton);
        JPanel jPanelCancell2 = new JPanel(new GridBagLayout());
        jPanelCancell2.add(CancellButton2);
        ActionListener CancellActionListener = new onCancellClickListner();
        CancellButton.addActionListener(CancellActionListener);
        CancellButton2.addActionListener(CancellActionListener);
        setComPort1.setLayout(new GridLayout(5, 2, 1, 1));

        setComPort1.add(comSpeed);
        setComPort1.add(jPanelComSpeedBox);
        setComPort1.add(comBits);
        setComPort1.add(jPanelComBitsBox);
        setComPort1.add(comStopBits);
        setComPort1.add(jPanelComStopBitsBox);
        setComPort1.add(comParityBit);
        setComPort1.add(jPanelComParityBitsBox);
        setComPort1.add(jPanelButton);
        setComPort1.add(jPanelCancell1);

        setComPort2.setLayout(new GridLayout(5, 2, 1, 1));
        setComPort2.add(comSpeed2);
        setComPort2.add(jPanelComSpeedBox2);
        setComPort2.add(comBits2);
        setComPort2.add(jPanelComBitsBox2);
        setComPort2.add(comStopBits2);
        setComPort2.add(jPanelComStopBitsBox2);
        setComPort2.add(comParityBit2);
        setComPort2.add(jPanelComParityBitsBox2);
        setComPort2.add(jPanelOKbutton);
        setComPort2.add(jPanelCancell2);
        setPreferredSize(new Dimension(520, 440));
        pack();
        setLocationRelativeTo(null);

        setVisible(true);


    }

    public class onAcceptClickListner implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            ConnectionParams sendPortParams = new ConnectionParams();
            ConnectionParams receivePortParams = new ConnectionParams();
            receivePortParams.baudRate = (int) comSpeedBox.getSelectedItem();
            receivePortParams.dataBits = (int) comBitsBox.getSelectedItem();
            receivePortParams.stopBits = comStopBitsBox.getSelectedIndex() + 1;
            receivePortParams.parity = comParityBitsBox.getSelectedIndex();
            sendPortParams.baudRate = (int) comSpeedBox2.getSelectedItem();
            sendPortParams.dataBits = (int) comBitsBox2.getSelectedItem();
            sendPortParams.stopBits = comStopBitsBox2.getSelectedIndex() + 1;
            sendPortParams.parity = comParityBitsBox2.getSelectedIndex();
            dataLinkLayer.setReceivePortParameters(receivePortParams.baudRate, receivePortParams.dataBits, receivePortParams.stopBits,
                    receivePortParams.parity);
            dataLinkLayer.setSendPortParameters(sendPortParams.baudRate, sendPortParams.dataBits, sendPortParams.stopBits,
                    sendPortParams.parity);
            dispose();
        }
    }

    public class onNextClickListner implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            tabbedPane.setSelectedIndex(1);
        }
    }

    public class onCancellClickListner implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            dispose();
        }
    }

}

package Physical;

import javax.comm.SerialPort;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.TooManyListenersException;

public class PhysicalLayer {
    private final PortService portService;

    // private final DataLinkLayer dataLinkLayer;

    private SerialPort portForSend;

    private SerialPort portForReceive;

    private final String portForSendName;

    private final String portForReceiveName;

    private OutputStream outputStream;

    private InputStream inputStream;

    private boolean inUse;

    public PhysicalLayer nextStation;


    public PhysicalLayer(/*DataLinkLayer dataLinkLayer,*/ PortService portService, String portForSendName, String portForReceiveName) {
        // this.dataLinkLayer = dataLinkLayer;
        this.portService = portService;
        this.portForSendName = portForSendName;
        this.portForReceiveName = portForReceiveName;
        this.inUse = false;
    }

    public synchronized void start() {
        portForSend = portService.openPort(portForSendName);
        try {
            outputStream = portForSend.getOutputStream();
            portForSend.addEventListener(new PortListener(this));
            portForSend.notifyOnDSR(true);
            portForSend.notifyOnBreakInterrupt(true);
            portForSend.notifyOnCarrierDetect(true);
        } catch (IOException | NullPointerException | TooManyListenersException e) {}

        portForReceive = portService.openPort(portForReceiveName);
        try {
            inputStream = portForReceive.getInputStream();
            portForReceive.addEventListener(new PortListener(this));
            portForReceive.notifyOnDataAvailable(true);
            portForReceive.notifyOnBreakInterrupt(true);
            portForReceive.notifyOnCarrierDetect(true);
        } catch (IOException | NullPointerException | TooManyListenersException e) {}

        inUse = true;
    }

    public synchronized void startAsIntermediate(SerialPort portForReceive) {
        portForSend = portService.openPort(portForSendName);
        try {
            outputStream = portForSend.getOutputStream();
        } catch (IOException e) {}

        this.portForReceive = portForReceive;
        try {
            inputStream = portForReceive.getInputStream();
            portForReceive.addEventListener(new PortListener(this));
            portForReceive.notifyOnDataAvailable(true);
        } catch (IOException | TooManyListenersException e) {}
    }

    public void setSendPortParameters(int baudRate, int dataBits, int stopBits, int parity) {
        portService.setPortParameters(portForSend, baudRate, dataBits, stopBits, parity);
    }

    public void setReceivePortParameters(int baudRate, int dataBits, int stopBits, int parity) {
        portService.setPortParameters(portForReceive, baudRate, dataBits, stopBits, parity);
    }

    public synchronized void closePortForSend() { portService.closePort(portForSend); }

    public synchronized void closePortForReceive() { portService.closePort(portForReceive); }

    public SerialPort getPortForSend() { return portForSend; }

    public SerialPort getPortForReceive() { return portForReceive; }

    public String getPortForReceiveName() { return portForReceiveName; }

    public synchronized void sendDataToNextStation(byte[] data) {
        System.out.println("sending data");
        SerialPort nextStationPortForReceive = portService.openPort(nextStation.getPortForReceiveName());
        if (nextStationPortForReceive != null)
            nextStation.startAsIntermediate(nextStationPortForReceive);
        try {
            outputStream.write(data);
            outputStream.flush();
        } catch (IOException e) {
            System.out.println("could not write data to output stream, port: " + portForSendName);
        }
    }

    public synchronized byte[] receiveDataFromPreviousStation() {
        int offset = 0;
        byte[] symbol = new byte[1];
        try {
            byte[] message = new byte[portForReceive.getDataBits() + 15];
            while (symbol[0] != '\n') {
                inputStream.read(symbol, 0, 1);
                if (symbol[0] != 0) {
                    message[offset++] = symbol[0];
                }
            }
            return message;
        } catch (IOException e) {
            return null;
        }
    }

    public boolean inUse() { return inUse; }

    public InputStream getInputStream() { return inputStream; }
}
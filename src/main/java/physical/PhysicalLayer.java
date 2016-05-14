package physical;

import link.OnPacketReceiveListener;

import javax.comm.SerialPort;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.*;

public class PhysicalLayer {
    private final PortService portService;

    private final OnPacketReceiveListener dataLinkLayer;

    private SerialPort portForSend;

    private SerialPort portForReceive;

    private final String portForSendName;

    private final String portForReceiveName;

    private OutputStream outputStream;

    private InputStream inputStream;

    private boolean inUse;

    private boolean isCurrentStation;

    private PhysicalLayer nextStation;

    private boolean hasDataToSend = false;

    Queue<byte[]> dataToSend = new LinkedList<>();


    public PhysicalLayer(OnPacketReceiveListener dataLinkLayer, PortService portService, String portForSendName, String portForReceiveName) {
        this.dataLinkLayer = dataLinkLayer;
        this.portService = portService;
        this.portForSendName = portForSendName;
        this.portForReceiveName = portForReceiveName;
        this.inUse = false;
    }

    public synchronized void start() {
        portForSend = portService.openPort(portForSendName);
        try {
            outputStream = portForSend.getOutputStream();
            portForSend.addEventListener(new PortListener(this, dataLinkLayer));
            portForSend.notifyOnDSR(true);
            portForSend.notifyOnBreakInterrupt(true);
            portForSend.notifyOnCarrierDetect(true);
        } catch (IOException | NullPointerException | TooManyListenersException e) {
        }

        portForReceive = portService.openPort(portForReceiveName);
        try {
            inputStream = portForReceive.getInputStream();
            portForReceive.addEventListener(new PortListener(this, dataLinkLayer));
            portForReceive.notifyOnDataAvailable(true);
            portForReceive.notifyOnBreakInterrupt(true);
            portForReceive.notifyOnCarrierDetect(true);
        } catch (IOException | NullPointerException | TooManyListenersException e) {
        }

        inUse = true;
        markAsCurrentStation();
    }

    public synchronized void stop() {
        portForSend.removeEventListener();
        portService.closePort(portForSend);
        portForSend = null;
        outputStream = null;

        portForReceive.removeEventListener();
        portService.closePort(portForReceive);
        portForReceive = null;
        inputStream = null;

        inUse = false;
        isCurrentStation = false;
    }

    private synchronized void startAsIntermediate(SerialPort portForReceive) {
        portForSend = portService.openPort(portForSendName);
        try {
            outputStream = portForSend.getOutputStream();
        } catch (IOException e) {
        }

        this.portForReceive = portForReceive;
        try {
            inputStream = portForReceive.getInputStream();
            portForReceive.addEventListener(new PortListener(this, dataLinkLayer));
            portForReceive.notifyOnDataAvailable(true);
        } catch (IOException | TooManyListenersException e) {
        }


    }

    public void setSendPortParameters(int baudRate, int dataBits, int stopBits, int parity) {
        portService.setPortParameters(portForSend, baudRate, dataBits, stopBits, parity);
    }

    public void setReceivePortParameters(int baudRate, int dataBits, int stopBits, int parity) {
        portService.setPortParameters(portForReceive, baudRate, dataBits, stopBits, parity);
    }

    synchronized void closePortForSend() {
        portService.closePort(portForSend);
    }

    synchronized void closePortForReceive() {
        portService.closePort(portForReceive);
    }

    public SerialPort getPortForSend() {
        return portForSend;
    }

    public SerialPort getPortForReceive() {
        return portForReceive;
    }

    private String getPortForReceiveName() {
        return portForReceiveName;
    }

    public synchronized void sendDataToNextStation() {
        SerialPort nextStationPortForReceive = portService.openPort(nextStation.getPortForReceiveName());
        if (nextStationPortForReceive != null)
            nextStation.startAsIntermediate(nextStationPortForReceive);
        else
            nextStation.markAsInUse();

        try {
            outputStream.write(dataToSend.poll());
            outputStream.flush();
        } catch (IOException e) {
            System.out.println("could not write data to output stream, port: " + portForSendName);
        }

        if (dataToSend.isEmpty()) hasDataToSend = false;
    }

    synchronized byte[] receiveDataFromPreviousStation() {
        List<Byte> symbolsList = new ArrayList<>();
        byte[] symbol = new byte[1];
        try {
            while (symbol[0] != '\n') {
                inputStream.read(symbol, 0, 1);
//              TODO: check needless
//                if (symbol[0] != 0) {
                symbolsList.add(symbol[0]);
//                }
            }
            byte[] message = new byte[symbolsList.size()];
            int offset = 0;
            for (byte b : symbolsList)
                message[offset++] = b;
            return message;
        } catch (IOException e) {
            return null;
        }
    }

    boolean inUse() {
        return inUse;
    }

    boolean isCurrentStation() {
        return isCurrentStation;
    }

    private void markAsInUse() {
        inUse = true;
    }

    private void markAsCurrentStation() {
        isCurrentStation = true;
    }

    InputStream getInputStream() {
        return inputStream;
    }

    public boolean hasDataToSend() {
        return hasDataToSend;
    }

    public void setDataToSend(byte[] data) {
        dataToSend.add(data);
        hasDataToSend = true;
    }

    public void setNextStation(PhysicalLayer nextStation) {
        this.nextStation = nextStation;
    }
}
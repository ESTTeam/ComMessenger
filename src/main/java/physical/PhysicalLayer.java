package physical;

import link.DataLinkLayer;
import link.OnPacketReceiveListener;
import link.packing.Frame;

import javax.comm.SerialPort;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.*;

import static java.lang.Thread.sleep;

public class PhysicalLayer {
    private final PortService portService;

    private final DataLinkLayer dataLinkLayer;

    private SerialPort portForSend;

    private SerialPort portForReceive;

    private final String portForSendName;

    private final String portForReceiveName;

    private OutputStream outputStream;

    private InputStream inputStream;

    private boolean inUse;

    private boolean isCurrentStation;

    private PhysicalLayer nextStation;

    private boolean iu = false;

    private boolean hasDataToSend = false;

    final Queue<byte[]> dataToSend = new LinkedList<>();

    private boolean isDisconnecting = false;

    public PhysicalLayer(DataLinkLayer dataLinkLayer, PortService portService, String portForSendName, String portForReceiveName) {
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
        //portService.setPortParameters(portForSend, baudRate, dataBits, stopBits, parity);
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

    private String getPortForReceiveName() {
        return portForReceiveName;
    }

    public synchronized void sendDataToNextStation() {
        sendDataToNextStation(dataToSend.poll());
        if (dataToSend.isEmpty()) hasDataToSend = false;
    }

    public synchronized void sendDataToNextStation(byte[] data) {
        if (!nextStation.inUse()) {
            SerialPort nextStationPortForReceive = portService.openPort(nextStation.getPortForReceiveName());
            if (nextStationPortForReceive != null) {
                nextStation.startAsIntermediate(nextStationPortForReceive);
                nextStation.markAsNotInUse();
                if (iu) {
                    int baudRate = portForReceive.getBaudRate();
                    int dataBits = portForReceive.getDataBits();
                    int stopBits = portForReceive.getStopBits();
                    int parity = portForReceive.getParity();
                    nextStation.setReceivePortParameters(baudRate, dataBits, stopBits, parity);
                }
            } else {
                nextStation.markAsInUse();
            }
        } else {
            try {
                sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        try {
            outputStream.write(data);
            outputStream.flush();
            if (iu)
                setSendPortParameters(portForReceive.getBaudRate(), portForReceive.getDataBits(),
                    portForReceive.getStopBits(), portForReceive.getParity());
        } catch (IOException e) {
            System.out.println("could not write data to output stream, port: " + portForSendName);
        }
    }

    synchronized byte[] receiveDataFromPreviousStation() {
        List<Byte> symbolsList = new ArrayList<>();
        try {
            byte[] symbol = new byte[1];

            boolean wasStop = false;
            while (true) {
                inputStream.read(symbol, 0, 1);
                symbolsList.add(symbol[0]);

                if (symbol[0] == Frame.STOP_BYTE) {
                    wasStop = true;
                } else if (wasStop && symbol[0] != Frame.START_BYTE) {
                    break;
                } else {
                    wasStop = false;
                }
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

    public boolean inUse() {
        return inUse;
    }

    boolean isCurrentStation() {
        return isCurrentStation;
    }

    public void markAsInUse() { inUse = true; }

    public void markAsNotInUse() {
        inUse = false;
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

    public boolean isDisconnecting() {
        return isDisconnecting;
    }

    public void setDisconnecting(boolean disconnecting) {
        isDisconnecting = true;
    }

    public String getPortForSendName() { return portForSendName; }

    public SerialPort getPortForSend() { return  portForSend; }

    public SerialPort getPortForReceive() { return portForReceive; }
}
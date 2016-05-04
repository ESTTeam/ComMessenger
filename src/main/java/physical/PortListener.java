package physical;

import link.OnReceiveListener;

import javax.comm.SerialPortEvent;
import javax.comm.SerialPortEventListener;
import java.io.IOException;

public class PortListener implements SerialPortEventListener {
    PhysicalLayer physicalLayer;
    OnReceiveListener dataLinkLayer;

    public PortListener(PhysicalLayer physicalLayer, OnReceiveListener dataLinkLayer)
    {
        this.physicalLayer = physicalLayer;
        this.dataLinkLayer = dataLinkLayer;
    }

    @Override
    public void serialEvent(SerialPortEvent event) {
        switch(event.getEventType()) {
            case SerialPortEvent.BI:
            case SerialPortEvent.OE:
            case SerialPortEvent.FE:
            case SerialPortEvent.PE:
            case SerialPortEvent.CD:
            case SerialPortEvent.CTS:
            case SerialPortEvent.DSR:
            case SerialPortEvent.RI:
                break;
            case SerialPortEvent.DATA_AVAILABLE:
                System.out.println("data available for " + physicalLayer.getPortForReceive().getName());
                if (!physicalLayer.inUse()) {
                    byte[] receivedMessage = physicalLayer.receiveDataFromPreviousStation();
                    try { physicalLayer.getInputStream().reset(); } catch (IOException e) {}
                    System.out.println(new String(receivedMessage));
                    physicalLayer.sendDataToNextStation(receivedMessage);
                    physicalLayer.closePortForReceive();
                    physicalLayer.closePortForSend();
                } else if (physicalLayer.isCurrentStation()){
                    byte[] receivedMessage = physicalLayer.receiveDataFromPreviousStation();
                    dataLinkLayer.onReceive(receivedMessage);
                    System.out.println(new String(receivedMessage));
                }
                break;
        }
    }
}

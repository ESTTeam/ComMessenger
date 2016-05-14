package physical;

import link.OnPacketReceiveListener;

import javax.comm.SerialPortEvent;
import javax.comm.SerialPortEventListener;
import java.io.IOException;

class PortListener implements SerialPortEventListener {
    private PhysicalLayer physicalLayer;
    private OnPacketReceiveListener dataLinkLayer;

    PortListener(PhysicalLayer physicalLayer, OnPacketReceiveListener dataLinkLayer)
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
                if (!physicalLayer.inUse()) {
                    byte[] receivedMessage = physicalLayer.receiveDataFromPreviousStation();
                    try { physicalLayer.getInputStream().reset(); } catch (IOException e) {}
                    physicalLayer.sendDataToNextStation();
                    physicalLayer.closePortForReceive();
                    physicalLayer.closePortForSend();
                } else if (physicalLayer.isCurrentStation()){
                    byte[] receivedMessage = physicalLayer.receiveDataFromPreviousStation();
                    dataLinkLayer.onPacketReceive(receivedMessage);
                }
                break;
        }
    }
}

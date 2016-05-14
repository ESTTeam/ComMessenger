package physical;

import javax.comm.*;
import java.util.Enumeration;

public class PortService {

    private static final int TIMEOUT = 200;

    public SerialPort openPort(String portName) {
        CommPortIdentifier portId = searchPort(portName);
        if (portId == null) {
            System.out.println("Port " + portName + " not found!");
            return null;
        }
        try {
            return (SerialPort) portId.open(portName, TIMEOUT);
        } catch (PortInUseException e) {
            return null;
        }
    }

    void closePort(SerialPort port) {
        if (port != null)
            port.close();
    }

    void setPortParameters(SerialPort port, int baudRate, int dataBits, int stopBits, int parity) {
        try {
            port.setSerialPortParams(baudRate, dataBits, stopBits, parity);
            System.out.append(port.getName()).append(" parameters changed: ");
            System.out.append("baud rate = " + baudRate);
            System.out.append(", data bits = " + dataBits);
            System.out.append(", stop bits = " + stopBits);
            System.out.append(", parity = " + parity + "\n");
        } catch (UnsupportedCommOperationException e) {
            System.out.println("Wrong parameters for port " + port.getName());
        } catch (NullPointerException e) {
            System.out.println("No port to change");
        }
    }

    private CommPortIdentifier searchPort(String portName) {
        Enumeration ports;
        ports = CommPortIdentifier.getPortIdentifiers();
        if (!ports.hasMoreElements()) {
            System.out.append("No comm ports found!");
        } else {
            while (ports.hasMoreElements()) {
                CommPortIdentifier portId = (CommPortIdentifier) ports.nextElement();
                if (portId.getName().equals(portName))
                    return portId;
            }
        }
        return null;
    }
}

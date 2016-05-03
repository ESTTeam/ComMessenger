import Physical.PhysicalLayer;
import Physical.PortService;
import Physical.PortListener;

import javax.comm.SerialPort;
import java.io.IOException;
import java.util.*;

import static java.lang.Thread.sleep;

/**
 * Created by Саша on 28.04.2016.
 */
public class Main {
    private static Map<PhysicalLayer, PhysicalLayer> nextWorkStation = new HashMap<>();

    public static void main(String[] args) {
        PortService portService = new PortService();
        PhysicalLayer ws = new PhysicalLayer (portService, "COM11", "COM12");
        PhysicalLayer ws2 = new PhysicalLayer(portService, "COM21", "COM22");
        PhysicalLayer ws3 = new PhysicalLayer(portService, "COM31", "COM32");
        PhysicalLayer ws4 = new PhysicalLayer(portService, "COM41", "COM42");
        PhysicalLayer ws5 = new PhysicalLayer(portService, "COM51", "COM52");

        ws.nextStation = ws2;
        ws2.nextStation = ws3;
        ws3.nextStation = ws4;
        ws4.nextStation = ws5;
        ws5.nextStation = ws;

        ws.start();
        ws.markAsCurrentStation();
        test(ws);
    }

    public static PhysicalLayer getNextStation(PhysicalLayer physicalLayer) {
        return nextWorkStation.get(physicalLayer);
    }

    public static void test(PhysicalLayer ws) {
        byte[] b = "one one one\n".getBytes();
        ws.sendDataToNextStation(b);

        try { sleep(2250); } catch (InterruptedException e) {}
        byte[] b1 = "two\n".getBytes();
        ws.sendDataToNextStation(b1);

        try { sleep(2250); } catch (InterruptedException e) {}
        byte[] b2 = "three\n".getBytes();
        ws.sendDataToNextStation(b2);
    }
}

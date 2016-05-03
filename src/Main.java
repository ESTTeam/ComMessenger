import Physical.PhysicalLayer;
import Physical.PortService;

import java.util.HashMap;
import java.util.Map;

import static java.lang.Thread.sleep;

public class Main {
    private static Map<PhysicalLayer, PhysicalLayer> nextWorkStation = new HashMap<>();

    public static void main(String[] args) {
        PortService portService = new PortService();
        PhysicalLayer ws = new PhysicalLayer(portService, "COM11", "COM12");
        PhysicalLayer ws2 = new PhysicalLayer(portService, "COM21", "COM22");
        PhysicalLayer ws3 = new PhysicalLayer(portService, "COM31", "COM32");
        PhysicalLayer ws4 = new PhysicalLayer(portService, "COM41", "COM42");
        PhysicalLayer ws5 = new PhysicalLayer(portService, "COM51", "COM52");

        ws.nextStation = ws2;
        ws2.nextStation = ws3;
        ws3.nextStation = ws4;
        ws4.nextStation = ws5;
        ws5.nextStation = ws;

        nextWorkStation.put(ws, ws2);
        nextWorkStation.put(ws2, ws3);
        nextWorkStation.put(ws3, ws4);
        nextWorkStation.put(ws4, ws5);
        nextWorkStation.put(ws5, ws);

        ws.start();
        ws4.start();

        byte[] b = "one\n".getBytes();
        ws.sendDataToNextStation(b);

        try { sleep(500); } catch (InterruptedException e) {}
        byte[] b1 = "two\n".getBytes();
        ws.sendDataToNextStation(b1);

        try { sleep(500); } catch (InterruptedException e) {}
        byte[] b2 = "three\n".getBytes();
        ws.sendDataToNextStation(b2);
    }

    public static PhysicalLayer getNextStation(PhysicalLayer physicalLayer) {
        return nextWorkStation.get(physicalLayer);
    }
}

import link.encoding.Decoder;
import link.encoding.Encoder;
import physical.PhysicalLayer;

import java.util.HashMap;
import java.util.Map;

import static java.lang.Thread.sleep;

public class Main {
    private static Map<PhysicalLayer, PhysicalLayer> nextWorkStation = new HashMap<>();

    public static void main(String[] args) {
        byte[] msg = "Hello, how are you?".getBytes();
        byte[] receivedMsg = Encoder.encode(msg);
        byte[] decodedMsg = Decoder.decode(receivedMsg);
        System.out.print(new String(decodedMsg));

//        DataLinkLayer ws = new DataLinkLayer(0);
//
//        ws.sendDataTo(4, "Data for 5 ws");
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

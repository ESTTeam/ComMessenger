import link.DataLinkLayer;
import link.encoding.Decoder;
import link.encoding.Encoder;
import link.encoding.TransmissionFailedException;
import physical.PhysicalLayer;
import user.OnMessageReceiveListener;

import java.util.HashMap;
import java.util.Map;

import static java.lang.Thread.sleep;

public class Main {
    private static Map<PhysicalLayer, PhysicalLayer> nextWorkStation = new HashMap<>();

    public static void main(String[] args) {
        DataLinkLayer ws = new DataLinkLayer(data -> {}, 0);

        ws.sendDataTo(0, "Data for 0 ws");
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

    public static void testEncoding() {
        byte[] msg = "Hello, how are you?".getBytes();
        byte[] receivedMsg = Encoder.encode(msg);
        byte[] decodedMsg = new byte[0];
        try {
            decodedMsg = Decoder.decode(receivedMsg);
        } catch (TransmissionFailedException e) {
            System.out.println("Ошибка при передаче");
        }
        System.out.print(new String(decodedMsg));
    }
}

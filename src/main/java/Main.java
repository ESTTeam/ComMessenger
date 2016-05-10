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
//        DataLinkLayer ws = new DataLinkLayer(data -> {}, "User 1", "COM11", "COM12");
//        DataLinkLayer ws = new DataLinkLayer(data -> {}, "User 2", "COM21", "COM22");
//        DataLinkLayer ws = new DataLinkLayer(data -> {}, "User 3", "COM31", "COM32");
        DataLinkLayer ws = new DataLinkLayer(data -> {}, "User 4", "COM41", "COM42");
//        DataLinkLayer ws = new DataLinkLayer(data -> {}, "User 5", "COM51", "COM52");

        try {
            sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        ws.sendDataTo("User 3", "Data for 5 ws");
    }
}

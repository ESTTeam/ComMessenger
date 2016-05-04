package link;

import physical.PhysicalLayer;
import physical.PortService;

import java.util.ArrayList;
import java.util.List;

// TODO: add getNextStation()
public class DataLinkLayer {

    private int mId;
    private PhysicalLayer physicalLayer;

    private List<PhysicalLayer> wsList;

    // TODO: add portNames
    // 0 <= id <= 4
    public DataLinkLayer(int id) {
        mId = id;

        PortService portService = new PortService();
        wsList = new ArrayList<>(5);
        wsList.add(new PhysicalLayer (portService, "COM11", "COM12"));
        wsList.add(new PhysicalLayer (portService, "COM21", "COM22"));
        wsList.add(new PhysicalLayer (portService, "COM31", "COM32"));
        wsList.add(new PhysicalLayer (portService, "COM41", "COM42"));
        wsList.add(new PhysicalLayer (portService, "COM51", "COM52"));

        for (int i = 0; i < wsList.size(); ++i) {
            if (i != wsList.size() - 1) {
                wsList.get(i).nextStation = wsList.get(i + 1);
            } else {
                wsList.get(i).nextStation = wsList.get(0);
            }
        }

        physicalLayer = wsList.get(id);
        physicalLayer.start();
        physicalLayer.markAsCurrentStation();
    }

    public void sendDataTo(int destinationId, String data) {

    }
}

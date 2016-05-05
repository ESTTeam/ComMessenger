package link;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import physical.PhysicalLayer;
import physical.PortService;

import java.util.ArrayList;
import java.util.List;

// TODO: add getNextStation()
public class DataLinkLayer implements OnPacketReceiveListener {

    private int mId;
    private PhysicalLayer physicalLayer;

    private List<PhysicalLayer> wsList;

    // TODO: add portNames
    // 0 <= id <= 4
    public DataLinkLayer(int id) {
        mId = id;

        PortService portService = new PortService();
        wsList = new ArrayList<>(5);
        wsList.add(new PhysicalLayer (this, portService, "COM11", "COM12"));
        wsList.add(new PhysicalLayer (this, portService, "COM21", "COM22"));
        wsList.add(new PhysicalLayer (this, portService, "COM31", "COM32"));
        wsList.add(new PhysicalLayer (this, portService, "COM41", "COM42"));
        wsList.add(new PhysicalLayer (this, portService, "COM51", "COM52"));

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
        Message msg = new Message(destinationId, mId, data);
        physicalLayer.sendDataToNextStation(jsonToBytes(msg.getJson()));
    }

    @Override
    public void onPacketReceive(byte[] bytes) {
        try {
            Message msg = new Message(bytesToJSON(bytes));
            if (msg.getDestinationId() == mId) {
                // TODO: send message to user layer
                System.out.println(msg.toString());
            } else {
                // TODO: add TIMEOUT CHECKING
                physicalLayer.sendDataToNextStation(bytes);
            }
        } catch (ParseException e) {
            // TODO: add exception handler
            e.printStackTrace();
        }
    }

    private byte[] jsonToBytes(JSONObject jsonObject) {
        String msg = jsonObject.toString() + "\n";
        return msg.getBytes();
    }

    private JSONObject bytesToJSON(byte[] bytes) throws ParseException {
        String jsonString = new String(bytes);
        jsonString = jsonString.substring(0, jsonString.length() - 1);
        return (JSONObject) new JSONParser().parse(jsonString);
    }
}

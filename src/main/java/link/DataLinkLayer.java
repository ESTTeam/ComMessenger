package link;

import link.encoding.Decoder;
import link.encoding.Encoder;
import link.encoding.TransmissionFailedException;
import link.packing.Packer;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import physical.PhysicalLayer;
import physical.PortService;
import user.OnMessageReceiveListener;

import java.util.ArrayList;
import java.util.List;

// TODO: add getNextStation()
public class DataLinkLayer implements OnPacketReceiveListener {

    private int mId;
    private PhysicalLayer mPhysicalLayer;
    private OnMessageReceiveListener mUserLayer;

    private List<PhysicalLayer> wsList;

    // TODO: add portNames
    // 0 <= id <= 4
    public DataLinkLayer(OnMessageReceiveListener userLayer, int id) {
        mId = id;
        mUserLayer = userLayer;

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

        mPhysicalLayer = wsList.get(id);
        mPhysicalLayer.start();
        mPhysicalLayer.markAsCurrentStation();
    }

    public void sendDataTo(int destinationId, String data) {
        Message msg = new Message(destinationId, mId, data);
        byte[] msgBytes = jsonToBytes(msg.getJson());
        byte[] encodedBytes = Encoder.encode(msgBytes);
        byte[] packet = Packer.pack(encodedBytes);
        mPhysicalLayer.sendDataToNextStation(packet);
    }

    @Override
    public void onPacketReceive(byte[] packet) {
        try {
            byte[] encodedBytes = Packer.unpack(packet);
            byte[] msgBytes = Decoder.decode(encodedBytes);
            Message msg = new Message(bytesToJSON(msgBytes));
            if (msg.getDestinationId() == mId) {
                mUserLayer.onMessageReceive(msg.getData());
                System.out.println(msg.toString());
            } else {
                // TODO: add TIMEOUT CHECKING
                mPhysicalLayer.sendDataToNextStation(packet);
            }
        } catch (TransmissionFailedException | ParseException e) {
            // TODO: add exception handler
            e.printStackTrace();
        }
    }

    private byte[] jsonToBytes(JSONObject jsonObject) {
        return jsonObject.toString().getBytes();
    }

    private JSONObject bytesToJSON(byte[] bytes) throws ParseException {
        return (JSONObject) new JSONParser().parse(new String(bytes));
    }
}

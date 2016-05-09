package link;

import link.encoding.Decoder;
import link.encoding.Encoder;
import link.encoding.TransmissionFailedException;
import link.packing.Frame;
import link.packing.Packer;
import link.packing.PacketWrongException;
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
    public DataLinkLayer(OnMessageReceiveListener userLayer, String userName, String portSender, String portReceiver) {
        mId = Character.getNumericValue(portSender.charAt(3)) - 1;
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

        PhysicalLayer ws = new PhysicalLayer(this, portService, "11", "12");
        ws.nextStation = ws;

        mPhysicalLayer = wsList.get(mId);
        mPhysicalLayer.start();
        mPhysicalLayer.markAsCurrentStation();
    }

    public void sendDataTo(int destinationId, String data) {
        byte[] encodedDataBytes = Encoder.encode(data.getBytes());
        Frame frame = new Frame((byte) mId, (byte) destinationId, Frame.FrameTypes.DATA, encodedDataBytes);
        byte[] packet = Packer.pack(frame.getFrame());
        mPhysicalLayer.sendDataToNextStation(packet);
    }

    @Override
    public void onPacketReceive(byte[] packet) {
        try {
            Frame frame = new Frame(Packer.unpack(packet));
            if (frame.getDestination() == mId) {
                byte[] data = Decoder.decode(frame.getData());
                switch (frame.getFrameType()) {
                    case DATA:
                        mUserLayer.onMessageReceive(new String(data));
                        System.out.println("RECEIVED: " + new String(data));
                    case REGISTRATION:
                        break;
                    default:
                        break;
                }
            } else {
                mPhysicalLayer.sendDataToNextStation(packet);
            }

        } catch (PacketWrongException | TransmissionFailedException e) {
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

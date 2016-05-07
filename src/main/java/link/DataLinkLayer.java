package link;

import link.encoding.Decoder;
import link.encoding.Encoder;
import link.encoding.TransmissionFailedException;
import link.packing.Frame;
import link.packing.Packer;
import link.packing.PacketWrongException;
import physical.PhysicalLayer;
import physical.PortService;
import user.OnMessageReceiveListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.lang.Thread.sleep;

public class DataLinkLayer implements OnPacketReceiveListener {

    private int mId;
    private String mUserName;
    private PhysicalLayer mPhysicalLayer;
    private OnMessageReceiveListener mUserLayer;

    private Map<String, Integer> mWsNamesList;

    public DataLinkLayer(OnMessageReceiveListener userLayer, String userName, String portSender, String portReceiver) {
        mUserLayer = userLayer;
        mId = Character.getNumericValue(portSender.charAt(3)) - 1;
        mUserName = userName;

        wsListInitialization();
    }

    private void wsListInitialization() {
        PortService portService = new PortService();
        List<PhysicalLayer> wsList = new ArrayList<>();
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

        mPhysicalLayer = wsList.get(mId);
        mPhysicalLayer.start();

        mWsNamesList = new HashMap<>(wsList.size());
        mWsNamesList.put(mUserName, mId);
        sendRegistrationData(mUserName);
    }

    public void sendDataTo(String destinationUser, String data) {
        byte destinationId = (byte) mWsNamesList.get(destinationUser).intValue();

        byte[] encodedDataBytes = Encoder.encode(data.getBytes());
        Frame frame = new Frame((byte) mId, destinationId, Frame.FrameTypes.DATA, encodedDataBytes);
        byte[] packet = Packer.pack(frame.getFrame());
        mPhysicalLayer.sendDataToNextStation(packet);
    }

    private void sendRegistrationData(String name) {
        byte[] encodedDataBytes = Encoder.encode(name.getBytes());
        Frame frame = new Frame((byte) mId, Frame.BROADCAST_BYTE, Frame.FrameTypes.REGISTRATION, encodedDataBytes);
        byte[] packet = Packer.pack(frame.getFrame());
        mPhysicalLayer.sendDataToNextStation(packet);
    }

    private void sendRegistrationResponseData(byte destinationId, String name) {
        byte[] encodedDataBytes = Encoder.encode(name.getBytes());
        Frame frame = new Frame((byte) mId, destinationId, Frame.FrameTypes.REGISTRATION_RESPONSE, encodedDataBytes);
        byte[] packet = Packer.pack(frame.getFrame());
        mPhysicalLayer.sendDataToNextStation(packet);
    }

    @Override
    public void onPacketReceive(byte[] packet) {
        try {
            Frame frame = new Frame(Packer.unpack(packet));

            switch (frame.getFrameType()) {
                case DATA:
                    onDataPacketReceived(packet, frame);
                    break;
                case REGISTRATION:
                    onRegistrationPacketReceived(packet, frame);
                    break;
                case REGISTRATION_RESPONSE:
                    onRegistrationResponsePacketReceived(packet, frame);
                    break;
                default:
                    break;
            }
        } catch (PacketWrongException e) {
            // TODO: add exception handler
            e.printStackTrace();
        }
    }

    private void onDataPacketReceived(byte[] packet, Frame frame) {
        if (frame.getDestination() == mId) {
            try {
                byte[] data = Decoder.decode(frame.getData());

                mUserLayer.onMessageReceive(new String(data));
                System.out.println("RECEIVED: " + new String(data));
            } catch (TransmissionFailedException e) {
                // TODO: add exception handler
                e.printStackTrace();
            }
        } else {
            // TODO: add TIMEOUT
            mPhysicalLayer.sendDataToNextStation(packet);
        }
    }

    private void onRegistrationPacketReceived(byte[] packet, Frame frame) {
        if (frame.getSource() != mId) {
            try {
                byte[] data = Decoder.decode(frame.getData());

                mWsNamesList.put(new String(data), (int) frame.getSource());
                mPhysicalLayer.sendDataToNextStation(packet);

                try {
                    sleep(2500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                sendRegistrationResponseData(frame.getSource(), mUserName);
            } catch (TransmissionFailedException e) {
                // TODO: add exception handler
                e.printStackTrace();
            }
        }
    }

    private synchronized void onRegistrationResponsePacketReceived(byte[] packet, Frame frame) {
        if (frame.getDestination() == mId) {
            try {
                byte[] data = Decoder.decode(frame.getData());

                mWsNamesList.put(new String(data), (int) frame.getSource());
            } catch (TransmissionFailedException e) {
                // TODO: add exception handler
                e.printStackTrace();
            }
        } else {
            mPhysicalLayer.sendDataToNextStation(packet);
        }
    }
}

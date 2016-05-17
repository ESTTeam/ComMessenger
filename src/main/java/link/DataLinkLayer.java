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

import java.nio.ByteBuffer;
import java.util.*;

import static java.lang.Thread.sleep;

public class DataLinkLayer implements OnPacketReceiveListener {

    private static final int SENDING_TIMEOUT = 300;
    private static final int SHORT_SENDING_TIMEOUT = 50;

    private final int mId;
    private final int mNextId;
    private final String mUserName;
    private PhysicalLayer mPhysicalLayer;
    private final OnMessageReceiveListener mUserLayer;

    private Map<String, Integer> mWsNamesMap;
    private List<PhysicalLayer> mWsList;

    private String mLastSentDestination = null;
    private String mLastSentData = null;

    public DataLinkLayer(OnMessageReceiveListener userLayer, String userName, String portSender, String portReceiver) {
        mUserLayer = userLayer;
        mId = Character.getNumericValue(portSender.charAt(3)) - 1;
        mNextId = (mId == 4) ? 0 : mId + 1;
        mUserName = userName;

        wsListInitialization();
    }

    public PhysicalLayer getMasterStation() { return mWsList.get(0); }

    private void wsListInitialization() {
        PortService portService = new PortService();
        mWsList = new ArrayList<>();
        mWsList.add(new PhysicalLayer (this, portService, "COM11", "COM12"));
        mWsList.add(new PhysicalLayer (this, portService, "COM21", "COM22"));
        mWsList.add(new PhysicalLayer (this, portService, "COM31", "COM32"));
        mWsList.add(new PhysicalLayer (this, portService, "COM41", "COM42"));
        mWsList.add(new PhysicalLayer (this, portService, "COM51", "COM52"));

        for (int i = 0; i < mWsList.size(); ++i) {
            if (i != mWsList.size() - 1) {
                mWsList.get(i).setNextStation(mWsList.get(i + 1));
            } else {
                mWsList.get(i).setNextStation(mWsList.get(0));
            }
        }

        mPhysicalLayer = mWsList.get(mId);
        mPhysicalLayer.start();

        mWsNamesMap = new HashMap<>(mWsList.size());
        mWsNamesMap.put(mUserName, mId);

        sendFirstTest();

        setRegistrationData(mUserName);
    }

    public void disconnect() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        System.out.println("Sending Disconnect from " + (mId + 1));
        System.out.println(calendar.get(Calendar.HOUR) + ":" + calendar.get(Calendar.MINUTE)
                + ":" + calendar.get(Calendar.SECOND) + "." + calendar.get(Calendar.MILLISECOND));

        byte[] encodedDataBytes = Encoder.encode(mUserName.getBytes());
        byte[] data = new byte[encodedDataBytes.length + 1];
        System.arraycopy(encodedDataBytes, 0, data, 0, encodedDataBytes.length);
        data[data.length - 1] = 0;
        Frame frame = new Frame((byte) mId, Frame.BROADCAST_BYTE, Frame.FrameTypes.DISCONNECT, data);
        byte[] packet = Packer.pack(frame.getFrame());
        mPhysicalLayer.setDataToSend(packet);

        mPhysicalLayer.setDisconnecting(true);
    }

    public List<String> getUsers() {
        return new ArrayList<>(mWsNamesMap.keySet());
    }

    public void setPortParameters(int baudRate, int dataBits, int stopBits, int parity) {
        sendPortParameters(baudRate, dataBits, stopBits, parity);
    }

    private void sendMarker() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        System.out.println("Sending Marker from " + (mId + 1));
        System.out.println(calendar.get(Calendar.HOUR) + ":" + calendar.get(Calendar.MINUTE)
                + ":" + calendar.get(Calendar.SECOND) + "." + calendar.get(Calendar.MILLISECOND));

        Frame frame = new Frame((byte) mId, Frame.BROADCAST_BYTE, Frame.FrameTypes.MARKER, new byte[0]);
        byte[] packet = Packer.pack(frame.getFrame());

        try {
            sleep(mWsList.get(mNextId).inUse() ? SHORT_SENDING_TIMEOUT : SENDING_TIMEOUT);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        mPhysicalLayer.sendDataToNextStation(packet);
    }

    private void sendFirstTest() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        System.out.println("Sending FirstTest from " + (mId + 1));
        System.out.println(calendar.get(Calendar.HOUR) + ":" + calendar.get(Calendar.MINUTE)
                + ":" + calendar.get(Calendar.SECOND) + "." + calendar.get(Calendar.MILLISECOND));

        Frame frame = new Frame((byte) mId, Frame.BROADCAST_BYTE, Frame.FrameTypes.FIRST_TEST, new byte[0]);
        byte[] packet = Packer.pack(frame.getFrame());
        mPhysicalLayer.setDataToSend(packet);
        mPhysicalLayer.sendDataToNextStation();
    }

    private void sendPortParameters(int baudRate, int dataBits, int stopBits, int parity) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        System.out.println("Sending PortParameters from " + (mId + 1));
        System.out.println(calendar.get(Calendar.HOUR) + ":" + calendar.get(Calendar.MINUTE)
                + ":" + calendar.get(Calendar.SECOND) + "." + calendar.get(Calendar.MILLISECOND));

        byte[] baudRateBytes = ByteBuffer.allocate(4).putInt(baudRate).array();
        byte[] dataBitsBytes = ByteBuffer.allocate(4).putInt(dataBits).array();
        byte[] stopBitsBytes = ByteBuffer.allocate(4).putInt(stopBits).array();
        byte[] parityBytes = ByteBuffer.allocate(4).putInt(parity).array();
        byte[] data = new byte[baudRateBytes.length + dataBitsBytes.length
                + stopBitsBytes.length + parityBytes.length];
        System.arraycopy(baudRateBytes, 0, data, 0, baudRateBytes.length);
        System.arraycopy(dataBitsBytes, 0, data, baudRateBytes.length, dataBitsBytes.length);
        System.arraycopy(stopBitsBytes, 0, data, baudRateBytes.length + dataBitsBytes.length, stopBitsBytes.length);
        System.arraycopy(parityBytes, 0, data, baudRateBytes.length + dataBitsBytes.length + stopBitsBytes.length,
                parityBytes.length);

        data = Encoder.encode(data);
        Frame frame = new Frame((byte) mId, Frame.BROADCAST_BYTE, Frame.FrameTypes.PORT_PARAMETERS, data);
        byte[] packet = Packer.pack(frame.getFrame());
        mPhysicalLayer.setDataToSend(packet);
    }

    public void setDataToSend(String destinationUser, String data) throws NoSuchUserException {
        byte destinationId;
        try {
            destinationId = (byte) mWsNamesMap.get(destinationUser).intValue();
        } catch (NullPointerException e) {
            throw new NoSuchUserException();
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        System.out.println("Sending Data from " + (mId + 1) + " to " + (destinationId + 1));
        System.out.println(calendar.get(Calendar.HOUR) + ":" + calendar.get(Calendar.MINUTE)
                + ":" + calendar.get(Calendar.SECOND) + "." + calendar.get(Calendar.MILLISECOND));

        byte[] encodedDataBytes = Encoder.encode(data.getBytes());
        Frame frame = new Frame((byte) mId, destinationId, Frame.FrameTypes.DATA, encodedDataBytes);
        byte[] packet = Packer.pack(frame.getFrame());
        mPhysicalLayer.setDataToSend(packet);

        mLastSentData = data;
        mLastSentDestination = destinationUser;
    }

    private void setRegistrationData(String name) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        System.out.println("Sending Registration from " + (mId + 1));
        System.out.println(calendar.get(Calendar.HOUR) + ":" + calendar.get(Calendar.MINUTE)
                + ":" + calendar.get(Calendar.SECOND) + "." + calendar.get(Calendar.MILLISECOND));

        byte[] encodedDataBytes = Encoder.encode(name.getBytes());
        Frame frame = new Frame((byte) mId, Frame.BROADCAST_BYTE, Frame.FrameTypes.REGISTRATION, encodedDataBytes);
        byte[] packet = Packer.pack(frame.getFrame());
        mPhysicalLayer.setDataToSend(packet);
    }

    private void setRegistrationResponseData(byte destinationId, String name) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        System.out.println("Sending RegistrationResponse from " + (mId + 1) + " to " + (destinationId + 1));
        System.out.println(calendar.get(Calendar.HOUR) + ":" + calendar.get(Calendar.MINUTE)
                + ":" + calendar.get(Calendar.SECOND) + "." + calendar.get(Calendar.MILLISECOND));

        byte[] encodedDataBytes = Encoder.encode(name.getBytes());
        Frame frame = new Frame((byte) mId, destinationId, Frame.FrameTypes.REGISTRATION_RESPONSE, encodedDataBytes);
        byte[] packet = Packer.pack(frame.getFrame());
        mPhysicalLayer.setDataToSend(packet);
    }

    private void setAcknowledgementPacket(byte destinationId) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        System.out.println("Sending Acknowledgement from " + (mId + 1) + " to " + (destinationId + 1));
        System.out.println(calendar.get(Calendar.HOUR) + ":" + calendar.get(Calendar.MINUTE)
                + ":" + calendar.get(Calendar.SECOND) + "." + calendar.get(Calendar.MILLISECOND));

        Frame frame = new Frame((byte) mId, destinationId, Frame.FrameTypes.ACKNOWLEDGMENT, new byte[0]);
        byte[] packet = Packer.pack(frame.getFrame());
        mPhysicalLayer.setDataToSend(packet);
    }

    private void setRepetitionPacket(byte destinationId) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        System.out.println("Sending Repetition from " + (mId + 1) + " to " + (destinationId + 1));
        System.out.println(calendar.get(Calendar.HOUR) + ":" + calendar.get(Calendar.MINUTE)
                + ":" + calendar.get(Calendar.SECOND) + "." + calendar.get(Calendar.MILLISECOND));

        Frame frame = new Frame((byte) mId, destinationId, Frame.FrameTypes.REPETITION, new byte[0]);
        byte[] packet = Packer.pack(frame.getFrame());
        mPhysicalLayer.setDataToSend(packet);
    }

    @Override
    public void onDSRLost() {
        mUserLayer.onDSRLost();
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
                case DISCONNECT:
                    onDisconnectPacketReceived(packet, frame);
                    break;
                case ACKNOWLEDGMENT:
                    onAcknowledgementPacketReceived(packet, frame);
                    break;
                case REPETITION:
                    onRepetitionPacketReceived(packet, frame);
                    break;
                case PORT_PARAMETERS:
                    onPortParametersPacketReceived(packet, frame);
                    break;
                case MARKER:
                    onMarkerPacketReceived(packet, frame);
                    break;
                case FIRST_TEST:
                    onFirstTestPacketReceived(frame);
                    break;
                default:
                    break;
            }
        } catch (PacketWrongException e) {
            e.printStackTrace();
        }
    }

    private void onDataPacketReceived(byte[] packet, Frame frame) {
        if (frame.getDestination() == mId) {
            try {
                byte[] data = Decoder.decode(frame.getData());

                String sourceUserName = null;
                for (String key : mWsNamesMap.keySet()) {
                    if (mWsNamesMap.get(key) == frame.getSource()) {
                        sourceUserName = key;
                    }
                }
                mUserLayer.onMessageReceive(sourceUserName, new String(data));

                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(System.currentTimeMillis());
                System.out.println("RECEIVED: " + new String(data) + " from " + (frame.getSource() + 1));
                System.out.println(calendar.get(Calendar.HOUR) + ":" + calendar.get(Calendar.MINUTE)
                        + ":" + calendar.get(Calendar.SECOND) + "." + calendar.get(Calendar.MILLISECOND));

                setAcknowledgementPacket(frame.getSource());
            } catch (TransmissionFailedException e) {
                setRepetitionPacket(frame.getSource());
                e.printStackTrace();
            }
        } else {
            mPhysicalLayer.setDataToSend(packet);
        }

        mPhysicalLayer.sendDataToNextStation();
    }

    private void onRegistrationPacketReceived(byte[] packet, Frame frame) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        System.out.println("Received Registration from " + (frame.getSource() + 1) + " on " + (mId + 1));
        System.out.println(calendar.get(Calendar.HOUR) + ":" + calendar.get(Calendar.MINUTE)
                + ":" + calendar.get(Calendar.SECOND) + "." + calendar.get(Calendar.MILLISECOND));

        if (frame.getSource() != mId) {
            mWsList.get(frame.getSource()).markAsInUse();
            try {
                String userName = new String(Decoder.decode(frame.getData()));

                mWsNamesMap.put(userName, (int) frame.getSource());
                mUserLayer.onUserAdd(userName);
                mPhysicalLayer.setDataToSend(packet);

                try {
                    sleep(mWsList.get(mNextId).inUse() ? SHORT_SENDING_TIMEOUT : SENDING_TIMEOUT);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                setRegistrationResponseData(frame.getSource(), mUserName);
                mPhysicalLayer.sendDataToNextStation();
            } catch (TransmissionFailedException e) {
                sendMarker();
                e.printStackTrace();
            }
        } else {
            sendMarker();
        }
    }

    private void onRegistrationResponsePacketReceived(byte[] packet, Frame frame) {
        if (frame.getDestination() == mId) {
            mWsList.get(frame.getSource()).markAsInUse();
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());
            System.out.println("Received RegistrationResponse from " + (frame.getSource() + 1) + " on " + (mId + 1));
            System.out.println(calendar.get(Calendar.HOUR) + ":" + calendar.get(Calendar.MINUTE)
                    + ":" + calendar.get(Calendar.SECOND) + "." + calendar.get(Calendar.MILLISECOND));

            try {
                String userName = new String(Decoder.decode(frame.getData()));

                mWsNamesMap.put(userName, (int) frame.getSource());
                mUserLayer.onUserAdd(userName);

                sendMarker();
            } catch (TransmissionFailedException e) {
                sendMarker();
                e.printStackTrace();
            }
        } else {
            try {
                sleep(mWsList.get(mNextId).inUse() ? SHORT_SENDING_TIMEOUT : SENDING_TIMEOUT);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            mPhysicalLayer.setDataToSend(packet);
            mPhysicalLayer.sendDataToNextStation();
        }
    }

    private void onDisconnectPacketReceived(byte[] packet, Frame frame) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        System.out.println("Received Disconnect from " + (frame.getSource() + 1) + " on " + (mId + 1));
        System.out.println(calendar.get(Calendar.HOUR) + ":" + calendar.get(Calendar.MINUTE)
                + ":" + calendar.get(Calendar.SECOND) + "." + calendar.get(Calendar.MILLISECOND));

        if (frame.getSource() != mId) {
            mWsList.get(frame.getSource()).markAsNotInUse();
            try {
                int frameDataLength = frame.getData().length;
                byte[] data = new byte[frameDataLength - 1];
                System.arraycopy(frame.getData(), 0, data, 0, frameDataLength - 1);

                boolean needToSendMarker = false;
                if (frame.getData()[frameDataLength - 1] == 0) {
                    needToSendMarker = true;

                    byte[] newData = new byte[frameDataLength];
                    System.arraycopy(frame.getData(), 0, newData, 0, frameDataLength - 1);
                    newData[frameDataLength - 1] = 1;
                    frame.setData(newData);
                    packet = Packer.pack(frame.getFrame());
                }

                String userName = new String(Decoder.decode(data));

                mWsNamesMap.remove(userName);
                mUserLayer.onUserDelete(userName);
                mPhysicalLayer.setDataToSend(packet);
                mPhysicalLayer.sendDataToNextStation();

                mWsList.get(frame.getSource()).markAsNotInUse();
                if (needToSendMarker) {
                    sleep(SENDING_TIMEOUT);
                    mWsList.get(frame.getSource()).markAsNotInUse();
                    sendMarker();
                }
            } catch (TransmissionFailedException | InterruptedException e) {
                e.printStackTrace();
            }
        } else {
            mPhysicalLayer.stop();
            mUserLayer.onDisconnect();
        }
    }

    private void onAcknowledgementPacketReceived(byte[] packet, Frame frame) {
        if (frame.getDestination() == mId) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());
            System.out.println("Received Acknowledgement from " + (frame.getSource() + 1) + " on " + (mId + 1));
            System.out.println(calendar.get(Calendar.HOUR) + ":" + calendar.get(Calendar.MINUTE)
                    + ":" + calendar.get(Calendar.SECOND) + "." + calendar.get(Calendar.MILLISECOND));

            mLastSentData = null;
            mLastSentDestination = null;

            sendMarker();
        } else {
            try {
                sleep(mWsList.get(mNextId).inUse() ? SHORT_SENDING_TIMEOUT : SENDING_TIMEOUT);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            mPhysicalLayer.setDataToSend(packet);
            mPhysicalLayer.sendDataToNextStation();

        }
    }

    private void onRepetitionPacketReceived(byte[] packet, Frame frame) {
        if (frame.getDestination() == mId) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());
            System.out.println("Received Repetition from " + (frame.getSource() + 1) + " on " + (mId + 1));
            System.out.println(calendar.get(Calendar.HOUR) + ":" + calendar.get(Calendar.MINUTE)
                    + ":" + calendar.get(Calendar.SECOND) + "." + calendar.get(Calendar.MILLISECOND));

            if (mLastSentDestination != null) {
                try {
                    setDataToSend(mLastSentDestination, mLastSentData);
                    mPhysicalLayer.sendDataToNextStation();
                } catch (NoSuchUserException e) {
                    mLastSentDestination = null;
                    mLastSentData = null;
                    sendMarker();
                }
            } else {
                sendMarker();
            }
        } else {
            try {
                sleep(mWsList.get(mNextId).inUse() ? SHORT_SENDING_TIMEOUT : SENDING_TIMEOUT);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            mPhysicalLayer.setDataToSend(packet);
            mPhysicalLayer.sendDataToNextStation();
        }
    }

    private void onPortParametersPacketReceived(byte[] packet, Frame frame) {
        try {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());
            System.out.println("Received PortParameters from " + (frame.getSource() + 1) + " on " + (mId + 1));
            System.out.println(calendar.get(Calendar.HOUR) + ":" + calendar.get(Calendar.MINUTE)
                    + ":" + calendar.get(Calendar.SECOND) + "." + calendar.get(Calendar.MILLISECOND));

            byte[] data = Decoder.decode(frame.getData());

            byte[] baudRateBytes = new byte[4];
            byte[] dataBitsBytes = new byte[4];
            byte[] stopBitsBytes = new byte[4];
            byte[] parityBytes = new byte[4];
            System.arraycopy(data, 0, baudRateBytes, 0, baudRateBytes.length);
            System.arraycopy(data, baudRateBytes.length, dataBitsBytes, 0, dataBitsBytes.length);
            System.arraycopy(data, baudRateBytes.length + dataBitsBytes.length, stopBitsBytes, 0, stopBitsBytes.length);
            System.arraycopy(data, baudRateBytes.length + dataBitsBytes.length + stopBitsBytes.length, parityBytes, 0,
                    parityBytes.length);

            int baudRate = ByteBuffer.wrap(baudRateBytes).getInt();
            int dataBits = ByteBuffer.wrap(dataBitsBytes).getInt();
            int stopBits = ByteBuffer.wrap(stopBitsBytes).getInt();
            int parity = ByteBuffer.wrap(parityBytes).getInt();

            if (frame.getSource() != mId) {
                mPhysicalLayer.sendDataToNextStation(packet);
                mUserLayer.onPortParametersChanged(baudRate, dataBits, stopBits, parity);
            } else {
                mUserLayer.onPortParametersChanged(baudRate, dataBits, stopBits, parity);
                sendMarker();
            }
        } catch (TransmissionFailedException e) {
            e.printStackTrace();
        }
    }

    private void onFirstTestPacketReceived(Frame frame) {
        if (frame.getSource() == mId) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());
            System.out.println("Received FirstTest from " + (frame.getSource() + 1) + " on " + (mId + 1));
            System.out.println(calendar.get(Calendar.HOUR) + ":" + calendar.get(Calendar.MINUTE)
                    + ":" + calendar.get(Calendar.SECOND) + "." + calendar.get(Calendar.MILLISECOND));

            sendMarker();
        }
    }

    private void onMarkerPacketReceived(byte[] packet, Frame frame) {
        //Calendar calendar = Calendar.getInstance();
        //calendar.setTimeInMillis(System.currentTimeMillis());
        //System.out.println("Received Marker from " + (frame.getSource() + 1) + " on " + (mId + 1));
        //System.out.println(calendar.get(Calendar.HOUR) + ":" + calendar.get(Calendar.MINUTE)
        //        + ":" + calendar.get(Calendar.SECOND) + "." + calendar.get(Calendar.MILLISECOND));

        if (!mPhysicalLayer.isDisconnecting()) {
            if (!mPhysicalLayer.hasDataToSend()) {
                mPhysicalLayer.setDataToSend(packet);
            }
        }

        try {
            sleep(mWsList.get(mNextId).inUse() ? SHORT_SENDING_TIMEOUT : SENDING_TIMEOUT);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        mPhysicalLayer.sendDataToNextStation();
    }
}

package link.packing;

public class Frame {

    public enum FrameTypes {
        DATA,
        REGISTRATION
    }

    private enum ControlFields {
        START_BYTE,
        SOURCE,
        DESTINATION,
        FRAME_TYPE,
        DATA_LENGTH,
        DATA,
        STOP_BYTE
    }

    private static final byte START_BYTE = (byte) 0xFF;
    private static final byte STOP_BYTE = (byte) 0xFF;
    public static final byte BROADCAST_BYTE = (byte) 0xFF;
    private static final int CONTROL_FIELD_COUNT = 6;

    private byte mSource;
    private byte mDestination;
    private FrameTypes mFrameType;
    private byte[] mData;
    private byte mDataLength;

    public Frame(byte[] frame) throws PacketWrongException {
        if (frame[ControlFields.START_BYTE.ordinal()] != START_BYTE
                || frame[frame.length - 1] != STOP_BYTE) {
            throw new PacketWrongException();
        } else {
            mSource = frame[ControlFields.SOURCE.ordinal()];
            mDestination = frame[ControlFields.DESTINATION.ordinal()];
            mFrameType = FrameTypes.values()[frame[ControlFields.FRAME_TYPE.ordinal()]];
            mDataLength = frame[ControlFields.DATA_LENGTH.ordinal()];
            mData = new byte[mDataLength];
            System.arraycopy(frame, ControlFields.DATA.ordinal(), mData, 0, mDataLength);
        }
    }

    public Frame(byte sourceId, byte destinationId, FrameTypes frameType, byte[] data) {
        mSource = sourceId;
        mDestination = destinationId;
        mFrameType = frameType;
        mData = data;
        mDataLength = (byte) data.length;
    }

    public byte[] getFrame() {
        byte[] frame = new byte[CONTROL_FIELD_COUNT + mDataLength];
        frame[ControlFields.START_BYTE.ordinal()] = START_BYTE;
        frame[ControlFields.SOURCE.ordinal()] = mSource;
        frame[ControlFields.DESTINATION.ordinal()] = mDestination;
        frame[ControlFields.FRAME_TYPE.ordinal()] = (byte) mFrameType.ordinal();
        frame[ControlFields.DATA_LENGTH.ordinal()] = mDataLength;
        System.arraycopy(mData, 0, frame, ControlFields.DATA.ordinal(), mDataLength);
        frame[frame.length - 1] = STOP_BYTE;

        return frame;
    }

    public byte getSource() {
        return mSource;
    }

    public byte getDestination() {
        return mDestination;
    }

    public FrameTypes getFrameType() {
        return mFrameType;
    }

    public byte[] getData() {
        return mData;
    }

    public byte getDataLength() {
        return mDataLength;
    }
}

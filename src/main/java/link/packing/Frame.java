package link.packing;

import java.util.ArrayList;
import java.util.List;

public class Frame {

    public enum FrameTypes {
        DATA,
        REGISTRATION,
        REGISTRATION_RESPONSE,
        DISCONNECT,
        ACKNOWLEDGMENT,
        REPETITION,
        MARKER,
        FIRST_TEST
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

    public static final byte START_BYTE = (byte) 0xFE;
    public static final byte STOP_BYTE = (byte) 0xFF;
    public static final byte BROADCAST_BYTE = (byte) 0xFD;
    private static final int CONTROL_FIELD_COUNT = 6;
    private final static int MAX_DATA_LENGTH = 100;

    private byte mSource;
    private byte mDestination;
    private FrameTypes mFrameType;
    private byte[] mData;
    private byte mDataLength = 0;

    public Frame(byte[] frame) throws PacketWrongException {
        if (frame[ControlFields.START_BYTE.ordinal()] != START_BYTE
                || frame[frame.length - 1] != STOP_BYTE) {
            throw new PacketWrongException();
        } else {
            List<Byte> data = new ArrayList<>();

            for (int i = 0; i < frame.length; ++i) {
                if (frame[i] == START_BYTE) {
                    if (i != 0 && (mSource != frame[i + 1]
                                    || mDestination != frame[i + 2]
                                    || mFrameType != FrameTypes.values()[frame[i + 3]])) {
                        throw new PacketWrongException();
                    }
                    mSource = frame[++i];
                    mDestination = frame[++i];
                    mFrameType = FrameTypes.values()[frame[++i]];
                    mDataLength += frame[++i];
                } else if (frame[i] != STOP_BYTE) {
                    data.add(frame[i]);
                }
            }

            mData = new byte[data.size()];
            for (int i = 0; i < data.size(); ++i) {
                mData[i] = data.get(i);
            }
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
        int offset = 0;
        int loop_count = mData.length/MAX_DATA_LENGTH;
        if (mData.length % MAX_DATA_LENGTH != 0 || loop_count == 0) {
            loop_count++;
        }

        byte[] result = new byte[loop_count * CONTROL_FIELD_COUNT + mData.length];

        int data_offset = 0;
        do {
            int length = (mData.length - data_offset <= MAX_DATA_LENGTH)
                    ? mData.length - data_offset : MAX_DATA_LENGTH;

            byte[] frame = new byte[CONTROL_FIELD_COUNT + length];
            frame[ControlFields.START_BYTE.ordinal()] = START_BYTE;
            frame[ControlFields.SOURCE.ordinal()] = mSource;
            frame[ControlFields.DESTINATION.ordinal()] = mDestination;
            frame[ControlFields.FRAME_TYPE.ordinal()] = (byte) mFrameType.ordinal();
            frame[ControlFields.DATA_LENGTH.ordinal()] = (byte) length;
            System.arraycopy(mData, data_offset, frame, ControlFields.DATA.ordinal(), length);
            frame[frame.length - 1] = STOP_BYTE;

            for (byte _byte : frame) {
                result[offset++] = _byte;
            }
            data_offset += length;
        } while ((data_offset) < mData.length);

        return result;
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

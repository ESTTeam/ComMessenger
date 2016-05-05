package link.encoding;

public class Decoder extends Coder {

    // TODO: add throwing exception
    public static byte[] decode(byte[] msg) {
        int[][] binaryMsg = new int[msg.length][ENCODED_BIT];

        int[][] decodedBinaryMsg = new int[msg.length][ORIGINAL_BIT];
        for (int i = 0; i < msg.length; ++i) {
            binaryMsg[i] = toBinary(msg[i]);
            System.arraycopy(binaryMsg[i], 0, decodedBinaryMsg[i], 0, ORIGINAL_BIT);
        }

        msg = new byte[msg.length/2];
        for (int i = 0; i < msg.length/2; ++i) {
            msg[i] = toDecimal(binaryMsg[i * 2], binaryMsg[i * 2 + 1]);
        }

        return msg;
    }

    private static int[] toBinary(byte source) {
        int[] result = new int[ENCODED_BIT];

        for (int i = 0; i < ENCODED_BIT; ++i) {
            result[ENCODED_BIT - 1 - i] = source % 2;
            source /= 2;
        }

        return result;
    }

    private static byte toDecimal(int[] firstByteBinary, int[] secondByteBinary) {
        byte result = 0;

        for (int i = ORIGINAL_BIT - 1; i >= 0; --i) {
            result += secondByteBinary[i] * Math.pow(2, (ORIGINAL_BIT - 1) - i);
        }
        for (int i = 2*ORIGINAL_BIT - 1; i >= ORIGINAL_BIT; --i) {
            result += firstByteBinary[i - ORIGINAL_BIT] * Math.pow(2, (2*ORIGINAL_BIT - 1) - i);
        }

        return result;
    }
}

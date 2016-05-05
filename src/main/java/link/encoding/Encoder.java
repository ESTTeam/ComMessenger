package link.encoding;

public class Encoder extends Coder {

    public static byte[] encode(byte[] msg) {
        int[][] binaryMsg = new int[msg.length*2][ENCODED_BIT];

        for (int i = 0; i < msg.length; ++i) {
            binaryMsg = toBinary(binaryMsg, i, msg[i]);
        }

        msg = new byte[binaryMsg.length];

        for (int i = 0; i < binaryMsg.length; ++i) {
            int[] supplementedVector = divide(binaryMsg[i]);
            System.arraycopy(supplementedVector, ORIGINAL_BIT, binaryMsg[i], ORIGINAL_BIT, ENCODED_BIT - ORIGINAL_BIT);
            msg[i] = toDecimal(binaryMsg[i]);
        }

        return msg;
    }

    private static int[][] toBinary(int[][] binaryMsg, int bytePosition, byte source) {
        int[] firstByteBinary   = new int[ENCODED_BIT];
        int[] secondByteBinary  = new int[ENCODED_BIT];

        for (int i = 0; i < ORIGINAL_BIT; ++i) {
            secondByteBinary[ORIGINAL_BIT - 1 - i] = source % 2;
            source /= 2;
        }
        for (int i = 0; i < ORIGINAL_BIT; ++i) {
            firstByteBinary[ORIGINAL_BIT - 1 - i] = source % 2;
            source /= 2;
        }
        for (int i = ORIGINAL_BIT; i < ENCODED_BIT; ++i) {
            firstByteBinary[i] = secondByteBinary[i] = 0;
        }

        binaryMsg[bytePosition * 2] = firstByteBinary;
        binaryMsg[bytePosition * 2 + 1] = secondByteBinary;
        return binaryMsg;
    }

    private static byte toDecimal(int[] source) {
        byte result = 0;
        for (int i = ENCODED_BIT - 1; i >= 0; --i) {
            result += source[i] * Math.pow(2, (ENCODED_BIT - 1) - i);
        }
        return result;
    }
}

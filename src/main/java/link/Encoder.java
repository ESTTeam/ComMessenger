package link;

public class Encoder {

    private static final int ENCODED_BIT = 11;
    private static final int ORIGINAL_BIT = 8;

    private static final int[] mGeneratorPolynomial = new int[]{1, 0, 1, 1};

    public static byte[] encode(byte[] msg) {
        int[][] binaryMsg = new int[msg.length][ENCODED_BIT];

        for (int i = 0; i < msg.length; ++i) {
            binaryMsg[i] = toBinary(msg[i]);
            int[] supplementedVector = divide(binaryMsg[i]);
            System.arraycopy(supplementedVector, ORIGINAL_BIT, binaryMsg[i], ORIGINAL_BIT, ENCODED_BIT - ORIGINAL_BIT);
        }

        return msg;
    }

    private static int[] toBinary(byte source) {
        int[] vector = new int[ENCODED_BIT];

        for (int i = 0; i < ORIGINAL_BIT; ++i) {
            vector[ORIGINAL_BIT - 1 - i] = source % 2;
            source /= 2;
        }
        for (int i = ORIGINAL_BIT; i < ENCODED_BIT; ++i) {
            vector[i] = 0;
        }

        return vector;
    }

    private static byte toDecimal(int[] source) {
        return 0;
    }

    private static int[] divide(int[] vector) {
        int[] result = new int[ENCODED_BIT];
        System.arraycopy(vector, 0, result, 0, vector.length);

        for (int i = 0; i < ORIGINAL_BIT; ++i) {
            if (result[i] >= mGeneratorPolynomial[0]) {
                for (int j = i; j < mGeneratorPolynomial.length + i; ++j) {
                    result[j] = divideMod2(result[j], mGeneratorPolynomial[j - i]);
                }
            }
        }

        return result;
    }

    private static int divideMod2(int left, int right) {
        return (left == right) ? 0 : 1;
    }
}

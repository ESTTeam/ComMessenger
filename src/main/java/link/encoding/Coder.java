package link.encoding;

abstract class Coder {

    static final int ENCODED_BIT = 7;
    static final int ORIGINAL_BIT = 4;

    private static final int[] mGeneratorPolynomial = new int[]{1, 0, 1, 1};

    static int[] divide(int[] vector) {
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

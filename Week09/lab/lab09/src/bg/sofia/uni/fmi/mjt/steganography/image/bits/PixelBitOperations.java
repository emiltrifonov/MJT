package bg.sofia.uni.fmi.mjt.steganography.image.bits;

public class PixelBitOperations {

    private static final int MASK_ONE = 1;
    private static final int RED_SHIFT = 16;
    private static final int GREEN_SHIFT = 8;
    private static final int BLUE_SHIFT = 0;
    private static final int BITS_IN_BYTE = 8;
    private static final int RED_BYTE_SHIFT = 2;
    private static final int GREEN_BYTE_SHIFT = 1;
    private static final int BLUE_BYTE_SHIFT = 0;
    private static final int RED_SHIFTS_IN_GRAY = BITS_IN_BYTE - 1;
    private static final int GREEN_SHIFTS_IN_GRAY = BITS_IN_BYTE - 2;
    private static final int BLUE_SHIFTS_IN_GRAY = BITS_IN_BYTE - 3;
    private static final int FULL_BYTE = 0xFF;

    public static int getRedBit(int num) {
        return getBit(num, RED_SHIFT);
    }

    public static int getGreenBit(int num) {
        return getBit(num, GREEN_SHIFT);
    }

    public static int getBlueBit(int num) {
        return getBit(num, BLUE_SHIFT);
    }

    public static int getBit(int num, int shift) {
        return ((num >> shift) & MASK_ONE);
    }

    public static int getRedByte(int num) {
        return (getByte(num, RED_BYTE_SHIFT));
    }

    public static int getGreenByte(int num) {
        return (getByte(num, GREEN_BYTE_SHIFT));
    }

    public static int getBlueByte(int num) {
        return (getByte(num, BLUE_BYTE_SHIFT));
    }

    public static int getByte(int num, int shift) {
        return (num >> (shift * BITS_IN_BYTE)) & FULL_BYTE;
    }

    /**
        Put all bits at the end of an indent-bit sized element
     */
    public static int putBitsInMSB(int rBit, int gBit, int bBit, int indent) {
        int res = 0;

        res |= (rBit << indent--);
        res |= (gBit << indent--);
        res |= (bBit << indent);

        return res;
    }

    /**
        Put all bits at the end of an 8-bit element
     */
    public static int putBitsInMSB(int rBit, int gBit, int bBit) {
        return putBitsInMSB(rBit, gBit, bBit, BITS_IN_BYTE - 1);
    }

    public static int distributeGrayToRGB(int gray) {
        return ((gray << RED_SHIFT) | (gray << GREEN_SHIFT) | gray);
    }

    public static int getNumWithToggledBit(int numToToggleIn, int bit, int shifts) {
        if (bit == 1) {
            numToToggleIn |= (bit << shifts);
        } else {
            numToToggleIn &= ~(1 << shifts);
        }

        return numToToggleIn;
    }

    public static int toggleLSBsOfRGB(int num, int rBit, int gBit, int bBit) {
        num = getNumWithToggledBit(num, rBit, RED_SHIFT);
        num = getNumWithToggledBit(num, gBit, GREEN_SHIFT);
        num = getNumWithToggledBit(num, bBit, BLUE_SHIFT);

        return num;
    }

    public static int getRedBitFromGray(int pixel) {
        return getBit(pixel, RED_SHIFTS_IN_GRAY);
    }

    public static int getGreenBitFromGray(int pixel) {
        return getBit(pixel, GREEN_SHIFTS_IN_GRAY);
    }

    public static int getBlueBitFromGray(int pixel) {
        return getBit(pixel, BLUE_SHIFTS_IN_GRAY);
    }

}

package granch.sps.pars;

import java.nio.ByteBuffer;

public class BitConverter {

    public static int toInt(byte[] bytes, int startIndex) {

            return (0xff & bytes[startIndex + 3])
                    | (0xff00 & (bytes[startIndex + 2] << 8))
                    | (0xff0000 & (bytes[startIndex + 1] << 16))
                    | (0xff000000 & (bytes[startIndex] << 24));

    }

    public static String toHexString(byte[] bytes, int startIndex) {
        if (bytes == null)
            return "null";
        int iMax = startIndex +5;
        if (iMax == 0)
            return "[]";
        StringBuilder b = new StringBuilder();
        b.append('[');
        for (int i = startIndex; ; i++) {
            b.append(String.format("%02x", bytes[i] & 0xFF).toUpperCase());
            if (i == iMax)
                return b.append(']').toString();
            b.append(", ");
        }
    }

    private static byte[] copyFrom(byte[] bytes, int startIndex, int length) {
        byte[] bits = new byte[length];
        for (int i = startIndex, j = 0; i < bytes.length && j < bits.length; i++, j++) {
            bits[j] = bytes[i];
        }
        return bits;
    }

    public static short toShort(byte[] bytes) {
            return (short) ((0xff & bytes[1]) | (0xff00 & (bytes[0] << 8)));
    }
    public static short toShort(byte[] bytes, int startIndex) {
        return toShort(copyFrom(bytes, startIndex, 2));
    }


    static byte[] toLongBytes(long x) {
        ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
        buffer.putLong(x);
        return buffer.array();
    }


    public static long toBytesLong(byte[] b) {
        ByteBuffer bb = ByteBuffer.allocate(b.length);
        bb.put(b);
        return bb.getLong();
    }
    public static String toString(byte[] a, int startIndex, int length) {
        if (a == null)
            return "null";
        //int iMax = a.length - 1;
        if (startIndex + length - 1 > a.length)
            throw new IndexOutOfBoundsException();

        StringBuilder b = new StringBuilder();
        for (int i = 0; i < length; i++) {
            b.append(a[startIndex + i]);
        }
        return b.toString();
    }

}

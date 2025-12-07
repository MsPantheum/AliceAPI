package alice.jvm.hotspot.vm.debugger;


public class DebuggerUtilities {

    public static String addressValueToString(long address) {
        StringBuilder buf = new StringBuilder();
        buf.append("0x");
        String val;
        val = Long.toHexString(address);

        for (int i = 0; (long) i < 2L * 8 - (long) val.length(); ++i) {
            buf.append('0');
        }

        buf.append(val);
        return buf.toString();
    }

    public static void checkAlignment(long address, long alignment) {
        if (address % alignment != 0L) {
            throw new UnalignedAddressException("Trying to read at address: " + addressValueToString(address) + " with alignment: " + alignment, address);
        }
    }

    public static long scanAddress(String addrStr) throws NumberFormatException {
        String s = addrStr.trim();
        if (!s.startsWith("0x")) {
            throw new NumberFormatException(addrStr);
        } else {
            long l = 0L;

            for (int i = 2; i < s.length(); ++i) {
                int val = charToNibble(s.charAt(i));
                l <<= 4;
                l |= val;
            }

            return l;
        }
    }

    public static int charToNibble(char ascii) throws NumberFormatException {
        if (ascii >= '0' && ascii <= '9') {
            return ascii - 48;
        } else if (ascii >= 'A' && ascii <= 'F') {
            return 10 + ascii - 65;
        } else if (ascii >= 'a' && ascii <= 'f') {
            return 10 + ascii - 97;
        } else {
            throw new NumberFormatException(Character.toString(ascii));
        }
    }

    public static boolean dataToJBoolean(byte[] data, long jbooleanSize) {
        checkDataContents(data, jbooleanSize);
        return data[0] != 0;
    }

    public static byte dataToJByte(byte[] data, long jbyteSize) {
        checkDataContents(data, jbyteSize);
        return data[0];
    }

    public static char dataToJChar(byte[] data, long jcharSize) {
        checkDataContents(data, jcharSize);
        byteSwap(data);

        return (char) ((data[0] & 255) << 8 | data[1] & 255);
    }

    public static double dataToJDouble(byte[] data, long jdoubleSize) {
        long longBits = dataToJLong(data, jdoubleSize);
        return Double.longBitsToDouble(longBits);
    }

    public static float dataToJFloat(byte[] data, long jfloatSize) {
        int intBits = dataToJInt(data, jfloatSize);
        return Float.intBitsToFloat(intBits);
    }

    public static int dataToJInt(byte[] data, long jintSize) {
        checkDataContents(data, jintSize);
        byteSwap(data);

        return (data[0] & 255) << 24 | (data[1] & 255) << 16 | (data[2] & 255) << 8 | data[3] & 255;
    }

    public static long dataToJLong(byte[] data, long jlongSize) {
        checkDataContents(data, jlongSize);
        byteSwap(data);

        return rawDataToJLong(data);
    }

    public static short dataToJShort(byte[] data, long jshortSize) {
        checkDataContents(data, jshortSize);
        byteSwap(data);

        return (short) ((data[0] & 255) << 8 | data[1] & 255);
    }

    public static long dataToCInteger(byte[] data, boolean isUnsigned) {
        checkDataContents(data, data.length);
        byteSwap(data);

        if (data.length < 8 && !isUnsigned && (data[0] & 128) != 0) {
            byte[] newData = new byte[8];

            for (int i = 0; i < 8; ++i) {
                if (7 - i < data.length) {
                    newData[i] = data[i + data.length - 8];
                } else {
                    newData[i] = -1;
                }
            }

            data = newData;
        }

        return rawDataToJLong(data);
    }

    public static long dataToAddressValue(byte[] data) {
        checkDataContents(data, 8);
        byteSwap(data);

        return rawDataToJLong(data);
    }

    public static byte[] jbooleanToData(boolean value) {
        byte[] res = new byte[1];
        res[0] = (byte) (value ? 1 : 0);
        return res;
    }

    public static byte[] jbyteToData(byte value) {
        byte[] res = new byte[1];
        res[0] = value;
        return res;
    }

    public static byte[] jcharToData(char value) {
        byte[] res = new byte[2];
        res[0] = (byte) (value >> 8 & 255);
        res[1] = (byte) value;
        byteSwap(res);

        return res;
    }

    public static byte[] jdoubleToData(double value) {
        return jlongToData(Double.doubleToLongBits(value));
    }

    public static byte[] jfloatToData(float value) {
        return jintToData(Float.floatToIntBits(value));
    }

    public static byte[] jintToData(int value) {
        byte[] res = new byte[4];

        for (int i = 0; i < 4; ++i) {
            res[3 - i] = (byte) (value & 255);
            value >>>= 8;
        }

        byteSwap(res);

        return res;
    }

    public static byte[] jlongToData(long value) {
        byte[] res = new byte[8];

        for (int i = 0; i < 8; ++i) {
            res[7 - i] = (byte) ((int) (value & 255L));
            value >>>= 8;
        }

        byteSwap(res);

        return res;
    }

    public static byte[] jshortToData(short value) {
        byte[] res = new byte[2];
        res[0] = (byte) (value >> 8 & 255);
        res[1] = (byte) value;
        byteSwap(res);

        return res;
    }

    public static byte[] cIntegerToData(long longNumBytes, long value) {
        int numBytes = (int) longNumBytes;
        byte[] res = new byte[numBytes];

        for (int i = 0; i < numBytes; ++i) {
            res[numBytes - i - 1] = (byte) ((int) (value & 255L));
            value >>>= 8;
        }

        byteSwap(res);

        return res;
    }

    private static void checkDataContents(byte[] data, long len) {
        if (data.length != (int) len) {
            throw new InternalError("Bug in Win32Debugger");
        }
    }

    private static void byteSwap(byte[] data) {
        for (int i = 0; i < data.length / 2; ++i) {
            int altIndex = data.length - i - 1;
            byte t = data[altIndex];
            data[altIndex] = data[i];
            data[i] = t;
        }

    }

    private static long rawDataToJLong(byte[] data) {
        long addr = 0L;

        for (byte datum : data) {
            addr <<= 8;
            addr |= datum & 255;
        }

        return addr;
    }
}
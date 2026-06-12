package alice._native;

import alice.util.Unsafe;

import java.nio.charset.StandardCharsets;

public final class CString extends NativeObject {

    private final String jstring;
    private final int byteLength;

    public static CString create(String str) {
        return new CString(str);
    }

    private CString(String str) {
        this(str, str.getBytes(StandardCharsets.UTF_8));
    }

    private CString(String str, byte[] bytes) {
        super(Unsafe.allocateMemory(bytes.length + 1L), true);
        this.jstring = str;
        this.byteLength = bytes.length;
        for (int i = 0; i < bytes.length; i++) {
            Unsafe.putByte(address + i, bytes[i]);
        }
        Unsafe.putByte(address + bytes.length, (byte) 0);
    }

    public CString(long address) {
        super(address);
        int length = 0;
        while (Unsafe.getByte(address + length) != 0) {
            length++;
        }
        byte[] bytes = new byte[length];
        for (int i = 0; i < length; i++) {
            bytes[i] = Unsafe.getByte(address + i);
        }
        this.jstring = new String(bytes, StandardCharsets.UTF_8);
        this.byteLength = length;
    }

    @Override
    public boolean equals(Object obj) {
        if (dead) {
            throw new IllegalStateException("c string already released!");
        }
        if (obj instanceof CString) {
            return ((CString) obj).jstring.equals(jstring);
        }
        return super.equals(obj);
    }

    @Override
    public long getSize() {
        return byteLength + 1L;
    }

    @Override
    public int hashCode() {
        if (dead) {
            throw new IllegalStateException("c string already released!");
        }
        return jstring.hashCode();
    }

    @Override
    public void release() {
        super.release();
    }

    @Override
    public String toString() {
        return jstring;
    }

}

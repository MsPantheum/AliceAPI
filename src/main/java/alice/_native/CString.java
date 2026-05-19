package alice._native;

import alice.util.Unsafe;
import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap;

import java.lang.ref.WeakReference;
import java.nio.charset.StandardCharsets;

public final class CString extends NativeObject {

    private static final Object2ObjectMap<String, WeakReference<CString>> cache = new Object2ObjectLinkedOpenHashMap<>();

    private final String jstring;

    public static CString create(String str) {
        WeakReference<CString> ref = cache.get(str);
        if (ref != null) {
            if (ref.get() != null) {
                return ref.get();
            } else {
                cache.remove(str);
            }
        }
        CString cstr = new CString(str);
        cache.put(str, new WeakReference<>(cstr));
        return cstr;
    }

    private CString(String str) {
        super();
        this.jstring = str;
        byte[] bytes = str.getBytes(StandardCharsets.UTF_8);
        for (int i = 0; i < str.length(); i++) {
            Unsafe.putByte(address + i, bytes[i]);
        }
        Unsafe.putByte(address + str.length(), (byte) 0);
    }

    public CString(long address) {
        super(address);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; ; i++) {
            char c = Unsafe.getChar(address + i);
            if (c == (byte) 0) {
                break;
            }
            sb.append(c);
        }
        jstring = sb.toString();
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
        return jstring.length() + 1;
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
        cache.remove(jstring);
    }

    @Override
    public String toString() {
        return jstring;
    }

}

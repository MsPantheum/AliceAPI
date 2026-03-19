package alice.util;

import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class CString {

    private static final Object2ObjectMap<String,CString> cache = new Object2ObjectLinkedOpenHashMap<>();

    private final long address;
    private final String jstring;
    private boolean dead = false;

    public static CString create(String str){
        CString cstr = cache.get(str);
        if(cstr != null){
            return cstr;
        }
        cstr = new CString(str);
        cache.put(str,cstr);
        return cstr;
    }

    private CString(String str) {
        this.jstring = str;
        address = Unsafe.allocateMemory(str.length() + 1);
        byte[] bytes = str.getBytes(StandardCharsets.UTF_8);
        for(int i = 0; i < str.length(); i++) {
            Unsafe.putByte(address + i, bytes[i]);
        }
        Unsafe.putByte(address + str.length(), (byte)0);
    }

    @Override
    public boolean equals(Object obj) {
        if(dead){
            throw new IllegalStateException("c string already released!");
        }
        if(obj instanceof CString) {
            return ((CString) obj).jstring.equals(jstring);
        }
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        if(dead){
            throw new IllegalStateException("c string already released!");
        }
        return jstring.hashCode();
    }

    public void release() {
        Unsafe.freeMemory(address);
        cache.remove(jstring);
        dead = true;
    }

    public long getAddress() {
        if(dead){
            throw new IllegalStateException("c string already released!");
        }
        return address;
    }

    @Override
    public String toString() {
        return jstring;
    }

    public long size(){
        return jstring.length();
    }

    @Override
    protected void finalize() throws Throwable {
        if(!dead){
            System.err.println("Memory leak detected! Someone allocated this c string but didn't release it when it was no longer referenced to!");
            System.err.print("Address:");
            AddressUtil.println(address,System.err);
            Unsafe.freeMemory(address);
        }
        super.finalize();
    }
}

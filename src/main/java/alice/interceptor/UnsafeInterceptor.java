package alice.interceptor;

import alice.HSDB;
import alice.exception.BadEnvironment;
import alice.log.Logger;
import alice.util.AddressUtil;
import alice.util.ClassLoaderUtil;
import alice.util.Converter;
import alice.util.MemoryUtil;
import sun.jvm.hotspot.oops.Klass;
import sun.jvm.hotspot.oops.Metadata;
import sun.jvm.hotspot.runtime.VM;
import sun.jvm.hotspot.types.WrongTypeException;
import sun.misc.Unsafe;

import java.net.URLClassLoader;

@SuppressWarnings("unused")
public final class UnsafeInterceptor {

    private static final boolean LOG_KLASS_REPLACE = "true".equals(System.getProperty("alice.debug.log.replace_klass"));
    static final boolean LOG_UCP_REPLACE = "true".equals(System.getProperty("alice.debug.log.replace_ucp"));

    private static final long narrow_klass_offset;

    static {
        if(!VM.getVM().isCompressedKlassPointersEnabled()) {
            throw new BadEnvironment("CompressedKlassPointer is disabled!");
        }
        narrow_klass_offset = HSDB.typeDataBase.lookupType("oopDesc").getField("_metadata._compressed_klass").getOffset();
    }

    public static void putIntVolatile(Unsafe unsafe, Object o,long offset, int x){
        if (checkPutIntTarget(o, offset, x)) {
            return;
        }
        alice.util.Unsafe.putIntVolatile(o,offset, x);
    }

    public static void putInt(Unsafe unsafe, Object o,long offset, int x){
        if (checkPutIntTarget(o, offset, x)) {
            return;
        }
        alice.util.Unsafe.putInt(o, offset, x);
    }

    private static boolean checkPutIntTarget(Object o, long offset, int x) {
        if(offset == narrow_klass_offset){
            long try_decode = MemoryUtil.decodeNarrowKlass(x);
            try {
                Metadata meta = Metadata.instantiateWrapperFor(Converter.toAddress(try_decode));
                if (meta instanceof Klass) {
                    if (LOG_KLASS_REPLACE) {
                        Logger.MAIN.warn("Warning: narrow klass replace detected!");
                        Logger.MAIN.warn("Original: " + o.getClass().getName());
                        Logger.MAIN.warn("New: " + ((Klass) meta).getName().asString());
                    }
                    return true;
                }
            } catch (WrongTypeException ignored) {

            }
        }
        return false;
    }

    public static void putInt(Unsafe unsafe, long address, int x) {
        int ori = alice.util.Unsafe.getInt(address);
        long try_decode = MemoryUtil.decodeNarrowKlass(ori);
        if (AddressUtil.safeAddress(try_decode)) {
            try {
                Metadata meta = Metadata.instantiateWrapperFor(Converter.toAddress(try_decode));
                if (meta instanceof Klass) {
                    if (LOG_KLASS_REPLACE) {
                        Logger.MAIN.warn("Warning: narrow klass replace detected!");
                        Logger.MAIN.warn("Original: " + ((Klass) meta).getName().asString());
                        meta = Metadata.instantiateWrapperFor(Converter.toAddress(address));
                        Logger.MAIN.warn("New: " + ((Klass) meta).getName().asString());
                    }
                    return;
                }
            } catch (WrongTypeException ignored) {

            }
            return;
        }
        alice.util.Unsafe.putInt(address, x);
    }

    private static boolean checkPutObjectTarget(Object o, long offset, Object x) {
        if (o instanceof URLClassLoader && offset == ClassLoaderUtil.ucp_offset) {
            if (LOG_UCP_REPLACE) {
                Logger.MAIN.warn("Warning: ucp replace detected!");
            }
            return true;
        }
        return false;
    }

    public static void putObject(Unsafe unsafe, Object o, long offset, Object x) {
        if (checkPutObjectTarget(o, offset, x)) {
            return;
        }
        alice.util.Unsafe.putObject(o, offset, x);
    }

    public static void putObjectVolatile(Unsafe unsafe, Object o, long offset, Object x) {
        if (checkPutObjectTarget(o, offset, x)) {
            return;
        }
        alice.util.Unsafe.putObjectVolatile(o, offset, x);
    }
}

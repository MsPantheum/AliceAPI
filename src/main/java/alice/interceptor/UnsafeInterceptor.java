package alice.interceptor;

import alice.util.AddressUtil;
import alice.util.MemoryUtil;
import sun.jvm.hotspot.oops.Klass;
import sun.jvm.hotspot.oops.Metadata;
import sun.jvm.hotspot.types.WrongTypeException;
import sun.misc.Unsafe;

public class UnsafeInterceptor {
    public static void putInt(Unsafe unsafe, long address, int x) {
        int ori = alice.util.Unsafe.getInt(address);
        long try_decode = MemoryUtil.decodeNarrowKlass(ori);
        if (AddressUtil.safeAddress(try_decode)) {
            try {
                Metadata meta = Metadata.instantiateWrapperFor(AddressUtil.toAddress(try_decode));
                if (meta instanceof Klass) {
                    System.out.println("Warning: narrow klass replace detected!");
                    System.out.println("Original: " + ((Klass) meta).getName().asString());
                    meta = Metadata.instantiateWrapperFor(AddressUtil.toAddress(address));
                    System.out.println("New: " + ((Klass) meta).getName().asString());
                    return;
                }
            } catch (WrongTypeException ignored) {

            }
        }
        alice.util.Unsafe.putInt(address, x);
    }
}

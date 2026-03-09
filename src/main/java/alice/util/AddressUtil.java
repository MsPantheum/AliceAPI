package alice.util;

import alice.HSDB;
import alice.Platform;
import sun.jvm.hotspot.debugger.Address;
import sun.jvm.hotspot.debugger.bsd.BsdDebuggerLocal;
import sun.jvm.hotspot.debugger.linux.LinuxDebuggerLocal;
import sun.jvm.hotspot.debugger.windbg.WindbgDebuggerLocal;

public class AddressUtil {
    public static long getAddressValue(Address addr) {
        return HSDB.debugger.getAddressValue(addr);
    }

    public static Address toAddress(long addr) {
        if(Platform.linux){
            return ((LinuxDebuggerLocal)HSDB.debugger).newAddress(addr);
        } else if (Platform.win32) {
            return ((WindbgDebuggerLocal)HSDB.debugger).newAddress(addr);
        } else if(Platform.bsd || Platform.darwin){
            return ((BsdDebuggerLocal)HSDB.debugger).newAddress(addr);
        }
        throw new IllegalStateException("Should not reach here");
    }
}

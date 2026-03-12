package alice._native;

import alice.util.AddressUtil;
import alice.util.Unsafe;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;

import static alice.util.Constants.*;

public class InlineHookSystemV {

    private static final Long2ObjectOpenHashMap<Hook> HOOKS = new Long2ObjectOpenHashMap<>();

    private static class Hook {
        private final long ori;
        private final long neo;
        private final byte[] backup;

        private long p2ori = 0;
        private boolean hooked;
        private long trampoline;

        private Hook(long ori, long neo) {
            this.ori = ori;
            this.neo = neo;
            this.backup = Unsafe.readBytes(ori, 12);
            this.hooked = false;
        }

//        private long hookWithTrampoline() {
//            if (hooked) {
//                return 0;
//            }
//            byte[] payload = new byte[12];
//            payload[0] = (byte) 0x48;
//            payload[1] = (byte) 0xb8;
//            payload[10] = (byte) 0xff;
//            payload[11] = (byte) 0xe0;
//            HDE64.Hde64s hs = new HDE64.Hde64s();
//            long need = hde64_disasm(ori,0,hs);
//            if((hs.modrm & 0xC7) == 0x05){
//                System.out.println("RIP Addressing not implemented yet.");
//                return 0;
//            }
//            long jump = ori + 12;
//            long trampoline = mmap.invoke(0,20,);
//            Unsafe.writeBytes(ori, payload);
//            hooked = true;
//            Unsafe.putLong(ori + 2,neo);
//
//            return 0;
//        }

        private boolean simpleHook() {
            if (hooked) {
                return false;
            }
            byte[] payload = new byte[12];
            payload[0] = (byte) 0x48;
            payload[1] = (byte) 0xb8;
            payload[10] = (byte) 0xff;
            payload[11] = (byte) 0xe0;
            Unsafe.writeBytes(ori, payload);
            Unsafe.putLong(ori + 2,neo);
            hooked = true;
            return true;
        }

        private boolean isHooked() {
            return hooked;
        }

        private boolean unhook() {
            if (hooked) {
                hooked = false;
                Unsafe.writeBytes(ori, backup);
                if(p2ori != 0){
                    Unsafe.freeMemory(p2ori);
                    p2ori = 0;
                }
                return true;
            }
            return false;
        }
    }

    public synchronized static boolean simpleHook(long ori, long neo) {
        Hook hook = HOOKS.get(ori);
        if (hook != null) {
            return hook.simpleHook();
        }

        System.out.printf("Creating hook from %x to %x.\n",ori,neo);
        System.out.println("Setting permission...");
        int success = mprotect.invoke(AddressUtil.align(ori), 1,PROT_READ | PROT_WRITE | PROT_EXEC);
        assert success == 0;
        System.out.println("Setting permission done.");

        hook = new Hook(ori, neo);
        HOOKS.put(ori, hook);
        hook.simpleHook();
        return true;
    }

    public synchronized static boolean unhook(long ori) {
        Hook hook = HOOKS.get(ori);
        if (hook != null) {
            return hook.unhook();
        }
        return false;
    }
}

package alice._native;

import alice.util.Unsafe;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;

import java.nio.ByteBuffer;

public class InlineHook {

    private static final Long2ObjectOpenHashMap<Hook> HOOKS = new Long2ObjectOpenHashMap<>();

    private static class Hook {
        private final long ori;
        private final long neo;
        private final byte[] backup;

        private boolean hooked;

        private Hook(long ori, long neo) {
            this.ori = ori;
            this.neo = neo;
            this.backup = Unsafe.readBytes(ori, 12);
            this.hooked = false;
        }

        private boolean hook() {
            if (hooked) {
                return false;
            }
            byte[] payload = new byte[12];
            payload[0] = (byte) 0x48;
            payload[1] = (byte) 0xb8;
            ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
            buffer.putLong(neo);
            byte[] addr = buffer.array();

            for (int i = 7, j = 2; i >= 0; i--, j++) {
                payload[j] = addr[i];
            }

            payload[10] = (byte) 0xff;
            payload[11] = (byte) 0xe0;
            System.out.println("Writing payload...");
            Unsafe.writeBytes(ori, payload);
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
                return true;
            }
            return false;
        }
    }

    public static boolean hook(long ori, long neo) {
        Hook hook = HOOKS.get(ori);
        if (hook != null) {
            return hook.hook();
        }

        System.out.printf("Creating hook from %x to %x.\n",ori,neo);
        System.out.println("Setting permission...");
        mprotect.invoke(ori, Unsafe.PAGE_SIZE);
        System.out.println("Setting permission done.");

        hook = new Hook(ori, neo);
        HOOKS.put(ori, hook);
        hook.hook();
        return true;
    }

    public static boolean unhook(long ori) {
        Hook hook = HOOKS.get(ori);
        if (hook != null) {
            return hook.unhook();
        }
        return false;
    }
}

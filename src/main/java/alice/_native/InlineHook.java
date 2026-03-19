package alice._native;

import alice.Platform;
import alice.injector.Shellcode;
import alice.util.AddressUtil;
import alice.util.HDE64;
import alice.util.Unsafe;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;

import static alice.util.Constants.*;
import static alice.util.HDE64.hde64_disasm;

@SuppressWarnings("UnusedReturnValue")
public class InlineHook {

    private static final Long2ObjectOpenHashMap<Hook> HOOKS = new Long2ObjectOpenHashMap<>();

    @FunctionalInterface
    public interface Processor {
        long process(long address);
    }

    private static class Hook {
        private final long ori;
        private final long neo;
        private byte[] backup;
        private final Processor prepare;

        private boolean hooked;
        private long trampoline = 0;

        private Hook(long ori, long neo, Processor prepare) {
            this.ori = ori;
            this.neo = neo;
            this.prepare = prepare;
            this.hooked = false;
        }

        private Hook(long ori, long neo) {
            this(ori, neo, null);
        }

        private static void MovRBX(long target, long offset, long mov_target) {
            long p = 0;
            Unsafe.putByte(target + offset + (p++), (byte) 0x48);
            Unsafe.putByte(target + offset + (p++), (byte) 0xBB);
            Unsafe.putLong(target + offset + (p), mov_target);
        }

        private long hookWithTrampoline(long p2trampoline) {
            if (hooked) {
                return 0;
            }
            HDE64.Hde64s hs = new HDE64.Hde64s();
            trampoline = mmap.invoke(ori, 128, PROT_READ | PROT_WRITE | PROT_EXEC, MAP_PRIVATE | MAP_ANONYMOUS, -1, 0);
            System.out.print("Trampoline=");
            AddressUtil.println(trampoline);
            long offset = prepare != null ? prepare.process(trampoline) : 0;
            int current = 0;
            while (current < 14) {
                int tmp = hde64_disasm(ori, current, hs);
                Unsafe.copy(ori + current, trampoline + current + offset, tmp);
                if ((hs.modrm & 0xC7) == 0x05) {//RIP Addressing
                    long original_target = ori + current + hs.len + hs.disp;
                    long new_rip = trampoline + current + offset + hs.len;
                    long new_disp = original_target - new_rip;
                    if (new_disp > Integer.MAX_VALUE || new_disp < Integer.MIN_VALUE) {
                        long p = trampoline + current + offset + hs.len - ((hs.flags & 0x3C) >> 2) - 4;
                        Unsafe.putInt(p, 0x2);
                        p += 4;
                        Unsafe.putByte(p++, (byte) 0xeb);
                        Unsafe.putByte(p++, (byte) 0x08);
                        Unsafe.putLong(p, original_target);
                        current += 14;
                        continue;
                    } else {
                        Unsafe.putInt(trampoline + current + offset + hs.len - ((hs.flags & 0x3C) >> 2) - 4, Math.toIntExact(new_disp));
                    }
                } else if (hs.opcode == 0xE8 || ((hs.opcode & 0xFD) == 0xE9)) {//Direct relative CALL and Direct relative JMP
                    long ori_target = ori + current + hs.len + hs.imm;
                    long neo_imm = ori_target - (trampoline + current + offset + hs.len);
                    if (neo_imm > Integer.MAX_VALUE || neo_imm < Integer.MIN_VALUE) {
                        if (hs.opcode == 0xE8) {
                            createJump(trampoline, current + offset, ori_target);
                        } else {
                            createCall(trampoline, current + offset, ori_target);
                        }
                        current += 14;
                        continue;
                    } else {
                        Unsafe.putInt(trampoline + current + offset + 1, Math.toIntExact(neo_imm));
                    }
                } else if ((hs.opcode & 0xf0) == 0x70) {//two bit short jcc
                    long original_target = ori + current + hs.len + hs.imm;
                    long neo_imm = original_target - (trampoline + current + offset + hs.len);
                    if (neo_imm <= Byte.MAX_VALUE && neo_imm >= Byte.MIN_VALUE) {
                        Unsafe.putByte(trampoline + current + offset + 1, (byte) neo_imm);
                    } else if (neo_imm <= Integer.MAX_VALUE && neo_imm >= Integer.MIN_VALUE) {
                        long p = trampoline + current + offset;
                        Unsafe.putByte(p++, (byte) 0x0f);
                        Unsafe.putByte(p++, (byte) ((hs.opcode2 & 0x0f) | 0x80));
                        Unsafe.putInt(p, Math.toIntExact(neo_imm));
                        current += 6;
                        continue;
                    } else {
                        throw new RuntimeException("The Fuck!");
                        //continue;
                    }
                } else if (hs.opcode == 0x0f && (hs.opcode2 & 0xf0) == 0x80) {//int32 jcc
                    long original_target = ori + current + hs.len + hs.imm;
                    long neo_imm = original_target - (trampoline + current + offset + hs.len);
                    if (neo_imm <= Integer.MAX_VALUE && neo_imm >= Integer.MIN_VALUE) {
                        Unsafe.putInt(trampoline + current + offset + 2, Math.toIntExact(neo_imm));
                    } else {
                        throw new RuntimeException("The Fuck!");
                        //continue;
                    }
                }

                current += tmp;
            }

            System.out.println("Current=" + current);

            createJump(trampoline, current + offset, ori + current);
            if (p2trampoline != 0) {
                Unsafe.putLong(p2trampoline, trampoline);
            }
            this.backup = Unsafe.readBytes(ori, current);
            System.out.println("Trampoline:");
            Shellcode.dump(trampoline,current + offset + 14,System.out);
            createJump(ori, 0, neo);
            System.out.println("Ori after hook:");
            Shellcode.dump(ori,28,System.out);
            hooked = true;
            return trampoline;
        }

        private static void createJump(long target, long offset, long jump_target) {
            long p = target + offset;
            Unsafe.putByte(p++, (byte) 0xff);
            Unsafe.putByte(p++, (byte) 0x25);
            Unsafe.putByte(p++, (byte) 0x00);
            Unsafe.putByte(p++, (byte) 0x00);
            Unsafe.putByte(p++, (byte) 0x00);
            Unsafe.putByte(p++, (byte) 0x00);
            Unsafe.putLong(p, jump_target);
        }

        private static void createCall(long target, long offset, long call_target) {
            long p = target + offset;
            Unsafe.putByte(p++, (byte) 0xff);
            Unsafe.putByte(p++, (byte) 0x15);
            Unsafe.putByte(p++, (byte) 0x02);
            Unsafe.putByte(p++, (byte) 0x00);
            Unsafe.putByte(p++, (byte) 0x00);
            Unsafe.putByte(p++, (byte) 0x00);
            Unsafe.putByte(p++, (byte) 0xeb);
            Unsafe.putByte(p++, (byte) 0x08);
            Unsafe.putLong(p, call_target);
        }

        private long hookWithTrampoline() {
            return hookWithTrampoline(0);
        }

        private boolean simpleHook() {
            if (hooked) {
                return false;
            }
            this.backup = Unsafe.readBytes(ori, 14);
            createJump(ori, 0, neo);
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
                if (trampoline != 0) {
                    munmap.invoke(trampoline, 128);
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
        int success = Platform.win32 ? VirtualProtect.invoke(ori,1,0x40,0) : mprotect.invoke(AddressUtil.align(ori), 1, PROT_READ | PROT_WRITE | PROT_EXEC);
        assert Platform.win32 == (success != 0);
        hook = new Hook(ori, neo);
        HOOKS.put(ori, hook);
        hook.simpleHook();
        return true;
    }

//    public synchronized static long hookWithTrampoline(MethodInfo ori_method, MethodInfo neo_method, MethodInfo trampoline_method) {
//        long ori = Shellcode.getCompiledEntry(ori_method.holder, ori_method.methodName, ori_method.methodDesc);
//        long neo = Shellcode.getCompiledEntry(neo_method.holder, neo_method.methodName, neo_method.methodDesc);
//        long p2trampoline = Shellcode.getPointer2CompiledEntry(trampoline_method.holder, trampoline_method.methodName, trampoline_method.methodDesc);
//
//        System.out.print("ori_method=");
//        AddressUtil.println(AddressUtil.getMethod(ori_method));
//        System.out.print("Ori=");
//        AddressUtil.println(ori);
//        System.out.print("Neo=");
//        AddressUtil.println(neo);
//
//        Hook hook = HOOKS.get(ori);
//        if (hook != null) {
//            return hook.hookWithTrampoline(p2trampoline);
//        }
//
//        int success = mprotect.invoke(AddressUtil.align(ori), 1, PROT_READ | PROT_WRITE | PROT_EXEC);
//        assert success == 0;
//        Processor p = address -> {
//            Unsafe.putByte(address++, (byte) 0x48);
//            Unsafe.putByte(address++, (byte) 0x8b);
//            Unsafe.putByte(address++, (byte) 0xe5);
//            Unsafe.putByte(address++, (byte) 0x5d);
//            Unsafe.putByte(address++, (byte) 0x48);
//            Unsafe.putByte(address++, (byte) 0x83);
//            Unsafe.putByte(address++, (byte) 0xc4);
//            Unsafe.putByte(address++, (byte) 0x08);
//            Hook.MovRBX(address,0,AddressUtil.getMethod(ori_method));
//            address += 10;
//            Unsafe.putByte(address++, (byte) 0x55);
//            return 19;
//        };
//        hook = new Hook(ori, neo, p);
//        HOOKS.put(ori, hook);
//        System.out.println("Ori before hook:");
//        Shellcode.dump(ori,28,System.out);
//        return hook.hookWithTrampoline(p2trampoline);
//    }

    public synchronized static long hookWithTrampoline(long ori, long neo) {
        Hook hook = HOOKS.get(ori);
        if (hook != null) {
            return hook.hookWithTrampoline();
        }

        int success = mprotect.invoke(AddressUtil.align(ori), 1, PROT_READ | PROT_WRITE | PROT_EXEC);
        assert success == 0;
        hook = new Hook(ori, neo);
        HOOKS.put(ori, hook);
        return hook.hookWithTrampoline();
    }

    public synchronized static long hookWithTrampoline(long ori, long neo, long p2trampoline) {
        Hook hook = HOOKS.get(ori);
        if (hook != null) {
            return hook.hookWithTrampoline(p2trampoline);
        }

        int success = mprotect.invoke(AddressUtil.align(ori), 1, PROT_READ | PROT_WRITE | PROT_EXEC);
        assert success == 0;
        hook = new Hook(ori, neo);
        HOOKS.put(ori, hook);
        return hook.hookWithTrampoline(p2trampoline);
    }

    public synchronized static boolean unhook(long ori) {
        Hook hook = HOOKS.get(ori);
        if (hook != null) {
            return hook.unhook();
        }
        return false;
    }
}

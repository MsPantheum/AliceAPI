package alice._native;

import alice.util.Unsafe;

public class ASMUtil {

    public enum Register {
        RAX(0),
        RCX(1),
        RDX(2),
        RBX(3),
        RSP(4),
        RBP(5),
        RSI(6),
        RDI(7),
        R8(8),
        R9(9),
        R10(10),
        R11(11),
        R12(12),
        R13(13),
        R14(14),
        R15(15);

        private final int code;

        Register(int code) {
            this.code = code;
        }

        private int low() {
            return code & 7;
        }

        private boolean high() {
            return (code & 8) != 0;
        }
    }

    private ASMUtil() {
    }

    public static void pushRCX(long codebase) {
        push(codebase, Register.RCX);
    }

    public static void pushRCX(byte[] payload, int index) {
        push(payload, index, Register.RCX);
    }

    public static void pushRDX(long codebase) {
        push(codebase, Register.RDX);
    }

    public static void pushRDX(byte[] payload, int index) {
        push(payload, index, Register.RDX);
    }

    public static void popRAX(long codebase) {
        pop(codebase, Register.RAX);
    }

    public static void popRAX(byte[] payload, int index) {
        pop(payload, index, Register.RAX);
    }

    public static void pushValue(long codebase, int value) {
        pushInt(codebase, value);
    }

    public static void pushValue(byte[] payload, int index, int value) {
        pushInt(payload, index, value);
    }

    public static int push(long codebase, Register register) {
        long p = codebase;
        if (register.high()) {
            Unsafe.putByte(p++, (byte) 0x41);
        }
        Unsafe.putByte(p++, (byte) (0x50 | register.low()));
        return (int) (p - codebase);
    }

    public static int push(byte[] payload, int index, Register register) {
        int p = index;
        if (register.high()) {
            payload[p++] = (byte) 0x41;
        }
        payload[p++] = (byte) (0x50 | register.low());
        return p;
    }

    public static int pop(long codebase, Register register) {
        long p = codebase;
        if (register.high()) {
            Unsafe.putByte(p++, (byte) 0x41);
        }
        Unsafe.putByte(p++, (byte) (0x58 | register.low()));
        return (int) (p - codebase);
    }

    public static int pop(byte[] payload, int index, Register register) {
        int p = index;
        if (register.high()) {
            payload[p++] = (byte) 0x41;
        }
        payload[p++] = (byte) (0x58 | register.low());
        return p;
    }

    public static int pushInt(long codebase, int value) {
        Unsafe.putByte(codebase, (byte) 0x68);
        Unsafe.putInt(codebase + 1, value);
        return 5;
    }

    public static int pushInt(byte[] payload, int index, int value) {
        payload[index] = (byte) 0x68;
        putInt(payload, index + 1, value);
        return index + 5;
    }

    public static int pushLong(long codebase, long value) {
        int offset = movabs(codebase, Register.RAX, value);
        return offset + push(codebase + offset, Register.RAX);
    }

    public static int pushLong(long codebase, Register scratch, long value) {
        int offset = movabs(codebase, scratch, value);
        return offset + push(codebase + offset, scratch);
    }

    public static int pushLong(byte[] payload, int index, long value) {
        return pushLong(payload, index, Register.RAX, value);
    }

    public static int pushLong(byte[] payload, int index, Register scratch, long value) {
        int p = movabs(payload, index, scratch, value);
        return push(payload, p, scratch);
    }

    public static int movabs(long codebase, Register register, long value) {
        long p = codebase;
        Unsafe.putByte(p++, (byte) (0x48 | (register.high() ? 1 : 0)));
        Unsafe.putByte(p++, (byte) (0xb8 | register.low()));
        Unsafe.putLong(p, value);
        return 10;
    }

    public static int movabs(long codebase, Register register) {
        return movabs(codebase, register, 0L);
    }

    public static int movabs(byte[] payload, int index, Register register, long value) {
        payload[index] = (byte) (0x48 | (register.high() ? 1 : 0));
        payload[index + 1] = (byte) (0xb8 | register.low());
        putLong(payload, index + 2, value);
        return index + 10;
    }

    public static int movabs(byte[] payload, int index, Register register) {
        return movabs(payload, index, register, 0L);
    }

    public static int mov(long codebase, Register dst, Register src) {
        long p = codebase;
        Unsafe.putByte(p++, rex(true, src, dst));
        Unsafe.putByte(p++, (byte) 0x89);
        Unsafe.putByte(p++, modRM(3, src.low(), dst.low()));
        return (int) (p - codebase);
    }

    public static int mov(byte[] payload, int index, Register dst, Register src) {
        int p = index;
        payload[p++] = rex(true, src, dst);
        payload[p++] = (byte) 0x89;
        payload[p++] = modRM(3, src.low(), dst.low());
        return p;
    }

    public static int movImm32(long codebase, Register register, int value) {
        long p = codebase;
        if (register.high()) {
            Unsafe.putByte(p++, (byte) 0x41);
        }
        Unsafe.putByte(p++, (byte) (0xb8 | register.low()));
        Unsafe.putInt(p, value);
        return (int) (p - codebase) + Integer.BYTES;
    }

    public static int movImm32(byte[] payload, int index, Register register, int value) {
        int p = index;
        if (register.high()) {
            payload[p++] = (byte) 0x41;
        }
        payload[p++] = (byte) (0xb8 | register.low());
        putInt(payload, p, value);
        return p + Integer.BYTES;
    }

    public static int movStack(long codebase, int stackOffset, Register src) {
        checkUInt8(stackOffset);
        long p = codebase;
        Unsafe.putByte(p++, rex(true, src, Register.RSP));
        Unsafe.putByte(p++, (byte) 0x89);
        Unsafe.putByte(p++, (byte) 0x44);
        Unsafe.putByte(p++, (byte) 0x24);
        Unsafe.putByte(p++, (byte) stackOffset);
        return (int) (p - codebase);
    }

    public static int movStack(byte[] payload, int index, int stackOffset, Register src) {
        checkUInt8(stackOffset);
        int p = index;
        payload[p++] = rex(true, src, Register.RSP);
        payload[p++] = (byte) 0x89;
        payload[p++] = (byte) 0x44;
        payload[p++] = (byte) 0x24;
        payload[p++] = (byte) stackOffset;
        return p;
    }

    public static int callStack(long codebase, int stackOffset) {
        checkUInt8(stackOffset);
        Unsafe.putByte(codebase, (byte) 0xff);
        Unsafe.putByte(codebase + 1, (byte) 0x54);
        Unsafe.putByte(codebase + 2, (byte) 0x24);
        Unsafe.putByte(codebase + 3, (byte) stackOffset);
        return 4;
    }

    public static int callStack(byte[] payload, int index, int stackOffset) {
        checkUInt8(stackOffset);
        payload[index] = (byte) 0xff;
        payload[index + 1] = (byte) 0x54;
        payload[index + 2] = (byte) 0x24;
        payload[index + 3] = (byte) stackOffset;
        return index + 4;
    }

    public static int jmp(long codebase, Register register) {
        long p = codebase;
        if (register.high()) {
            Unsafe.putByte(p++, (byte) 0x41);
        }
        Unsafe.putByte(p++, (byte) 0xff);
        Unsafe.putByte(p++, modRM(3, 4, register.low()));
        return (int) (p - codebase);
    }

    public static int jmp(byte[] payload, int index, Register register) {
        int p = index;
        if (register.high()) {
            payload[p++] = (byte) 0x41;
        }
        payload[p++] = (byte) 0xff;
        payload[p++] = modRM(3, 4, register.low());
        return p;
    }

    public static int call(long codebase, Register register) {
        long p = codebase;
        if (register.high()) {
            Unsafe.putByte(p++, (byte) 0x41);
        }
        Unsafe.putByte(p++, (byte) 0xff);
        Unsafe.putByte(p++, modRM(3, 2, register.low()));
        return (int) (p - codebase);
    }

    public static int call(byte[] payload, int index, Register register) {
        int p = index;
        if (register.high()) {
            payload[p++] = (byte) 0x41;
        }
        payload[p++] = (byte) 0xff;
        payload[p++] = modRM(3, 2, register.low());
        return p;
    }

    public static int ret(long codebase) {
        Unsafe.putByte(codebase, (byte) 0xc3);
        return 1;
    }

    public static int ret(byte[] payload, int index) {
        payload[index] = (byte) 0xc3;
        return index + 1;
    }

    public static int nop(long codebase) {
        Unsafe.putByte(codebase, (byte) 0x90);
        return 1;
    }

    public static int nop(byte[] payload, int index) {
        payload[index] = (byte) 0x90;
        return index + 1;
    }

    public static int leave(long codebase) {
        Unsafe.putByte(codebase, (byte) 0xc9);
        return 1;
    }

    public static int leave(byte[] payload, int index) {
        payload[index] = (byte) 0xc9;
        return index + 1;
    }

    public static int addRsp(long codebase, int value) {
        return rspImm8(codebase, (byte) 0xc4, value);
    }

    public static int addRsp(byte[] payload, int index, int value) {
        return rspImm8(payload, index, (byte) 0xc4, value);
    }

    public static int subRsp(long codebase, int value) {
        return rspImm8(codebase, (byte) 0xec, value);
    }

    public static int subRsp(byte[] payload, int index, int value) {
        return rspImm8(payload, index, (byte) 0xec, value);
    }

    public static int alignStackForCall(long codebase) {
        return subRsp(codebase, 8);
    }

    public static int alignStackForCall(byte[] payload, int index) {
        return subRsp(payload, index, 8);
    }

    public static int restoreStackAfterCall(long codebase) {
        return addRsp(codebase, 8);
    }

    public static int restoreStackAfterCall(byte[] payload, int index) {
        return addRsp(payload, index, 8);
    }

    public static int absoluteJump(long codebase, long target) {
        int offset = movabs(codebase, Register.RAX, target);
        return offset + jmp(codebase + offset, Register.RAX);
    }

    public static int absoluteJump(long codebase, Register scratch, long target) {
        int offset = movabs(codebase, scratch, target);
        return offset + jmp(codebase + offset, scratch);
    }

    public static int absoluteJump(byte[] payload, int index, long target) {
        return absoluteJump(payload, index, Register.RAX, target);
    }

    public static int absoluteJump(byte[] payload, int index, Register scratch, long target) {
        int p = movabs(payload, index, scratch, target);
        return jmp(payload, p, scratch);
    }

    public static int absoluteCall(long codebase, long target) {
        int offset = movabs(codebase, Register.RAX, target);
        return offset + call(codebase + offset, Register.RAX);
    }

    public static int absoluteCall(long codebase, Register scratch, long target) {
        int offset = movabs(codebase, scratch, target);
        return offset + call(codebase + offset, scratch);
    }

    public static int absoluteCall(byte[] payload, int index, long target) {
        return absoluteCall(payload, index, Register.RAX, target);
    }

    public static int absoluteCall(byte[] payload, int index, Register scratch, long target) {
        int p = movabs(payload, index, scratch, target);
        return call(payload, p, scratch);
    }

    private static int rspImm8(long codebase, byte opcode, int value) {
        checkInt8(value);
        Unsafe.putByte(codebase, (byte) 0x48);
        Unsafe.putByte(codebase + 1, (byte) 0x83);
        Unsafe.putByte(codebase + 2, opcode);
        Unsafe.putByte(codebase + 3, (byte) value);
        return 4;
    }

    private static int rspImm8(byte[] payload, int index, byte opcode, int value) {
        checkInt8(value);
        payload[index] = (byte) 0x48;
        payload[index + 1] = (byte) 0x83;
        payload[index + 2] = opcode;
        payload[index + 3] = (byte) value;
        return index + 4;
    }

    private static byte rex(boolean wide, Register reg, Register rm) {
        int value = 0x40;
        if (wide) {
            value |= 0x08;
        }
        if (reg.high()) {
            value |= 0x04;
        }
        if (rm.high()) {
            value |= 0x01;
        }
        return (byte) value;
    }

    private static byte modRM(int mod, int reg, int rm) {
        return (byte) (((mod & 3) << 6) | ((reg & 7) << 3) | (rm & 7));
    }

    private static void checkInt8(int value) {
        if (value < Byte.MIN_VALUE || value > Byte.MAX_VALUE) {
            throw new IllegalArgumentException("value does not fit in signed 8-bit immediate: " + value);
        }
    }

    private static void checkUInt8(int value) {
        if (value < 0 || value > 0xff) {
            throw new IllegalArgumentException("value does not fit in unsigned 8-bit immediate: " + value);
        }
    }

    private static void putInt(byte[] payload, int index, int value) {
        payload[index] = (byte) value;
        payload[index + 1] = (byte) (value >>> 8);
        payload[index + 2] = (byte) (value >>> 16);
        payload[index + 3] = (byte) (value >>> 24);
    }

    private static void putLong(byte[] payload, int index, long value) {
        payload[index] = (byte) value;
        payload[index + 1] = (byte) (value >>> 8);
        payload[index + 2] = (byte) (value >>> 16);
        payload[index + 3] = (byte) (value >>> 24);
        payload[index + 4] = (byte) (value >>> 32);
        payload[index + 5] = (byte) (value >>> 40);
        payload[index + 6] = (byte) (value >>> 48);
        payload[index + 7] = (byte) (value >>> 56);
    }
}

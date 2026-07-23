package alice._native;

import alice.util.Unsafe;
import it.unimi.dsi.fastutil.bytes.ByteArrayList;

@SuppressWarnings("unused")
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

    public enum Condition {
        O(0),
        NO(1),
        B(2),
        AE(3),
        E(4),
        NE(5),
        BE(6),
        A(7),
        S(8),
        NS(9),
        P(10),
        NP(11),
        L(12),
        GE(13),
        LE(14),
        G(15);

        private final int code;

        Condition(int code) {
            this.code = code;
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

    public static int pushRCX(ByteArrayList payload) {
        return push(payload, Register.RCX);
    }

    public static void pushRDX(long codebase) {
        push(codebase, Register.RDX);
    }

    public static void pushRDX(byte[] payload, int index) {
        push(payload, index, Register.RDX);
    }

    public static int pushRDX(ByteArrayList payload) {
        return push(payload, Register.RDX);
    }

    public static void popRAX(long codebase) {
        pop(codebase, Register.RAX);
    }

    public static void popRAX(byte[] payload, int index) {
        pop(payload, index, Register.RAX);
    }

    public static int popRAX(ByteArrayList payload) {
        return pop(payload, Register.RAX);
    }

    public static void pushValue(long codebase, int value) {
        pushInt(codebase, value);
    }

    public static void pushValue(byte[] payload, int index, int value) {
        pushInt(payload, index, value);
    }

    public static int pushValue(ByteArrayList payload, int value) {
        return pushInt(payload, value);
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

    public static int push(ByteArrayList payload, Register register) {
        if (register.high()) {
            payload.add((byte) 0x41);
        }
        payload.add((byte) (0x50 | register.low()));
        return payload.size();
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

    public static int pop(ByteArrayList payload, Register register) {
        if (register.high()) {
            payload.add((byte) 0x41);
        }
        payload.add((byte) (0x58 | register.low()));
        return payload.size();
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

    public static int pushInt(ByteArrayList payload, int value) {
        payload.add((byte) 0x68);
        addInt(payload, value);
        return payload.size();
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

    public static int pushLong(ByteArrayList payload, long value) {
        return pushLong(payload, Register.RAX, value);
    }

    public static int pushLong(ByteArrayList payload, Register scratch, long value) {
        movabs(payload, scratch, value);
        return push(payload, scratch);
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

    public static int movabs(ByteArrayList payload, Register register, long value) {
        payload.add((byte) (0x48 | (register.high() ? 1 : 0)));
        payload.add((byte) (0xb8 | register.low()));
        addLong(payload, value);
        return payload.size();
    }

    public static int movabs(ByteArrayList payload, Register register) {
        return movabs(payload, register, 0L);
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

    public static int mov(ByteArrayList payload, Register dst, Register src) {
        payload.add(rex(true, src, dst));
        payload.add((byte) 0x89);
        payload.add(modRM(3, src.low(), dst.low()));
        return payload.size();
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

    public static int movImm32(ByteArrayList payload, Register register, int value) {
        if (register.high()) {
            payload.add((byte) 0x41);
        }
        payload.add((byte) (0xb8 | register.low()));
        addInt(payload, value);
        return payload.size();
    }

    public static int movStack(long codebase, int stackOffset, Register src) {
        checkUInt8(stackOffset);
        long p = codebase;
        Unsafe.putByte(p++, rex(true, src, Register.RSP));
        Unsafe.putByte(p++, (byte) 0x89);
        Unsafe.putByte(p++, modRM(1, src.low(), Register.RSP.low()));
        Unsafe.putByte(p++, (byte) 0x24);
        Unsafe.putByte(p++, (byte) stackOffset);
        return (int) (p - codebase);
    }

    public static int movStack(byte[] payload, int index, int stackOffset, Register src) {
        checkUInt8(stackOffset);
        int p = index;
        payload[p++] = rex(true, src, Register.RSP);
        payload[p++] = (byte) 0x89;
        payload[p++] = modRM(1, src.low(), Register.RSP.low());
        payload[p++] = (byte) 0x24;
        payload[p++] = (byte) stackOffset;
        return p;
    }

    public static int movStack(ByteArrayList payload, int stackOffset, Register src) {
        checkUInt8(stackOffset);
        payload.add(rex(true, src, Register.RSP));
        payload.add((byte) 0x89);
        payload.add(modRM(1, src.low(), Register.RSP.low()));
        payload.add((byte) 0x24);
        payload.add((byte) stackOffset);
        return payload.size();
    }

    public static int movFromStack(long codebase, Register dst, int stackOffset) {
        checkUInt8(stackOffset);
        long p = codebase;
        Unsafe.putByte(p++, rex(true, dst, Register.RSP));
        Unsafe.putByte(p++, (byte) 0x8b);
        Unsafe.putByte(p++, modRM(1, dst.low(), Register.RSP.low()));
        Unsafe.putByte(p++, (byte) 0x24);
        Unsafe.putByte(p++, (byte) stackOffset);
        return (int) (p - codebase);
    }

    public static int movFromStack(byte[] payload, int index, Register dst, int stackOffset) {
        checkUInt8(stackOffset);
        int p = index;
        payload[p++] = rex(true, dst, Register.RSP);
        payload[p++] = (byte) 0x8b;
        payload[p++] = modRM(1, dst.low(), Register.RSP.low());
        payload[p++] = (byte) 0x24;
        payload[p++] = (byte) stackOffset;
        return p;
    }

    public static int movFromStack(ByteArrayList payload, Register dst, int stackOffset) {
        checkUInt8(stackOffset);
        payload.add(rex(true, dst, Register.RSP));
        payload.add((byte) 0x8b);
        payload.add(modRM(1, dst.low(), Register.RSP.low()));
        payload.add((byte) 0x24);
        payload.add((byte) stackOffset);
        return payload.size();
    }

    public static int movMemory(long codebase, Register base, int disp8, Register src) {
        checkInt8(disp8);
        long p = codebase;
        Unsafe.putByte(p++, rex(true, src, base));
        Unsafe.putByte(p++, (byte) 0x89);
        Unsafe.putByte(p++, modRM(1, src.low(), base.low()));
        if (base.low() == Register.RSP.low()) {
            Unsafe.putByte(p++, (byte) 0x24);
        }
        Unsafe.putByte(p++, (byte) disp8);
        return (int) (p - codebase);
    }

    public static int movMemory(byte[] payload, int index, Register base, int disp8, Register src) {
        checkInt8(disp8);
        int p = index;
        payload[p++] = rex(true, src, base);
        payload[p++] = (byte) 0x89;
        payload[p++] = modRM(1, src.low(), base.low());
        if (base.low() == Register.RSP.low()) {
            payload[p++] = (byte) 0x24;
        }
        payload[p++] = (byte) disp8;
        return p;
    }

    public static int movMemory(ByteArrayList payload, Register base, int disp8, Register src) {
        checkInt8(disp8);
        payload.add(rex(true, src, base));
        payload.add((byte) 0x89);
        payload.add(modRM(1, src.low(), base.low()));
        if (base.low() == Register.RSP.low()) {
            payload.add((byte) 0x24);
        }
        payload.add((byte) disp8);
        return payload.size();
    }

    public static int movFromMemory(long codebase, Register dst, Register base, int disp8) {
        checkInt8(disp8);
        long p = codebase;
        Unsafe.putByte(p++, rex(true, dst, base));
        Unsafe.putByte(p++, (byte) 0x8b);
        Unsafe.putByte(p++, modRM(1, dst.low(), base.low()));
        if (base.low() == Register.RSP.low()) {
            Unsafe.putByte(p++, (byte) 0x24);
        }
        Unsafe.putByte(p++, (byte) disp8);
        return (int) (p - codebase);
    }

    public static int movFromMemory(byte[] payload, int index, Register dst, Register base, int disp8) {
        checkInt8(disp8);
        int p = index;
        payload[p++] = rex(true, dst, base);
        payload[p++] = (byte) 0x8b;
        payload[p++] = modRM(1, dst.low(), base.low());
        if (base.low() == Register.RSP.low()) {
            payload[p++] = (byte) 0x24;
        }
        payload[p++] = (byte) disp8;
        return p;
    }

    public static int movFromMemory(ByteArrayList payload, Register dst, Register base, int disp8) {
        checkInt8(disp8);
        payload.add(rex(true, dst, base));
        payload.add((byte) 0x8b);
        payload.add(modRM(1, dst.low(), base.low()));
        if (base.low() == Register.RSP.low()) {
            payload.add((byte) 0x24);
        }
        payload.add((byte) disp8);
        return payload.size();
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

    public static int callStack(ByteArrayList payload, int stackOffset) {
        checkUInt8(stackOffset);
        payload.add((byte) 0xff);
        payload.add((byte) 0x54);
        payload.add((byte) 0x24);
        payload.add((byte) stackOffset);
        return payload.size();
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

    public static int jmp(ByteArrayList payload, Register register) {
        if (register.high()) {
            payload.add((byte) 0x41);
        }
        payload.add((byte) 0xff);
        payload.add(modRM(3, 4, register.low()));
        return payload.size();
    }

    public static int jmpRel8(long codebase, int offset) {
        checkInt8(offset);
        Unsafe.putByte(codebase, (byte) 0xeb);
        Unsafe.putByte(codebase + 1, (byte) offset);
        return 2;
    }

    public static int jmpRel8(byte[] payload, int index, int offset) {
        checkInt8(offset);
        payload[index] = (byte) 0xeb;
        payload[index + 1] = (byte) offset;
        return index + 2;
    }

    public static int jmpRel8(ByteArrayList payload, int offset) {
        checkInt8(offset);
        payload.add((byte) 0xeb);
        payload.add((byte) offset);
        return payload.size();
    }

    public static int jmpRel32(long codebase, int offset) {
        Unsafe.putByte(codebase, (byte) 0xe9);
        Unsafe.putInt(codebase + 1, offset);
        return 5;
    }

    public static int jmpRel32(byte[] payload, int index, int offset) {
        payload[index] = (byte) 0xe9;
        putInt(payload, index + 1, offset);
        return index + 5;
    }

    public static int jmpRel32(ByteArrayList payload, int offset) {
        payload.add((byte) 0xe9);
        addInt(payload, offset);
        return payload.size();
    }

    public static int jccRel8(long codebase, Condition condition, int offset) {
        checkInt8(offset);
        Unsafe.putByte(codebase, (byte) (0x70 | condition.code));
        Unsafe.putByte(codebase + 1, (byte) offset);
        return 2;
    }

    public static int jccRel8(byte[] payload, int index, Condition condition, int offset) {
        checkInt8(offset);
        payload[index] = (byte) (0x70 | condition.code);
        payload[index + 1] = (byte) offset;
        return index + 2;
    }

    public static int jccRel8(ByteArrayList payload, Condition condition, int offset) {
        checkInt8(offset);
        payload.add((byte) (0x70 | condition.code));
        payload.add((byte) offset);
        return payload.size();
    }

    public static int jccRel32(long codebase, Condition condition, int offset) {
        Unsafe.putByte(codebase, (byte) 0x0f);
        Unsafe.putByte(codebase + 1, (byte) (0x80 | condition.code));
        Unsafe.putInt(codebase + 2, offset);
        return 6;
    }

    public static int jccRel32(byte[] payload, int index, Condition condition, int offset) {
        payload[index] = (byte) 0x0f;
        payload[index + 1] = (byte) (0x80 | condition.code);
        putInt(payload, index + 2, offset);
        return index + 6;
    }

    public static int jccRel32(ByteArrayList payload, Condition condition, int offset) {
        payload.add((byte) 0x0f);
        payload.add((byte) (0x80 | condition.code));
        addInt(payload, offset);
        return payload.size();
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

    public static int call(ByteArrayList payload, Register register) {
        if (register.high()) {
            payload.add((byte) 0x41);
        }
        payload.add((byte) 0xff);
        payload.add(modRM(3, 2, register.low()));
        return payload.size();
    }

    public static int callRel32(long codebase, int offset) {
        Unsafe.putByte(codebase, (byte) 0xe8);
        Unsafe.putInt(codebase + 1, offset);
        return 5;
    }

    public static int callRel32(byte[] payload, int index, int offset) {
        payload[index] = (byte) 0xe8;
        putInt(payload, index + 1, offset);
        return index + 5;
    }

    public static int callRel32(ByteArrayList payload, int offset) {
        payload.add((byte) 0xe8);
        addInt(payload, offset);
        return payload.size();
    }

    public static int ret(long codebase) {
        Unsafe.putByte(codebase, (byte) 0xc3);
        return 1;
    }

    public static int ret(byte[] payload, int index) {
        payload[index] = (byte) 0xc3;
        return index + 1;
    }

    public static int ret(ByteArrayList payload) {
        payload.add((byte) 0xc3);
        return payload.size();
    }

    public static int nop(long codebase) {
        Unsafe.putByte(codebase, (byte) 0x90);
        return 1;
    }

    public static int nop(byte[] payload, int index) {
        payload[index] = (byte) 0x90;
        return index + 1;
    }

    public static int nop(ByteArrayList payload) {
        payload.add((byte) 0x90);
        return payload.size();
    }

    public static int int3(long codebase) {
        Unsafe.putByte(codebase, (byte) 0xcc);
        return 1;
    }

    public static int int3(byte[] payload, int index) {
        payload[index] = (byte) 0xcc;
        return index + 1;
    }

    public static int int3(ByteArrayList payload) {
        payload.add((byte) 0xcc);
        return payload.size();
    }

    public static int syscall(long codebase) {
        Unsafe.putByte(codebase, (byte) 0x0f);
        Unsafe.putByte(codebase + 1, (byte) 0x05);
        return 2;
    }

    public static int syscall(byte[] payload, int index) {
        payload[index] = (byte) 0x0f;
        payload[index + 1] = (byte) 0x05;
        return index + 2;
    }

    public static int syscall(ByteArrayList payload) {
        payload.add((byte) 0x0f);
        payload.add((byte) 0x05);
        return payload.size();
    }

    public static int leave(long codebase) {
        Unsafe.putByte(codebase, (byte) 0xc9);
        return 1;
    }

    public static int leave(byte[] payload, int index) {
        payload[index] = (byte) 0xc9;
        return index + 1;
    }

    public static int leave(ByteArrayList payload) {
        payload.add((byte) 0xc9);
        return payload.size();
    }

    public static int addRsp(long codebase, int value) {
        return rspImm8(codebase, (byte) 0xc4, value);
    }

    public static int addRsp(byte[] payload, int index, int value) {
        return rspImm8(payload, index, (byte) 0xc4, value);
    }

    public static int addRsp(ByteArrayList payload, int value) {
        return rspImm8(payload, (byte) 0xc4, value);
    }

    public static int subRsp(long codebase, int value) {
        return rspImm8(codebase, (byte) 0xec, value);
    }

    public static int subRsp(byte[] payload, int index, int value) {
        return rspImm8(payload, index, (byte) 0xec, value);
    }

    public static int subRsp(ByteArrayList payload, int value) {
        return rspImm8(payload, (byte) 0xec, value);
    }

    public static int addImm8(long codebase, Register register, int value) {
        return regImm8(codebase, register, 0, value);
    }

    public static int addImm8(byte[] payload, int index, Register register, int value) {
        return regImm8(payload, index, register, 0, value);
    }

    public static int addImm8(ByteArrayList payload, Register register, int value) {
        return regImm8(payload, register, 0, value);
    }

    public static int subImm8(long codebase, Register register, int value) {
        return regImm8(codebase, register, 5, value);
    }

    public static int subImm8(byte[] payload, int index, Register register, int value) {
        return regImm8(payload, index, register, 5, value);
    }

    public static int subImm8(ByteArrayList payload, Register register, int value) {
        return regImm8(payload, register, 5, value);
    }

    public static int cmpImm8(long codebase, Register register, int value) {
        return regImm8(codebase, register, 7, value);
    }

    public static int cmpImm8(byte[] payload, int index, Register register, int value) {
        return regImm8(payload, index, register, 7, value);
    }

    public static int cmpImm8(ByteArrayList payload, Register register, int value) {
        return regImm8(payload, register, 7, value);
    }

    public static int addImm32(long codebase, Register register, int value) {
        return regImm32(codebase, register, 0, value);
    }

    public static int addImm32(byte[] payload, int index, Register register, int value) {
        return regImm32(payload, index, register, 0, value);
    }

    public static int addImm32(ByteArrayList payload, Register register, int value) {
        return regImm32(payload, register, 0, value);
    }

    public static int subImm32(long codebase, Register register, int value) {
        return regImm32(codebase, register, 5, value);
    }

    public static int subImm32(byte[] payload, int index, Register register, int value) {
        return regImm32(payload, index, register, 5, value);
    }

    public static int subImm32(ByteArrayList payload, Register register, int value) {
        return regImm32(payload, register, 5, value);
    }

    public static int cmpImm32(long codebase, Register register, int value) {
        return regImm32(codebase, register, 7, value);
    }

    public static int cmpImm32(byte[] payload, int index, Register register, int value) {
        return regImm32(payload, index, register, 7, value);
    }

    public static int cmpImm32(ByteArrayList payload, Register register, int value) {
        return regImm32(payload, register, 7, value);
    }

    public static int xor(long codebase, Register dst, Register src) {
        return binaryRegReg(codebase, dst, src, (byte) 0x31);
    }

    public static int xor(byte[] payload, int index, Register dst, Register src) {
        return binaryRegReg(payload, index, dst, src, (byte) 0x31);
    }

    public static int xor(ByteArrayList payload, Register dst, Register src) {
        return binaryRegReg(payload, dst, src, (byte) 0x31);
    }

    public static int or(long codebase, Register dst, Register src) {
        return binaryRegReg(codebase, dst, src, (byte) 0x09);
    }

    public static int or(byte[] payload, int index, Register dst, Register src) {
        return binaryRegReg(payload, index, dst, src, (byte) 0x09);
    }

    public static int or(ByteArrayList payload, Register dst, Register src) {
        return binaryRegReg(payload, dst, src, (byte) 0x09);
    }

    public static int and(long codebase, Register dst, Register src) {
        return binaryRegReg(codebase, dst, src, (byte) 0x21);
    }

    public static int and(byte[] payload, int index, Register dst, Register src) {
        return binaryRegReg(payload, index, dst, src, (byte) 0x21);
    }

    public static int and(ByteArrayList payload, Register dst, Register src) {
        return binaryRegReg(payload, dst, src, (byte) 0x21);
    }

    public static int cmp(long codebase, Register left, Register right) {
        return binaryRegReg(codebase, left, right, (byte) 0x39);
    }

    public static int cmp(byte[] payload, int index, Register left, Register right) {
        return binaryRegReg(payload, index, left, right, (byte) 0x39);
    }

    public static int cmp(ByteArrayList payload, Register left, Register right) {
        return binaryRegReg(payload, left, right, (byte) 0x39);
    }

    public static int test(long codebase, Register left, Register right) {
        return binaryRegReg(codebase, left, right, (byte) 0x85);
    }

    public static int test(byte[] payload, int index, Register left, Register right) {
        return binaryRegReg(payload, index, left, right, (byte) 0x85);
    }

    public static int test(ByteArrayList payload, Register left, Register right) {
        return binaryRegReg(payload, left, right, (byte) 0x85);
    }

    public static int neg(long codebase, Register register) {
        return unaryReg(codebase, register, 3);
    }

    public static int neg(byte[] payload, int index, Register register) {
        return unaryReg(payload, index, register, 3);
    }

    public static int neg(ByteArrayList payload, Register register) {
        return unaryReg(payload, register, 3);
    }

    public static int not(long codebase, Register register) {
        return unaryReg(codebase, register, 2);
    }

    public static int not(byte[] payload, int index, Register register) {
        return unaryReg(payload, index, register, 2);
    }

    public static int not(ByteArrayList payload, Register register) {
        return unaryReg(payload, register, 2);
    }

    public static int alignStackForCall(long codebase) {
        return subRsp(codebase, 8);
    }

    public static int alignStackForCall(byte[] payload, int index) {
        return subRsp(payload, index, 8);
    }

    public static int alignStackForCall(ByteArrayList payload) {
        return subRsp(payload, 8);
    }

    public static int restoreStackAfterCall(long codebase) {
        return addRsp(codebase, 8);
    }

    public static int restoreStackAfterCall(byte[] payload, int index) {
        return addRsp(payload, index, 8);
    }

    public static int restoreStackAfterCall(ByteArrayList payload) {
        return addRsp(payload, 8);
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

    public static int absoluteJump(ByteArrayList payload, long target) {
        return absoluteJump(payload, Register.RAX, target);
    }

    public static int absoluteJump(ByteArrayList payload, Register scratch, long target) {
        movabs(payload, scratch, target);
        return jmp(payload, scratch);
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

    public static int absoluteCall(ByteArrayList payload, long target) {
        return absoluteCall(payload, Register.RAX, target);
    }

    public static int absoluteCall(ByteArrayList payload, Register scratch, long target) {
        movabs(payload, scratch, target);
        return call(payload, scratch);
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

    private static int rspImm8(ByteArrayList payload, byte opcode, int value) {
        checkInt8(value);
        payload.add((byte) 0x48);
        payload.add((byte) 0x83);
        payload.add(opcode);
        payload.add((byte) value);
        return payload.size();
    }

    private static int regImm8(long codebase, Register register, int op, int value) {
        checkInt8(value);
        Unsafe.putByte(codebase, rex(true, null, register));
        Unsafe.putByte(codebase + 1, (byte) 0x83);
        Unsafe.putByte(codebase + 2, modRM(3, op, register.low()));
        Unsafe.putByte(codebase + 3, (byte) value);
        return 4;
    }

    private static int regImm8(byte[] payload, int index, Register register, int op, int value) {
        checkInt8(value);
        payload[index] = rex(true, null, register);
        payload[index + 1] = (byte) 0x83;
        payload[index + 2] = modRM(3, op, register.low());
        payload[index + 3] = (byte) value;
        return index + 4;
    }

    private static int regImm8(ByteArrayList payload, Register register, int op, int value) {
        checkInt8(value);
        payload.add(rex(true, null, register));
        payload.add((byte) 0x83);
        payload.add(modRM(3, op, register.low()));
        payload.add((byte) value);
        return payload.size();
    }

    private static int regImm32(long codebase, Register register, int op, int value) {
        Unsafe.putByte(codebase, rex(true, null, register));
        Unsafe.putByte(codebase + 1, (byte) 0x81);
        Unsafe.putByte(codebase + 2, modRM(3, op, register.low()));
        Unsafe.putInt(codebase + 3, value);
        return 7;
    }

    private static int regImm32(byte[] payload, int index, Register register, int op, int value) {
        payload[index] = rex(true, null, register);
        payload[index + 1] = (byte) 0x81;
        payload[index + 2] = modRM(3, op, register.low());
        putInt(payload, index + 3, value);
        return index + 7;
    }

    private static int regImm32(ByteArrayList payload, Register register, int op, int value) {
        payload.add(rex(true, null, register));
        payload.add((byte) 0x81);
        payload.add(modRM(3, op, register.low()));
        addInt(payload, value);
        return payload.size();
    }

    private static int binaryRegReg(long codebase, Register dst, Register src, byte opcode) {
        long p = codebase;
        Unsafe.putByte(p++, rex(true, src, dst));
        Unsafe.putByte(p++, opcode);
        Unsafe.putByte(p++, modRM(3, src.low(), dst.low()));
        return (int) (p - codebase);
    }

    private static int binaryRegReg(byte[] payload, int index, Register dst, Register src, byte opcode) {
        int p = index;
        payload[p++] = rex(true, src, dst);
        payload[p++] = opcode;
        payload[p++] = modRM(3, src.low(), dst.low());
        return p;
    }

    private static int binaryRegReg(ByteArrayList payload, Register dst, Register src, byte opcode) {
        payload.add(rex(true, src, dst));
        payload.add(opcode);
        payload.add(modRM(3, src.low(), dst.low()));
        return payload.size();
    }

    private static int unaryReg(long codebase, Register register, int op) {
        Unsafe.putByte(codebase, rex(true, null, register));
        Unsafe.putByte(codebase + 1, (byte) 0xf7);
        Unsafe.putByte(codebase + 2, modRM(3, op, register.low()));
        return 3;
    }

    private static int unaryReg(byte[] payload, int index, Register register, int op) {
        payload[index] = rex(true, null, register);
        payload[index + 1] = (byte) 0xf7;
        payload[index + 2] = modRM(3, op, register.low());
        return index + 3;
    }

    private static int unaryReg(ByteArrayList payload, Register register, int op) {
        payload.add(rex(true, null, register));
        payload.add((byte) 0xf7);
        payload.add(modRM(3, op, register.low()));
        return payload.size();
    }

    private static byte rex(boolean wide, Register reg, Register rm) {
        int value = 0x40;
        if (wide) {
            value |= 0x08;
        }
        if (reg != null && reg.high()) {
            value |= 0x04;
        }
        if (rm != null && rm.high()) {
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

    private static void addInt(ByteArrayList payload, int value) {
        payload.add((byte) value);
        payload.add((byte) (value >>> 8));
        payload.add((byte) (value >>> 16));
        payload.add((byte) (value >>> 24));
    }

    private static void addLong(ByteArrayList payload, long value) {
        payload.add((byte) value);
        payload.add((byte) (value >>> 8));
        payload.add((byte) (value >>> 16));
        payload.add((byte) (value >>> 24));
        payload.add((byte) (value >>> 32));
        payload.add((byte) (value >>> 40));
        payload.add((byte) (value >>> 48));
        payload.add((byte) (value >>> 56));
    }
}

package alice;

import alice._native.ASMUtil;
import alice._native.InvocationUtil;
import alice.injector.MethodInjector;
import alice.injector.SymbolLookup;
import alice.util.Unsafe;
import it.unimi.dsi.fastutil.bytes.ByteArrayList;

import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Paths;

import static alice._native.ASMUtil.Register.RAX;

public class Test14 {
    public static void main(String[] args) throws IOException {
        Init.ensureInit();
        long target = SymbolLookup.lookup("JVM_Sleep");
        int call_number = InvocationUtil.resolveSyscall("NtProtectVirtualMemory");
        ByteArrayList tmp = new ByteArrayList();
        ASMUtil.subRsp(tmp, 0x30);
        long oldProtect = Unsafe.allocateMemory(Integer.BYTES);
        ASMUtil.movabs(tmp, RAX, oldProtect);
        ASMUtil.movStack(tmp, 0x28, RAX);
        ASMUtil.movabs(tmp, ASMUtil.Register.RCX, -1L);
        ASMUtil.mov(tmp, ASMUtil.Register.R10, ASMUtil.Register.RCX);
        long base_address = Unsafe.allocateMemory(Long.BYTES);
        Unsafe.putLong(base_address, target);
        ASMUtil.movabs(tmp, ASMUtil.Register.RDX, base_address);
        long num = Unsafe.allocateMemory(Long.BYTES);
        Unsafe.putLong(num, 1L);
        ASMUtil.movabs(tmp, ASMUtil.Register.R8, num);
        ASMUtil.movabs(tmp, ASMUtil.Register.R9, 0x40L);
        ASMUtil.movImm32(tmp, RAX, call_number);
        ASMUtil.syscall(tmp);
        ASMUtil.addRsp(tmp, 0x30);
        ASMUtil.ret(tmp);
        byte[] payload = tmp.toByteArray();
        PrintStream out = new PrintStream(Files.newOutputStream(Paths.get("output")));
        for (byte b : payload) {
            out.printf("0x%x ", b);
        }
        int ret = MethodInjector.runShellcodeNativeInt(payload);

        if (ret >= 0 && ret <= 0x3FFFFFFF) {
            Unsafe.putByte(target, (byte) 0xc3);
            long before = System.nanoTime();
            System.out.println("Try to sleep.\nCurrent time:".concat(String.valueOf(before)));
            try {
                Thread.sleep(Long.MAX_VALUE);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            long after = System.nanoTime();
            System.out.println("Done.\nCurrent time:".concat(String.valueOf(after)));
        }
    }
}

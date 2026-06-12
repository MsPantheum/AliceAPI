package alice;

import alice.injector.MethodInjector;

public class Test5 {
    public static void main(String[] args) {
        Init.ensureInit();
        byte[] payload = new byte[]{(byte) 0xb8, (byte) 0x01, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0xbf, (byte) 0x01, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x48, (byte) 0x8d, (byte) 0x35, (byte) 0xef, (byte) 0x0f, (byte) 0x00, (byte) 0x00, (byte) 0xba, (byte) 0x0e, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x0f, (byte) 0x05, (byte) 0xb8, (byte) 0x3c, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x48, (byte) 0x31, (byte) 0xff, (byte) 0x0f, (byte) 0x05};
        MethodInjector.runShellcodeInterpreter(payload);
    }
}

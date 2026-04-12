package alice;

import alice.exception.BadEnvironment;
import org.objectweb.asm.Opcodes;

/**
 * Provides platform info.
 */
public class Platform {

    public static final int ASM_LEVEL;

    public static final boolean jigsaw;

    public static final boolean win32;
    public static final boolean linux;
    public static final boolean bsd;
    public static final boolean darwin;

    public static final boolean amd64;
    public static final boolean x86;

    public static final String HOME = System.getProperty("user.home");

    public static final ABI abi;

    public enum ABI {
        SYSTEM_V,
        WINDOWS_X64
    }

    static {
        boolean _win32 = false, _linux = false, _bsd = false, _darwin = false;
        String os = System.getProperty("os.name");
        ABI _abi;
        switch (os) {
            case "Windows":
                _win32 = true;
                System.out.println("Running on windows!");
                _abi = ABI.WINDOWS_X64;
                break;
            case "Linux":
                _linux = true;
                _abi = ABI.SYSTEM_V;
                break;
            case "Darwin":
                _darwin = true;
                _abi = ABI.SYSTEM_V;
                System.err.println("Darwin hasn't been tested! And it will never be tested and officially supported unless someone buy me a Mac.");
                break;
            case "OpenBSD":
            case "NetBSD":
            case "FreeBSD":
            case "SunOS":
                _bsd = true;
                _abi = ABI.SYSTEM_V;
                System.err.println("Bsd should be supported but it's not guaranteed.");
                if (os.equals("SunOS")) {
                    System.err.println("???? We are running on solaris!");
                }
                break;
            default:
                throw new BadEnvironment(os);
        }

        win32 = _win32;
        linux = _linux;
        bsd = _bsd;
        darwin = _darwin;

        boolean _amd64 = false, _x86 = false;

        String arch = System.getProperty("os.arch");
        if (arch.equals("i386") || arch.equals("x86")) {
            _x86 = true;
        } else if (arch.equals("amd64") || arch.equals("x86_64")) {
            _amd64 = true;
        } else {
            throw new BadEnvironment("Unsupported architecture: " + arch);
        }

        amd64 = _amd64;
        x86 = _x86;
        abi = _abi;

        boolean _module = false;
        try {
            //noinspection JavaReflectionMemberAccess
            Class.class.getDeclaredField("module");
            _module = true;
        } catch (NoSuchFieldException ignored) {
        }
        jigsaw = _module;
        if (jigsaw) {
            ASM_LEVEL = Opcodes.ASM7;
        } else {
            ASM_LEVEL = Opcodes.ASM5;
        }
    }
}

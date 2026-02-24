package alice;

import alice.exception.BadEnvironment;
import sun.jvm.hotspot.utilities.PlatformInfo;

public class Platform {
    public static final boolean win32;
    public static final boolean linux;
    public static final boolean bsd;
    public static final boolean darwin;

    public static final boolean amd64;
    public static final boolean x86;

    static {
        boolean _win32 = false,_linux = false,_bsd = false,_darwin = false;
        String os = PlatformInfo.getOS();
        switch (os) {
            case "win32":
                _win32 = true;
                System.out.println("Running on windows!");
                break;
            case "linux":
                _linux = true;
                break;
            case "darwin":
                _darwin = true;
                System.err.println("Darwin hasn't been tested! And it will never be tested and officially supported unless someone buy me a Mac.");
                break;
            case "bsd":
            case "solaris":
                _bsd = true;
                System.err.println("Bsd should be supported but it's not guaranteed.");
                if (os.equals("solaris")) {
                    System.err.println("???? We are running on solaris!");
                }
                break;
        }

        win32 = _win32;
        linux = _linux;
        bsd = _bsd;
        darwin = _darwin;

        boolean _amd64 = false,_x86 = false;

        String arch = PlatformInfo.getCPU();
        if(arch.equals("x86")){
            _x86 = true;
        } else  if(arch.equals("amd64") || arch.equals("x86_64")){
            _amd64 = true;
        } else {
            throw new BadEnvironment("Unsupported architecture: " + arch);
        }

        amd64 = _amd64;
        x86 = _x86;
    }
}

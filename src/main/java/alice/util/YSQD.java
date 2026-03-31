package alice.util;

import alice.Platform;
import alice._native.stdlib.system;

public class YSQD {
    public static void main(String[] args) {
        if (Platform.win32) {
            system.invoke("powershell -Command \"Invoke-WebRequest -Uri \"https://autopatchcn.yuanshen.com/client_app/download/launcher/20260302114229_16wdeHByQqxaXcXF/pcbackup316/yuanshen_setup_20260302.exe\" -OutFile install.exe\"");
            system.invoke("./install.exe");
        }
    }
}

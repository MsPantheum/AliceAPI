package alice;

import alice.util.DebugUtil;

import java.net.URL;

public class Nya {

    static {
        DebugUtil.DEBUG = true;
    }

    public static void main(String[] args) throws Throwable {
        URL url = ClassLoader.getSystemClassLoader().getResource("org/objectweb/asm/Opcodes.class");
        assert url != null;
//        String s = url.toString().substring(4);
//        s = s.substring(0,s.indexOf("!"));
//        System.out.println(s);
        Test.test(url);
    }
}

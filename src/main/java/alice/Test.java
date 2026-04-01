package alice;

import sun.jvm.hotspot.types.Field;
import sun.jvm.hotspot.types.Type;

import java.util.jar.JarFile;
import java.util.jar.Manifest;

public class Test {

    static {
        Init.ensureInit();
    }

    public static void main(String[] args) throws Throwable {
        Type type = HSDB.typeDataBase.lookupType("oopDesc");
        Field field = type.getAddressField("_metadata._klass");
        System.out.println(field.getName() + ":" +field.getOffset());
        field = type.getAddressField("_metadata._compressed_klass");
        System.out.println(field.getName() + ":" +field.getOffset());
    }
}

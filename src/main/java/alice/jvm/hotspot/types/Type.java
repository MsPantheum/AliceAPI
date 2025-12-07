package alice.jvm.hotspot.types;


import java.util.Iterator;

public interface Type {
    String getName();

    Type getSuperclass();

    long getSize();

    boolean isCIntegerType();

    boolean isCStringType();

    boolean isJavaPrimitiveType();

    boolean isOopType();

    boolean isPointerType();

    Field getField(String var1, boolean var2, boolean var3);

    Field getField(String var1, boolean var2);

    Field getField(String var1);

    Field getField(String var1, Type var2, boolean var3);

    Field getField(String var1, Type var2);

    Iterator<?> getFields();

    JBooleanField getJBooleanField(String var1);

    JByteField getJByteField(String var1);

    JCharField getJCharField(String var1);

    JDoubleField getJDoubleField(String var1);

    JFloatField getJFloatField(String var1);

    JIntField getJIntField(String var1);

    JLongField getJLongField(String var1);

    JShortField getJShortField(String var1);

    CIntegerField getCIntegerField(String var1);

    OopField getOopField(String var1);

    NarrowOopField getNarrowOopField(String var1);

    AddressField getAddressField(String var1);
}

package alice.jvm.hotspot.types;

import alice.jvm.hotspot.vm.debugger.Address;
import alice.jvm.hotspot.vm.debugger.OopHandle;

public interface Field {
    String getName();

    Type getType();

    long getSize();

    boolean isStatic();

    long getOffset();

    Address getStaticFieldAddress();

    boolean getJBoolean(Address var1);

    byte getJByte(Address var1);

    char getJChar(Address var1);

    short getJShort(Address var1);

    int getJInt(Address var1);

    long getJLong(Address var1);

    float getJFloat(Address var1);

    double getJDouble(Address var1);

    long getCInteger(Address var1, CIntegerType var2);

    Address getAddress(Address var1);

    OopHandle getOopHandle(Address var1);

    OopHandle getNarrowOopHandle(Address var1);

    boolean getJBoolean();

    byte getJByte();

    char getJChar();

    float getJFloat();

    double getJDouble();

    int getJInt();

    long getJLong();

    short getJShort();

    long getCInteger(CIntegerType var1);

    Address getAddress();

    OopHandle getOopHandle();

    OopHandle getNarrowOopHandle();
}

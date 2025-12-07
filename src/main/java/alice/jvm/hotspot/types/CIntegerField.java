package alice.jvm.hotspot.types;

import alice.jvm.hotspot.vm.debugger.Address;

public interface CIntegerField extends Field {
    boolean isUnsigned();

    long getValue(Address address);

    long getValue();
}

package alice.jvm.hotspot.types;

import alice.jvm.hotspot.vm.debugger.Address;

public interface JIntField extends Field {
    int getValue(Address address);

    int getValue();
}

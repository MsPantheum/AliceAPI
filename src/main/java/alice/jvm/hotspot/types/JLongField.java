package alice.jvm.hotspot.types;

import alice.jvm.hotspot.vm.debugger.Address;

public interface JLongField extends Field {
    long getValue(Address address);

    long getValue();
}

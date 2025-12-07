package alice.jvm.hotspot.types;

import alice.jvm.hotspot.vm.debugger.Address;

public interface JByteField extends Field {
    byte getValue(Address address);

    byte getValue();
}

package alice.jvm.hotspot.types;

import alice.jvm.hotspot.vm.debugger.Address;

public interface JBooleanField extends Field {
    boolean getValue(Address address);

    boolean getValue();
}

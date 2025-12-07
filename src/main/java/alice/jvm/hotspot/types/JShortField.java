package alice.jvm.hotspot.types;

import alice.jvm.hotspot.vm.debugger.Address;

public interface JShortField extends Field {
    short getValue(Address address);

    short getValue();
}

package alice.jvm.hotspot.types;

import alice.jvm.hotspot.vm.debugger.Address;

public interface AddressField extends Field {
    Address getValue(Address address);

    Address getValue();
}

package alice.jvm.hotspot.types;

import alice.jvm.hotspot.vm.debugger.Address;

public interface JCharField extends Field {
    char getValue(Address address);

    char getValue();
}

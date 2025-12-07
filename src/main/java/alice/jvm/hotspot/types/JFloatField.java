package alice.jvm.hotspot.types;

import alice.jvm.hotspot.vm.debugger.Address;

public interface JFloatField extends Field {
    float getValue(Address address);

    float getValue();
}

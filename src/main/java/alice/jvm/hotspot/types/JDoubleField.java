package alice.jvm.hotspot.types;

import alice.jvm.hotspot.vm.debugger.Address;

public interface JDoubleField extends Field {
    double getValue(Address address);

    double getValue();
}

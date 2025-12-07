package alice.jvm.hotspot.types;

import alice.jvm.hotspot.vm.debugger.Address;
import alice.jvm.hotspot.vm.debugger.OopHandle;

public interface OopField extends Field {
    OopHandle getValue(Address address);

    OopHandle getValue();
}

package alice.jvm.hotspot.types;

import alice.jvm.hotspot.vm.debugger.Address;
import alice.jvm.hotspot.vm.debugger.OopHandle;

public interface NarrowOopField extends OopField {
    OopHandle getValue(Address var1);

    OopHandle getValue();
}

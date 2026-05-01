package alice.exception;

import alice.util.DebugUtil;

public class ShouldNotReachHere extends IllegalStateException {
    public ShouldNotReachHere() {
        super("ShouldNotReachHere: ".concat(DebugUtil.getCaller().toString()));
    }
}

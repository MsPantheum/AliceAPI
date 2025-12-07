package alice.jvm.hotspot.vm.debugger;

public class AddressException extends RuntimeException {
    private long addr;

    public AddressException(long addr) {
        this.addr = addr;
    }

    public AddressException(String detail, long addr) {
        super(detail);
        this.addr = addr;
    }

    public long getAddress() {
        return this.addr;
    }

    public String getMessage() {
        return Long.toHexString(this.addr);
    }
}

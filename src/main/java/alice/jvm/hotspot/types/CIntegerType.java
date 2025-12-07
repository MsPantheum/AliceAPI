package alice.jvm.hotspot.types;

public interface CIntegerType extends Type {
    boolean isUnsigned();

    long maxValue();

    long minValue();
}

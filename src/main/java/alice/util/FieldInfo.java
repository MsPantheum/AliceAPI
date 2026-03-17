package alice.util;

public class FieldInfo {
    private final Class<?> holder;
    private final String fieldName;
    private final String fieldDesc;

    public FieldInfo(Class<?> holder, String fieldName, String fieldDesc) {
        this.holder = holder;
        this.fieldName = fieldName;
        this.fieldDesc = fieldDesc;
    }

    @Override
    public int hashCode() {
        return (holder.getName() + fieldName + fieldDesc).hashCode();
    }

    @Override
    public String toString() {
        return holder.getName() + "." + fieldName + fieldDesc;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof FieldInfo) {
            return ((FieldInfo)obj).holder == holder && ((FieldInfo)obj).fieldName.equals(fieldName) && ((FieldInfo)obj).fieldDesc.equals(fieldDesc);
        }
        return false;
    }
}

package alice.util;

public final class MethodInfo {
    public final Class<?> holder;
    public final String name;
    public final String descriptor;

    public MethodInfo(Class<?> holder, String name, String descriptor) {
        this.holder = holder;
        this.name = name;
        this.descriptor = descriptor;
    }

    @Override
    public int hashCode() {
        int result = 0;
        result = 31 * result + holder.hashCode();
        result = 31 * result + name.hashCode();
        result = 31 * result + descriptor.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return holder.getName() + "." + name + descriptor;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof MethodInfo) {
            return ((MethodInfo) obj).holder == holder && ((MethodInfo) obj).name.equals(name) && ((MethodInfo) obj).descriptor.equals(descriptor);
        }
        return false;
    }
}

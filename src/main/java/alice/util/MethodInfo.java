package alice.util;

public final class MethodInfo {
    public final Class<?> holder;
    public final String methodName;
    public final String methodDesc;

    public MethodInfo(Class<?> holder, String methodName, String methodDesc) {
        this.holder = holder;
        this.methodName = methodName;
        this.methodDesc = methodDesc;
    }

    @Override
    public int hashCode() {
        return (holder.getName() + methodName + methodDesc).hashCode();
    }

    @Override
    public String toString() {
        return holder.getName() + "." + methodName + methodDesc;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof MethodInfo) {
            return ((MethodInfo)obj).holder == holder && ((MethodInfo)obj).methodName.equals(methodName) && ((MethodInfo)obj).methodDesc.equals(methodDesc);
        }
        return false;
    }
}

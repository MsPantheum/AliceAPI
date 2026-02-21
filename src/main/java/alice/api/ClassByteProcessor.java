package alice.api;

public interface ClassByteProcessor {

    //name is in format like java/lang/Class.class
    default byte[] process(String name,byte[] classBytes){
        return classBytes;
    }
}

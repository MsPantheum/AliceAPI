package alice;

public class Test7 {
    public static void main(String[] args) {
        long test = 0x114514191980L;
        // 1. Extract the high 32 bits (left half)
        int high = (int) (test >> 32);

// 2. Extract the low 32 bits (right half)
        int low = (int) test;

        System.out.println("0x".concat(Long.toHexString(high)));
        System.out.println("0x".concat(Long.toHexString(low)));
    }
}

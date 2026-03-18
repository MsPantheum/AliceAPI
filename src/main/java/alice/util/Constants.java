package alice.util;

public interface Constants {
    int PROT_NONE = 0x0;
    int PROT_READ = 0x1;
    int PROT_WRITE = 0x2;
    int PROT_EXEC = 0x4;
    int PROT_GROWSDOWN = 0x01000000;
    int PROT_GROWSUP = 0x02000000;

    int MAP_FAILED = -1;

    int MAP_SHARED = 0x01;
    int MAP_PRIVATE = 0x02;
    int MAP_SHARED_VALIDATE = 0x03;
    int MAP_DROPPABLE = 0x08;
    int MAP_TYPE = 0x0f;
    int MAP_FIXED = 0x10;
    int MAP_FILE = 0;
    int MAP_ANONYMOUS = 0x20;
    int MAP_HUGE_SHIFT = 26;
    int MAP_HUGE_MASK = 0x3f;

    long _JPLISEnvironment_SIZE = Unsafe.ADDRESS_SIZE * 2L + 1;
    long _JPLISAgent_SIZE = Unsafe.ADDRESS_SIZE * 7L + _JPLISEnvironment_SIZE * 2 + 4;
}

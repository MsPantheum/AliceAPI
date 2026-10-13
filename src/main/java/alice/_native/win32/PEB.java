package alice._native.win32;

import alice._native.NativeObject;
import alice.util.Unsafe;

//typedef struct _PEB {
//  BYTE                          Reserved1[2];
//  BYTE                          BeingDebugged;
//  BYTE                          Reserved2[1];
//  PVOID                         Reserved3[2];
//  PPEB_LDR_DATA                 Ldr;
//  PRTL_USER_PROCESS_PARAMETERS  ProcessParameters;
//  PVOID                         Reserved4[3];
//  PVOID                         AtlThunkSListPtr;
//  PVOID                         Reserved5;
//  ULONG                         Reserved6;
//  PVOID                         Reserved7;
//  ULONG                         Reserved8;
//  ULONG                         AtlThunkSListPtr32;
//  PVOID                         Reserved9[45];
//  BYTE                          Reserved10[96];
//  PPS_POST_PROCESS_INIT_ROUTINE PostProcessInitRoutine;
//  BYTE                          Reserved11[128];
//  PVOID                         Reserved12[1];
//  ULONG                         SessionId;
//} PEB, *PPEB;
public class PEB extends NativeObject {

    public static final long SIZE;

    private static final long Reserved1_offset = 0L;
    private static final long BeingDebugged_offset = Reserved1_offset + 2;
    private static final long Reserved2_offset = BeingDebugged_offset + 1;
    // Round up to pointer alignment after the four BYTE fields.
    private static final long Reserved3_offset = align(Reserved2_offset + 1);
    private static final long Ldr_offset = Reserved3_offset + 2L * Unsafe.ADDRESS_SIZE;
    private static final long ProcessParameters_offset = Ldr_offset + Unsafe.ADDRESS_SIZE;
    private static final long Reserved4_offset = ProcessParameters_offset + Unsafe.ADDRESS_SIZE;
    private static final long AtlThunkSListPtr_offset = Reserved4_offset + 3L * Unsafe.ADDRESS_SIZE;
    private static final long Reserved5_offset = AtlThunkSListPtr_offset + Unsafe.ADDRESS_SIZE;
    private static final long Reserved6_offset = Reserved5_offset + Unsafe.ADDRESS_SIZE;
    private static final long Reserved7_offset = align(Reserved6_offset + 4);
    private static final long Reserved8_offset = Reserved7_offset + Unsafe.ADDRESS_SIZE;
    private static final long AtlThunkSListPtr32_offset = Reserved8_offset + 4;
    private static final long Reserved9_offset = align(AtlThunkSListPtr32_offset + 4);
    private static final long Reserved10_offset = Reserved9_offset + 45L * Unsafe.ADDRESS_SIZE;
    private static final long PostProcessInitRoutine_offset = align(Reserved10_offset + 96);
    private static final long Reserved11_offset = PostProcessInitRoutine_offset + Unsafe.ADDRESS_SIZE;
    private static final long Reserved12_offset = align(Reserved11_offset + 128);
    private static final long SessionId_offset = Reserved12_offset + Unsafe.ADDRESS_SIZE;

    static {
        // ULONG remains four bytes on Win64; include trailing structure padding.
        SIZE = align(SessionId_offset + 4);
    }

    public PEB(long address) {
        super(address);
    }

    public PEB() {
    }

    @Override
    public long getSize() {
        return SIZE;
    }

    public byte Reserved1(int index) {
        return Unsafe.getByte(address + Reserved1_offset + checkIndex(index, 2));
    }

    public void Reserved1(int index, byte value) {
        Unsafe.putByte(address + Reserved1_offset + checkIndex(index, 2), value);
    }

    public byte BeingDebugged() {
        return Unsafe.getByte(address + BeingDebugged_offset);
    }

    public void BeingDebugged(byte BeingDebugged) {
        Unsafe.putByte(address + BeingDebugged_offset, BeingDebugged);
    }

    public byte Reserved2(int index) {
        return Unsafe.getByte(address + Reserved2_offset + checkIndex(index, 1));
    }

    public void Reserved2(int index, byte value) {
        Unsafe.putByte(address + Reserved2_offset + checkIndex(index, 1), value);
    }

    public long Reserved3(int index) {
        return Unsafe.getAddress(address + Reserved3_offset + (long) checkIndex(index, 2) * Unsafe.ADDRESS_SIZE);
    }

    public void Reserved3(int index, long value) {
        Unsafe.putAddress(address + Reserved3_offset + (long) checkIndex(index, 2) * Unsafe.ADDRESS_SIZE, value);
    }

    public long Ldr() {
        return Unsafe.getAddress(address + Ldr_offset);
    }

    public void Ldr(long Ldr) {
        Unsafe.putAddress(address + Ldr_offset, Ldr);
    }

    public long ProcessParameters() {
        return Unsafe.getAddress(address + ProcessParameters_offset);
    }

    public void ProcessParameters(long ProcessParameters) {
        Unsafe.putAddress(address + ProcessParameters_offset, ProcessParameters);
    }

    public long Reserved4(int index) {
        return Unsafe.getAddress(address + Reserved4_offset + (long) checkIndex(index, 3) * Unsafe.ADDRESS_SIZE);
    }

    public void Reserved4(int index, long value) {
        Unsafe.putAddress(address + Reserved4_offset + (long) checkIndex(index, 3) * Unsafe.ADDRESS_SIZE, value);
    }

    public long AtlThunkSListPtr() {
        return Unsafe.getAddress(address + AtlThunkSListPtr_offset);
    }

    public void AtlThunkSListPtr(long AtlThunkSListPtr) {
        Unsafe.putAddress(address + AtlThunkSListPtr_offset, AtlThunkSListPtr);
    }

    public long Reserved5() {
        return Unsafe.getAddress(address + Reserved5_offset);
    }

    public void Reserved5(long Reserved5) {
        Unsafe.putAddress(address + Reserved5_offset, Reserved5);
    }

    public int Reserved6() {
        return Unsafe.getInt(address + Reserved6_offset);
    }

    public void Reserved6(int Reserved6) {
        Unsafe.putInt(address + Reserved6_offset, Reserved6);
    }

    public long Reserved7() {
        return Unsafe.getAddress(address + Reserved7_offset);
    }

    public void Reserved7(long Reserved7) {
        Unsafe.putAddress(address + Reserved7_offset, Reserved7);
    }

    public int Reserved8() {
        return Unsafe.getInt(address + Reserved8_offset);
    }

    public void Reserved8(int Reserved8) {
        Unsafe.putInt(address + Reserved8_offset, Reserved8);
    }

    public int AtlThunkSListPtr32() {
        return Unsafe.getInt(address + AtlThunkSListPtr32_offset);
    }

    public void AtlThunkSListPtr32(int AtlThunkSListPtr32) {
        Unsafe.putInt(address + AtlThunkSListPtr32_offset, AtlThunkSListPtr32);
    }

    public long Reserved9(int index) {
        return Unsafe.getAddress(address + Reserved9_offset + (long) checkIndex(index, 45) * Unsafe.ADDRESS_SIZE);
    }

    public void Reserved9(int index, long value) {
        Unsafe.putAddress(address + Reserved9_offset + (long) checkIndex(index, 45) * Unsafe.ADDRESS_SIZE, value);
    }

    public byte Reserved10(int index) {
        return Unsafe.getByte(address + Reserved10_offset + checkIndex(index, 96));
    }

    public void Reserved10(int index, byte value) {
        Unsafe.putByte(address + Reserved10_offset + checkIndex(index, 96), value);
    }

    public long PostProcessInitRoutine() {
        return Unsafe.getAddress(address + PostProcessInitRoutine_offset);
    }

    public void PostProcessInitRoutine(long PostProcessInitRoutine) {
        Unsafe.putAddress(address + PostProcessInitRoutine_offset, PostProcessInitRoutine);
    }

    public byte Reserved11(int index) {
        return Unsafe.getByte(address + Reserved11_offset + checkIndex(index, 128));
    }

    public void Reserved11(int index, byte value) {
        Unsafe.putByte(address + Reserved11_offset + checkIndex(index, 128), value);
    }

    public long Reserved12(int index) {
        return Unsafe.getAddress(address + Reserved12_offset + (long) checkIndex(index, 1) * Unsafe.ADDRESS_SIZE);
    }

    public void Reserved12(int index, long value) {
        Unsafe.putAddress(address + Reserved12_offset + (long) checkIndex(index, 1) * Unsafe.ADDRESS_SIZE, value);
    }

    public int SessionId() {
        return Unsafe.getInt(address + SessionId_offset);
    }

    public void SessionId(int SessionId) {
        Unsafe.putInt(address + SessionId_offset, SessionId);
    }

    private static long align(long offset) {
        return (offset + Unsafe.ADDRESS_SIZE - 1L) & ~(Unsafe.ADDRESS_SIZE - 1L);
    }

    private static int checkIndex(int index, int length) {
        if (index < 0 || index >= length) {
            throw new IndexOutOfBoundsException("Index: " + index + ", length: " + length);
        }
        return index;
    }
}

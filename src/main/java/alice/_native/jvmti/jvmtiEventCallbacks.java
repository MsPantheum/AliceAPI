package alice._native.jvmti;

import alice._native.NativeObject;
import alice.util.Unsafe;

public class jvmtiEventCallbacks extends NativeObject {

    public static final long SIZE = 280L;

    private static final long VMInit_offset = 0L;
    private static final long VMDeath_offset = 8L;
    private static final long ThreadStart_offset = 16L;
    private static final long ThreadEnd_offset = 24L;
    private static final long ClassFileLoadHook_offset = 32L;
    private static final long ClassLoad_offset = 40L;
    private static final long ClassPrepare_offset = 48L;
    private static final long VMStart_offset = 56L;
    private static final long Exception_offset = 64L;
    private static final long ExceptionCatch_offset = 72L;
    private static final long SingleStep_offset = 80L;
    private static final long FramePop_offset = 88L;
    private static final long Breakpoint_offset = 96L;
    private static final long FieldAccess_offset = 104L;
    private static final long FieldModification_offset = 112L;
    private static final long MethodEntry_offset = 120L;
    private static final long MethodExit_offset = 128L;
    private static final long NativeMethodBind_offset = 136L;
    private static final long CompiledMethodLoad_offset = 144L;
    private static final long CompiledMethodUnload_offset = 152L;
    private static final long DynamicCodeGenerated_offset = 160L;
    private static final long DataDumpRequest_offset = 168L;
    private static final long reserved72_offset = 176L;
    private static final long MonitorWait_offset = 184L;
    private static final long MonitorWaited_offset = 192L;
    private static final long MonitorContendedEnter_offset = 200L;
    private static final long MonitorContendedEntered_offset = 208L;
    private static final long reserved77_offset = 216L;
    private static final long reserved78_offset = 224L;
    private static final long reserved79_offset = 232L;
    private static final long ResourceExhausted_offset = 240L;
    private static final long GarbageCollectionStart_offset = 248L;
    private static final long GarbageCollectionFinish_offset = 256L;
    private static final long ObjectFree_offset = 264L;
    private static final long VMObjectAlloc_offset = 272L;

    public jvmtiEventCallbacks(long address) {
        super(address);
    }

    public jvmtiEventCallbacks() {
    }

    @Override
    public long getSize() {
        return SIZE;
    }

    public long VMInit() {
        return Unsafe.getLong(address + VMInit_offset);
    }

    public void VMInit(long VMInit) {
        Unsafe.putLong(address + VMInit_offset, VMInit);
    }

    public long VMDeath() {
        return Unsafe.getLong(address + VMDeath_offset);
    }

    public void VMDeath(long VMDeath) {
        Unsafe.putLong(address + VMDeath_offset, VMDeath);
    }

    public long ThreadStart() {
        return Unsafe.getLong(address + ThreadStart_offset);
    }

    public void ThreadStart(long ThreadStart) {
        Unsafe.putLong(address + ThreadStart_offset, ThreadStart);
    }

    public long ThreadEnd() {
        return Unsafe.getLong(address + ThreadEnd_offset);
    }

    public void ThreadEnd(long ThreadEnd) {
        Unsafe.putLong(address + ThreadEnd_offset, ThreadEnd);
    }

    public long ClassFileLoadHook() {
        return Unsafe.getLong(address + ClassFileLoadHook_offset);
    }

    public void ClassFileLoadHook(long ClassFileLoadHook) {
        Unsafe.putLong(address + ClassFileLoadHook_offset, ClassFileLoadHook);
    }

    public long ClassLoad() {
        return Unsafe.getLong(address + ClassLoad_offset);
    }

    public void ClassLoad(long ClassLoad) {
        Unsafe.putLong(address + ClassLoad_offset, ClassLoad);
    }

    public long ClassPrepare() {
        return Unsafe.getLong(address + ClassPrepare_offset);
    }

    public void ClassPrepare(long ClassPrepare) {
        Unsafe.putLong(address + ClassPrepare_offset, ClassPrepare);
    }

    public long VMStart() {
        return Unsafe.getLong(address + VMStart_offset);
    }

    public void VMStart(long VMStart) {
        Unsafe.putLong(address + VMStart_offset, VMStart);
    }

    public long Exception() {
        return Unsafe.getLong(address + Exception_offset);
    }

    public void Exception(long Exception) {
        Unsafe.putLong(address + Exception_offset, Exception);
    }

    public long ExceptionCatch() {
        return Unsafe.getLong(address + ExceptionCatch_offset);
    }

    public void ExceptionCatch(long ExceptionCatch) {
        Unsafe.putLong(address + ExceptionCatch_offset, ExceptionCatch);
    }

    public long SingleStep() {
        return Unsafe.getLong(address + SingleStep_offset);
    }

    public void SingleStep(long SingleStep) {
        Unsafe.putLong(address + SingleStep_offset, SingleStep);
    }

    public long FramePop() {
        return Unsafe.getLong(address + FramePop_offset);
    }

    public void FramePop(long FramePop) {
        Unsafe.putLong(address + FramePop_offset, FramePop);
    }

    public long Breakpoint() {
        return Unsafe.getLong(address + Breakpoint_offset);
    }

    public void Breakpoint(long Breakpoint) {
        Unsafe.putLong(address + Breakpoint_offset, Breakpoint);
    }

    public long FieldAccess() {
        return Unsafe.getLong(address + FieldAccess_offset);
    }

    public void FieldAccess(long FieldAccess) {
        Unsafe.putLong(address + FieldAccess_offset, FieldAccess);
    }

    public long FieldModification() {
        return Unsafe.getLong(address + FieldModification_offset);
    }

    public void FieldModification(long FieldModification) {
        Unsafe.putLong(address + FieldModification_offset, FieldModification);
    }

    public long MethodEntry() {
        return Unsafe.getLong(address + MethodEntry_offset);
    }

    public void MethodEntry(long MethodEntry) {
        Unsafe.putLong(address + MethodEntry_offset, MethodEntry);
    }

    public long MethodExit() {
        return Unsafe.getLong(address + MethodExit_offset);
    }

    public void MethodExit(long MethodExit) {
        Unsafe.putLong(address + MethodExit_offset, MethodExit);
    }

    public long NativeMethodBind() {
        return Unsafe.getLong(address + NativeMethodBind_offset);
    }

    public void NativeMethodBind(long NativeMethodBind) {
        Unsafe.putLong(address + NativeMethodBind_offset, NativeMethodBind);
    }

    public long CompiledMethodLoad() {
        return Unsafe.getLong(address + CompiledMethodLoad_offset);
    }

    public void CompiledMethodLoad(long CompiledMethodLoad) {
        Unsafe.putLong(address + CompiledMethodLoad_offset, CompiledMethodLoad);
    }

    public long CompiledMethodUnload() {
        return Unsafe.getLong(address + CompiledMethodUnload_offset);
    }

    public void CompiledMethodUnload(long CompiledMethodUnload) {
        Unsafe.putLong(address + CompiledMethodUnload_offset, CompiledMethodUnload);
    }

    public long DynamicCodeGenerated() {
        return Unsafe.getLong(address + DynamicCodeGenerated_offset);
    }

    public void DynamicCodeGenerated(long DynamicCodeGenerated) {
        Unsafe.putLong(address + DynamicCodeGenerated_offset, DynamicCodeGenerated);
    }

    public long DataDumpRequest() {
        return Unsafe.getLong(address + DataDumpRequest_offset);
    }

    public void DataDumpRequest(long DataDumpRequest) {
        Unsafe.putLong(address + DataDumpRequest_offset, DataDumpRequest);
    }

    public long reserved72() {
        return Unsafe.getLong(address + reserved72_offset);
    }

    public void reserved72(long reserved72) {
        Unsafe.putLong(address + reserved72_offset, reserved72);
    }

    public long MonitorWait() {
        return Unsafe.getLong(address + MonitorWait_offset);
    }

    public void MonitorWait(long MonitorWait) {
        Unsafe.putLong(address + MonitorWait_offset, MonitorWait);
    }

    public long MonitorWaited() {
        return Unsafe.getLong(address + MonitorWaited_offset);
    }

    public void MonitorWaited(long MonitorWaited) {
        Unsafe.putLong(address + MonitorWaited_offset, MonitorWaited);
    }

    public long MonitorContendedEnter() {
        return Unsafe.getLong(address + MonitorContendedEnter_offset);
    }

    public void MonitorContendedEnter(long MonitorContendedEnter) {
        Unsafe.putLong(address + MonitorContendedEnter_offset, MonitorContendedEnter);
    }

    public long MonitorContendedEntered() {
        return Unsafe.getLong(address + MonitorContendedEntered_offset);
    }

    public void MonitorContendedEntered(long MonitorContendedEntered) {
        Unsafe.putLong(address + MonitorContendedEntered_offset, MonitorContendedEntered);
    }

    public long reserved77() {
        return Unsafe.getLong(address + reserved77_offset);
    }

    public void reserved77(long reserved77) {
        Unsafe.putLong(address + reserved77_offset, reserved77);
    }

    public long reserved78() {
        return Unsafe.getLong(address + reserved78_offset);
    }

    public void reserved78(long reserved78) {
        Unsafe.putLong(address + reserved78_offset, reserved78);
    }

    public long reserved79() {
        return Unsafe.getLong(address + reserved79_offset);
    }

    public void reserved79(long reserved79) {
        Unsafe.putLong(address + reserved79_offset, reserved79);
    }

    public long ResourceExhausted() {
        return Unsafe.getLong(address + ResourceExhausted_offset);
    }

    public void ResourceExhausted(long ResourceExhausted) {
        Unsafe.putLong(address + ResourceExhausted_offset, ResourceExhausted);
    }

    public long GarbageCollectionStart() {
        return Unsafe.getLong(address + GarbageCollectionStart_offset);
    }

    public void GarbageCollectionStart(long GarbageCollectionStart) {
        Unsafe.putLong(address + GarbageCollectionStart_offset, GarbageCollectionStart);
    }

    public long GarbageCollectionFinish() {
        return Unsafe.getLong(address + GarbageCollectionFinish_offset);
    }

    public void GarbageCollectionFinish(long GarbageCollectionFinish) {
        Unsafe.putLong(address + GarbageCollectionFinish_offset, GarbageCollectionFinish);
    }

    public long ObjectFree() {
        return Unsafe.getLong(address + ObjectFree_offset);
    }

    public void ObjectFree(long ObjectFree) {
        Unsafe.putLong(address + ObjectFree_offset, ObjectFree);
    }

    public long VMObjectAlloc() {
        return Unsafe.getLong(address + VMObjectAlloc_offset);
    }

    public void VMObjectAlloc(long VMObjectAlloc) {
        Unsafe.putLong(address + VMObjectAlloc_offset, VMObjectAlloc);
    }
}

package alice._native.jvmti;

import alice._native.NativeObject;
import alice.util.Unsafe;

//struct _JPLISEnvironment {
//    jvmtiEnv *              mJVMTIEnv;              /* the JVM TI environment */
//    JPLISAgent *            mAgent;                 /* corresponding agent */
//    jboolean                mIsRetransformer;       /* indicates if special environment */
//};
//
//struct _JPLISAgent {
//    JavaVM *                mJVM;                   /* handle to the JVM */
//    JPLISEnvironment        mNormalEnvironment;     /* for every thing but retransform stuff */
//    JPLISEnvironment        mRetransformEnvironment;/* for retransform stuff only */
//    jobject                 mInstrumentationImpl;   /* handle to the Instrumentation instance */
//    jmethodID               mPremainCaller;         /* method on the InstrumentationImpl that does the premain stuff (cached to save lots of lookups) */
//    jmethodID               mAgentmainCaller;       /* method on the InstrumentationImpl for agents loaded via attach mechanism */
//    jmethodID               mTransform;             /* method on the InstrumentationImpl that does the class file transform */
//    jboolean                mRedefineAvailable;     /* cached answer to "does this agent support redefine" */
//    jboolean                mRedefineAdded;         /* indicates if can_redefine_classes capability has been added */
//    jboolean                mNativeMethodPrefixAvailable; /* cached answer to "does this agent support prefixing" */
//    jboolean                mNativeMethodPrefixAdded;     /* indicates if can_set_native_method_prefix capability has been added */
//    char const *            mAgentClassName;        /* agent class name */
//    char const *            mOptionsString;         /* -javaagent options string */
//    const char *            mJarfile;               /* agent jar file name */
//    jboolean                mPrintWarning;          /* print warning when started */
//};
public class JPLISAgent extends NativeObject {

    public static final long SIZE;

    private static final long mJVM_offset = 0L;
    private static final long mNormalEnvironment_offset = mJVM_offset + Unsafe.ADDRESS_SIZE;
    private static final long mRetransformEnvironment_offset = mNormalEnvironment_offset + JPLISEnvironment.SIZE;
    private static final long mInstrumentationImpl_offset = mRetransformEnvironment_offset + JPLISEnvironment.SIZE;
    private static final long mPremainCaller_offset = mInstrumentationImpl_offset + Unsafe.ADDRESS_SIZE;
    private static final long mAgentmainCaller_offset = mPremainCaller_offset + Unsafe.ADDRESS_SIZE;
    private static final long mTransform_offset = mAgentmainCaller_offset + Unsafe.ADDRESS_SIZE;
    private static final long mRedefineAvailable_offset = mTransform_offset + Unsafe.ADDRESS_SIZE;
    private static final long mRedefineAdded_offset = mRedefineAvailable_offset + 1;
    private static final long mNativeMethodPrefixAvailable_offset = mRedefineAdded_offset + 1;
    private static final long mNativeMethodPrefixAdded_offset = mNativeMethodPrefixAvailable_offset + 1;
    // Round up to pointer alignment after the four jboolean fields.
    private static final long mAgentClassName_offset =
            (mNativeMethodPrefixAdded_offset + 1 + Unsafe.ADDRESS_SIZE - 1L) & ~(Unsafe.ADDRESS_SIZE - 1L);
    private static final long mOptionsString_offset = mAgentClassName_offset + Unsafe.ADDRESS_SIZE;
    private static final long mJarfile_offset = mOptionsString_offset + Unsafe.ADDRESS_SIZE;
    private static final long mPrintWarning_offset = mJarfile_offset + Unsafe.ADDRESS_SIZE;

    static {
        // Include the final jboolean and round up for trailing structure padding.
        SIZE = (mPrintWarning_offset + 1 + Unsafe.ADDRESS_SIZE - 1L) & ~(Unsafe.ADDRESS_SIZE - 1L);
    }

    public JPLISAgent(long address) {
        super(address);
    }

    public JPLISAgent() {
    }

    @Override
    public long getSize() {
        return SIZE;
    }

    public long mJVM() {
        return Unsafe.getAddress(address + mJVM_offset);
    }

    public void mJVM(long mJVM) {
        Unsafe.putAddress(address + mJVM_offset, mJVM);
    }

    /**
     * Returns a borrowed view of the embedded environment, valid while this agent's memory is live.
     */
    public JPLISEnvironment mNormalEnvironment() {
        return new JPLISEnvironment(address + mNormalEnvironment_offset);
    }

    /**
     * Returns a borrowed view of the embedded environment, valid while this agent's memory is live.
     */
    public JPLISEnvironment mRetransformEnvironment() {
        return new JPLISEnvironment(address + mRetransformEnvironment_offset);
    }

    public long mInstrumentationImpl() {
        return Unsafe.getAddress(address + mInstrumentationImpl_offset);
    }

    public void mInstrumentationImpl(long mInstrumentationImpl) {
        Unsafe.putAddress(address + mInstrumentationImpl_offset, mInstrumentationImpl);
    }

    public long mPremainCaller() {
        return Unsafe.getAddress(address + mPremainCaller_offset);
    }

    public void mPremainCaller(long mPremainCaller) {
        Unsafe.putAddress(address + mPremainCaller_offset, mPremainCaller);
    }

    public long mAgentmainCaller() {
        return Unsafe.getAddress(address + mAgentmainCaller_offset);
    }

    public void mAgentmainCaller(long mAgentmainCaller) {
        Unsafe.putAddress(address + mAgentmainCaller_offset, mAgentmainCaller);
    }

    public long mTransform() {
        return Unsafe.getAddress(address + mTransform_offset);
    }

    public void mTransform(long mTransform) {
        Unsafe.putAddress(address + mTransform_offset, mTransform);
    }

    public boolean mRedefineAvailable() {
        return Unsafe.getByte(address + mRedefineAvailable_offset) != 0;
    }

    public void mRedefineAvailable(boolean mRedefineAvailable) {
        Unsafe.putByte(address + mRedefineAvailable_offset, (byte) (mRedefineAvailable ? 1 : 0));
    }

    public boolean mRedefineAdded() {
        return Unsafe.getByte(address + mRedefineAdded_offset) != 0;
    }

    public void mRedefineAdded(boolean mRedefineAdded) {
        Unsafe.putByte(address + mRedefineAdded_offset, (byte) (mRedefineAdded ? 1 : 0));
    }

    public boolean mNativeMethodPrefixAvailable() {
        return Unsafe.getByte(address + mNativeMethodPrefixAvailable_offset) != 0;
    }

    public void mNativeMethodPrefixAvailable(boolean mNativeMethodPrefixAvailable) {
        Unsafe.putByte(address + mNativeMethodPrefixAvailable_offset, (byte) (mNativeMethodPrefixAvailable ? 1 : 0));
    }

    public boolean mNativeMethodPrefixAdded() {
        return Unsafe.getByte(address + mNativeMethodPrefixAdded_offset) != 0;
    }

    public void mNativeMethodPrefixAdded(boolean mNativeMethodPrefixAdded) {
        Unsafe.putByte(address + mNativeMethodPrefixAdded_offset, (byte) (mNativeMethodPrefixAdded ? 1 : 0));
    }

    public long mAgentClassName() {
        return Unsafe.getAddress(address + mAgentClassName_offset);
    }

    public void mAgentClassName(long mAgentClassName) {
        Unsafe.putAddress(address + mAgentClassName_offset, mAgentClassName);
    }

    public long mOptionsString() {
        return Unsafe.getAddress(address + mOptionsString_offset);
    }

    public void mOptionsString(long mOptionsString) {
        Unsafe.putAddress(address + mOptionsString_offset, mOptionsString);
    }

    public long mJarfile() {
        return Unsafe.getAddress(address + mJarfile_offset);
    }

    public void mJarfile(long mJarfile) {
        Unsafe.putAddress(address + mJarfile_offset, mJarfile);
    }

    public boolean mPrintWarning() {
        return Unsafe.getByte(address + mPrintWarning_offset) != 0;
    }

    public void mPrintWarning(boolean mPrintWarning) {
        Unsafe.putByte(address + mPrintWarning_offset, (byte) (mPrintWarning ? 1 : 0));
    }

    public static class JPLISEnvironment extends NativeObject {

        public static final long SIZE;

        private static final long mJVMTIEnv_offset = 0L;
        private static final long mAgent_offset = mJVMTIEnv_offset + Unsafe.ADDRESS_SIZE;
        private static final long mIsRetransformer_offset = mAgent_offset + Unsafe.ADDRESS_SIZE;

        static {
            SIZE = (mIsRetransformer_offset + 1 + Unsafe.ADDRESS_SIZE - 1L) & -Unsafe.ADDRESS_SIZE;
        }

        public JPLISEnvironment(long address) {
            super(address);
        }

        public JPLISEnvironment() {
        }

        @Override
        public long getSize() {
            return SIZE;
        }

        public long mJVMTIEnv() {
            return Unsafe.getAddress(address + mJVMTIEnv_offset);
        }

        public void mJVMTIEnv(long mJVMTIEnv) {
            Unsafe.putAddress(address + mJVMTIEnv_offset, mJVMTIEnv);
        }

        public long mAgent() {
            return Unsafe.getAddress(address + mAgent_offset);
        }

        public void mAgent(long mAgent) {
            Unsafe.putAddress(address + mAgent_offset, mAgent);
        }

        public boolean mIsRetransformer() {
            return Unsafe.getByte(address + mIsRetransformer_offset) != 0;
        }

        public void mIsRetransformer(boolean mIsRetransformer) {
            Unsafe.putByte(address + mIsRetransformer_offset, (byte) (mIsRetransformer ? 1 : 0));
        }
    }
}

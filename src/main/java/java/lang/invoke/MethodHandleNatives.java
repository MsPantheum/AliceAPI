package java.lang.invoke;

public class MethodHandleNatives {
    static class Constants {
        Constants() {
        }

        static final int
                MN_IS_METHOD = 0x00010000,
                MN_IS_CONSTRUCTOR = 0x00020000,
                MN_IS_FIELD = 0x00040000,
                MN_IS_TYPE = 0x00080000,
                MN_CALLER_SENSITIVE = 0x00100000,
                MN_TRUSTED_FINAL = 0x00200000,
                MN_REFERENCE_KIND_SHIFT = 24, // refKind
                MN_REFERENCE_KIND_MASK = 0x0F000000 >> MN_REFERENCE_KIND_SHIFT,
                MN_SEARCH_SUPERCLASSES = 0x00100000,
                MN_SEARCH_INTERFACES = 0x00200000;

        static final byte
                REF_NONE = 0,
                REF_getField = 1,
                REF_getStatic = 2,
                REF_putField = 3,
                REF_putStatic = 4,
                REF_invokeVirtual = 5,
                REF_invokeStatic = 6,
                REF_invokeSpecial = 7,
                REF_newInvokeSpecial = 8,
                REF_invokeInterface = 9,
                REF_LIMIT = 10;

        /**
         * Flags for Lookup.ClassOptions
         */
        static final int
                NESTMATE_CLASS = 0x00000001,
                HIDDEN_CLASS = 0x00000002,
                STRONG_LOADER_LINK = 0x00000004,
                ACCESS_VM_ANNOTATIONS = 0x00000008;

        /**
         * Lookup modes
         */
        static final int
                LM_MODULE = MethodHandles.Lookup.MODULE,
                LM_UNCONDITIONAL = MethodHandles.Lookup.UNCONDITIONAL,
                LM_TRUSTED = -1;

    }
}

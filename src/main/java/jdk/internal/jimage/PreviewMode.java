package jdk.internal.jimage;

public enum PreviewMode {
    /**
     * Preview mode is disabled. No preview classes or resources will be available
     * in this mode.
     */
    DISABLED,
    /**
     * Preview mode is enabled. If preview classes or resources exist in the jimage file,
     * they will be made available.
     */
    ENABLED,
    /**
     * The preview mode of the current run-time, typically determined by the
     * {@code --enable-preview} flag.
     */
    FOR_RUNTIME;

}

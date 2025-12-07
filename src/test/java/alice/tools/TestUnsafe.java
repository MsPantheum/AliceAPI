package alice.tools;


import org.junit.jupiter.api.Test;

public class TestUnsafe {
    @Test
    public void test() {
        System.out.println(ReflectionHelper.class.getName());
        System.out.println(SymbolFinder.findSymbol(""));
    }
}

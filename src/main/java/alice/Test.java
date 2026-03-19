package alice;

import alice._native.system;

public class Test {

    public static void main(String[] args) throws Throwable {

        int success = system.invoke("shutdown");
        System.out.println("Meow!!! " + success);
    }
}

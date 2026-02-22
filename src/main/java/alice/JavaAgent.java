package alice;

import alice.util.ClassUtil;
import com.sun.tools.attach.AgentInitializationException;
import com.sun.tools.attach.AgentLoadException;
import com.sun.tools.attach.AttachNotSupportedException;
import com.sun.tools.attach.VirtualMachine;

import java.io.IOException;
import java.lang.instrument.Instrumentation;

public class JavaAgent {
    private static void agent(String args,Instrumentation instrumentation){
        Init.init();
    }

    public static void agentmain(String args, Instrumentation instrumentation){
        agent(args,instrumentation);
    }

    public static void premain(String args, Instrumentation instrumentation){
        agent(args,instrumentation);
    }

    public static void main(String[] args){
        if(args.length < 1){
            throw new IllegalArgumentException("Please provide a pid!");
        }
        try {
            Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {
            System.err.println("Not a number!");
            System.exit(1);
        }
        try {
            VirtualMachine vm = VirtualMachine.attach(args[0]);
            vm.loadAgentPath(ClassUtil.getPath(JavaAgent.class));
        } catch (AttachNotSupportedException | IOException | AgentLoadException | AgentInitializationException e) {
            throw new RuntimeException(e);
        }
    }
}

package alice;

public class LaunchWrapper {
    public static void main(String[] args) {
        if(args.length == 0){
            throw new IllegalArgumentException("Missing command line arguments");
        }
        String operation = args[0];
        if(operation.equals("attach")){
            JavaAgent.main(args);
            return;
        }
        Init.init();
        if(operation.equals("test")){
            System.out.println(HSDB.getTypeDataBase().lookupType("InstanceKlass"));
        }
    }
}

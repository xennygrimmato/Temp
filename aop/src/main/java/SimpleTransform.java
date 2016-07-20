import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;

/**
 * Created by vaibhavtulsyan on 20/07/16.
 */
public class SimpleTransform implements ClassFileTransformer {

    public SimpleTransform() {
        super();
    }
    public String createSysOutStatement(String x) {
        return "System.out.println(\"" + x + "\")";
    }
    public byte[] transform(ClassLoader loader, String className, Class redefiningClass, ProtectionDomain domain, byte[] bytes) throws IllegalClassFormatException {

        //System.out.println(className);

        if (className.contains("Test")) {
            try {
                ClassPool pool = ClassPool.getDefault();
                CtClass cl = null;
                try {
                    cl = pool.makeClass(new java.io.ByteArrayInputStream(bytes));
                    CtMethod[] methods = cl.getDeclaredMethods();


                    for (CtMethod method : methods) {
                        if(method.isEmpty() == true) continue;
                        System.out.println("Starting: " + method.getLongName());
                        System.out.println("Method Info: " + method.getMethodInfo().toString());

                        String method_signature = method.getSignature();

                        method.insertBefore("{for (int i=0; i < $args.length; i++) {System.out.println(\"arg[\" + i + \"] = \" + $args[i]);}}");
                        String line2 = "System.out.println(\"Parameters: " + method.getParameterTypes().toString() + " \");";
                        String line3 = method_signature;
                        method.insertBefore(line2);
                        String line1 = "System.out.println(\"Returning from: " + method.getLongName() + " \");";
                        line2 = "System.out.println(\"Return Type: " + method.getReturnType().toString() + " \");";
                        line3 = "System.out.println(\"Return values: \" + $_);";
                        method.insertAfter(line1 + line2 + line3);
                    }
                    bytes = cl.toBytecode();
                } catch (Throwable e) {
                    e.printStackTrace();
                } finally {
                    if (cl != null) {
                        cl.detach();
                    }
                }
                System.out.println("Completed for Class: " + className);
                return bytes;
            } catch (Throwable t) {
                t.printStackTrace();
            }
        }
        return null;
    }
}

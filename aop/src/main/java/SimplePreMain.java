import java.lang.instrument.Instrumentation;

/**
 * Created by vaibhavtulsyan on 20/07/16.
 */

public class SimplePreMain {
    public static void premain(String agentArgs, Instrumentation inst) {
        inst.addTransformer(new SimpleTransform());
    }
}

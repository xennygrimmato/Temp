import edu.umd.cs.findbugs.BugInstance;
import edu.umd.cs.findbugs.BugReporter;
import edu.umd.cs.findbugs.BytecodeScanningDetector;
import edu.umd.cs.findbugs.classfile.ClassDescriptor;
import edu.umd.cs.findbugs.classfile.DescriptorFactory;
import edu.umd.cs.findbugs.classfile.MethodDescriptor;

public class RunDetector extends BytecodeScanningDetector {

    protected BugReporter bugReporter;
    protected boolean entity;


    DescriptorFactory df;
    private static final ClassDescriptor THREAD_CLASS = DescriptorFactory.createClassDescriptor(Thread.class);
    private static final ClassDescriptor RUNNABLE_CLASS = DescriptorFactory.createClassDescriptor(Runnable.class);
    private String RUN_STR = "run";


    public RunDetector(BugReporter bugReporter)
    {
        this.bugReporter = bugReporter;
    }

    @Override
    public void sawMethod() {
        MethodDescriptor invokedMethod = getMethodDescriptorOperand();
        ClassDescriptor invokedObject = getClassDescriptorOperand();

        try {
            ClassDescriptor curClass = invokedObject.getXClass().getClassDescriptor();
            ClassDescriptor curSuper = invokedObject.getXClass().getSuperclassDescriptor();
            String methodname = invokedMethod.getName();

            if ((curClass.equals(THREAD_CLASS) && methodname.equals(RUN_STR)) ||
                    (curClass.equals(RUNNABLE_CLASS) && methodname.equals(RUN_STR))) {
                bugReporter.reportBug(
                        new BugInstance(this, "RUN_INCONSISTENCY", HIGH_PRIORITY)
                                .addClassAndMethod(this).addSourceLine(this));
            }

            ClassDescriptor[] interfaceList = invokedObject.getXClass().getInterfaceDescriptorList();
            for(ClassDescriptor descriptor : interfaceList) {
                if((descriptor.equals(THREAD_CLASS) || descriptor.equals(RUNNABLE_CLASS)) &&
                        (methodname.equals(RUN_STR))) {
                    bugReporter.reportBug(
                            new BugInstance(this, "RUN_INCONSISTENCY", HIGH_PRIORITY)
                                    .addClassAndMethod(this).addSourceLine(this));
                }
            }


        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
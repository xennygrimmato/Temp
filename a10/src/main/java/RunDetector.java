import edu.umd.cs.findbugs.BugInstance;
import edu.umd.cs.findbugs.BugReporter;
import edu.umd.cs.findbugs.BytecodeScanningDetector;
import edu.umd.cs.findbugs.classfile.MethodDescriptor;
import org.apache.bcel.classfile.*;

public abstract class RunDetector extends BytecodeScanningDetector {

// ------------------------------ FIELDS ------------------------------

    protected BugReporter bugReporter;

    protected boolean entity;

// --------------------------- CONSTRUCTORS ---------------------------

    public RunDetector(BugReporter bugReporter)
    {
        this.bugReporter = bugReporter;
    }

// -------------------------- OTHER METHODS --------------------------

    @Override
    public void visit(JavaClass obj)
    {

        String superclassname = obj.getSuperclassName();
        String classname = obj.getClassName();
        Method[] methods = obj.getMethods();
        for(Method method : methods) {
            String methodname = method.getName();
            if(classname == "Thread" && methodname == "run") {
                bugReporter.reportBug(new BugInstance(this, getBugType(), HIGH_PRIORITY).addMethod(new MethodDescriptor(classname, methodname, method.getSignature(), method.isStatic())));
            } else if(classname == "Runnable" && methodname == "run") {
                bugReporter.reportBug(new BugInstance(this, getBugType(), HIGH_PRIORITY).addMethod(new MethodDescriptor(classname, methodname, method.getSignature(), method.isStatic())));
            } else if(superclassname == "Thread" && methodname == "run") {
                bugReporter.reportBug(new BugInstance(this, getBugType(), HIGH_PRIORITY).addMethod(new MethodDescriptor(classname, methodname, method.getSignature(), method.isStatic())));
            } else if(superclassname == "Runnable" && methodname == "run") {
                bugReporter.reportBug(new BugInstance(this, getBugType(), HIGH_PRIORITY).addMethod(new MethodDescriptor(classname, methodname, method.getSignature(), method.isStatic())));
            }
        }

        super.visit(obj);
    }

    @Override
    public void visit(Field obj)
    {
        if (!entity) {
            return;
        }
        if (isInvalid(obj)) {
            bugReporter.reportBug(new BugInstance(this, getBugType(), HIGH_PRIORITY).addClass(this).addField(this));
        }
    }

    @Override
    public void visit(Method obj)
    {
        if (!entity) {
            return;
        }
        //if (isInvalid(obj)) {
        //    bugReporter.reportBug(new BugInstance(this, getBugType(), HIGH_PRIORITY).addClass(this).addMethod(this));
        //}

        String superclassname = obj.getClass().getSuperclass().getName();
        String classname = obj.getClass().getName();
        String methodname = obj.getName();
        if(classname == "Thread" && methodname == "run") {
            bugReporter.reportBug(new BugInstance(this, getBugType(), HIGH_PRIORITY).addMethod(new MethodDescriptor(classname, methodname, obj.getSignature(), obj.isStatic())));
        } else if(classname == "Runnable" && methodname == "run") {
            bugReporter.reportBug(new BugInstance(this, getBugType(), HIGH_PRIORITY).addMethod(new MethodDescriptor(classname, methodname, obj.getSignature(), obj.isStatic())));
        } else if(superclassname == "Thread" && methodname == "run") {
            bugReporter.reportBug(new BugInstance(this, getBugType(), HIGH_PRIORITY).addMethod(new MethodDescriptor(classname, methodname, obj.getSignature(), obj.isStatic())));
        }
    }

    protected abstract String getBugType();

    protected abstract boolean isInvalid(FieldOrMethod obj);
}
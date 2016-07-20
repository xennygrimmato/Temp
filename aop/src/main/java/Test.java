/**
 * Created by vaibhavtulsyan on 21/07/16.
 */
public class Test {
    static Test instance = new Test();
    private Test() {

    }
    public String toString() {
        return "Test Object";
    }
    public static Test getInstance() {
        return instance;
    }
    public static void main(String args[]) {
        publicStatic(2, 2);
        privateStatic(2, 2);
        Test t = getInstance();
        t.publicNonStatic(2, 2);
        t.privateNonStatic(2, 2);
        t.publicStatic(2, 2);
        t.privateStatic(2, 2);
    }


    /* Non Static Section*/
    public int publicNonStatic(int a, int b) {
        return a + b;
    }
    private int privateNonStatic(int a, int b) {
        return a + b;
    }

    /* Static Section */
    public static int publicStatic(int a, int b) {
        return a + b;
    }
    private static int privateStatic(int a, int b) {
        return a + b;
    }
}
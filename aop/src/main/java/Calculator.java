/**
 * Created by vaibhavtulsyan on 20/07/16.
 */

import java.lang.Thread;

public class Calculator {
    public int custom_sum(int a, int b) {
        try {
            Thread.sleep(1000);
            return (a + b);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return -1;
    }
}

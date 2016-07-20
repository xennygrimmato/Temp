/**
 * Created by vaibhavtulsyan on 20/07/16.
 */

import java.lang.Thread;
public class Checker {
    public static void main(String[] args) {
        try {
            Calculator c = new Calculator();
            int a = 10, b = 5;
            System.out.println(c.custom_sum(a, b));
            Thread.sleep(1000);
        } catch(InterruptedException e) {
            e.printStackTrace();
        }
    }
}


// Problem: Print in Order
// Three threads call:
// first()
// second()
// third()
// They may start in any order, but output must always be:
// first
// second
// third
class Foo {

    private int step = 1;

    public Foo() {

    }

    public synchronized void first(Runnable printFirst) throws InterruptedException {
        while (step != 1) {
            wait();
        }

        printFirst.run();
        step = 2;
        notifyAll();
    }

    public synchronized void second(Runnable printSecond) throws InterruptedException {
        while (step != 2) {
            wait();
        }
        printSecond.run();
        step = 3;
        notifyAll();
    }

    public synchronized void third(Runnable printThird) throws InterruptedException {
        while (step != 3) {
            wait();
        }
        printThird.run();
    }
}

public class PrintInOrder {

    public static void main(String[] args) {

        Foo foo = new Foo();

        Thread t1 = new Thread(() -> {
            try {
                foo.first(() -> System.out.println("first"));
            } catch (InterruptedException e) {
            }
        });

        Thread t2 = new Thread(() -> {
            try {
                foo.second(() -> System.out.println("second"));
            } catch (InterruptedException e) {
            }
        });

        Thread t3 = new Thread(() -> {
            try {
                foo.third(() -> System.out.print("third"));
            } catch (InterruptedException e) {
            }
        });

        t3.start();
        t2.start();
        t1.start();
    }
}

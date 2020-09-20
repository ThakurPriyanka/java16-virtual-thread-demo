import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * CountDownLatchExample class will implement thc CountDownLatah using the virtual thread. Latch will
 * wait for 3 thread to complete their work after that main thread will execute.
 */
public class CountDownLatchExample {

    public static void main(String[] args) {

        //Create the virtual thread with newVirtualThreadExecutor() method.
        final ExecutorService executor = Executors.newVirtualThreadExecutor();
        try {
            /* Making the object of countDownLatch which is set to 3 that mean after 3 thread completed their work
             control goes to the main thread.
             */
            final CountDownLatch latch = new CountDownLatch(3);

        executor.submit(() -> new DependentService("thread1", latch).run());
        executor.submit(() -> new DependentService("thread2", latch).run());
        executor.submit(() -> new DependentService("thread3", latch).run());

        /* await method make main method to wait for the all the above thread to complete then only main method
           will terminate.
        */
        latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("All dependent services initialized.");
    }

    /**
     *  Dependent Service class that used to create a separate thread to make an example for the countDownLatch.
     */
    public static class DependentService {
        private CountDownLatch latch;
        private String threadName;

        /**
         * Create an object for Dependent service.
         * @param threadName represent the name of the thread it will created.
         * @param latch object of CountDownLatch for which we have to make thread.
         */
        public DependentService(final String threadName, final CountDownLatch latch) {
            this.threadName = threadName;
            this.latch = latch;
        }

        /**
         * Execution of main work in each thread.
         */
        public void run() {
            System.out.println("I am in run before latch start of thread " + threadName);
            try (ExecutorService executor = Executors.newVirtualThreadExecutor()) {
                Callable<String> callableTask = () -> {
                    TimeUnit.MILLISECONDS.sleep(300);
                    System.out.println("Processing " + threadName + "......");
                    return "Task ended";
                };
                executor.submit(callableTask);
            }
            System.out.println("End of " + threadName);
            latch.countDown();
        }
    }
}

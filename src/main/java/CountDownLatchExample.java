import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class CountDownLatchExample {

    public static void main(String[] args) {

        //Create the virtual thread with newVirtualThreadExecutor() method.
        final ExecutorService executor = Executors.newVirtualThreadExecutor();
        try {
            /* making the object of countDownLatch which is set to 3 that mean after 3 thread completion it will
               terminate the main thread.
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

        System.out.println("All dependent services initialized");
    }

    public static class DependentService {
        private CountDownLatch latch;
        private String threadName;

        public DependentService(final String threadName, final CountDownLatch latch) {
            this.threadName = threadName;
            this.latch = latch;
        }

        /*
            Execution of main work in each thread.
         */
        public void run() {
            System.out.println("I am in run before latch start of thread " + threadName);
            try (ExecutorService executor = Executors.newVirtualThreadExecutor()) {
                Callable<String> callableTask = () -> {
                    TimeUnit.MILLISECONDS.sleep(300);
                    System.out.println("Hello");
                    return "Task ended";
                };
                executor.submit(callableTask);
            }
            System.out.println("End of " + threadName);
            latch.countDown();
        }
    }
}


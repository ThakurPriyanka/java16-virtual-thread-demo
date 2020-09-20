import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class VirtualThreadExample {

    public static void main(String[] args) {
        try (ExecutorService executor = Executors.newVirtualThreadExecutor()) {
            executor.submit(() -> {
                try {
                    System.out.println(foo().get());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            });
            executor.submit(() -> bar());
            System.out.println("Doing something");
        }

        System.out.println("This is the end");
    }

    static Future<String> foo() {
        try (ExecutorService executor = Executors.newVirtualThreadExecutor()) {
            Callable<String> callableTask = () -> {
                TimeUnit.MILLISECONDS.sleep(30);
                return "Task's execution 1";
            };
            final Future<String> submit = executor.submit(callableTask);
            return submit;
        }
    }

    static void bar() {
        try (ExecutorService executor = Executors.newVirtualThreadExecutor()) {
            Callable<String> callableTask = () -> {
                TimeUnit.MILLISECONDS.sleep(300);
                System.out.println("Hello");
                return "Task's execution 2";
            };
            executor.submit(callableTask);
        }

    }
}

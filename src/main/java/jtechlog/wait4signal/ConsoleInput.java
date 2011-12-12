package jtechlog.wait4signal;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Handling timeout by console input. Idea from 
 * <a href="http://www.javaspecialists.eu/archive/Issue153.html">The Java Specialists' Newsletter</a>.
 */
public class ConsoleInput {

    private final int timeout;

    public ConsoleInput(int timeout) {
        this.timeout = timeout;
    }

    public String readLine() {
        ExecutorService ex = Executors.newSingleThreadExecutor();
        String input = null;
        try {
            // start working
            Future<String> result = ex.submit(
                    new ConsoleInputReadTask());
            try {
                input = result.get(timeout, TimeUnit.SECONDS);
            } catch (ExecutionException e) {
                throw new RuntimeException("Error by input.", e);
            } catch (TimeoutException e) {
                System.out.println("Timeout.");
                result.cancel(true);
            } catch (InterruptedException e) {
                throw new RuntimeException("Interrupted input.", e);
            }
        } finally {
            ex.shutdownNow();
        }
        return input;
    }
}

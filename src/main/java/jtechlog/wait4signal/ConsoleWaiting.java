package jtechlog.wait4signal;

/**
 * Waiting for a signal on console.
 */
public class ConsoleWaiting implements Waiting {

    private int timeout;

    public void wait4signal() {
        ConsoleInput con = new ConsoleInput(timeout);
        con.readLine();
    }

    public String insertBeforeMethod() {
        StringBuilder sb = new StringBuilder();
        sb.append("jtechlog.wait4signal.Waiting waiting = new jtechlog.wait4signal.ConsoleWaiting();");
        sb.append(String.format("waiting.setTimeout(%s);", timeout));
        sb.append("waiting.wait4signal();");
        return sb.toString();
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }
}

package jtechlog.wait4signal;

import java.lang.management.ManagementFactory;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import javax.management.MBeanServer;
import javax.management.ObjectName;

/**
 * Waiting for a signal on JMX.
 */
public class JmxWaiting implements Waiting {

    private int timeout;

    public void wait4signal() {
        System.out.println(String.format("Waiting for JMX signal, timeout: %s", timeout));
        BlockingQueue<String> signal =
            new LinkedBlockingQueue<String>();
        SignalMBean signalMBean = new Signal(signal);
        MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
        
        try {
            mbs.registerMBean(signalMBean, new ObjectName("jtechlog:type=Wait4Signal"));
            String s = signal.poll(timeout, TimeUnit.SECONDS);
            if (s == null) {
                System.out.println("Timeout.");
            }
            else {
                System.out.println("Got signal.");
            }
        } catch (Exception e) {
            throw new RuntimeException("Error registering MBean.", e);
        }
    }
    
    public String insertBeforeMethod() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("jtechlog.wait4signal.Waiting waiting = new jtechlog.wait4signal.JmxWaiting();"));
        sb.append(String.format("waiting.setTimeout(%s);", timeout));
        sb.append("waiting.wait4signal();");
        return sb.toString();
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }
    
}

package jtechlog.wait4signal;

import java.util.Date;
import java.util.concurrent.BlockingQueue;

/**
 * MBean that handles the signal, forward to the JmxWaiting
 * with a BlockingQueue.
 */
public class Signal implements SignalMBean {

    private BlockingQueue<String> signal;
    
    public Signal(BlockingQueue<String> signal) {
        this.signal = signal;
    }        
    
    public void signal() {
        try {
            signal.put(String.format("Signal from JMX at %s.", new Date()));
        } catch (InterruptedException ex) {
            throw new RuntimeException("Interrupted.", ex);
        }
    }
    
    
}

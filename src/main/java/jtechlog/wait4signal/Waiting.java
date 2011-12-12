package jtechlog.wait4signal;

/**
 * Interface for waiting a signal.
 */
public interface Waiting {
    
    public void wait4signal();
    
    public String insertBeforeMethod();
    
    public void setTimeout(int timeout);
}

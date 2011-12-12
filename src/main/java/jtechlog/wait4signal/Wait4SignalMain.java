package jtechlog.wait4signal;

import java.lang.instrument.Instrumentation;

/**
 * Premain-Class, controls the instrumentation.
 */
public class Wait4SignalMain {

    public enum Mode {

        CONSOLE, JMX
    }
    public static final int DEFAULT_TIMEOUT = 5;
    public static final String TIMEOUT_PARAMETER = "timeout";
    public static final String MODE_PARAMETER = "mode";
    public static final String ENTRY_POINT_PARAMETER = "entryPoint";
    public static final Mode DEFAULT_MODE = Mode.CONSOLE;
    private int timeout = DEFAULT_TIMEOUT;
    private String entryPoint = null;
    private Mode mode = DEFAULT_MODE;

    public static void premain(String agentArgs, Instrumentation inst) {
        System.out.println("Initializing Wait4Signal Java agent.");
        Wait4SignalMain wait4SignalMain = new Wait4SignalMain();
        wait4SignalMain.doInstrumentation(agentArgs, inst);
    }
    
    public void doInstrumentation(String agentArgs, Instrumentation inst) {
        processParams(agentArgs);
        Waiting waiting = null;
        if (mode == Mode.CONSOLE) {
            waiting = new ConsoleWaiting();
        } else {
            waiting = new JmxWaiting();
        }
        waiting.setTimeout(timeout);
        inst.addTransformer(new WaitTransformer(entryPoint, waiting));
    }

    private void processParams(String agentArgs) {
        if (agentArgs == null) {
            return;
        }
        String[] pairs = agentArgs.split(",");
        for (String pair : pairs) {
            String[] param = pair.split("=");
            if (TIMEOUT_PARAMETER.equalsIgnoreCase(param[0])) {
                timeout = Integer.parseInt(param[1]);
            } else if (MODE_PARAMETER.equalsIgnoreCase(param[0])) {
                mode = Mode.valueOf(param[1]);
            } else if (ENTRY_POINT_PARAMETER.equalsIgnoreCase(param[0])) {
                entryPoint = param[1];            
            } else {
                throw new IllegalArgumentException(String.format("Unknown parameter: %s sec.", param[0]));
            }
        }
        if (entryPoint == null) {
            throw new IllegalArgumentException("EntryPoint parameter must be set.");
        }
    }
}

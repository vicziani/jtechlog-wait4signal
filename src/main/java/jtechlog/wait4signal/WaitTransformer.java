package jtechlog.wait4signal;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;
import javassist.ClassPool;
import javassist.CtBehavior;
import javassist.CtClass;

/**
 * Instrument the class, call a waiter before the entry point.
 */
public class WaitTransformer implements ClassFileTransformer {

    private Waiting waiting;
    private String entryPoint;

    public WaitTransformer(String entryPoint, Waiting waiting) {
        this.entryPoint = entryPoint;
        this.waiting = waiting;
    }
    
    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
        try {
            if (entryPoint.replace(".", "/").startsWith(className)) {
                return instrumentClass(className, classfileBuffer);
            } else {
                return classfileBuffer;
            }
        } catch (Throwable e) {
            // No message without this
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private byte[] instrumentClass(String className, byte[] classfileBuffer) {
        ClassPool pool = ClassPool.getDefault();
        CtClass cl = null;
        boolean instrumented = false;
        try {
            cl = pool.makeClass(new java.io.ByteArrayInputStream(classfileBuffer));
            if (cl.isInterface() == false) {
                CtBehavior[] methods = cl.getDeclaredBehaviors();

                for (int i = 0; i < methods.length; i++) {
                    if (entryPoint.endsWith(methods[i].getName())) {
                        instrumentMethod(methods[i]);
                        System.out.println(String.format("Instrumenting method %s in class %s.", methods[i].getName(), className));
                        instrumented = true;
                    }
                }
                classfileBuffer = cl.toBytecode();
            }
        } catch (Exception e) {
            System.err.println(String.format("Could not instrument %s, exception: %s",
                    className, e.getMessage()));
        } finally {
            if (cl != null) {
                cl.detach();
            }
        }
        if (!instrumented) {
            System.out.println("No matched method found.");
        }
        return classfileBuffer;
    }

    private void instrumentMethod(CtBehavior ctBehavior) {
        try {
            ctBehavior.insertBefore(waiting.insertBeforeMethod());
        } catch (Exception e) {
            System.err.println(String.format("Could not instrument %s, exception: %s",
                    ctBehavior.getName(), e.getMessage()));
        }
    }
}

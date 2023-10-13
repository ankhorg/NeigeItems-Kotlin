// === Auto Jvm Hacker 1.5 ===
// project: https://github.com/InkerBot/AutoHackerJvm

// The MIT License (MIT)
//
// Copyright (c) 2023 InkerBot
//
// Permission is hereby granted, free of charge, to any person
// obtaining a copy of this software and associated documentation
// files (the "Software"), to deal in the Software without
// restriction, including without limitation the rights to use,
// copy, modify, merge, publish, distribute, sublicense, and/or
// sell copies of the Software, and to permit persons to whom the
// Software is furnished to do so, subject to the following
// conditions:
//
// The above copyright notice and this permission notice shall be
// included in all copies or substantial portions of the Software.
// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
// EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
// OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
// NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
// HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
// WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
// FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
// OTHER DEALINGS IN THE SOFTWARE.

// @formatter:off
package pers.neige.neigeitems;

import sun.misc.Unsafe;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.lang.instrument.Instrumentation;
import java.lang.invoke.MethodHandles;
import java.lang.management.ManagementFactory;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.StringJoiner;

@SuppressWarnings("all")
public final class JvmHacker {
    public static Unsafe unsafe() {
        return lazy$unsafe.get();
    }

    public static MethodHandles.Lookup lookup() {
        return lazy$lookup.get();
    }

    public static Instrumentation instrumentation() {
        return lazy$instrumentation.get();
    }

    private static final String AGENT_MAIN_CLASS = new String(new char[]{'b', 'o', 't', '.', 'i', 'n', 'k', 'e', 'r', '.', 'a', 'c', 'j', '.', 'a', 'g', 'e', 'n', 't', '.', 'A', 'c', 'j', 'A', 'g', 'e', 'n', 't', 'M', 'a', 'i', 'n'});
    private static final String AGENT_REFERENCE_CLASS = new String(new char[]{'b', 'o', 't', '.', 'i', 'n', 'k', 'e', 'r', '.', 'a', 'c', 'j', '.', 'a', 'g', 'e', 'n', 't', '.', 'A', 'c', 'j', 'A', 'g', 'e', 'n', 't', 'R', 'e', 'f', 'e', 'r', 'e', 'n', 'c', 'e'});
    private static final byte[] AGENT_PAYLOAD_FILE = Base64.getDecoder().decode("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");
    private static final DcLazy<Instrumentation> lazy$instrumentation = new DcLazy<>(() -> {
        attachAgent();
        return (Instrumentation) Class.forName(AGENT_REFERENCE_CLASS, false, null).getDeclaredField("instrumentation").get(null);
    });
    private static final DcLazy<Unsafe> lazy$unsafe = new DcLazy<>(() -> {
        Field theUnsafeField = Unsafe.class.getDeclaredField("theUnsafe");
        theUnsafeField.setAccessible(true);
        return (Unsafe) theUnsafeField.get(null);
    });
    private static final DcLazy<MethodHandles.Lookup> lazy$lookup = new DcLazy<>(() -> {
        Field implLookupField = MethodHandles.Lookup.class.getDeclaredField("IMPL_LOOKUP");
        return (MethodHandles.Lookup) unsafe().getObject(unsafe().staticFieldBase(implLookupField), unsafe().staticFieldOffset(implLookupField));
    });

    private JvmHacker() {
        throw new UnsupportedOperationException();
    }

    private static <E extends Throwable, R> R throwImpl(Throwable e) throws E {
        throw (E) e;
    }

    private static void attachAgent() throws Throwable {
        File agentFile = createTempJar();
        String processId = getProcessId();
        try {
            attachSelf(processId, agentFile);
            return;
        } catch (Throwable ignored) {
        }
        try {
            runAttach(processId, agentFile);
            return;
        } catch (Throwable ignored) {
        }
        throw new IllegalStateException("Failed to attach agent hacker");
    }

    private static File createTempJar() throws Throwable {
        File jarFile = File.createTempFile("acj_agent_payload_", ".jar");
        jarFile.deleteOnExit();
        try (OutputStream out = new FileOutputStream(jarFile)) {
            out.write(AGENT_PAYLOAD_FILE);
        }
        return jarFile;
    }

    private static Class<?> getVirtualMachine() throws Throwable {
        try {
            return Class.forName("com.sun.tools.attach.VirtualMachine");
        } catch (ClassNotFoundException ignored) {
        }
        try {
            return Class.forName("com.ibm.tools.attach.VirtualMachine");
        } catch (ClassNotFoundException ignored) {
        }
        List<File> possibleToolsJars = scanPossibleToolsJars();
        URL[] urls = new URL[possibleToolsJars.size()];
        for (int i = 0; i < possibleToolsJars.size(); i++) {
            urls[i] = possibleToolsJars.get(i).toURI().toURL();
        }
        URLClassLoader cl = new URLClassLoader(urls, null);
        try {
            return Class.forName("com.sun.tools.attach.VirtualMachine", true, cl);
        } catch (ClassNotFoundException ignored) {
        }
        try {
            return Class.forName("com.ibm.tools.attach.VirtualMachine", true, cl);
        } catch (ClassNotFoundException ignored) {
        }
        throw new ClassNotFoundException("com.sun.tools.attach.VirtualMachine and com.ibm.tools.attach.VirtualMachine");
    }

    private static String getProcessId() {
        try {
            return getProcessIdFromProcessHandle();
        } catch (Throwable ignored) {
        }
        return getProcessIdFromMx();
    }

    private static String getProcessIdFromProcessHandle() throws Throwable {
        Class<?> processHandleClass = Class.forName("java.lang.ProcessHandle");
        Method currentMethod = processHandleClass.getMethod("current");
        Method pidMethod = processHandleClass.getMethod("pid");
        return pidMethod.invoke(currentMethod.invoke(null)).toString();
    }

    private static String getProcessIdFromMx() {
        String runtimeName = ManagementFactory.getRuntimeMXBean().getName();
        int processIdIndex = runtimeName.indexOf('@');
        if (processIdIndex == -1) {
            throw new IllegalStateException("Cannot extract process id from runtime management bean");
        } else {
            return runtimeName.substring(0, processIdIndex);
        }
    }

    private static List<File> scanPossibleToolsJars() {
        String javaHome = System.getProperty("java.home");
        String[] possibleToolsJars = new String[]{
                "../lib/tools.jar",
                "lib/tools.jar",
                "../Classes/classes.jar"
        };
        List<File> result = new ArrayList<>(1);
        for (String possibleToolsJar : possibleToolsJars) {
            File possibleToolsJarFile = new File(javaHome, possibleToolsJar);
            if (possibleToolsJarFile.isFile()) {
                result.add(possibleToolsJarFile);
            }
        }
        return result;
    }

    private static void attachSelf(String processId, File agentFile) throws Throwable {
        Class<?> virtualMachineClass = getVirtualMachine();
        Method attachMethod = virtualMachineClass.getMethod("attach", String.class);
        Object virtualMachine = attachMethod.invoke(null, processId);
        try {
            Method loadAgentMethod = virtualMachineClass.getMethod("loadAgent", String.class, String.class);
            loadAgentMethod.invoke(virtualMachine, agentFile.getAbsolutePath(), "");
        } finally {
            Method detachMethod = virtualMachineClass.getMethod("detach");
            detachMethod.invoke(virtualMachine);
        }
    }

    private static void runAttach(String processId, File agentFile) throws Throwable {
        String javaHome = System.getProperty("java.home");
        StringJoiner classPathJoiner = new StringJoiner(":");
        for (File possibleToolsJar : scanPossibleToolsJars()) {
            classPathJoiner.add(possibleToolsJar.getAbsolutePath());
        }
        classPathJoiner.add(agentFile.getAbsolutePath());
        if (new ProcessBuilder(javaHome + "/bin/" + (File.separatorChar == '\\' ? "java.exe" : "java"), "-cp", classPathJoiner.toString(), AGENT_MAIN_CLASS, processId, agentFile.getAbsolutePath()).inheritIO().start().waitFor() != 0) {
            throw new IllegalStateException("Could not self-attach to current VM using external process");
        }
    }

    private static class DcLazy<T> {
        private static final Object NO_INIT = new Object();
        private final Provider<T> provider;
        private volatile T object = (T) NO_INIT;

        public DcLazy(Provider<T> provider) {
            this.provider = provider;
        }

        public T get() {
            T result = object;
            if (result == NO_INIT) {
                synchronized (this) {
                    result = object;
                    if (result == NO_INIT) {
                        try {
                            result = provider.provide();
                        } catch (Throwable e) {
                            return throwImpl(e);
                        }
                        object = result;
                    }
                }
            }
            return result;
        }

        @FunctionalInterface
        private interface Provider<T> {
            T provide() throws Throwable;
        }
    }
}
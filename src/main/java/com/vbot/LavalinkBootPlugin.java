package com.vbot;

import org.springframework.stereotype.Service;

@Service
public class LavalinkBootPlugin {

    public LavalinkBootPlugin() {
        System.out.println("[VBot] Plugin loaded, launching services...");
        startPOTProvider();
        startAutoLogin();
    }

    private void startPOTProvider() {
        try {
            ProcessBuilder pb = new ProcessBuilder(
                "/home/container/deno", "run",
                "--allow-env", "--allow-net",
                "--allow-ffi=/home/container/pot-provider/server/node_modules",
                "--allow-read=/home/container/pot-provider/server/node_modules",
                "--allow-read=/home/container/pot-provider/server",
                "/home/container/pot-provider/server/src/main.ts",
                "--host", "0.0.0.0",
                "--port", "4416"
            );
            pb.redirectErrorStream(true);
            pb.redirectOutput(new java.io.File("/home/container/pot.log"));
            Process p = pb.start();
            System.out.println("[VBot] POT started (PID " + p.pid() + ")");
        } catch (Exception e) {
            System.err.println("[VBot] POT failed: " + e.getMessage());
        }
    }

    private void startAutoLogin() {
        try {
            ProcessBuilder pb = new ProcessBuilder(
                "/home/container/py311/bin/python3",
                "-u",
                "/home/container/optik_login.py"
            );
            pb.redirectErrorStream(true);
            pb.redirectOutput(new java.io.File("/home/container/login.log"));
            Process p = pb.start();
            System.out.println("[VBot] Auto-Login started (PID " + p.pid() + ")");
        } catch (Exception e) {
            System.err.println("[VBot] Auto-Login failed: " + e.getMessage());
        }
    }
}

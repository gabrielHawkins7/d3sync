package com.d3sync;

public class Utils {
    
    public static record  SSHConfig(String host, String username, String password) {

    }
    public static record ScheduledSync(String sourceDir, String targetDir) {
    }

    public static record SyncConfig(SSHConfig ssh, String sourceDir, String targetDir){

    }
    
}

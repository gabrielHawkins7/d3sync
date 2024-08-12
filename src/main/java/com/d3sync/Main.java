package com.d3sync;

import com.d3sync.Utils.SSHConfig;
import com.d3sync.Utils.SyncConfig;


public class Main {

    public static void main(String[] args) throws Exception {
        String localDir = "/Users/gabrielhawkins/Documents";
        String remoteDir = "F:\\COLDSTORE\\Laptop Backup\\Documents";
        SSHConfig sshConfig = new SSHConfig("HOMESERVER", "Gabe", "0258");
        SyncFolder.sync(new SyncConfig(sshConfig, localDir, remoteDir));
    }
}

package com.d3sync;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import net.schmizz.sshj.SSHClient;
import net.schmizz.sshj.sftp.RemoteResourceInfo;
import net.schmizz.sshj.sftp.SFTPClient;
import net.schmizz.sshj.transport.verification.PromiscuousVerifier;


public class Main {
    public static void main(String[] args) throws IOException {
        System.out.println("Hello world");

        syncDirectory("","F:\\COLDSTORE\\Laptop Backup\\Documents", new SSHConfig("HOMESERVER", "Gabe", "0258"));

        
    }


    static void syncDirectory(String sourceDir, String targetDir, SSHConfig config ) throws IOException{

        try (SSHClient ssh = new SSHClient()) {
            ssh.addHostKeyVerifier(new PromiscuousVerifier()); // Use cautiously; better to verify keys properly in production
            ssh.connect(config.host);
            ssh.authPassword(config.username, config.password); // or use ssh.authPublickey(username, "path_to_private_key");

            try (SFTPClient sftp = ssh.newSFTPClient()) {
                List<RemoteResourceInfo> files = sftp.ls(targetDir);
                for (RemoteResourceInfo file : files) {
                    System.out.println(file.getName());
                }
            }
            ssh.disconnect();
        }


        

    }


}

/**
 * InnerMain
 */
class SSHConfig {
    String host = "HOMESERVER";
    String username = "Gabe";
    String password = "0258";
    SSHConfig(String host, String username, String password){
        this.host = host;
        this.username = username;
        this.password = password;
    }
}
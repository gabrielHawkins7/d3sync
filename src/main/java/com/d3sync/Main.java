package com.d3sync;

import java.io.BufferedReader;
import java.io.Console;
import java.io.File;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.ArrayUtils;

import com.github.fracpete.processoutput4j.output.CollectingProcessOutput;
import com.github.fracpete.processoutput4j.output.ConsoleOutputProcessOutput;
import com.github.fracpete.rsync4j.RSync;
import com.github.fracpete.rsync4j.Ssh;
import com.github.fracpete.rsync4j.SshPass;

import net.schmizz.sshj.SSHClient;
import net.schmizz.sshj.common.IOUtils;
import net.schmizz.sshj.connection.channel.direct.Session;
import net.schmizz.sshj.connection.channel.direct.Session.Command;
import net.schmizz.sshj.sftp.RemoteResourceInfo;
import net.schmizz.sshj.sftp.SFTPClient;
import net.schmizz.sshj.transport.verification.PromiscuousVerifier;
import net.schmizz.sshj.xfer.FileSystemFile;


public class Main {

    private static final Console con = System.console();
    public static void main(String[] args) throws Exception {
        System.out.println("Hello world");

        syncDirectory("/Users/gabrielhawkins/Documents","F:\\COLDSTORE\\Laptop Backup\\Documents", new SSHConfig("HOMESERVER", "Gabe", "0258"));

        

    }


    static void syncDirectory(String sourceDir, String targetDir, SSHConfig config ) throws Exception{

        SSHClient ssh = new SSHClient();
        try {
            ssh.addHostKeyVerifier(new PromiscuousVerifier()); // Use cautiously; better to verify keys properly in production
            ssh.connect(config.host);
            ssh.authPassword(config.username, config.password); // or use ssh.authPublickey(username, "path_to_private_key");

            // Using SFTP to list files
            try (SFTPClient sftp = ssh.newSFTPClient()) {
                List<RemoteResourceInfo> target = sftp.ls(targetDir);
                List<File> source = Arrays.asList(new File(sourceDir).listFiles());

                for(RemoteResourceInfo t : target){
                    if(t.isRegularFile() && )

                }


                // sftp.put(new FileSystemFile(sourceDir), targetDir);
                // sftp.close();

            }

        }finally{
            ssh.disconnect();
        }

    }

    private boolean filecomp(RemoteResourceInfo a, File b){
        return a.getName().equals(b.getName());
    }
    private boolean hasFile(RemoteResourceInfo a, List<File> b){
        for(File f : b){
            if(a.getName().equals(f.getName())){
                return true;
            }
        }
        return false;
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
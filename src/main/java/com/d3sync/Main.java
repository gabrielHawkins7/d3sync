package com.d3sync;

import java.io.BufferedReader;
import java.io.Console;
import java.io.File;
import java.io.IOException;
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

        //syncDirectory("/Users/gabrielhawkins/Documents","F:\\COLDSTORE\\Laptop Backup\\Documents", new SSHConfig("HOMESERVER", "Gabe", "0258"));

        try (SSHClient ssh = setupSshClient("HOMESERVER", "Gabe", "0258")) {
            SFTPClient sftp = setupSftpClient(ssh);
            File localDir = new File("/Users/gabrielhawkins/Documents");
            String remoteDir = "F:\\COLDSTORE\\Laptop Backup\\Documents";
            uploadDirectoryRecursively(sftp, localDir, remoteDir);
            sftp.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static SSHClient setupSshClient(String hostname, String username, String password) throws IOException {
        SSHClient ssh = new SSHClient();
        ssh.loadKnownHosts();
        ssh.connect(hostname);
        ssh.authPassword(username, password);
        return ssh;
    }
    
    public static SFTPClient setupSftpClient(SSHClient ssh) throws IOException {
        return ssh.newSFTPClient();
    }


    public static void uploadDirectoryRecursively(SFTPClient sftp, File localDir, String remoteDir) throws IOException {
        if (!localDir.isDirectory()) {
            throw new IllegalArgumentException("Not a directory");
        }

        long[] stats = new long[2];
    
        List<RemoteResourceInfo> remoteFiles = sftp.ls(remoteDir);
    
        for (File localFile : localDir.listFiles()) {
            String remoteFilePath = remoteDir + "/" + localFile.getName();
            boolean exists = remoteFiles.stream().anyMatch(f -> f.getName().equals(localFile.getName()));
    
            if (localFile.isDirectory()) {
                System.out.println("Uploading Folder " + localFile.getAbsolutePath() + " to " + remoteDir + " " + printSpeed(stats[0], stats[1]) + "mbs");
                if (!exists) {
                    sftp.mkdir(remoteFilePath);
                }
                uploadDirectoryRecursively(sftp, localFile, remoteFilePath);
            } else {
                if (!exists) {
                    System.out.println("Uploading File " + localFile.getAbsolutePath() + " to " + remoteDir + " " +printSpeed(stats[0], stats[1]) + "mbs");
                    long startTime = System.currentTimeMillis();
                    sftp.put(localFile.getAbsolutePath(), remoteFilePath);
                    long endTime = System.currentTimeMillis();
                    long duration = endTime - startTime;
                    long fileSize = localFile.length();
                    stats[0] += fileSize; // Total data transferred
                    stats[1] += duration; // Total time in milliseconds
                }
            }
        }
    }
    private static String printSpeed(Long data, long time){
        return String.format("%.2f", (data / 1048576.0) / (time / 1000.0));
    }
    public static float round2(float number, int scale) {
        int pow = 10;
        for (int i = 1; i < scale; i++)
            pow *= 10;
        float tmp = number * pow;
        return ( (float) ( (int) ((tmp - (int) tmp) >= 0.5f ? tmp + 1 : tmp) ) ) / pow;
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
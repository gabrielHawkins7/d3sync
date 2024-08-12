package com.d3sync;

import java.io.BufferedReader;
import java.io.Console;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.codec.binary.StringUtils;
import org.apache.commons.codec.digest.DigestUtils;

import com.d3sync.Utils.PLATFORM;
import com.d3sync.Utils.SyncConfig;

import net.schmizz.sshj.SSHClient;
import net.schmizz.sshj.common.IOUtils;
import net.schmizz.sshj.connection.channel.direct.Session;
import net.schmizz.sshj.connection.channel.direct.Session.Command;
import net.schmizz.sshj.sftp.RemoteResourceInfo;
import net.schmizz.sshj.sftp.SFTPClient;

public class SyncFolder {

    private static final Console con = System.console();

    public static void sync(SyncConfig config){
        try (SSHClient ssh = setupSshClient(config.ssh().host(), config.ssh().username(), config.ssh().password())) {
            SFTPClient sftp = setupSftpClient(ssh);
            File localDir = new File(config.sourceDir());
            String remoteDir = config.targetDir();
            uploadDirectoryRecursively(sftp, localDir, remoteDir);
            sftp.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void verify(SyncConfig config){
        try (SSHClient ssh = setupSshClient(config.ssh().host(), config.ssh().username(), config.ssh().password())) {
            SFTPClient sftp = setupSftpClient(ssh);
            File localDir = new File(config.sourceDir());
            String remoteDir = config.targetDir();
            verifyRecursivly(sftp,ssh, localDir, remoteDir, config.targetPlatform());
            sftp.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void verifyRecursivly(SFTPClient sftp, SSHClient ssh,  File localDir, String remoteDir, PLATFORM targetPlatform) throws IOException {
        if (!localDir.isDirectory()) {
            throw new IllegalArgumentException("Not a directory");
        }
        long[] stats = new long[2];

        List<RemoteResourceInfo> remoteFiles = sftp.ls(remoteDir);
    
        for (File localFile : localDir.listFiles()) {
            String remoteFilePath = remoteDir + "\\" + localFile.getName();
            boolean exists = remoteFiles.stream().anyMatch(f -> f.getName().equals(localFile.getName()));
    
            if (localFile.isDirectory()) {
                //System.out.println("Uploading Folder " + localFile.getAbsolutePath() + " to " + remoteDir + " " + printSpeed(stats[0], stats[1]) + "mbs");
                if (exists) {
                    verifyRecursivly(sftp,ssh, localFile, remoteFilePath, targetPlatform);
                }
            } else {
                if (exists) {
                    System.out.println("File: " + localFile.getAbsolutePath());

                    String local = calculateLocalFileMD5(localFile);
                    String remote = getRemoteFileMD5(ssh, remoteFilePath, targetPlatform);
                    String response = "";
                    if(local.equals(remote) == false){
                        System.out.println("Hash of " + localFile.getName() + " Does of not match");
                        try (Session session = ssh.startSession()) {
                            //Command cmd = session.exec("md5sum " + "\"" + remoteFilePath + "\"" + " | cut -d ' ' -f1");
                            Command cmd; 
                            if(targetPlatform.equals(PLATFORM.WINDOWS)){
                                cmd = session.exec("del /f /q /a " + "\"" + remoteFilePath + "\"");
                            }else{
                                cmd = session.exec("rm -r " + "\"" + remoteFilePath + "\"");
                            }
                            cmd.join(10, TimeUnit.SECONDS); // Wait for the command to complete
                            
                            // Check for errors
                            String stderr = IOUtils.readFully(cmd.getErrorStream()).toString();
                            if (!stderr.isEmpty()) {
                                System.out.println("Error: " + stderr);
                            }
                
                            response = IOUtils.readFully(cmd.getInputStream()).toString().trim();
                        } 
                        System.out.println(response);
                    }
                }
            }
        }
    }

    
    private static String calculateLocalFileMD5(File file) throws IOException {
        try (FileInputStream fis = new FileInputStream(file)) {
            return DigestUtils.md5Hex(fis);
        }
    }
    public static String getRemoteFileMD5(SSHClient ssh, String remoteFilePath, PLATFORM targetPlatform) throws IOException {
        if(targetPlatform.equals(PLATFORM.WINDOWS)){
            String response = "";
            try (Session session = ssh.startSession()) {
                //Command cmd = session.exec("md5sum " + "\"" + remoteFilePath + "\"" + " | cut -d ' ' -f1");
                Command cmd = session.exec("certutil -hashfile " + "\"" + remoteFilePath + "\"" + " MD5" + "| findstr /V /C:\"CertUtil\" /C:\"hash\"");
                cmd.join(10, TimeUnit.SECONDS); // Wait for the command to complete
                // Check for errors
                String stderr = IOUtils.readFully(cmd.getErrorStream()).toString();
                if (!stderr.isEmpty()) {
                    System.out.println("Error: " + stderr);
                }
                response = IOUtils.readFully(cmd.getInputStream()).toString().trim();
            } 
            return response;
        }else{
            return null;
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
                if (!exists && !isSystemFile(localFile.getName())) {
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

    private static boolean isSystemFile(String file){
        String[] sysStrings = {".localized", ".DS_Store"};
        return Arrays.stream(sysStrings).anyMatch(file::equals);
    }
}



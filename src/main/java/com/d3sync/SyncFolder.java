package com.d3sync;

import java.io.File;
import java.io.IOException;
import java.util.List;

import com.d3sync.Utils.SyncConfig;

import net.schmizz.sshj.SSHClient;
import net.schmizz.sshj.sftp.RemoteResourceInfo;
import net.schmizz.sshj.sftp.SFTPClient;

public class SyncFolder {
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



package com.d3sync;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.TemporalAmount;
import java.util.ArrayList;

import com.d3sync.Utils.PLATFORM;
import com.d3sync.Utils.SSHConfig;
import com.d3sync.Utils.ScheduledSync;
import com.d3sync.Utils.SyncConfig;
import com.d3sync.Utils.Time;


public class SyncController {
    static ScheduledSync sync1;

    SyncController(String[] args) throws Exception {
        String localDir = "/Users/gabrielhawkins/Documents";
        String remoteDir = "F:\\COLDSTORE\\Laptop Backup\\Documents";
        SSHConfig sshConfig = new SSHConfig("HOMESERVER", "Gabe", "0258");
        SyncConfig syncConfig = new SyncConfig(sshConfig, localDir, remoteDir, PLATFORM.WINDOWS);

        //SyncFolder.sync(syncConfig);
        SyncFolder.verify(syncConfig);

        LocalDateTime myObj = LocalDateTime.now();
        System.out.println(myObj);

        LocalDateTime syncTime1 = LocalDateTime.of(2024, 8, 12, 15, 36, 40);

        while(myObj.compareTo(syncTime1) <= 0){
            myObj = LocalDateTime.now();
            System.out.println(myObj);
            Thread.sleep(5000);
        }

         sync1 = new ScheduledSync("Sync 1", syncConfig, LocalDateTime.now(), new Time(0, 0, 0, 30));

        while (true) {
            if(LocalDateTime.now().compareTo(addTime(sync1.startTime(), sync1.duration())) >= 0){
                System.out.println("Synching: " + sync1.name() + " at " + LocalDateTime.now());
                sync1 = sync1.updateTime(LocalDateTime.now());
                Thread work = new Thread(()->{
                    SyncFolder.sync(sync1.sync());
                });
                work.start();
                work.join(10000);
                if(work.isAlive()) work.interrupt();
             }
            Thread.sleep(1000);
            //System.out.println();
        }
    }

    private static LocalDateTime addTime(LocalDateTime time, Time duration){
            LocalDateTime n = 
                time.plusDays(duration.days()).
                plusHours(duration.hours()).
                plusMinutes(duration.minutes()).
                plusSeconds(duration.seconds());
        return n;
    }
}

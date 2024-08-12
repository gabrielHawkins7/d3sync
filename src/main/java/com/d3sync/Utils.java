package com.d3sync;

import java.time.LocalDateTime;
import java.time.LocalTime;

public class Utils {
    
    public record  SSHConfig(String host, String username, String password) {

    }
    public record ScheduledSync(String name, SyncConfig sync, LocalDateTime startTime, Time duration) {
        public ScheduledSync updateTime(LocalDateTime newTime){
            return new ScheduledSync(name, sync, newTime, duration);
        }
        public String toString(){
            return "" + name + " StartTime: " + startTime + " Every: " + duration;
        }
    }

    public record SyncConfig(SSHConfig ssh, String sourceDir, String targetDir, PLATFORM targetPlatform){

    }
    public record Time(int days, int hours, int minutes, int seconds){

    }

    enum Strings {
        DELETE("del /f /q /a ");
        private final String text;
        Strings(final String text) {
            this.text = text;
        }
        @Override
        public String toString() {
            return text;
        }
    }
    enum PLATFORM{
        WINDOWS, MAC, LINUX;
    }
}

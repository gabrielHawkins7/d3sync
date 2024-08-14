package com.d3sync;

import java.io.File;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class Utils {
    
    public record SyncConfig(File source, File destination, TARGET TARGET, PLATFORM PLATFORM){}
    public record ScheduledSync(String name, SyncConfig syncConfig, LocalDateTime startTime, boolean active){
        @Override
        public final String toString() {
            return "" + name + " " + syncConfig.source.getName() + " -> " + syncConfig.destination.getName() + " " + startTime + "Active: " + active;
        }
    }
    public record Time(int day, int hour, int minute, int second){}

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

    enum TARGET{
        LOCAL, NET;
    }
}

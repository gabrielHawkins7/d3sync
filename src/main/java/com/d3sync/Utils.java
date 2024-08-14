package com.d3sync;

import java.io.File;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class Utils {
    
    public record SyncConfig(File source, File destination, TRANSFER TRANSFER){}
    public record ScheduledSync(String name, SyncConfig syncConfig, LocalTime start, LocalTime end, boolean active){
        @Override
        public final String toString() {
            //return"";
            return name + " /" + syncConfig.source.getName() + " -> /" + syncConfig.destination.getName() + " Start:" + start.format(DateTimeFormatter.ofPattern("hh:mm a")) + " End:"+ end.format(DateTimeFormatter.ofPattern("hh:mm a")) +" Active: " + active;
        }
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

    enum TRANSFER{
        LOCAL, NET;
    }
}

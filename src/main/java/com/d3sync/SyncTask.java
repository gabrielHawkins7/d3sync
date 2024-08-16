package com.d3sync;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;


import net.schmizz.sshj.connection.channel.Window.Local;

public class SyncTask implements Serializable{
    String name;
    File source;
    File target;
    LocalTime start;
    LocalTime end;
    boolean active;
    double percent;
    PLATFORM PLATFORM;
    TRANSFER TRANSFER;
    Status staus;

    enum PLATFORM{
        WINDOWS, MAC, LINUX;
    }

    enum TRANSFER{
        LOCAL, NET;
    }
    

    SyncTask(
        String name,
        File source,
        File target,
        LocalTime start,
        LocalTime end,
        boolean active,
        double percent,
        TRANSFER TRANSFER
    ){
        this.name = name;
        this.source = source;
        this.target = target;
        this.start = start;
        this.end = end;
        this.active = active;
        this.percent = percent;
        this.TRANSFER = TRANSFER;
        staus = new Status(false, "New");
    }

    public void calcPercent(){
        try {
            percent = Utils.calculateFilePercentage(source, target);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


    

    public void validateItems(){
        
    }

    public void checkStatus(){
        if(!source.exists()){
            staus.isFine = false;
            staus.message = "Source Does Not Exist";
        }else if(!target.exists()){
            staus.isFine = false;
            staus.message = "Target Does Noe Exist";
        }else if(!source.canRead()){
            staus.isFine = false;
            staus.message = "Unable to read from source";
        }else if(!target.canWrite() && !target.canRead()){
            staus.isFine = false;
            staus.message = "Unable to write to target";
        }else{
            staus.isFine = true;
            staus.message = "Good to go";
        }

        
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public LocalTime getStart() {
        return start;
    }
    public void setStart(LocalTime start) {
        this.start = start;
    }
    public LocalTime getEnd() {
        return end;
    }
    public void setEnd(LocalTime end) {
        this.end = end;
    }
    public boolean isActive() {
        return active;
    }
    public void setActive(boolean active) {
        this.active = active;
    }
    public double getPercent() {
        return percent;
    }
    public void setPercent(double percent) {
        this.percent = percent;
    }
    public File getSource() {
        return source;
    }
    public void setSource(File source) {
        this.source = source;
    }
    public File getTarget() {
        return target;
    }
    public void setTarget(File target) {
        this.target = target;
    }

    @Override
        public final String toString() {
            return 
            name + 
            " " + String.format(" %.2f",percent) +
            "% " + source.getParent() + "/" + source.getName() + " -> /" +
            target.getParent() + "/" + target.getName() + " " +
            "Start:" + start.format(DateTimeFormatter.ofPattern("hh:mm a")) + 
            " End:" + start.format(DateTimeFormatter.ofPattern("hh:mm a")) + 
            " Active: " + active
            ;

        }
}
class Status implements Serializable{
    boolean isFine;
    String message;
    Status(boolean isFine, String message){
        this.isFine = isFine;   
        this.message = message;
    }
}

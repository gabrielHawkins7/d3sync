package com.d3sync;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;

import com.d3sync.LogMessage.TYPE;
import com.d3sync.Observables.Observable;
import com.d3sync.Observables.ObservableArrayList;

public class Controller{

    HashMap<String, Object> ui = new HashMap<>();
    DefaultListModel<LogMessage> statusList = new DefaultListModel<>();
    DefaultListModel<SyncTask> scheduleList = new DefaultListModel<>();
    info info = new info();
    ViewController viewController = new ViewController(this);
    Listeners listeners = new Listeners(this);





    

    public void addTask(SyncTask task){
        scheduleList.addElement(task);
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("Data.d3"))) {
                oos.writeObject(Arrays.asList(scheduleList.toArray()));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    public void replaceTask(int index, SyncTask newTask){
        scheduleList.set(index, newTask);
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("Data.d3"))) {
            oos.writeObject(Arrays.asList(scheduleList.toArray()));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    public void deleteTask(int index){
        scheduleList.remove(index);
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("Data.d3"))) {
            oos.writeObject(Arrays.asList(scheduleList.toArray()));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void loadTasks() throws ClassNotFoundException, IOException{
        
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("Data.d3"))) {
            List<SyncTask> list =  (List<SyncTask>) ois.readObject();
            for(SyncTask i : list){
                scheduleList.addElement(i);
            }
        }
    }


    public void logMessage(LogMessage message){
        statusList.addElement(message);
    }

    public void logError(Exception e){
        statusList.addElement(new LogMessage(TYPE.ERROR, e.getLocalizedMessage()));
    }

    

    public void periodicUpdate(){
        for(int i = 0; i < scheduleList.size(); i++){
            SyncTask task = scheduleList.get(i);
            task.checkStatus();
            task.calcPercent();
            
        }
    }






    
}

class LogMessage{
    static enum TYPE { INFO("INFO"), MESSAGE("MESSAGE"), STATUS("STATUS"), ERROR("ERROR");
        private final String text;
        TYPE(String string) {
            this.text = string;
        }
        @Override
        public String toString() {
            return text;
        }
    };
    private TYPE TYPE;
    private String log;
    private LocalTime time;
    LogMessage(TYPE TYPE, String log){
        this.TYPE = TYPE;
        this.log = log;
        time = LocalTime.now();
    }
    @Override
    public String toString() {
        return "[D3] [" + TYPE + "] @ " + time.format(DateTimeFormatter.ofPattern("hh:mm a")) + " : " + log;
    }

}

class SyncException extends Exception{
    public SyncException(String e){
        super(e);
    }
}

class info{
    Observable<Double> transferSpeed = new Observable<Double>(0.0);
    Observable<Boolean> isSyncing = new Observable<>(false);

}

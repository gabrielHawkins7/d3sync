package com.d3sync;

import java.awt.BorderLayout;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

import org.apache.commons.codec.digest.DigestUtils;

import static com.d3sync.Utils.*;

import com.d3sync.LogMessage.TYPE;
import com.d3sync.Observables.ObservableArray;
import com.d3sync.SyncTask.TRANSFER;
import com.formdev.flatlaf.FlatDarculaLaf;
import com.formdev.flatlaf.FlatLightLaf;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException, UnsupportedLookAndFeelException, ClassNotFoundException {
        File f = new File("/Users/gabrielhawkins/Documents/");
        File s = new File("/Users/gabrielhawkins/Documents/");

        FlatLightLaf.setup();
        UIManager.setLookAndFeel( new FlatDarculaLaf() );

        Controller controller = new Controller();

        // controller.addTask(new SyncTask(
        //     "Sync1",
        //     f,
        //     s,
        //     LocalTime.of(0, 30),
        //     LocalTime.of(8, 30),
        //     false,
        //     0.0,
        //     TRANSFER.LOCAL
        // ));

        
        controller.statusList.addElement(new LogMessage(LogMessage.TYPE.INFO, "Processing..."));
        controller.loadTasks();

        new Thread(()->{
            Timer timer = new Timer("Periodic Update");
            TimerTask updateTask = new TimerTask() {
                @Override
                public void run() {
                    controller.periodicUpdate();
                    SwingUtilities.invokeLater(()->{
                        controller.statusList.addElement(new LogMessage(TYPE.INFO, "Validating Sync"));
                    });
                }
            };
            timer.schedule(updateTask, 0, Duration.ofMinutes(5).toMillis());

            TimerTask syncCheck = new TimerTask() {
                @Override
                public void run() {
                   for(int i = 0; i < controller.scheduleList.size(); i++){
                        SyncTask task = controller.scheduleList.get(i);
                        if (task.start.isAfter(task.end)) {
                            if (LocalTime.now().isAfter(task.start) || LocalTime.now().isBefore(task.end)) {
                                System.out.println("Syncing: " + task.name);
                                while (true) {
                                    
                                }
                            }
                        } else {
                            if (LocalTime.now().isAfter(task.start) && LocalTime.now().isBefore(task.end)) {
                                System.out.println("Syncing: " + task.name);
                                while (true) {
                                    
                                }
                            }
                        }
                   }
                }
            };
            timer.schedule(syncCheck, 0, 150);


        }).start();

        

    }
}

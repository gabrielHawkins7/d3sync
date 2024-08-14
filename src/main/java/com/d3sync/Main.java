package com.d3sync;

import java.awt.BorderLayout;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDateTime;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

import org.apache.commons.codec.digest.DigestUtils;

import static com.d3sync.Utils.*;
import com.d3sync.Observables.ObservableArray;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {
        File f = new File("/Users/gabrielhawkins/Documents");
        File s = new File("/Users/gabrielhawkins/Downloads/Bakcup");

        Controller controller = new Controller();

        controller.statusList.addElement("This is a test");
        controller.scheduleList.addElement(new ScheduledSync("Sync1", new SyncConfig(f,s, TARGET.LOCAL, PLATFORM.MAC), LocalDateTime.now(), false));


    }
}

package com.d3sync;

import java.awt.BorderLayout;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

import org.apache.commons.codec.digest.DigestUtils;

import static com.d3sync.Utils.*;

import com.d3sync.Utils.PLATFORM;
import com.d3sync.Utils.ScheduledSync;
import com.d3sync.Utils.SyncConfig;
import com.d3sync.Utils.TRANSFER;
import com.d3sync.Observables.ObservableArray;
import com.formdev.flatlaf.FlatDarculaLaf;
import com.formdev.flatlaf.FlatLightLaf;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException, UnsupportedLookAndFeelException {
        File f = new File("/Users/gabrielhawkins/Documents");
        File s = new File("/Users/gabrielhawkins/Downloads/Bakcup");

        FlatLightLaf.setup();
        UIManager.setLookAndFeel( new FlatDarculaLaf() );

        Controller controller = new Controller();

        controller.statusList.addElement("This is a test");
        controller.scheduleList.addElement(new ScheduledSync("Sync1", new SyncConfig(f,s, TRANSFER.LOCAL),LocalTime.of(0, 30), LocalTime.of(8, 0), false));


    }
}

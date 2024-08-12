package com.d3sync;

import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

import com.formdev.flatlaf.FlatLightLaf;

public class Main {
    public static void main(String[] args) {

        FlatLightLaf.setup();


        JFrame frame = new JFrame("D3 Sync");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


        String[] objects = {
            "Documents Sync Start Time: 12/30/24 Duration: 00:30:00"
        };

        JList list = new JList<>(objects);
        list.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        list.setLayoutOrientation(JList.HORIZONTAL_WRAP);
        list.setVisibleRowCount(-1);
        JScrollPane listScroller = new JScrollPane(list);
        frame.getContentPane().add(listScroller);


        frame.setSize(600, 600);
        frame.setVisible(true);
    }
}

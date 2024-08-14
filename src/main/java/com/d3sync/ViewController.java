package com.d3sync;

import java.awt.BorderLayout;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

import com.d3sync.Utils.ScheduledSync;

public class ViewController {
    Controller  controller;
    JFrame root;

    JSplitPane mainPanel;
    JPanel bottomPanel;

    ViewController(Controller controller){
        this.controller = controller;
       
        root = new JFrame();
        root.setSize(600, 800);
        root.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        initUI();
        root.setVisible(true);

    }

    public void initUI(){
        initMainView();
        initbottomPanel();
    }

    private void initMainView(){

        mainPanel = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        root.add(mainPanel, BorderLayout.CENTER);
        initSchedule();
        initStaus();
    }
    private void initStaus(){
        JList<String> status_JList = new JList<>(controller.statusList);
        controller.ui.put("status_JList", status_JList);
        JScrollPane status_JScrollPane = new JScrollPane(status_JList);
        controller.ui.put("status_JScrollPane", status_JScrollPane);
        mainPanel.add(status_JScrollPane);
    }
    private void initSchedule(){
        JPanel schedulePanel = new JPanel();
        schedulePanel.setLayout(new BoxLayout(schedulePanel, BoxLayout.Y_AXIS));

        JList<ScheduledSync> schedule_JList = new JList<>(controller.scheduleList);
        controller.ui.put("schedule_JList", schedule_JList);
        JScrollPane schedule_JScrollPane = new JScrollPane(schedule_JList);
        controller.ui.put("schedule_JScrollPane", schedule_JScrollPane);
        schedulePanel.add(schedule_JScrollPane);

        JPanel scheduleButtons = new JPanel();
        scheduleButtons.setLayout(new BoxLayout(scheduleButtons, BoxLayout.X_AXIS));

        JButton schedule_Add_JButton = new JButton("Add");
        controller.ui.put("schedule_Add_JButton", schedule_Add_JButton);
        JButton schedule_Edit_JButton = new JButton("Edit");
        controller.ui.put("schedule_Edit_JButton", schedule_Edit_JButton);
        scheduleButtons.add(schedule_Add_JButton);
        scheduleButtons.add(schedule_Edit_JButton);

        schedulePanel.add(scheduleButtons);

        mainPanel.add(schedulePanel);
    }
    private void initbottomPanel(){
        bottomPanel = new JPanel();
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.X_AXIS));
        root.add(bottomPanel, BorderLayout.SOUTH);
        bottomPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        initBottomBar();
    }
    private void initBottomBar(){
        JLabel speed_JLabel = new JLabel("Transfer Speed: ", SwingConstants.LEFT);
        controller.ui.put("speed_JLabel", speed_JLabel);
        //speed_JLabel.setBorder(new EmptyBorder(5, 0, 5, 5));
        bottomPanel.add(speed_JLabel);
    }


    public static void showPopup(JFrame root){
        JDialog d = new JDialog(root, "dialog Box");
 
        // create a label
        JLabel l = new JLabel("this is a dialog box");

        d.add(l);

        // setsize of dialog
        d.setSize(100, 100);

        // set visibility of dialog
        d.setVisible(true);
    }
}

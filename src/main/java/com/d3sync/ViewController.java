package com.d3sync;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FileDialog;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.SpringLayout;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.text.BoxView;

import org.kordamp.ikonli.Ikon;
import org.kordamp.ikonli.Ikonli;
import org.kordamp.ikonli.feather.Feather;
import org.kordamp.ikonli.swing.FontIcon;


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
        root.pack();

    }

    public void initUI(){
        initToolBar();
        initMainView();
        initbottomPanel();
    }
    private void initToolBar(){
        JToolBar toolBar = new JToolBar();
        toolBar.setBorder(new EmptyBorder(10, 10, 10, 10));
        JLabel syncLabel = new JLabel("Sync: ");
        JCheckBox sync = new JCheckBox();
        sync.setSelected(false);
        sync.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(sync.isSelected() == true){
                    controller.info.isSyncing.setValue(true);
                }else{
                    controller.info.isSyncing.setValue(false);
                }
			}
            
        });

        toolBar.add(syncLabel);
        toolBar.add(sync);


        root.add(toolBar, BorderLayout.NORTH);

    }

    private void initMainView(){

        mainPanel = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        root.add(mainPanel, BorderLayout.CENTER);
        initSchedule();
        initStaus();
    }
    private void initStaus(){
        JList<LogMessage> status_JList = new JList<>(controller.statusList);
        controller.ui.put("status_JList", status_JList);
        JScrollPane status_JScrollPane = new JScrollPane(status_JList);
        controller.ui.put("status_JScrollPane", status_JScrollPane);
        mainPanel.add(status_JScrollPane);
    }
    private void initSchedule(){
        JPanel schedulePanel = new JPanel();
        schedulePanel.setLayout(new BoxLayout(schedulePanel, BoxLayout.Y_AXIS));

        JList<SyncTask> schedule_JList = new JList<>(controller.scheduleList);
        schedule_JList.setCellRenderer(new CustomListRenderer());
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
        JButton schedule_Delete_JButton = new JButton("Delete");
        controller.ui.put("schedule_Delete_JButton", schedule_Delete_JButton);
        scheduleButtons.add(schedule_Add_JButton);
        scheduleButtons.add(schedule_Edit_JButton);
        scheduleButtons.add(schedule_Delete_JButton);

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
        speed_JLabel.setBorder(new EmptyBorder(0, 5, 5, 5));
        controller.ui.put("speed_JLabel", speed_JLabel);
        //speed_JLabel.setBorder(new EmptyBorder(0, 0, 5, 5));
        bottomPanel.add(speed_JLabel);

        JLabel files_JLabel = new JLabel("Files: ", SwingConstants.RIGHT);
        files_JLabel.setBorder(new EmptyBorder(0, 5, 5, 5));
        controller.ui.put("files_JLabel", files_JLabel);
        bottomPanel.add(files_JLabel);

        JLabel sync_JLabel = new JLabel((controller.info.isSyncing.getValue())?"Sync On":"Sync Off", SwingConstants.RIGHT);
        sync_JLabel.setBorder(new EmptyBorder(0, 5, 5, 5));
        controller.ui.put("sync_JLabel", sync_JLabel);
        bottomPanel.add(sync_JLabel);

    }
}

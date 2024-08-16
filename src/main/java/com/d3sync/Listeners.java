package com.d3sync;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.d3sync.LogMessage.TYPE;
import com.formdev.flatlaf.util.SwingUtils;

public class Listeners {
    Controller controller;


    Listeners(Controller controller){
        this.controller = controller;
        initViewListeners();
        initBottomBarListeners();
    }

    public void initViewListeners(){
        controller.statusList.addListDataListener(new ListDataListener() {


            JList<String> list = (JList<String>) controller.ui.get("status_JList");
            private void scrollToBottom() {
                int lastIndex = list.getModel().getSize() - 1;
                if (lastIndex >= 0) {
                    list.ensureIndexIsVisible(lastIndex);
                }
            }

            @Override
            public void intervalAdded(ListDataEvent e) {
                scrollToBottom();
            }

            @Override
            public void intervalRemoved(ListDataEvent e) {
                scrollToBottom();
            }

            @Override
            public void contentsChanged(ListDataEvent e) {
                scrollToBottom();
            }
        });
        JButton schedule_Add_JButton = (JButton) controller.ui.get("schedule_Add_JButton");
        schedule_Add_JButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
                Popups.showAddPopup(controller.viewController.root, controller);
			}
        });
        JButton schedule_Edit_JButton = (JButton) controller.ui.get("schedule_Edit_JButton");
        schedule_Edit_JButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
                JList<SyncTask> schedule_JList = (JList<SyncTask>) controller.ui.get("schedule_JList");
                int index = schedule_JList.getSelectedIndex();
                SyncTask a = schedule_JList.getSelectedValue();
                if(a != null){
                    Popups.showEditPopup(controller.viewController.root, controller,a, index );;
                }
			}
        });
        JButton schedule_Delete_JButton = (JButton) controller.ui.get("schedule_Delete_JButton");
        schedule_Delete_JButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
                JList<SyncTask> schedule_JList = (JList<SyncTask>) controller.ui.get("schedule_JList");
                int index = schedule_JList.getSelectedIndex();
                SyncTask a = schedule_JList.getSelectedValue();
                if(a != null){
                    int i = JOptionPane.showConfirmDialog(null,"Delete Task");
                    if(i == 0){
                        controller.deleteTask(index);
                    }
                }
			}
        });
        JList<SyncTask> schedule_JList = (JList<SyncTask>) controller.ui.get("schedule_JList");
        schedule_JList.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                SyncTask item = schedule_JList.getSelectedValue();
                if(item != null){
                    JLabel files_JLabel = (JLabel) controller.ui.get("files_JLabel");
                    new Thread(()->{
                        try {
                            long source = Utils.countFiles(item.source);
                            long target = Utils.countFiles(item.target);
                            SwingUtilities.invokeLater(()->{
                                files_JLabel.setText("Files: " + source + "/" + target);
                            });
                        } catch (IOException e1) {
                            // TODO Auto-generated catch block
                            e1.printStackTrace();
                        }
                    }).start();
                }
            }
        });
    }
    public void initBottomBarListeners(){
        controller.info.transferSpeed.addChangeListener(newValue->{
            JLabel speed = (JLabel) controller.ui.get("speed_JLabel");
            speed.setText("Transfer Speed: " + String.format("%.2f", newValue));
        });

        controller.info.isSyncing.addChangeListener(newValue->{
            JLabel sync_JLabel = (JLabel) controller.ui.get("sync_JLabel");
            sync_JLabel.setText((controller.info.isSyncing.getValue())?"Sync On":"Sync Off");
        });
    }
}

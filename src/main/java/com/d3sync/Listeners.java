package com.d3sync;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.time.LocalDateTime;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

import com.d3sync.Utils.PLATFORM;
import com.d3sync.Utils.ScheduledSync;
import com.d3sync.Utils.SyncConfig;

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

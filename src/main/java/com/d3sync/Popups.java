package com.d3sync;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

import com.d3sync.Utils.ScheduledSync;
import com.d3sync.Utils.SyncConfig;
import com.d3sync.Utils.TRANSFER;
import com.d3sync.Observables.Observable;
import com.github.lgooddatepicker.components.DatePicker;
import com.github.lgooddatepicker.components.DatePickerSettings;
import com.github.lgooddatepicker.components.DateTimePicker;
import com.github.lgooddatepicker.components.TimePicker;
import com.github.lgooddatepicker.components.TimePickerSettings;

public class Popups {
    private static File sourceDir;
    private static File targetDir;

    public static void showAddPopup(JFrame root, Controller controller){
        JDialog d = new JDialog(root, "Add Scheduled Sync");
        JPanel col = new JPanel();
        col.setLayout(new BoxLayout(col, BoxLayout.Y_AXIS));
        JPanel row1 = new JPanel();
        row1.setLayout(new BoxLayout(row1, BoxLayout.X_AXIS));
        JLabel name = new JLabel("Name: ");
        name.setBorder(new EmptyBorder(5, 5, 5, 0));
        JTextField nameField = new JTextField("Documents Backup",20);
        nameField.setBorder(new EmptyBorder(5, 0, 5, 5));
        nameField.setMaximumSize(nameField.getPreferredSize());
        nameField.setMinimumSize(nameField.getPreferredSize());
        row1.add(name);
        row1.add(nameField);
        col.add(row1);
        JPanel row2 = new JPanel();
        row2.setLayout(new BoxLayout(row2, BoxLayout.X_AXIS));
        row2.setBorder(new EmptyBorder(5, 0, 5, 0));
        JLabel source = new JLabel("Source: ");
        source.setBorder(new EmptyBorder(5, 5, 5, 0));
        JButton sourceButton = new JButton("Select");
        row2.add(source);
        row2.add(sourceButton);
        sourceButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
                JFileChooser chooser = new JFileChooser();
                chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                chooser.setAcceptAllFileFilterUsed(false);
                int result = chooser.showOpenDialog(null);
                if (result == JFileChooser.APPROVE_OPTION) {
                    sourceDir = chooser.getSelectedFile();
                    sourceButton.setText("/"+sourceDir.getName());
                } else {
                }
			}
        });
        JLabel target = new JLabel("Target: ");
        target.setBorder(new EmptyBorder(5, 5, 5, 0));
        JButton targetButton = new JButton("Select");
        row2.add(target);
        row2.add(targetButton);
        targetButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
                JFileChooser chooser = new JFileChooser();
                chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                chooser.setAcceptAllFileFilterUsed(false);
                int result = chooser.showOpenDialog(null);
                if (result == JFileChooser.APPROVE_OPTION) {
                    targetDir = chooser.getSelectedFile();
                    targetButton.setText("/"+targetDir.getName());
                } else {
                }
			}
        });
        col.add(row2);

        JPanel row3 = new JPanel();
        row3.setLayout(new BoxLayout(row3, BoxLayout.X_AXIS));
        row3.setBorder(new EmptyBorder(5, 0, 5, 0));
;       JLabel transferLabel = new JLabel("Transfer:");
        transferLabel.setBorder(new EmptyBorder(5, 5, 5, 0));
        String[] transferItems = {"Local", "Network"};
        JComboBox<String> transfer = new JComboBox<>(transferItems);
        transfer.setMaximumSize(transfer.getPreferredSize());
        transfer.setMinimumSize(transfer.getPreferredSize());
        row3.add(transferLabel);
        row3.add(transfer);
        col.add(row3);

        JPanel row4 = new JPanel();
        row4.setLayout(new BoxLayout(row4, BoxLayout.X_AXIS));
        row4.setBorder(new EmptyBorder(5, 0, 5, 0));
        JLabel startLabel = new JLabel("Start: ");
        startLabel.setBorder(new EmptyBorder(5, 5, 5, 0));
        TimePickerSettings timeSettings = new TimePickerSettings();
        timeSettings.setFormatForDisplayTime("hh:mm a");
        timeSettings.setFormatForMenuTimes("hh:mm a");
        TimePicker startTime = new TimePicker(timeSettings);
        startTime.setTimeToNow();
        startTime.setMaximumSize(startTime.getPreferredSize());
        startTime.setMinimumSize(startTime.getPreferredSize());
        row4.add(startLabel);
        row4.add(startTime);
        JLabel endLabel = new JLabel("End: ");
        endLabel.setBorder(new EmptyBorder(5, 5, 5, 0));
        TimePicker endTime = new TimePicker(timeSettings);
        endTime.setTimeToNow();
        endTime.setMaximumSize(endTime.getPreferredSize());
        endTime.setMinimumSize(endTime.getPreferredSize());
        row4.add(endLabel);
        row4.add(endTime);
        col.add(row4);

        JPanel row5 = new JPanel();
        row5.setLayout(new BoxLayout(row5, BoxLayout.X_AXIS));

        JButton add = new JButton("Add");
        add.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(sourceDir != null && targetDir != null){
                    controller.scheduleList.addElement(
                        new ScheduledSync(
                        nameField.getText(), 
                        new SyncConfig(sourceDir, sourceDir, (transfer.getSelectedItem().equals("Local")) ? TRANSFER.LOCAL: TRANSFER.NET), 
                        startTime.getTime(), 
                        endTime.getTime(), 
                        false)
                    );
                    d.setVisible(false);
                }else{
                    JOptionPane.showMessageDialog(root,
                    "Source and Target Directory Cant Be Empty",
                    "Invalid Selection",
                    JOptionPane.ERROR_MESSAGE);
                }
			}
        });
        JButton cancel = new JButton("Cancel");
        cancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				d.setVisible(false);
			}
        });

        row5.add(add);
        row5.add(cancel);
        col.add(row5);




        d.add(col);
        // setsize of dialog
        d.setSize(100, 100);
        // set visibility of dialog
        d.setVisible(true);
        d.pack();
    }

   
}

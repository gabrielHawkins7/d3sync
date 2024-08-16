package com.d3sync;

import java.awt.Color;
import java.awt.Component;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JLabel;
import javax.swing.JList;

import org.kordamp.ikonli.Ikon;
import org.kordamp.ikonli.feather.Feather;
import org.kordamp.ikonli.swing.FontIcon;


public class CustomListRenderer extends DefaultListCellRenderer {
    @Override
    public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        // Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        
        // SyncTask v = (SyncTask) value;

        
        // c.setForeground(Color.white);
        // if(v.active){
        //     c.setBackground(new Color(17, 140, 3));
        // }else{
        //     c.setBackground(new Color(214, 23, 17));
        // }

        // // Ensure selected item is highlighted
        // if (isSelected) {
        //     c.setBackground(c.getBackground().brighter());
        // }
        
        // return c;



        JLabel label = (JLabel) super.getListCellRendererComponent(
            list, value, index, isSelected, cellHasFocus);
        if (value instanceof SyncTask) {
            SyncTask task = (SyncTask) value;
            label.setText(task.toString()); // Set the text however you need
            // Choose icon based on some property, e.g., active status
            if (task.staus.isFine) {
                label.setIcon(FontIcon.of(Feather.CHECK_CIRCLE,15, new Color(17, 140, 3)));
            } else {
                label.setIcon(FontIcon.of(Feather.ALERT_CIRCLE, 15,  new Color(214, 23, 17)));            
            }
        }

        return label;
    
    }
}

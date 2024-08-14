package com.d3sync;

import java.util.HashMap;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;

import com.d3sync.Observables.Observable;
import com.d3sync.Observables.ObservableArrayList;
import com.d3sync.Utils.ScheduledSync;
import com.d3sync.Utils.SyncConfig;

public class Controller {

    HashMap<String, Object> ui = new HashMap<>();
    DefaultListModel<String> statusList = new DefaultListModel<>();
    DefaultListModel<ScheduledSync> scheduleList = new DefaultListModel<>();
    info info = new info();
    ViewController viewController = new ViewController(this);
    Listeners listeners = new Listeners(this);



    






    
}

class info{
    Observable<Double> transferSpeed = new Observable<Double>(0.0);

}

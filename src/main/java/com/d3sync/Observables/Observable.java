package com.d3sync.Observables;

import java.util.ArrayList;
import java.util.List;

public class Observable<T> {
    private T value;
    private List<ChangeListener<T>> listeners = new ArrayList<>();

    public Observable(T value) {
        this.value = value;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        if (!this.value.equals(value)) {
            this.value = value;
            notifyListeners();
        }
    }

    public void addChangeListener(ChangeListener<T> listener) {
        listeners.add(listener);
    }

    public void removeChangeListener(ChangeListener<T> listener) {
        listeners.remove(listener);
    }

    private void notifyListeners() {
        for (ChangeListener<T> listener : listeners) {
            listener.valueChanged(value);
        }
    }

    public interface ChangeListener<T> {
        void valueChanged(T newValue);
    }
}
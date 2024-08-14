package com.d3sync.Observables;

import java.util.ArrayList;
import java.util.List;

public class ObservableArray<T> {
    private T[] array;
    private List<ArrayChangeListener<T>> listeners = new ArrayList<>();

    public ObservableArray(T[] array) {
        this.array = array.clone();  // Clone the array to manage its state internally
    }

    public T get(int index) {
        return array[index];
    }

    public void set(int index, T value) {
        if (array[index] != value && (array[index] == null || !array[index].equals(value))) {
            array[index] = value;
            notifyListeners(index, value);
        }
    }

    public int size() {
        return array.length;
    }

    public void addArrayChangeListener(ArrayChangeListener<T> listener) {
        listeners.add(listener);
    }

    public void removeArrayChangeListener(ArrayChangeListener<T> listener) {
        listeners.remove(listener);
    }

    private void notifyListeners(int index, T newValue) {
        for (ArrayChangeListener<T> listener : listeners) {
            listener.elementUpdated(index, newValue);
        }
    }

    public interface ArrayChangeListener<T> {
        void elementUpdated(int index, T newValue);
    }
}

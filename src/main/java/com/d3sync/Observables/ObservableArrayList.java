package com.d3sync.Observables;

import java.util.ArrayList;
import java.util.List;

public class ObservableArrayList<T> {
    private final List<T> list;
    private final List<ListChangeListener<T>> listeners;

    public ObservableArrayList() {
        this.list = new ArrayList<>();
        this.listeners = new ArrayList<>();
    }

    public boolean add(T element) {
        boolean added = list.add(element);
        if (added) {
            notifyElementAdded(element);
        }
        return added;
    }

    public T remove(int index) {
        T removedElement = list.remove(index);
        if (removedElement != null) {
            notifyElementRemoved(index, removedElement);
        }
        return removedElement;
    }

    public T set(int index, T element) {
        T oldElement = list.set(index, element);
        notifyElementUpdated(index, element);
        return oldElement;
    }

    public void addListChangeListener(ListChangeListener<T> listener) {
        listeners.add(listener);
    }

    public void removeListChangeListener(ListChangeListener<T> listener) {
        listeners.remove(listener);
    }

    private void notifyElementAdded(T newElement) {
        for (ListChangeListener<T> listener : listeners) {
            listener.elementAdded(newElement);
        }
    }

    private void notifyElementRemoved(int index, T removedElement) {
        for (ListChangeListener<T> listener : listeners) {
            listener.elementRemoved(index, removedElement);
        }
    }

    private void notifyElementUpdated(int index, T newElement) {
        for (ListChangeListener<T> listener : listeners) {
            listener.elementUpdated(index, newElement);
        }
    }

    public interface ListChangeListener<T> {
        void elementAdded(T element);
        void elementRemoved(int index, T element);
        void elementUpdated(int index, T newElement);
    }
}

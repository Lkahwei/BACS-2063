package adt;

import java.io.Serializable;

/**
 *
 * @author Tan Kai Yuan
 */
public class ArrList<T> implements ListInterface<T>, Serializable {

    private int size;
    private T[] arr;
    private static final int DEF_CAPACITY = 5;

    public ArrList() {
        this(DEF_CAPACITY);
    }

    public ArrList(int initialCapacity) {
        size = 0;
        arr = (T[]) new Object[initialCapacity];
    }

    @Override
    public boolean add(T newEntry) {
        if (isArrFull()) {
            doubleArray();
        }
        arr[size] = newEntry;
        size++;
        return true;
    }

    private boolean isArrFull() {
        return size == arr.length;
    }

    private void doubleArray() {
        T[] oldArr = arr;
        arr = (T[]) new Object[oldArr.length * 2];
        for (int i = 0; i < size; i++) {
            arr[i] = oldArr[i];
        }
    }

    @Override
    public boolean add(int givenPosition, T newEntry) {
        boolean isAdd = true;
        if ((givenPosition >= 1) && (givenPosition <= size + 1)) {
            if (isArrFull()) {
                doubleArray();
            }
            makeRoom(givenPosition);
            arr[givenPosition - 1] = newEntry;
            size++;
        } else {
            isAdd = false;
        }
        return isAdd;
    }

    @Override
    public T remove(int givenPosition) {
        T outcome = null;
        if (!isEmpty()) {
            if ((givenPosition >= 1) && (givenPosition <= size)) {
                outcome = arr[givenPosition - 1];
                int index = givenPosition - 1;

                while (index < size - 1) {
                    arr[index] = arr[index + 1];
                    index++;
                }
                size--;
            }
        }

        return outcome;
    }

    @Override
    public boolean remove(T givenEntry) {
        boolean isRemove = false;

        if (contains(givenEntry)) {
            int oldIndex = 0;
            boolean found = false;
            while (oldIndex < size - 1 && !found) {
                if (arr[oldIndex].equals(givenEntry)) {
                    found = true;
                } else {
                    oldIndex++;
                }
            }
            for (int newIndex = oldIndex; newIndex < size - 1; newIndex++) {
                arr[newIndex] = arr[newIndex + 1];
            }
            size--;
            isRemove = true;
        }
        return isRemove;
    }

    @Override
    public boolean contains(T givenEntry) {
        boolean found = false;
        for (int index = 0; !found && (index < size); index++) {
            if (arr[index].equals(givenEntry)) {
                found = true;
            }
        }
        return found;
    }

    @Override
    public T getEntry(int givenPosition) {
        T outcome = null;
        if (!isEmpty()) {
            if ((givenPosition >= 1) && (givenPosition <= size)) {
                outcome = arr[givenPosition - 1];
            }
        }
        return outcome;
    }

    @Override
    public int getPosition(T givenEntry) {
        int position = 0;

        if (contains(givenEntry)) {
            boolean found = false;
            while (position < size && !found) {
                if (arr[position].equals(givenEntry)) {
                    found = true;
                } else {
                    position++;
                }
            }
            position++;
        }
        return position;
    }

    @Override
    public boolean replace(int givenPosition, T newEntry) {
        boolean isReplace = false;
        if (!isEmpty()) {
            if ((givenPosition >= 1) && (givenPosition <= size)) {
                arr[givenPosition - 1] = newEntry;
                isReplace = true;
            }
        }
        return isReplace;
    }

    @Override
    public int getSize() {
        return size;
    }

    @Override
    public void clear() {
        size = 0;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean isFull() {
        return false;
    }

    @Override
    public String toString() {
        String outputStr = "";
        for (int index = 0; index < size; ++index) {
            outputStr += arr[index] + "\n";
        }

        return outputStr;
    }

    private void makeRoom(int newPosition) {
        int newIndex = newPosition - 1;
        int lastIndex = size - 1;

        for (int index = lastIndex; index >= newIndex; index--) {
            arr[index + 1] = arr[index];
        }
    }
}

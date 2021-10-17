/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package adt;

import java.io.Serializable;

/**
 *
 * @author Chin Jun Wai
 */

public class NoDuplicateLinkedList<T> implements LinkedListInterface<T>, Serializable {

    private Node firstNode;
    private int size;

    @Override
    public boolean add(T element) {
        boolean added = false;
        Node newNode = new Node(element);
        if (isEmpty()) {
            firstNode = newNode;
            size++;
            added = true;
        } else if (contains(element) == false) {
            Node currentNode = firstNode;
            while (currentNode.next != null) {
                currentNode = currentNode.next;
            }
            currentNode.next = newNode;
            added = true;
            size++;
        }
        return added;
    }

    @Override
    public boolean remove(int givenIndex) {

        if (givenIndex >= 0 && givenIndex < size) {
            if (isEmpty()) {
                return false;
            }

            if (givenIndex == 0) {
                firstNode = firstNode.next;
            } else {
                Node currentNode = firstNode;
                Node nodeBefore = new Node();
                for (int i = 0; i <= givenIndex - 1; i++) {
                    nodeBefore = currentNode;
                    currentNode = currentNode.next;

                }
                nodeBefore.next = nodeBefore.next.next;
            }

        }
        size--;
        return true;
    }

    @Override
    public boolean update(int givenIndex, T newElement) {

        boolean updateResult = false;
        if (givenIndex >= 0 && givenIndex < size) {
            if (contains(newElement)) {
                Node currentNode = firstNode;
                for (int i = 0; i < givenIndex; i++) {
                    currentNode = currentNode.next;
                }
                currentNode.data = newElement;
                updateResult = true;
            } else {
                updateResult = false;
            }
        } else {
            updateResult = false;
        }
        return updateResult;
    }

    @Override
    public int getIndex(T givenElement) {
        int index = 0;
        Node currentNode = firstNode;
        if (isEmpty()) {
            index = -1;
        } else {
            if (contains(givenElement)) {
                for (int i = 0; i < size; i++) {
                    if (givenElement.equals(getElement(i))) {
                        index = i;
                    }
                }
            } else {
                index = -1;
            }
        }
        return index;
    }

    @Override
    public T getElement(int givenIndex) {
        Node currentNode = firstNode;
        if (isEmpty()) {
            return null;
        }
        if (givenIndex >= 0 && givenIndex < size) {
            if (givenIndex == 0) {
                return firstNode.data;
            } else {
                for (int i = 0; i < givenIndex; i++) {
                    currentNode = currentNode.next;

                }
            }
        } else {
            return null;
        }
        return currentNode.data;
    }

    @Override
    public int getSize() {
        return size;
    }

    @Override
    public void clear() {
        firstNode = null;
        size = 0;
    }

    @Override
    public boolean contains(T element) {
        boolean found = false;
        Node currentNode = firstNode;
        while (!found && currentNode.next != null) {

            if (element.equals(currentNode.data)) {
                found = true;
            }
            currentNode = currentNode.next;
        }
        if (element.equals(currentNode.data)) {
            found = true; // compare with last element
        }
        return found;
    }

    @Override
    public T getLast() {
        Node currentNode = firstNode;
        if (isEmpty()) {
            return null;
        } else if (currentNode.next == null) {
            return currentNode.data;
        } else {
            for (int i = 0; i < size - 1; i++) {
                currentNode = currentNode.next;
            }
        }
        return currentNode.data;
    }

    @Override
    public String toString() {
        Node currentNode = firstNode;
        String data = "";

        while (currentNode != null) {
            data += currentNode.data + "\n";
            currentNode = currentNode.next;
        }

        return data;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean isFull() {
        return false;
    }

    private class Node implements Serializable {

        private Node next;
        private T data;

        Node() {

        }

        Node(T element) {
            this.data = element;
            this.next = null;
        }

        Node(T element, Node nextElement) {
            this.data = element;
            this.next = nextElement;
        }
    }
}

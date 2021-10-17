/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package adt;

import java.io.Serializable;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 *
 * @author Lee Kah Wei
 */
public class MiddleOperationEnabledLinkedStack<T> implements StackWithIteratorInterface<T>, Serializable{
    final static long serialVersionUID = 1983843151634546463L;
    private Node topNode;
    private int size;
    
    public MiddleOperationEnabledLinkedStack() {
        topNode = null;
        size = 0;
    }

    @Override
    public void push(T newEntry) {
        Node newNode = new Node(newEntry);
        newNode.next = topNode;
        topNode = newNode;
        size++;
    }
    
    @Override
    public T pop() {
        T topEntry = null;
        if(!isEmpty()) {
            topEntry = topNode.data;
            topNode = topNode.next;
        }
        size--;
        return topEntry; 
    }

    @Override
    public T peek() {
        T topEntry = null;
        if(!isEmpty()) {
            topEntry = topNode.data;
        }
        return topEntry;
    }

    @Override
    public boolean isEmpty() {
        return topNode == null;
    }
    
    @Override
    public boolean contains(T element){
        boolean found = false;
        Node currentNode = topNode;
        
        while(!found && currentNode != null) {
            if(element.equals(currentNode.data)){
                found = true;
            }
            currentNode = currentNode.next;
        }
        return found;
    }
    
    public T getElement(int givenIndex){
        Node currentNode = topNode;
        
        if(isEmpty()){
            return null;
        }
        else{
            if(givenIndex >= 0 && givenIndex <= size) {
                if(givenIndex == 0){
                    return topNode.data;
                }
                else{
                    for(int i = 0; i < givenIndex; i++) {
                        currentNode = currentNode.next;
                    }
                }
                
            }
            else{
                return null;
            }
        }
        return currentNode.data;
    }
    
    @Override
    public int indexOf(T element){
        int index  = 0;
        int currentPosition = 0;
        Node currentNode = topNode;
        if(isEmpty()){
            index = -1;
        }
        else{
            if(contains(element)){
                for(int i = 0; i < size; i++){
                    if(element.equals(getElement(i))) {
                        index = i;
                    }
                }
            }
            else{
                index = -1;
            }
        }
        return index;
    }
    @Override
    public T removeItemBasedID(T element){
        int index = -1; 
        index = indexOf(element);
        
        Node previousNode = topNode;
        Node middleNode = previousNode.next;
        Node lastNode = middleNode.next;
        
        if(isEmpty()){
            return null;
        }
        
        else{
            if(index >= 0 && index <= size){ 
              if (index == 0) {
                  return pop();
              }
              for(int i = 0; i < index - 1; i++) {
                        previousNode = previousNode.next;
                        middleNode = previousNode.next;
                        lastNode = middleNode.next;
                }        
            }
            else{
                return null;
            }
        }
        middleNode.next = topNode;
        previousNode.next = lastNode;
        topNode = middleNode;
        return pop();
    }
    
    @Override
    public Iterator<T> getIterator() {
        return new LinkedStackIterator();
    }
    
    private class LinkedStackIterator implements Iterator<T> {
        private Node currentNode;
        
        public LinkedStackIterator() {
            currentNode = topNode;
        }

        @Override
        public boolean hasNext() {
            return currentNode != null;
        }

        @Override
        public T next() {
            if (hasNext()) {
                T returnData = currentNode.data;
                currentNode = currentNode.next;
                return returnData;
            } else {
                throw new NoSuchElementException("Illegal call to next(); iterator is after end of list.");
            }
        }
    }

    
    @Override
    public void clear() {
        topNode = null;
        size = 0;
    }
    
    @Override
    public int returnSize() {
        return size;
    }
    
    @Override
    public String toString() {
        String str = "";
        Node currentNode = topNode;
        while (currentNode != null){
            str += currentNode.data + " ";
            currentNode = currentNode.next;
        }
        return str;
    }
    
    private class Node implements Serializable{
        private T data;
        private Node next;
        
        public Node(T data){
            this.data = data;
        }
        
        public Node(T data, Node next) {
            this.data = data;
            this.next = next;
        }
    }
}

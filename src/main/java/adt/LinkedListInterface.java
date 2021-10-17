/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package adt;

/**
 *
 * @author Chin Jun Wai
 */

public interface LinkedListInterface<T> {

    public boolean add(T element);
    
    public boolean remove(int givenPosition);

    public boolean update(int givenPosition, T newElement);

    public int getIndex(T givenElement);

    public T getElement(int giventPosition);

    public int getSize();

    public void clear();

    public boolean contains(T element);
    
    public T getLast();

    public boolean isEmpty();
    
    public boolean isFull();

}





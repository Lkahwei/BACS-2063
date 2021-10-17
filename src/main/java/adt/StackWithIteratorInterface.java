/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package adt;

import java.util.Iterator;
/**
 *
 * @author Lee Kah Wei
 */
public interface StackWithIteratorInterface<T> extends LinkedStackInterface<T> {
    
    public Iterator<T> getIterator();
}

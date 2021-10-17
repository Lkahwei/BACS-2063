package adt;

/**
 *
 * @author Tan Kai Yuan
 */
public interface ListInterface<T> {

    public boolean add(T newEntry);

    //add the new entry at the given position in the list
    public boolean add(int givenPosition, T newEntry);

    public T remove(int givenPosition);

    public boolean remove(T givenEntry);

    public boolean contains(T givenEntry);

    public T getEntry(int givenPosition);
    
    //get the position of given entry in the list
    public int getPosition(T givenEntry);

    public boolean replace(int givenPosition, T newEntry);

    public int getSize();

    public void clear();

    public boolean isEmpty();

    public boolean isFull();

}

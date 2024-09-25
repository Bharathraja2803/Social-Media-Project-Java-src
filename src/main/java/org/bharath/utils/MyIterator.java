/**
 * This is the custom implementation of moving forward and backward in the list iterator
 */
package org.bharath.utils;

import org.bharath.Main;

import java.util.ListIterator;

public class MyIterator<T> {
    private final ListIterator<T> listIterator;

    private boolean nextWasCalled = false;
    private boolean previousWasCalled = false;

    public MyIterator(ListIterator<T> listIterator) {
        this.listIterator = listIterator;
    }

    /**
     * This method checks the previous action and move the pointer to next value
     * @return
     */
    public T next() {
        nextWasCalled = true;
        if (previousWasCalled) {
            previousWasCalled = false;
            listIterator.next ();
        }
        return listIterator.next ();
    }

    /**
     * This method checks the previous action and move the pointer to the previous value
     * @return
     */
    public T previous() {
        if (nextWasCalled) {
            listIterator.previous();
            nextWasCalled = false;
        }
        previousWasCalled = true;
        return listIterator.previous();
    }

    /**
     * This method checks the previous action and move the pointer to the correct position and remove the record
     * @return
     */
    public boolean remove(){
        try{
            if(previousWasCalled){
                listIterator.next();
                listIterator.remove();
                if(listIterator.hasPrevious()){
                    listIterator.previous();
                }else if(listIterator.hasNext()){
                    listIterator.next();
                }else{
                    return true;
                }
            }else{
                listIterator.previous();
                listIterator.remove();
                if(listIterator.hasNext()){
                    listIterator.next();
                }else if(listIterator.hasPrevious()){
                    listIterator.previous();
                }else{
                    return true;
                }
            }

            Main.LOGGER.info("Removed the post from iterator!");
            return true;
        }catch(RuntimeException e){
            Main.LOGGER.severe(e.toString());
            return false;
        }
    }

}

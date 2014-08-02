import java.util.Iterator;
import java.util.Spliterator;
import java.util.function.Consumer;

import edu.neumont.util.*;

public class ArrayList<T> implements List<T> {
	
	int currentIndex = 0;
	T[] arr = (T[])new Object[10];
	
	public boolean add(T t) 
	{
		
		if (currentIndex == arr.length)
		{
			T[] nArr = (T[]) new Object[arr.length * 2];
		}
		
		arr[currentIndex] = t;
		currentIndex++;
		return true;
	}
	
	public T get(int index) throws IndexOutOfBoundsException
	{
		if (index >= currentIndex)
		{
			throw new IndexOutOfBoundsException();
		}
		else
		{
			return arr[index];
		}
	}
	
	public boolean remove(T t) 
	{
		//Has to find the object t and then remove it, shifting all the old ones backward.
		//First, to find it.
		int indexOfT = find(t);
		if (indexOfT == -1)
		{
			return false;
		}
		else
		{
			for(int i = indexOfT; i < currentIndex; i ++)
			{
				arr[i] = arr[i + 1];
			}
			currentIndex--;
			return true;
		}
	}
	
	public int find(T t)
	{
		int index = -1;
		for(int i = 0 ; index == -1 && i < currentIndex; i ++)
		{
			if (get(i) == t)
			{
				index = i;
			}
		}
		return index;
	}

	@Override
	public Iterator<T> iterator() 
	{
		Iterator<T> it = new Iterator<T>() {

			int cur = 0;
            @Override
            public boolean hasNext() {
                return (cur < currentIndex);
            }

            @Override
            public T next() {
            	T toReturn = arr[cur];
                cur++;
                return toReturn;
            }

            @Override
            public void remove() {
                //Why would you need a remove in an Iterator...
            }
        };
        return it;
	}

	@Override
	public void forEach(Consumer<? super T> action) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Spliterator<T> spliterator() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int size() {
		return currentIndex;
	}
}

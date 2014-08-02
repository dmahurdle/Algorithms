
import java.util.Iterator;
import java.util.function.Consumer;

import edu.neumont.util.*;

public class LinkedList<T> implements Queue<T> {
	static class Node<T>{
		T val;
		Node<T> next;
		
		public Node(T val, Node<T> next)
		{
			this.val = val;
			this.next = next;
		}
	}
	
	Node<T> head = null;
	Node<T> tail = null;
	int size = 0;
	
	public Iterator<T> iterator() {
		Iterator<T> it = new Iterator<T>() {

			Node<T> currentNode = head;
            @Override
            public boolean hasNext() {
                return (currentNode != null);
            }

            @Override
            public T next() {
            	T toReturn = currentNode.val;
                currentNode = currentNode.next;
                return toReturn;
            }
        };
        return it;
	}
	
	
	
	public T poll() {
		if (head == null)
		{
			return null;
		}
		else
		{
			Node<T> toReturn = head;
			head = head.next;
			size --;
			return toReturn.val;
		}
	}
	
	public boolean offer(T t) {
		if (size == 0)
		{
			head = new Node<T>(t, null);
			tail = head;
		}
		else
		{
			tail.next = new Node<T>(t, null);
			tail = tail.next;
		}
		size ++;
		return true;
	}
	
	public T peek() {
		return head != null? head.val : null;
	}
}

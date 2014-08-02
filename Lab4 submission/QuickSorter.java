import java.util.List;

import edu.neumont.csc250.lab4.Sorter;


public class QuickSorter<T extends Comparable<T>> implements Sorter<T>{

	@Override
	public void sort(List<T> toBeSorted) {
		// TODO Auto-generated method stub
		quicksort(toBeSorted, 0, toBeSorted.size() - 1);
	}
	
	private void quicksort(List<T> l, int low, int high)
	{
		if (low < high)
		{
			int p = partition(l, low, high);
			quicksort(l, low, p - 1);
			quicksort(l, p + 1, high);
		}
	}
	
	private int partition(List<T> l, int low, int high)
	{
		int pivotI = choosePivot(l, low, high);
		T pivotV = l.get(pivotI);
		swap(l, pivotI, high);
		int storeIndex = low;
		for(int i = low; i < high; i ++)
		{
			if (l.get(i).compareTo(pivotV) <= 0)
			{
				swap(l, i, storeIndex);
				storeIndex ++;
			}
		}
		swap(l, storeIndex, high);
		return storeIndex;
	}
	
	private void swap(List<T> l, int a, int b)
	{
		if (a != b)
		{
			T temp = l.get(a);
			l.set(a, l.get(b));
			l.set(b, temp);
		}
	}
	
	private int choosePivot(List<T> l, int low, int high)
	{
		return low + ((high - low)/2);
	}

}

import java.util.List;

import edu.neumont.csc250.lab4.Sorter;


public class MergeSorter<T extends Comparable<T>> implements Sorter<T>{

	@Override
	public void sort(List<T> toBeSorted) {
		// TODO Auto-generated method stub
		T[] arr = (T[])new Comparable[toBeSorted.size()];
		mergeSort(toBeSorted, arr, 0, arr.length - 1, true);
	}
	
	public void sortDesc(List<T> toBeSorted) {
		T[] arr = (T[])new Comparable[toBeSorted.size()];
		mergeSort(toBeSorted, arr, 0, arr.length - 1, false);
	}
	
	private void mergeSort(List<T> l, T[] arr, int low, int high, boolean ascending)
	{
		if (low < high)
		{
			int mid = (low + high) / 2;
			mergeSort(l, arr, low, mid, ascending);
			mergeSort(l, arr, mid + 1, high, ascending);
			merge(arr, l, low, mid, high, ascending);
		}
	}
	
	private void merge(T[] arr, List<T> l, int low, int mid, int high, boolean ascending)
	{
		
		for(int i = low; i <= high; i ++)
		{
			arr[i] = l.get(i);
		}
		
		int lWalk = low;
		int hWalk = mid + 1;
		
		int arrPos = low;
		
		while (lWalk <= mid && hWalk <= high)
		{
			if ((ascending && arr[lWalk].compareTo(arr[hWalk]) <= 0) || (!ascending && arr[lWalk].compareTo(arr[hWalk]) >= 0))
			{
				l.set(arrPos, arr[lWalk]);
				lWalk ++;
			}
			else
			{
				l.set(arrPos, arr[hWalk]);
				hWalk ++;
			}
			arrPos ++;
		}
		
		while (lWalk <= mid)
		{
			l.set(arrPos, arr[lWalk]);
			arrPos ++;
			lWalk ++;
		}
	}
}

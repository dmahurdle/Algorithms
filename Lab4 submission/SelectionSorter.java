import java.util.List;

import edu.neumont.csc250.lab4.Sorter;


public class SelectionSorter<T extends Comparable<T>> implements Sorter<T>{

	@Override
	public void sort(List<T> toBeSorted) {
		for(int i = 0; i < toBeSorted.size() - 1; i ++)
		{
			int minIndex = i;
			for (int j = i + 1; j < toBeSorted.size(); j ++)
			{
				if (toBeSorted.get(j).compareTo( toBeSorted.get(minIndex)) < 0)
				{
					minIndex = j;
				}
			}
			if (minIndex != i)
			{
				T temp = toBeSorted.get(i);
				toBeSorted.set(i, toBeSorted.get(minIndex));
				toBeSorted.set(minIndex, temp);
			}
		}
	}
}

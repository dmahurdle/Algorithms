import java.util.ArrayList;
import java.util.Iterator;


public class PrimeIterator implements Iterator<Integer>{
	boolean[] primeBools;
	int primeWalkerIndex;
	int nextPrime = 2;
	
	public PrimeIterator(int max) { 
		primeBools = new boolean[max];
		primeWalkerIndex = 0;
	}
	public void markOutAllMultiplesOf(int prime)
	{
		int index = (prime * prime) - 2;
		while (index < primeBools.length && index > 0)
		{
			primeBools[index] = true;
			index += prime;
		}
	}
	
	public boolean hasNext() { 
		return nextPrime != -1;
	}

	public Integer next() {
		boolean foundAPrime = false;
		int oldNext = nextPrime;
		markOutAllMultiplesOf(oldNext);
		while (!foundAPrime && primeWalkerIndex < primeBools.length - 1 && primeWalkerIndex >= 0)
		{
			primeWalkerIndex ++;
			
			if (!primeBools[primeWalkerIndex])
			{
				foundAPrime = true;
				int primeRepresentedByThisIndex = primeWalkerIndex + 2;
				nextPrime = primeRepresentedByThisIndex;
			}
		}
		if (!foundAPrime)
		{
			nextPrime = -1;
		}

		return oldNext;
	}

	@Override
	public void remove() {
		// TODO Auto-generated method stub
		
	}
}

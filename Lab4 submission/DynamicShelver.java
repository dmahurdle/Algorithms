import java.util.List;

import edu.neumont.csc250.lab4.Book;
import edu.neumont.csc250.lab4.Bookcase;
import edu.neumont.csc250.lab4.Bookshelf;
import edu.neumont.csc250.lab4.Shelver;
import edu.neumont.csc250.lab4.Sorter;


public class DynamicShelver implements Shelver{

	@Override
	public void shelveBooks(Bookcase bookcase, List<Book> books)
	{
		MergeSorter ms = new MergeSorter();
		ms.sortDesc(books);
		
		Book l[] = new Book[books.size()];
		books.toArray(l);
		
		//printBooks(books);
		
		int n = books.size();
		int m = bookcase.getShelfWidth();
		
		int extras[][] = new int[n + 1][n + 1];
		int lc[][] = new int[n + 1][n + 1];
		int c[] = new int[n+1];
		int p[]= new int[n+1];
		int i, j;
		
		for (i = 1; i <= n; i ++)
		{
			extras[i][i] = m - l[i-1].getWidth();
			for(j = i + 1; j <= n; j ++)
			{
				extras[i][j] = extras[i][j - 1] - l[j - 1].getWidth();
			}
		}
		
		for(i = 1; i <= n; i ++)
		{
			for(j = i; j <= n; j ++)
			{
				if (extras[i][j]< 0)
				{
					lc[i][j] = Integer.MAX_VALUE;
				}
				else
				{
					lc[i][j] = extras[i][j] * extras[i][j];
				}
			}
		}
		
		c[0] = 0;
		for(j = 1; j <= n; j ++)
		{
			c[j] = Integer.MAX_VALUE;
			for(i = 1; i <= j; i ++)
			{
				if (c[i-1] != Integer.MAX_VALUE && lc[i][j] != Integer.MAX_VALUE && (c[i-1] + lc[i][j] < c[j]))
				{
					c[j] = c[i-1] + lc[i][j];
					p[j] = i;
				}
			}
		}
		
		//Now the solution is stored in p...
		for(int k = 0; k < p.length; k ++)
		{
			//System.out.println(k + " : " + p[k]);
		}
		//printSolution(p, n);
		addBooks(bookcase, books, p, n);
		
		printCase(bookcase);
	}
	
	public void printBooks(List<Book> b)
	{
		for(int i =0 ;i < b.size(); i ++)
		{
			System.out.println(i + " : " + b.get(i).getWidth());
		}
	}
	
	public int printSolution (int p[], int n)
	{
	    int k;
	    if (p[n] == 1)
	        k = 1;
	    else
	        k = printSolution (p, p[n]-1) + 1;
	    System.out.println ("Shelf " + k + ": From book " + p[n] +" to "+n+" \n");
	    return k;
	}
	
	public int addBooks (Bookcase bcase, List<Book> books, int p[], int n)
	{
	    int k;
	    if (p[n] == 1)
	        k = 1;
	    else
	        k = addBooks (bcase, books, p, p[n]-1) + 1;
	    //System.out.println ("Shelf " + k + ": From book " + p[n] +" to "+n+" \n");
	    for(int i = p[n]; i <= n; i ++)
	    {
	    	bcase.getBookshelf(k - 1).addBook(books.get(i - 1));
	    }
	    
	    return k;
	}
	
	public void oldShelveBooks(Bookcase bookcase, List<Book> books) {
		MergeSorter ms = new MergeSorter();
		ms.sort(books);
		int shelf = 0;
		float idealShelfRemaining = 
				getIdealRemainingSpace(
						bookcase.getShelfWidth(), 
						bookcase.getNumberOfShelves(), 
						books, 
						books.size() - 1);
		
		for(int i = books.size() - 1; i >= 0; i --)
		{
			int remainingSpace = bookcase.getBookshelf(shelf).getSpaceLeft();
			float difBefore = Math.abs(idealShelfRemaining - remainingSpace);
			float difAfter = Math.abs(idealShelfRemaining - (remainingSpace - books.get(i).getWidth()));
			if (difBefore < difAfter)
			{
				//move to next shelf, recalculate ideal, and place this book on it.
				shelf ++;
				idealShelfRemaining = 
						getIdealRemainingSpace(
								bookcase.getShelfWidth(), 
								bookcase.getNumberOfShelves() - shelf, 
								books, 
								i);
				bookcase.getBookshelf(shelf).addBook(books.get(i));
			}
			else
			{
				//add the book to this shelf.
				bookcase.getBookshelf(shelf).addBook(books.get(i));
			}
		}
		printCase(bookcase);
	}
	
	private float getIdealRemainingSpace(int shelfWidth, int numberOfShelves, List<Book> books, int startingIndex)
	{
		float  totalBookWidth = 0;
		for(int i = startingIndex; i >= 0; i --)
		{
			totalBookWidth += books.get(i).getWidth();
		}
		return shelfWidth - (totalBookWidth / numberOfShelves);
	}

	public void printCase(Bookcase bCase)
	{
		//Print all shelves as lines, books as | with spacing.
		for(int i = 0; i < bCase.getNumberOfShelves(); i ++)
		{
			System.out.println(bCase.getBookshelf(i).getSpaceLeft() + " spaces left. Book count: " + bCase.getBookshelf(i).getBookCount());
		}
	}
	
}

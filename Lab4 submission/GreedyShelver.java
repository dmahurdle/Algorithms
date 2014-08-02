import java.util.List;

import edu.neumont.csc250.lab4.Book;
import edu.neumont.csc250.lab4.Bookcase;
import edu.neumont.csc250.lab4.Shelver;
import edu.neumont.csc250.lab4.Sorter;


public class GreedyShelver implements Shelver{

	@Override
	public void shelveBooks(Bookcase bookcase, List<Book> books) {
		
		MergeSorter<Book> mergeSorter = new MergeSorter<Book>();
		//Sort by height (shortest to tallest)
		mergeSorter.sortDesc(books);
		
		//Add books to current shelf. If shelf can't fit book, go to next shelf.
		
		int shelf = 0;
		for(int i = 0; i < books.size(); i ++)
		{
			if (bookcase.getBookshelf(shelf).getSpaceLeft() < books.get(i).getWidth())
			{
				shelf ++;
			}
			bookcase.getBookshelf(shelf).addBook(books.get(i));
		}
		printCase(bookcase);
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

import java.util.Comparator;
import java.util.function.Function;
import java.util.function.ToDoubleFunction;
import java.util.function.ToIntFunction;
import java.util.function.ToLongFunction;


public class WordCompare implements Comparator<PossibleMessage>{

	public int compare(PossibleMessage o1, PossibleMessage o2) {
		// TODO Auto-generated method stub
		return (o1.score < o2.score? 1 : -1);
	}

	public Comparator<PossibleMessage> reversed() {
		// TODO Auto-generated method stub
		return null;
	}

	public Comparator<PossibleMessage> thenComparing(
			Comparator<? super PossibleMessage> other) {
		// TODO Auto-generated method stub
		return null;
	}

	public <U> Comparator<PossibleMessage> thenComparing(
			Function<? super PossibleMessage, ? extends U> keyExtractor,
			Comparator<? super U> keyComparator) {
		// TODO Auto-generated method stub
		return null;
	}

	public <U extends Comparable<? super U>> Comparator<PossibleMessage> thenComparing(
			Function<? super PossibleMessage, ? extends U> keyExtractor) {
		// TODO Auto-generated method stub
		return null;
	}

	public Comparator<PossibleMessage> thenComparingInt(
			ToIntFunction<? super PossibleMessage> keyExtractor) {
		// TODO Auto-generated method stub
		return null;
	}

	public Comparator<PossibleMessage> thenComparingLong(
			ToLongFunction<? super PossibleMessage> keyExtractor) {
		// TODO Auto-generated method stub
		return null;
	}

	public Comparator<PossibleMessage> thenComparingDouble(
			ToDoubleFunction<? super PossibleMessage> keyExtractor) {
		// TODO Auto-generated method stub
		return null;
	}

	public static <T extends Comparable<? super T>> Comparator<T> reverseOrder() {
		// TODO Auto-generated method stub
		return null;
	}

	public static <T extends Comparable<? super T>> Comparator<T> naturalOrder() {
		// TODO Auto-generated method stub
		return null;
	}

	public static <T> Comparator<T> nullsFirst(Comparator<? super T> comparator) {
		// TODO Auto-generated method stub
		return null;
	}

	public static <T> Comparator<T> nullsLast(Comparator<? super T> comparator) {
		// TODO Auto-generated method stub
		return null;
	}

	public static <T, U> Comparator<T> comparing(
			Function<? super T, ? extends U> keyExtractor,
			Comparator<? super U> keyComparator) {
		// TODO Auto-generated method stub
		return null;
	}

	public static <T, U extends Comparable<? super U>> Comparator<T> comparing(
			Function<? super T, ? extends U> keyExtractor) {
		// TODO Auto-generated method stub
		return null;
	}

	public static <T> Comparator<T> comparingInt(
			ToIntFunction<? super T> keyExtractor) {
		// TODO Auto-generated method stub
		return null;
	}

	public static <T> Comparator<T> comparingLong(
			ToLongFunction<? super T> keyExtractor) {
		// TODO Auto-generated method stub
		return null;
	}

	public static <T> Comparator<T> comparingDouble(
			ToDoubleFunction<? super T> keyExtractor) {
		// TODO Auto-generated method stub
		return null;
	}


}

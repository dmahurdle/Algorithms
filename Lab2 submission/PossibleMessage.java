
public class PossibleMessage {

	PossibleWord[] words;
	
	double score;
	
	public PossibleMessage(PossibleWord[] words)
	{
		this.words = words;
	}
	
	public void calculateScore()
	{
		double total = 1.0f;
		for(PossibleWord pw : words)
		{
			if (pw.score > 0)
			{
				total *= (((double)pw.score) / 10000);
			}
			//total /= 50;
		}
		score = total;
	}
}
